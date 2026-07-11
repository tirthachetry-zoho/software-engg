# Spring Boot — Interview Q&A

## 1. What is auto-configuration?
**Answer:** Spring Boot automatically configures beans based on the **classpath** and properties. `@EnableAutoConfiguration` (via `@SpringBootApplication`) loads `META-INF/spring.factories` / `AutoConfiguration` classes that use `@ConditionalOnClass`, `@ConditionalOnMissingBean`, etc., to wire sensible defaults only when dependencies are present.

## 2. What is the difference between `@SpringBootApplication` and its parts?
**Answer:** It's a composite of: `@Configuration` (config class), `@EnableAutoConfiguration` (auto-config), and `@ComponentScan` (scan current package + subpackages). Together they bootstrap the app with minimal code.

## 3. How do profiles work?
**Answer:** Profiles (`@Profile("dev")`, `spring.profiles.active=dev`) activate subsets of beans/config. Use `application-{profile}.yml` for environment-specific properties (dev/test/prod). Beans not matching the active profile aren't created.

## 4. What is `@ConfigurationProperties`?
**Answer:** Binds external config (yml/properties) to a **type-safe** Java object (e.g., `@ConfigurationProperties("app.db")`). Preferred over `@Value` for structured, validated config (add `@Validated` for bean validation). Enables IDE completion and grouping.

## 5. What is the difference between `@Value` and `@ConfigurationProperties`?
**Answer:** `@Value("${key}")` injects a single property (supports SpEL) — fine for one-off values. `@ConfigurationProperties` binds a whole **prefix** to a POJO — better for structured, validated, refactoring-safe configuration.

## 6. How does Spring Boot handle transactions?
**Answer:** `@Transactional` (AOP proxy) starts a tx on method entry, commits on success, rolls back on `RuntimeException` (not checked by default). Key caveats: only works via proxy (self-invocation fails), default propagation REQUIRED, isolation DEFAULT.

## 7. What is the difference between `@Transactional` propagation types?
**Answer:** **REQUIRED** (join/create, default), **REQUIRES_NEW** (suspend current, new tx), **NESTED** (savepoint), **SUPPORTS** (use if exists), **NOT_SUPPORTED** (suspend), **NEVER** (error if exists), **MANDATORY** (error if none). Choose based on whether work must commit independently.

## 8. How do you implement validation?
**Answer:** Use Bean Validation (`jakarta.validation`): annotate DTOs with `@NotNull`, `@Size`, `@Email`, then `@Valid` on the controller param. Spring throws `MethodArgumentNotValidException` → handle via `@ControllerAdvice` returning 400 with field errors.

## 9. What is `@Scheduled` and `@Async`?
**Answer:** `@Scheduled(fixedRate = 5000)` runs a method periodically (enable with `@EnableScheduling`). `@Async` runs a method in a separate thread pool (enable `@EnableAsync`); returns `CompletableFuture`/`void`. Both need the enabling annotation and a configured executor for production.

## 10. How do you externalize and override configuration?
**Answer:** Precedence (low→high): defaults → `application.yml` → profile yml → env vars → command-line args (`--key=val`). Externalize secrets via env/secret managers, not committed files. Use `spring.config.import` for config servers.

## 11. What is the difference between `@Controller` and `@RestController`?
**Answer:** `@Controller` returns a view name (MVC, server-side rendering) unless `@ResponseBody`. `@RestController` = `@Controller` + `@ResponseBody` — methods return the serialized body (JSON) directly, the standard for REST APIs.

## 12. How do you change the embedded server or its port?
**Answer:** Spring Boot uses Tomcat by default; swap by excluding `tomcat` and adding `jetty`/`undertow` starter. Change port via `server.port=8081` (or `SERVER_PORT` env). Context path via `server.servlet.context-path`.