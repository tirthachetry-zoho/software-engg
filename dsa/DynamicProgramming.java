import java.util.*;

/** DSA — Dynamic Programming (LeetCode). Each method has inline explanation.
 *  Run: javac DynamicProgramming.java && java DynamicProgramming */
public class DynamicProgramming {

    // LC #70 Climbing Stairs — DP with O(n) time, O(1) space
    // Explanation: dp[i] = dp[i-1] + dp[i-2], can reach step i from i-1 or i-2
    public static int climbStairs(int n) {
        if (n <= 2) return n; // base cases: 1 step = 1 way, 2 steps = 2 ways
        int a = 1, b = 2; // a = dp[i-2], b = dp[i-1]
        for (int i = 3; i <= n; i++) { int t = a + b; a = b; b = t; } // dp[i] = dp[i-1] + dp[i-2]
        return b;
    }

    // LC #322 Coin Change — Unbounded knapsack, O(amount * coins) time
    // Explanation: dp[a] = min coins to make amount a, try each coin
    public static int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1]; // dp[i] = min coins for amount i
        Arrays.fill(dp, amount + 1); // initialize with max value (amount+1 is infinity)
        dp[0] = 0; // 0 coins needed for amount 0
        for (int a = 1; a <= amount; a++) // for each amount
            for (int c : coins) // try each coin
                if (c <= a) dp[a] = Math.min(dp[a], dp[a - c] + 1); // use coin c if possible
        return dp[amount] > amount ? -1 : dp[amount]; // if still max, impossible
    }

    // LC #300 Longest Increasing Subsequence — Patience sorting, O(n log n)
    // Explanation: Maintain piles where each pile stores increasing sequence
    public static int lengthOfLIS(int[] nums) {
        List<Integer> tails = new ArrayList<>(); // tails[i] = smallest tail of LIS length i+1
        for (int x : nums) {
            int i = Collections.binarySearch(tails, x); // find position to insert
            if (i < 0) i = -(i + 1); // if not found, get insertion point
            if (i == tails.size()) tails.add(x); // extend LIS
            else tails.set(i, x); // improve existing LIS
        }
        return tails.size();
    }

    // LC #1143 Longest Common Subsequence — O(m*n) time
    // Explanation: dp[i][j] = LCS of s1[0..i] and s2[0..j]
    public static int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length(), n = text2.length();
        int[][] dp = new int[m + 1][n + 1]; // dp[i][j] = LCS length for first i chars of text1 and j chars of text2
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) // chars match
                    dp[i][j] = dp[i - 1][j - 1] + 1; // extend LCS
                else dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]); // take max from skipping either char
            }
        }
        return dp[m][n];
    }

    // LC #518 Coin Change 2 — Number of ways to make amount, O(amount * coins)
    // Explanation: dp[a] = number of ways to make amount a
    public static int change(int amount, int[] coins) {
        int[] dp = new int[amount + 1]; // dp[i] = ways to make amount i
        dp[0] = 1; // 1 way to make amount 0 (use no coins)
        for (int c : coins) // for each coin (order matters to avoid permutations)
            for (int a = c; a <= amount; a++) // for each amount from coin value
                dp[a] += dp[a - c]; // add ways without this coin
        return dp[amount];
    }

    // LC #494 Target Sum — Subset sum with +/-, O(n * sum) time
    // Explanation: Find subset with sum (target + total) / 2
    public static int findTargetSumWays(int[] nums, int target) {
        int sum = 0; for (int x : nums) sum += x;
        if (Math.abs(target) > sum || (sum + target) % 2 != 0) return 0; // impossible
        int t = (sum + target) / 2; // target subset sum
        int[] dp = new int[t + 1]; dp[0] = 1; // dp[i] = ways to make sum i
        for (int x : nums) for (int s = t; s >= x; s--) dp[s] += dp[s - x]; // 0/1 knapsack
        return dp[t];
    }

    // LC #72 Edit Distance — O(m*n) time
    // Explanation: dp[i][j] = min operations to convert word1[0..i] to word2[0..j]
    public static int minDistance(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        int[][] dp = new int[m + 1][n + 1]; // dp[i][j] = edit distance for first i chars of word1 and j chars of word2
        for (int i = 0; i <= m; i++) dp[i][0] = i; // delete all chars from word1
        for (int j = 0; j <= n; j++) dp[0][j] = j; // insert all chars into word1
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) // chars match
                    dp[i][j] = dp[i - 1][j - 1]; // no operation needed
                else dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], // replace
                    Math.min(dp[i - 1][j], dp[i][j - 1])); // delete or insert
            }
        }
        return dp[m][n];
    }

    // LC #64 Minimum Path Sum — Grid DP, O(m*n) time
    // Explanation: dp[i][j] = min sum to reach cell (i,j) from top-left
    public static int minPathSum(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        for (int i = 1; i < m; i++) grid[i][0] += grid[i - 1][0]; // first column
        for (int j = 1; j < n; j++) grid[0][j] += grid[0][j - 1]; // first row
        for (int i = 1; i < m; i++) // for remaining cells
            for (int j = 1; j < n; j++)
                grid[i][j] += Math.min(grid[i - 1][j], grid[i][j - 1]); // take min from top or left
        return grid[m - 1][n - 1];
    }

    // LC #198 House Robber — Cannot rob adjacent houses, O(n) time
    // Explanation: dp[i] = max money from houses 0..i, either rob i or skip i
    public static int rob(int[] nums) {
        int prev = 0, prev2 = 0; // prev = dp[i-1], prev2 = dp[i-2]
        for (int x : nums) {
            int curr = Math.max(prev, prev2 + x); // either skip current or rob it
            prev2 = prev; prev = curr; // shift window
        }
        return prev;
    }

    // LC #213 House Robber II — Circular houses, O(n) time
    // Explanation: Rob houses 0..n-2 or 1..n-1, take max
    public static int robII(int[] nums) {
        if (nums.length == 1) return nums[0];
        return Math.max(robLinear(nums, 0, nums.length - 2), robLinear(nums, 1, nums.length - 1));
    }
    static int robLinear(int[] nums, int start, int end) {
        int prev = 0, prev2 = 0;
        for (int i = start; i <= end; i++) {
            int curr = Math.max(prev, prev2 + nums[i]);
            prev2 = prev; prev = curr;
        }
        return prev;
    }

    // LC #416 Partition Equal Subset Sum — 0/1 knapsack, O(n*sum) time
    // Explanation: Find subset with sum = total/2
    public static boolean canPartition(int[] nums) {
        int sum = 0; for (int x : nums) sum += x;
        if (sum % 2 != 0) return false; // odd sum cannot be partitioned
        int target = sum / 2;
        boolean[] dp = new boolean[target + 1]; dp[0] = true; // dp[i] = can we make sum i
        for (int x : nums) for (int s = target; s >= x; s--) dp[s] |= dp[s - x]; // try including each num
        return dp[target];
    }

    // LC #139 Word Break — Dictionary word segmentation, O(n^2) time
    // Explanation: dp[i] = true if s[0..i-1] can be segmented
    public static boolean wordBreak(String s, List<String> wordDict) {
        Set<String> dict = new HashSet<>(wordDict);
        int n = s.length();
        boolean[] dp = new boolean[n + 1]; dp[0] = true; // dp[i] = can segment first i chars
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && dict.contains(s.substring(j, i))) { // if prefix j can be segmented and suffix is word
                    dp[i] = true; break;
                }
            }
        }
        return dp[n];
    }

    // LC #140 Word Break II — Return all possible sentences, O(2^n) time
    // Explanation: Backtrack with memoization
    public static List<String> wordBreakII(String s, List<String> wordDict) {
        return dfs(s, new HashSet<>(wordDict), new HashMap<>());
    }
    static List<String> dfs(String s, Set<String> dict, Map<String, List<String>> memo) {
        if (memo.containsKey(s)) return memo.get(s);
        List<String> res = new ArrayList<>();
        if (s.isEmpty()) { res.add(""); return res; }
        for (int i = 1; i <= s.length(); i++) {
            String prefix = s.substring(0, i);
            if (dict.contains(prefix)) {
                for (String suffix : dfs(s.substring(i), dict, memo)) {
                    res.add(prefix + (suffix.isEmpty() ? "" : " " + suffix));
                }
            }
        }
        memo.put(s, res);
        return res;
    }

    // LC #91 Decode Ways — Number of ways to decode string, O(n) time
    // Explanation: dp[i] = ways to decode first i characters
    public static int numDecodings(String s) {
        int n = s.length();
        int[] dp = new int[n + 1]; dp[0] = 1; dp[1] = s.charAt(0) == '0' ? 0 : 1;
        for (int i = 2; i <= n; i++) {
            int one = Integer.parseInt(s.substring(i - 1, i)); // single digit
            int two = Integer.parseInt(s.substring(i - 2, i)); // two digits
            if (one >= 1 && one <= 9) dp[i] += dp[i - 1]; // valid single digit
            if (two >= 10 && two <= 26) dp[i] += dp[i - 2]; // valid two digits
        }
        return dp[n];
    }

    // LC #62 Unique Paths — Grid paths from top-left to bottom-right, O(m*n) time
    // Explanation: dp[i][j] = paths to reach (i,j) = dp[i-1][j] + dp[i][j-1]
    public static int uniquePaths(int m, int n) {
        int[] dp = new int[n]; Arrays.fill(dp, 1); // first row: only 1 way to reach each cell
        for (int i = 1; i < m; i++) // for remaining rows
            for (int j = 1; j < n; j++)
                dp[j] += dp[j - 1]; // paths from top + paths from left
        return dp[n - 1];
    }

    // LC #63 Unique Paths II — Grid with obstacles, O(m*n) time
    // Explanation: dp[i][j] = 0 if obstacle, else dp[i-1][j] + dp[i][j-1]
    public static int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length, n = obstacleGrid[0].length;
        int[] dp = new int[n]; dp[0] = obstacleGrid[0][0] == 0 ? 1 : 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (obstacleGrid[i][j] == 1) dp[j] = 0; // obstacle, no path
                else if (j > 0) dp[j] += dp[j - 1]; // add paths from left
            }
        }
        return dp[n - 1];
    }

    // LC #221 Maximal Square — Largest square of 1s in binary matrix, O(m*n) time
    // Explanation: dp[i][j] = side length of largest square ending at (i,j)
    public static int maximalSquare(char[][] matrix) {
        int m = matrix.length, n = matrix[0].length, max = 0;
        int[][] dp = new int[m + 1][n + 1]; // dp[i][j] = max square side ending at (i-1,j-1)
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (matrix[i - 1][j - 1] == '1') {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j], Math.min(dp[i][j - 1], dp[i - 1][j - 1]));
                    max = Math.max(max, dp[i][j]);
                }
            }
        }
        return max * max;
    }

    // LC #309 Best Time to Buy and Sell Stock with Cooldown, O(n) time
    // Explanation: hold[i] = max profit with stock at day i, cash[i] = max profit without stock
    public static int maxProfitCooldown(int[] prices) {
        int n = prices.length;
        if (n < 2) return 0;
        int[] hold = new int[n], cash = new int[n]; // hold = have stock, cash = no stock
        hold[0] = -prices[0]; hold[1] = Math.max(-prices[0], -prices[1]);
        cash[0] = 0; cash[1] = Math.max(0, prices[1] - prices[0]);
        for (int i = 2; i < n; i++) {
            hold[i] = Math.max(hold[i - 1], cash[i - 2] - prices[i]); // keep or buy (after cooldown)
            cash[i] = Math.max(cash[i - 1], hold[i - 1] + prices[i]); // keep or sell
        }
        return cash[n - 1];
    }

    // LC #377 Combination Sum IV — Number of combinations that sum to target, O(target * n) time
    // Explanation: dp[t] = sum of dp[t - nums[i]] for all valid nums[i]
    public static int combinationSum4(int[] nums, int target) {
        int[] dp = new int[target + 1]; dp[0] = 1; // dp[i] = number of ways to make sum i
        for (int t = 1; t <= target; t++) {
            for (int x : nums) if (x <= t) dp[t] += dp[t - x]; // try each number
        }
        return dp[target];
    }

    // LC #10 Regular Expression Matching — '.' matches any char, '*' matches zero or more, O(m*n) time
    // Explanation: dp[i][j] = true if s[0..i-1] matches p[0..j-1]
    public static boolean isMatch(String s, String p) {
        int m = s.length(), n = p.length();
        boolean[][] dp = new boolean[m + 1][n + 1]; dp[0][0] = true;
        for (int j = 2; j <= n; j++) if (p.charAt(j - 1) == '*') dp[0][j] = dp[0][j - 2]; // handle '*' for empty string
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (p.charAt(j - 1) == '.' || p.charAt(j - 1) == s.charAt(i - 1)) dp[i][j] = dp[i - 1][j - 1]; // direct match
                else if (p.charAt(j - 1) == '*') {
                    dp[i][j] = dp[i][j - 2]; // zero occurrences
                    if (p.charAt(j - 2) == '.' || p.charAt(j - 2) == s.charAt(i - 1)) dp[i][j] |= dp[i - 1][j]; // one or more
                }
            }
        }
        return dp[m][n];
    }

    public static void main(String[] a) {
        System.out.println(climbStairs(5)); // 8
        System.out.println(coinChange(new int[]{1,2,5}, 11)); // 3
        System.out.println(lengthOfLIS(new int[]{10,9,2,5,3,7,101,18})); // 4
        System.out.println(longestCommonSubsequence("abcde", "ace")); // 3
        System.out.println(change(5, new int[]{1,2,5})); // 4
        System.out.println(findTargetSumWays(new int[]{1,1,1,1,1}, 3)); // 5
        System.out.println(minDistance("horse", "ros")); // 3
        System.out.println(rob(new int[]{2,7,9,3,1})); // 12
        System.out.println(robII(new int[]{2,3,2})); // 3
        System.out.println(canPartition(new int[]{1,5,11,5})); // true
        System.out.println(wordBreak("leetcode", Arrays.asList("leet","code"))); // true
        System.out.println(numDecodings("12")); // 2
        System.out.println(uniquePaths(3, 7)); // 28
        System.out.println(maximalSquare(new char[][]{{'1','0','1','0','0'},{'1','0','1','1','1'},{'1','1','1','1','1'},{'1','0','0','1','0'}})); // 4
        System.out.println(combinationSum4(new int[]{1,2,3}, 4)); // 7
        System.out.println(isMatch("aa", "a*")); // true
    }
}
