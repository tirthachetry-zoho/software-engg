# Testing — Interview Q&A

## 1. What is the testing pyramid?
**Answer:** A guidance for test distribution: many **unit tests** (fast, isolated) at the base, fewer **integration tests** (components together) in the middle, and few **E2E/UI tests** (slow, brittle) at the top. Inverts the "ice cream cone" anti-pattern (too many E2E).

## 2. What is the difference between unit, integration, and E2E tests?
**Answer:** **Unit** tests a single class/method with mocks (fast). **Integration** tests multiple components together (DB, HTTP) — real deps or testcontainers. **E2E** tests the full system via the UI/API (slow, realistic). Each has a distinct cost/benefit.

## 3. What is JUnit 5 and its key annotations?
**Answer:** JUnit 5 = `JUnit Jupiter`. Annotations: `@Test`, `@BeforeEach`/`@AfterEach`, `@BeforeAll`/`@AfterAll` (static), `@DisplayName`, `@Nested`, `@ParameterizedTest`, `@Tag`. Assertions via `Assertions` (`assertEquals`, `assertThrows`).

## 4. What is Mockito and how do you mock?
**Answer:** Mockito creates **test doubles** (mocks/spies). `when(mock.method()).thenReturn(x)` stubs; `verify(mock).method()` asserts interaction. Use `@Mock` + `@ExtendWith(MockitoExtension.class)` or `Mockito.mock()`. Mock dependencies, not the class under test.

## 5. What is the difference between mocking and stubbing?
**Answer:** **Stubbing** = pre-programing a fake's return value (`when().thenReturn()`). **Mocking** = also **verifying** interactions (`verify()` that a method was/wasn't called). A mock is a stub that also records calls; a pure stub only returns canned data.

## 6. What is Testcontainers?
**Answer:** A library that spins up **real** dependencies (Postgres, Kafka, Redis) in Docker during tests — no mocks, high fidelity. Replaces in-memory fakes for integration tests. Ensures tests match production behavior. Use `@Container` + `@Testcontainers`.

## 7. What is the difference between TDD and BDD?
**Answer:** **TDD** = write a failing test, then code to pass it (red-green-refactor). **BDD** (Behavior-Driven) = specs in natural language (Given/When/Then, Cucumber) shared with non-devs. BDD is TDD with a business-readable layer.

## 8. What is contract testing?
**Answer:** Verifies that a **provider** and **consumer** of an API agree on the contract (request/response shape) — e.g., Pact. Catches breaking API changes without full E2E. Essential in microservices where many teams own services.

## 9. What is the difference between a mock, stub, fake, and spy?
**Answer:** **Stub** returns canned data. **Mock** is a stub that also verifies calls. **Fake** is a working lightweight impl (e.g., in-memory repo). **Spy** wraps a real object, recording calls while delegating real behavior (partial mocking).

## 10. How do you test a Spring Boot app?
**Answer:** `@SpringBootTest` for full context integration; `@WebMvcTest` for controller slice (mock services); `@DataJpaTest` for repo slice (Testcontainers/H2). Use `MockMvc`/`TestRestTemplate` for HTTP. Slice tests are faster than full context.

## 11. What is code coverage and its limitation?
**Answer:** Coverage = % of code exercised by tests (line/branch). High coverage ≠ good tests (you can hit lines without asserting behavior). Use coverage as a **signal**, not a goal; prioritize meaningful assertions and edge cases over 100%.

## 12. How do you handle flaky tests?
**Answer:** Flaky tests erode trust. Fix root causes: non-determinism (time, randomness, concurrency), shared state between tests (use `@BeforeEach` cleanup), brittle waits (use awaitility/explicit waits), and test order dependence. Quarantine flaky tests until fixed.