# DSA Problems List

A curated set of LeetCode problems implemented in this `dsa/` directory, grouped by topic. Each entry includes the LeetCode number, the problem, and a short explanation of the approach and time complexity.

## Arrays (`Arrays.java`)
- **LC #1 Two Sum** — Find two indices whose values sum to a target. Use a hash map to store `value → index` and check for `target - num` in O(n).
- **LC #121 Best Time to Buy and Sell Stock** — Max profit from one buy/sell. Track running minimum price and max difference in a single O(n) pass.
- **LC #53 Maximum Subarray (Kadane)** — Largest sum of a contiguous subarray. Keep a running `current` sum, reset to current element when it exceeds the accumulated sum, O(n).
- **LC #238 Product of Array Except Self** — Product of all elements except self, no division. Prefix and suffix products in two passes, O(n).
- **LC #56 Merge Intervals** — Merge overlapping intervals. Sort by start, then merge adjacent overlapping ranges, O(n log n).
- **LC #15 3Sum** — All unique triplets summing to 0. Sort, fix one element, two-pointer the rest, skip duplicates, O(n²).
- **LC #189 Rotate Array** — Rotate array right by k. Reverse whole array, then reverse first k and remaining parts, O(n).
- **LC #169 Majority Element** — Element appearing > n/2 times. Boyer-Moore voting: cancel pairs, O(n).
- **LC #48 Rotate Image** — Rotate N×N matrix 90°. Transpose then reverse each row, O(n²).

## Algorithms (`Algorithms.java`)
- **LC #33 Search in Rotated Sorted Array** — Binary search on a rotated sorted array by comparing mid against the sorted half, O(log n).
- **LC #875 Koko Eating Bananas** — Minimum eating speed to finish in h hours. Binary search on the answer, O(n log max).
- **LC #11 Container With Most Water** — Max area between two lines. Two pointers from both ends, move the shorter one, O(n).
- **LC #560 Subarray Sum Equals K** — Count subarrays summing to k. Prefix-sum + hash map of seen prefix counts, O(n).
- **LC #70 Climbing Stairs** — Ways to climb n steps (1 or 2 at a time). Fibonacci DP, O(n).
- **LC #300 Longest Increasing Subsequence** — Length of LIS. Patience sorting with binary search, O(n log n).
- **LC #322 Coin Change** — Fewest coins for an amount. Unbounded knapsack DP, O(amount × coins).
- **LC #55 Jump Game** — Can you reach the last index? Greedy furthest-reach tracking, O(n).
- **LC #62 Unique Paths** — Paths in an m×n grid. DP where each cell = sum of top + left, O(mn).
- **LC #5 Longest Palindromic Substring** — Expand around every center, O(n²).

## Backtracking (`Backtracking.java`)
- **LC #46 Permutations** — All orderings of an array. Swap-based backtracking, O(n·n!).
- **LC #78 Subsets** — All subsets. Include/exclude each element, O(2ⁿ).
- **LC #39 Combination Sum** — Combinations summing to target (reuse allowed). Backtrack with start index, O(2ⁿ).
- **LC #17 Letter Combinations of a Phone Number** — All digit→letter mappings. DFS over digits, O(4ⁿ).
- **LC #79 Word Search** — Find a word in a grid. DFS with visited marking, O(m·n·4ᴸ).
- **LC #22 Generate Parentheses** — All valid `n`-pair parentheses. Track open/close counts, O(2ⁿ).
- **LC #130 Surround Regions** — Flip 'O' regions not connected to the border. DFS from border, mark safe cells, O(mn).

## Bit Manipulation (`BitManipulation.java`)
- **LC #136 Single Number** — One element appears once, rest twice. XOR all, pairs cancel, O(n).
- **LC #137 Single Number II** — One appears once, rest thrice. Two bitmasks counting mod 3, O(n).
- **LC #260 Single Number III** — Two singles, rest twice. XOR then partition by rightmost set bit, O(n).
- **LC #191 Number of 1 Bits** — Count set bits. Brian Kernighan: `n &= n-1`, O(1).
- **LC #338 Counting Bits** — Set-bit count for 0..n. DP `dp[i] = dp[i>>1] + (i&1)`, O(n).
- **LC #461 Hamming Distance** — Differing bits of two ints. XOR then count bits, O(1).
- **LC #476 Number Complement** — Flip all bits up to the highest set bit. Mask XOR, O(1).
- **LC #231 Power of Two** — Check `n > 0 && (n & (n-1)) == 0`, O(1).

