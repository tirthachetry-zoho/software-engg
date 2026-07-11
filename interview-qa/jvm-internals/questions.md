# JVM Internals — Interview Q&A

## 1. Explain the class loading lifecycle.
**Answer:** Three phases: (1) **Loading** — classloader reads `.class` bytecode into method area, creating a `Class` object. (2) **Linking** — Verification (bytecode valid), Preparation (static vars default values, memory allocated), Resolution (symbolic refs → direct). (3) **Initialization** — executes static initializers and `<clinit>` in order.

## 2. What are the built-in classloaders and parent delegation?
**Answer:** **Bootstrap** (loads core JDK classes, native), **Platform/Extension** (Java 9+, loads platform modules), **Application/System** (classpath). **Parent delegation**: a loader first delegates to its parent before attempting to load itself. This prevents duplicate/core class tampering and ensures single definition of core classes.

## 3. Why does parent delegation matter?
**Answer:** It guarantees the core Java classes (e.g., `java.lang.String`) are always loaded by the bootstrap loader, not a malicious or duplicate user version. This protects the integrity and security of the JVM and avoids multiple incompatible definitions of the same class.

## 4. What is the JIT compiler and tiered compilation?
**Answer:** The JIT (Just-In-Time) compiler converts frequently executed ("hot") bytecode into optimized native machine code at runtime, boosting performance. **Tiered compilation** (default) combines **C1** (client, fast, less optimization) for warm methods and **C2** (server, aggressive optimization) for hot methods — balancing startup speed and peak throughput.

## 5. What is bytecode and why is it platform-independent?
**Answer:** Bytecode is the stack-based intermediate instruction set emitted by `javac` from `.java` files. It's platform-independent because it's not native machine code — the JVM on each OS interprets/compiles it. The JVM abstracts OS/hardware differences, enabling "write once, run anywhere."

## 6. What is the difference between interpretation and JIT compilation?
**Answer:** **Interpreter** executes bytecode line-by-line (slow but instant start, no warmup). **JIT** compiles hot methods to native code (fast execution, but has compile overhead). HotSpot uses both: interprets initially, then JIT-compiles hot paths for speed.

## 7. What is Metaspace and how does it differ from PermGen?
**Answer:** Metaspace (Java 8+) stores class metadata in **native memory** and auto-grows, replacing PermGen which was fixed-size heap memory. This removes the common `PermGen space` OOM and lets metadata grow with available system memory (bounded by `MaxMetaspaceSize`).

## 8. What is a `ClassNotFoundException` vs `NoClassDefFoundError`?
**Answer:** `ClassNotFoundException` is a checked exception thrown at runtime when an app tries to load a class by name (e.g., `Class.forName`) and it's not on the classpath. `NoClassDefFoundError` is an Error thrown when the JVM could link/load a class at *runtime* that was present at compile time but is now missing (e.g., static init failed).

## 9. How does the JVM handle method invocation and stacks?
**Answer:** Each thread has a **stack** of **frames**; a new frame is pushed per method call holding local vars, operand stack, and return info. When the method returns, the frame pops. Deep recursion exhausts the stack → `StackOverflowError`. Stack size is set via `-Xss`.

## 10. What are intrinsic methods?
**Answer:** Intrinsics are methods the JVM treats specially, replacing the bytecode with highly optimized hand-written native/CPU instructions (e.g., `Math.sin`, `System.arraycopy`, `String.indexOf`). They bypass normal JIT for performance-critical operations.

## 11. What is the difference between a classloader leak and a memory leak?
**Answer:** A **classloader leak** happens when a classloader (and all classes it loaded) cannot be GC'd — typically because a static reference or thread holds onto it (common in app servers redeploying apps). It manifests as Metaspace growth. A general memory leak is retained heap objects. Both exhaust resources over time.

## 12. How would you inspect JVM internals in production?
**Answer:** Use `jcmd` (VM.native_memory, GC.class_histogram), `jstat` (GC/compilation stats), `jstack` (thread dumps), `jmap` (heap/class histograms), and `jinfo` (flags). For deep analysis: async-profiler, JFR (Java Flight Recorder), and VisualVM. These are low-overhead and safe on production.