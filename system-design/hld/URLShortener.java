import java.util.*;
import java.util.concurrent.*;

/**
 * System Design — URL Shortener (High-Level Design)
 * Implements a distributed URL shortening service similar to bit.ly
 * Features: Base62 encoding, distributed ID generation, caching, rate limiting
 */
public class URLShortener {

    // Base62 character set for encoding
    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = BASE62.length();

    // Distributed unique ID generator (Snowflake-like)
    static class DistributedIDGenerator {
        private final long machineId; private long sequence = 0; private long lastTimestamp = -1L;
        public DistributedIDGenerator(long machineId) { this.machineId = machineId; }
        
        public synchronized long nextId() {
            long timestamp = System.currentTimeMillis();
            if (timestamp < lastTimestamp) throw new RuntimeException("Clock moved backwards");
            if (timestamp == lastTimestamp) { sequence = (sequence + 1) & 0xFFF; if (sequence == 0) timestamp = tilNextMillis(lastTimestamp); }
            else { sequence = 0L; }
            lastTimestamp = timestamp;
            return ((timestamp - 1288834974657L) << 22) | (machineId << 12) | sequence;
        }
        private long tilNextMillis(long lastTimestamp) {
            long timestamp = System.currentTimeMillis();
            while (timestamp <= lastTimestamp) timestamp = System.currentTimeMillis();
            return timestamp;
        }
    }

    // URL Storage with TTL support
    static class URLStore {
        private final ConcurrentHashMap<String, URLRecord> shortToLong = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<String, String> longToShort = new ConcurrentHashMap<>();
        private final DelayQueue<ExpiredURL> expirationQueue = new DelayQueue<>();
        
        public void store(String shortKey, String longUrl, long ttlMillis) {
            URLRecord record = new URLRecord(longUrl, System.currentTimeMillis() + ttlMillis);
            shortToLong.put(shortKey, record);
            longToShort.put(longUrl, shortKey);
            if (ttlMillis > 0) expirationQueue.put(new ExpiredURL(shortKey, ttlMillis));
        }
        
        public String getLongUrl(String shortKey) {
            URLRecord record = shortToLong.get(shortKey);
            return (record != null && !record.isExpired()) ? record.getLongUrl() : null;
        }
        
        public String getShortKey(String longUrl) { return longToShort.get(longUrl); }
        
        public void cleanupExpired() {
            ExpiredURL expired;
            while ((expired = expirationQueue.poll()) != null) {
                URLRecord record = shortToLong.remove(expired.shortKey);
                if (record != null) longToShort.remove(record.getLongUrl());
            }
        }
    }
    
    static class URLRecord {
        private final String longUrl; private final long expiryTime;
        public URLRecord(String longUrl, long expiryTime) { this.longUrl = longUrl; this.expiryTime = expiryTime; }
        public String getLongUrl() { return longUrl; }
        public boolean isExpired() { return System.currentTimeMillis() > expiryTime; }
    }
    
    static class ExpiredURL implements Delayed {
        private final String shortKey; private final long expiryTime;
        public ExpiredURL(String shortKey, long delayMillis) { this.shortKey = shortKey; this.expiryTime = System.currentTimeMillis() + delayMillis; }
        @Override public long getDelay(TimeUnit unit) { return unit.convert(expiryTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS); }
        @Override public int compareTo(Delayed other) { return Long.compare(this.expiryTime, ((ExpiredURL) other).expiryTime); }
    }

    // Rate Limiter using Token Bucket algorithm
    static class RateLimiter {
        private final ConcurrentHashMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();
        private final long refillRateMillis; private final int capacity;
        
        public RateLimiter(int capacity, long refillRateMillis) { this.capacity = capacity; this.refillRateMillis = refillRateMillis; }
        
        public boolean allowRequest(String apiKey) {
            TokenBucket bucket = buckets.computeIfAbsent(apiKey, k -> new TokenBucket(capacity, refillRateMillis));
            return bucket.tryConsume();
        }
    }
    