## Dynamic Programming (`DynamicProgramming.java`)
- **LC #70 Climbing Stairs** — Same as above; iterative O(n)/O(1) DP.
- **LC #322 Coin Change** — Min coins (see Algorithms); DP table version.
- **LC #300 Longest Increasing Subsequence** — Patience sorting version.
- **LC #1143 Longest Common Subsequence** — `dp[i][j]` over two strings, O(mn).

## Greedy (`Greedy.java`)
- **LC #55 / #45 Jump Game / Jump Game II** — Reachability and minimum jumps via greedy furthest reach, O(n).
- **LC #121 / #122 Best Time to Buy and Sell Stock (I & II)** — One vs. multiple transactions, O(n).
- **LC #134 Gas Station** — Find a valid starting station. Track net fuel, O(n).
- **LC #135 Candy** — Min candies with rating constraints. Left/right passes, O(n).
- **LC #406 Queue Reconstruction by Height** — Sort by height desc, insert by k, O(n²).
- **LC #621 Task Scheduler** — Min intervals with cooldown. Schedule most frequent first, O(n).
- **LC #763 Partition Labels** — Greedy by last-seen index, O(n).
- **LC #56 / #435 / #452 Merge / Non-overlapping / Min Arrows** — Interval greedy by end time, O(n log n).
- **LC #1029 Two City Scheduling** — Min cost via refund sorting, O(n log n).
- **LC #870 Advantage Shuffle** — Maximize wins (Highest-Card-Wins), O(n log n).
- **LC #334 / #605 / #392 / #846 / #659** — Increasing triplet, place flowers, subsequence, hand of straights, consecutive splits — various greedy scans, O(n).

## Heap & Graph (`HeapGraph.java`)
- **LC #215 Kth Largest Element** — Min-heap of size k, O(n log k).
- **LC #347 Top K Frequent** — Bucket sort by frequency, O(n).
- **LC #23 Merge k Sorted Lists** — Min-heap of list heads, O(N log k).
- **LC #200 Number of Islands** — Count connected land components via DFS, O(mn).
- **LC #207 Course Schedule** — Detect cycle via Kahn's topological sort, O(V+E).
- **LC #208 Implement Trie** — Prefix tree with insert/search/startsWith, O(L).
- **LC #323 Number of Connected Components** — Union-Find, O(V+E α).
- **LC #994 Rotting Oranges** — Multi-source BFS timer, O(mn).
- **LC #127 Word Ladder** — Shortest transform via BFS, O(n·m²).
- **LC #286 Walls and Gates** — BFS distance from gates, O(mn).
- **LC #332 Reconstruct Itinerary** — Hierholzer's Euler-path, O(n log n).
- **LC #743 Network Delay Time** — Dijkstra from source, O((V+E) log V).

## Linked List (`LinkedList.java`)
- **LC #206 Reverse Linked List** — Iterative pointer reversal, O(n).
- **LC #21 Merge Two Sorted Lists** — Two-pointer merge, O(n+m).
- **LC #141 / #142 Linked List Cycle / Cycle II** — Floyd's slow/fast pointers, O(n).
- **LC #19 Remove Nth Node From End** — Two-pointer gap of n, O(n).
- **LC #2 Add Two Numbers** — Digit-by-digit addition with carry, O(max(n,m)).
- **LC #234 Palindrome Linked List** — Reverse half and compare, O(n).
- **LC #138 Copy List with Random Pointer** — Interleave clones then split, O(n).
- **LC #25 Reverse Nodes in k-Group** — Reverse in chunks of k, O(n).
- **LC #160 Intersection of Two Lists** — Two-pointer length equalization, O(n).
- **LC #83 / #82 Remove Duplicates (I & II)** — Single-pass skip, O(n).
- **LC #143 Reorder List** — Mid + reverse + merge, O(n).
- **LC #61 Rotate List** — Connect tail to head, break at new tail, O(n).
- **LC #86 Partition List** — Two dummy lists around x, O(n).
- **LC #148 Sort List** — Merge sort on linked list, O(n log n).

