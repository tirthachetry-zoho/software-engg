# ElasticSearch — Interview Q&A

## 1. What is an index, document, and mapping?
**Answer:** An **index** is like a DB table (a logical namespace for documents). A **document** is a JSON record (like a row). A **mapping** defines the schema — field names and their **types** (text, keyword, integer, date, nested). Mappings are schema-on-write (enforced at index time).

## 2. What is the difference between `text` and `keyword` types?
**Answer:** **text** is analyzed (tokenized, lowercased) for **full-text search** (e.g., "Quick Fox" → ["quick","fox"]). **keyword** is NOT analyzed — stored as-is for **exact** matching, aggregations, and sorting (e.g., status, email, IDs). Choosing wrong breaks search/aggs.

## 3. What is an analyzer and its components?
**Answer:** An **analyzer** converts text to tokens at index/search time. It has: **Character filters** (pre-process), **Tokenizer** (split into tokens, e.g., standard), **Token filters** (lowercase, stopwords, stemming). The same analyzer should be used at index and query time for consistency.

## 4. What is the Query DSL and common query types?
**Answer:** JSON-based query language. **term** = exact match (keyword). **match** = full-text analyzed match. **bool** = combine must/should/must_not/filter. **range** = numeric/date ranges. **exists**, **prefix**, **wildcard**, **nested** for object arrays.

## 5. What is the difference between `match` and `term` queries?
**Answer:** `term` queries the **exact**, unanalyzed value (use on keyword/ids). `match` applies the analyzer (tokenizes the input) — use for full-text search on text fields. Using `term` on a `text` field usually returns nothing because the stored tokens differ from the raw input.

## 6. What are aggregations?
**Answer:** ES's analytics engine (like SQL GROUP BY + more). **Bucket** aggs group docs (terms, date_histogram, range). **Metric** aggs compute values (avg, sum, max, cardinality). **Pipeline** aggs operate on other aggs. Powerful for dashboards/facets.

## 7. How does ES achieve near-real-time search?
**Answer:** Writes go to an in-memory buffer, then a **refresh** (default 1s) flushes to a new **segment** (in-memory, searchable via a refresh). A **flush** later persists segments to disk and clears the translog. This 1s refresh gives NRT, not instant.

## 8. What is a shard and replica?
**Answer:** An index is split into **primary shards** (the unit of distribution/parallelism). Each primary can have **replica shards** (copies) for HA and read scaling. More shards = more parallelism but overhead; replicas protect against node loss.

## 9. What is the difference between ES and a relational DB?
**Answer:** RDBMS = ACID, strict schema, JOINs, transactions — great for structured OLTP. ES = distributed, schema-flexible, inverted-index **full-text search** + analytics — great for search/logs. ES is NOT a primary system of record (use a DB + sync to ES).

## 10. How do you handle nested objects / arrays of objects?
**Answer:** By default ES flattens object arrays, breaking intra-object relationships. Use **`nested`** type + **`nested` query** to keep each object independent (separate hidden docs), or **`object`** (flattened, loses pairing). Choose nested when you query within one object.

## 11. What is the difference between `filter` and `query` context?
**Answer:** **Query** context scores relevance (affects `_score`) — used for full-text matching. **Filter** context is boolean (yes/no), **not scored**, and **cached** — used for exact/range conditions (status, date). Put non-relevance conditions in filter for speed.

## 12. How do you reindex or update a mapping?
**Answer:** Mappings are mostly immutable (can add new fields). To change a field type, use the **Reindex API**: create a new index with the desired mapping, `POST _reindex` from old to new, then alias-swap. Avoid downtime via index aliases.