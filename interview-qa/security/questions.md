# Security — Interview Q&A

## 1. What is OAuth2 and the Authorization Code flow?
**Answer:** OAuth2 is a delegation protocol for granting limited access without sharing credentials. **Auth Code** flow: user logs in at the provider → gets an auth code → server exchanges it (with secret) for an **access token** + refresh token. With **PKCE** for public clients (no secret). Tokens scoped to permissions.

## 2. What is JWT and how does it work?
**Answer:** JSON Web Token is a stateless, signed (HMAC/RSA) token with three base64 parts: header, **payload** (claims: sub, exp, roles), signature. The server verifies the signature and trusts the claims — no server-side session needed. Verify `exp` and signature on every request.

## 3. What is the difference between authentication and authorization?
**Answer:** **Authentication** verifies identity (who you are) — login, JWT, OAuth. **Authorization** verifies permissions (what you can do) — roles, scopes, policies. AuthN precedes AuthZ. Spring Security handles both via the `SecurityFilterChain`.

## 4. How does Spring Security's filter chain work?
**Answer:** A series of `Filter`s (e.g., `UsernamePasswordAuthenticationFilter`, `AuthorizationFilter`) intercept requests. They build an `Authentication` object, resolve it via `AuthenticationManager`, store it in the `SecurityContext`, and enforce authorization rules. Order matters.

## 5. What is CSRF and how do you prevent it?
**Answer:** Cross-Site Request Forgery tricks an authenticated user's browser into submitting unwanted state-changing requests. Prevent with **CSRF tokens** (synchronizer token pattern) — Spring Security enables this for browser sessions. APIs using JWT/stateless auth are typically immune (no cookie session).

## 6. What is CORS and how is it configured?
**Answer:** Cross-Origin Resource Sharing controls which domains may call your API from a browser. Configure allowed **origins**, **methods**, and **headers** via `CorsConfiguration`/`@CrossOrigin`. Never use `*` with credentials; whitelist specific origins.

## 7. What is the difference between symmetric and asymmetric JWT signing?
**Answer:** **Symmetric** (HS256) uses a shared secret (HMAC) — both issuer and verifier share the key; simple but the verifier can also forge. **Asymmetric** (RS256/ES256) uses a private key to sign and a public key to verify — verifiers can't forge; better for distributed systems.

## 8. What are the OWASP Top 10 key risks?
**Answer:** Notable ones: **Injection** (SQL), **Broken Authentication**, **Sensitive Data Exposure**, **XML External Entities**, **Broken Access Control**, **Security Misconfiguration**, **XSS**, **Insecure Deserialization**, **Using Components with Known Vulns**, **Insufficient Logging**. Know how to mitigate each.

## 9. How do you securely store passwords?
**Answer:** Never store plaintext. Use a **slow, salted** adaptive hash: **bcrypt**, **scrypt**, or **Argon2** (Spring Security's `PasswordEncoder`). Each hash includes a random salt. Avoid fast hashes (MD5/SHA) vulnerable to rainbow tables.

## 10. What is the difference between stateful and stateless auth?
**Answer:** **Stateful** (server-side session, JSESSIONID cookie) keeps session state on the server. **Stateless** (JWT) embeds claims in a signed token the client sends — no server storage, scales horizontally, but token revocation is harder (use short TTL + refresh/denylist).

## 11. What is the difference between encryption, hashing, and encoding?
**Answer:** **Encryption** is reversible (with key) — protects data in transit/at rest. **Hashing** is one-way (passwords, integrity) — can't reverse. **Encoding** (base64) is just representation, NOT security. Don't confuse them.

## 12. How do you handle token expiration and refresh?
**Answer:** Access tokens are short-lived (e.g., 15 min). A **refresh token** (longer-lived, stored securely, httpOnly cookie) is used to obtain a new access token without re-login. On refresh-token compromise, revoke it. Rotate refresh tokens on use.