## Math (`Math.java`)
- **LC #7 / #9 Reverse Integer / Palindrome Number** — Digit reversal with overflow guard, O(log n).
- **LC #13 / #12 Roman to Integer / Integer to Roman** — Map-based conversion, O(n)/O(1).
- **LC #69 / #50 Sqrt(x) / Pow(x,n)** — Binary search / fast exponentiation, O(log n).
- **LC #29 Divide Two Integers** — Bit-shift long division, O(log n).
- **LC #172 Factorial Trailing Zeroes** — Count factors of 5, O(log n).
- **LC #202 Happy Number** — Cycle detection on digit squares, O(log n).
- **LC #204 Count Primes** — Sieve of Eratosthenes, O(n log log n).
- **LC #149 Max Points on a Line** — Slope hashmap per point, O(n²).
- **LC #365 Water and Jug** — GCD check (Diophantine), O(1).
- **LC #67 / #43 / #415 / #66 Add/Multiply Binary & Decimal Strings, Plus One** — Schoolbook arithmetic, O(n)/O(mn).
- **LC #8 String to Integer (atoi)** — Stateful parsing, O(n).
- **LC #223 Rectangle Area** — Overlap subtraction, O(1).
- **LC #227 Basic Calculator II** — Two-stack expression eval, O(n).

## Recursion (`Recursion.java`)
- **LC #509 Fibonacci / #70 Climbing Stairs** — Memoized recursion, O(n).
- **LC #22 / #78 / #46 / #47 / #77 Generate Parentheses, Subsets, Permutations (I/II), Combinations** — Classic backtracking, exponential.
- **LC #39 / #40 / #216 Combination Sum (I/II/III)** — Recursive sum targeting, O(2ⁿ).
- **LC #17 / #79 Letter Combinations / Word Search** — DFS recursion.
- **LC #51 / #52 N-Queens (I/II)** — Backtracking with conflict checks, O(n!).
- **LC #37 Sudoku Solver** — Backtracking with validity checks, O(9ⁿ·ⁿ).
- **LC #131 / #93 / #241 / #491 / #306 Palindrome Partitioning, Restore IP, Add Parentheses, Increasing Subseq., Additive Number** — Recursive enumeration, exponential.

## Searching / Binary Search (`Searching.java`)
- **LC #33 / #81 Search in Rotated Sorted Array (I/II)** — Rotated binary search, O(log n).
- **LC #153 / #154 Find Minimum in Rotated Sorted Array (I/II)** — O(log n).
- **LC #35 / #34 Search Insert Position / First & Last Position** — Bound binary search, O(log n).
- **LC #69 / #367 Sqrt(x) / Valid Perfect Square** — O(log n).
- **LC #74 / #240 Search a 2D Matrix (I/II)** — Row/col sorted search, O(log(mn))/O(m+n).
- **LC #378 Kth Smallest in Sorted Matrix** — Binary search on value, O(n log range).
- **LC #4 Median of Two Sorted Arrays** — O(log(min(m,n))).
- **LC #875 / #1011 / #410 / #1283 / #1482 / #1552** — Binary search on answer family (Koko, ship packages, split array, smallest divisor, bouquets, magnetic balls), O(n log range).
- **LC #162 / #852 / #1095 Find Peak / Peak in Mountain / Find in Mountain Array** — O(log n).

## Sorting (`Sorting.java`)
- **LC #912 Sort an Array** — Merge sort, O(n log n).
- **LC #75 Sort Colors** — Dutch national flag three-pointer, O(n).
- **LC #169 Majority Element** — Boyer-Moore, O(n).
- **LC #215 Kth Largest** — Quickselect, O(n) avg.
- **LC #252 / #253 Meeting Rooms (I/II)** — Sort by start; min heap for rooms, O(n log n).
- **LC #179 Largest Number** — Custom comparator concatenation, O(n log n).
- **LC #164 Maximum Gap** — Radix/bucket, O(n).
- **LC #242 / #49 Valid Anagram / Group Anagrams** — Count/sort key, O(n log k).
- **LC #347 / #451 Top K Frequent / Sort by Frequency** — Bucket sort, O(n).
- **LC #973 K Closest Points** — Quickselect, O(n log n).
- **LC #324 / #280 Wiggle Sort (I/II)** — Sort + interleave, O(n log n)/O(n).
- **LC #321 / #767 / #1051 / #1122 / #1365 / #1331 / #1509 / #1637** — Various comparator/ranking problems, O(n log n).

