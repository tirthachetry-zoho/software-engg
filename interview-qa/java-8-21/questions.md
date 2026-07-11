# Java 8–21 Features — Interview Q&A

## 1. What are lambda expressions and functional interfaces?
**Answer:** A lambda is a concise way to express an anonymous function: `(a, b) -> a + b`. It requires a **functional interface** — an interface with exactly one abstract method (marked `@FunctionalInterface`), e.g., `Predicate`, `Function`, `Supplier`, `Consumer`. Lambdas enable behavior parameterization and replace verbose anonymous classes.

## 2. What are Streams and the difference from collections?
**Answer:** A `Stream` is a sequence of elements supporting functional, often parallel, aggregate operations (filter/map/reduce). Unlike a Collection (stores data), a Stream is **not a data structure** — it's a pipeline that's lazy (intermediate ops) and consumed once. Source → intermediate (lazy) → terminal (triggers execution).

## 3. What is the difference between `map` and `flatMap`?
**Answer:** `map` transforms each element 1:1 (e.g., `s -> s.length()`). `flatMap` transforms each element into a stream and **flattens** the results (e.g., splitting a sentence into words across a list of sentences). Use `flatMap` when each element maps to multiple elements.

## 4. What is `Optional` and its best practices?
**Answer:** `Optional<T>` is a container to represent presence/absence of a value, avoiding `NullPointerException`. Best practices: avoid `get()` (use `orElse`/`orElseGet`/`ifPresent`), don't use as a field/collection element, use `map`/`filter`/`flatMap` for chaining, and return it from methods that may have no result.

## 5. What is the difference between `parallelStream()` and sequential stream?
**Answer:** `parallelStream()` splits work across the **ForkJoinPool.commonPool()** for CPU-bound large datasets — can speed up heavy computations. Avoid for I/O-bound or small datasets (overhead dominates). Also beware shared mutable state and `synchronized` blocks inside parallel ops.

## 6. What are method references?
**Answer:** A shorthand for lambdas that just call an existing method: `String::length` (instance method), `System.out::println` (bound), `Integer::parseInt` (static), `ClassName::new` (constructor). They improve readability when the lambda body is a single method call.

## 7. What are Records (Java 16/17+)?
**Answer:** `record Point(int x, int y)` is a concise immutable data class. The compiler auto-generates the constructor, private final fields, accessors (`x()`, not `getX()`), `equals()`, `hashCode()`, and `toString()`. Ideal for DTOs and value objects. Can implement interfaces and have additional (static/compact) methods.

## 8. What are Sealed Classes (Java 17+)?
**Answer:** `sealed interface Expr permits Const, Add, Mul` restricts which classes may implement/extend it — enabling exhaustive `switch` patterns and controlled hierarchies. Subclasses must be `final`, `sealed`, or `non-sealed`. Improves domain modeling and pattern matching safety.

## 9. What are Virtual Threads (Java 21, Project Loom)?
**Answer:** Lightweight, JVM-managed threads that map many-to-few OS threads, enabling **millions** of concurrent tasks cheaply. Created via `Thread.startVirtualThread(() -> ...)` or `Executors.newVirtualThreadPerTaskExecutor()`. Great for I/O-bound workloads; they don't need pooling.

## 10. What is the danger of `synchronized` with Virtual Threads?
**Answer:** When a virtual thread blocks on a `synchronized` monitor, it **pins** the underlying carrier (platform) thread, negating the scalability benefit. Fix: use `ReentrantLock` (which releases the carrier during blocking) instead of `synchronized` in hot virtual-thread paths.

## 11. What is Structured Concurrency (Java 21)?
**Answer:** `StructuredTaskScope` lets a task spawn multiple subtasks that are treated as a unit — they're scoped to a block, errors/cancellations propagate, and you get clean cleanup. It makes concurrent code as readable and manageable as sequential code, avoiding orphaned threads.

## 12. What is the Java Platform Module System (JPMS, Java 9)?
**Answer:** JPMS (Project Jigsaw) introduces `module-info.java` to declare explicit dependencies (`requires`) and exported packages (`exports`), enabling strong encapsulation, reliable configuration, and smaller runtime images via `jlink`. It replaced the classpath's flat, unrestricted visibility with modular boundaries.