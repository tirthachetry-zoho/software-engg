package companywise.GoldmanSachs.lld;

import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.*;

/**
 * Low-Level Design — LRU Cache (Goldman Sachs LLD favorite)
 *
 * O(1) get/put using a doubly-linked list (order) + HashMap (lookup).
 * Most-recently-used at the head, least-recently-used at the tail.
 * Includes a thread-safe variant and a TTL variant for completeness.
 */
public class DesignLRUCache {

    static class DNode {
        int key, value;
        DNode prev, next;
        DNode(int k, int v) { key = k; value = v; }
    }

    static class LRUCache {
        private final int capacity;
        private final Map<Integer, DNode> cache;
        private final DNode head, tail;

        LRUCache(int capacity) {
            this.capacity = capacity;
            this.cache = new HashMap<>();
            this.head = new DNode(0, 0);
            this.tail = new DNode(0, 0);
            head.next = tail; tail.prev = head;
        }

        public int get(int key) {
            if (!cache.containsKey(key)) return -1;
            DNode n = cache.get(key);
            moveToHead(n);
            return n.value;
        }

        public void put(int key, int value) {
            if (cache.containsKey(key)) {
                DNode n = cache.get(key);
                n.value = value;
                moveToHead(n);
            } else {
                DNode n = new DNode(key, value);
                cache.put(key, n);
                addToFront(n);
                if (cache.size() > capacity) removeLRU();
            }
        }

        private void addToFront(DNode n) {
            n.prev = head; n.next = head.next;
            head.next.prev = n; head.next = n;
        }
        private void removeNode(DNode n) {
            n.prev.next = n.next; n.next.prev = n.prev;
        }
        private void moveToHead(DNode n) { removeNode(n); addToFront(n); }
        private void removeLRU() {
            DNode lru = tail.prev;
            removeNode(lru);
            cache.remove(lru.key);
        }
    }

    // Thread-safe wrapper
    static class ThreadSafeLRUCache {
        private final LRUCache cache;
        private final Object lock = new Object();
        ThreadSafeLRUCache(int c) { cache = new LRUCache(c); }
        int get(int k) { synchronized (lock) { return cache.get(k); } }
        void put(int k, int v) { synchronized (lock) { cache.put(k, v); } }
    }

    // TTL variant
    static class LRUCacheWithTTL {
        private final LRUCache cache;
        private final Map<Integer, Long> expiry;
        LRUCacheWithTTL(int c) { cache = new LRUCache(c); expiry = new HashMap<>(); }
        int get(int k) {
            if (expiry.getOrDefault(k, Long.MAX_VALUE) < System.currentTimeMillis()) { cache.put(k, -1); expiry.remove(k); return -1; }
            return cache.get(k);
        }
        void put(int k, int v, long ttlMillis) { cache.put(k, v); expiry.put(k, System.currentTimeMillis() + ttlMillis); }
    }

    public static void main(String[] args) {
        LRUCache lru = new LRUCache(2);
        lru.put(1, 1);
        lru.put(2, 2);
        System.out.println(lru.get(1)); // 1
        lru.put(3, 3);                  // evicts key 2
        System.out.println(lru.get(2)); // -1
        lru.put(4, 4);                  // evicts key 1
        System.out.println(lru.get(1)); // -1
        System.out.println(lru.get(3)); // 3
        System.out.println(lru.get(4)); // 4
    }
}