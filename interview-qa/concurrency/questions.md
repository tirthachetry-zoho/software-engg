# Multithreading & Concurrency — Interview Q&A

## 1. What is the difference between a process and a thread?
**Answer:** A **process** is an independent program with its own memory space. A **thread** is a lightweight unit of execution within a process, sharing the same heap/memory but with its own stack and PC. Threads are cheaper to create/context-switch and enable concurrency within one process.

## 2. Explain the difference between `synchronized` and `ReentrantLock`.
**Answer:** `synchronized` is a JVM built-in monitor lock (reentrant, auto-release on block exit, no timeout). `ReentrantLock` (from `java.util.concurrent.locks`) offers `tryLock(timeout)`, **fairness** option, multiple condition variables, and interruptible lock acquisition. Use `ReentrantLock` when you need advanced control; otherwise `synchronized` is simpler.

## 3. What is the difference between `volatile` and `Atomic` classes?
**Answer:** `volatile` guarantees **visibility** (reads/writes go to main memory) and prevents certain reorderings, but NOT atomicity (e.g., `i++` is still 3 operations). `AtomicInteger`/`AtomicReference` use **CAS (Compare-And-Swap)** for lock-free atomic compound operations like `incrementAndGet()`.

## 4. What is the happens-before relationship?
**Answer:** A memory visibility guarantee: if action A *happens-before* action B, then B sees A's effects. Established by: unlocking a monitor happens-before subsequent lock; writing a `volatile` happens-before subsequent reads; thread start; joining a thread. It's the foundation of safe publication in Java's JMM.

## 5. What is a deadlock and how do you prevent it?
**Answer:** A deadlock is when two+ threads each hold a lock and wait forever for the other's lock. Prevention: (1) **lock ordering** (always acquire locks in a consistent global order), (2) `tryLock(timeout)` to back off, (3) minimize lock scope, (4) use a single higher-level lock or `StampedLock` optimistic reads.

## 6. Explain `ExecutorService` and thread pool sizing.
**Answer:** `ExecutorService` abstracts thread management; submit `Runnable`/`Callable` tasks. Pool sizing rule of thumb: **CPU-bound** ≈ `N + 1` (N = cores); **I/O-bound** ≈ `N * (1 + waitTime/serviceTime)`. Oversized pools cause context-switch overhead; undersized ones cause starvation.

## 7. What is `CompletableFuture` and how do you chain async tasks?
**Answer:** `CompletableFuture` represents a future result of async work with a rich fluent API. Chain with `thenApply` (transform), `thenCompose` (flatMap-style sequential), `thenCombine` (join two), `thenAccept` (consume), and `exceptionally`/`handle` for errors. Runs on `ForkJoinPool.commonPool()` by default or a custom executor.

## 8. What is the difference between `Runnable` and `Callable`?
**Answer:** `Runnable.run()` returns `void` and cannot throw checked exceptions. `Callable.call()` returns a result `V` and can throw checked exceptions. `Callable` is used with `ExecutorService.submit()` which returns a `Future<V>` to retrieve the result.

## 9. What is a `BlockingQueue` and how does it solve producer-consumer?
**Answer:** A thread-safe queue where `put()` blocks when full and `take()` blocks when empty — perfect for producer-consumer without manual `wait/notify`. Implementations: `ArrayBlockingQueue` (bounded), `LinkedBlockingQueue`, `PriorityBlockingQueue`, `SynchronousQueue` (handoff).

## 10. What is the difference between `CountDownLatch`, `CyclicBarrier`, and `Semaphore`?
**Answer:** `CountDownLatch` — one-time countdown to zero, releasing waiting threads (e.g., start gate). `CyclicBarrier` — threads wait for each other to reach a point, then all proceed (reusable). `Semaphore` — permits limiting concurrent access to a resource (e.g., connection pool size).

## 11. What is `ThreadLocal` and what is the danger with thread pools?
**Answer:** `ThreadLocal` gives each thread its own isolated variable copy (e.g., user context, SimpleDateFormat). Danger: in thread pools, threads are reused, so a `ThreadLocal` set by one task leaks into the next unless explicitly `remove()`d — a common source of bugs and memory leaks.

## 12. What is the difference between `Future` and `CompletableFuture`?
**Answer:** `Future` (from `ExecutorService.submit`) is read-only — you can `get()` (blocking) or `cancel()`, but cannot compose or chain. `CompletableFuture` extends `Future` with non-blocking callbacks, combinators, and exception handling, enabling reactive async pipelines without blocking threads.