import java.util.*;

/**
 * System Design — LRU Cache (Low-Level Design)
 * Implements a Least Recently Used cache with O(1) get and put operations
 * Features: Doubly linked list + hashmap, capacity management, thread safety
 */
public class DesignLRUCache {

    // Doubly linked list node
    static class DNode {
        int key; int value; DNode prev; DNode next;
        public DNode(int key, int value) { this.key = key; this.value = value; }
    }

    // LRU Cache implementation
    static class LRUCache {
        private final int capacity;
        private final Map<Integer, DNode> cache;
        private final DNode head; private final DNode tail;
        
        public LRUCache(int capacity) {
            this.capacity = capacity;
            this.cache = new HashMap<>();
            this.head = new DNode(0, 0); this.tail = new DNode(0, 0);
            head.next = tail; tail.prev = head;
        }
        
        public int get(int key) {
            if (!cache.containsKey(key)) return -1;
            DNode node = cache.get(key);
            moveToHead(node); // move to front (most recently used)
            return node.value;
        }
        
        public void put(int key, int value) {
            if (cache.containsKey(key)) {
                DNode node = cache.get(key);
                node.value = value;
                moveToHead(node);
            } else {
                DNode newNode = new DNode(key, value);
                cache.put(key, newNode);
                addToFront(newNode);
                if (cache.size() > capacity) removeLRU(); // remove least recently used
            }
        }
        
        private void addToFront(DNode node) {
            node.prev = head; node.next = head.next;
            head.next.prev = node; head.next = node;
        }
        
        private void removeNode(DNode node) {
            node.prev.next = node.next; node.next.prev = node.prev;
        }
        
        private void moveToHead(DNode node) {
            removeNode(node); addToFront(node);
        }
        
        private void removeLRU() {
            DNode lru = tail.prev;
            removeNode(lru);
            cache.remove(lru.key);
        }
    }

    // Thread-safe LRU Cache
    static class ThreadSafeLRUCache {
        private final LRUCache cache; private final Object lock = new Object();
        
        public ThreadSafeLRUCache(int capacity) { this.cache = new LRUCache(capacity); }
        
        public int get(int key) {
            synchronized (lock) { return cache.get(key); }
        }
        
        public void put(int key, int value) {
            synchronized (lock) { cache.put(key, value); }
        }
    }

    // LRU Cache with TTL support
    static class LRUCacheWithTTL {
        private final LRUCache cache;
        private final Map<Integer, Long> expiryMap;
        private final ScheduledExecutorService scheduler;
        
        public LRUCacheWithTTL(int capacity) {
            this.cache = new LRUCache(capacity);
            this.expiryMap = new HashMap<>();
            this.scheduler = Executors.newSingleThreadScheduledExecutor();
            startCleanupTask();
        }
        
        public int get(int key) {
            if (isExpired(key)) { remove(key); return -1; }
            return cache.get(key);
        }
        
        public void put(int key, int value, long ttlMillis) {
            cache.put(key, value);
            expiryMap.put(key, System.currentTimeMillis() + ttlMillis);
        }
        
        public void put(int key, int value) { put(key, value, Long.MAX_VALUE); }
        
        private boolean isExpired(int key) {
            Long expiry = expiryMap.get(key);
            return expiry != null && System.currentTimeMillis() > expiry;
        }
        
        private void remove(int key) {
            cache.put(key, -1); // Mark as invalid
            expiryMap.remove(key);
        }
        
        private void startCleanupTask() {
            scheduler.scheduleAtFixedRate(() -> {
                long now = System.currentTimeMillis();
                Iterator<Map.Entry<Integer, Long>> it = expiryMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<Integer, Long> entry = it.next();
                    if (now > entry.getValue()) {
                        remove(entry.getKey());
                        it.remove();
                    }
                }
            }, 1, 1, TimeUnit.SECONDS);
        }
        
        public void shutdown() { scheduler.shutdown(); }
    }

    // LRU Cache with statistics
    static class LRUCacheWithStats {
        private final LRUCache cache;
        private final AtomicLong hitCount = new AtomicLong(0);
        private final AtomicLong missCount = new AtomicLong(0);
        private final AtomicLong evictionCount = new AtomicLong(0);
        
        public LRUCacheWithStats(int capacity) { this.cache = new LRUCache(capacity); }
        
        public int get(int key) {
            int value = cache.get(key);
            if (value != -1) hitCount.incrementAndGet(); else missCount.incrementAndGet();
            return value;
        }
        
        public void put(int key, int value) {
            int sizeBefore = cache.cache.size();
            cache.put(key, value);
            if (cache.cache.size() < sizeBefore) evictionCount.incrementAndGet();
        }
        
        public double getHitRate() {
            long total = hitCount.get() + missCount.get();
            return total == 0 ? 0 : (double) hitCount.get() / total;
        }
        
        public long getHitCount() { return hitCount.get(); }
        public long getMissCount() { return missCount.get(); }
        public long getEvictionCount() { return evictionCount.get(); }
    }

    public static void main(String[] args) throws InterruptedException {
        // Test basic LRU Cache
        System.out.println("=== Basic LRU Cache ===");
        LRUCache lru = new LRUCache(2);
        lru.put(1, 1);
        lru.put(2, 2);
        System.out.println(lru.get(1)); // 1
        lru.put(3, 3); // evicts key 2
        System.out.println(lru.get(2)); // -1 (not found)
        lru.put(4, 4); // evicts key 1
        System.out.println(lru.get(1)); // -1 (not found)
        System.out.println(lru.get(3)); // 3
        System.out.println(lru.get(4)); // 4
        
        // Test Thread-safe LRU Cache
        System.out.println("\n=== Thread-safe LRU Cache ===");
        ThreadSafeLRUCache safeCache = new ThreadSafeLRUCache(2);
        safeCache.put(1, 10);
        safeCache.put(2, 20);
        System.out.println(safeCache.get(1)); // 10
        
        // Test LRU Cache with TTL
        System.out.println("\n=== LRU Cache with TTL ===");
        LRUCacheWithTTL ttlCache = new LRUCacheWithTTL(10);
        ttlCache.put(1, 100, 2000); // 2 seconds TTL
        System.out.println(ttlCache.get(1)); // 100
        Thread.sleep(2500);
        System.out.println(ttlCache.get(1)); // -1 (expired)
        ttlCache.shutdown();
        
        // Test LRU Cache with statistics
        System.out.println("\n=== LRU Cache with Statistics ===");
        LRUCacheWithStats statsCache = new LRUCacheWithStats(2);
        statsCache.put(1, 10);
        statsCache.put(2, 20);
        statsCache.get(1); // hit
        statsCache.get(3); // miss
        statsCache.put(3, 30); // eviction
        System.out.println("Hit count: " + statsCache.getHitCount());
        System.out.println("Miss count: " + statsCache.getMissCount());
        System.out.println("Eviction count: " + statsCache.getEvictionCount());
        System.out.println("Hit rate: " + statsCache.getHitRate());
    }
}
