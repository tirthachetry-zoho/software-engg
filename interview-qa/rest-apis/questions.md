# REST APIs — Interview Q&A

## 1. What are the characteristics of a RESTful API?
**Answer:** REST constraints: **stateless** (each request has all info), **client-server** separation, **cacheable** responses, **uniform interface** (resource URIs, HTTP verbs, representations), **layered system**, and optional **code-on-demand**. Resources are nouns (e.g., `/users`), verbs are HTTP methods.

## 2. What is the difference between PUT and PATCH?
**Answer:** **PUT** is idempotent and replaces the **entire** resource (missing fields become null/default). **PATCH** is for **partial** updates (only changed fields). PUT with same body twice = same result; PATCH semantics can vary but should be idempotent too.

## 3. What is idempotency and why does it matter?
**Answer:** An idempotent operation produces the same result no matter how many times it's repeated (GET, PUT, DELETE are idempotent; POST isn't). Critical for **retries** — a network retry of a POST may duplicate, so use idempotency keys (e.g., `Idempotency-Key` header) for safe retries.

## 4. How do you version a REST API?
**Answer:** Common strategies: **URI** (`/v1/users`), **header** (`Accept: application/vnd.api.v1+json`), or **query** (`?version=1`). URI versioning is simplest/most visible; header keeps URLs clean. Avoid breaking changes within a version; deprecate gracefully.

## 5. What is the difference between REST and GraphQL?
**Answer:** REST exposes fixed resource endpoints (over/under-fetching possible). GraphQL lets clients **query exactly** the fields they need via a single endpoint — solves over-fetching, enables strong typing, but adds caching/complexity challenges. Choose based on client diversity and data shape needs.

## 6. How do you implement pagination?
**Answer:** **Offset** (`?page=2&size=20`) — simple but slow on deep pages. **Keyset/Cursor** (`?cursor=lastId&limit=20`) — fast, stable during inserts, preferred at scale. **Seek** pagination avoids OFFSET scan. Always return total count or next-cursor.

## 7. What is HATEOAS?
**Answer:** Hypermedia As The Engine Of Application State — responses include **links** to related actions (e.g., `_links: { next, self, cancel }`), letting clients navigate the API dynamically without hardcoding URLs. Drives discoverability; used by Spring HATEOAS.

## 8. How do you handle errors in REST?
**Answer:** Use proper status codes (400 bad request, 401/403 auth, 404 not found, 409 conflict, 422 validation, 500 server) and a consistent error body: `{ "timestamp", "status", "error", "message", "path", "fieldErrors": [...] }`. Never leak stack traces to clients.

## 9. What is the difference between authentication and authorization?
**Answer:** **Authentication** = who you are (login, JWT, OAuth). **Authorization** = what you're allowed to do (roles/permissions, scopes). AuthN happens first, then AuthZ checks access. Both enforced via filters/interceptors.

## 10. What are idempotency keys used for?
**Answer:** A client-sent unique key (header) lets the server dedupe repeated requests (e.g., double payment submit). The server stores the result of the first request and returns it for retries with the same key — preventing duplicate side effects.

## 11. How do you design a clean resource URL structure?
**Answer:** Use plural nouns (`/users`), nested sub-resources for relations (`/users/123/orders`), avoid verbs in URLs (use POST to an action resource if needed, e.g., `/orders/123/cancel`), and keep hierarchies shallow (≤2 levels). Use query params for filtering/sorting.

## 12. What is the difference between 401 and 403?
**Answer:** **401 Unauthorized** = authentication required/failed (no valid credentials). **403 Forbidden** = authenticated but not permitted (insufficient rights). 401 usually prompts login; 403 means "I know who you are, but no."