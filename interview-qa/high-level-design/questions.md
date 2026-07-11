# High-Level System Design — Interview Q&A

## 1. How do you approach a system design interview?
**Answer:** (1) Clarify requirements & scale (QPS, data size, latency). (2) Estimate capacity (read/write ratio, storage). (3) Sketch high-level components (clients, LB, services, DB, cache, queue). (4) Deep-dive key parts (data model, scaling, tradeoffs). (5) Identify bottlenecks & failure modes. Communicate throughout.

## 2. Design a URL Shortener (e.g., bit.ly).
**Answer:** Hash/encode the long URL to a short key (base62 of an auto-increment or hash). Store mapping in a DB (or KV like DynamoDB/Redis). Serve via a redirect (301/302). Scale with caching (Redis), partitioning by key, and async analytics. Handle collisions, expiry (TTL), and custom aliases.

## 3. How would you design a rate limiter?
**Answer:** Algorithms: **Token Bucket** (refill tokens, allows bursts), **Leaky Bucket** (steady outflow), **Sliding Window** (precise). Implement at the API gateway/edge using Redis (`INCR`+TTL or sorted-set sliding window). Decide per-user/IP, return `429` when exceeded. Store counters in a fast KV.

## 4. Design a notification service.
**Answer:** Producers publish events to a **message queue** (Kafka). Consumers per channel (email/SMS/push) process with templates and provider integrations. Use **idempotency** (event ID) to avoid dupes, retries with DLQ, preferences/opt-out store, and a template service. Decouples senders from channels.

## 5. How do you design a scalable key-value / caching layer?
**Answer:** Put a **distributed cache** (Redis Cluster) in front of the DB. Use **cache-aside**; handle penetration (bloom filter), breakdown (single-flight), avalanche (jittered TTL). Shard the cache by key hash. For multi-region, replicate or use a global store (DynamoDB).

## 6. Design a pastebin / file-sharing service.
**Answer:** Client uploads content → generate a unique ID (UUID/base62) → store in object storage (S3) + metadata in DB (ID, size, expiry). Return a short URL. Serve via CDN for downloads. Handle large files via multipart upload, expiry via TTL/cleanup job, and access control via signed URLs.

## 7. How would you design a chat / messaging system?
**Answer:** WebSocket/gRPC streaming for real-time. Messages persisted in a DB (partitioned by conversation), fanned out via a queue to online users' push servers. Use **read receipts**, last-seen, and offline delivery (pull on connect). Scale with sharded connection servers and a presence service.

## 8. Design a payment gateway (simplified).
**Answer:** API receives a payment request → **idempotency key** prevents double-charge → validate → call PSP (Stripe) via circuit breaker → update ledger (DB transaction) → publish events (outbox) for settlement/receipts. Strong consistency for the ledger; async for notifications. Reconciliation job vs PSP.

## 9. How do you design for high availability?
**Answer:** Eliminate SPOFs (multi-AZ, multi-region), use **load balancers** + health checks, replicate data (DB replicas, cache replicas), deploy with **rolling/blue-green** updates, and design for graceful degradation (circuit breakers, fallbacks). Target N+1 redundancy and define RTO/RPO.

## 10. Design a search engine / typeahead (autocomplete).
**Answer:** Index documents in **ElasticSearch** (or a Trie for prefix). For typeahead, use a **Trie** or ES `prefix`/`completion` suggester, cached hot queries in Redis. Rank by frequency/recency. Scale ES with shards/replicas; precompute top suggestions offline.

## 11. How would you design a video/photo sharing feed (Twitter/Instagram)?
**Answer:** **Fan-out-on-write** (push to followers' feeds) for celebrities vs **fan-out-on-read** (pull) for active users — hybrid. Store feed in a wide-column/KV store. Use CDN for media, caching for hot feeds, and async generation. Handle celebrity spikes with celebrity-specific pull.

## 12. How do you handle consistency vs availability tradeoffs?
**Answer:** Use **CAP** thinking: under partition, choose CP (strong consistency, e.g., payments) or AP (availability, e.g., feeds). Apply **quorum** reads/writes (R+W>N) for tunable consistency. Use eventual consistency + idempotency where strict consistency isn't required.