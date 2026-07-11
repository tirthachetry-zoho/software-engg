# Java Collections — Interview Q&A

## 1. Explain HashMap internal working in Java 8+.
**Answer:** Backed by a `Node<K,V>[]` array (buckets). Key hash is spread via `hash = h ^ (h >>> 16)` to reduce collisions. Bucket index = `(n - 1) & hash`. On collision, entries form a linked list; once a bucket reaches 8 nodes AND the table size ≥ 64, it's **treeified** into a Red-Black tree (O(log n) lookup). Average O(1), worst O(log n).

## 2. What is the difference between HashMap and ConcurrentHashMap?
**Answer:** `HashMap` is not thread-safe (lost updates, corrupted structure under concurrency). `ConcurrentHashMap` (Java 8+) uses per-bucket `synchronized` on the bucket head + CAS for inserts, and lock-free reads — allowing high concurrency. It does not throw `ConcurrentModificationException` and supports atomic `putIfAbsent`, `compute`, etc.

## 3. What is the load factor and when does HashMap resize?
**Answer:** Default load factor is **0.75** and initial capacity 16. When `size > capacity * loadFactor`, the table **doubles** in capacity and all entries are **rehashed** into the new buckets. Lower load factor = less collision but more memory; higher = more collisions.

## 4. ArrayList vs LinkedList — when to use which?
**Answer:** `ArrayList` (dynamic array): O(1) random access `get(i)`, fast iteration, but O(n) insert/remove in the middle (shifting). `LinkedList` (doubly-linked): O(1) insert/remove at ends, but O(n) to reach an index. Prefer ArrayList for most cases; use LinkedList only for heavy head/tail operations or as a queue/deque.

## 5. What is the difference between fail-fast and fail-safe iterators?
**Answer:** **Fail-fast** (ArrayList, HashMap) throws `ConcurrentModificationException` if the collection is structurally modified during iteration (checks `modCount`). **Fail-safe** (CopyOnWriteArrayList, ConcurrentHashMap iterators) operate on a snapshot/clone, so they never throw but may not reflect concurrent updates.

## 6. TreeMap vs HashMap — when would you use TreeMap?
**Answer:** `HashMap` is unordered with O(1) average ops. `TreeMap` (Red-Black tree) keeps keys **sorted** by natural order or a Comparator, with O(log n) ops. Use TreeMap when you need range queries, sorted traversal, or `ceilingKey`/`floorKey` operations.

## 7. What is the difference between HashSet and TreeSet?
**Answer:** `HashSet` is backed by a HashMap, unordered, O(1) add/contains. `TreeSet` is backed by a TreeMap, keeps elements sorted, O(log n). `LinkedHashSet` preserves insertion order. Choose based on whether you need ordering.

## 8. How does LinkedHashMap support LRU cache?
**Answer:** `LinkedHashMap` maintains a doubly-linked list of entries in access/insertion order. Override `removeEldestEntry()` to evict the oldest entry when size exceeds a limit. Set `accessOrder=true` to reorder on `get`, enabling an **LRU (Least Recently Used)** cache.

## 9. What is the difference between Comparator.naturalOrder and a custom Comparator?
**Answer:** `naturalOrder()` uses the class's `Comparable` implementation. A custom `Comparator` lets you define arbitrary ordering (e.g., by length, descending, multiple fields) without changing the class. `Comparator.comparing(...).thenComparing(...)` chains fields cleanly.

## 10. Explain the difference between Queue, Deque, and PriorityQueue.
**Answer:** `Queue` is FIFO (offer/poll/peek). `Deque` (double-ended) supports both ends (can be stack or queue). `PriorityQueue` orders elements by priority (heap), not insertion — `poll()` returns the min (or max via reverse comparator), O(log n) insert/remove.

## 11. What are the legacy thread-safe collections and why avoid them?
**Answer:** `Vector` and `Hashtable` synchronize every method, causing contention and poor scalability. Modern replacements: `ArrayList`/`HashMap` for single-thread, and `CopyOnWriteArrayList`/`ConcurrentHashMap` for concurrent use — they offer better performance via finer-grained locking or copy-on-write.

## 12. What is a Spliterator and what is it used for?
**Answer:** `Spliterator` (Splitable Iterator) traverses and partitions a source, enabling **parallel** processing in Streams. It can split itself into smaller spliterators for fork-join execution, reports characteristics (SIZED, ORDERED) for optimization, and is the backbone of `stream().parallel()`.