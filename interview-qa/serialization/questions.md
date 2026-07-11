# Serialization — Interview Q&A

## 1. What is Java Serialization and how does it work?
**Answer:** Java Serialization converts an object's state to a byte stream (via `ObjectOutputStream`) and back (`ObjectInputStream`). The class must implement the `Serializable` marker interface. It uses reflection to write field values, making it slow and fragile across versions.

## 2. What is `serialVersionUID` and why is it important?
**Answer:** A `static final long` version identifier. During deserialization, the JVM checks the stream's `serialVersionUID` matches the class's. If they differ, it throws `InvalidClassException`. Declaring it explicitly makes class evolution (adding fields) backward-compatible.

## 3. What is the difference between `Serializable` and `Externalizable`?
**Answer:** `Serializable` uses default reflection-based serialization (simple, but slow and exposes all non-transient fields). `Externalizable` requires you to implement `writeExternal`/`readExternal`, giving full control over the format (faster, compact, version-safe) at the cost of more code.

## 4. What does the `transient` keyword do in serialization?
**Answer:** Fields marked `transient` are **skipped** during default serialization (e.g., passwords, caches, derived data). On deserialization they get default values (null/0/false) unless you restore them in `readObject`. Use it to exclude sensitive or non-essential state.

## 5. What is the difference between `serialVersionUID` auto-generation and explicit?
**Answer:** If omitted, the JVM computes one from class structure (fields, methods, etc.). Any change (even adding a method) changes it → `InvalidClassException` on old streams. Explicit declaration keeps it stable, allowing safe class evolution.

## 6. What are the security concerns with Java Serialization?
**Answer:** Deserializing untrusted data can trigger **gadget-chain attacks** (executing arbitrary code during reconstruction). It's slow and verbose. Modern practice: avoid native serialization for cross-system data; use JSON, Protobuf, or Avro instead.

## 7. How do you customize serialization?
**Answer:** Implement `private void writeObject(ObjectOutputStream oos)` and `private void readObject(ObjectInputStream ois)` to add encryption, validation, or transient handling. Or implement `Externalizable` for full control. Always validate in `readObject` to prevent invariant violations.

## 8. What is the difference between serialization and persistence?
**Answer:** **Serialization** is Java-specific object-to-byte conversion (for storage/network). **Persistence** is the broader concept of storing data durably (DB, files). Serialization is one persistence mechanism but tightly coupled to Java classes.

## 9. Can you serialize a static field? Why not?
**Answer:** No — `static` fields belong to the **class**, not the instance, and serialization only captures object state. Static values aren't written; on deserialization they come from the current class definition. Don't rely on serialization for class-level data.

## 10. What happens to object references during serialization (graph handling)?
**Answer:** Java serialization handles **object graphs** — each object is written once and referenced by handle thereafter, preserving shared references and avoiding infinite loops on cyclic graphs. The same object deserialized remains a single shared instance.

## 11. What is the difference between Java serialization and JSON (Jackson/Gson)?
**Answer:** Java native serialization is binary, Java-only, reflection-based, and version-fragile. JSON is text, language-agnostic, human-readable, and schema-flexible — ideal for REST APIs and cross-system communication. JSON is the modern default for data exchange.

## 12. How do you make a singleton survive serialization?
**Answer:** Implement `readResolve()` (returns the singleton instance) so deserialization returns the existing instance instead of a new one — preserving the singleton contract. Without it, deserialization creates a second instance, breaking the pattern.