# Spring Framework — Interview Q&A

## 1. What is Inversion of Control (IoC)?
**Answer:** IoC is a design principle where the framework (Spring container) **controls object creation and lifecycle** instead of your code doing `new`. You declare dependencies; Spring wires them. This decouples components and enables DI, making code testable and modular.

## 2. What is Dependency Injection and the types?
**Answer:** DI is how IoC is implemented — dependencies are provided to a class rather than created inside. Types: **constructor** (preferred, immutable, testable), **setter** (optional/mutable deps), and **field** (concise but hard to test — avoid). Constructor injection is recommended by Spring.

## 3. Explain the bean lifecycle.
**Answer:** (1) Instantiate → (2) Populate properties (DI) → (3) `BeanNameAware`/`BeanFactoryAware` callbacks → (4) `BeanPostProcessor.postProcessBeforeInitialization` → (5) `@PostConstruct` / `InitializingBean.afterPropertiesSet` → (6) `postProcessAfterInitialization` → (7) ready → (8) `@PreDestroy` on shutdown.

## 4. What is the difference between `@Component`, `@Service`, `@Repository`, `@Controller`?
**Answer:** All are stereotypes that register beans. They're semantically identical for component scanning, but convey intent and enable extras: `@Repository` adds automatic exception translation (JDBC → Spring's `DataAccessException`); `@Controller`/`@RestController` for web handlers.

## 5. What is the difference between `@Autowired` and constructor injection?
**Answer:** `@Autowired` on a field/setter is reflection-based and harder to test (needs Spring context). **Constructor injection** is explicit, works without the container (plain `new` in tests), enforces required deps, and supports `final` fields. Modern Spring recommends constructor injection (no annotation needed if single constructor).

## 6. What is AOP and its core concepts?
**Answer:** Aspect-Oriented Programming modularizes cross-cutting concerns (logging, tx, security). Core: **Aspect** (module), **Join Point** (executable point, e.g., method), **Pointcut** (which join points), **Advice** (action: @Before/@After/@Around), **Weaving** (linking aspects). Spring AOP uses JDK dynamic proxies or CGLIB.

## 7. How does Spring proxy-based AOP work?
**Answer:** Spring creates a **proxy** around the bean. Calls go through the proxy, which applies advices then delegates to the real bean. JDK proxy for interfaces, CGLIB for classes. Limitation: self-invocation (method calling another method in the same class) bypasses the proxy — advices won't run.

## 8. What is the difference between singleton and prototype scope?
**Answer:** **Singleton** (default) — one shared instance per container. **Prototype** — a new instance per request/ injection. Others: request/session (web). Be careful injecting a prototype into a singleton — the prototype is resolved once at wiring time unless you use `ObjectProvider`/lookup.

## 9. What is `@Qualifier` and when is it needed?
**Answer:** When multiple beans implement the same type, autowiring is ambiguous. `@Qualifier("name")` (or `@Primary`) disambiguates by bean name. `@Primary` marks a default; `@Qualifier` selects a specific one.

## 10. What is the difference between `@Bean` and `@Component`?
**Answer:** `@Component` (with stereotype) is auto-detected via component scanning. `@Bean` is used inside a `@Configuration` class to explicitly instantiate/configure third-party or complex objects (e.g., `DataSource`, `RestTemplate`) where you control construction.

## 11. What is `@Configuration` vs `@Component`?
**Answer:** `@Configuration` is a special `@Component` whose methods returning `@Bean` are **proxied** to ensure singleton semantics (calling `beanA()` twice returns the same instance). Lite `@Component` with `@Bean` methods doesn't guarantee this inter-bean singleton behavior.

## 12. What is the difference between `@ControllerAdvice` and `@Controller`?
**Answer:** `@Controller`/`@RestController` handle requests. `@ControllerAdvice` (or `@RestControllerAdvice`) is a global interceptor for **exception handling** (`@ExceptionHandler`), `@ModelAttribute`, and `@InitBinder` across all controllers — centralizing error responses.