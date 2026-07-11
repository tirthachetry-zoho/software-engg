# Kubernetes — Interview Q&A

## 1. What is the difference between a Pod and a Container?
**Answer:** A **Pod** is the smallest deployable unit — one or more **containers** that share network/IPC/storage and are scheduled together. Usually one container per pod; sidecars (logging, proxy) share a pod. You deploy/manage Pods, not raw containers, in K8s.

## 2. What is a Deployment and ReplicaSet?
**Answer:** A **ReplicaSet** ensures N pod replicas run (self-heals). A **Deployment** manages ReplicaSets, enabling **rolling updates/rollbacks** (declarative desired state). You usually create Deployments, not ReplicaSets directly.

## 3. What is a Service and the types?
**Answer:** A **Service** provides stable networking (VIP + DNS) to a set of pods (via label selector), decoupling clients from pod churn. Types: **ClusterIP** (internal), **NodePort** (node port), **LoadBalancer** (external LB), **Headless** (no VIP, for stateful/peer discovery).

## 4. What is the difference between ConfigMap and Secret?
**Answer:** **ConfigMap** stores non-sensitive config (env vars, files) — mounted/injected into pods. **Secret** stores sensitive data (passwords, tokens) — base64-encoded (not encrypted by default; use encryption-at-rest/externalsecrets). Both are referenced by pods, not baked into images.

## 5. What is a liveness, readiness, and startup probe?
**Answer:** **Liveness** — restart the pod if it fails (deadlock). **Readiness** — remove from Service rotation if not ready (still running, loading). **Startup** — give slow-start apps grace before liveness kicks in. Correct probes prevent traffic to broken pods and crash loops.

## 6. What is an Ingress?
**Answer:** **Ingress** exposes HTTP/HTTPS routes from outside to Services, with path/host-based routing, TLS termination, and rewrites — via an **Ingress Controller** (nginx, Traefik). It's L7 routing; for raw TCP use LoadBalancer/NodePort.

## 7. What is HPA (Horizontal Pod Autoscaler)?
**Answer:** **HPA** automatically scales pod replica count based on metrics (CPU%, custom metrics like QPS) against a target. It watches and adjusts the Deployment's replicas. Pair with resource requests/limits so scaling decisions are accurate.

## 8. What is the difference between HPA and VPA?
**Answer:** **HPA** scales **replica count** (horizontal, preferred — add pods). **VPA** (Vertical Pod Autoscaler) adjusts **resource requests/limits** (CPU/mem) of existing pods (requires restart). HPA is production-standard; VPA trickier (pod restart).

## 9. What is a Namespace and why use it?
**Answer:** A **Namespace** partitions a cluster into virtual sub-clusters for isolation, resource quotas, and RBAC scoping (e.g., dev/staging/prod, or per-team). Not a security boundary by itself, but aids organization and multi-tenancy.

## 10. What is the difference between a StatefulSet and Deployment?
**Answer:** **Deployment** is for stateless apps (pods interchangeable, random names). **StatefulSet** is for stateful apps — stable network IDs, **ordered** deploy/scale/terminate, and **persistent volumes** per pod (databases, ZooKeeper). Use StatefulSet when identity + storage matter.

## 11. What is a DaemonSet?
**Answer:** A **DaemonSet** runs **one pod per node** (or matched subset) — for node-level agents: log shippers (Fluentd), monitoring (node-exporter), networking (CNI). Ensures cluster-wide node coverage.

## 12. How does K8s handle storage (PV/PVC)?
**Answer:** **PersistentVolume (PV)** is a cluster storage resource (provisioned by admin or dynamically). **PersistentVolumeClaim (PVC)** is a pod's request for storage. Pods mount PVCs; the PVC binds to a PV. Storage survives pod restarts (decoupled from pod lifecycle).