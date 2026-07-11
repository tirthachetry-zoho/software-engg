# Object-Oriented Programming — Interview Q&A

## 1. What are the four pillars of OOP?
**Answer:** (1) **Encapsulation** — bundling data with methods that operate on it and hiding internal state (access modifiers). (2) **Abstraction** — exposing only essential features, hiding complexity (interfaces/abstract classes). (3) **Inheritance** — `is-a` relationship, reusing parent behavior. (4) **Polymorphism** — one interface, many implementations (overloading/overriding).

## 2. Why favor composition over inheritance?
**Answer:** Inheritance is compile-time bound, breaks encapsulation (subclass depends on parent internals), and creates tight coupling/fragile base class problem. Composition (has-a) is flexible, testable, and allows behavior to be swapped at runtime. "Favor object composition over class inheritance" — GoF.

## 3. What is the difference between association, aggregation, and composition?
**Answer:** **Association** — general relationship between objects. **Aggregation** — weak "has-a", independent lifecycles (a Department has Employees, but employees exist without the department). **Composition** — strong "owns-a", lifecycle-bound (a House has Rooms; rooms die with the house). Composition implies ownership and exclusive containment.

## 4. Explain the Liskov Substitution Principle with an example.
**Answer:** Subtypes must be substitutable for their base types without breaking behavior. Classic violation: `Square extends Rectangle` — if you set width and height independently on a Rectangle but enforce width==height on Square, calling `setWidth` alone breaks the Rectangle contract. Solution: don't force the inheritance; use a shared `Shape` interface instead.

## 5. What is the Open-Closed Principle? Give an example.
**Answer:** Software entities should be open for extension but closed for modification. Example: a `PaymentProcessor` using the **Strategy pattern** — you add a new `CryptoPayment` strategy class without modifying the existing `Processor` context. New behavior is added via new code, not by editing tested code.

## 6. What is abstraction vs encapsulation?
**Answer:** Abstraction is about *hiding complexity* (what an object does) — achieved via interfaces/abstract classes. Encapsulation is about *hiding data* (how state is protected) — achieved via private fields + public getters/setters. Abstraction is design-level; encapsulation is implementation-level.

## 7. Can you override a static method? Why or why not?
**Answer:** No. Static methods belong to the class, not instances, and are resolved at compile time (static binding). You can *hide* a static method in a subclass (same signature), but it's not polymorphism — which version runs depends on the reference type, not the runtime object.

## 8. What is method hiding vs method overriding?
**Answer:** Overriding applies to instance methods (runtime polymorphism, `@Override`). Hiding applies to static methods and fields — the subclass defines a member with the same name, and resolution depends on the *compile-time* reference type, not the object type.

## 9. What is coupling and cohesion?
**Answer:** **Coupling** = degree of interdependence between modules — aim for *low* coupling. **Cohesion** = how closely related the responsibilities within a module are — aim for *high* cohesion. A well-designed class does one thing well (high cohesion) and depends minimally on others (low coupling).

## 10. Explain DRY, KISS, and YAGNI.
**Answer:** **DRY** (Don't Repeat Yourself) — avoid duplicated logic. **KISS** (Keep It Simple, Stupid) — prefer simple solutions over clever ones. **YAGNI** (You Aren't Gonna Need It) — don't build speculative features you don't currently need; they add complexity and maintenance cost.

## 11. What is the difference between an abstract class and an interface?
**Answer:** Abstract class can have state (fields), constructors, and partial implementation; supports single inheritance. Interface (Java 8+) can have default/static methods but no state (until `private` methods); a class can implement many interfaces. Use abstract class for "is-a" with shared code; interface for capability/contract ("can-do").

## 12. What is polymorphism? Give a real-world example in Java.
**Answer:** Polymorphism lets one interface be used for different underlying types. Example: a `List` reference holding `ArrayList` or `LinkedList` — `list.get(i)` behaves correctly for whichever implementation is assigned at runtime. Achieved via method overriding (runtime) and overloading (compile-time).