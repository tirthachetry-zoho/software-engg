# Low-Level Design — Interview Q&A

## 1. How do you approach an LLD / OOD problem?
**Answer:** (1) Clarify requirements & use cases. (2) Identify **entities/classes** and their responsibilities (noun extraction). (3) Define relationships (association/aggregation/composition). (4) Apply **design patterns** where they fit. (5) Sketch class diagram + key method signatures. (6) Write clean, extensible code. Think in interfaces.

## 2. Design a Parking Lot.
**Answer:** Classes: `ParkingLot` (has `List<Level>`), `Level` (has `Spot[]`), `ParkingSpot` (type: compact/large/electric, isFree), `Vehicle` (Car/Bike/Truck). Strategy for spot assignment by vehicle type. Use Factory for vehicles. Methods: `assignSpot()`, `freeSpot()`, `getAvailableCount(type)`.

## 3. Design an Elevator system.
**Answer:** `Elevator` (currentFloor, direction, requests), `ElevatorController` (dispatches requests to elevators via a **Strategy** — SCAN/LOOK algorithm), `Request` (source, dest), `Button`/external hall calls. Handle multiple elevators with a scheduler choosing the nearest/idle one.

## 4. Design a Chess game.
**Answer:** `Board` (8x8 `Square`s), `Piece` hierarchy (King/Queen/... with `move()` polymorphism), `Player`, `Game` (turn management, `Move` history), `Move` (from, to, piece). Use **Strategy** for move-validation per piece type. Enforce rules in `Game` (check/checkmate). Keep board state separate from rendering.

## 5. Design a Library management system.
**Answer:** `Book` (ISBN, title, copies), `Member` (borrowed list), `Librarian`, `Loan` (book, member, dueDate), `Catalog` (search). Relationships: Member *borrows* Book via Loan (association). Apply **Observer** to notify on due/return; **Factory** for membership types.

## 6. Design Splitwise (expense splitting).
**Answer:** `User`, `Group`, `Expense` (paidBy, splitAmong, amounts[], type: EQUAL/EXACT/PERCENT), `Settlement` (from, to, amount). `ExpenseService` computes balances per user; `SettleUp` reduces transactions (greedy matching creditors/debtors). Use **Strategy** for split-type calculation.

## 7. Design an ATM.
**Answer:** `ATM` (has `CardReader`, `CashDispenser`, `Screen`, `BankService` via network), `Session` (authenticated card), `Transaction` hierarchy (Withdraw/Deposit/Balance). **State** pattern for session states (Idle/CardInserted/Authenticated). Delegate balance/auth to `BankService` (interface, for testability).

## 8. Design a BookMyShow / ticket booking.
**Answer:** `Movie`, `Theatre`, `Screen`, `Show` (movie, screen, time, `Seat[]`), `Seat` (status: available/locked/booked), `Booking` (seats, user, payment). Use **locking** (timed seat hold via Redis) to prevent double-booking. **Factory** for pricing (matinee/prime).

## 9. Design a Cache (LRU) — code it.
**Answer:** Use `LinkedHashMap` with `accessOrder=true` and override `removeEldestEntry()` to evict when size > capacity. Or a manual `HashMap` + doubly-linked list. O(1) get/put. This is a classic LLD + DSA combo (LeetCode #146).

## 10. Design a Vending Machine.
**Answer:** **State** pattern: `IdleState`, `HasMoneyState`, `DispenseState`, `ReturnChangeState`. `VendingMachine` holds current state + inventory (`Map<Item, count>`). Insert coin → select item → dispense → return change. State encapsulates allowed transitions; clean and extensible.

## 11. Design a Cab / Ride booking system (simplified).
**Answer:** `Rider`, `Driver` (location, status), `Trip` (rider, driver, pickup, drop, fare), `Location`, `FareStrategy` (distance/time — **Strategy**). `MatchingService` finds nearest available driver (geospatial). `TripService` manages lifecycle (request→matched→ongoing→completed→paid).

## 12. What patterns are most useful in LLD interviews?
**Answer:** **Strategy** (swappable algorithms), **State** (lifecycle/state machines), **Factory/Builder** (object creation), **Observer** (notifications), **Singleton** (config/registry), **Decorator** (add behavior). Identify the "varies" part and encapsulate it behind an interface.