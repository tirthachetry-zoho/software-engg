# Low-Level Design (LLD) Problems List

Object-oriented designs implemented in `system-design/lld/`. Each entry describes the problem and the key design decisions demonstrated in the code.

## 1. LRU Cache (DesignLRUCache.java)
Design a fixed-capacity cache that evicts the least-recently-used item when full and treats every get/put as a "recently used" event.
- **Core design** — Doubly linked list (for O(1) reorder/move) + hash map (for O(1) key lookup). Most-recently-used at head, least at tail.
- **Variants included**
  - `LRUCache` — Basic capacity-bounded cache.
  - `ThreadSafeLRUCache` — Adds synchronization for concurrent access.
  - `LRUCacheWithTTL` — Entries expire after a time-to-live using a delayed-removal sweep.
  - `LRUCacheWithStats` — Tracks hit/miss ratios for observability.

## 2. HashMap (DesignHashMap.java)
Build a hash map from scratch supporting put, get, and remove.
- **Core design** — Array of buckets with separate chaining (linked list of Entry nodes) to resolve collisions; hash code modulo capacity picks the bucket.
- **Variants included**
  - `MyHashMap` — Single-threaded chaining hash map.
  - `ConcurrentHashMap` — Fine-grained segment-based locking for thread safety.
  - `LRUCache` (nested) — A hash map that also evicts least-recently-used entries, combining hashing with ordering.

## 3. Logger System (DesignLogger.java)
Design a flexible logging framework similar to SLF4J/Log4j.
- **Core design** — Strategy pattern via a LogAppender interface; LogMessage carries level + text + timestamp.
- **Components included**
  - `ConsoleAppender` — Writes logs to stdout.
  - `FileAppender` — Writes to a file with size-based log rotation.
  - `AsyncAppender` — Wraps another appender and emits logs on a background thread (non-blocking).
  - `LevelFilter` — Suppresses messages below a configured severity threshold.

## 4. Parking Lot (DesignParkingLot.java)
Model a multi-level parking garage that assigns spots, issues tickets, and computes fees.
- **Core design** — Composition of ParkingLot to ParkingLevel to ParkingSpot; Vehicle hierarchy (Car, Bike, Truck) drives spot-type compatibility.
- **Components included**
  - `ParkingSpot` — Tracks type (small/medium/large), occupancy, and parked vehicle.
  - `ParkingTicket` — Records entry time and spot for a vehicle.
  - `PricingStrategy` (e.g. `HourlyPricing`) — Pluggable fee calculation; hourly pricing rounds up to the nearest hour.
  - Spot allocation finds the nearest free compatible spot across levels.

## Design Patterns Demonstrated
- **Strategy** — Pricing strategies, log appenders.
- **Decorator / Wrapper** — Async appender wrapping a base appender.
- **Composite** — Parking lot composed of levels and spots.
- **Encapsulation** — Internal state (linked-list nodes, buckets) hidden behind clean public APIs.