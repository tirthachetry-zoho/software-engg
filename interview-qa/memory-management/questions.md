# Java Memory Management — Interview Q&A

## 1. What are the different memory areas in the JVM?
**Answer:** (1) **Heap** — objects, GC-managed (Young: Eden+S0+S1, and Old). (2) **Stack** — per-thread frames holding locals, operands, return addresses. (3) **Metaspace** — class metadata (native memory, auto-grows). (4) **Native/Off-heap** — NIO buffers, JNI, thread stacks. (5) **PC register & native method stack** per thread.

## 2. Explain the heap generations and why they exist.
**Answer:** Heap is split into **Young** (Eden + two Survivor spaces) where most objects die young, and **Old** (Tenured) for long-lived objects. The generational hypothesis ("most objects die young") lets GC use efficient copying collection on Young and less frequent, slower collection on Old — optimizing throughput and pause times.

## 3. What is the difference between Serial, Parallel, G1, ZGC, and Shenandoah collectors?
**Answer:** **Serial** — single-threaded STW, small apps. **Parallel** — multi-threaded STW, throughput-focused. **G1** (default) — region-based, balances pause/time, predictable pauses. **ZGC** — sub-millisecond pauses, handles huge heaps (TB). **Shenandoah** — concurrent compaction, low latency. Choice depends on heap size and latency SLA.

## 4. What causes a memory leak in Java?
**Answer:** Objects no longer needed but still *referenced*. Common causes: static collections that grow unbounded, unclosed resources (connections/streams), `ThreadLocal` not removed (especially in thread pools), listeners/callbacks never deregistered, and caches without eviction. Use heap dumps + profilers (VisualVM, async-profiler) to find retained objects.

## 5. What are the common OutOfMemoryError types?
**Answer:** `Java heap space` (objects exceed heap), `Metaspace` (too many classes/loaded by dynamic classloaders), `GC overhead limit exceeded` (GC spends >98% time reclaiming <2% memory), `Unable to create new native thread` (thread limit/ulimit), `Direct buffer memory` (off-heap NIO). Each points to a different root cause.

## 6. What is StackOverflowError and when does it occur?
**Answer:** Thrown when the stack of a thread exceeds its capacity, typically from **deep or infinite recursion** (no base case) or extremely deep method chains. Unlike OOM, it's usually a code bug, not a sizing issue. Fix by adding a termination condition or converting recursion to iteration.

## 7. What is escape analysis and how does it help?
**Answer:** JIT's escape analysis determines whether an object escapes a method/thread. If not, the JVM may **stack-allocate** it (instead of heap) or eliminate the allocation entirely (scalar replacement), and synchronize lock-elision. This reduces GC pressure and improves performance for short-lived local objects.

## 8. What is the difference between strong, soft, weak, and phantom references?
**Answer:** **Strong** — normal references, never collected while reachable. **Soft** — collected only when memory is low (good for caches). **Weak** — collected on next GC regardless (used in `WeakHashMap`). **Phantom** — enqueued after finalization, for post-mortem cleanup (e.g., off-heap memory). Strongest → weakest retention.

## 9. How does garbage collection determine what to collect?
**Answer:** Via **reachability** from GC roots (active stack frames, static fields, JNI refs). Objects reachable from roots are live; unreachable ones are garbage. Modern collectors use generational + concurrent marking to find live objects with minimal pauses.

## 10. What is the difference between Minor, Major, and Full GC?
**Answer:** **Minor GC** — collects Young gen only (frequent, fast). **Major GC** — collects Old gen. **Full GC** — entire heap + metaspace (slowest, longest pauses). G1 blurs these with mixed collections. Tuning aims to minimize Full GC frequency.

## 11. How do you diagnose a memory issue in production?
**Answer:** Capture `jmap -histo` / heap dump (`jmap -dump`), analyze with Eclipse MAT or VisualVM to find the biggest retained objects. Use `jstat -gc` for GC stats, async-profiler for allocation hotspots, and `-XX:+HeapDumpOnOutOfMemoryError` to auto-dump on OOM.

## 12. What is the difference between PermGen and Metaspace?
**Answer:** **PermGen** (Java 7 and earlier) was a fixed-size, heap-resident region for class metadata — prone to `OutOfMemoryError: PermGen`. **Metaspace** (Java 8+) lives in native memory and **auto-grows** by default, removing the fixed-size limit and reducing metadata OOMs (bounded by `MaxMetaspaceSize` if set).