# DSA Problems List

A curated set of LeetCode problems implemented in this `dsa/` directory, grouped by topic. Each entry links to the problem and lists the companies that commonly ask it, plus a short explanation of the approach and time complexity.

## Arrays (`Arrays.java`)
- **[LC #1 Two Sum](https://leetcode.com/problems/two-sum/)** — Find two indices whose values sum to a target. Hash map `value → index`, check `target - num` in O(n). · _Companies: Amazon, Google, Apple, Bloomberg_
- **[LC #121 Best Time to Buy and Sell Stock](https://leetcode.com/problems/best-time-to-buy-and-sell-stock/)** — Max profit from one buy/sell. Track running min price and max difference, O(n). · _Companies: Amazon, Microsoft, Bloomberg_
- **[LC #53 Maximum Subarray (Kadane)](https://leetcode.com/problems/maximum-subarray/)** — Largest sum of a contiguous subarray. Running `current` sum reset to element when larger, O(n). · _Companies: Amazon, LinkedIn, Bloomberg_
- **[LC #238 Product of Array Except Self](https://leetcode.com/problems/product-of-array-except-self/)** — Product of all elements except self, no division. Prefix + suffix products, O(n). · _Companies: Amazon, Apple, Microsoft_
- **[LC #56 Merge Intervals](https://leetcode.com/problems/merge-intervals/)** — Merge overlapping intervals. Sort by start, merge adjacent overlaps, O(n log n). · _Companies: Amazon, Google, Facebook_
- **[LC #15 3Sum](https://leetcode.com/problems/3sum/)** — All unique triplets summing to 0. Sort, fix one, two-pointer rest, skip dupes, O(n²). · _Companies: Amazon, Facebook, Bloomberg_
- **[LC #189 Rotate Array](https://leetcode.com/problems/rotate-array/)** — Rotate right by k. Reverse whole, then two parts, O(n). · _Companies: Amazon, Microsoft_
- **[LC #169 Majority Element](https://leetcode.com/problems/majority-element/)** — Element appearing > n/2 times. Boyer-Moore voting, O(n). · _Companies: Adobe, Google, Amazon_
- **[LC #48 Rotate Image](https://leetcode.com/problems/rotate-image/)** — Rotate N×N matrix 90°. Transpose then reverse rows, O(n²). · _Companies: Amazon, Microsoft, Apple_

## Algorithms (`Algorithms.java`)
- **[LC #33 Search in Rotated Sorted Array](https://leetcode.com/problems/search-in-rotated-sorted-array/)** — Binary search on rotated array by comparing mid to sorted half, O(log n). · _Companies: Amazon, Google, Microsoft, Facebook_
- **[LC #875 Koko Eating Bananas](https://leetcode.com/problems/koko-eating-bananas/)** — Min eating speed for h hours. Binary search on answer, O(n log max). · _Companies: Google, LinkedIn, Facebook_
- **[LC #11 Container With Most Water](https://leetcode.com/problems/container-with-most-water/)** — Max area between two lines. Two pointers, move shorter, O(n). · _Companies: Amazon, Google, Bloomberg_
- **[LC #560 Subarray Sum Equals K](https://leetcode.com/problems/subarray-sum-equals-k/)** — Count subarrays summing to k. Prefix-sum + hash map, O(n). · _Companies: Google, Facebook, Amazon_
- **[LC #70 Climbing Stairs](https://leetcode.com/problems/climbing-stairs/)** — Ways to climb n steps (1 or 2). Fibonacci DP, O(n). · _Companies: Amazon, Adobe, Google_
- **[LC #300 Longest Increasing Subsequence](https://leetcode.com/problems/longest-increasing-subsequence/)** — Length of LIS. Patience sorting + binary search, O(n log n). · _Companies: Google, Amazon_
- **[LC #322 Coin Change](https://leetcode.com/problems/coin-change/)** — Fewest coins for an amount. Unbounded knapsack DP, O(amount × coins). · _Companies: Amazon, Microsoft, Bloomberg_
- **[LC #55 Jump Game](https://leetcode.com/problems/jump-game/)** — Reach last index? Greedy furthest-reach, O(n). · _Companies: Amazon, Google_
- **[LC #62 Unique Paths](https://leetcode.com/problems/unique-paths/)** — Paths in m×n grid. DP cell = top + left, O(mn). · _Companies: Amazon, Google_
- **[LC #5 Longest Palindromic Substring](https://leetcode.com/problems/longest-palindromic-substring/)** — Expand around every center, O(n²). · _Companies: Amazon, Microsoft, Bloomberg_

## Backtracking (`Backtracking.java`)
- **[LC #46 Permutations](https://leetcode.com/problems/permutations/)** — All orderings. Swap-based backtracking, O(n·n!). · _Companies: Amazon, Microsoft, Google_
- **[LC #78 Subsets](https://leetcode.com/problems/subsets/)** — All subsets. Include/exclude each element, O(2ⁿ). · _Companies: Amazon, Facebook, Google_
- **[LC #39 Combination Sum](https://leetcode.com/problems/combination-sum/)** — Combinations summing to target (reuse allowed). Backtrack with start index, O(2ⁿ). · _Companies: Amazon, Google_
- **[LC #17 Letter Combinations of a Phone Number](https://leetcode.com/problems/letter-combinations-of-a-phone-number/)** — All digit→letter mappings. DFS over digits, O(4ⁿ). · _Companies: Amazon, Google, Uber_
- **[LC #79 Word Search](https://leetcode.com/problems/word-search/)** — Find a word in a grid. DFS with visited marking, O(m·n·4ᴸ). · _Companies: Amazon, Microsoft_
- **[LC #22 Generate Parentheses](https://leetcode.com/problems/generate-parentheses/)** — All valid n-pair parentheses. Track open/close counts, O(2ⁿ). · _Companies: Amazon, Google, Facebook_
- **[LC #130 Surround Regions](https://leetcode.com/problems/surrounded-regions/)** — Flip 'O' regions not connected to border. DFS from border, O(mn). · _Companies: Amazon, Google_

## Bit Manipulation (`BitManipulation.java`)
- **[LC #136 Single Number](https://leetcode.com/problems/single-number/)** — One appears once, rest twice. XOR all, pairs cancel, O(n). · _Companies: Amazon, Google, Apple_
- **[LC #137 Single Number II](https://leetcode.com/problems/single-number-ii/)** — One appears once, rest thrice. Two bitmasks mod 3, O(n). · _Companies: Amazon_
- **[LC #260 Single Number III](https://leetcode.com/problems/single-number-iii/)** — Two singles, rest twice. XOR then partition by rightmost set bit, O(n). · _Companies: Google, Amazon_
- **[LC #191 Number of 1 Bits](https://leetcode.com/problems/number-of-1-bits/)** — Count set bits. Brian Kernighan `n &= n-1`, O(1). · _Companies: Apple, Amazon_
- **[LC #338 Counting Bits](https://leetcode.com/problems/counting-bits/)** — Set-bit count for 0..n. DP `dp[i]=dp[i>>1]+(i&1)`, O(n). · _Companies: Amazon, Google_
- **[LC #461 Hamming Distance](https://leetcode.com/problems/hamming-distance/)** — Differing bits of two ints. XOR then count, O(1). · _Companies: Facebook, Amazon_
- **[LC #476 Number Complement](https://leetcode.com/problems/number-complement/)** — Flip bits up to highest set bit. Mask XOR, O(1). · _Companies: Amazon_
- **[LC #231 Power of Two](https://leetcode.com/problems/power-of-two/)** — Check `n>0 && (n&(n-1))==0`, O(1). · _Companies: Google, Amazon_

## Dynamic Programming (`DynamicProgramming.java`)
- **[LC #70 Climbing Stairs](https://leetcode.com/problems/climbing-stairs/)** — Iterative O(n)/O(1) DP. · _Companies: Amazon, Adobe_
- **[LC #322 Coin Change](https://leetcode.com/problems/coin-change/)** — Min coins DP table version. · _Companies: Amazon, Microsoft_
- **[LC #300 Longest Increasing Subsequence](https://leetcode.com/problems/longest-increasing-subsequence/)** — Patience sorting version. · _Companies: Google, Amazon_
- **[LC #1143 Longest Common Subsequence](https://leetcode.com/problems/longest-common-subsequence/)** — `dp[i][j]` over two strings, O(mn). · _Companies: Amazon, Google, Microsoft_

## Greedy (`Greedy.java`)
- **[LC #55 / #45 Jump Game / Jump Game II](https://leetcode.com/problems/jump-game/)** — Reachability and min jumps via furthest reach, O(n). · _Companies: Amazon, Google_
- **[LC #121 / #122 Best Time to Buy and Sell Stock (I & II)](https://leetcode.com/problems/best-time-to-buy-and-sell-stock/)** — One vs. multiple transactions, O(n). · _Companies: Amazon, Microsoft_
- **[LC #134 Gas Station](https://leetcode.com/problems/gas-station/)** — Find valid starting station. Track net fuel, O(n). · _Companies: Amazon, Google_
- **[LC #135 Candy](https://leetcode.com/problems/candy/)** — Min candies with rating constraints. Left/right passes, O(n). · _Companies: Google, Amazon_
- **[LC #406 Queue Reconstruction by Height](https://leetcode.com/problems/queue-reconstruction-by-height/)** — Sort by height desc, insert by k, O(n²). · _Companies: Google, Amazon_
- **[LC #621 Task Scheduler](https://leetcode.com/problems/task-scheduler/)** — Min intervals with cooldown. Schedule most frequent first, O(n). · _Companies: Amazon, Google, Facebook_
- **[LC #763 Partition Labels](https://leetcode.com/problems/partition-labels/)** — Greedy by last-seen index, O(n). · _Companies: Amazon, Google_
- **[LC #56 / #435 / #452 Merge / Non-overlapping / Min Arrows](https://leetcode.com/problems/merge-intervals/)** — Interval greedy by end time, O(n log n). · _Companies: Amazon, Google, Facebook_
- **[LC #1029 Two City Scheduling](https://leetcode.com/problems/two-city-scheduling/)** — Min cost via refund sorting, O(n log n). · _Companies: Amazon, Google_
- **[LC #870 Advantage Shuffle](https://leetcode.com/problems/advantage-shuffle/)** — Maximize wins (Highest-Card-Wins), O(n log n). · _Companies: Google_
- **[LC #334 / #605 / #392 / #846 / #659](https://leetcode.com/problems/increasing-triplet-subsequence/)** — Increasing triplet, place flowers, subsequence, hand of straights, consecutive splits — various greedy scans, O(n). · _Companies: Amazon, Google_

## Heap & Graph (`HeapGraph.java`)
- **[LC #215 Kth Largest Element](https://leetcode.com/problems/kth-largest-element-in-an-array/)** — Min-heap of size k, O(n log k). · _Companies: Amazon, Facebook, Google_
- **[LC #347 Top K Frequent](https://leetcode.com/problems/top-k-frequent-elements/)** — Bucket sort by frequency, O(n). · _Companies: Amazon, Google, Facebook_
- **[LC #23 Merge k Sorted Lists](https://leetcode.com/problems/merge-k-sorted-lists/)** — Min-heap of list heads, O(N log k). · _Companies: Amazon, Google, Microsoft, Facebook_
- **[LC #200 Number of Islands](https://leetcode.com/problems/number-of-islands/)** — Count connected land via DFS, O(mn). · _Companies: Amazon, Google, Microsoft, Facebook_
- **[LC #207 Course Schedule](https://leetcode.com/problems/course-schedule/)** — Detect cycle via Kahn's topo sort, O(V+E). · _Companies: Amazon, Google, Facebook_
- **[LC #208 Implement Trie](https://leetcode.com/problems/implement-trie-prefix-tree/)** — Prefix tree with insert/search/startsWith, O(L). · _Companies: Amazon, Google, Microsoft_
- **[LC #323 Number of Connected Components](https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/)** — Union-Find, O(V+E α). · _Companies: Amazon_
- **[LC #994 Rotting Oranges](https://leetcode.com/problems/rotting-oranges/)** — Multi-source BFS timer, O(mn). · _Companies: Amazon, Google_
- **[LC #127 Word Ladder](https://leetcode.com/problems/word-ladder/)** — Shortest transform via BFS, O(n·m²). · _Companies: Amazon, Google, Facebook_
- **[LC #286 Walls and Gates](https://leetcode.com/problems/walls-and-gates/)** — BFS distance from gates, O(mn). · _Companies: Google, Amazon_
- **[LC #332 Reconstruct Itinerary](https://leetcode.com/problems/reconstruct-itinerary/)** — Hierholzer's Euler-path, O(n log n). · _Companies: Google, Amazon_
- **[LC #743 Network Delay Time](https://leetcode.com/problems/network-delay-time/)** — Dijkstra from source, O((V+E) log V). · _Companies: Amazon, Google, Facebook_

## Linked List (`LinkedList.java`)
- **[LC #206 Reverse Linked List](https://leetcode.com/problems/reverse-linked-list/)** — Iterative pointer reversal, O(n). · _Companies: Amazon, Microsoft, Apple, Google_
- **[LC #21 Merge Two Sorted Lists](https://leetcode.com/problems/merge-two-sorted-lists/)** — Two-pointer merge, O(n+m). · _Companies: Amazon, Microsoft_
- **[LC #141 / #142 Linked List Cycle / Cycle II](https://leetcode.com/problems/linked-list-cycle/)** — Floyd's slow/fast pointers, O(n). · _Companies: Amazon, Microsoft, Bloomberg_
- **[LC #19 Remove Nth Node From End](https://leetcode.com/problems/remove-nth-node-from-end-of-list/)** — Two-pointer gap of n, O(n). · _Companies: Amazon, Microsoft, Bloomberg_
- **[LC #2 Add Two Numbers](https://leetcode.com/problems/add-two-numbers/)** — Digit addition with carry, O(max(n,m)). · _Companies: Amazon, Microsoft, Google, Bloomberg_
- **[LC #234 Palindrome Linked List](https://leetcode.com/problems/palindrome-linked-list/)** — Reverse half and compare, O(n). · _Companies: Amazon, Facebook, Microsoft_
- **[LC #138 Copy List with Random Pointer](https://leetcode.com/problems/copy-list-with-random-pointer/)** — Interleave clones then split, O(n). · _Companies: Amazon, Microsoft, Google_
- **[LC #25 Reverse Nodes in k-Group](https://leetcode.com/problems/reverse-nodes-in-k-group/)** — Reverse in chunks of k, O(n). · _Companies: Google, Amazon_
- **[LC #160 Intersection of Two Lists](https://leetcode.com/problems/intersection-of-two-linked-lists/)** — Two-pointer length equalization, O(n). · _Companies: Amazon, Microsoft_
- **[LC #83 / #82 Remove Duplicates (I & II)](https://leetcode.com/problems/remove-duplicates-from-sorted-list/)** — Single-pass skip, O(n). · _Companies: Amazon_
- **[LC #143 Reorder List](https://leetcode.com/problems/reorder-list/)** — Mid + reverse + merge, O(n). · _Companies: Amazon, Microsoft, Google_
- **[LC #61 Rotate List](https://leetcode.com/problems/rotate-list/)** — Connect tail to head, break at new tail, O(n). · _Companies: Amazon, Microsoft_
- **[LC #86 Partition List](https://leetcode.com/problems/partition-list/)** — Two dummy lists around x, O(n). · _Companies: Amazon, Microsoft_
- **[LC #148 Sort List](https://leetcode.com/problems/sort-list/)** — Merge sort on linked list, O(n log n). · _Companies: Amazon, Microsoft, Google_

## Math (`Math.java`)
- **[LC #7 / #9 Reverse Integer / Palindrome Number](https://leetcode.com/problems/reverse-integer/)** — Digit reversal with overflow guard, O(log n). · _Companies: Amazon, Microsoft, Apple_
- **[LC #13 / #12 Roman to Integer / Integer to Roman](https://leetcode.com/problems/roman-to-integer/)** — Map-based conversion, O(n)/O(1). · _Companies: Amazon, Microsoft, Bloomberg_
- **[LC #69 / #50 Sqrt(x) / Pow(x,n)](https://leetcode.com/problems/sqrtx/)** — Binary search / fast exponentiation, O(log n). · _Companies: Amazon, Google, Microsoft_
- **[LC #29 Divide Two Integers](https://leetcode.com/problems/divide-two-integers/)** — Bit-shift long division, O(log n). · _Companies: Amazon, Google_
- **[LC #172 Factorial Trailing Zeroes](https://leetcode.com/problems/factorial-trailing-zeroes/)** — Count factors of 5, O(log n). · _Companies: Amazon, Microsoft_
- **[LC #202 Happy Number](https://leetcode.com/problems/happy-number/)** — Cycle detection on digit squares, O(log n). · _Companies: Amazon, Google_
- **[LC #204 Count Primes](https://leetcode.com/problems/count-primes/)** — Sieve of Eratosthenes, O(n log log n). · _Companies: Amazon, Microsoft_
- **[LC #149 Max Points on a Line](https://leetcode.com/problems/max-points-on-a-line/)** — Slope hashmap per point, O(n²). · _Companies: Amazon, Google_
- **[LC #365 Water and Jug](https://leetcode.com/problems/water-and-jug-problem/)** — GCD check (Diophantine), O(1). · _Companies: Google, Amazon_
- **[LC #67 / #43 / #415 / #66 Add/Multiply Binary & Decimal Strings, Plus One](https://leetcode.com/problems/add-binary/)** — Schoolbook arithmetic, O(n)/O(mn). · _Companies: Amazon, Microsoft_
- **[LC #8 String to Integer (atoi)](https://leetcode.com/problems/string-to-integer-atoi/)** — Stateful parsing, O(n). · _Companies: Amazon, Microsoft, Bloomberg_
- **[LC #223 Rectangle Area](https://leetcode.com/problems/rectangle-area/)** — Overlap subtraction, O(1). · _Companies: Amazon_
- **[LC #227 Basic Calculator II](https://leetcode.com/problems/basic-calculator-ii/)** — Two-stack expression eval, O(n). · _Companies: Amazon, Google, Microsoft_

## Recursion (`Recursion.java`)
- **[LC #509 Fibonacci / #70 Climbing Stairs](https://leetcode.com/problems/fibonacci-number/)** — Memoized recursion, O(n). · _Companies: Amazon_
- **[LC #22 / #78 / #46 / #47 / #77 Generate Parentheses, Subsets, Permutations (I/II), Combinations](https://leetcode.com/problems/generate-parentheses/)** — Classic backtracking, exponential. · _Companies: Amazon, Google, Facebook_
- **[LC #39 / #40 / #216 Combination Sum (I/II/III)](https://leetcode.com/problems/combination-sum/)** — Recursive sum targeting, O(2ⁿ). · _Companies: Amazon, Google_
- **[LC #17 / #79 Letter Combinations / Word Search](https://leetcode.com/problems/letter-combinations-of-a-phone-number/)** — DFS recursion. · _Companies: Amazon_
- **[LC #51 / #52 N-Queens (I/II)](https://leetcode.com/problems/n-queens/)** — Backtracking with conflict checks, O(n!). · _Companies: Amazon, Google, Facebook_
- **[LC #37 Sudoku Solver](https://leetcode.com/problems/sudoku-solver/)** — Backtracking with validity checks, O(9ⁿ·ⁿ). · _Companies: Amazon, Google_
- **[LC #131 / #93 / #241 / #491 / #306 Palindrome Partitioning, Restore IP, Add Parentheses, Increasing Subseq., Additive Number](https://leetcode.com/problems/palindrome-partitioning/)** — Recursive enumeration, exponential. · _Companies: Amazon, Google_

## Searching / Binary Search (`Searching.java`)
- **[LC #33 / #81 Search in Rotated Sorted Array (I/II)](https://leetcode.com/problems/search-in-rotated-sorted-array/)** — Rotated binary search, O(log n). · _Companies: Amazon, Google_
- **[LC #153 / #154 Find Minimum in Rotated Sorted Array (I/II)](https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/)** — O(log n). · _Companies: Amazon, Microsoft_
- **[LC #35 / #34 Search Insert Position / First & Last Position](https://leetcode.com/problems/search-insert-position/)** — Bound binary search, O(log n). · _Companies: Amazon, Google, Microsoft_
- **[LC #69 / #367 Sqrt(x) / Valid Perfect Square](https://leetcode.com/problems/sqrtx/)** — O(log n). · _Companies: Amazon_
- **[LC #74 / #240 Search a 2D Matrix (I/II)](https://leetcode.com/problems/search-a-2d-matrix/)** — Row/col sorted search, O(log(mn))/O(m+n). · _Companies: Amazon, Google, Microsoft_
- **[LC #378 Kth Smallest in Sorted Matrix](https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/)** — Binary search on value, O(n log range). · _Companies: Amazon, Google_
- **[LC #4 Median of Two Sorted Arrays](https://leetcode.com/problems/median-of-two-sorted-arrays/)** — O(log(min(m,n))). · _Companies: Amazon, Google, Microsoft, Apple_
- **[LC #875 / #1011 / #410 / #1283 / #1482 / #1552](https://leetcode.com/problems/koko-eating-bananas/)** — Binary search on answer family (Koko, ship packages, split array, smallest divisor, bouquets, magnetic balls), O(n log range). · _Companies: Amazon, Google, Facebook_
- **[LC #162 / #852 / #1095 Find Peak / Peak in Mountain / Find in Mountain Array](https://leetcode.com/problems/find-peak-element/)** — O(log n). · _Companies: Amazon, Google_

## Sorting (`Sorting.java`)
- **[LC #912 Sort an Array](https://leetcode.com/problems/sort-an-array/)** — Merge sort, O(n log n). · _Companies: Amazon_
- **[LC #75 Sort Colors](https://leetcode.com/problems/sort-colors/)** — Dutch national flag three-pointer, O(n). · _Companies: Amazon, Microsoft, Facebook_
- **[LC #169 Majority Element](https://leetcode.com/problems/majority-element/)** — Boyer-Moore, O(n). · _Companies: Adobe, Google_
- **[LC #215 Kth Largest](https://leetcode.com/problems/kth-largest-element-in-an-array/)** — Quickselect, O(n) avg. · _Companies: Amazon_
- **[LC #252 / #253 Meeting Rooms (I/II)](https://leetcode.com/problems/meeting-rooms/)** — Sort by start; min heap for rooms, O(n log n). · _Companies: Amazon, Google, Facebook_
- **[LC #179 Largest Number](https://leetcode.com/problems/largest-number/)** — Custom comparator concatenation, O(n log n). · _Companies: Amazon, Microsoft_
- **[LC #164 Maximum Gap](https://leetcode.com/problems/maximum-gap/)** — Radix/bucket, O(n). · _Companies: Amazon, Google_
- **[LC #242 / #49 Valid Anagram / Group Anagrams](https://leetcode.com/problems/valid-anagram/)** — Count/sort key, O(n log k). · _Companies: Amazon, Google_
- **[LC #347 / #451 Top K Frequent / Sort by Frequency](https://leetcode.com/problems/top-k-frequent-elements/)** — Bucket sort, O(n). · _Companies: Amazon_
- **[LC #973 K Closest Points](https://leetcode.com/problems/k-closest-points-to-origin/)** — Quickselect, O(n log n). · _Companies: Amazon, Google, Facebook_
- **[LC #324 / #280 Wiggle Sort (I/II)](https://leetcode.com/problems/wiggle-sort-ii/)** — Sort + interleave, O(n log n)/O(n). · _Companies: Google, Amazon_
- **[LC #321 / #767 / #1051 / #1122 / #1365 / #1331 / #1509 / #1637](https://leetcode.com/problems/create-maximum-number/)** — Various comparator/ranking problems, O(n log n). · _Companies: Amazon, Google_

## Stack & Queue (`StackQueue.java`)
- **[LC #20 Valid Parentheses](https://leetcode.com/problems/valid-parentheses/)** — Stack matching, O(n). · _Companies: Amazon, Microsoft, Google, Bloomberg_
- **[LC #739 Daily Temperatures](https://leetcode.com/problems/daily-temperatures/)** — Monotonic stack of indices, O(n). · _Companies: Amazon, Google_
- **[LC #84 / #85 Largest Rectangle / Maximal Rectangle](https://leetcode.com/problems/largest-rectangle-in-histogram/)** — Monotonic increasing stack, O(n)/O(mn). · _Companies: Amazon, Google_
- **[LC #232 / #225 Implement Queue/Stack using the other](https://leetcode.com/problems/implement-queue-using-stacks/)** — Two-structure wrappers, amortized O(1)/O(n). · _Companies: Amazon, Microsoft_
- **[LC #155 Min Stack](https://leetcode.com/problems/min-stack/)** — Track min alongside values, O(1). · _Companies: Amazon, Google, Microsoft, Bloomberg_
- **[LC #150 Evaluate RPN](https://leetcode.com/problems/evaluate-reverse-polish-notation/)** — Operand stack, O(n). · _Companies: Amazon, Microsoft_
- **[LC #71 Simplify Path](https://leetcode.com/problems/simplify-path/)** — Stack of path segments, O(n). · _Companies: Amazon, Google_
- **[LC #394 Decode String](https://leetcode.com/problems/decode-string/)** — Stack for repeat counts, O(n). · _Companies: Amazon, Google, Microsoft_
- **[LC #496 / #503 Next Greater Element (I/II)](https://leetcode.com/problems/next-greater-element-i/)** — Monotonic stack, O(n). · _Companies: Amazon, Google_
- **[LC #42 Trapping Rain Water](https://leetcode.com/problems/trapping-rain-water/)** — Two-pointer/monotonic stack, O(n). · _Companies: Amazon, Google, Microsoft, Bloomberg_
- **[LC #239 Sliding Window Maximum](https://leetcode.com/problems/sliding-window-maximum/)** — Deque of indices, O(n). · _Companies: Amazon, Google, Facebook_
- **[LC #649 Dota2 Senate](https://leetcode.com/problems/dota2-senate/)** — Queue simulation, O(n). · _Companies: Amazon_
- **[LC #346 / #622 / #641 Moving Average / Circular Queue / Circular Deque](https://leetcode.com/problems/design-circular-queue/)** — O(1) data-structure designs. · _Companies: Amazon, Google_

## Strings (`Strings.java`)
- **[LC #3 Longest Substring Without Repeating](https://leetcode.com/problems/longest-substring-without-repeating-characters/)** — Sliding window with last-seen map, O(n). · _Companies: Amazon, Microsoft, Bloomberg, Adobe_
- **[LC #5 Longest Palindromic Substring](https://leetcode.com/problems/longest-palindromic-substring/)** — Center expansion, O(n²). · _Companies: Amazon_
- **[LC #20 Valid Parentheses](https://leetcode.com/problems/valid-parentheses/)** — Stack, O(n). · _Companies: Amazon_
- **[LC #49 Group Anagrams](https://leetcode.com/problems/group-anagrams/)** — Sort-key grouping, O(n k log k). · _Companies: Amazon, Google, Facebook_
- **[LC #76 Minimum Window Substring](https://leetcode.com/problems/minimum-window-substring/)** — Sliding window with frequency counts, O(|s|+|t|). · _Companies: Amazon, Google, Facebook, Microsoft_
- **[LC #14 Longest Common Prefix](https://leetcode.com/problems/longest-common-prefix/)** — Vertical scan, O(n·m). · _Companies: Amazon, Google_
- **[LC #125 Valid Palindrome](https://leetcode.com/problems/valid-palindrome/)** — Two pointers, O(n). · _Companies: Amazon, Microsoft_
- **[LC #387 First Unique Char](https://leetcode.com/problems/first-unique-character-in-a-string/)** — Count then scan, O(n). · _Companies: Amazon, Microsoft, Bloomberg_
- **[LC #344 / #242 / #43 / #151 / #165 / #443 / #557 / #686 Reverse String, Is Anagram, Multiply Strings, Reverse Words, Compare Versions, String Compression, Reverse Words III, Repeated String Match](https://leetcode.com/problems/reverse-string/)** — Standard string scans, O(n)–O(n·m). · _Companies: Amazon, Microsoft, Google_

## Trees (`Trees.java`)
- **[LC #94 Inorder (iterative) / #104 Max Depth / #100 Same Tree / #101 Symmetric](https://leetcode.com/problems/binary-tree-inorder-traversal/)** — Traversals & properties, O(n). · _Companies: Amazon, Microsoft, Google_
- **[LC #102 Level Order (BFS) / #103 Zigzag / #199 Right Side View](https://leetcode.com/problems/binary-tree-level-order-traversal/)** — Queue-based level traversals, O(n). · _Companies: Amazon, Microsoft, Google, Facebook_
- **[LC #98 Validate BST](https://leetcode.com/problems/validate-binary-search-tree/)** — In-order bound check, O(n). · _Companies: Amazon, Microsoft, Google, Bloomberg_
- **[LC #108 Sorted Array → BST](https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/)** — Pick mid as root, O(n). · _Companies: Amazon, Google_
- **[LC #236 Lowest Common Ancestor](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/)** — Recursive ancestor search, O(n). · _Companies: Amazon, Microsoft, Google, Facebook, Bloomberg_
- **[LC #105 Build Tree from Preorder+Inorder](https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/)** — Recurse on index ranges, O(n). · _Companies: Amazon, Google, Microsoft_
- **[LC #124 Max Path Sum](https://leetcode.com/problems/binary-tree-maximum-path-sum/)** — Post-order max gain, O(n). · _Companies: Amazon, Google, Microsoft_
- **[LC #230 Kth Smallest in BST](https://leetcode.com/problems/kth-smallest-element-in-a-bst/)** — In-order counter, O(n). · _Companies: Amazon, Google_
- **[LC #297 Serialize/Deserialize](https://leetcode.com/problems/serialize-and-deserialize-binary-tree/)** — Pre-order with null markers, O(n). · _Companies: Amazon, Google, Microsoft, Facebook_
- **[LC #337 House Robber III](https://leetcode.com/problems/house-robber-iii/)** — DP on subtrees (rob/skip), O(n). · _Companies: Amazon, Google_
- **[LC #114 Flatten to Linked List](https://leetcode.com/problems/flatten-binary-tree-to-linked-list/)** — Morris-style rewire, O(n). · _Companies: Amazon, Microsoft_
- **[LC #987 Vertical Order](https://leetcode.com/problems/vertical-order-traversal-of-a-binary-tree/)** — BFS with column index, O(n log n). · _Companies: Amazon, Google_
- **[LC #116/117 Populate Next Right Pointers](https://leetcode.com/problems/populating-next-right-pointers-in-each-node/)** — Level-link or BFS, O(n). · _Companies: Amazon, Microsoft, Bloomberg_

## Two Pointers (`TwoPointers.java`)
- **[LC #125 / #167 / #15 / #16 / #18 Valid Palindrome, Two Sum II, 3Sum, 3Sum Closest, 4Sum](https://leetcode.com/problems/valid-palindrome/)** — Sorted-array pointer techniques, O(n)–O(n³). · _Companies: Amazon, Google, Microsoft, Bloomberg_
- **[LC #11 Container With Most Water / #42 Trapping Rain Water](https://leetcode.com/problems/container-with-most-water/)** — Two-pointer area/water, O(n). · _Companies: Amazon, Google_
- **[LC #75 Sort Colors / #283 Move Zeroes / #26 / #80 Remove Duplicates (I/II)](https://leetcode.com/problems/sort-colors/)** — In-place pointer compaction, O(n). · _Companies: Amazon, Microsoft_
- **[LC #88 Merge Sorted Array](https://leetcode.com/problems/merge-sorted-array/)** — Back-to-front merge, O(m+n). · _Companies: Amazon, Microsoft_
- **[LC #209 Minimum Size Subarray Sum](https://leetcode.com/problems/minimum-size-subarray-sum/)** — Sliding window, O(n). · _Companies: Amazon, Google_
- **[LC #3 / #76 Longest Substring / Minimum Window Substring](https://leetcode.com/problems/longest-substring-without-repeating-characters/)** — Sliding window, O(n). · _Companies: Amazon, Microsoft_
- **[LC #392 Is Subsequence / #633 Sum of Square Numbers / #680 Valid Palindrome II / #524 Longest Word in Dictionary](https://leetcode.com/problems/is-subsequence/)** — Pointer scans, O(n)–O(n·m). · _Companies: Amazon, Google_