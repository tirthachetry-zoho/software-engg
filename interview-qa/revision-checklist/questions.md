# Revision Checklist — Interview Q&A

## 1. What is the 8-week SDE3 prep plan?
**Answer:** W1 Core Java/OOP/Collections; W2 JVM/Concurrency/Java8+; W3 SQL/Spring Boot/REST; W4 Redis/Kafka/Docker; W5 K8s/Microservices/Security; W6 DSA revision/Machine Coding; W7 LLD/HLD; W8 Mocks/Resume/Behavioral. Adjust to your weak spots.

## 2. Which Java topics must you revise cold?
**Answer:** HashMap internals, GC collectors, concurrency (volatile/atomic/locks), `CompletableFuture`, Java 8–21 features (records, virtual threads), equals/hashCode, and string pooling. These appear in almost every round.

## 3. What DSA patterns should be second nature?
**Answer:** Binary search (incl. on answer), two-pointer, sliding window, prefix sum, BFS/DFS, topological sort, DP (knapsack/LIS), backtracking, heaps, and union-find. Practice 150–250 LeetCode Medium/Hard (NeetCode 150).

## 4. What Spring/backend must you know?
**Answer:** IoC/DI (constructor injection), bean lifecycle, `@Transactional` caveats, auto-config, REST design (idempotency, pagination), and Spring Security filter chain. Be ready to debug a non-commiting transaction.

## 5. Which system-design areas are highest yield?
**Answer:** URL shortener, rate limiter, caching strategy, messaging/notification, and consistency-vs-availability tradeoffs (CAP/quorum). Practice the approach (clarify → estimate → sketch → deep-dive → tradeoffs), not memorized diagrams.

## 6. What LLD patterns recur most?
**Answer:** Strategy (swappable logic), State (lifecycles), Factory/Builder (creation), Observer (notifications), and Singleton (config). Practice Parking Lot, Elevator, LRU Cache, and Vending Machine with clean class diagrams.

## 7. Which DevOps/cloud topics are expected at SDE3?
**Answer:** Docker multi-stage, K8s (Deployment/Service/HPA/probes), CI/CD (GitOps, canary), AWS basics (EC2/RDS/S3/Lambda), and observability (metrics/logs/traces, SLOs). You should speak to how you ship and operate.

## 8. What behavioral stories should you pre-write?
**Answer:** 6–8 STAR stories covering: leadership, conflict, failure, production incident, mentoring, ownership, architecture decision, and tradeoff. Quantify every result. Rehearse, don't memorize verbatim.

## 9. How do you avoid common revision mistakes?
**Answer:** Don't just read — *write code* for DSA and LLD. Don't skip mock interviews. Don't over-memorize trivia; focus on explaining tradeoffs. Track weak areas and revisit them weekly.

## 10. What is the single highest-leverage activity?
**Answer:** **Mock interviews** (Pramp/Exponent/peers) — they expose communication gaps, time-management, and blind spots that solo study misses. Combine with a spaced-repetition review of this Q&A set.

## 11. How should you use this interview-qa folder?
**Answer:** Treat each `questions.md` as a drill. Read a question, answer aloud, then compare. Mark weak topics, revisit them, and convert the hardest ones into flashcards. Pair with the SDE3-guide.md for deeper links.

## 12. What mindset do interviewers look for at SDE3?
**Answer:** You *drive* the conversation: clarify ambiguity, state tradeoffs explicitly, consider scale/failure, and communicate clearly. Senior signal = designing and owning outcomes, not just implementing a spec.