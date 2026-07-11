import java.util.*;
import java.util.concurrent.*;

/**
 * System Design — HashMap (Low-Level Design)
 * Implements a hash map with collision resolution and resizing
 * Features: Separate chaining, dynamic resizing, thread safety
 */
public class DesignHashMap<K, V> {

    // Entry for linked list (separate chaining)
    static class Entry<K, V> {
        final K key; V value; Entry<K, V> next;
        
        public Entry(K key, V value) {
            this.key = key; this.value = value; this.next = null;
        }
    }

    // HashMap implementation
    static class MyHashMap<K, V> {
        private static final int DEFAULT_CAPACITY = 16;
        private static final float LOAD_FACTOR = 0.75f;
        
        private Entry<K, V>[] table;
        private int size;
        private int capacity;
        
        @SuppressWarnings("unchecked")
        public MyHashMap() {
            this.capacity = DEFAULT_CAPACITY;
            this.table = (Entry<K, V>[]) new Entry[capacity];
            this.size = 0;
        }
        
        public MyHashMap(int initialCapacity) {
            this.capacity = initialCapacity;
            this.table = (Entry<K, V>[]) new Entry[capacity];
            this.size = 0;
        }
        
        private int hash(K key) {
            return (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
        }
        
        public void put(K key, V value) {
            if (size >= capacity * LOAD_FACTOR) {
                resize();
            }
            
            int index = hash(key);
            Entry<K, V> entry = table[index];
            
            // Check if key already exists
            while (entry != null) {
                if (Objects.equals(entry.key, key)) {
                    entry.value = value; // Update existing
                    return;
                }
                entry = entry.next;
            }
            
            // Add new entry at head
            Entry<K, V> newEntry = new Entry<>(key, value);
            newEntry.next = table[index];
            table[index] = newEntry;
            size++;
        }
        
        public V get(K key) {
            int index = hash(key);
            Entry<K, V> entry = table[index];
            
            while (entry != null) {
                if (Objects.equals(entry.key, key)) {
                    return entry.value;
                }
                entry = entry.next;
            }
            
            return null;
        }
        
        public V remove(K key) {
            int index = hash(key);
            Entry<K, V> entry = table[index];
            Entry<K, V> prev = null;
            
            while (entry != null) {
                if (Objects.equals(entry.key, key)) {
                    if (prev == null) {
                        table[index] = entry.next;
                    } else {
                        prev.next = entry.next;
                    }
                    size--;
                    return entry.value;
                }
                prev = entry;
                entry = entry.next;
            }
            
            return null;
        }
        
        public boolean containsKey(K key) {
            return get(key) != null;
        }
        
        public boolean containsValue(V value) {
            for (Entry<K, V> entry : table) {
                while (entry != null) {
                    if (Objects.equals(entry.value, value)) {
                        return true;
                    }
                    entry = entry.next;
                }
            }
            return false;
        }
        
        public int size() { return size; }
        public boolean isEmpty() { return size == 0; }
        
        public void clear() {
            table = (Entry<K, V>[]) new Entry[capacity];
            size = 0;
        }
        
        public Set<K> keySet() {
            Set<K> keys = new HashSet<>();
            for (Entry<K, V> entry : table) {
                while (entry != null) {
                    keys.add(entry.key);
                    entry = entry.next;
                }
            }
            return keys;
        }
        
        public Collection<V> values() {
            List<V> values = new ArrayList<>();
            for (Entry<K, V> entry : table) {
                while (entry != null) {
                    values.add(entry.value);
                    entry = entry.next;
                }
            }
            return values;
        }
        
        @SuppressWarnings("unchecked")
        private void resize() {
            int newCapacity = capacity * 2;
            Entry<K, V>[] newTable = (Entry<K, V>[]) new Entry[newCapacity];
            
            for (Entry<K, V> entry : table) {
                while (entry != null) {
                    Entry<K, V> next = entry.next;
                    int newIndex = Math.abs(entry.key.hashCode() % newCapacity);
                    entry.next = newTable[newIndex];
                    newTable[newIndex] = entry;
                    entry = next;
                }
            }
            
            table = newTable;
            capacity = newCapacity;
        }
    }

    // Thread-safe HashMap using fine-grained locking
    static class ConcurrentHashMap<K, V> {
        private static final int DEFAULT_SEGMENTS = 16;
        private final Segment<K, V>[] segments;
        
        @SuppressWarnings("unchecked")
        public ConcurrentHashMap() {
            this.segments = new Segment[DEFAULT_SEGMENTS];
            for (int i = 0; i < DEFAULT_SEGMENTS; i++) {
                segments[i] = new Segment<>();
            }
        }
        
