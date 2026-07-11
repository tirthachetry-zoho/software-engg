# Core Java — Interview Q&A

## 1. What is the difference between JDK, JRE, and JVM?
**Answer:** JDK (Java Development Kit) = JRE + compiler (`javac`) + dev tools. JRE (Java Runtime Environment) = JVM + standard libraries needed to *run* Java programs. JVM (Java Virtual Machine) executes bytecode and provides memory management, GC, and platform independence. You need JDK to develop, JRE to run, and JVM is the engine inside both.

## 2. Explain the Java compilation and execution flow.
**Answer:** `.java` source → `javac` compiles to `.class` bytecode → JVM loads bytecode via classloaders → JIT (Just-In-Time) compiler compiles hot bytecode to native machine code at runtime. This two-step process gives both portability (bytecode runs anywhere) and performance (native compilation of hot paths).

## 3. Why is String immutable in Java?
**Answer:** (1) **Security** — class loaders, network URLs, DB credentials rely on string constants not being mutated. (2) **Thread safety** — immutable objects are inherently safe to share. (3) **String Pool** — literals are cached; mutability would corrupt shared references. (4) **Hashcode caching** — `hashCode()` is computed once and cached, making Strings excellent HashMap keys.

## 4. What is the difference between `==` and `equals()`?
**Answer:** `==` compares references (memory address) for objects and values for primitives. `equals()` compares logical/content equality (when overridden, e.g., in String, Integer). For objects, `==` returns true only if both refer to the same instance. Always override `equals()` (with `hashCode()`) for value-based equality.

## 5. Why must you override `hashCode()` when you override `equals()`?
**Answer:** The contract states: if two objects are equal via `equals()`, they must have the same `hashCode()`. HashMap/HashSet use `hashCode()` to locate the bucket; if equal objects have different hashes, they land in different buckets and the collection fails to find them — breaking set uniqueness and map lookups.

## 6. Explain `final`, `finally`, and `finalize`.
**Answer:** `final` — keyword: prevents variable reassignment, method override, or class extension. `finally` — block in try-catch that always executes (except `System.exit`), used for resource cleanup. `finalize()` — a method called by GC before reclaiming an object; **deprecated in Java 9+** and unreliable; use try-with-resources instead.

## 7. What is the difference between `StringBuilder` and `StringBuffer`?
**Answer:** Both are mutable string builders. `StringBuilder` is not synchronized (faster, single-threaded use). `StringBuffer` is synchronized (thread-safe, slower). Since most string building is single-threaded, prefer `StringBuilder`.

## 8. What are the main methods of the `Object` class?
**Answer:** `equals()`, `hashCode()`, `toString()`, `clone()`, `getClass()`, `wait()`, `notify()`, `notifyAll()`, and the deprecated `finalize()`. These are inherited by every Java class.

## 9. What is the difference between `Comparable` and `Comparator`?
**Answer:** `Comparable` (`compareTo`) defines the *natural* ordering of a class — implemented by the class itself (e.g., `String`, `Integer`). `Comparator` is a separate strategy object used for *custom* orderings without modifying the class (e.g., sort by name vs age). Use `Comparator` when you need multiple sort orders.

## 10. What is autoboxing and unboxing?
**Answer:** Autoboxing is automatic conversion of a primitive (e.g., `int`) to its wrapper object (`Integer`) — e.g., `List<Integer> l = new ArrayList<>(); l.add(5);`. Unboxing is the reverse. Watch for `NullPointerException` when unboxing a null wrapper, and performance cost in tight loops.

## 11. What is the difference between method overloading and overriding?
**Answer:** Overloading = same method name, different parameters, resolved at **compile time** (static polymorphism), within same class. Overriding = subclass redefines a parent method with same signature, resolved at **runtime** (dynamic polymorphism). Overriding requires `@Override`, covariant return types allowed, cannot reduce visibility.

## 12. Explain `transient` and `volatile` keywords.
**Answer:** `transient` — field is skipped during default Java serialization. `volatile` — guarantees cross-thread *visibility* of a variable (reads/writes go to main memory) and prevents reordering around it, but provides **no atomicity** (use `AtomicInteger` or locks for compound actions like `i++`).