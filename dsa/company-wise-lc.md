# LeetCode Problems & Solutions — Company Wise

> Curated list of frequently asked LeetCode problems at top product companies, with Java solutions.
> Difficulty legend: 🟢 Easy · 🟡 Medium · 🔴 Hard

## Table of Contents
- [Zoho ⭐⭐⭐⭐⭐](#zoho)
- [Microsoft / Microsoft India](#microsoft)
- [Adobe / Adobe India](#adobe)
- [Atlassian / Atlassian India](#atlassian)
- [Visa / Visa India](#visa)
- [Mastercard / Mastercard India](#mastercard)
- [American Express / Amex India](#amex)
- [Intuit / Intuit India](#intuit)
- [Apple](#apple)
- [ServiceNow](#servicenow)
- [HubSpot](#hubspot)
- [SAP](#sap)
- [PayPal](#paypal)
- [Goldman Sachs](#goldman)
- [Adyen](#adyen)
- [Uber](#uber)
- [Rippling](#rippling)
- [Razorpay](#razorpay)
- [Flipkart](#flipkart)
- [LLD Problems (5)](#lld)
- [HLD Problems (5)](#hld)

---

<a name="zoho"></a>
## Zoho ⭐⭐⭐⭐⭐ (great for stability)

Zoho coding rounds focus on fundamentals: arrays, strings, matrices, number theory, and pattern problems. Multiple problems in a single round, time-bound.

### 1. 🟢 Reverse a String
*Approach: Use `StringBuilder.reverse()` for an O(n) reversal without manual char swapping.*

```java
static String reverse(String s) {
    return new StringBuilder(s).reverse().toString();
}
```

### 2. 🟢 Missing Number (XOR approach, O(n), O(1))
*Approach: XOR all indices 1..n+1 with every array value; duplicates cancel, leaving the missing number. O(n) time, O(1) space.*

```java
static int missingNumber(int[] a, int n) { // n = size of array, numbers 1..n+1
    int xor = 0;
    for (int i = 1; i <= n + 1; i++) xor ^= i;
    for (int v : a) xor ^= v;
    return xor;
}
```

### 3. 🟡 Spiral Matrix Traversal
*Approach: Walk the outer boundary (top→right→bottom→left), then shrink the bounds and repeat until exhausted. O(m·n).*

```java
static List<Integer> spiral(int[][] m) {
    List<Integer> res = new ArrayList<>();
    int top = 0, bottom = m.length - 1, left = 0, right = m[0].length - 1;
    while (top <= bottom && left <= right) {
        for (int i = left; i <= right; i++) res.add(m[top][i]);
        top++;
        for (int i = top; i <= bottom; i++) res.add(m[i][right]);
        right--;
        if (top <= bottom) { for (int i = right; i >= left; i--) res.add(m[bottom][i]); bottom--; }
        if (left <= right) { for (int i = bottom; i >= top; i--) res.add(m[i][left]); left++; }
    }
    return res;
}
```

### 4. 🟢 Second Largest Element
*Approach: Single pass tracking `max` and `sec`; when a value beats `max`, demote `max` to `sec`. O(n).*

```java
static int secondLargest(int[] a) {
    int max = Integer.MIN_VALUE, sec = Integer.MIN_VALUE;
    for (int v : a) {
        if (v > max) { sec = max; max = v; }
        else if (v > sec && v != max) sec = v;
    }
    return sec;
}
```

### 5. 🟢 Anagram Check
*Approach: Tally character frequencies in two 26-slot arrays; identical tallies mean anagram. O(n).*

```java
static boolean isAnagram(String a, String b) {
    if (a.length() != b.length()) return false;
    int[] c = new int[26];
    for (char ch : a.toCharArray()) c[ch - 'a']++;
    for (char ch : b.toCharArray()) c[ch - 'a']--;
    for (int x : c) if (x != 0) return false;
    return true;
}
```

### 6. 🟢 Prime Check
*Approach: Test divisors only up to √n; the first divisor found proves compositeness. O(√n).*

```java
static boolean isPrime(int n) {
    if (n < 2) return false;
    for (int i = 2; i * i <= n; i++) if (n % i == 0) return false;
    return true;
}
```

### 7. 🟡 Kadane's Maximum Subarray Sum
*Approach: Keep a running sum, reset it to the current element whenever it goes negative, and track the global maximum. O(n).*

```java
static int maxSubArray(int[] a) {
    int max = a[0], cur = a[0];
    for (int i = 1; i < a.length; i++) {
        cur = Math.max(a[i], cur + a[i]);
        max = Math.max(max, cur);
    }
    return max;
}
```

### 8. 🟢 Pyramid Pattern
*Approach: For each row print leading spaces then an odd number of stars; both derived from the row index. O(n²).*

```java
static void pyramid(int n) {
    for (int i = 1; i <= n; i++) {
        for (int s = 0; s < n - i; s++) System.out.print(" ");
        for (int j = 0; j < 2 * i - 1; j++) System.out.print("*");
        System.out.println();
    }
}
```

---

<a name="microsoft"></a>
## Microsoft / Microsoft India

Microsoft loves trees, linked lists, graphs, DP, and classic design problems. Expect at least one tree/graph question.

### 1. 🔴 LRU Cache (Design)
*Approach: `LinkedHashMap` keeps insertion order; on `get`/`put` remove-then-reinsert to move the key to the most-recent end, and evict the oldest when over capacity. O(1) both ops.*

```java
class LRUCache {
    LinkedHashMap<Integer, Integer> m;
    int cap;
    LRUCache(int capacity) { cap = capacity; m = new LinkedHashMap<>(); }
    public int get(int key) {
        if (!m.containsKey(key)) return -1;
        int v = m.remove(key); m.put(key, v); return v;
    }
    public void put(int key, int value) {
        if (m.containsKey(key)) m.remove(key);
        else if (m.size() == cap) m.remove(m.keySet().iterator().next());
        m.put(key, value);
    }
}
```

### 2. 🔴 Serialize / Deserialize Binary Tree
*Approach: Preorder traversal with `"null"` sentinels for missing children; rebuild recursively by consuming tokens in the same order. O(n) time/space.*

```java
public String serialize(TreeNode root) {
    StringBuilder sb = new StringBuilder();
    ser(root, sb); return sb.toString();
}
void ser(TreeNode n, StringBuilder sb) {
    if (n == null) { sb.append("null,"); return; }
    sb.append(n.val).append(","); ser(n.left, sb); ser(n.right, sb);
}
public TreeNode deserialize(String data) {
    Queue<String> q = new LinkedList<>(Arrays.asList(data.split(",")));
    return des(q);
}
TreeNode des(Queue<String> q) {
    String v = q.poll();
    if (v.equals("null")) return null;
    TreeNode n = new TreeNode(Integer.parseInt(v));
    n.left = des(q); n.right = des(q); return n;
}
```

### 3. 🟡 Lowest Common Ancestor of BST
*Approach: In a BST, walk down: if both targets are smaller go left, both larger go right, otherwise the current node is the LCA. O(h).*

```java
TreeNode lca(TreeNode root, TreeNode p, TreeNode q) {
    while (root != null) {
        if (root.val < p.val && root.val < q.val) root = root.right;
        else if (root.val > p.val && root.val > q.val) root = root.left;
        else return root;
    }
    return null;
}
```

### 4. 🔴 Merge K Sorted Lists (Min-Heap)
*Approach: Push the head of each list into a min-heap; repeatedly pop the smallest, append its next node back into the heap. O(N log k).*

```java
ListNode mergeKLists(ListNode[] lists) {
    PriorityQueue<ListNode> pq = new PriorityQueue<>((a, b) -> a.val - b.val);
    for (ListNode l : lists) if (l != null) pq.offer(l);
    ListNode dummy = new ListNode(0), cur = dummy;
    while (!pq.isEmpty()) {
        cur.next = pq.poll(); cur = cur.next;
        if (cur.next != null) pq.offer(cur.next);
    }
    return dummy.next;
}
```

### 5. 🟡 Number of Islands (DFS)
*Approach: Scan the grid; on each land cell run DFS to sink (mark '0') its whole connected component, counting one island per start. O(m·n).*

```java
int numIslands(char[][] g) {
    int c = 0;
    for (int i = 0; i < g.length; i++)
        for (int j = 0; j < g[0].length; j++)
            if (g[i][j] == '1') { c++; dfs(g, i, j); }
    return c;
}
void dfs(char[][] g, int i, int j) {
    if (i < 0 || j < 0 || i >= g.length || j >= g[0].length || g[i][j] != '1') return;
    g[i][j] = '0';
    dfs(g, i+1, j); dfs(g, i-1, j); dfs(g, i, j+1); dfs(g, i, j-1);
}
```

### 6. 🔴 Trapping Rain Water
*Approach: Two pointers with running left/right maxes; water above a side equals its max minus its height. O(n) time, O(1) space.*

```java
int trap(int[] h) {
    int l = 0, r = h.length - 1, leftMax = 0, rightMax = 0, res = 0;
    while (l < r) {
        if (h[l] < h[r]) { leftMax = Math.max(leftMax, h[l]); res += leftMax - h[l++]; }
        else { rightMax = Math.max(rightMax, h[r]); res += rightMax - h[r--]; }
    }
    return res;
}
```

### 7. 🟡 Coin Change (DP)
*Approach: `dp[i]` = fewest coins for amount `i`; for each coin update `dp[i] = min(dp[i], dp[i-coin]+1)`. O(amount·coins).*

```java
int coinChange(int[] coins, int amount) {
    int[] dp = new int[amount + 1];
    Arrays.fill(dp, amount + 1); dp[0] = 0;
    for (int c : coins) for (int i = c; i <= amount; i++) dp[i] = Math.min(dp[i], dp[i - c] + 1);
    return dp[amount] > amount ? -1 : dp[amount];
}
```

### 8. 🟢 Valid Parentheses
*Approach: Push open brackets; on a close bracket pop and verify the matching type. Empty stack at end means valid. O(n).*

```java
boolean isValid(String s) {
    Stack<Character> st = new Stack<>();
    for (char c : s.toCharArray()) {
        if (c == '(' || c == '{' || c == '[') st.push(c);
        else { if (st.isEmpty()) return false;
            char t = st.pop();
            if ((c == ')' && t != '(') || (c == '}' && t != '{') || (c == ']' && t != '[')) return false;
        }
    }
    return st.isEmpty();
}
```

---

<a name="adobe"></a>
## Adobe / Adobe India

Adobe focuses on arrays, strings, DP, and matrix manipulation. Often 2 coding problems + 1 design/MCQ.

### 1. 🟡 Next Permutation
*Approach: Find the first dip from the right, swap it with the next greater element, then reverse the suffix to get the smallest larger permutation. O(n).*

```java
void nextPermutation(int[] a) {
    int i = a.length - 2;
    while (i >= 0 && a[i] >= a[i + 1]) i--;
    if (i >= 0) {
        int j = a.length - 1;
        while (a[j] <= a[i]) j--;
        int t = a[i]; a[i] = a[j]; a[j] = t;
    }
    reverse(a, i + 1);
}
void reverse(int[] a, int s) {
    int e = a.length - 1;
    while (s < e) { int t = a[s]; a[s++] = a[e]; a[e--] = t; }
}
```

### 2. 🟡 Product of Array Except Self
*Approach: Prefix products left-to-right, then multiply by suffix products right-to-left in one pass. O(n), O(1) extra space.*

```java
int[] productExceptSelf(int[] a) {
    int n = a.length, res[] = new int[n];
    res[0] = 1;
    for (int i = 1; i < n; i++) res[i] = res[i - 1] * a[i - 1];
    int r = 1;
    for (int i = n - 1; i >= 0; i--) { res[i] *= r; r *= a[i]; }
    return res;
}
```

### 3. 🟡 Longest Substring Without Repeating Characters
*Approach: Sliding window; `seen` stores the last index of each char, so `l` jumps past any repeat. O(n).*

```java
int lengthOfLongestSubstring(String s) {
    int[] seen = new int[128];
    int l = 0, max = 0;
    for (int r = 0; r < s.length(); r++) {
        l = Math.max(l, seen[s.charAt(r)]);
        max = Math.max(max, r - l + 1);
        seen[s.charAt(r)] = r + 1;
    }
    return max;
}
```

### 4. 🟡 Rotate Image (90° clockwise, in-place)
*Approach: Transpose the matrix, then reverse each row — the combined effect is a 90° clockwise rotation. O(n²), in-place.*

```java
void rotate(int[][] m) {
    int n = m.length;
    for (int i = 0; i < n; i++)
        for (int j = i; j < n; j++) { int t = m[i][j]; m[i][j] = m[j][i]; m[j][i] = t; }
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n / 2; j++) { int t = m[i][j]; m[i][j] = m[i][n - 1 - j]; m[i][n - 1 - j] = t; }
}
```

### 5. 🟡 Set Matrix Zeroes
*Approach: Use the first row/column as flags for which rows/cols to zero, after saving their own state. O(m·n), O(1) space.*

```java
void setZeroes(int[][] m) {
    boolean row0 = false, col0 = false;
    for (int i = 0; i < m.length; i++) if (m[i][0] == 0) col0 = true;
    for (int j = 0; j < m[0].length; j++) if (m[0][j] == 0) row0 = true;
    for (int i = 1; i < m.length; i++)
        for (int j = 1; j < m[0].length; j++)
            if (m[i][j] == 0) { m[i][0] = 0; m[0][j] = 0; }
    for (int i = 1; i < m.length; i++) if (m[i][0] == 0) Arrays.fill(m[i], 0);
    for (int j = 1; j < m[0].length; j++) if (m[0][j] == 0) for (int i = 0; i < m.length; i++) m[i][j] = 0;
    if (row0) Arrays.fill(m[0], 0);
    if (col0) for (int i = 0; i < m.length; i++) m[i][0] = 0;
}
```

### 6. 🟡 Word Break (DP)
*Approach: `dp[i]` = can segment prefix of length `i`; for each `j<i` if `dp[j]` and `s[j..i]` is a word, mark `dp[i]`. O(n²·w).*

```java
boolean wordBreak(String s, List<String> wordDict) {
    Set<String> set = new HashSet<>(wordDict);
    boolean[] dp = new boolean[s.length() + 1];
    dp[0] = true;
    for (int i = 1; i <= s.length(); i++)
        for (int j = 0; j < i; j++)
            if (dp[j] && set.contains(s.substring(j, i))) { dp[i] = true; break; }
    return dp[s.length()];
}
```

### 7. 🟡 Maximum Product Subarray
*Approach: Track both max and min running products (a negative flips them), and keep the global max. O(n).*

```java
int maxProduct(int[] a) {
    int max = a[0], min = a[0], res = a[0];
    for (int i = 1; i < a.length; i++) {
        if (a[i] < 0) { int t = max; max = min; min = t; }
        max = Math.max(a[i], max * a[i]);
        min = Math.min(a[i], min * a[i]);
        res = Math.max(res, max);
    }
    return res;
}
```

---

<a name="atlassian"></a>
## Atlassian / Atlassian India

Atlassian emphasizes hashing, trees, graphs, and practical design. Collaborative-tool style problems.

### 1. 🟢 Two Sum
*Approach: Hash map of value→index; for each element check if `target - value` was already seen. O(n) time, O(n) space.*

```java
int[] twoSum(int[] a, int t) {
    Map<Integer, Integer> m = new HashMap<>();
    for (int i = 0; i < a.length; i++) {
        if (m.containsKey(t - a[i])) return new int[]{m.get(t - a[i]), i};
        m.put(a[i], i);
    }
    return new int[]{};
}
```

### 2. 🟡 Group Anagrams
*Approach: Anagrams share a sorted-character key; bucket words by that key in a map. O(n·k log k).*

```java
List<List<String>> groupAnagrams(String[] s) {
    Map<String, List<String>> m = new HashMap<>();
    for (String w : s) {
        char[] c = w.toCharArray(); Arrays.sort(c);
        String k = new String(c);
        m.computeIfAbsent(k, x -> new ArrayList<>()).add(w);
    }
    return new ArrayList<>(m.values());
}
```

### 3. 🟡 Valid Sudoku
*Approach: Use a set of encoded keys (row/col/box + digit); a duplicate key means invalid. O(1) fixed 9×9.*

```java
boolean isValidSudoku(char[][] b) {
    Set<String> seen = new HashSet<>();
    for (int i = 0; i < 9; i++) for (int j = 0; j < 9; j++) {
        if (b[i][j] == '.') continue;
        String r = "r" + i + b[i][j], c = "c" + j + b[i][j], g = "g" + (i/3) + (j/3) + b[i][j];
        if (!seen.add(r) || !seen.add(c) || !seen.add(g)) return false;
    }
    return true;
}
```

### 4. 🟡 Top K Frequent Elements
*Approach: Count frequencies, then a min-heap of size k keeps only the k most frequent. O(n log k).*

```java
int[] topKFrequent(int[] a, int k) {
    Map<Integer, Integer> f = new HashMap<>();
    for (int v : a) f.put(v, f.getOrDefault(v, 0) + 1);
    PriorityQueue<Integer> pq = new PriorityQueue<>((x, y) -> f.get(x) - f.get(y));
    for (int key : f.keySet()) {
        pq.offer(key);
        if (pq.size() > k) pq.poll();
    }
    int[] res = new int[k];
    for (int i = k - 1; i >= 0; i--) res[i] = pq.poll();
    return res;
}
```

### 5. 🟡 Course Schedule (Cycle detection)
*Approach: Kahn's algorithm — topological sort via BFS on in-degrees; if all nodes are processed there's no cycle. O(V+E).*

```java
boolean canFinish(int n, int[][] p) {
    List<List<Integer>> g = new ArrayList<>();
    for (int i = 0; i < n; i++) g.add(new ArrayList<>());
    int[] indeg = new int[n];
    for (int[] e : p) { g.get(e[1]).add(e[0]); indeg[e[0]]++; }
    Queue<Integer> q = new LinkedList<>();
    for (int i = 0; i < n; i++) if (indeg[i] == 0) q.offer(i);
    int cnt = 0;
    while (!q.isEmpty()) {
        int u = q.poll(); cnt++;
        for (int v : g.get(u)) if (--indeg[v] == 0) q.offer(v);
    }
    return cnt == n;
}
```

### 6. 🟢 Binary Tree Level Order Traversal
*Approach: BFS with a queue, draining one level (size-bounded) at a time. O(n).*

```java
List<List<Integer>> levelOrder(TreeNode root) {
    List<List<Integer>> res = new ArrayList<>();
    if (root == null) return res;
    Queue<TreeNode> q = new LinkedList<>(); q.offer(root);
    while (!q.isEmpty()) {
        int sz = q.size(); List<Integer> lvl = new ArrayList<>();
        for (int i = 0; i < sz; i++) {
            TreeNode n = q.poll(); lvl.add(n.val);
            if (n.left != null) q.offer(n.left);
            if (n.right != null) q.offer(n.right);
        }
        res.add(lvl);
    }
    return res;
}
```

---

<a name="visa"></a>
## Visa / Visa India

Visa rounds are approachable: arrays, strings, math, and basic DP. Often includes a simple logic puzzle.

### 1. 🟢 Valid Palindrome
*Approach: Two pointers skipping non-alphanumerics; compare lowercased chars from both ends. O(n).*

```java
boolean isPalindrome(String s) {
    int l = 0, r = s.length() - 1;
    while (l < r) {
        while (l < r && !Character.isLetterOrDigit(s.charAt(l))) l++;
        while (l < r && !Character.isLetterOrDigit(s.charAt(r))) r--;
        if (Character.toLowerCase(s.charAt(l)) != Character.toLowerCase(s.charAt(r))) return false;
        l++; r--;
    }
    return true;
}
```

### 2. 🟢 Plus One
*Approach: Add 1 from the last digit, propagating carries left; if all were 9, allocate a longer array. O(n).*

```java
int[] plusOne(int[] d) {
    for (int i = d.length - 1; i >= 0; i--) {
        if (d[i] < 9) { d[i]++; return d; }
        d[i] = 0;
    }
    int[] res = new int[d.length + 1]; res[0] = 1; return res;
}
```

### 3. 🟢 Move Zeroes
*Approach: Keep a `j` pointer for the next non-zero slot; swap zeroes to the back in one pass. O(n).*

```java
void moveZeroes(int[] a) {
    int j = 0;
    for (int i = 0; i < a.length; i++) if (a[i] != 0) { int t = a[i]; a[i] = a[j]; a[j++] = t; }
}
```

### 4. 🟢 Best Time to Buy and Sell Stock
*Approach: Track the running minimum price; profit at each day is price − min. O(n).*

```java
int maxProfit(int[] p) {
    int min = Integer.MAX_VALUE, profit = 0;
    for (int v : p) { min = Math.min(min, v); profit = Math.max(profit, v - min); }
    return profit;
}
```

### 5. 🟢 Climbing Stairs (DP)
*Approach: Ways[n] = ways[n-1] + ways[n-2] (Fibonacci); iterate with two variables. O(n).*

```java
int climbStairs(int n) {
    if (n <= 2) return n;
    int a = 1, b = 2;
    for (int i = 3; i <= n; i++) { int c = a + b; a = b; b = c; }
    return b;
}
```

### 6. 🟡 FizzBuzz
*Approach: Straightforward modulo checks per number; build the string accordingly. O(n).*

```java
List<String> fizzBuzz(int n) {
    List<String> res = new ArrayList<>();
    for (int i = 1; i <= n; i++) {
        if (i % 15 == 0) res.add("FizzBuzz");
        else if (i % 3 == 0) res.add("Fizz");
        else if (i % 5 == 0) res.add("Buzz");
        else res.add(String.valueOf(i));
    }
    return res;
}
```

---

<a name="mastercard"></a>
## Mastercard / Mastercard India

Mastercard asks clean string/array problems and number theory. Focus on correctness and edge cases.

### 1. 🟢 Valid Anagram
*Approach: Increment counts for `a`, decrement for `b`; all zero means anagram. O(n).*

```java
boolean isAnagram(String a, String b) {
    if (a.length() != b.length()) return false;
    int[] c = new int[26];
    for (int i = 0; i < a.length(); i++) { c[a.charAt(i) - 'a']++; c[b.charAt(i) - 'a']--; }
    for (int x : c) if (x != 0) return false;
    return true;
}
```

### 2. 🟢 First Unique Character
*Approach: Count frequencies, then scan left-to-right for the first count of 1. O(n).*

```java
int firstUniqChar(String s) {
    int[] c = new int[26];
    for (char ch : s.toCharArray()) c[ch - 'a']++;
    for (int i = 0; i < s.length(); i++) if (c[s.charAt(i) - 'a'] == 1) return i;
    return -1;
}
```

### 3. 🟢 Ransom Note
*Approach: Tally magazine chars; for each ransom char decrement and fail if it goes negative. O(n).*

```java
boolean canConstruct(String r, String m) {
    int[] c = new int[26];
    for (char ch : m.toCharArray()) c[ch - 'a']++;
    for (char ch : r.toCharArray()) if (--c[ch - 'a'] < 0) return false;
    return true;
}
```

### 4. 🟢 Longest Common Prefix
*Approach: Start from the first string and keep shrinking the prefix until every other string starts with it. O(n·m).*

```java
String longestCommonPrefix(String[] s) {
    if (s.length == 0) return "";
    String pre = s[0];
    for (int i = 1; i < s.length; i++)
        while (s[i].indexOf(pre) != 0) pre = pre.substring(0, pre.length() - 1);
    return pre;
}
```

### 5. 🟢 Implement strStr (KMP)
*Approach: Build the LPS (longest prefix-suffix) table for the needle, then scan the haystack using LPS to skip comparisons. O(n+m).*

```java
int strStr(String h, String n) {
    if (n.isEmpty()) return 0;
    int[] lps = new int[n.length()];
    for (int i = 1, len = 0; i < n.length(); ) {
        if (n.charAt(i) == n.charAt(len)) lps[i++] = ++len;
        else if (len > 0) len = lps[len - 1];
        else i++;
    }
    for (int i = 0, j = 0; i < h.length(); ) {
        if (h.charAt(i) == n.charAt(j)) { i++; j++; if (j == n.length()) return i - j; }
        else if (j > 0) j = lps[j - 1];
        else i++;
    }
    return -1;
}
```

### 6. 🟢 Count Primes
*Approach: Sieve of Eratosthenes — mark multiples of each prime starting at its square. O(n log log n).*

```java
int countPrimes(int n) {
    if (n <= 2) return 0;
    boolean[] p = new boolean[n]; Arrays.fill(p, true);
    for (int i = 2; i * i < n; i++) if (p[i]) for (int j = i * i; j < n; j += i) p[j] = false;
    int c = 0; for (int i = 2; i < n; i++) if (p[i]) c++;
    return c;
}
```

---

<a name="amex"></a>
## American Express / Amex India

Amex mixes easy DSA with SQL/Data questions. Keep solutions clean and well-commented.

### 1. 🟢 Palindrome Number
*Approach: Reverse half the digits and compare to the other half (avoids overflow). O(log n).*

```java
boolean isPalindrome(int x) {
    if (x < 0 || (x % 10 == 0 && x != 0)) return false;
    int rev = 0;
    while (x > rev) { rev = rev * 10 + x % 10; x /= 10; }
    return x == rev || x == rev / 10;
}
```

### 2. 🟢 Roman to Integer
*Approach: Add each symbol's value, but subtract it when a larger value follows (e.g. IV). O(n).*

```java
int romanToInt(String s) {
    Map<Character, Integer> m = Map.of('I',1,'V',5,'X',10,'L',50,'C',100,'D',500,'M',1000);
    int res = 0;
    for (int i = 0; i < s.length(); i++) {
        int cur = m.get(s.charAt(i));
        if (i + 1 < s.length() && m.get(s.charAt(i + 1)) > cur) res -= cur;
        else res += cur;
    }
    return res;
}
```

### 3. 🟢 Merge Two Sorted Lists
*Approach: Standard two-pointer merge into a dummy-headed list. O(n+m).*

```java
ListNode mergeTwoLists(ListNode l1, ListNode l2) {
    ListNode dummy = new ListNode(0), cur = dummy;
    while (l1 != null && l2 != null) {
        if (l1.val < l2.val) { cur.next = l1; l1 = l1.next; }
        else { cur.next = l2; l2 = l2.next; }
        cur = cur.next;
    }
    cur.next = l1 != null ? l1 : l2;
    return dummy.next;
}
```

### 4. 🟢 Maximum Subarray (Kadane)
*Approach: Same Kadane idea — reset running sum on negativity, track global max. O(n).*

```java
int maxSubArray(int[] a) {
    int max = a[0], cur = a[0];
    for (int i = 1; i < a.length; i++) { cur = Math.max(a[i], cur + a[i]); max = Math.max(max, cur); }
    return max;
}
```

### 5. 🟡 Single Number II (bitwise)
*Approach: `ones`/`twos` bitmasks count mod-3 occurrences; the element appearing once ends in `ones`. O(n).*

```java
int singleNumber(int[] a) {
    int ones = 0, twos = 0;
    for (int x : a) { ones = (ones ^ x) & ~twos; twos = (twos ^ x) & ~ones; }
    return ones;
}
```

---

<a name="intuit"></a>
## Intuit / Intuit India

Intuit (TurboTax, QuickBooks) asks practical array/string/design problems with a focus on clarity.

### 1. 🟢 Reverse Linked List
*Approach: Iterate reversing each `next` pointer with a `prev`/`cur`/`nxt` triplet. O(n).*

```java
ListNode reverseList(ListNode head) {
    ListNode prev = null, cur = head;
    while (cur != null) { ListNode nxt = cur.next; cur.next = prev; prev = cur; cur = nxt; }
    return prev;
}
```

### 2. 🟢 Contains Duplicate
*Approach: Insert into a set; a failed `add` means a duplicate was seen. O(n).*

```java
boolean containsDuplicate(int[] a) {
    Set<Integer> s = new HashSet<>();
    for (int v : a) if (!s.add(v)) return true;
    return false;
}
```

### 3. 🟢 Intersection of Two Arrays
*Approach: Put one array in a set, then collect elements of the other that are present. O(n+m).*

```java
int[] intersection(int[] a, int[] b) {
    Set<Integer> s = new HashSet<>(), res = new HashSet<>();
    for (int v : a) s.add(v);
    for (int v : b) if (s.contains(v)) res.add(v);
    return res.stream().mapToInt(Integer::intValue).toArray();
}
```

### 4. 🟢 Happy Number
*Approach: Replace the number by the sum of squared digits; a cycle (detected via a set) means not happy, reaching 1 means happy. O(log n) per step.*

```java
boolean isHappy(int n) {
    Set<Integer> seen = new HashSet<>();
    while (n != 1 && seen.add(n)) {
        int sum = 0;
        while (n > 0) { int d = n % 10; sum += d * d; n /= 10; }
        n = sum;
    }
    return n == 1;
}
```

### 5. 🟡 Valid Parentheses (see Microsoft #8)

---

<a name="apple"></a>
## Apple

Apple asks a broad mix — strings, linked lists, DP, and two-pointer problems. Strong on edge cases.

### 1. 🟡 Add Two Numbers
*Approach: Walk both lists digit by digit with a carry, building the result list. O(max(n,m)).*

```java
ListNode addTwoNumbers(ListNode l1, ListNode l2) {
    ListNode dummy = new ListNode(0), cur = dummy; int carry = 0;
    while (l1 != null || l2 != null || carry > 0) {
        int s = carry + (l1 != null ? l1.val : 0) + (l2 != null ? l2.val : 0);
        carry = s / 10; cur.next = new ListNode(s % 10); cur = cur.next;
        if (l1 != null) l1 = l1.next; if (l2 != null) l2 = l2.next;
    }
    return dummy.next;
}
```

### 2. 🔴 Longest Palindromic Substring
*Approach: Expand around each center (odd and even) to find the longest palindrome. O(n²).*

```java
String longestPalindrome(String s) {
    int start = 0, max = 0;
    for (int i = 0; i < s.length(); i++) {
        int l1 = expand(s, i, i), l2 = expand(s, i, i + 1);
        int len = Math.max(l1, l2);
        if (len > max) { max = len; start = i - (len - 1) / 2; }
    }
    return s.substring(start, start + max);
}
int expand(String s, int l, int r) {
    while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) { l--; r++; }
    return r - l - 1;
}
```

### 3. 🟡 3Sum
*Approach: Sort, fix the first element, then two-pointer the rest to hit sum 0; skip duplicates. O(n²).*

```java
List<List<Integer>> threeSum(int[] a) {
    Arrays.sort(a); List<List<Integer>> res = new ArrayList<>();
    for (int i = 0; i < a.length - 2; i++) {
        if (i > 0 && a[i] == a[i - 1]) continue;
        int l = i + 1, r = a.length - 1;
        while (l < r) {
            int sum = a[i] + a[l] + a[r];
            if (sum == 0) { res.add(Arrays.asList(a[i], a[l], a[r]));
                while (l < r && a[l] == a[l + 1]) l++; while (l < r && a[r] == a[r - 1]) r--; l++; r--; }
            else if (sum < 0) l++; else r--;
        }
    }
    return res;
}
```

### 4. 🟡 Container With Most Water
*Approach: Two pointers from the ends; move the shorter side inward since it limits area. O(n).*

```java
int maxArea(int[] h) {
    int l = 0, r = h.length - 1, max = 0;
    while (l < r) {
        max = Math.max(max, Math.min(h[l], h[r]) * (r - l));
        if (h[l] < h[r]) l++; else r--;
    }
    return max;
}
```

### 5. 🟢 Two Sum (see Atlassian #1)

---

<a name="servicenow"></a>
## ServiceNow

ServiceNow focuses on trees, strings, and fundamental data structures. Often includes a system-design-lite question.

### 1. 🟢 Binary Tree Inorder Traversal
*Approach: Iterative DFS with an explicit stack — go left fully, then visit, then right. O(n).*

```java
List<Integer> inorder(TreeNode root) {
    List<Integer> res = new ArrayList<>(); Stack<TreeNode> st = new Stack<>();
    while (root != null || !st.isEmpty()) {
        while (root != null) { st.push(root); root = root.left; }
        root = st.pop(); res.add(root.val); root = root.right;
    }
    return res;
}
```

### 2. 🟢 Maximum Depth of Binary Tree
*Approach: Recursively return 1 + max(depth(left), depth(right)). O(n).*

```java
int maxDepth(TreeNode root) {
    return root == null ? 0 : 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
}
```

### 3. 🟢 Symmetric Tree
*Approach: Two trees are mirrors if roots match and left↔right subtrees are mirrors. O(n).*

```java
boolean isSymmetric(TreeNode root) { return root == null || isMirror(root.left, root.right); }
boolean isMirror(TreeNode a, TreeNode b) {
    if (a == null || b == null) return a == b;
    return a.val == b.val && isMirror(a.left, b.right) && isMirror(a.right, b.left);
}
```

### 4. 🟢 Valid Parentheses (see Microsoft #8)
### 5. 🟢 Two Sum (see Atlassian #1)

---

<a name="hubspot"></a>
## HubSpot

HubSpot asks approachable problems — strings, arrays, hashing, and linked lists. Good for mid-level roles.

### 1. 🟢 Reverse String (in-place)
*Approach: Swap characters from both ends moving inward. O(n).*

```java
void reverseString(char[] s) {
    int l = 0, r = s.length - 1;
    while (l < r) { char t = s[l]; s[l++] = s[r]; s[r--] = t; }
}
```

### 2. 🟢 Intersection of Two Linked Lists
*Approach: Two pointers traverse both lists; when one ends, jump to the other's head — they meet at the intersection (or null). O(n).*

```java
ListNode getIntersectionNode(ListNode a, ListNode b) {
    ListNode p = a, q = b;
    while (p != q) { p = (p == null) ? b : p.next; q = (q == null) ? a : q.next; }
    return p;
}
```

### 3. 🟢 Excel Sheet Column Title
*Approach: Repeatedly take `(n-1) % 26` for the letter and divide by 26 (1-based → 0-based). O(log n).*

```java
String convertToTitle(int n) {
    StringBuilder sb = new StringBuilder();
    while (n > 0) { n--; sb.append((char)('A' + n % 26)); n /= 26; }
    return sb.reverse().toString();
}
```

### 4. 🟢 Valid Anagram (see Mastercard #1)
### 5. 🟢 Two Sum (see Atlassian #1)

---

<a name="sap"></a>
## SAP

SAP (enterprise software) rounds test arrays, strings, trees, DP, and BFS/graph fundamentals. Expect clean OOP-style solutions.

### 1. 🟢 Two Sum (see [Atlassian #1](#atlassian))
### 2. 🟢 Valid Parentheses (see [Microsoft #8](#microsoft))
### 3. 🟡 Merge Intervals
*Approach: Sort by start time; merge a running interval with the next when they overlap, else start a new one. O(n log n).*

```java
int[][] merge(int[][] iv) {
    Arrays.sort(iv, (a, b) -> a[0] - b[0]);
    List<int[]> res = new ArrayList<>();
    for (int[] i : iv) {
        if (res.isEmpty() || res.get(res.size() - 1)[1] < i[0]) res.add(i);
        else res.get(res.size() - 1)[1] = Math.max(res.get(res.size() - 1)[1], i[1]);
    }
    return res.toArray(new int[0][]);
}
```
### 4. 🔴 Word Ladder (BFS)
*Approach: BFS from begin to end word, generating one-char neighbors and removing visited words from the set. O(N·26·L).*

```java
int ladderLength(String b, String e, List<String> w) {
    Set<String> set = new HashSet<>(w);
    if (!set.contains(e)) return 0;
    Queue<String> q = new LinkedList<>(); q.offer(b); set.remove(b);
    int len = 1;
    while (!q.isEmpty()) {
        int sz = q.size();
        for (int i = 0; i < sz; i++) {
            String s = q.poll();
            if (s.equals(e)) return len;
            for (int j = 0; j < s.length(); j++)
                for (char c = 'a'; c <= 'z'; c++) {
                    String nb = s.substring(0, j) + c + s.substring(j + 1);
                    if (set.contains(nb)) { q.offer(nb); set.remove(nb); }
                }
        }
        len++;
    }
    return 0;
}
```
### 5. 🔴 Edit Distance (DP)
*Approach: `dp[i][j]` = edits to turn prefix i of a into prefix j of b; pick insert/delete/replace minimum. O(|a|·|b|).*

```java
int minDistance(String a, String b) {
    int[][] dp = new int[a.length() + 1][b.length() + 1];
    for (int i = 0; i <= a.length(); i++) dp[i][0] = i;
    for (int j = 0; j <= b.length(); j++) dp[0][j] = j;
    for (int i = 1; i <= a.length(); i++)
        for (int j = 1; j <= b.length(); j++)
            dp[i][j] = a.charAt(i - 1) == b.charAt(j - 1) ? dp[i - 1][j - 1]
                : 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
    return dp[a.length()][b.length()];
}
```
### 6. 🟡 Binary Tree Right Side View
*Approach: BFS level by level; the last node visited at each level is the right-side view. O(n).*

```java
List<Integer> rightSideView(TreeNode root) {
    List<Integer> res = new ArrayList<>();
    if (root == null) return res;
    Queue<TreeNode> q = new LinkedList<>(); q.offer(root);
    while (!q.isEmpty()) {
        int sz = q.size();
        for (int i = 0; i < sz; i++) {
            TreeNode n = q.poll();
            if (i == sz - 1) res.add(n.val);
            if (n.left != null) q.offer(n.left);
            if (n.right != null) q.offer(n.right);
        }
    }
    return res;
}
```
### 7. 🟢 Design HashSet
*Approach: A fixed boolean array indexed by the value gives O(1) add/remove/contains (constraint: values < 10⁶).*

```java
class MyHashSet {
    boolean[] s = new boolean[1000001];
    void add(int k) { s[k] = true; }
    void remove(int k) { s[k] = false; }
    boolean contains(int k) { return s[k]; }
}
```
### 8. 🟡 String to Integer (atoi)
*Approach: Trim leading spaces, read the optional sign, accumulate digits, and clamp to 32-bit bounds on overflow. O(n).*

```java
int myAtoi(String s) {
    int i = 0, n = s.length(), sign = 1, res = 0;
    while (i < n && s.charAt(i) == ' ') i++;
    if (i < n && (s.charAt(i) == '+' || s.charAt(i) == '-')) sign = s.charAt(i++) == '-' ? -1 : 1;
    while (i < n && Character.isDigit(s.charAt(i))) {
        int d = s.charAt(i++) - '0';
        if (res > (Integer.MAX_VALUE - d) / 10) return sign == 1 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        res = res * 10 + d;
    }
    return res * sign;
}
```
### 9. 🟡 Letter Combinations of Phone Number
*Approach: BFS/iterative expansion — for each digit append its letters to every string built so far. O(4ⁿ).*

```java
List<String> letterCombinations(String d) {
    if (d.isEmpty()) return new ArrayList<>();
    String[] m = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};
    List<String> res = new ArrayList<>(); res.add("");
    for (char c : d.toCharArray()) {
        List<String> tmp = new ArrayList<>();
        for (String p : res) for (char x : m[c - '0'].toCharArray()) tmp.add(p + x);
        res = tmp;
    }
    return res;
}
```
### 10. 🔴 Minimum Window Substring
*Approach: Sliding window with a frequency map; expand `r` until all target chars are present, then shrink `l` to minimize. O(|s|+|t|).*

```java
String minWindow(String s, String t) {
    int[] need = new int[128]; int req = 0;
    for (char c : t.toCharArray()) if (need[c]++ == 0) req++;
    int l = 0, r = 0, formed = 0, start = 0, len = Integer.MAX_VALUE;
    while (r < s.length()) {
        char c = s.charAt(r); need[c]--;
        if (need[c] == 0) formed++;
        while (formed == req) {
            if (r - l + 1 < len) { len = r - l + 1; start = l; }
            char cl = s.charAt(l); need[cl]++; if (need[cl] > 0) formed--; l++;
        }
        r++;
    }
    return len == Integer.MAX_VALUE ? "" : s.substring(start, start + len);
}
```

---

<a name="paypal"></a>
## PayPal

PayPal (payments) asks string/array/DP problems with a focus on correctness and edge cases around money/validation.

### 1. 🟢 Valid Palindrome (see [Visa #1](#visa))
### 2. 🟡 Group Anagrams (see [Atlassian #2](#atlassian))
### 3. 🔴 LRU Cache (see [Microsoft #1](#microsoft))
### 4. 🟡 Coin Change (see [Microsoft #7](#microsoft))
### 5. 🟡 Decode Ways (DP)
*Approach: `dp[i]` = ways to decode prefix i; add `dp[i-1]` for a valid single digit and `dp[i-2]` for a valid two-digit. O(n).*

```java
int numDecodings(String s) {
    int[] dp = new int[s.length() + 1]; dp[0] = 1; dp[1] = s.charAt(0) == '0' ? 0 : 1;
    for (int i = 2; i <= s.length(); i++) {
        int one = s.charAt(i - 1) - '0';
        int two = Integer.parseInt(s.substring(i - 2, i));
        if (one >= 1) dp[i] += dp[i - 1];
        if (two >= 10 && two <= 26) dp[i] += dp[i - 2];
    }
    return dp[s.length()];
}
```
### 6. 🟡 Validate IP Address
*Approach: Split on `.` (IPv4) or `:` (IPv6) and validate each chunk's format/range; return the matching type or "Neither". O(1).*

```java
String validIPAddress(String ip) {
    if (ip.indexOf('.') >= 0) {
        String[] p = ip.split("\\.", -1);
        if (p.length != 4) return "Neither";
        for (String s : p) {
            if (s.isEmpty() || s.length() > 3 || (s.length() > 1 && s.charAt(0) == '0')) return "Neither";
            for (char c : s.toCharArray()) if (!Character.isDigit(c)) return "Neither";
            if (Integer.parseInt(s) > 255) return "Neither";
        }
        return "IPv4";
    } else {
        String[] p = ip.split(":", -1);
        if (p.length != 8) return "Neither";
        for (String s : p) {
            if (s.isEmpty() || s.length() > 4) return "Neither";
            for (char c : s.toCharArray())
                if (!Character.isDigit(c) && !('a' <= c && c <= 'f') && !('A' <= c && c <= 'F')) return "Neither";
        }
        return "IPv6";
    }
}
```
### 7. 🟡 Maximum Product Subarray (see [Adobe #7](#adobe))
### 8. 🟡 Subarray Sum Equals K
*Approach: Prefix-sum + hash map counting how many times `sum - k` occurred; each contributes a valid subarray. O(n).*

```java
int subarraySum(int[] a, int k) {
    Map<Integer, Integer> m = new HashMap<>(); m.put(0, 1);
    int sum = 0, cnt = 0;
    for (int v : a) { sum += v; cnt += m.getOrDefault(sum - k, 0); m.put(sum, m.getOrDefault(sum, 0) + 1); }
    return cnt;
}
```
### 9. 🟢 Design HashMap (see [LLD #4](#lld))
### 10. 🟡 Word Break (see [Adobe #6](#adobe))

---

<a name="goldman"></a>
## Goldman Sachs

Goldman Sachs (finance) has some of the hardest DSA screens — expect DP, stacks, and two-pointer/array mastery.

### 1. 🟢 Two Sum (see [Atlassian #1](#atlassian))
### 2. 🟡 3Sum (see [Apple #3](#apple))
### 3. 🔴 Trapping Rain Water (see [Microsoft #6](#microsoft))
### 4. 🔴 Largest Rectangle in Histogram
*Approach: Monotonic increasing stack; when a shorter bar arrives, pop and compute area with the popped bar as height and stack top as left bound. O(n).*

```java
int largestRectangleArea(int[] h) {
    Stack<Integer> st = new Stack<>(); int max = 0;
    for (int i = 0; i <= h.length; i++) {
        int cur = i == h.length ? 0 : h[i];
        while (!st.isEmpty() && cur < h[st.peek()]) {
            int ht = h[st.pop()];
            int w = st.isEmpty() ? i : i - st.peek() - 1;
            max = Math.max(max, ht * w);
        }
        st.push(i);
    }
    return max;
}
```
### 5. 🟢 Best Time to Buy and Sell Stock (see [Visa #4](#visa))
### 6. 🟡 Permutations
*Approach: Backtrack picking each unused element, building the permutation and undoing the choice on return. O(n!).*

```java
List<List<Integer>> permute(int[] a) {
    List<List<Integer>> res = new ArrayList<>();
    backtrack(a, new ArrayList<>(), new boolean[a.length], res);
    return res;
}
void backtrack(int[] a, List<Integer> t, boolean[] used, List<List<Integer>> res) {
    if (t.size() == a.length) { res.add(new ArrayList<>(t)); return; }
    for (int i = 0; i < a.length; i++) if (!used[i]) {
        used[i] = true; t.add(a[i]); backtrack(a, t, used, res); t.remove(t.size() - 1); used[i] = false;
    }
}
```
### 7. 🟡 Combination Sum
*Approach: Backtrack allowing reuse of the same candidate (index not advanced) until the remaining target hits 0 or negative. O(2ⁿ).*

```java
List<List<Integer>> combinationSum(int[] a, int t) {
    List<List<Integer>> res = new ArrayList<>();
    backtrack(a, t, 0, new ArrayList<>(), res);
    return res;
}
void backtrack(int[] a, int rem, int s, List<Integer> t, List<List<Integer>> res) {
    if (rem == 0) { res.add(new ArrayList<>(t)); return; }
    if (rem < 0) return;
    for (int i = s; i < a.length; i++) { t.add(a[i]); backtrack(a, rem - a[i], i, t, res); t.remove(t.size() - 1); }
}
```
### 8. 🟡 Word Search
*Approach: DFS from every cell; mark visited with a temporary char and restore on backtrack. O(m·n·3^L).*

```java
boolean exist(char[][] b, String w) {
    for (int i = 0; i < b.length; i++) for (int j = 0; j < b[0].length; j++) if (dfs(b, w, i, j, 0)) return true;
    return false;
}
boolean dfs(char[][] b, String w, int i, int j, int k) {
    if (k == w.length()) return true;
    if (i < 0 || j < 0 || i >= b.length || j >= b[0].length || b[i][j] != w.charAt(k)) return false;
    char c = b[i][j]; b[i][j] = '#';
    boolean f = dfs(b, w, i + 1, j, k + 1) || dfs(b, w, i - 1, j, k + 1) || dfs(b, w, i, j + 1, k + 1) || dfs(b, w, i, j - 1, k + 1);
    b[i][j] = c; return f;
}
```
### 9. 🔴 Median of Two Sorted Arrays
*Approach: Binary search the smaller array to find a partition where left/right halves are balanced and max-left ≤ min-right. O(log min(m,n)).*

```java
double findMedianSortedArrays(int[] a, int[] b) {
    if (a.length > b.length) { int[] t = a; a = b; b = t; }
    int m = a.length, n = b.length, lo = 0, hi = m;
    while (lo <= hi) {
        int i = (lo + hi) / 2, j = (m + n + 1) / 2 - i;
        int maxL = (i == 0) ? Integer.MIN_VALUE : a[i - 1];
        int minR = (i == m) ? Integer.MAX_VALUE : a[i];
        int maxL2 = (j == 0) ? Integer.MIN_VALUE : b[j - 1];
        int minR2 = (j == n) ? Integer.MAX_VALUE : b[j];
        if (maxL <= minR2 && maxL2 <= minR) {
            if ((m + n) % 2 == 0) return (Math.max(maxL, maxL2) + Math.min(minR, minR2)) / 2.0;
            return Math.max(maxL, maxL2);
        } else if (maxL > minR2) hi = i - 1; else lo = i + 1;
    }
    return 0;
}
```
### 10. 🔴 Regular Expression Matching
*Approach: DP where `dp[i][j]` = match of s[0..i] with p[0..j]; handle `.` and `*` (zero or more of previous char). O(|s|·|p|).*

```java
boolean isMatch(String s, String p) {
    boolean[][] dp = new boolean[s.length() + 1][p.length() + 1];
    dp[0][0] = true;
    for (int j = 1; j <= p.length(); j++) if (p.charAt(j - 1) == '*') dp[0][j] = dp[0][j - 2];
    for (int i = 1; i <= s.length(); i++) for (int j = 1; j <= p.length(); j++) {
        char pc = p.charAt(j - 1);
        if (pc == s.charAt(i - 1) || pc == '.') dp[i][j] = dp[i - 1][j - 1];
        else if (pc == '*') {
            dp[i][j] = dp[i][j - 2];
            if (p.charAt(j - 2) == s.charAt(i - 1) || p.charAt(j - 2) == '.') dp[i][j] |= dp[i - 1][j];
        }
    }
    return dp[s.length()][p.length()];
}
```

---

<a name="adyen"></a>
## Adyen

Adyen (payments, Netherlands) asks practical arrays, hashing, trees, and design problems similar to other fin-tech firms.

### 1. 🟢 Valid Parentheses (see [Microsoft #8](#microsoft))
### 2. 🟢 Reverse Linked List (see [Intuit #1](#intuit))
### 3. 🟢 Two Sum (see [Atlassian #1](#atlassian))
### 4. 🟡 Merge Intervals (see [SAP #3](#sap))
### 5. 🔴 LRU Cache (see [Microsoft #1](#microsoft))
### 6. 🟡 Number of Islands (see [Microsoft #5](#microsoft))
### 7. 🟡 Product of Array Except Self (see [Adobe #2](#adobe))
### 8. 🟡 Top K Frequent (see [Atlassian #4](#atlassian))
### 9. 🟡 Design Twitter (see [HLD #2](#hld))
### 10. 🟡 Valid Sudoku (see [Atlassian #3](#atlassian))

---

<a name="uber"></a>
## Uber

Uber (maps / mobility) emphasizes graphs, intervals, and system design. Expect geo/route-style problems.

### 1. 🔴 Design Uber (see [HLD](#hld) — extend DesignChatSystem/URLShortener patterns)
### 2. 🟡 Merge Intervals (see [SAP #3](#sap))
### 3. 🟡 Meeting Rooms II
*Approach: Sort by start; a min-heap of end times tracks active rooms — pop ended meetings, push the new end. Heap size = rooms needed. O(n log n).*

```java
int minMeetingRooms(int[][] iv) {
    Arrays.sort(iv, (a, b) -> a[0] - b[0]);
    PriorityQueue<Integer> pq = new PriorityQueue<>();
    for (int[] i : iv) {
        if (!pq.isEmpty() && pq.peek() <= i[0]) pq.poll();
        pq.offer(i[1]);
    }
    return pq.size();
}
```
### 4. 🟡 Word Ladder (see [SAP #4](#sap))
### 5. 🟡 Number of Islands (see [Microsoft #5](#microsoft))
### 6. 🔴 Dijkstra Shortest Path
*Approach: Priority-queue Dijkstra relaxing edges from the closest unvisited node; skip stale heap entries. O((V+E) log V).*

```java
int dijkstra(int n, int[][] edges, int src, int dst) {
    Map<Integer, List<int[]>> g = new HashMap<>();
    for (int[] e : edges) {
        g.computeIfAbsent(e[0], k -> new ArrayList<>()).add(new int[]{e[1], e[2]});
        g.computeIfAbsent(e[1], k -> new ArrayList<>()).add(new int[]{e[0], e[2]});
    }
    int[] dist = new int[n]; Arrays.fill(dist, Integer.MAX_VALUE); dist[src] = 0;
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]); pq.offer(new int[]{src, 0});
    while (!pq.isEmpty()) {
        int[] cur = pq.poll(); int u = cur[0], d = cur[1];
        if (d > dist[u]) continue;
        for (int[] nb : g.getOrDefault(u, new ArrayList<>())) {
            int v = nb[0], w = nb[1];
            if (d + w < dist[v]) { dist[v] = d + w; pq.offer(new int[]{v, dist[v]}); }
        }
    }
    return dist[dst] == Integer.MAX_VALUE ? -1 : dist[dst];
}
```
### 7. 🟢 Two Sum (see [Atlassian #1](#atlassian))
### 8. 🟢 Valid Parentheses (see [Microsoft #8](#microsoft))
### 9. 🔴 LRU Cache (see [Microsoft #1](#microsoft))
### 10. 🟡 Design Parking Lot (see [LLD #2](#lld))

---

<a name="rippling"></a>
## Rippling

Rippling (HR/payroll) asks a balanced mix — hashing, DP, intervals, and design. Clean, testable code matters.

### 1. 🟢 Two Sum (see [Atlassian #1](#atlassian))
### 2. 🟡 Group Anagrams (see [Atlassian #2](#atlassian))
### 3. 🔴 LRU Cache (see [Microsoft #1](#microsoft))
### 4. 🟢 Design HashMap (see [LLD #4](#lld))
### 5. 🟢 Valid Parentheses (see [Microsoft #8](#microsoft))
### 6. 🟡 Merge Intervals (see [SAP #3](#sap))
### 7. 🟡 Top K Frequent (see [Atlassian #4](#atlassian))
### 8. 🟡 Word Break (see [Adobe #6](#adobe))
### 9. 🟡 Product Except Self (see [Adobe #2](#adobe))
### 10. 🟡 Course Schedule (see [Atlassian #5](#atlassian))

---

<a name="razorpay"></a>
## Razorpay

Razorpay (Indian payments) asks approachable arrays/strings/hashing plus one design problem — similar to other fin-tech screens.

### 1. 🟢 Valid Anagram (see [Mastercard #1](#mastercard))
### 2. 🟢 Two Sum (see [Atlassian #1](#atlassian))
### 3. 🟢 Reverse Linked List (see [Intuit #1](#intuit))
### 4. 🟢 Move Zeroes (see [Visa #3](#visa))
### 5. 🟢 Maximum Subarray (see [Amex #4](#amex))
### 6. 🔴 LRU Cache (see [Microsoft #1](#microsoft))
### 7. 🟡 Group Anagrams (see [Atlassian #2](#atlassian))
### 8. 🟢 Valid Parentheses (see [Microsoft #8](#microsoft))
### 9. 🟡 Coin Change (see [Microsoft #7](#microsoft))
### 10. 🟢 Design HashMap (see [LLD #4](#lld))

---

<a name="flipkart"></a>
## Flipkart

Flipkart (Indian e-commerce) asks a strong mix of DSA plus LLD/HLD. Often includes a design round.

### 1. 🟢 Two Sum (see [Atlassian #1](#atlassian))
### 2. 🔴 LRU Cache (see [Microsoft #1](#microsoft))
### 3. 🟡 Merge Intervals (see [SAP #3](#sap))
### 4. 🟡 Design Parking Lot (see [LLD #2](#lld))
### 5. 🟡 Design URL Shortener (see [HLD #1](#hld))
### 6. 🟡 Number of Islands (see [Microsoft #5](#microsoft))
### 7. 🟢 Valid Parentheses (see [Microsoft #8](#microsoft))
### 8. 🟡 Product Except Self (see [Adobe #2](#adobe))
### 9. 🟡 Top K Frequent (see [Atlassian #4](#atlassian))
### 10. 🟡 Word Break (see [Adobe #6](#adobe))

---

<a name="lld"></a>
## LLD Problems (5) — Low Level Design

Reference implementations live in `system-design/lld/`. Practice writing these from scratch with proper classes, interfaces, and OOP.

### 1. 🟡 LRU Cache — `system-design/lld/DesignLRUCache.java`
*Approach: Combine a doubly-linked list (order) with a HashMap (O(1) lookup); move accessed nodes to the head, evict the tail. O(1) get/put.*
Design a cache with `get`/`put` in O(1) using `LinkedHashMap` or a doubly-linked list + HashMap.

### 2. 🟡 Design Parking Lot — `system-design/lld/DesignParkingLot.java`
*Approach: Class hierarchy of Vehicle types and Spot types; a ParkingLot holds Floors → Rows → Spots and assigns the nearest free compatible spot.*
Model floors, rows, parking spots by vehicle type (car/bike/truck), and spot allocation/freeing.

### 3. 🟡 Logger Rate Limiter — `system-design/lld/DesignLogger.java`
*Approach: Keep a `Map<message, lastTimestamp>`; print only if `now - last >= 10`, then update. O(1) per call.*
`shouldPrintMessage(timestamp, message)` returns true if the same message was NOT printed in the last 10 seconds. Use a HashMap of message→last timestamp.

### 4. 🟢 Design HashMap — `system-design/lld/DesignHashMap.java`
*Approach: Array of buckets; each bucket is a linked list of entries (separate chaining). Hash the key, find the bucket, then linear-search the chain.*
Implement `put`, `get`, `remove` with separate chaining (array of linked-list buckets) and a hash function.

### 5. 🟡 Design a Vending Machine
*Approach: Model the machine with a State pattern (Idle, HasMoney, Dispensing, ReturnChange) and an Inventory of products + a Coin handler.*
Model states (Idle → CollectCash → Dispense → ReturnChange), inventory of items, and coin handling. Use the State pattern.

---

<a name="hld"></a>
## HLD Problems (5) — High Level Design

Reference implementations live in `system-design/hld/`. Focus on requirements, API, data model, scaling, and trade-offs.

### 1. 🟡 URL Shortener — `system-design/hld/URLShortener.java`
*Approach: Generate a unique ID (counter / hashid), base62-encode it to a short key, store the mapping in a DB, and serve reads through a cache (CDN-friendly).*
Encode long URLs to short keys (base62), handle collisions, TTL, analytics, and read-heavy scaling with cache.

### 2. 🟡 Design Twitter — `system-design/hld/DesignTwitter.java`
*Approach: Users follow others; on tweet, push to followers' timelines (or pull on read), merge by timestamp, and cache hot timelines.*
Feed timeline, follow/unfollow, tweet posting, fan-out (push vs pull), and caching hot timelines.

### 3. 🟡 Design Chat System — `system-design/hld/DesignChatSystem.java`
*Approach: WebSocket gateway for live messages, a message store (sharded by conversation), and async delivery with read receipts and ordering by sequence id.*
1:1 and group chat, message ordering, delivery receipts, WebSocket vs polling, and message store sharding.

### 4. 🟡 Design File Storage — `system-design/hld/DesignFileStorage.java`
*Approach: Client uploads to a metadata service which returns upload URLs; files are chunked, stored in object storage, served via CDN, with dedup by content hash.*
Upload/download, chunking, metadata service, CDN, deduplication, and consistency.

### 5. 🟡 Design Notification Service — `system-design/hld/DesignNotificationService.java`
*Approach: Producers emit events to a queue; consumers format templates per channel (email/SMS/push) and send with retry + rate limiting.*
Multi-channel (email/SMS/push) with a queue, retry, rate limiting, and template store.

---

## Quick Revision Tips
- **Zoho**: Master arrays, strings, matrices, prime/number theory, and pattern printing. Speed matters.
- **Microsoft / Adobe**: Trees, graphs (DFS/BFS), DP, and at least one design problem (LRU, serialize).
- **Fin-tech (Visa, Mastercard, Amex, Intuit)**: Clean string/array/number problems; watch edge cases.
- **Atlassian / ServiceNow / HubSpot**: Hashing, trees, and practical problems.
- **Apple**: Two-pointer, DP, and palindrome/string manipulation with strong edge-case handling.

> All solutions are in Java and follow optimal time/space complexity. Practice by re-implementing from scratch under time pressure.