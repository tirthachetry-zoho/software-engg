# Clean Code — Interview Q&A

## 1. What makes a name "good" in clean code?
**Answer:** Names should be **intention-revealing**, pronounceable, and searchable — avoid abbreviations and single letters (except loop counters). `elapsedTimeInDays` beats `etd`; `AccountRepository` beats `AR`. Good names remove the need for comments.

## 2. What are common code smells?
**Answer:** Long method, large class (god object), duplicate code, long parameter list, feature envy (a method uses another class's data more than its own), dead code, shotgun surgery (one change scatters across many classes), and primitive obsession (using strings/ints instead of domain types).

## 3. What is the rule for function length and parameters?
**Answer:** Keep functions small (ideally < ~20 lines, do one thing). Limit parameters to **≤3**; more signals the function does too much or needs a parameter object. Small functions are easier to name, test, and reason about.

## 4. What is the difference between DRY and WET, and when is duplication OK?
**Answer:** **DRY** (Don't Repeat Yourself) avoids duplicated logic. But not all duplication is bad — "accidental" duplication with different reasons to change shouldn't be forced together (Rule of Three: refactor after 3rd occurrence). Over-abstraction can be worse than slight duplication.

## 5. How should you handle comments?
**Answer:** Comments are a failure to express intent in code — prefer self-documenting code. Use comments only for: legal/licensing, explanation of *why* (not what), warnings, and TODOs. Delete commented-out code. Never restate the obvious.

## 6. What is "one level of abstraction per function"?
**Answer:** A function should operate at a single conceptual level — don't mix high-level orchestration with low-level bit-twiddling. Example: `processOrder()` shouldn't contain raw SQL string concatenation; delegate to a repository. This keeps functions readable and testable.

## 7. What is feature envy and how do you fix it?
**Answer:** A method in class A heavily uses data/methods of class B. Fix by moving the method to B (where the data lives) or creating a method on B that A calls. This improves cohesion and follows "tell, don't ask."

## 8. What is the difference between command and query methods?
**Answer:** A **command** (mutator) changes state but returns nothing/void. A **query** returns data without side effects. Mixing both (a getter that also mutates) violates Command-Query Separation and confuses callers. Keep them distinct.

## 9. How do you write testable clean code?
**Answer:** Depend on abstractions (interfaces), inject dependencies, avoid static/global state, keep functions pure where possible, and isolate side effects at boundaries (I/O). Small, single-responsibility functions are trivially unit-testable.

## 10. What is the "boy scout rule"?
**Answer:** "Leave the code cleaner than you found it." Whenever you touch a file, make a small improvement (rename, extract, remove duplication). Over time the codebase improves incrementally rather than requiring big rewrites.

## 11. What are magic numbers and how do you avoid them?
**Answer:** Unnamed numeric literals scattered in code (e.g., `if (status == 3)`). Replace with named constants (`STATUS_SHIPPED`) or enums. This improves readability and prevents errors when the value's meaning changes.

## 12. How does clean code relate to code review and team velocity?
**Answer:** Clean code reduces cognitive load, making reviews faster and bugs rarer. At senior level, you set the standard — your PRs and reviews enforce naming, structure, and testing, directly impacting team velocity and onboarding speed.