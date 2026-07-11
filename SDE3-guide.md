
> Complete roadmap for Senior Java Backend Engineer / SDE3 interviews.
> Every section includes concise notes, interview-ready answers, and **curated links to FREE problems & solutions**.

---

# Table of Contents

1. [Interview Expectations](#1-interview-expectations)
2. [Core Java](#2-core-java)
3. [Object Oriented Programming](#3-object-oriented-programming)
4. [Java Collections](#4-java-collections)
5. [Java Memory Management](#5-java-memory-management)
6. [JVM Internals](#6-jvm-internals)
7. [Multithreading & Concurrency](#7-multithreading--concurrency)
8. [Java 8–21 Features](#8-java-821-features)
9. [Exception Handling](#9-exception-handling)
10. [Generics](#10-generics)
11. [Reflection & Annotations](#11-reflection--annotations)
12. [Serialization](#12-serialization)
13. [Design Patterns](#13-design-patterns)
14. [SOLID Principles](#14-solid-principles)
15. [Clean Code](#15-clean-code)
16. [Data Structures](#16-data-structures)
17. [Algorithms](#17-algorithms)
18. [SQL](#18-sql)
19. [Database Design](#19-database-design)
20. [Spring Framework](#20-spring-framework)
21. [Spring Boot](#21-spring-boot)
22. [REST APIs](#22-rest-apis)
23. [Security](#23-security)
24. [Microservices](#24-microservices)
25. [Messaging Systems](#25-messaging-systems)
26. [Redis](#26-redis)
27. [ElasticSearch](#27-elasticsearch)
28. [Docker](#28-docker)
29. [Kubernetes](#29-kubernetes)
30. [Linux](#30-linux)
31. [Networking](#31-networking)
32. [High Level System Design](#32-high-level-system-design)
33. [Low Level Design](#33-low-level-design)
34. [Cloud](#34-cloud)
35. [CI/CD](#35-cicd)
36. [Testing](#36-testing)
37. [Observability](#37-observability)
38. [Performance Tuning](#38-performance)
39. [Behavioral Interview](#39-behavioral)
40. [Machine Coding](#40-machine-coding)
41. [Resume Preparation](#41-resume)
42. [Revision Checklist](#42-revision-checklist)

---

# 1. Interview Expectations

## Typical SDE3 Interview Loop

| Round | Focus | What they look for |
|-------|-------|--------------------|
| Coding (DSA) | 1–2 problems, medium/hard | Correctness, complexity, clean code |
| Core Java | Language depth | Memory model, collections, JVM |
| JVM | Internals & GC | GC algorithms, tuning, class loading |
| Concurrency | Threads, locks, async | Race conditions, thread safety |
| Spring Boot | Framework depth | DI, auto-config, transactions |
| Database | SQL + design | Indexing, transactions, scaling |
| System Design | HLD | Scalability, tradeoffs, CAP |
| Low Level Design | LLD | Classes, patterns, extensibility |
| Behavioral | STAR stories | Leadership, ownership |
| Architecture Discussion | Past projects | Impact, decisions, metrics |

## Interviewers evaluate
- **Coding quality** — readable, tested, edge-case aware.
- **Scalability thinking** — horizontal scaling, caching, queues.
- **Leadership** — drove decisions, mentored, owned outcomes.
- **Tradeoff analysis** — why X over Y, what you'd sacrifice.
- **Ownership** — end-to-end responsibility, on-call, postmortems.
- **Communication** — explain clearly, listen, clarify requirements.
- **Problem solving** — structured approach, hypothesis-driven.

> SDE3 is a **senior** bar: you are expected to design, not just implement. Interviewers want to see you *drive* the conversation.

### Useful Free Links
- [Tech Interview Handbook (free)](https://www.techinterviewhandbook.org/)
- [Exponent — Free Interview Guides](https://www.tryexponent.com/)
- [System Design Primer (GitHub, free)](https://github.com/donnemartin/system-design-primer)

---

# 2. Core Java

## Basics
- **JVM:** executes bytecode; provides memory management, GC, platform independence.
- **JRE:** JVM + libraries to run Java apps.
- **JDK:** JRE + compiler (`javac`) + dev tools.
- **Bytecode & Compilation:** `.java --(javac)--> .class (bytecode) --(JVM/JIT)--> native code`.
- **ClassLoader:** loads `.class` files → Loading → Linking (Verification, Preparation, Resolution) → Initialization.

## OOP
- **Encapsulation:** hide state, expose via methods.
- **Inheritance:** `is-a`; reuse but tight coupling.
- **Polymorphism:** same interface, different behavior.
- **Abstraction:** hide complexity behind interfaces/abstract classes.

## Keywords
| Keyword | Meaning |
|---------|---------|
| `static` | Belongs to class; one shared copy. |
| `final` | Cannot reassign (var) / extend (class) / override (method). |
| `transient` | Skipped in default serialization. |
| `volatile` | Cross-thread visibility; no atomicity. |
| `synchronized` | Monitor-based mutual exclusion. |
| `native` | Platform C/C++ implementation. |

## Object Class
- `equals()` — logical equality (reflexive, symmetric, transitive, consistent).
- `hashCode()` — int hash; **override with `equals`**.
- `clone()` — prefer copy constructors over `Cloneable`.
- `finalize()` — deprecated in Java 9+; don't rely on it.
- `toString()` — always override.

## String
- **String Pool:** JVM caches literals → memory savings.
- **Immutable:** safe sharing, pooling, thread safety, hash caching.
- `StringBuilder` — mutable, non-synchronized (fast).
- `StringBuffer` — mutable, synchronized (thread-safe).

### Interview Questions
- **Why String immutable?** Security, thread safety, pool efficiency, hashcode caching.
- **Why override hashCode with equals?** Equal objects must have equal hashes; HashMap/HashSet depend on it.
- **`==` vs `equals()`?** `==` compares references; `equals()` compares content (when overridden).
- **String Pool memory issues?** Old PermGen fixed size → OOM; modern heap-based but excessive interning still pressures heap.

### Free Practice & Reference Links
- [Baeldung — Java Strings (free)](https://www.baeldung.com/java-string)
- [GeeksforGeeks — String Pool (free)](https://www.geeksforgeeks.org/string-pool-in-java/)
- [Oracle — Object equals/hashCode (free docs)](https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#equals-java.lang.Object-)
- [GeeksforGeeks — Java MCQ (free)](https://www.geeksforgeeks.org/java-mcq-set-1/)

---

# 3. Object Oriented Programming

## Relationships
- **Composition:** strong ownership; lifecycle-bound. *Favor this.*
- **Aggregation:** weak ownership; independent lifecycles.
- **Association:** general relationship.
- **Dependency:** temporary usage; weakest coupling.

## Principles
- **DRY** — don't repeat yourself.
- **KISS** — keep it simple.
- **YAGNI** — don't build speculative features.

### Questions
- **Favor Composition over Inheritance?** Inheritance breaks encapsulation, is compile-time bound, fragile. Composition is flexible/testable.
- **LSP example:** `Square extends Rectangle` breaks contract if width≠height enforced → don't force it.
- **OCP example:** Strategy pattern adds algorithms without modifying context.

### Free Links
- [Refactoring Guru — Composition vs Inheritance (free)](https://refactoring.guru/design-patterns/composite)
- [SpringFramework.guru — LSP (free)](https://springframework.guru/solid-principle-liskov-substitution-principle/)
- [GeeksforGeeks — OOP in Java (free)](https://www.geeksforgeeks.org/object-oriented-programming-oop-concept-in-java/)

---

# 4. Java Collections

## Interfaces
`Collection` → `List`, `Set`, `Queue`; standalone `Map`.

## Implementations
| Structure | Backing | Thread-safe? | Notes |
|-----------|---------|--------------|-------|
| ArrayList | dynamic array | no | O(1) get |
| LinkedList | doubly-linked | no | O(1) ends |
| Vector | array | yes | legacy |
| HashSet | HashMap | no | no order |
| TreeSet | Red-Black | no | sorted |
| LinkedHashSet | linked HashMap | no | insertion order |
| HashMap | array+list/tree | no | O(1) avg |
| TreeMap | Red-Black | no | sorted |
| LinkedHashMap | HashMap+list | no | LRU possible |
| Hashtable | array | yes | legacy |
| ConcurrentHashMap | buckets | yes | CAS/sync |

## Topics
- **HashMap internals:** `Node[]` array; `hash = h ^ (h>>>16)`; bucket `(n-1)&hash`; collision → linked list → treeified at 8 (table ≥64).
- **Load Factor 0.75:** resize & rehash when exceeded; capacity doubles.
- **Fail-Fast:** `ConcurrentModificationException` on structural change (ArrayList, HashMap).
- **Fail-Safe:** snapshot-based (CopyOnWriteArrayList, ConcurrentHashMap iterators).
- **Spliterator:** parallel traversal for Streams.

### Interview Questions
- **HashMap internals (Java 8+):** bucket array of `Node`; treeify at threshold; O(1) avg, O(log n) worst.
- **ConcurrentHashMap internals:** per-bucket `synchronized` head + CAS; lock-free reads.
- **Why HashMap not thread-safe?** Lost updates, corrupted structure under concurrency.
- **TreeMap vs HashMap:** sorted O(log n) vs unordered O(1).
- **ArrayList vs LinkedList:** random access vs end insert/remove.

### Free Links
- [GeeksforGeeks — HashMap internal working (free)](https://www.geeksforgeeks.org/internal-working-of-hashmap-java/)
- [Baeldung — ConcurrentHashMap (free)](https://www.baeldung.com/java-concurrent-hash-map)
- [Oracle — Collections Tutorial (free)](https://docs.oracle.com/javase/tutorial/collections/)
- [LeetCode — HashMap problems (free)](https://leetcode.com/problemset/all/?search=hashmap)

---

# 5. Java Memory Management

## Memory Areas
- **Heap:** objects; Young (Eden, S0, S1) + Old; GC-managed.
- **Stack:** per-thread frames; locals, calls.
- **Metaspace:** class metadata (native, auto-grows).
- **Native Memory:** off-heap (NIO, JNI, thread stacks).
- **Escape Analysis:** JIT may stack-allocate non-escaping objects.

## Garbage Collection
| Collector | Type | Use case |
|-----------|------|----------|
| Serial | STW, single | small apps |
| Parallel | STW, multi | throughput |
| CMS (dep.) | concurrent | legacy low-pause |
| G1 | region | balanced (default) |
| ZGC | sub-ms | huge heaps |
| Shenandoah | concurrent compaction | low-latency |

### Questions
- **Memory leak:** referenced-but-unused objects (static maps, unclosed resources, thread-locals).
- **OOM types:** heap space, Metaspace, GC overhead limit, unable to create native thread.
- **StackOverflowError:** deep/infinite recursion.

### Free Links
- [Oracle — GC Tuning Guide (free)](https://docs.oracle.com/en/java/javase/21/gctuning/)
- [Baeldung — Java Memory Leaks (free)](https://www.baeldung.com/java-memory-leaks)
- [OpenJDK — ZGC (free)](https://openjdk.org/projects/zgc/)
- [GeeksforGeeks — Garbage Collection (free)](https://www.geeksforgeeks.org/garbage-collection-java/)

---

# 6. JVM Internals

## Class Loading Lifecycle
1. **Loading** — load bytecode via classloader.
2. **Linking** — Verification, Preparation, Resolution.
3. **Initialization** — static initializers (`<clinit>`).

## Class Loaders
- **Bootstrap / Platform / Application.**
- **Parent Delegation:** delegate to parent first → no duplicate/core tampering.

## JIT Compiler
HotSpot compiles hot bytecode to native; tiered (C1 + C2).

### Questions
- **Parent Delegation:** protects core classes, avoids duplication.
- **Metaspace vs PermGen:** native, auto-resizing.
- **Bytecode:** stack-based, verified, platform-independent.

### Free Links
- [Baeldung — JVM Internals (free)](https://www.baeldung.com/jvm)
- [Oracle JVM Spec — Class Loading (free)](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-5.html)
- [GeeksforGeeks — JVM (free)](https://www.geeksforgeeks.org/jvm-works-jvm-architecture/)

---

# 7. Multithreading & Concurrency

## Building Blocks
- `Thread` / `Runnable` / `Callable<V>` / `Future` / `CompletableFuture` / `ExecutorService` / `ForkJoinPool`.

## Locks
- `synchronized` (monitor, reentrant), `ReentrantLock` (tryLock, fairness), `ReadWriteLock`, `StampedLock` (optimistic read).

## Concepts
- `volatile` (visibility, not atomicity), Atomic classes (CAS), **Happens-Before**, Deadlock, Starvation, Livelock.

## Concurrent Collections
- `ConcurrentHashMap`, `CopyOnWriteArrayList`, `BlockingQueue`.

### Questions
- **Producer-Consumer:**
```java
BlockingQueue<Integer> q = new LinkedBlockingQueue<>(10);
// producer: q.put(x) blocks if full; consumer: q.take() blocks if empty
```
- **Thread pool sizing:** CPU-bound `N+1`; I/O-bound `N*(1+wait/time)`.
- **CompletableFuture chain:**
```java
CompletableFuture.supplyAsync(() -> fetch())
  .thenApply(r -> transform(r))
  .thenCompose(r -> saveAsync(r))
  .exceptionally(ex -> fallback());
```
- **Deadlock prevention:** lock ordering, `tryLock(timeout)`, minimize scope.

### Free Links
- [Baeldung — Java Concurrency (free)](https://www.baeldung.com/java-concurrency)
- [Baeldung — CompletableFuture (free)](https://www.baeldung.com/java-completablefuture)
- [GeeksforGeeks — Producer-Consumer (free)](https://www.geeksforgeeks.org/producer-consumer-problem-in-java/)
- [LeetCode — Concurrency Problems (free)](https://leetcode.com/problemset/concurrency/)
- [LeetCode 1114 — Print in Order (free)](https://leetcode.com/problems/print-in-order/)

---

# 8. Java 8–21 Features

## Java 8
- **Lambda, Streams, Optional, Method Reference, Functional Interface** (`Predicate`, `Function`, `Supplier`, `Consumer`).

## Java 9+
- **Modules (JPMS).**

## Java 11
- **HTTP Client** (`java.net.http`): async, HTTP/2.

## Java 17 (LTS)
- **Records:** `record Point(int x, int y)` (auto accessors/equals/hashCode).
- **Sealed Classes:** `sealed interface Expr permits Const, Add`.

## Java 21 (LTS)
- **Virtual Threads:** `Thread.startVirtualThread(() -> ...)`; lightweight, millions, Project Loom.
- **Structured Concurrency:** `StructuredTaskScope`.

### Questions
- **Stream vs Parallel Stream:** parallel uses ForkJoinPool.commonPool; good for CPU-bound large data; avoid for I/O/small.
- **Optional best practices:** avoid `get()`; use `orElse`/`ifPresent`.
- **Virtual Threads:** don't pool; avoid `synchronized` pinning in hot paths (use `ReentrantLock`).

### Free Links
- [Baeldung — Java 8 Streams (free)](https://www.baeldung.com/java-8-streams)
- [OpenJDK — Project Loom / Virtual Threads (free)](https://openjdk.org/projects/loom/)
- [Oracle — Records (free docs)](https://docs.oracle.com/en/java/javase/21/language/records.html)
- [Oracle — Sealed Classes (free docs)](https://docs.oracle.com/en/java/javase/21/language/sealed-classes-and-interfaces.html)
- [GeeksforGeeks — Java 8 Features (free)](https://www.geeksforgeeks.org/java-8-features/)

---

# 9. Exception Handling

- **Checked** (compile-time), **Unchecked** (`RuntimeException`), **Error** (JVM failures).
- **try-with-resources:** auto-closes `AutoCloseable` in reverse order.
```java
try (Connection c = ds.getConnection();
     PreparedStatement ps = c.prepareStatement(sql)) { /* ... */ }
```
- **Custom Exceptions:** extend `Exception`/`RuntimeException`; preserve cause.

### Free Links
- [Oracle — Exceptions Tutorial (free)](https://docs.oracle.com/javase/tutorial/essential/exceptions/)
- [Baeldung — Java Exceptions (free)](https://www.baeldung.com/java-exceptions)
- [GeeksforGeeks — Exception Handling (free)](https://www.geeksforgeeks.org/exceptions-in-java/)

---

# 10. Generics

- **Type Erasure:** generic info removed at compile time (backward compat).
- **Wildcards:** `? extends T` (producer), `? super T` (consumer).
- **PECS:** Producer `extends`, Consumer `super`.
```java
void copy(List<? extends Number> src, List<? super Number> dest)
```
- **Generic Methods:** `public <T extends Comparable<T>> T max(List<T> l)`.

### Free Links
- [Oracle — Generics Tutorial (free)](https://docs.oracle.com/javase/tutorial/java/generics/)
- [Baeldung — Java Generics (free)](https://www.baeldung.com/java-generics)
- [GeeksforGeeks — Generics (free)](https://www.geeksforgeeks.org/generics-in-java/)

---

# 11. Reflection & Annotations

- **Reflection API:** `Class.forName`, `getDeclaredMethods`, `setAccessible(true)`; breaks encapsulation, slower.
- **Dynamic Proxy:** runtime interface proxies — basis for Spring AOP, RPC clients.
- **Annotation Processing:** `@Retention`, `@Target`; compile-time `AbstractProcessor` (Lombok, MapStruct).

### Free Links
- [Oracle — Reflection Tutorial (free)](https://docs.oracle.com/javase/tutorial/reflect/)
- [Baeldung — Java Reflection (free)](https://www.baeldung.com/java-reflection)
- [Baeldung — Annotations (free)](https://www.baeldung.com/java-annotations)

---

# 12. Serialization

- **Serializable:** marker; reflection-based (slow/fragile).
- **Externalizable:** `writeExternal`/`readExternal` (control, faster).
- **serialVersionUID:** prevents `InvalidClassException` on evolution.
- Prefer JSON / Protobuf / Avro for cross-system data.

### Free Links
- [Baeldung — Java Serialization (free)](https://www.baeldung.com/java-serialization)
- [GeeksforGeeks — Serialization (free)](https://www.geeksforgeeks.org/serialization-in-java/)
- [Oracle — Serializable (free docs)](https://docs.oracle.com/javase/8/docs/api/java/io/Serializable.html)

---

# 13. Design Patterns

## Creational
- **Singleton** (enum preferred), **Factory**, **Builder**, **Prototype**.
```java
enum Singleton { INSTANCE; }
```

## Structural
- **Adapter, Decorator, Composite, Facade, Proxy.**

## Behavioral
- **Strategy, Observer, State, Command, Template, Chain of Responsibility.**

### Free Links
- [Refactoring Guru — Design Patterns (free)](https://refactoring.guru/design-patterns)
- [Baeldung — Java Design Patterns (free)](https://www.baeldung.com/java-design-patterns)
- [GeeksforGeeks — Design Patterns (free)](https://www.geeksforgeeks.org/software-design-patterns/)
- [JournalDev — Singleton (free)](https://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples)

---

# 14. SOLID

- **SRP** — one reason to change.
- **OCP** — open for extension, closed for modification.
- **LSP** — subtypes substitutable.
- **ISP** — small, client-specific interfaces.
- **DIP** — depend on abstractions (DI).

### Free Links
- [Baeldung — SOLID Principles (free)](https://www.baeldung.com/solid-principles)
- [Refactoring Guru — SOLID (free)](https://refactoring.guru/design-patterns/solid-principles)
- [GeeksforGeeks — SOLID (free)](https://www.geeksforgeeks.org/solid-principle-in-java/)

---

# 15. Clean Code

- **Naming:** intention-revealing, no abbreviations.
- **Refactoring:** extract method, rename, replace magic numbers.
- **Code Smells:** long method, large class, duplication, feature envy.
- **Sonar Issues:** complexity, duplication, uncovered branches.
- Keep methods < ~20 lines, ≤3 params.

### Free Links
- [SourceMaking — Code Smells (free)](https://sourcemaking.com/refactoring/smells)
- [SonarQube — Java Rules (free)](https://rules.sonarsource.com/java)
- [GeeksforGeeks — Clean Code (free)](https://www.geeksforgeeks.org/clean-code-principles/)
- [Refactoring Guru — Refactoring (free)](https://refactoring.guru/refactoring)

---

# 16. Data Structures

- **Array, Linked List, Stack, Queue, Heap, Trie, Graph, Tree, Segment Tree, Fenwick (BIT), Union Find.**
- Union Find: `find`/`union` with path compression + union by rank → O(α(n)).

### Free Practice Links
- [LeetCode — Arrays (free)](https://leetcode.com/problemset/algorithms/?topicSlugs=array)
- [LeetCode — Linked List (free)](https://leetcode.com/problemset/algorithms/?topicSlugs=linked-list)
- [LeetCode — Trees (free)](https://leetcode.com/problemset/algorithms/?topicSlugs=tree)
- [LeetCode — Graphs (free)](https://leetcode.com/problemset/algorithms/?topicSlugs=graph)
- [LeetCode — Heap (free)](https://leetcode.com/problemset/algorithms/?topicSlugs=heap-priority-queue)
- [CP-Algorithms — Disjoint Set Union (free)](https://cp-algorithms.com/data_structures/disjoint_set_union.html)
- [VisuAlgo — Data Structure Visualizations (free)](https://visualgo.net/)

---

# 17. Algorithms

## Must Know
- **Binary Search** (incl. on answer space)
- **DFS / BFS**
- **Sliding Window**
- **Prefix Sum**
- **Greedy**
- **Dynamic Programming** (knapsack, LIS, edit distance)
- **Backtracking** (N-Queens)
- **Topological Sort**
- **Dijkstra / Floyd-Warshall**
- **Kruskal / Prim** (MST)
- **KMP / Rabin-Karp** (string matching)

## Practice
- 150–250 LeetCode Medium/Hard.

### Free Links
- [NeetCode 150 (free)](https://neetcode.io/)
- [LeetCode Top Interview 150 (free)](https://leetcode.com/studyplan/top-interview-150/)
- [Blind 75 (LeetCode, free)](https://leetcode.com/discuss/general-discussion/460599/blind-75-leetcode-questions)
- [GeeksforGeeks — DSA (free)](https://www.geeksforgeeks.org/data-structures/)
- [VisuAlgo — Algorithm Visualizations (free)](https://visualgo.net/)
- [LeetCode — Dynamic Programming (free)](https://leetcode.com/problemset/algorithms/?topicSlugs=dynamic-programming)

---

# 18. SQL

## Topics
- **Joins** (INNER/LEFT/RIGHT/FULL/CROSS/SELF)
- **Index** (B-Tree; composite leftmost-prefix)
- **GROUP BY / HAVING**
- **Window Functions** (`ROW_NUMBER`, `RANK`, `SUM() OVER`)
- **CTE** (`WITH`)
- **Views, Transactions, Locks**

## Questions
```sql
-- 2nd highest salary
SELECT MAX(salary) FROM emp
WHERE salary < (SELECT MAX(salary) FROM emp);

-- Running total
SELECT id, salary, SUM(salary) OVER (ORDER BY id) AS running FROM emp;

-- Top N per group
SELECT * FROM (
  SELECT *, ROW_NUMBER() OVER (PARTITION BY dept ORDER BY salary DESC) rn FROM emp
) t WHERE rn <= 3;
```

### Free Links
- [LeetCode — Database Problems (free)](https://leetcode.com/problemset/database/)
- [SQLZoo (free practice)](https://sqlzoo.net/)
- [Mode — SQL Tutorial / Window Functions (free)](https://mode.com/sql-tutorial/)
- [Use The Index, Luke — Indexing (free)](https://use-the-index-luke.com/)
- [GeeksforGeeks — SQL (free)](https://www.geeksforgeeks.org/sql-tutorial/)

---

# 19. Database Design

- **Normalization (1NF–3NF, BCNF)** vs **Denormalization** (read perf).
- **Partitioning** (range/hash/list) vs **Sharding** (horizontal across nodes).
- **Replication** (master-slave, multi-master).
- **CAP** (pick 2 under partition), **ACID**, **BASE**.

### Free Links
- [GeeksforGeeks — DBMS (free)](https://www.geeksforgeeks.org/DBMS/)
- [Wikipedia — CAP Theorem (free)](https://en.wikipedia.org/wiki/CAP_theorem)
- [MongoDB — Sharding Docs (free)](https://www.mongodb.com/docs/manual/sharding/)
- [Visual Paradigm — DB Design (free)](https://www.visual-paradigm.com/guide/data-modeling/)

---

# 20. Spring Framework

- **IoC:** framework controls object lifecycle.
- **Dependency Injection:** constructor (preferred) > setter > field (avoid).
- **Bean Lifecycle:** instantiate → populate → `@PostConstruct` → init → `@PreDestroy`.
- **AOP:** aspects/pointcuts/advice; dynamic proxy or CGLIB.

### Free Links
- [Spring Core — Official Docs (free)](https://docs.spring.io/spring-framework/reference/core.html)
- [Baeldung — Spring DI (free)](https://www.baeldung.com/spring-dependency-injection)
- [Baeldung — Spring AOP (free)](https://www.baeldung.com/spring-aop)
- [GeeksforGeeks — Spring Framework (free)](https://www.geeksforgeeks.org/spring-framework/)

---

# 21. Spring Boot

- **Auto-Configuration:** conditional bean wiring by classpath.
- **Profiles:** `application-{profile}.yml`.
- **Configuration:** `@ConfigurationProperties` (type-safe).
- **Starters, Validation (`@Valid`), Scheduling (`@Scheduled`), Async (`@Async`).**

### Free Links
- [Spring Boot Docs (free)](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Baeldung — Spring Boot (free)](https://www.baeldung.com/spring-boot)
- [Baeldung — Auto-configuration (free)](https://www.baeldung.com/spring-boot-auto-config)
- [Spring Initializr (free tool)](https://start.spring.io/)

---

# 22. REST APIs

- **REST Principles:** stateless, resource-oriented, uniform interface.
- **HATEOAS, Versioning** (URL/header/accept), **Pagination, Filtering, Caching, Idempotency.**

### Free Links
- [REST API Tutorial (free)](https://restfulapi.net/)
- [Microsoft — API Design (free)](https://learn.microsoft.com/en-us/azure/architecture/best-practices/api-design)
- [Stripe — Idempotency (free)](https://stripe.com/blog/idempotency)
- [Spring — HATEOAS (free)](https://spring.io/projects/spring-hateoas)

---

# 23. Security

- **OAuth2** (auth code flow, PKCE), **JWT** (stateless tokens, signature verification).
- **Spring Security** (filter chain, `SecurityFilterChain`).
- **CSRF** (state-changing POST), **CORS** (cross-origin allowlist).

### Free Links
- [Spring Security Docs (free)](https://spring.io/projects/spring-security)
- [OAuth2 RFC 6749 (free)](https://datatracker.ietf.org/doc/html/rfc6749)
- [JWT.io (free)](https://jwt.io/)
- [OWASP Top 10 (free)](https://owasp.org/www-project-top-ten/)
- [Baeldung — Spring Security (free)](https://www.baeldung.com/spring-security)

---

# 24. Microservices

- **Service Discovery** (Eureka, Consul), **API Gateway** (Spring Cloud Gateway).
- **Circuit Breaker** (Resilience4j), **Retry, Bulkhead.**
- **Distributed Transactions:** **Saga** (orchestration/choreography), **CQRS** (separate read/write models).

### Free Links
- [Microservices.io — Patterns (free)](https://microservices.io/)
- [Resilience4j Docs (free)](https://resilience4j.readme.io/)
- [Spring Cloud Docs (free)](https://spring.io/projects/spring-cloud)
- [Microsoft — Saga Pattern (free)](https://learn.microsoft.com/en-us/azure/architecture/reference-architectures/saga/saga)
- [Martin Fowler — CQRS (free)](https://martinfowler.com/bliki/CQRS.html)

---

# 25. Messaging Systems

- **Kafka, RabbitMQ, ActiveMQ.**
- **Consumer Groups, Partitions, Ordering, Exactly-Once, Retry, DLQ.**

### Free Links
- [Apache Kafka Docs (free)](https://kafka.apache.org/documentation/)
- [Confluent — Exactly-Once (free)](https://www.confluent.io/blog/exactly-once-semantics-are-possible-heres-how-apache-kafka-does-it/)
- [RabbitMQ Tutorials (free)](https://www.rabbitmq.com/tutorials)
- [Baeldung — Spring Kafka (free)](https://www.baeldung.com/spring-apache-kafka)
- [AWS — Dead Letter Queues (free)](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-dead-letter-queues.html)

---

# 26. Redis

- **Cache** (cache-aside, write-through), **TTL**, **Pub/Sub**, **Distributed Lock** (`SET NX PX` / Redlock).
- Data types: String, Hash, List, Set, Sorted Set, Streams.

### Free Links
- [Redis Docs (free)](https://redis.io/docs/latest/)
- [Redis — Caching Patterns (free)](https://redis.io/docs/latest/develop/use/patterns/)
- [Redis — Distributed Locks / Redlock (free)](https://redis.io/docs/latest/develop/reference/patterns/distributed-locks/)
- [Baeldung — Redis with Spring (free)](https://www.baeldung.com/spring-data-redis-tutorial)

---

# 27. ElasticSearch

- **Index, Mapping, Analyzer** (tokenizer/filter), **Query DSL** (bool, match, term, range, aggregations).

### Free Links
- [Elasticsearch Guide (free)](https://www.elastic.co/guide/index.html)
- [Elastic — Query DSL (free)](https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl.html)
- [Baeldung — Elasticsearch (free)](https://www.baeldung.com/elasticsearch)
- [Elastic — Analyzers (free)](https://www.elastic.co/guide/en/elasticsearch/reference/current/analyzer.html)

---

# 28. Docker

- **Dockerfile** (layered, cache-friendly), **Volumes** (persistence), **Networking** (bridge/host), **Multi-stage Build** (small final image).
```dockerfile
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN ./gradlew build

FROM eclipse-temurin:21-jre
COPY --from=build /app/build/libs/app.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
```

### Free Links
- [Dockerfile Reference (free)](https://docs.docker.com/reference/dockerfile/)
- [Docker — Multi-stage Builds (free)](https://docs.docker.com/build/building/multi-stage/)
- [Docker Curriculum (free)](https://docker-curriculum.com/)
- [Baeldung — Dockerizing Spring Boot (free)](https://www.baeldung.com/dockerizing-spring-boot-application)

---

# 29. Kubernetes

- **Pod, Deployment, ReplicaSet, Service, Ingress, ConfigMap, Secret, HPA** (Horizontal Pod Autoscaler).

### Free Links
- [Kubernetes Docs (free)](https://kubernetes.io/docs/home/)
- [Kubernetes by Example (free)](https://kubernetesbyexample.com/)
- [K8s — Horizontal Pod Autoscaler (free)](https://kubernetes.io/docs/tasks/run-application/horizontal-pod-autoscale/)
- [Baeldung — Kubernetes (free)](https://www.baeldung.com/kubernetes)

---

# 30. Linux

## Commands
- `grep` (search), `awk` (column processing), `sed` (stream edit), `top`/`htop` (CPU/mem), `ps` (processes), `netstat`/`ss` (sockets), `lsof` (open files), `curl` (HTTP).

### Examples
```bash
# Top CPU processes
ps -eo pid,ppid,cmd,%mem,%cpu --sort=-%cpu | head

# Find listening port
ss -ltnp | grep :8080

# Count 5xx in access log
grep -oE '" [0-9]{3} ' access.log | grep -E '" 5[0-9]{2} ' | wc -l
```

### Free Links
- [Linux Journey (free)](https://linuxjourney.com/)
- [GNU — Coreutils Manual (free)](https://www.gnu.org/software/coreutils/manual/coreutils.html)
- [ExplainShell (free tool)](https://explainshell.com/)
- [GeeksforGeeks — Linux Commands (free)](https://www.geeksforgeeks.org/linux-commands/)

---

# 31. Networking

- **HTTP/HTTPS/TLS, TCP/UDP, DNS, Load Balancer.**
- TCP 3-way handshake, flow/congestion control; UDP connectionless; TLS handshake (cert, key exchange).

### Free Links
- [MDN — HTTP Overview (free)](https://developer.mozilla.org/en-US/docs/Web/HTTP/Overview)
- [Cloudflare — How HTTPS/TLS works (free)](https://www.cloudflare.com/learning/ssl/what-is-https/)
- [GeeksforGeeks — TCP vs UDP (free)](https://www.geeksforgeeks.org/difference-between-tcp-and-udp/)
- [AWS — What is a Load Balancer (free)](https://aws.amazon.com/what-is/load-balancing/)

---

# 32. High Level System Design

## Design Problems
- URL Shortener, WhatsApp, Uber, Netflix, YouTube, Twitter, Payment Gateway, Rate Limiter, Notification Service, Search Engine, Hotel Booking, Food Delivery.

## Concepts
- **Scalability, Availability, Consistency, CAP, CDN, Queue, Cache, Database, Storage.**

### Free Links
- [System Design Primer (GitHub, free)](https://github.com/donnemartin/system-design-primer)
- [GeeksforGeeks — System Design (free)](https://www.geeksforgeeks.org/system-design-tutorial/)
- [LeetCode — System Design (free articles)](https://leetcode.com/discuss/interview-question/system-design)
- [Excalidraw (free diagram tool)](https://excalidraw.com/)
- [High Scalability (free blog)](http://highscalability.com/)

---

# 33. Low Level Design

## Design Problems
- Parking Lot, Elevator, BookMyShow, Splitwise, Chess, ATM, Library, Cab Booking.

### Free Links
- [Refactoring Guru — Design Patterns (free)](https://refactoring.guru/design-patterns)
- [GeeksforGeeks — LLD / OOD (free)](https://www.geeksforgeeks.org/object-oriented-analysis-and-design/)
- [GitHub — LLD interview problems (free)](https://github.com/prasadgujar/low-level-design)
- [Baeldung — OOP (free)](https://www.baeldung.com/java-oop)

---

# 34. Cloud

## AWS
- **EC2, ECS, EKS, Lambda, S3, RDS, DynamoDB, CloudWatch.**

### Free Links
- [AWS — Free Tier (free)](https://aws.amazon.com/free/)
- [AWS Documentation (free)](https://docs.aws.amazon.com/)
- [AWS — Well-Architected (free)](https://aws.amazon.com/architecture/well-architected/)
- [GeeksforGeeks — AWS (free)](https://www.geeksforgeeks.org/aws-2/)

---

# 35. CI/CD

- **Jenkins, GitHub Actions, GitLab CI, ArgoCD.**

### Free Links
- [GitHub Actions Docs (free)](https://docs.github.com/en/actions)
- [GitLab CI/CD Docs (free)](https://docs.gitlab.com/ee/ci/)
- [Jenkins Docs (free)](https://www.jenkins.io/doc/)
- [ArgoCD Docs (free)](https://argo-cd.readthedocs.io/)
- [GeeksforGeeks — CI/CD (free)](https://www.geeksforgeeks.org/ci-cd/)

---

# 36. Testing

- **JUnit, Mockito, Integration Testing, Contract Testing, TestContainers.**

### Free Links
- [JUnit 5 Docs (free)](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Docs (free)](https://site.mockito.org/)
- [Testcontainers (free)](https://testcontainers.com/)
- [Baeldung — Testing (free)](https://www.baeldung.com/spring-boot-testing)
- [GeeksforGeeks — JUnit (free)](https://www.geeksforgeeks.org/junit-tutorial/)

---

# 37. Observability

- **Prometheus, Grafana, ELK (Elasticsearch/Logstash/Kibana), OpenTelemetry, Jaeger.**

### Free Links
- [Prometheus Docs (free)](https://prometheus.io/docs/)
- [Grafana Docs (free)](https://grafana.com/docs/)
- [OpenTelemetry Docs (free)](https://opentelemetry.io/docs/)
- [Jaeger Docs (free)](https://www.jaegertracing.io/docs/)
- [Elastic — ELK Stack (free)](https://www.elastic.co/elastic-stack)

---

# 38. Performance Tuning

- **Profiling, GC Tuning, Heap Dump, Thread Dump, JMH.**
- Tools: VisualVM, async-profiler (free), jstack, jmap.

### Free Links
- [OpenJDK — async-profiler (free)](https://github.com/async-profiler/async-profiler)
- [Oracle — JMH (free)](https://openjdk.org/projects/code-tools/jmh/)
- [Baeldung — Heap/Thread Dumps (free)](https://www.baeldung.com/java-heap-dump)
- [VisualVM (free)](https://visualvm.github.io/)
- [GeeksforGeeks — JVM Performance (free)](https://www.geeksforgeeks.org/java-virtual-machine-jvm-performance-optimization/)

---

# 39. Behavioral Interview

## Prepare STAR stories for:
- Conflict, Failure, Ownership, Leadership, Mentoring, Scaling, Innovation, Difficult Customer, Production Issue.

**STAR = Situation, Task, Action, Result.** Quantify impact (%, ms, $ saved, users).

### Free Links
- [Tech Interview Handbook — Behavioral (free)](https://www.techinterviewhandbook.org/behavioral-interview/)
- [Exponent — Behavioral Questions (free)](https://www.tryexponent.com/questions/behavioral)
- [STAR Method (free)](https://www.indeed.com/career-advice/interviewing/star-interview-questions)

---

# 40. Machine Coding

## Practice
- Snake Game, Chess, Parking Lot, Splitwise, ATM, Logger, File System.

### Free Links
- [GitHub — Machine Coding problems (free)](https://github.com/prasadgujar/low-level-design)
- [LeetCode — Design Problems (free)](https://leetcode.com/problemset/all/?search=design)
- [Refactoring Guru — Patterns for LLD (free)](https://refactoring.guru/design-patterns)
- [GeeksforGeeks — OOD (free)](https://www.geeksforgeeks.org/object-oriented-analysis-and-design/)

---

# 41. Resume Preparation

## Should demonstrate
- **Scale, Performance Improvements, Cost Optimization, Leadership, Architecture, Metrics.**

### Example
> "Reduced API latency by 42% serving 18M requests/day."

### Free Links
- [Tech Interview Handbook — Resume (free)](https://www.techinterviewhandbook.org/resume/)
- [Exponent — Resume Tips (free)](https://www.tryexponent.com/blog/software-engineer-resume)
- [FAANG Tech Leads — Resume Guide (free)](https://www.faangtechleads.com/)

---

# 42. Revision Checklist

## Java
- [ ] Core Java
- [ ] Collections
- [ ] JVM
- [ ] GC
- [ ] Multithreading
- [ ] Java 8+
- [ ] Design Patterns

## Spring
- [ ] Boot
- [ ] Security
- [ ] Data JPA
- [ ] REST

## Database
- [ ] SQL
- [ ] Index
- [ ] Transactions

## Backend
- [ ] Redis
- [ ] Kafka
- [ ] Docker
- [ ] Kubernetes

## Coding
- [ ] Arrays
- [ ] Strings
- [ ] Trees
- [ ] Graphs
- [ ] DP
- [ ] Heap

## Design
- [ ] HLD
- [ ] LLD

## Behavioral
- [ ] STAR Stories
- [ ] Leadership
- [ ] Ownership

---

# Suggested Preparation Timeline (8 Weeks)

**Week 1** — Core Java, OOP, Collections
**Week 2** — JVM, Multithreading, Java 8+
**Week 3** — SQL, Spring Boot, REST
**Week 4** — Redis, Kafka, Docker
**Week 5** — Kubernetes, Microservices, Security
**Week 6** — DSA Revision, Machine Coding
**Week 7** — Low Level Design, High Level Design
**Week 8** — Mock Interviews, Resume Review, Behavioral Practice

---

# Recommended Free Practice

**Coding**
- [LeetCode Top 150 (free)](https://leetcode.com/studyplan/top-interview-150/)
- [NeetCode 150 (free)](https://neetcode.io/)

**System Design**
- [System Design Primer (free)](https://github.com/donnemartin/system-design-primer)
- [GeeksforGeeks — System Design (free)](https://www.geeksforgeeks.org/system-design-tutorial/)

**Machine Coding**
- [GitHub — LLD problems (free)](https://github.com/prasadgujar/low-level-design)

**Mock Interviews**
- [Pramp — Free Peer Mock Interviews](https://www.pramp.com/)
- [Exponent — Free Mocks](https://www.tryexponent.com/)

---

# Final Advice

An SDE3 interview is less about remembering APIs and more about demonstrating:

- Strong Java fundamentals
- Deep JVM knowledge
- Concurrency expertise
- Scalable backend design
- Tradeoff analysis
- Leadership and ownership
- Clear communication
  </content>