    static class TokenBucket {
        private int tokens; private final long refillRateMillis; private long lastRefillTime;
        public TokenBucket(int capacity, long refillRateMillis) { this.tokens = capacity; this.refillRateMillis = refillRateMillis; this.lastRefillTime = System.currentTimeMillis(); }
        
        public synchronized boolean tryConsume() {
            refill();
            if (tokens > 0) { tokens--; return true; }
            return false;
        }
        
        private void refill() {
            long now = System.currentTimeMillis();
            long elapsed = now - lastRefillTime;
            if (elapsed >= refillRateMillis) { tokens = Math.min(tokens + (int)(elapsed / refillRateMillis), 10); lastRefillTime = now; }
        }
    }

    // Cache layer using LRU eviction
    static class URLCache {
        private final int capacity; private final LinkedHashMap<String, String> cache;
        
        public URLCache(int capacity) {
            this.capacity = capacity;
            this.cache = new LinkedHashMap<String, String>(capacity, 0.75f, true) {
                @Override protected boolean removeEldestEntry(Map.Entry<String, String> eldest) { return size() > capacity; }
            };
        }
        
        public void put(String shortKey, String longUrl) { cache.put(shortKey, longUrl); }
        public String get(String shortKey) { return cache.get(shortKey); }
    }

    // Main URL Shortener service
    static class URLShortenerService {
        private final DistributedIDGenerator idGenerator;
        private final URLStore urlStore;
        private final RateLimiter rateLimiter;
        private final URLCache cache;
        
        public URLShortenerService(long machineId) {
            this.idGenerator = new DistributedIDGenerator(machineId);
            this.urlStore = new URLStore();
            this.rateLimiter = new RateLimiter(10, 1000); // 10 requests per second
            this.cache = new URLCache(10000);
            startCleanupTask();
        }
        
        public String shortenURL(String longUrl, String apiKey) {
            if (!rateLimiter.allowRequest(apiKey)) throw new RuntimeException("Rate limit exceeded");
            
            String existingShortKey = urlStore.getShortKey(longUrl);
            if (existingShortKey != null) return "http://short.url/" + existingShortKey;
            
            long id = idGenerator.nextId();
            String shortKey = encodeBase62(id);
            urlStore.store(shortKey, longUrl, 7 * 24 * 60 * 60 * 1000L); // 7 days TTL
            cache.put(shortKey, longUrl);
            return "http://short.url/" + shortKey;
        }
        
        public String expandURL(String shortKey) {
            String cached = cache.get(shortKey);
            if (cached != null) return cached;
            
            String longUrl = urlStore.getLongUrl(shortKey);
            if (longUrl != null) cache.put(shortKey, longUrl);
            return longUrl;
        }
        
        private String encodeBase62(long num) {
            StringBuilder sb = new StringBuilder();
            while (num > 0) { sb.append(BASE62.charAt((int)(num % BASE))); num /= BASE; }
            return sb.reverse().toString();
        }
        
        private void startCleanupTask() {
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(urlStore::cleanupExpired, 1, 1, TimeUnit.HOURS);
        }
    }

    public static void main(String[] args) {
        URLShortenerService service = new URLShortenerService(1);
        
        // Test URL shortening
        String longUrl = "https://www.example.com/very/long/url/that/needs/to/be/shortened";
        String shortUrl = service.shortenURL(longUrl, "api_key_123");
        System.out.println("Shortened URL: " + shortUrl);
        
        // Test URL expansion
        String expanded = service.expandURL(shortUrl.substring("http://short.url/".length()));
        System.out.println("Expanded URL: " + expanded);
        
        // Test rate limiting
        for (int i = 0; i < 15; i++) {
            try {
                service.shortenURL("https://test.com/" + i, "api_key_123");
                System.out.println("Request " + i + " succeeded");
            } catch (RuntimeException e) {
                System.out.println("Request " + i + " failed: " + e.getMessage());
            }
        }
    }
}
