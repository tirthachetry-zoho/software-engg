package companywise.GoldmanSachs.lld;

import java.util.*;
import java.util.concurrent.*;

/**
 * Low-Level Design — Rate Limiter (Goldman Sachs LLD)
 *
 * Implements two common rate-limiting algorithms used to throttle API / order traffic:
 *  1. Token Bucket  — tokens refill at a fixed rate; a request consumes one token.
 *  2. Sliding Window Log — keep timestamps of recent requests within a window.
 *
 * Both are keyed by client/tenant id and are thread-safe (synchronized / ConcurrentHashMap).
 */
public class DesignRateLimiter {

    // ---------------- Token Bucket ----------------
    static class TokenBucket {
        private int tokens;
        private final int capacity;
        private final long refillMillis;
        private long lastRefill;

        TokenBucket(int capacity, long refillMillis) {
            this.capacity = capacity;
            this.refillMillis = refillMillis;
            this.tokens = capacity;
            this.lastRefill = System.currentTimeMillis();
        }

        synchronized boolean tryConsume() {
            refill();
            if (tokens > 0) { tokens--; return true; }
            return false;
        }

        private void refill() {
            long now = System.currentTimeMillis();
            long elapsed = now - lastRefill;
            if (elapsed >= refillMillis) {
                tokens = Math.min(capacity, tokens + (int) (elapsed / refillMillis));
                lastRefill = now;
            }
        }
    }

    static class TokenBucketLimiter {
        private final Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();
        private final int capacity;
        private final long refillMillis;

        TokenBucketLimiter(int capacity, long refillMillis) {
            this.capacity = capacity;
            this.refillMillis = refillMillis;
        }

        boolean allow(String key) {
            TokenBucket b = buckets.computeIfAbsent(key, k -> new TokenBucket(capacity, refillMillis));
            return b.tryConsume();
        }
    }

    // ---------------- Sliding Window Log ----------------
    static class SlidingWindowLimiter {
        private final int maxRequests;
        private final long windowMillis;
        private final Map<String, Deque<Long>> logs = new ConcurrentHashMap<>();

        SlidingWindowLimiter(int maxRequests, long windowMillis) {
            this.maxRequests = maxRequests;
            this.windowMillis = windowMillis;
        }

        boolean allow(String key) {
            long now = System.currentTimeMillis();
            Deque<Long> q = logs.computeIfAbsent(key, k -> new ConcurrentLinkedDeque<>());
            synchronized (q) {
                while (!q.isEmpty() && now - q.peekFirst() >= windowMillis) q.pollFirst();
                if (q.size() < maxRequests) { q.addLast(now); return true; }
                return false;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Token Bucket (capacity 3, refill 1/sec) ===");
        TokenBucketLimiter tbl = new TokenBucketLimiter(3, 1000);
        for (int i = 0; i < 5; i++)
            System.out.println("req " + i + " allowed=" + tbl.allow("clientA"));
        Thread.sleep(1100);
        System.out.println("after refill, allowed=" + tbl.allow("clientA"));

        System.out.println("\n=== Sliding Window (max 3 per 1s) ===");
        SlidingWindowLimiter swl = new SlidingWindowLimiter(3, 1000);
        for (int i = 0; i < 5; i++)
            System.out.println("req " + i + " allowed=" + swl.allow("clientB"));
    }
}