        private Segment<K, V> getSegment(K key) {
            int index = (key == null) ? 0 : Math.abs(key.hashCode() % DEFAULT_SEGMENTS);
            return segments[index];
        }
        
        public void put(K key, V value) {
            Segment<K, V> segment = getSegment(key);
            segment.put(key, value);
        }
        
        public V get(K key) {
            Segment<K, V> segment = getSegment(key);
            return segment.get(key);
        }
        
        public V remove(K key) {
            Segment<K, V> segment = getSegment(key);
            return segment.remove(key);
        }
        
        public int size() {
            int total = 0;
            for (Segment<K, V> segment : segments) {
                total += segment.size();
            }
            return total;
        }
        
        static class Segment<K, V> {
            private final MyHashMap<K, V> map = new MyHashMap<>();
            private final Object lock = new Object();
            
            public void put(K key, V value) {
                synchronized (lock) { map.put(key, value); }
            }
            
            public V get(K key) {
                synchronized (lock) { return map.get(key); }
            }
            
            public V remove(K key) {
                synchronized (lock) { return map.remove(key); }
            }
            
            public int size() {
                synchronized (lock) { return map.size(); }
            }
        }
    }

    // HashMap with LRU eviction
    static class LRUCache<K, V> {
        private final int capacity;
        private final MyHashMap<K, Node<K, V>> map;
        private final Node<K, V> head, tail;
        
        static class Node<K, V> {
            K key; V value; Node<K, V> prev, next;
            public Node(K key, V value) { this.key = key; this.value = value; }
        }
        
        public LRUCache(int capacity) {
            this.capacity = capacity;
            this.map = new MyHashMap<>();
            this.head = new Node<>(null, null);
            this.tail = new Node<>(null, null);
            head.next = tail; tail.prev = head;
        }
        
        public V get(K key) {
            Node<K, V> node = map.get(key);
            if (node == null) return null;
            moveToHead(node);
            return node.value;
        }
        
        public void put(K key, V value) {
            Node<K, V> node = map.get(key);
            if (node != null) {
                node.value = value;
                moveToHead(node);
            } else {
                Node<K, V> newNode = new Node<>(key, value);
                map.put(key, newNode);
                addToFront(newNode);
                if (map.size() > capacity) {
                    removeLRU();
                }
            }
        }
        
        private void addToFront(Node<K, V> node) {
            node.prev = head; node.next = head.next;
            head.next.prev = node; head.next = node;
        }
        
        private void removeNode(Node<K, V> node) {
            node.prev.next = node.next; node.next.prev = node.prev;
        }
        
        private void moveToHead(Node<K, V> node) {
            removeNode(node); addToFront(node);
        }
        
        private void removeLRU() {
            Node<K, V> lru = tail.prev;
            removeNode(lru);
            map.remove(lru.key);
        }
        
        public int size() { return map.size(); }
    }

    public static void main(String[] args) {
        // Test basic HashMap
        System.out.println("=== Basic HashMap ===");
        MyHashMap<String, Integer> map = new MyHashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        
        System.out.println("Get 'two': " + map.get("two")); // 2
        System.out.println("Contains key 'three': " + map.containsKey("three")); // true
        System.out.println("Contains value 2: " + map.containsValue(2)); // true
        System.out.println("Size: " + map.size()); // 3
        
        map.remove("two");
        System.out.println("After remove, size: " + map.size()); // 2
        
        // Test Thread-safe HashMap
        System.out.println("\n=== Thread-safe HashMap ===");
        ConcurrentHashMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();
        concurrentMap.put("key1", 100);
        concurrentMap.put("key2", 200);
        System.out.println("Get 'key1': " + concurrentMap.get("key1")); // 100
        System.out.println("Size: " + concurrentMap.size()); // 2
        
        // Test LRU Cache
        System.out.println("\n=== LRU Cache ===");
        LRUCache<String, Integer> lru = new LRUCache<>(2);
        lru.put("a", 1);
        lru.put("b", 2);
        System.out.println("Get 'a': " + lru.get("a")); // 1
        lru.put("c", 3); // evicts 'b'
        System.out.println("Get 'b': " + lru.get("b")); // null
        System.out.println("Get 'c': " + lru.get("c")); // 3
        
        // Test resize
        System.out.println("\n=== Resize Test ===");
        MyHashMap<Integer, String> resizeMap = new MyHashMap<>(4);
        for (int i = 0; i < 20; i++) {
            resizeMap.put(i, "value" + i);
        }
        System.out.println("Size after adding 20 elements: " + resizeMap.size()); // 20
        System.out.println("Get value10: " + resizeMap.get(10)); // value10
    }
}
