# Messaging Systems — Interview Q&A

## 1. What is the difference between Kafka and RabbitMQ?
**Answer:** **Kafka** is a distributed, partitioned, append-only **log** (pub-sub, high throughput, replayable, at-least/exactly-once). **RabbitMQ** is a broker with rich **routing** (exchanges, queues, bindings), push model, good for complex routing and lower latency. Kafka scales better for streams; RabbitMQ for task queues.

## 2. What is a consumer group in Kafka?
**Answer:** A **consumer group** is a set of consumers sharing a topic's partitions — each partition is consumed by exactly one member, enabling **parallel processing** and horizontal scaling. Adding consumers beyond partition count leaves some idle. Rebalancing redistributes partitions on membership change.

## 3. What is the difference between a queue and a topic?
**Answer:** A **queue** (point-to-point) delivers each message to **one** consumer (competing consumers). A **topic** (pub-sub) delivers each message to **all** subscribed consumers/groups. RabbitMQ = queues; Kafka = topics+partitions.

## 4. What does "exactly-once" semantics mean and how is it achieved?
**Answer:** Each message is processed **exactly once** — no duplicates, no loss. In Kafka it's achieved via **idempotent producers** (enable.idempotence), **transactional** writes, and **offset commits with processing** in one transaction. True exactly-once is hard; often "effectively once" via idempotency.

## 5. What is a Dead Letter Queue (DLQ)?
**Answer:** A DLQ holds messages that repeatedly fail processing (poison messages) so they don't block the main queue. Consumers route failures there after N retries. Enables inspection/retry without losing data or stalling throughput.

## 6. How do you guarantee message ordering?
**Answer:** In Kafka, ordering is per-**partition** — messages with the same key go to the same partition (ordered). For global order you'd need a single partition (limits throughput). RabbitMQ preserves FIFO per queue. Design keys so related messages share a partition.

## 7. What is the difference between at-most-once, at-least-once, and exactly-once?
**Answer:** **At-most-once** — may lose messages (ack before processing). **At-least-once** — no loss but possible duplicates (ack after processing; crash → redelivery). **Exactly-once** — no loss, no dupes (idempotency + transactions). Tradeoff: stronger guarantees = more overhead.

## 8. What is a message broker and why use one?
**Answer:** A broker (Kafka, RabbitMQ, ActiveMQ) mediates async message exchange, **decoupling** producers/consumers, buffering load (backpressure), enabling retries, and supporting pub-sub. It improves resilience and scalability vs direct sync calls.

## 9. How do you handle poison messages?
**Answer:** Detect repeated failures (retry with backoff), then route to a **DLQ** after a threshold. Log and alert. Optionally add a "retry topic" with delayed reprocessing. Never let a bad message loop forever and block the partition.

## 10. What is the difference between push and pull consumption?
**Answer:** **Push** (RabbitMQ) — broker delivers messages to consumers (low latency, but harder to control rate). **Pull** (Kafka consumers fetch) — consumers control pace (better backpressure, replay). Kafka's pull model suits high-throughput batching.

## 11. What is idempotency in consumers and how do you implement it?
**Answer:** Consumers may receive duplicates (at-least-once). Make processing **idempotent**: dedupe by message ID (store processed IDs in a DB/Redis with a TTL), or design updates as upserts. This makes redelivery safe.

## 12. What is partitioning in Kafka and why does it matter?
**Answer:** A topic is split into **partitions** — the unit of parallelism and ordering. More partitions = more throughput and consumer parallelism, but more open files/overhead and slower rebalances. Choose based on target throughput and consumer count.