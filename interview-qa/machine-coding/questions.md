# Machine Coding — Interview Q&A

## 1. What is machine coding and how is it evaluated?
**Answer:** A round where you **write working, clean code** for a small feature/problem (e.g., a snake game, a rate limiter, an LRU cache) within ~45–60 min. Evaluated on: correctness, clean structure, edge cases, and testability — not just a passing solution.

## 2. Implement an LRU Cache (LeetCode #146).
**Answer:** Use `LinkedHashMap` with `accessOrder=true`, override `removeEldestEntry()` to evict when `size > capacity`. Or `HashMap` + doubly-linked list (head=tail). O(1) get/put. Common MC + LLD question — know it cold.

## 3. Design and code a simple Logger (with levels).
**Answer:** `Logger` with `Level` (DEBUG/INFO/ERROR), an `Appender` interface (Console/File), and `log(level, msg)`. Use **Strategy** for appenders and a level threshold. Keep it extensible (add appenders without modifying core).

## 4. Implement a Rate Limiter (token bucket).
**Answer:** Class holds `capacity`, `tokens`, `refillRate`, `lastRefill`. On `allow()`: refill `tokens += (now-last)*rate` (cap at capacity), then if `tokens>=1` decrement and return true. Thread-safe via `ReentrantLock`. O(1).

## 5. Code a Parking Lot (LLD + MC).
**Answer:** `ParkingLot` → `Level` → `Spot` (type, isFree). `park(vehicle)` finds a free spot of the right type (Strategy by vehicle). Return ticket; `unpark(ticket)` frees it. Use clean classes + enums; show the design, not just logic.

## 6. Implement a Snake game (console).
**Answer:** Model `Snake` (list of points), `Food`, `Direction`. Game loop: read input → move head → check food (grow) / wall/self collision (game over). Use a grid `char[][]` for rendering. Tests edge cases: reverse direction guard, wrap vs wall.

## 7. Code a File System (in-memory, like LeetCode #588).
**Answer:** `Directory` has a `Map<String, Node>` (children), `File` has content. Support `mkdir`, `addContent`, `ls`, `readContent`. Use composition (Directory contains Nodes). Clean recursion for `ls` of a path.

## 8. Implement a Task Scheduler (with priorities).
**Answer:** Use a `PriorityQueue<Task>` ordered by `priority` then `time`. `schedule(task)` offers; `runNext()` polls. For delayed tasks, check `executeAt <= now`. Show thread-safety if asked.

## 9. Code a simple Pub/Sub (Event Bus).
**Answer:** `EventBus` holds `Map<Class, List<Subscriber>>`. `register(sub)` and `post(event)` invoke matching subscribers. Use generics (`EventBus<T>`). Decoupled, extensible — demonstrates OOP + patterns.

## 10. Implement a URL Shortener (encode + store).
**Answer:** `Shortener` with `Map<String,String>` (or DB) + a `base62` encoder of an auto-increment counter. `shorten(url)` → store + return short code; `resolve(code)` → original. Handle collisions (random suffix fallback).

## 11. Code a Vending Machine (state pattern).
**Answer:** `VendingMachine` with `State` (Idle/HasMoney/Dispense). Insert coin → select → dispense → return change. State encapsulates transitions; inventory is a `Map<Item,Integer>`. Clean, extensible design scores high.

## 12. What makes machine-coding code "interview-ready"?
**Answer:** Small, single-responsibility classes; interfaces for extensibility; meaningful names; handled edge cases (null, empty, full); basic tests or a `main` demo; and no copy-paste. Write as if it's production code a teammate will read.