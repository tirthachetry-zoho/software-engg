# Data Structures — Interview Q&A (with LeetCode problems)

## 1. Array — Two Sum (LeetCode #1, Easy)
**Q:** Given an array of integers and a target, return indices of the two numbers that sum to target.
**A:** Use a `HashMap<Integer, Integer>` (value → index). For each element `x`, check if `target - x` exists in the map; if yes, return the pair. O(n) time, O(n) space. Brute force is O(n²). This is the canonical hashmap-on-array problem.

## 2. Array — Best Time to Buy and Sell Stock (LeetCode #121, Easy)
**Q:** Max profit from one buy and one sell.
**A:** Track `minPrice` so far and `maxProfit`. Iterate once: `minPrice = min(minPrice, price)`, `maxProfit = max(maxProfit, price - minPrice)`. O(n) time, O(1) space. Teaches single-pass state tracking.

## 3. Linked List — Reverse Linked List (LeetCode #206, Easy)
**Q:** Reverse a singly linked list iteratively.
**A:** Three pointers `prev=null, curr=head, next`. In a loop: `next = curr.next; curr.next = prev; prev = curr; curr = next`. Return `prev`. O(n) time, O(1) space. Core linked-list pointer manipulation.

## 4. Linked List — Merge Two Sorted Lists (LeetCode #21, Easy)
**Q:** Merge two sorted linked lists into one sorted list.
**A:** Use a dummy node and a `tail` pointer; repeatedly attach the smaller of the two heads, advance that list. O(n+m) time. Teaches dummy-node + two-pointer merging.

## 5. Stack — Valid Parentheses (LeetCode #20, Easy)
**Q:** Determine if a string of `()[]{}` is valid.
**A:** Use a stack; push opening brackets, on closing pop and match. If mismatch or stack empty at close → invalid. At end stack must be empty. O(n) time/space. Classic stack use case.

## 6. Stack/Monotonic — Daily Temperatures (LeetCode #739, Medium)
**Q:** For each day, find days until a warmer temperature.
**A:** Monotonic decreasing stack of indices. When a warmer temp is seen, pop indices and set their answer = `i - idx`. O(n) time (each index pushed/popped once). Teaches monotonic stack.

## 7. Queue/Deque — Sliding Window Maximum (LeetCode #239, Hard)
**Q:** Max of each sliding window of size k.
**A:** Use a **deque** storing indices in decreasing value order. Remove out-of-window from front, smaller-than-current from back, then add current. O(n) amortized. Teaches deque as a monotonic structure.

## 8. Hash / Heap — Top K Frequent Elements (LeetCode #347, Medium)
**Q:** Return the k most frequent elements.
**A:** Count frequencies (HashMap), then use a **min-heap** of size k (evict smallest freq) → O(n log k), or bucket sort by frequency → O(n). Teaches heap vs counting tradeoffs.

## 9. Tree — Binary Tree Inorder Traversal (LeetCode #94, Easy)
**Q:** Return inorder (left, root, right) traversal.
**A:** Recursive is trivial; iterative uses a stack, pushing left children then popping/processing then going right. O(n). Foundation for all tree traversals.

## 10. Tree — Maximum Depth of Binary Tree (LeetCode #104, Easy)
**Q:** Max depth (longest root-to-leaf path).
**A:** Recursively `1 + max(depth(left), depth(right))`, base case null → 0. O(n). Teaches recursion on trees / DFS.

## 11. Graph — Number of Islands (LeetCode #200, Medium)
**Q:** Count connected components of '1's in a grid (4-directional).
**A:** For each unvisited '1', run **DFS/BFS** flipping connected '1's to '0' (visited). Count components. O(rows×cols) time/space. Teaches graph traversal on grids.

## 12. Graph / Union-Find — Number of Connected Components (LeetCode #323, Medium)
**Q:** Given n nodes and edges, count connected components.
**A:** **Union-Find** with path compression + union by rank: union endpoints of each edge, then count distinct roots. O(E·α(n)). Teaches DSU for dynamic connectivity.

## 13. Trie — Implement Trie (Prefix Tree) (LeetCode #208, Medium)
**Q:** Implement insert, search, startsWith.
**A:** Each node has `Map<Character, TrieNode>` (or array of 26) and an `isEnd` flag. Insert walks/creates nodes; search walks and checks `isEnd`; prefix walks only. O(len) per op. Teaches Trie structure.

## 14. Heap — Kth Largest Element (LeetCode #215, Medium)
**Q:** Find the kth largest element in an unsorted array.
**A:** Maintain a **min-heap** of size k (top is kth largest) → O(n log k). Or Quickselect (average O(n)). Teaches heap invariants and selection algorithms.

## 15. Binary Search Tree — Validate BST (LeetCode #98, Medium)
**Q:** Determine if a binary tree is a valid BST.
**A:** Recurse with valid `(min, max)` bounds; each node must be `> min` and `< max`. O(n). Teaches BST property and bound propagation.