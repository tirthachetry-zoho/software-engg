package companywise.GoldmanSachs.dsa;

import java.util.*;

/**
 * Goldman Sachs — DSA Interview Problems (Java)
 *
 * Goldman Sachs coding screens are notoriously hard and ask MULTIPLE problems per round.
 * This file contains the most frequently asked GS problems across arrays, two-pointers,
 * stacks, strings, DP, graphs, trees, and design, each with an inline explanation of the
 * approach and optimal complexity. Every method is runnable from main() with a sanity test.
 *
 * Difficulty legend: E = Easy, M = Medium, H = Hard
 */
public class GoldmanDSA {

    // =========================================================================
    // 1. TWO SUM  [E]  (Arrays / Hash Map)
    // Approach: Store value->index in a HashMap. For each element x, if (target-x)
    // was already seen, we have the pair. O(n) time, O(n) space.
    // =========================================================================
    static int[] twoSum(int[] a, int t) {
        Map<Integer, Integer> m = new HashMap<>();
        for (int i = 0; i < a.length; i++) {
            if (m.containsKey(t - a[i])) return new int[]{m.get(t - a[i]), i};
            m.put(a[i], i);
        }
        return new int[]{};
    }

    // =========================================================================
    // 2. 3SUM  [M]  (Array / Two Pointers)
    // Approach: Sort, fix the first element, then two-pointer the rest to hit sum 0.
    // Skip duplicates to avoid repeated triplets. O(n^2) time, O(1) extra space.
    // =========================================================================
    static List<List<Integer>> threeSum(int[] a) {
        Arrays.sort(a);
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < a.length - 2; i++) {
            if (i > 0 && a[i] == a[i - 1]) continue;
            int l = i + 1, r = a.length - 1;
            while (l < r) {
                int s = a[i] + a[l] + a[r];
                if (s == 0) {
                    res.add(Arrays.asList(a[i], a[l], a[r]));
                    while (l < r && a[l] == a[l + 1]) l++;
                    while (l < r && a[r] == a[r - 1]) r--;
                    l++; r--;
                } else if (s < 0) l++; else r--;
            }
        }
        return res;
    }

    // =========================================================================
    // 3. TRAPPING RAIN WATER  [H]  (Array / Two Pointers)
    // Approach: Water above a bar = min(maxLeft, maxRight) - height. Two pointers
    // advance the side with the smaller max, accumulating trapped water. O(n), O(1).
    // =========================================================================
    static int trap(int[] h) {
        int l = 0, r = h.length - 1, leftMax = 0, rightMax = 0, res = 0;
        while (l < r) {
            if (h[l] < h[r]) {
                leftMax = Math.max(leftMax, h[l]);
                res += leftMax - h[l++];
            } else {
                rightMax = Math.max(rightMax, h[r]);
                res += rightMax - h[r--];
            }
        }
        return res;
    }

    // =========================================================================
    // 4. LARGEST RECTANGLE IN HISTOGRAM  [H]  (Stack / Monotonic)
    // Approach: Maintain a monotonic-increasing stack of bar indices. When a shorter
    // bar arrives, pop and compute area with popped bar as height and stack-top as
    // left bound. O(n) time, O(n) space.
    // =========================================================================
    static int largestRectangleArea(int[] h) {
        Stack<Integer> st = new Stack<>();
        int max = 0;
        for (int i = 0; i <= h.length; i++) {
            int cur = (i == h.length) ? 0 : h[i];
            while (!st.isEmpty() && cur < h[st.peek()]) {
                int ht = h[st.pop()];
                int w = st.isEmpty() ? i : i - st.peek() - 1;
                max = Math.max(max, ht * w);
            }
            st.push(i);
        }
        return max;
    }

    // =========================================================================
    // 5. CONTAINER WITH MOST WATER  [M]  (Array / Two Pointers)
    // Approach: Two pointers from ends. Area = min(h[l],h[r]) * (r-l). Move the
    // shorter side inward (it limits area). O(n) time, O(1) space.
    // =========================================================================
    static int maxArea(int[] h) {
        int l = 0, r = h.length - 1, max = 0;
        while (l < r) {
            max = Math.max(max, Math.min(h[l], h[r]) * (r - l));
            if (h[l] < h[r]) l++; else r--;
        }
        return max;
    }

    // =========================================================================
    // 6. BEST TIME TO BUY AND SELL STOCK  [E]  (Array / Greedy)
    // Approach: Track running minimum price; profit at each day = price - min. O(n).
    // =========================================================================
    static int maxProfit(int[] p) {
        int min = Integer.MAX_VALUE, profit = 0;
        for (int v : p) {
            min = Math.min(min, v);
            profit = Math.max(profit, v - min);
        }
        return profit;
    }

    // =========================================================================
    // 7. PRODUCT OF ARRAY EXCEPT SELF  [M]  (Array / Prefix-Suffix)
    // Approach: Left-to-right prefix products, then multiply by right-to-left suffix
    // in one pass. O(n) time, O(1) extra space (output excluded).
    // =========================================================================
    static int[] productExceptSelf(int[] a) {
        int n = a.length, res[] = new int[n];
        res[0] = 1;
        for (int i = 1; i < n; i++) res[i] = res[i - 1] * a[i - 1];
        int r = 1;
        for (int i = n - 1; i >= 0; i--) { res[i] *= r; r *= a[i]; }
        return res;
    }

    // =========================================================================
    // 8. SUBARRAY SUM EQUALS K  [M]  (Array / Prefix Sum + Hash)
    // Approach: Count how many times (prefixSum - k) occurred. Each such prefix gives
    // a valid subarray. O(n) time, O(n) space.
    // =========================================================================
    static int subarraySum(int[] a, int k) {
        Map<Integer, Integer> m = new HashMap<>();
        m.put(0, 1);
        int sum = 0, cnt = 0;
        for (int v : a) {
            sum += v;
            cnt += m.getOrDefault(sum - k, 0);
            m.put(sum, m.getOrDefault(sum, 0) + 1);
        }
        return cnt;
    }

    // =========================================================================
    // 9. MEDIAN OF TWO SORTED ARRAYS  [H]  (Array / Binary Search)
    // Approach: Binary search the smaller array to find a partition where left/right
    // halves are balanced and maxLeft <= minRight. O(log min(m,n)).
    // =========================================================================
    static double findMedianSortedArrays(int[] a, int[] b) {
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

    // =========================================================================
    // 10. VALID PARENTHESES  [E]  (Stack)
    // Approach: Push open brackets; on close, pop and verify matching type. Empty
    // stack at end means valid. O(n) time, O(n) space.
    // =========================================================================
    static boolean isValid(String s) {
        Stack<Character> st = new Stack<>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '{' || c == '[') st.push(c);
            else {
                if (st.isEmpty()) return false;
                char t = st.pop();
                if ((c == ')' && t != '(') || (c == '}' && t != '{') || (c == ']' && t != '[')) return false;
            }
        }
        return st.isEmpty();
    }

    // =========================================================================
    // 11. DAILY TEMPERATURES  [M]  (Stack / Monotonic)
    // Approach: Monotonic-decreasing stack of indices. When a warmer day arrives, pop
    // and record the gap. O(n) time, O(n) space.
    // =========================================================================
    static int[] dailyTemperatures(int[] t) {
        int[] res = new int[t.length];
        Stack<Integer> st = new Stack<>();
        for (int i = 0; i < t.length; i++) {
            while (!st.isEmpty() && t[i] > t[st.peek()]) {
                int idx = st.pop();
                res[idx] = i - idx;
            }
            st.push(i);
        }
        return res;
    }

    // =========================================================================
    // 12. VALID PALINDROME  [E]  (Two Pointers / String)
    // Approach: Skip non-alphanumerics, compare lowercased ends. O(n) time, O(1) space.
    // =========================================================================
    static boolean isPalindrome(String s) {
        int l = 0, r = s.length() - 1;
        while (l < r) {
            while (l < r && !Character.isLetterOrDigit(s.charAt(l))) l++;
            while (l < r && !Character.isLetterOrDigit(s.charAt(r))) r--;
            if (Character.toLowerCase(s.charAt(l)) != Character.toLowerCase(s.charAt(r))) return false;
            l++; r--;
        }
        return true;
    }

    // =========================================================================
    // 13. STRING TO INTEGER (ATOI)  [M]  (String / Simulation)
    // Approach: Trim spaces, read optional sign, accumulate digits, clamp on 32-bit
    // overflow. O(n) time, O(1) space.
    // =========================================================================
    static int myAtoi(String s) {
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

    // =========================================================================
    // 14. MINIMUM WINDOW SUBSTRING  [H]  (String / Sliding Window)
    // Approach: Frequency map of target; expand r until all chars present, then shrink
    // l to minimize. O(|s|+|t|) time, O(1) space (fixed alphabet).
    // =========================================================================
    static String minWindow(String s, String t) {
        int[] need = new int[128];
        int req = 0;
        for (char c : t.toCharArray()) if (need[c]++ == 0) req++;
        int l = 0, r = 0, formed = 0, start = 0, len = Integer.MAX_VALUE;
        while (r < s.length()) {
            char c = s.charAt(r);
            need[c]--;
            if (need[c] == 0) formed++;
            while (formed == req) {
                if (r - l + 1 < len) { len = r - l + 1; start = l; }
                char cl = s.charAt(l);
                need[cl]++;
                if (need[cl] > 0) formed--;
                l++;
            }
            r++;
        }
        return len == Integer.MAX_VALUE ? "" : s.substring(start, start + len);
    }

    // =========================================================================
    // 15. REGULAR EXPRESSION MATCHING  [H]  (String / DP)
    // Approach: dp[i][j] = match of s[0..i] with p[0..j]; handle '.' and '*'
    // (zero or more of previous char). O(|s|*|p|) time/space.
    // =========================================================================
    static boolean isMatch(String s, String p) {
        boolean[][] dp = new boolean[s.length() + 1][p.length() + 1];
        dp[0][0] = true;
        for (int j = 1; j <= p.length(); j++) if (p.charAt(j - 1) == '*') dp[0][j] = dp[0][j - 2];
        for (int i = 1; i <= s.length(); i++)
            for (int j = 1; j <= p.length(); j++) {
                char pc = p.charAt(j - 1);
                if (pc == s.charAt(i - 1) || pc == '.') dp[i][j] = dp[i - 1][j - 1];
                else if (pc == '*') {
                    dp[i][j] = dp[i][j - 2];
                    if (p.charAt(j - 2) == s.charAt(i - 1) || p.charAt(j - 2) == '.') dp[i][j] |= dp[i - 1][j];
                }
            }
        return dp[s.length()][p.length()];
    }

    // =========================================================================
    // 16. DECODE WAYS  [M]  (String / DP)
    // Approach: dp[i] = ways to decode prefix i; add dp[i-1] for valid single digit
    // and dp[i-2] for valid two-digit (10..26). O(n) time, O(n) space (or O(1)).
    // =========================================================================
    static int numDecodings(String s) {
        int[] dp = new int[s.length() + 1];
        dp[0] = 1;
        dp[1] = s.charAt(0) == '0' ? 0 : 1;
        for (int i = 2; i <= s.length(); i++) {
            int one = s.charAt(i - 1) - '0';
            int two = Integer.parseInt(s.substring(i - 2, i));
            if (one >= 1) dp[i] += dp[i - 1];
            if (two >= 10 && two <= 26) dp[i] += dp[i - 2];
        }
        return dp[s.length()];
    }

    // =========================================================================
    // 17. LONGEST SUBSTRING WITHOUT REPEATING  [M]  (String / Sliding Window)
    // Approach: seen[] stores last index of each char; l jumps past any repeat. O(n).
    // =========================================================================
    static int lengthOfLongestSubstring(String s) {
        int[] seen = new int[128];
        int l = 0, max = 0;
        for (int r = 0; r < s.length(); r++) {
            l = Math.max(l, seen[s.charAt(r)]);
            max = Math.max(max, r - l + 1);
            seen[s.charAt(r)] = r + 1;
        }
        return max;
    }

    // =========================================================================
    // 18. COIN CHANGE  [M]  (DP / Unbounded Knapsack)
    // Approach: dp[i] = fewest coins for amount i; for each coin update
    // dp[i] = min(dp[i], dp[i-coin]+1). O(amount * coins) time, O(amount) space.
    // =========================================================================
    static int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        for (int c : coins) for (int i = c; i <= amount; i++) dp[i] = Math.min(dp[i], dp[i - c] + 1);
        return dp[amount] > amount ? -1 : dp[amount];
    }

    // =========================================================================
    // 19. EDIT DISTANCE  [H]  (DP / Strings)
    // Approach: dp[i][j] = edits to turn prefix i of a into prefix j of b; pick
    // insert/delete/replace minimum. O(|a|*|b|) time/space.
    // =========================================================================
    static int minDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= b.length(); j++) dp[0][j] = j;
        for (int i = 1; i <= a.length(); i++)
            for (int j = 1; j <= b.length(); j++)
                dp[i][j] = a.charAt(i - 1) == b.charAt(j - 1) ? dp[i - 1][j - 1]
                        : 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
        return dp[a.length()][b.length()];
    }

    // =========================================================================
    // 20. WORD BREAK  [M]  (DP / String)
    // Approach: dp[i] = can segment prefix of length i; for each j<i if dp[j] and
    // s[j..i] is a word, mark dp[i]. O(n^2 * w) time.
    // =========================================================================
    static boolean wordBreak(String s, List<String> wordDict) {
        Set<String> set = new HashSet<>(wordDict);
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        for (int i = 1; i <= s.length(); i++)
            for (int j = 0; j < i; j++)
                if (dp[j] && set.contains(s.substring(j, i))) { dp[i] = true; break; }
        return dp[s.length()];
    }

    // =========================================================================
    // 21. PERMUTATIONS  [M]  (Backtracking)
    // Approach: Backtrack picking each unused element, building the permutation and
    // undoing the choice on return. O(n!) time.
    // =========================================================================
    static List<List<Integer>> permute(int[] a) {
        List<List<Integer>> res = new ArrayList<>();
        backtrackPerm(a, new ArrayList<>(), new boolean[a.length], res);
        return res;
    }
    static void backtrackPerm(int[] a, List<Integer> t, boolean[] used, List<List<Integer>> res) {
        if (t.size() == a.length) { res.add(new ArrayList<>(t)); return; }
        for (int i = 0; i < a.length; i++) if (!used[i]) {
            used[i] = true; t.add(a[i]); backtrackPerm(a, t, used, res); t.remove(t.size() - 1); used[i] = false;
        }
    }

    // =========================================================================
    // 22. COMBINATION SUM  [M]  (Backtracking)
    // Approach: Backtrack allowing reuse of the same candidate (index not advanced)
    // until target hits 0 or goes negative. O(2^n) time.
    // =========================================================================
    static List<List<Integer>> combinationSum(int[] a, int t) {
        List<List<Integer>> res = new ArrayList<>();
        backtrackCombo(a, t, 0, new ArrayList<>(), res);
        return res;
    }
    static void backtrackCombo(int[] a, int rem, int s, List<Integer> t, List<List<Integer>> res) {
        if (rem == 0) { res.add(new ArrayList<>(t)); return; }
        if (rem < 0) return;
        for (int i = s; i < a.length; i++) { t.add(a[i]); backtrackCombo(a, rem - a[i], i, t, res); t.remove(t.size() - 1); }
    }

    // =========================================================================
    // 23. MAXIMUM PRODUCT SUBARRAY  [M]  (DP / Array)
    // Approach: Track both max and min running products (a negative flips them) and
    // keep the global max. O(n) time, O(1) space.
    // =========================================================================
    static int maxProduct(int[] a) {
        int max = a[0], min = a[0], res = a[0];
        for (int i = 1; i < a.length; i++) {
            if (a[i] < 0) { int t = max; max = min; min = t; }
            max = Math.max(a[i], max * a[i]);
            min = Math.min(a[i], min * a[i]);
            res = Math.max(res, max);
        }
        return res;
    }

    // =========================================================================
    // 24. CLIMBING STAIRS  [E]  (DP / Fibonacci)
    // Approach: ways[n] = ways[n-1] + ways[n-2]; iterate with two variables. O(n).
    // =========================================================================
    static int climbStairs(int n) {
        if (n <= 2) return n;
        int a = 1, b = 2;
        for (int i = 3; i <= n; i++) { int c = a + b; a = b; b = c; }
        return b;
    }

    // =========================================================================
    // 25. NUMBER OF ISLANDS  [M]  (Graph / DFS)
    // Approach: Scan grid; on each land cell run DFS to sink (mark '0') its whole
    // connected component, counting one island per start. O(m*n) time/space.
    // =========================================================================
    static int numIslands(char[][] g) {
        int c = 0;
        for (int i = 0; i < g.length; i++)
            for (int j = 0; j < g[0].length; j++)
                if (g[i][j] == '1') { c++; dfsIsland(g, i, j); }
        return c;
    }
    static void dfsIsland(char[][] g, int i, int j) {
        if (i < 0 || j < 0 || i >= g.length || j >= g[0].length || g[i][j] != '1') return;
        g[i][j] = '0';
        dfsIsland(g, i + 1, j); dfsIsland(g, i - 1, j); dfsIsland(g, i, j + 1); dfsIsland(g, i, j - 1);
    }

    // =========================================================================
    // 26. COURSE SCHEDULE  [M]  (Graph / Topological Sort)
    // Approach: Kahn's algorithm — BFS on in-degrees; if all nodes processed there's
    // no cycle. O(V+E) time, O(V+E) space.
    // =========================================================================
    static boolean canFinish(int n, int[][] p) {
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

    // =========================================================================
    // 27. WORD LADDER  [H]  (Graph / BFS)
    // Approach: BFS from begin to end word, generating one-char neighbors and removing
    // visited words from the set. O(N * 26 * L) time.
    // =========================================================================
    static int ladderLength(String b, String e, List<String> w) {
        Set<String> set = new HashSet<>(w);
        if (!set.contains(e)) return 0;
        Queue<String> q = new LinkedList<>();
        q.offer(b); set.remove(b);
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

    // =========================================================================
    // 28. LEVEL ORDER TRAVERSAL  [E]  (Tree / BFS)
    // Approach: BFS with a queue, draining one level (size-bounded) at a time. O(n).
    // =========================================================================
    static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        while (!q.isEmpty()) {
            int sz = q.size();
            List<Integer> lvl = new ArrayList<>();
            for (int i = 0; i < sz; i++) {
                TreeNode n = q.poll();
                lvl.add(n.val);
                if (n.left != null) q.offer(n.left);
                if (n.right != null) q.offer(n.right);
            }
            res.add(lvl);
        }
        return res;
    }

    // =========================================================================
    // 29. LOWEST COMMON ANCESTOR (BST)  [M]  (Tree)
    // Approach: In a BST, walk down: if both targets smaller go left, both larger go
    // right, otherwise current node is the LCA. O(h) time.
    // =========================================================================
    static TreeNode lca(TreeNode root, TreeNode p, TreeNode q) {
        while (root != null) {
            if (root.val < p.val && root.val < q.val) root = root.right;
            else if (root.val > p.val && root.val > q.val) root = root.left;
            else return root;
        }
        return null;
    }

    // =========================================================================
    // 30. MERGE INTERVALS  [M]  (Array / Sorting)
    // Approach: Sort by start; merge a running interval with the next when they
    // overlap, else start a new one. O(n log n) time.
    // =========================================================================
    static int[][] merge(int[][] iv) {
        Arrays.sort(iv, (a, b) -> a[0] - b[0]);
        List<int[]> res = new ArrayList<>();
        for (int[] i : iv) {
            if (res.isEmpty() || res.get(res.size() - 1)[1] < i[0]) res.add(i);
            else res.get(res.size() - 1)[1] = Math.max(res.get(res.size() - 1)[1], i[1]);
        }
        return res.toArray(new int[0][]);
    }

    // =========================================================================
    // 31. WORD SEARCH  [M]  (Backtracking / Matrix)
    // Approach: DFS from every cell; mark visited with a temp char and restore on
    // backtrack. O(m*n*4^L) time.
    // =========================================================================
    static boolean exist(char[][] b, String w) {
        for (int i = 0; i < b.length; i++)
            for (int j = 0; j < b[0].length; j++)
                if (dfsWord(b, w, i, j, 0)) return true;
        return false;
    }
    static boolean dfsWord(char[][] b, String w, int i, int j, int k) {
        if (k == w.length()) return true;
        if (i < 0 || j < 0 || i >= b.length || j >= b[0].length || b[i][j] != w.charAt(k)) return false;
        char c = b[i][j];
        b[i][j] = '#';
        boolean f = dfsWord(b, w, i + 1, j, k + 1) || dfsWord(b, w, i - 1, j, k + 1)
                || dfsWord(b, w, i, j + 1, k + 1) || dfsWord(b, w, i, j - 1, k + 1);
        b[i][j] = c;
        return f;
    }

    // =========================================================================
    // 32. LRU CACHE  [H]  (Design)
    // Approach: LinkedHashMap keeps insertion/access order; on get/put remove-then
    // reinsert to move key to most-recent end, evict oldest when over capacity. O(1).
    // =========================================================================
    static class LRUCache {
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

    // =========================================================================
    // 33. KADANE'S MAXIMUM SUBARRAY  [E]  (DP / Array)
    // Approach: Keep running sum, reset to current element when negative, track
    // global max. O(n) time, O(1) space.
    // =========================================================================
    static int maxSubArray(int[] a) {
        int max = a[0], cur = a[0];
        for (int i = 1; i < a.length; i++) {
            cur = Math.max(a[i], cur + a[i]);
            max = Math.max(max, cur);
        }
        return max;
    }

    // =========================================================================
    // 34. TOP K FREQUENT ELEMENTS  [M]  (Heap / Hash)
    // Approach: Count frequencies, then a min-heap of size k keeps only the k most
    // frequent. O(n log k) time.
    // =========================================================================
    static int[] topKFrequent(int[] a, int k) {
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

    // =========================================================================
    // 35. GROUP ANAGRAMS  [M]  (Hash / String)
    // Approach: Anagrams share a sorted-character key; bucket words by that key.
    // O(n * k log k) time.
    // =========================================================================
    static List<List<String>> groupAnagrams(String[] s) {
        Map<String, List<String>> m = new HashMap<>();
        for (String w : s) {
            char[] c = w.toCharArray();
            Arrays.sort(c);
            String k = new String(c);
            m.computeIfAbsent(k, x -> new ArrayList<>()).add(w);
        }
        return new ArrayList<>(m.values());
    }

    // =========================================================================
    // Helper: simple TreeNode for tree problems
    // =========================================================================
    static class TreeNode {
        int val; TreeNode left; TreeNode right;
        TreeNode(int v) { val = v; }
    }

    // =========================================================================
    // MAIN — sanity tests for every method
    // =========================================================================
    public static void main(String[] args) {
        check("Two Sum", Arrays.toString(twoSum(new int[]{2, 7, 11, 15}, 9)), "[0, 1]");
        check("3Sum", threeSum(new int[]{-1, 0, 1, 2, -1, -4}).size() > 0, true);
        check("Trapping Rain Water", trap(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}), 6);
        check("Largest Rectangle", largestRectangleArea(new int[]{2, 1, 5, 6, 2, 3}), 10);
        check("Container Water", maxArea(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7}), 49);
        check("Max Profit", maxProfit(new int[]{7, 1, 5, 3, 6, 4}), 5);
        check("Product Except Self", Arrays.toString(productExceptSelf(new int[]{1, 2, 3, 4})), "[24, 12, 8, 6]");
        check("Subarray Sum K", subarraySum(new int[]{1, 1, 1}, 2), 2);
        check("Median 2 Arrays", findMedianSortedArrays(new int[]{1, 3}, new int[]{2}), 2.0);
        check("Valid Parens", isValid("()[]{}"), true);
        check("Daily Temps", Arrays.toString(dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73})), "[1, 1, 4, 2, 1, 1, 0, 0]");
        check("Valid Palindrome", isPalindrome("A man, a plan, a canal: Panama"), true);
        check("Atoi", myAtoi("   -42abc"), -42);
        check("Min Window", minWindow("ADOBECODEBANC", "ABC"), "BANC");
        check("Regex Match", isMatch("aab", "c*a*b"), true);
        check("Decode Ways", numDecodings("12"), 2);
        check("Longest Substr", lengthOfLongestSubstring("abcabcbb"), 3);
        check("Coin Change", coinChange(new int[]{1, 2, 5}, 11), 3);
        check("Edit Distance", minDistance("horse", "ros"), 3);
        check("Word Break", wordBreak("leetcode", Arrays.asList("leet", "code")), true);
        check("Permutations", permute(new int[]{1, 2, 3}).size(), 6);
        check("Combination Sum", combinationSum(new int[]{2, 3, 6, 7}, 7).size() > 0, true);
        check("Max Product", maxProduct(new int[]{2, 3, -2, 4}), 6);
        check("Climbing Stairs", climbStairs(5), 8);
        check("Num Islands", numIslands(new char[][]{{'1','1','0'},{'0','1','0'},{'1','1','1'}}), 1);
        check("Course Schedule", canFinish(2, new int[][]{{1, 0}}), true);
        check("Word Ladder", ladderLength("hit", "cog", Arrays.asList("hot","dot","dog","lot","log","cog")), 5);
        check("Merge Intervals", Arrays.deepToString(merge(new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}})), "[[1, 6], [8, 10], [15, 18]]");
        check("Word Search", exist(new char[][]{{'A','B','C','E'},{'S','F','C','S'},{'A','D','E','E'}}, "ABCCED"), true);
        check("Kadane", maxSubArray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}), 6);
        check("Top K Frequent", topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2).length, 2);
        check("Group Anagrams", groupAnagrams(new String[]{"eat", "tea", "tan", "ate", "nat", "bat"}).size(), 3);

        // LRU Cache test
        LRUCache lru = new LRUCache(2);
        lru.put(1, 1); lru.put(2, 2);
        check("LRU get(1)", lru.get(1), 1);
        lru.put(3, 3);
        check("LRU get(2) evicted", lru.get(2), -1);

        System.out.println("\nAll Goldman Sachs DSA sanity checks executed.");
    }

    static void check(String name, Object actual, Object expected) {
        boolean pass = (actual == null && expected == null) || (actual != null && actual.equals(expected));
        System.out.println((pass ? "PASS" : "FAIL") + " - " + name + " => " + actual + (pass ? "" : " (expected " + expected + ")"));
    }
}