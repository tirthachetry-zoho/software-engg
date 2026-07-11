# Algorithms — Interview Q&A (with LeetCode problems)

## 1. Binary Search — Search in Rotated Sorted Array (LeetCode #33, Medium)
**Q:** Find a target in a rotated sorted array in O(log n).
**A:** Standard binary search with a twist: determine which half is sorted (`nums[lo] <= nums[mid]`), then check if target lies in that sorted half to narrow. O(log n). Teaches binary search on answer space / invariants.

## 2. Binary Search on Answer — Koko Eating Bananas (LeetCode #875, Medium)
**Q:** Find min hourly banana-eating speed to finish in `h` hours.
**A:** Binary search the speed `k` in `[1, maxPile]`. For a candidate `k`, compute hours = `sum(ceil(pile/k))`; if ≤ h it's feasible (search left), else right. O(n log maxPile). Teaches BS on the *answer*, not an index.

## 3. Two Pointers — Container With Most Water (LeetCode #11, Medium)
**Q:** Max area between two lines.
**A:** Two pointers at ends; area = `min(h[l], h[r]) * (r-l)`. Move the *shorter* pointer inward (taller can't improve by moving). O(n). Teaches greedy two-pointer elimination.

## 4. Sliding Window — Longest Substring Without Repeating (LeetCode #3, Medium)
**Q:** Longest substring with all unique chars.
**A:** Expand `right`, add to a HashSet/lastSeen map; on duplicate, move `left` past the last occurrence. Track max window. O(n). Teaches variable-size sliding window.

## 5. Prefix Sum — Subarray Sum Equals K (LeetCode #560, Medium)
**Q:** Count subarrays summing to k.
**A:** Running prefix sum `ps`; count how many previous prefixes equal `ps - k` using a HashMap (init `count[0]=1`). O(n). Teaches prefix-sum + hashmap pattern.

## 6. Greedy — Jump Game (LeetCode #55, Medium)
**Q:** Can you reach the last index if `nums[i]` = max jump from i?
**A:** Track `maxReach` as you iterate; if `i > maxReach` you're stuck. Update `maxReach = max(maxReach, i + nums[i])`. O(n). Teaches greedy reachability.

## 7. Dynamic Programming — Climbing Stairs (LeetCode #70, Easy)
**Q:** Ways to climb n stairs taking 1 or 2 steps.
**A:** `dp[i] = dp[i-1] + dp[i-2]` → Fibonacci. Optimize to O(1) with two variables. Teaches the DP state-transition foundation.

## 8. DP — Longest Increasing Subsequence (LeetCode #300, Medium)
**Q:** Length of longest strictly increasing subsequence.
**A:** DP `dp[i]` = LIS ending at i (O(n²)), or use a **patience-sorting** tails array with binary search for O(n log n). Teaches DP + binary search optimization.

## 9. DP — Coin Change (LeetCode #322, Medium)
**Q:** Minimum coins to make amount (unbounded knapsack).
**A:** `dp[a]` = min coins for amount `a`; `dp[a] = min(dp[a], dp[a-coin]+1)`. Init `dp[0]=0`, others ∞. O(amount × coins). Teaches unbounded knapsack / min-coin DP.

## 10. Backtracking — N-Queens (LeetCode #51, Hard)
**Q:** Place n queens on an n×n board with no conflicts.
**A:** Backtrack row by row, tracking occupied columns and diagonals (sets/bitmasks); prune invalid placements; collect solutions. Teaches backtracking + constraint propagation.

## 11. Graph BFS/DFS — Course Schedule (LeetCode #207, Medium)
**Q:** Can you finish all courses given prerequisites (detect cycle)?
**A:** Build a directed graph; use **topological sort** (Kahn's BFS with indegree, or DFS with visited/recursion-stack colors). A cycle ⇒ impossible. Teaches cycle detection + topo sort.

## 12. Shortest Path — Dijkstra (concept; LC #743 Network Delay Time, Medium)
**Q:** Time for a signal to reach all nodes in a weighted graph.
**A:** **Dijkstra** with a min-heap from the source; relax edges; answer = max distance among reachable nodes (or -1 if disconnected). O((V+E) log V). Teaches priority-queue shortest paths.

## 13. String — Longest Palindromic Substring (LeetCode #5, Medium)
**Q:** Find the longest palindromic substring.
**A:** Expand around each center (2n-1 centers: odd/even). For each center expand while chars match; track max. O(n²). Teaches center-expansion technique.

## 14. Divide & Conquer — Merge Sort / Kth Smallest in Matrix (LeetCode #378, Medium)
**Q:** Find kth smallest in a sorted-by-row-and-column matrix.
**A:** Binary search the *value* range `[min,max]`; count elements ≤ mid via row-wise upper_bound (O(n log max)). O(n log max). Teaches BS-on-answer for 2D.

## 15. Union-Find / MST — Min Cost to Connect All Points (LeetCode #1584, Medium)
**Q:** Connect all points with min total distance (MST).
**A:** **Kruskal**: build all edges, sort by weight, union-find to add edges avoiding cycles until n-1 chosen. O(E log E). Teaches MST + DSU.