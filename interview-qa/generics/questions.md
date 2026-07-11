# Generics — Interview Q&A

## 1. What is type erasure?
**Answer:** Java implements generics via **type erasure** — generic type info is removed at compile time, and raw types/`Object` casts are inserted. This preserves backward compatibility with pre-generics code. Consequence: you can't use `T` in `new T()`, `instanceof T`, or create `T[]` directly.

## 2. What is the difference between `List` and `List<Object>` and raw `List`?
**Answer:** `List<String>` is type-safe (only Strings). `List<Object>` accepts any Object but isn't a supertype of `List<String>` (invariance). Raw `List` (no type param) opts out of generics entirely — disables compile-time checks and triggers unchecked warnings. Always use parameterized types.

## 3. Explain PECS (Producer Extends, Consumer Super).
**Answer:** **PECS** guides wildcard use: use `? extends T` when the collection is a **producer** (you read T out), and `? super T` when it's a **consumer** (you write T in). Example: `void copy(List<? extends Number> src, List<? super Number> dest)` — read from src, write to dest safely.

## 4. What is the difference between `extends` and `super` wildcards?
**Answer:** `? extends T` (upper bound) — can read T but can't add (except null), because the actual type could be any subtype. `? super T` (lower bound) — can write T (and subtypes) but reads return `Object`. This is the Get/Put principle behind PECS.

## 5. Can you create a generic array? Why not?
**Answer:** No — `new T[10]` is illegal because of type erasure (the JVM can't know T's type at runtime to create the correct array). Workarounds: use `ArrayList<T>`, or `(T[]) new Object[size]` with an unchecked cast, or pass a `Class<T>`/`IntFunction` to `toArray`.

## 6. What is a bounded type parameter?
**Answer:** Restricts the type to a bound: `<T extends Number>` allows only Number and its subclasses. Multiple bounds: `<T extends A & B>` (first must be a class if mixing class+interface). This lets you call methods of the bound on T.

## 7. What is the difference between generic methods and generic classes?
**Answer:** A **generic class** declares type params at class level (`class Box<T>`), applying to all methods. A **generic method** declares its own type param in the signature (`public <T> T max(List<T> l)`), usable in any class (even static). Method type params can differ from class params.

## 8. Why can't you use primitives with generics?
**Answer:** Generics rely on `Object` (reference types) due to erasure; primitives (int, double) aren't Objects. You must use wrapper types (`Integer`, `Double`). Autoboxing hides this, but be mindful of boxing overhead and NPE on unboxing.

## 9. What is the difference between `Comparable` and `Comparator` with generics?
**Answer:** `Comparable<T>` is implemented by the type itself (`class Person implements Comparable<Person>`), defining natural order. `Comparator<T>` is an external strategy (`Comparator<Person> byAge`). Generics make both type-safe — no casting needed in `compareTo`/`compare`.

## 10. What are reified vs non-reified types in generics?
**Answer:** **Non-reified** = generic types whose info is erased at runtime (e.g., `List<String>` is just `List` at runtime). **Reified** = types fully available at runtime (arrays, primitives, raw types). This is why `instanceof List<String>` is illegal but `instanceof List` is fine.

## 11. How do you write a generic method with multiple bounds?
**Answer:** Use `&`: `public static <T extends Comparable<T> & Serializable> T process(T t)`. The first bound must be a class if you mix a class and interfaces. This ensures T has all required capabilities inside the method.

## 12. What is the difference between `List<?>` and `List<Object>`?
**Answer:** `List<?>` (unbounded wildcard) is a supertype of every `List<X>` — you can assign any list to it, but can only read as `Object` and can't add anything (except null). `List<Object>` is a specific list that holds any Object but is NOT assignable from `List<String>` (invariance).