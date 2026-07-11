# Performance Tuning — Interview Q&A

## 1. How do you profile a Java application?
**Answer:** Use **async-profiler** (low-overhead, CPU/alloc/lock flamegraphs), **JFR** (Java Flight Recorder, continuous recording), **VisualVM**, and **JMC**. Start with CPU sampling to find hot methods, then allocation profiling for GC pressure. Always profile on realistic load.

## 2. What is the difference between CPU, memory, and allocation profiling?
**Answer:** **CPU** profiling finds hot methods (where time goes). **Allocation** profiling finds objects created most (GC pressure source). **Memory/heap** analysis (heap dump) finds retained objects (leaks). Each targets a different bottleneck.

## 3. What is JMH and when do you use it?
**Answer:** **JMH** (Java Microbenchmark Harness) measures the performance of small code snippets accurately (handles JVM warmup, dead-code elimination, forking). Use it for micro-optimizations — never `System.currentTimeMillis()` loops (misleading).

## 4. How do you take and analyze a heap dump?
**Answer:** Trigger via `jmap -dump:live,format=b,file=heap.hprof <pid>` or `-XX:+HeapDumpOnOutOfMemoryError`. Analyze with **Eclipse MAT** / **JVisualVM** — find the **dominator tree** and largest retained objects to locate leaks (static maps, ThreadLocals, unclosed resources).

## 5. How do you take a thread dump and read it?
**Answer:** `jstack <pid>` (or `kill -3`). Look for: threads in **BLOCKED** (contention on a monitor), **deadlocks** (JVM prints them), and thread pools exhausted (all busy/waiting). Map thread states to code via stack traces.

## 6. What is the difference between a flame graph and a call tree?
**Answer:** A **flame graph** is an inverted, SVG visualization where width = time, stacked by call hierarchy — instantly shows hot paths. A **call tree** is a textual/nested list. Flame graphs scale better for finding the widest (hottest) frames.

## 7. How do you reduce GC overhead?
**Answer:** Reduce allocation (object pooling for hot paths, avoid boxing in loops), right-size the heap, choose the right collector (G1 default, ZGC for low-latency), tune young gen, and eliminate `finalize()`. Measure GC logs (`-Xlog:gc*`) before/after.

## 8. What is the difference between throughput and latency optimization?
**Answer:** **Throughput** = total work/time (favor Parallel GC, batching). **Latency** = per-request time (favor G1/ZGC, low pause). They often trade off — know which the SLA demands before tuning.

## 9. How do you find a memory leak in production safely?
**Answer:** Enable `-XX:+HeapDumpOnOutOfMemoryError` + `-XX:HeapDumpPath`. On OOM, pull the dump (off the box), analyze retained objects in MAT. For slow leaks, compare two dumps over time (dominator delta). Avoid forced full GC in prod.

## 10. What is the difference between synchronous and asynchronous profiling?
**Answer:** **Synchronous** (sampling on safe-points, e.g., default profilers) can miss some paths (safepoint bias). **Async** (async-profiler, uses `perf`/APs) samples via signals/interrupts — accurate, low overhead, no safepoint bias. Prefer async-profiler.

## 11. How do you tune the JVM heap and GC?
**Answer:** Set `-Xms = -Xmx` (avoid resize), size young gen for object lifetime, and pick collector by SLA. Use `-Xlog:gc*` to observe pause times. Tune only against measured baselines — don't guess flags.

## 12. What are common Java performance anti-patterns?
**Answer:** Excessive **boxing** in loops, **string concatenation** in loops (use `StringBuilder`), **synchronized` on hot paths, creating objects in tight loops, `toString()` on large collections for logging, and **N+1 queries** (fetch joins). Profile to confirm, don't prematurely optimize.