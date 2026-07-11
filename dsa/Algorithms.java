import java.util.*;

/** DSA — Algorithms: Binary Search, Two Ptr, Sliding Window, Prefix, DP, Backtracking (LeetCode).
 *  Run: javac Algorithms.java && java Algorithms */
public class Algorithms {

    // LC #33 Search in Rotated Sorted Array — O(log n)
    public static int searchRotated(int[] a, int t) {
        int l = 0, r = a.length - 1;
        while (l <= r) {
            int m = (l + r) / 2;
            if (a[m] == t) return m;
            if (a[l] <= a[m]) { if (t >= a[l] && t < a[m]) r = m - 1; else l = m + 1; }
            else { if (t > a[m] && t <= a[r]) l = m + 1; else r = m - 1; }
        }
        return -1;
    }

    // LC #875 Koko Eating Bananas (BS on answer) — O(n log max)
    public static int minEatingSpeed(int[] p, int h) {
        int lo = 1, hi = 0; for (int x : p) hi = Math.max(hi, x);
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (canEat(p, mid, h)) hi = mid; else lo = mid + 1;
        }
        return lo;
    }
    static boolean canEat(int[] p, int k, int h) { long t = 0; for (int x : p) t += (x + k - 1) / k; return t <= h; }

    // LC #11 Container With Most Water — two ptr O(n)
    public static int maxArea(int[] h) {
        int l = 0, r = h.length - 1, best = 0;
        while (l < r) { best = Math.max(best, Math.min(h[l], h[r]) * (r - l)); if (h[l] < h[r]) l++; else r--; }
        return best;
    }

    // LC #560 Subarray Sum Equals K — prefix sum O(n)
    public static int subarraySum(int[] a, int k) {
        Map<Integer, Integer> m = new HashMap<>(); m.put(0, 1); int ps = 0, c = 0;
        for (int x : a) { ps += x; c += m.getOrDefault(ps - k, 0); m.put(ps, m.getOrDefault(ps, 0) + 1); }
        return c;
    }

    // LC #70 Climbing Stairs — DP O(n)
    public static int climbStairs(int n) {
        if (n <= 2) return n; int a = 1, b = 2;
        for (int i = 3; i <= n; i++) { int t = a + b; a = b; b = t; }
        return b;
    }

    // LC #300 Longest Increasing Subsequence — patience O(n log n)
    public static int lengthLIS(int[] a) {
        List<Integer> t = new ArrayList<>();
        for (int x : a) { int i = Collections.binarySearch(t, x); if (i < 0) i = -(i + 1); if (i == t.size()) t.add(x); else t.set(i, x); }
        return t.size();
    }

    // LC #322 Coin Change — unbounded knapsack O(amount*coins)
    public static int coinChange(int[] c, int amount) {
        int[] dp = new int[amount + 1]; Arrays.fill(dp, amount + 1); dp[0] = 0;
        for (int a = 1; a <= amount; a++) for (int x : c) if (x <= a) dp[a] = Math.min(dp[a], dp[a - x] + 1);
        return dp[amount] > amount ? -1 : dp[amount];
    }

    // LC #55 Jump Game — greedy O(n)
    public static boolean canJump(int[] a) {
        int reach = 0;
        for (int i = 0; i < a.length; i++) { if (i > reach) return false; reach = Math.max(reach, i + a[i]); }
        return true;
    }

    // LC #62 Unique Paths — DP O(m*n)
    public static int uniquePaths(int m, int n) {
        int[] dp = new int[n]; Arrays.fill(dp, 1);
        for (int i = 1; i < m; i++) for (int j = 1; j < n; j++) dp[j] += dp[j - 1];
        return dp[n - 1];
    }

    // LC #5 Longest Palindromic Substring — center expand O(n^2)
    public static String longestPal(String s) {
        int st = 0, en = 0;
        for (int i = 0; i < s.length(); i++) {
            int a = exp(s, i, i), b = exp(s, i, i + 1), len = Math.max(a, b);
            if (len > en - st) { st = i - (len - 1) / 2; en = i + len / 2; }
        }
        return s.substring(st, en + 1);
    }
    static int exp(String s, int l, int r) { while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) { l--; r++; } return r - l - 1; }

    // LC #378 Kth Smallest in Sorted Matrix (BS on value) — O(n log range)
    public static int kthSmallest(int[][] m, int k) {
        int lo = m[0][0], hi = m[m.length - 1][m[0].length - 1];
        while (lo < hi) {
            int mid = (lo + hi) / 2; int cnt = 0;
            for (int[] row : m) cnt += upper(row, mid);
            if (cnt < k) lo = mid + 1; else hi = mid;
        }
        return lo;
    }
    static int upper(int[] row, int v) { int l = 0, r = row.length; while (l < r) { int m = (l + r) / 2; if (row[m] <= v) l = m + 1; else r = m; } return l; }

    // LC #152 Max Product Subarray
    public static int maxProduct(int[] a) {
        int max = a[0], min = a[0], best = a[0];
        for (int i = 1; i < a.length; i++) {
            if (a[i] < 0) { int t = max; max = min; min = t; }
            max = Math.max(a[i], max * a[i]); min = Math.min(a[i], min * a[i]);
            best = Math.max(best, max);
        }
        return best;
    }

    public static void main(String[] a) {
        System.out.println(searchRotated(new int[]{4,5,6,7,0,1,2}, 0)); // 4
        System.out.println(minEatingSpeed(new int[]{3,6,7,11}, 8)); // 4
        System.out.println(maxArea(new int[]{1,8,6,2,5,4}));     // 24
        System.out.println(subarraySum(new int[]{1,1,1}, 2));    // 2
        System.out.println(climbStairs(5));                 // 8
        System.out.println(coinChange(new int[]{1,2,5}, 11)); // 3
        System.out.println(canJump(new int[]{2,3,1,1,4}));  // true
    }
}