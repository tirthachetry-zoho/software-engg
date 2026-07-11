# CI/CD — Interview Q&A

## 1. What is the difference between CI and CD?
**Answer:** **CI** (Continuous Integration) = frequently merging code + automated build/test on every push. **CD** = Continuous Delivery (auto-deploy to staging, manual prod) or Continuous Deployment (fully auto to prod). CI catches breaks early; CD ships fast/safely.

## 2. What is the difference between GitHub Actions, GitLab CI, Jenkins, and ArgoCD?
**Answer:** **GitHub Actions / GitLab CI** are YAML-based, hosted, repo-integrated. **Jenkins** is self-hosted, plugin-rich, scripted (Groovy) — flexible but ops-heavy. **ArgoCD** is GitOps — syncs K8s clusters from a Git repo (declarative, pull-based).

## 3. What is GitOps?
**Answer:** A CD practice where the **Git repo is the single source of truth** for desired cluster state; a controller (ArgoCD/Flux) continuously reconciles the cluster to match Git. Benefits: auditable, rollback via git revert, and drift detection.

## 4. What is a build pipeline stage?
**Answer:** A pipeline is split into **stages** (e.g., checkout → build → unit test → integration test → package → deploy). Stages run sequentially (or parallel jobs within), failing fast stops the pipeline. Each stage should be fast and isolated.

## 5. How do you cache dependencies in CI for speed?
**Answer:** Use the CI's cache (GitHub Actions `cache`, GitLab `cache`, Jenkins `stash`/artifacts) keyed by lockfile hash (e.g., `pom.xml`, `package-lock.json`). Restores `~/.m2`, `~/.gradle`, `node_modules` between runs — drastically cuts build time.

## 6. What is the difference between blue-green and canary deployment?
**Answer:** **Blue-green** = run two identical envs; switch traffic from old (blue) to new (green) atomically (instant rollback). **Canary** = gradually shift a small % of traffic to the new version, watch metrics, then ramp. Canary is lower-risk, slower.

## 7. What is the difference between a rolling update and recreate?
**Answer:** **Rolling** = replace pods gradually (some always serving) — zero downtime, but old+new coexist briefly. **Recreate** = kill all then start new — downtime, but simple and consistent state. K8s Deployments default to rolling.

## 8. How do you handle secrets in CI/CD?
**Answer:** Never commit secrets. Use the CI's **secret store** (masked env vars), or an external vault (HashiCorp Vault, AWS Secrets Manager) fetched at runtime. Inject as env vars, not files in the repo. Rotate regularly.

## 9. What is the difference between artifact and image registry?
**Answer:** An **artifact repository** (Nexus, Artifactory, Maven Central) stores build outputs (jars, npm, docker layers metadata). A **container registry** (ECR, GCR, Docker Hub) stores Docker images. Both version and distribute build outputs.

## 10. How do you make CI fast and reliable?
**Answer:** Parallelize test suites, cache deps, use layered Docker builds, run only affected tests (monorepo), fail fast on lint/compile, and keep flaky tests fixed/quarantined. Fast feedback = higher dev velocity.

## 11. What is the difference between push and pull-based CD?
**Answer:** **Push** (Jenkins/GitHub Actions) = the CI tool connects to the cluster and applies changes. **Pull/GitOps** (ArgoCD) = the cluster watches Git and pulls changes itself. Pull is more secure (cluster isn't exposed to CI) and self-healing.

## 12. What is a release vs a deployment?
**Answer:** A **deployment** is shipping code to an environment (may be behind a feature flag, invisible to users). A **release** is making a feature available to users (via flag/traffic shift). Decoupling them lets you deploy often and release safely.