# Microservices — Interview Q&A

## 1. What are the benefits and drawbacks of microservices?
**Answer:** **Pros:** independent deployability, tech diversity, fault isolation, team autonomy, scalability. **Cons:** distributed complexity, network latency, data consistency, ops overhead, harder testing. Not free — monolith-first is often wise.

## 2. What is service discovery?
**Answer:** A mechanism for services to find each other's network locations dynamically. **Client-side** (Eureka, Consul) — client queries a registry. **Server-side** (K8s Service, ALB) — load balancer resolves. Avoids hardcoding host:port.

## 3. What is an API Gateway and what does it do?
**Answer:** A single entry point that handles **routing**, auth, rate limiting, caching, request aggregation, and TLS termination (e.g., Spring Cloud Gateway, Kong). It decouples clients from internal service topology and centralizes cross-cutting concerns.

## 4. What is a circuit breaker and how does it work?
**Answer:** A resilience pattern (Resilience4j) that **trips** after N consecutive failures, short-circuiting calls for a cooldown period (OPEN → HALF_OPEN → CLOSED). Prevents cascading failures and gives the downstream time to recover. Pair with fallback responses.

## 5. What is the difference between orchestration and choreography (Saga)?
**Answer:** **Orchestration** — a central coordinator (Saga orchestrator) tells each service what to do (easier to track, single point). **Choreography** — services react to events, no central brain (decoupled, but harder to reason about). Saga maintains distributed transaction consistency via compensating actions.

## 6. What is the Saga pattern?
**Answer:** A sequence of local transactions where each step has a **compensating** action to undo on failure (since no distributed ACID txn). E.g., Book→Pay→Ship; if Ship fails, compensate Pay (refund) and Book (cancel). Achieves eventual consistency across services.

## 7. What is CQRS?
**Answer:** Command Query Responsibility Segregation — **separate write (command)** and **read (query)** models. Writes go to a normalized store; reads use a denormalized, optimized view (often via events). Improves performance/scalability and lets each side scale independently.

## 8. How do you handle distributed transactions?
**Answer:** Avoid 2PC (blocking, fragile). Use **Saga** (compensating transactions) for business workflows, **outbox pattern** (write DB + event in one txn, then publish) for reliable event delivery, and **eventual consistency** with idempotent consumers.

## 9. What is the difference between monolith and microservices?
**Answer:** Monolith = single deployable, shared DB, in-process calls (simple, but scaling/coupling limits). Microservices = multiple deployables, per-service data, network calls (autonomous, but distributed complexity). Choose by team size, scale, and domain boundaries (DDD).

## 10. What is the outbox pattern?
**Answer:** To reliably publish domain events without 2PC: write the business change **and** the event to the DB in one transaction, then a relay publishes the event to the broker and marks it sent. Guarantees "DB committed ⟺ event published" — no lost updates.

## 11. How do you handle inter-service communication?
**Answer:** **Synchronous** (REST/gRPC) for request-response (simple, but tight coupling, latency). **Asynchronous** (Kafka/RabbitMQ) for events (decoupled, resilient, eventual consistency). Prefer async for workflows; sync for immediate reads. Use timeouts + circuit breakers on sync calls.

## 12. What is bulkhead and retry pattern?
**Answer:** **Bulkhead** isolates resources (thread pools/connections) per dependency so one slow service can't exhaust all threads (like ship compartments). **Retry** re-attempts failed calls with **exponential backoff + jitter**; always pair with timeouts and circuit breakers to avoid retry storms.