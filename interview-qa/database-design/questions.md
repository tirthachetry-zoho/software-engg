# Database Design — Interview Q&A

## 1. What are the normal forms (1NF–3NF, BCNF)?
**Answer:** **1NF** — atomic values, no repeating groups. **2NF** — 1NF + no partial dependency (full dependency on the whole PK). **3NF** — 2NF + no transitive dependency (non-key depends only on key). **BCNF** — every determinant is a candidate key. Higher forms reduce redundancy/anomalies.

## 2. When would you denormalize?
**Answer:** For **read-heavy** workloads where join cost hurts latency. Denormalization (duplicated/embedded data) trades storage + write complexity for faster reads (e.g., precomputed counters, materialized views). Common in analytics and caching layers.

## 3. What is the difference between partitioning and sharding?
**Answer:** **Partitioning** splits one table within a single DB (range/hash/list) for manageability/performance. **Sharding** distributes data across *multiple* database nodes (horizontal scaling). Sharding is cross-machine; partitioning is within a DB instance.

## 4. Explain replication and its types.
**Answer:** Replication copies data to replicas. **Master-Slave** (one writer, read replicas) — scales reads, not writes. **Multi-Master** (multiple writers) — higher availability, needs conflict resolution. **Synchronous** (strong consistency, higher latency) vs **Asynchronous** (eventual consistency, faster).

## 5. What is the CAP theorem?
**Answer:** In a distributed system facing a network partition, you can guarantee only **two** of: **C**onsistency (all see latest data), **A**vailability (every request gets a response), **P**artition tolerance (works despite network splits). Since partitions are inevitable, the real choice is CP vs AP.

## 6. What is the difference between ACID and BASE?
**Answer:** **ACID** (SQL) — strong consistency, transactions (Atomicity, Consistency, Isolation, Durability). **BASE** (NoSQL, e.g., DynamoDB) — **B**asically **A**vailable, **S**oft state, **E**ventual consistency. BASE favors availability and partition tolerance over strict consistency.

## 7. How do you design a many-to-many relationship?
**Answer:** Use a **junction/associative table** with foreign keys to both entities (e.g., `student_course(student_id, course_id)` with a composite PK). This avoids duplication and models the relationship cleanly with its own attributes if needed.

## 8. What is a surrogate vs natural primary key?
**Answer:** **Natural** key is meaningful in the domain (e.g., email, SSN) — can change, may be wide. **Surrogate** (auto-increment/UUID) is system-generated, stable, narrow, and recommended for joins and index performance. Use surrogate PKs; keep natural keys as unique constraints.

## 9. What is an index and what are its tradeoffs?
**Answer:** An index (B-Tree/hash) speeds reads but adds **write overhead** (maintained on INSERT/UPDATE/DELETE) and **storage**. Too many indexes slow writes; too few slow reads. Choose based on query patterns; monitor with the query planner.

## 10. How do you handle a hot row / write contention?
**Answer:** Options: break the hot row into multiple rows (e.g., counter sharding), use optimistic concurrency, batch writes, move counters to an append-only log + async aggregation, or use a different store (Redis) for high-frequency counters. Avoid single-row update bottlenecks.

## 11. What is a covering index?
**Answer:** An index that contains **all columns** needed by a query, so the DB satisfies it from the index alone without touching the table (no bookmark lookup). Great for read performance; created via composite indexes ordered by query needs.

## 12. How do you choose between SQL and NoSQL?
**Answer:** Use **SQL** for structured data, complex queries, transactions, and strong consistency (finance, ERP). Use **NoSQL** (document/key-value/column/graph) for flexible schemas, massive scale, high write throughput, and eventual consistency (logs, feeds, IoT). Often a polyglot approach.