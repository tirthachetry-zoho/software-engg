# Goldman Sachs — Interview Preparation (DSA + HLD + LLD)

> Curated, frequently-asked **Goldman Sachs** interview problems with **Java** solutions and explanations.
> Goldman Sachs is known for some of the hardest coding screens in the industry: multiple problems per round, heavy on **DP, stacks, two-pointers, arrays, strings, graphs, and design**. The final rounds include **Low-Level Design (LLD)** and **High-Level Design (HLD)**, often in a finance/trading context.

## Folder Structure

```
GoldmanSachs/
├── README.md                          # This index
├── dsa/
│   └── GoldmanDSA.java                # 30+ DSA problems with explanations (runnable)
├── lld/                               # Low-Level Design (OOP, classes, patterns)
│   ├── DesignOrderBook.java           # Limit-order book / matching engine (GS classic)
│   ├── DesignLRUCache.java            # LRU cache (doubly-linked list + HashMap)
│   ├── DesignRateLimiter.java         # Token-bucket / sliding-window rate limiter
│   └── DesignCalculator.java          # Expression evaluator (Shunting-yard)
└── hld/                               # High-Level Design (scalable systems)
    ├── DesignTradingSystem.java       # Order matching + execution + clearing
    ├── DesignMarketDataFeed.java      # Low-latency market-data distribution
    └── DesignRiskManagementSystem.java# Real-time risk & limits engine
```

## DSA Topics Covered
- **Arrays / Two Pointers**: Two Sum, 3Sum, Trapping Rain Water, Container With Most Water, Best Time to Buy/Sell, Product Except Self, Subarray Sum = K, Median of Two Sorted Arrays
- **Stacks / Monotonic**: Valid Parentheses, Largest Rectangle in Histogram, Daily Temperatures
- **Strings**: Valid Palindrome, String to Integer (atoi), Minimum Window Substring, Regular Expression Matching, Decode Ways, Longest Substring Without Repeating
- **DP / Backtracking**: Coin Change, Edit Distance, Word Break, Permutations, Combination Sum, Maximum Product Subarray, Climbing Stairs
- **Graphs / Trees**: Number of Islands, Course Schedule, Word Ladder, Level Order, LCA
- **Design**: LRU Cache, HashMap, Merge Intervals, Word Search

## How to Run
```bash
# DSA
javac companywise/GoldmanSachs/dsa/GoldmanDSA.java && java companywise.GoldmanSachs.dsa.GoldmanDSA

# LLD
javac companywise/GoldmanSachs/lld/DesignOrderBook.java && java companywise.GoldmanSachs.lld.DesignOrderBook
javac companywise/GoldmanSachs/lld/DesignLRUCache.java && java companywise.GoldmanSachs.lld.DesignLRUCache
javac companywise/GoldmanSachs/lld/DesignRateLimiter.java && java companywise.GoldmanSachs.lld.DesignRateLimiter
javac companywise/GoldmanSachs/lld/DesignCalculator.java && java companywise.GoldmanSachs.lld.DesignCalculator

# HLD
javac companywise/GoldmanSachs/hld/DesignTradingSystem.java && java companywise.GoldmanSachs.hld.DesignTradingSystem
javac companywise/GoldmanSachs/hld/DesignMarketDataFeed.java && java companywise.GoldmanSachs.hld.DesignMarketDataFeed
javac companywise/GoldmanSachs/hld/DesignRiskManagementSystem.java && java companywise.GoldmanSachs.hld.DesignRiskManagementSystem
```

## Interview Tips for Goldman Sachs
1. **Multiple problems per round** — practice speed AND correctness.
2. **Always discuss complexity** (time + space) before coding.
3. **Handle edge cases** — empty input, overflow, duplicates, negative numbers.
4. **HLD rounds** favor finance systems: trading, risk, market data, clearing.
5. **LLD rounds** favor clean OOP: order books, caches, rate limiters, calculators.