## Stack & Queue (`StackQueue.java`)
- **LC #20 Valid Parentheses** — Stack matching, O(n).
- **LC #739 Daily Temperatures** — Monotonic stack of indices, O(n).
- **LC #84 / #85 Largest Rectangle / Maximal Rectangle** — Monotonic increasing stack, O(n)/O(mn).
- **LC #232 / #225 Implement Queue/Stack using the other** — Two-structure wrappers, amortized O(1)/O(n).
- **LC #155 Min Stack** — Track min alongside values, O(1).
- **LC #150 Evaluate RPN** — Operand stack, O(n).
- **LC #71 Simplify Path** — Stack of path segments, O(n).
- **LC #394 Decode String** — Stack for repeat counts, O(n).
- **LC #496 / #503 Next Greater Element (I/II)** — Monotonic stack, O(n).
- **LC #42 Trapping Rain Water** — Two-pointer/monotonic stack, O(n).
- **LC #239 Sliding Window Maximum** — Deque of indices, O(n).
- **LC #649 Dota2 Senate** — Queue simulation, O(n).
- **LC #346 / #622 / #641 Moving Average / Circular Queue / Circular Deque** — O(1) data-structure designs.

## Strings (`Strings.java`)
- **LC #3 Longest Substring Without Repeating** — Sliding window with last-seen map, O(n).
- **LC #5 Longest Palindromic Substring** — Center expansion, O(n²).
- **LC #20 Valid Parentheses** — Stack, O(n).
- **LC #49 Group Anagrams** — Sort-key grouping, O(n k log k).
- **LC #76 Minimum Window Substring** — Sliding window with frequency counts, O(|s|+|t|).
- **LC #14 Longest Common Prefix** — Vertical scan, O(n·m).
- **LC #125 Valid Palindrome** — Two pointers, O(n).
- **LC #387 First Unique Char** — Count then scan, O(n).
- **LC #344 / #242 / #43 / #151 / #165 / #443 / #557 / #686 Reverse String, Is Anagram, Multiply Strings, Reverse Words, Compare Versions, String Compression, Reverse Words III, Repeated String Match** — Standard string scans, O(n)–O(n·m).

## Trees (`Trees.java`)
- **LC #94 Inorder (iterative) / #104 Max Depth / #100 Same Tree / #101 Symmetric** — Traversals & properties, O(n).
- **LC #102 Level Order (BFS) / #103 Zigzag / #199 Right Side View** — Queue-based level traversals, O(n).
- **LC #98 Validate BST** — In-order bound check, O(n).
- **LC #108 Sorted Array → BST** — Pick mid as root, O(n).
- **LC #236 Lowest Common Ancestor** — Recursive ancestor search, O(n).
- **LC #105 Build Tree from Preorder+Inorder** — Recurse on index ranges, O(n).
- **LC #124 Max Path Sum** — Post-order max gain, O(n).
- **LC #230 Kth Smallest in BST** — In-order counter, O(n).
- **LC #297 Serialize/Deserialize** — Pre-order with null markers, O(n).
- **LC #337 House Robber III** — DP on subtrees (rob/skip), O(n).
- **LC #114 Flatten to Linked List** — Morris-style rewire, O(n).
- **LC #987 Vertical Order** — BFS with column index, O(n log n).
- **LC #116/117 Populate Next Right Pointers** — Level-link or BFS, O(n).

## Two Pointers (`TwoPointers.java`)
- **LC #125 / #167 / #15 / #16 / #18 Valid Palindrome, Two Sum II, 3Sum, 3Sum Closest, 4Sum** — Sorted-array pointer techniques, O(n)–O(n³).
- **LC #11 Container With Most Water / #42 Trapping Rain Water** — Two-pointer area/water, O(n).
- **LC #75 Sort Colors / #283 Move Zeroes / #26 / #80 Remove Duplicates (I/II)** — In-place pointer compaction, O(n).
- **LC #88 Merge Sorted Array** — Back-to-front merge, O(m+n).
- **LC #209 Minimum Size Subarray Sum** — Sliding window, O(n).
- **LC #3 / #76 Longest Substring / Minimum Window Substring** — Sliding window, O(n).
- **LC #392 Is Subsequence / #633 Sum of Square Numbers / #680 Valid Palindrome II / #524 Longest Word in Dictionary** — Pointer scans, O(n)–O(n·m).