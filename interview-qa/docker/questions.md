# Docker — Interview Q&A

## 1. What is the difference between an image and a container?
**Answer:** An **image** is an immutable, layered blueprint (filesystem + metadata). A **container** is a running (or stopped) instance of an image — a writable layer on top of the read-only image. You build images, run containers.

## 2. How does Docker layering work?
**Answer:** Each `Dockerfile` instruction creates a **layer** (a diff). Layers are cached and reused across builds/images — changing a later line invalidates only that and subsequent layers. Keep stable steps (deps) early for cache hits and faster rebuilds.

## 3. What is a multi-stage build and why use it?
**Answer:** Multiple `FROM` stages in one Dockerfile: build the app in a full JDK image, then copy only the artifact into a slim runtime (JRE) image. Drastically reduces final image size and attack surface. Example: `FROM maven ...` → `FROM eclipse-temurin:21-jre COPY --from=build ...`.

## 4. What is the difference between COPY and ADD?
**Answer:** `COPY` copies local files/dirs into the image (simple, predictable). `ADD` also copies but additionally supports **auto-extracting tar** and **remote URL** fetching. Prefer `COPY`; use `ADD` only when you need tar extraction.

## 5. What is the difference between CMD and ENTRYPOINT?
**Answer:** `ENTRYPOINT` defines the **executable** (the container's main command). `CMD` provides **default arguments** to ENTRYPOINT (or the command if no ENTRYPOINT). Overriding at run time replaces CMD but not ENTRYPOINT. Use ENTRYPOINT+CMD for flexible defaults.

## 6. How do you manage persistent data in Docker?
**Answer:** Containers are ephemeral; use **volumes** (Docker-managed, persistent, performant) or **bind mounts** (host dir). Named volumes survive container restarts and are the recommended way to persist DB data. Don't store state in the container writable layer.

## 7. What is the difference between a volume and a bind mount?
**Answer:** **Volume** is managed by Docker (stored in Docker's area, portable, backed up easily, better perf on some systems). **Bind mount** maps a specific host path (good for dev config/code hot-reload, but couples to host). Volumes are preferred for production data.

## 8. What is the difference between Docker bridge and host networking?
**Answer:** **Bridge** (default) — isolated virtual network, containers get internal IPs, ports published via `-p`. **Host** — container shares the host's network namespace (no isolation, max perf, port conflicts possible). Use bridge for isolation, host for low-latency single-host needs.

## 9. How do you reduce Docker image size?
**Answer:** Use **slim/alpine** base images, **multi-stage builds**, minimize layers (combine RUNs, clean caches in same layer), avoid dev dependencies, and leverage layer caching. A small image = faster pulls, less attack surface, lower cost.

## 10. What is the difference between `docker-compose` and `docker run`?
**Answer:** `docker run` starts a single container with flags. **`docker-compose`** defines **multi-container** apps (services, networks, volumes) in YAML — reproducible, version-controlled local orchestration. Compose is for dev/test; K8s for prod.

## 11. What are the security best practices for Docker?
**Answer:** Run as **non-root** user, use minimal/signed base images, scan for CVEs (Trivy), don't embed secrets (use secrets/env), keep images patched, drop Linux capabilities, and use read-only root filesystems where possible.

## 12. What is the difference between a container and a VM?
**Answer:** A **VM** virtualizes hardware + full guest OS (heavy, strong isolation). A **container** shares the host OS kernel (lightweight, fast startup, less isolation). Containers are better for microservices density; VMs for strong isolation/multi-OS.