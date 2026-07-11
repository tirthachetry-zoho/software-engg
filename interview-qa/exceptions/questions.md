# Exception Handling — Interview Q&A

## 1. What is the difference between checked and unchecked exceptions?
**Answer:** **Checked** exceptions (extend `Exception`, not `RuntimeException`) are checked at compile time — you must handle or declare them (`IOException`). **Unchecked** (extend `RuntimeException`) are not forced — usually programming errors (`NullPointerException`, `IllegalArgumentException`). **Error** (e.g., `OutOfMemoryError`) signals serious JVM failures you shouldn't catch.

## 2. What is the difference between `Error` and `Exception`?
**Answer:** Both extend `Throwable`. `Exception` represents conditions an app might reasonably catch and recover from. `Error` represents serious, usually unrecoverable JVM problems (e.g., `StackOverflowError`, `OutOfMemoryError`) — catching them is generally discouraged.

## 3. Explain try-with-resources.
**Answer:** A `try` block that declares resources implementing `AutoCloseable`; they're **automatically closed** in reverse declaration order, even on exception. It eliminates boilerplate `finally` blocks and prevents resource leaks. Example: `try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) { ... }`.

## 4. What is the order of `catch` blocks and why does it matter?
**Answer:** Catch blocks must go from **most specific (subclass) to most general (superclass)**. A `catch (Exception e)` before `catch (IOException e)` won't compile because the first already catches everything. Proper ordering ensures the right handler runs.

## 5. What happens to a `finally` block if `try` or `catch` has a `return`?
**Answer:** The `finally` block **always executes** before the method returns (unless `System.exit()` or JVM crash). If `finally` also has a `return`, it overrides the value from `try`/`catch` — a common pitfall; avoid returning from `finally`.

## 6. What is the difference between `throw` and `throws`?
**Answer:** `throw` is used to **actually throw** an exception instance (`throw new IllegalArgumentException()`). `throws` is a **declaration** in a method signature listing the checked exceptions it may propagate upward (`void read() throws IOException`).

## 7. How do you create a custom exception?
**Answer:** Extend `Exception` (checked) or `RuntimeException` (unchecked), provide constructors that take a message and a cause, and always chain the `cause` (via `super(message, cause)`) to preserve the stack trace for debugging.

## 8. What is exception chaining / suppressed exceptions?
**Answer:** **Chaining** preserves the root cause via `initCause`/`Throwable(Throwable cause)` so the original error isn't lost. **Suppressed exceptions** (Java 7+) are exceptions thrown while closing resources in try-with-resources, accessible via `getSuppressed()` — the primary exception is still thrown.

## 9. Why shouldn't you catch `Throwable` or `Exception` broadly?
**Answer:** Catching `Throwable` also catches `Error` (e.g., `OutOfMemoryError`) which you usually can't recover from, and may hide bugs. Broad `catch (Exception)` swallows expected errors silently. Catch specific exceptions and let unexpected ones surface (fail fast).

## 10. What is the difference between `final`, `finally`, and `finalize` in exception context?
**Answer:** `final` (variable/method/class modifier), `finally` (block that runs cleanup code regardless of exception), `finalize()` (deprecated GC callback). Only `finally` relates to exception flow; the other two are unrelated but commonly confused in interviews.

## 11. What is the best practice for logging exceptions?
**Answer:** Log the full exception with stack trace (not just `getMessage()`), include contextual data (IDs, inputs), use the appropriate level (ERROR for failures, WARN for recoverable), and avoid logging then rethrowing the same exception (causes duplicate noise). Wrap and rethrow with context when crossing layers.

## 12. What is the difference between `RuntimeException` and a checked exception in API design?
**Answer:** Use **checked** exceptions for recoverable, expected conditions the caller should handle (e.g., `FileNotFound`). Use **unchecked** for programming errors or unrecoverable states (e.g., `NullPointer`). Overusing checked exceptions clutters signatures; modern APIs favor unchecked for most cases.