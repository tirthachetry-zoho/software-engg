# SOLID Principles — Interview Q&A

## 1. What does SOLID stand for?
**Answer:** Five design principles: **S**RP (Single Responsibility), **O**CP (Open-Closed), **L**SP (Liskov Substitution), **I**SP (Interface Segregation), **D**IP (Dependency Inversion). They guide maintainable, extensible, low-coupling object-oriented design.

## 2. Explain the Single Responsibility Principle (SRP).
**Answer:** A class should have **one reason to change** — one responsibility. Example: a `Report` class shouldn't both compute data and format/email it. Split into `ReportGenerator`, `ReportFormatter`, `ReportMailer`. Reduces coupling and makes testing/maintenance easier.

## 3. Explain the Open-Closed Principle (OCP).
**Answer:** Software entities should be **open for extension, closed for modification**. Add new behavior via new code (subclasses/strategies), not by editing working classes. Achieved with polymorphism + interfaces (e.g., Strategy, Template patterns). Protects tested code from regressions.

## 4. Explain the Liskov Substitution Principle (LSP).
**Answer:** Subtypes must be substitutable for their base type without altering correctness. Violation example: `Penguin extends Bird` but `fly()` throws — clients expecting any Bird to fly break. Fix: don't force inappropriate inheritance; use a `Flyable` interface instead.

## 5. Explain the Interface Segregation Principle (ISP).
**Answer:** Clients shouldn't depend on interfaces they don't use. Prefer **small, client-specific** interfaces over one fat interface. Example: split `Worker` into `Coder` and `Tester` rather than forcing a `Manager` interface with unused methods on every implementer.

## 6. Explain the Dependency Inversion Principle (DIP).
**Answer:** High-level modules shouldn't depend on low-level details; both should depend on **abstractions**. And abstractions shouldn't depend on details — details depend on abstractions. Achieved via Dependency Injection (constructor injection). Enables swapping implementations (e.g., test doubles).

## 7. Give a real example of violating SRP and fixing it.
**Answer:** A `UserService` that validates input, saves to DB, sends email, and logs. Each concern is a separate reason to change. Refactor: `UserValidator`, `UserRepository`, `EmailService`, `AuditLogger` — `UserService` orchestrates them. Now changes to email don't touch persistence.

## 8. How does OCP relate to design patterns?
**Answer:** OCP is the motivation behind many patterns: Strategy (swap algorithms), Decorator (add behavior), Observer (add listeners), Template Method (vary steps). They let you extend behavior without modifying the stable core.

## 9. What is the difference between DIP and Dependency Injection?
**Answer:** **DIP** is the *principle* (depend on abstractions). **Dependency Injection** is a *mechanism* (passing dependencies in rather than creating them inside) that realizes DIP. DI is the how; DIP is the why. Spring's IoC container is a DI implementation of DIP.

## 10. How would you apply ISP in a large microservice client?
**Answer:** Instead of one `PaymentClient` interface with `charge`, `refund`, `getHistory`, `dispute`, split into `PaymentCharger`, `RefundProcessor`, `PaymentHistoryReader`. A service needing only refunds depends only on `RefundProcessor`, reducing coupling and recompile impact.

## 11. Why is SOLID important for senior engineers?
**Answer:** SOLID produces code that's easier to test, extend, and review; reduces regression risk; and supports team-scale development. At SDE3 level, you're expected to design systems where these principles prevent the "big ball of mud" as the codebase grows.

## 12. Can SOLID be over-applied? Give a caution.
**Answer:** Yes — excessive abstraction for trivial code adds indirection and complexity (YAGNI conflict). Apply SOLID where the code is likely to change or be extended; don't prematurely split a simple, stable class into five interfaces. Balance with KISS.