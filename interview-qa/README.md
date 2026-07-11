# Interview Q&A — Folder-wise

Interview-ready **Question & Answer** sets for an SDE3 / Senior Java Backend Engineer loop, organized **folder-wise** (one folder per topic). Each folder contains a `questions.md` with **12 Q&A** (10–20 range as requested).

> Companion to `SDE3-guide.md`. Use these as spoken drills: read the question, answer aloud, then compare.

## How to use
1. Pick a topic folder below.
2. Open `questions.md`.
3. Answer each question out loud, then check the **Answer**.
4. Mark weak topics; revisit weekly. Pair with mock interviews.

## Topic Index (42 folders)

### Java Fundamentals
- `core-java/` — JDK/JRE/JVM, String, equals/hashCode, overloading/overriding
- `oop/` — pillars, composition vs inheritance, LSP, DRY/KISS/YAGNI
- `collections/` — HashMap internals, ConcurrentHashMap, fail-fast/safe, TreeMap
- `memory-management/` — heap gens, GC collectors, OOM, memory leaks
- `jvm-internals/` — class loading, parent delegation, JIT, tiered compilation
- `concurrency/` — synchronized vs lock, volatile/atomic, CompletableFuture, deadlocks
- `java-8-21/` — lambdas, streams, Optional, records, virtual threads
- `exceptions/` — checked/unchecked, try-with-resources, custom exceptions
- `generics/` — type erasure, PECS, wildcards, bounded types
- `reflection-annotations/` — Reflection API, dynamic proxies, retention, annotation processing
- `serialization/` — Serializable vs Externalizable, serialVersionUID, transient

### Design & Code Quality
- `design-patterns/` — creational/structural/behavioral, Singleton, Strategy, Observer
- `solid/` — SRP, OCP, LSP, ISP, DIP
- `clean-code/` — naming, code smells, function size, boy-scout rule

### DSA (LeetCode problems)
- `data-structures/` — arrays, linked lists, stacks, heaps, trees, graphs, tries (LeetCode #1, #206, #239, #347, #200, #208…)
- `algorithms/` — binary search, sliding window, DP, backtracking, graphs, MST (LeetCode #33, #3, #70, #322, #51, #207, #1584…)

### Data & Backend
- `sql/` — joins, window functions, indexes, transactions, isolation
- `database-design/` — normalization, sharding, CAP, ACID vs BASE
- `spring-framework/` — IoC, DI, bean lifecycle, AOP, scopes
- `spring-boot/` — auto-config, profiles, @ConfigurationProperties, @Transactional
- `rest-apis/` — REST constraints, idempotency, pagination, HATEOAS, status codes
- `security/` — OAuth2, JWT, CSRF/CORS, OWASP, password hashing
- `microservices/` — service discovery, circuit breaker, Saga, CQRS, outbox
- `messaging/` — Kafka vs RabbitMQ, consumer groups, DLQ, exactly-once
- `redis/` — data types, cache patterns, distributed lock, eviction
- `elasticsearch/` — index/mapping, text vs keyword, Query DSL, aggregations

### Infra & Ops
- `docker/` — images vs containers, layering, multi-stage, volumes
- `kubernetes/` — Pod, Deployment, Service, probes, HPA, StatefulSet
- `linux/` — ps/ss/grep/awk, kill signals, permissions, fd
- `networking/` — TCP/UDP, TLS, DNS, load balancers, HTTP versions
- `cloud/` — EC2/ECS/EKS/Lambda, S3, RDS/DynamoDB, IAM, VPC
- `ci-cd/` — CI vs CD, GitOps, blue-green/canary, secrets
- `testing/` — testing pyramid, JUnit/Mockito, Testcontainers, contract tests
- `observability/` — metrics/logs/traces, Prometheus, OTel, SLO/SLI/SLA
- `performance-tuning/` — profiling, heap/thread dumps, JMH, GC tuning

### Interviews & Career
- `high-level-design/` — URL shortener, rate limiter, notification, feeds, CAP tradeoffs
- `low-level-design/` — Parking Lot, Elevator, Chess, ATM, LRU cache
- `behavioral/` — STAR, leadership, conflict, failure, mentoring
- `machine-coding/` — LRU cache, rate limiter, logger, snake game, file system
- `resume/` — impact bullets, XYZ formula, quantification, tailoring
- `revision-checklist/` — 8-week plan, high-yield topics, mindset

## Counts
- **41 topic folders**, each with a `questions.md` (most have **12 Q&A**, the two DSA folders have **15** each) = **498 questions** total.
- DSA folders (`data-structures/`, `algorithms/`) use **real LeetCode problem numbers** as requested (e.g., #1, #206, #239, #347, #200, #208, #33, #3, #70, #322, #51, #207, #1584).
