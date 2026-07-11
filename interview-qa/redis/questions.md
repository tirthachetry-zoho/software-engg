# Redis — Interview Q&A

## 1. What are Redis data types?
**Answer:** **String** (KV, counters), **Hash** (field-value map, objects), **List** (linked, queue/stack), **Set** (unique, union/inter), **Sorted Set** (ZSET, score-ranked, leaderboards), **Streams** (append-only log, like Kafka-lite), plus Bitmaps, HyperLogLog, Geospatial. Choose by access pattern.

## 2. What is the difference between cache-aside, write-through, and write-back?
**Answer:** **Cache-aside** (lazy) — app reads cache, on miss loads from DB and fills cache (most common). **Write-through** — write to cache AND DB together (consistent, slower writes). **Write-back** — write to cache only, flush to DB later (fast, risk of loss).

## 3. How do you implement a distributed lock with Redis?
**Answer:** Use `SET key value NX PX 30000` (set if absent, with TTL) to acquire; release only if you own it (Lua script comparing value) to avoid deleting another's lock. **Redlock** is the multi-node algorithm for higher safety; simpler single-node locks suffice for many cases.

## 4. What is the difference between Redis persistence RDB and AOF?
**Answer:** **RDB** snapshots at intervals — fast restart, compact, but loses recent data. **AOF** logs every write — durable (fsync options), larger, slower restart. Can combine both. Choose RDB for speed/caching, AOF for durability.

## 5. How do you prevent cache stampede / thundering herd?
**Answer:** When a hot key expires, many requests hit DB simultaneously. Mitigate with: **single-flight** (one request refreshes, others wait), **jittered TTLs** (stagger expiry), **request coalescing**, or serving **stale-while-revalidate** data.

## 6. What is cache penetration, breakdown, and avalanche?
**Answer:** **Penetration** — querying non-existent keys (DB hit every time) → guard with **bloom filter** or cache nulls. **Breakdown** — one hot key expires → use lock/single-flight. **Avalanche** — many keys expire at once → use **jittered TTLs**.

## 7. How do you build a leaderboard with Redis?
**Answer:** Use a **Sorted Set (ZSET)**: `ZADD leaderboard <score> <userId>`, `ZREVRANK` for rank, `ZREVRANGE` for top-N, `ZINCRBY` to update. O(log n) updates, O(1) rank lookups — ideal for real-time rankings.

## 8. What is Pub/Sub in Redis?
**Answer:** `PUBLISH`/`SUBSCRIBE` to channels for fire-and-forget messaging. Lightweight but **non-persistent** (no replay, subscribers miss messages sent while offline). For durable streams use **Redis Streams** (consumer groups, ACKs).

## 9. What is the difference between Redis and a relational cache?
**Answer:** Redis is an in-memory KV store (sub-ms, rich types, TTL) — great for caching, sessions, queues. A DB query cache (e.g., Hibernate 2nd-level) caches query results but is tied to the DB and slower. Redis is general-purpose and standalone.

## 10. How do you handle Redis eviction when memory is full?
**Answer:** Configure `maxmemory-policy`: `noeviction` (error on writes), `allkeys-lru`/`volatile-lru` (evict least-recently-used), `allkeys-lfu` (least-frequently-used), `volatile-ttl` (shortest TTL). Choose LRU/LFU for caches; ensure keys have TTLs for volatile-* policies.

## 11. What is the difference between Redis Cluster and Sentinel?
**Answer:** **Sentinel** provides HA (monitoring + automatic failover) for a single master-replica setup — no sharding. **Cluster** provides **sharding** (data split across nodes via hash slots) + failover — for horizontal scaling of large datasets.

## 12. How do you use Redis for rate limiting?
**Answer:** **Fixed window**: `INCR` + `EXPIRE` per key (simple, bursty at boundaries). **Sliding window** with sorted sets (precise). **Token bucket** via Lua. Common pattern: `INCR ip:cnt; EXPIRE ip:cnt 60` then reject if > limit — fast and atomic with Lua.