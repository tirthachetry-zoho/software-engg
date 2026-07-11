# SQL — Interview Q&A

## 1. What is the difference between INNER, LEFT, RIGHT, and FULL JOIN?
**Answer:** **INNER** returns rows with matches in both tables. **LEFT** returns all left rows + matched right (NULLs if none). **RIGHT** is the mirror. **FULL OUTER** returns all rows from both, matching where possible. CROSS JOIN produces the Cartesian product.

## 2. How do you find the Nth highest salary?
**Answer:** Using `DENSE_RANK`/`ROW_NUMBER` window function: `SELECT salary FROM (SELECT salary, DENSE_RANK() OVER (ORDER BY salary DESC) r FROM emp) t WHERE r = N;`. Or for 2nd highest: `SELECT MAX(salary) FROM emp WHERE salary < (SELECT MAX(salary) FROM emp);`.

## 3. What is a window function and how does it differ from GROUP BY?
**Answer:** `ROW_NUMBER()`, `RANK()`, `SUM() OVER (PARTITION BY ... ORDER BY ...)` compute values across a set of rows *without collapsing them* — each row keeps its identity. `GROUP BY` aggregates and reduces rows. Window functions are ideal for running totals, rankings, and top-N-per-group.

## 4. What is a CTE and when would you use it?
**Answer:** A Common Table Expression (`WITH cte AS (...)`) is a named temporary result set, improving readability over nested subqueries and enabling recursion (`WITH RECURSIVE` for hierarchies like employee-manager trees). It's not materialized (usually) and scopes to one statement.

## 5. What is the difference between WHERE and HAVING?
**Answer:** `WHERE` filters rows **before** grouping/aggregation. `HAVING` filters **after** aggregation (operates on GROUP BY results, can use aggregate functions like `COUNT(*) > 1`). You cannot use aggregate conditions in WHERE.

## 6. How do indexes work and what is a composite index?
**Answer:** Indexes (usually B-Tree) speed up lookups by avoiding full table scans. A **composite index** `(a, b, c)` follows **leftmost-prefix**: queries filtering on `a`, or `a,b`, or `a,b,c` can use it, but `b` alone cannot. Order columns by selectivity and query patterns.

## 7. What is the difference between clustered and non-clustered index?
**Answer:** A **clustered** index determines the physical row order on disk (one per table, often the PK) — lookups are fast. **Non-clustered** indexes are separate structures with pointers to rows (many allowed). Clustered reads are faster; non-clustered add write overhead.

## 8. What are transactions and the ACID properties?
**Answer:** A transaction is a unit of work. **A**tomicity (all-or-nothing), **C**onsistency (valid state), **I**solation (concurrent txns don't interfere), **D**urability (committed data survives crashes). Enforced via logs, locks, and constraints.

## 9. What are the transaction isolation levels?
**Answer:** READ UNCOMMITTED (dirty reads possible) → READ COMMITTED (no dirty reads) → REPEATABLE READ (no non-repeatable reads) → SERIALIZABLE (full isolation, locks). Higher levels prevent more anomalies (dirty/non-repeatable/phantom reads) at the cost of concurrency.

## 10. What is a deadlock in SQL and how is it resolved?
**Answer:** Two transactions hold locks the other needs, waiting indefinitely. Databases detect deadlocks and **roll back** the cheaper/younger transaction. Prevent via consistent lock ordering, short transactions, and appropriate isolation.

## 11. What is the difference between DELETE, TRUNCATE, and DROP?
**Answer:** `DELETE` removes rows (can filter, logs each row, can rollback, fires triggers). `TRUNCATE` removes all rows instantly (minimal logging, resets identity, no WHERE). `DROP` removes the entire table structure. TRUNCATE/DROP are DDL; DELETE is DML.

## 12. How do you optimize a slow query?
**Answer:** Use `EXPLAIN`/`EXPLAIN ANALYZE` to see the plan; add/adjust indexes (composite, covering); avoid `SELECT *` and functions on indexed columns; rewrite subqueries as joins; analyze table statistics; consider partitioning for huge tables. Measure before/after.