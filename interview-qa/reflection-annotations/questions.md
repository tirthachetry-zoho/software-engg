# Reflection & Annotations — Interview Q&A

## 1. What is the Reflection API and when is it used?
**Answer:** Reflection lets a program inspect/modify its own structure at runtime — classes, methods, fields, constructors via `Class`, `Method`, `Field`. Used by frameworks (Spring, Hibernate), serialization, and test tools. Downsides: breaks encapsulation, slower (no JIT inlining), and bypasses compile-time checks.

## 2. How do you get a `Class` object?
**Answer:** Three ways: (1) `MyClass.class` (compile-time, no instance), (2) `obj.getClass()` (from instance), (3) `Class.forName("com.x.MyClass")` (runtime by name, throws checked `ClassNotFoundException`). The last is how JDBC drivers and app servers load plugins dynamically.

## 3. How do you invoke a private method via reflection?
**Answer:** `Method m = obj.getClass().getDeclaredMethod("secret", argTypes); m.setAccessible(true); m.invoke(obj, args);`. `setAccessible(true)` suppresses the Java language access check (may be blocked by a SecurityManager/module system). This is how frameworks inject dependencies into private fields.

## 4. What is a dynamic proxy?
**Answer:** `java.lang.reflect.Proxy` creates a runtime implementation of an interface that routes all calls to an `InvocationHandler`. Foundation of Spring AOP, retry wrappers, and RPC clients (e.g., Feign) — adding behavior (logging, tx, retry) without modifying the target class.

## 5. What is the difference between `@Retention` policies?
**Answer:** `SOURCE` — discarded by compiler (e.g., `@Override`). `CLASS` — retained in `.class` but not at runtime (default, used by some bytecode tools). `RUNTIME` — retained at runtime, readable via reflection (most framework annotations like `@Autowired`, `@Entity`).

## 6. What does `@Target` do?
**Answer:** `@Target` restricts where an annotation can be applied — `TYPE`, `METHOD`, `FIELD`, `PARAMETER`, `CONSTRUCTOR`, etc. Using the annotation elsewhere is a compile error. It documents and enforces correct usage.

## 7. What is annotation processing?
**Answer:** Compile-time processing via `AbstractProcessor` (the `javax.annotation.processing` API) that reads annotations to generate code/validate. Lombok (boilerplate), MapStruct (mappers), and Dagger (DI) use it. Unlike reflection, it runs at build time — zero runtime cost.

## 8. What is the difference between reflection and annotation processing?
**Answer:** **Reflection** runs at runtime, inspecting live objects (flexible but slow, breaks encapsulation). **Annotation processing** runs at compile time, generating source/bytecode (fast, type-safe, no runtime overhead). Prefer compile-time generation (Lombok) over runtime reflection when possible.

## 9. What are the risks of using `setAccessible(true)`?
**Answer:** It violates the module/encapsulation boundaries, may throw `InaccessibleObjectException` under JPMS (strong encapsulation of JDK internals), and can break across Java versions when internal APIs change. Use sparingly and prefer supported APIs.

## 10. How does Spring use reflection internally?
**Answer:** Spring uses reflection + dynamic proxies/CGLIB to: instantiate beans (constructor reflection), inject dependencies (field/method `setAccessible`), create AOP proxies for `@Transactional`/`@Cacheable`, and resolve `@RequestMapping` handlers. CGLIB subclasses concrete classes; JDK proxies implement interfaces.

## 11. What is the difference between a marker annotation and a marker interface?
**Answer:** A **marker interface** (e.g., `Serializable`, `Cloneable`) is an empty interface the type system recognizes. A **marker annotation** (e.g., `@Override`, `@Test`) carries metadata read via reflection/processing. Annotations are more flexible (can carry params, target precisely) and are the modern preference.

## 12. How would you read all annotations on a class at runtime?
**Answer:** `clazz.getAnnotations()` (incl. inherited) or `getDeclaredAnnotations()` (only declared). For a specific one: `clazz.getAnnotation(MyAnno.class)`. Then read its elements like methods. Requires `@Retention(RetentionPolicy.RUNTIME)`.