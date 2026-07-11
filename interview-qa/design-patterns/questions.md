# Design Patterns — Interview Q&A

## 1. What are the three categories of design patterns?
**Answer:** **Creational** (object creation: Singleton, Factory, Builder, Prototype, Abstract Factory). **Structural** (composition/relationships: Adapter, Decorator, Composite, Facade, Proxy, Bridge, Flyweight). **Behavioral** (communication: Strategy, Observer, State, Command, Template, Chain of Responsibility, Iterator, Mediator, Memento, Visitor).

## 2. How do you implement a thread-safe Singleton?
**Answer:** Best practice: use an `enum` (`enum Singleton { INSTANCE; }`) — inherently thread-safe, serialization-safe, and reflection-proof. Alternatives: eager initialization, or the holder pattern (`private static class Holder { static final INSTANCE = new ...; }`) for lazy + thread-safe without locking.

## 3. What is the difference between Factory and Abstract Factory?
**Answer:** **Factory Method** creates one product via a method subclasses override. **Abstract Factory** provides an interface to create *families* of related products (e.g., a UI factory producing Button+Checkbox for Windows vs Mac). Use Abstract Factory when products must be compatible.

## 4. When would you use the Builder pattern?
**Answer:** When constructing an object with many optional parameters or a complex multi-step setup. Builder improves readability vs telescoping constructors and enforces immutability (build returns a final object). Example: `Pizza.builder().size(12).cheese(true).build()`.

## 5. What is the difference between Adapter and Decorator?
**Answer:** **Adapter** changes an interface to make incompatible classes work together (changes the *interface*). **Decorator** adds behavior to an object while keeping the same interface (adds *responsibility*). Both wrap, but Adapter = convert, Decorator = enhance.

## 6. Explain the Strategy pattern with an example.
**Answer:** Encapsulates a family of algorithms behind a common interface, letting the client swap them at runtime. Example: a `PaymentProcessor` holds a `PaymentStrategy` (CreditCard, PayPal, Crypto) — adding a new strategy needs no change to the processor (OCP).

## 7. What is the Observer pattern and where is it used?
**Answer:** Defines a one-to-many dependency: when one subject changes state, all registered observers are notified. Used in event systems, Spring `ApplicationEvent`, GUI listeners, and reactive streams (`Flow.Publisher`/`Subscriber`). Decouples producer from consumers.

## 8. What is the difference between State and Strategy?
**Answer:** Both use composition with interchangeable objects. **Strategy** is chosen by the client and is usually stateless/interchangeable. **State** is internally switched by the object as its state changes (e.g., Order: New → Paid → Shipped) and often knows about transitions to other states.

## 9. Explain the Proxy pattern.
**Answer:** Provides a surrogate object controlling access to the real one — for lazy loading, security, logging, or remote calls. Spring uses JDK dynamic proxies / CGLIB to create proxies for `@Transactional` and AOP. The client interacts with the proxy transparently.

## 10. What is the Template Method pattern?
**Answer:** Defines the skeleton of an algorithm in a base class with `final` steps, leaving specific steps abstract for subclasses to implement. Example: an `AbstractOrderProcessor` with `process()` calling `validate()`, `charge()`, `notify()` — subclasses override the steps but not the flow.

## 11. What is the difference between Command and Chain of Responsibility?
**Answer:** **Command** encapsulates a request as an object (enabling undo, queuing, macros). **Chain of Responsibility** passes a request along a chain of handlers until one processes it (e.g., logging levels, middleware). Command = encapsulate action; Chain = pass-along handling.

## 12. What is the Composite pattern?
**Answer:** Lets you treat individual objects and compositions uniformly via a tree structure. Example: a `File` and a `Directory` both implement a `FileSystemNode` with `getSize()` — the directory sums its children recursively. Enables recursive operations without special-casing containers.