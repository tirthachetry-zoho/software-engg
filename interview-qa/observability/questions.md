# Observability — Interview Q&A

## 1. What are the three pillars of observability?
**Answer:** **Metrics** (numeric time-series: CPU, latency, QPS — Prometheus), **Logs** (discrete events: ELK/ Loki), and **Traces** (request flow across services: Jaeger/ Tempo). Together they let you understand system behavior and debug.

## 2. What is the difference between metrics, logs, and traces?
**Answer:** **Metrics** = aggregated numbers (cheap, for alerting/dashboards). **Logs** = detailed text events (debugging, high volume). **Traces** = the path of one request through services (latency per span). Use metrics for alerts, traces for "why slow", logs for "what happened".

## 3. What is Prometheus and how does it work?
**Answer:** A **pull-based** TSDB that scrapes `/metrics` endpoints on an interval, stores time-series, and queries via **PromQL**. Great for metrics + alerting (Alertmanager). Pull model simplifies target discovery via service discovery.

## 4. What is the difference between Prometheus and Grafana?
**Answer:** **Prometheus** collects/stores metrics and evaluates alerts. **Grafana** is a **dashboard/visualization** tool that queries Prometheus (and many other sources). Prometheus = data + alerting; Grafana = UI.

## 5. What is OpenTelemetry (OTel)?
**Answer:** A vendor-neutral standard/SDK for generating **traces, metrics, and logs** — instrument once, export anywhere (Jaeger, Prometheus, etc.). Replaces proprietary agents and unifies instrumentation across languages.

## 6. What is distributed tracing and why is it needed?
**Answer:** Tracing follows a single request across **multiple microservices**, showing each span's latency and dependencies. Essential in distributed systems where a slow call could be in any service. Uses context propagation (trace/span IDs via headers).

## 7. What is the difference between RED and USE monitoring?
**Answer:** **RED** (for services/requests): Rate, Errors, Duration. **USE** (for resources): Utilization, Saturation, Errors. RED for app health; USE for infra (CPU, disk). Both are focused, actionable metric sets.

## 8. What is the ELK stack?
**Answer:** **E**lasticsearch (search/index), **L**ogstash (ingest/transform), **K**ibana (visualize). Modern variant: **EFK** (Fluentd) or **Loki** (Grafana's log system, cheaper). Centralized logs for search/debug across services.

## 9. What is the difference between sampling and full tracing?
**Answer:** **Full** tracing captures every request (high cost at scale). **Sampling** captures a fraction (head-based random, or tail-based keeping errors/slow) — balances cost vs coverage. Tail-based is better (keeps interesting traces).

## 10. What is an SLO, SLI, and SLA?
**Answer:** **SLI** = the actual metric (e.g., 99.5% success). **SLO** = your target for the SLI (99.9%). **SLA** = the contractual commitment (often with penalties). Error budgets = 100% − SLO; burn rate drives alerts/release decisions.

## 11. What is the difference between black-box and white-box monitoring?
**Answer:** **Black-box** = external probes (synthetic checks, "can users reach it?") — no internal knowledge. **White-box** = internal metrics (JVM, DB connections) — deep visibility. Use both: black-box for user-impact, white-box for root cause.

## 12. How do you set up alerting without alert fatigue?
**Answer:** Alert on **symptoms** (high error rate, latency SLO breach), not causes. Use **error budgets** + multi-window burn-rate alerts, group/dedupe, and page only for actionable, urgent issues. Route warnings to chat, critical to pager.