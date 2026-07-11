import java.util.*;

/**
 * DSA — Arrays (LeetCode). Each method is a self-contained solution.
 * Run: javac Arrays.java && java Arrays
 */
public class ArrayQ {

    // LC #1 Two Sum — hashmap, O(n)
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> m = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (m.containsKey(target - nums[i])) return new int[]{m.get(target - nums[i]), i};
            m.put(nums[i], i);
        }
        return new int[]{};
    }

    // LC #121 Best Time to Buy and Sell Stock — O(n)
    public static int maxProfit(int[] p) {
        int min = Integer.MAX_VALUE, max = 0;
        for (int x : p) { min = Math.min(min, x); max = Math.max(max, x - min); }
        return max;
    }

    // LC #53 Maximum Subarray (Kadane) — O(n)
    public static int maxSubArray(int[] a) {
        int best = a[0], cur = a[0];
        for (int i = 1; i < a.length; i++) { cur = Math.max(a[i], cur + a[i]); best = Math.max(best, cur); }
        return best;
    }

    // LC #238 Product of Array Except Self — O(n), no division
    public static int[] productExceptSelf(int[] a) {
        int n = a.length, res[] = new int[n];
        res[0] = 1;
        for (int i = 1; i < n; i++) res[i] = res[i - 1] * a[i - 1];
        int r = 1;
        for (int i = n - 1; i >= 0; i--) { res[i] *= r; r *= a[i]; }
        return res;
    }

    // LC #56 Merge Intervals — O(n log n)
    public static int[][] merge(int[][] iv) {
        java.util.Arrays.sort(iv, (x, y) -> Integer.compare(x[0], y[0]));
        List<int[]> out = new ArrayList<>();
        for (int[] it : iv) {
            if (out.isEmpty() || out.get(out.size() - 1)[1] < it[0]) out.add(it);
            else out.get(out.size() - 1)[1] = Math.max(out.get(out.size() - 1)[1], it[1]);
        }
        return out.toArray(new int[0][]);
    }

    // LC #15 3Sum — O(n^2)
    public static List<List<Integer>> threeSum(int[] a) {
        java.util.Arrays.sort(a); List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < a.length - 2; i++) {
            if (i > 0 && a[i] == a[i - 1]) continue;
            int l = i + 1, r = a.length - 1;
            while (l < r) {
                int s = a[i] + a[l] + a[r];
                if (s == 0) { res.add(List.of(a[i], a[l], a[r])); while (l < r && a[l] == a[l + 1]) l++; while (l < r && a[r] == a[r - 1]) r--; l++; r--; }
                else if (s < 0) l++; else r--;
            }
        }
        return res;
    }

    // LC #189 Rotate Array — O(n) reverse trick
    public static void rotate(int[] a, int k) {
        int n = a.length; k %= n;
        reverse(a, 0, n - 1); reverse(a, 0, k - 1); reverse(a, k, n - 1);
    }
    static void reverse(int[] a, int i, int j) { while (i < j) { int t = a[i]; a[i++] = a[j]; a[j--] = t; } }

    // LC #169 Majority Element (Boyer-Moore) — O(n)
    public static int majorityElement(int[] a) {
        int c = a[0], cnt = 0;
        for (int x : a) { if (cnt == 0) c = x; cnt += (x == c) ? 1 : -1; }
        return c;
    }

    // LC #48 Rotate Image (matrix 90 deg) — O(n^2)
    public static void rotateMatrix(int[][] m) {
        int n = m.length;
        for (int i = 0; i < n; i++) for (int j = i; j < n; j++) { int t = m[i][j]; m[i][j] = m[j][i]; m[j][i] = t; }
        for (int i = 0; i < n; i++) for (int j = 0; j < n / 2; j++) { int t = m[i][j]; m[i][j] = m[i][n - 1 - j]; m[i][n - 1 - j] = t; }
    }

    // LC #75 Sort Colors (Dutch National Flag) — O(n)
    public static void sortColors(int[] a) {
        int lo = 0, hi = a.length - 1, mid = 0;
        while (mid <= hi) {
            if (a[mid] == 0) { swap(a, lo++, mid++); }
            else if (a[mid] == 2) { swap(a, mid, hi--); }
            else mid++;
        }
    }
    static void swap(int[] a, int i, int j) { int t = a[i]; a[i] = a[j]; a[j] = t; }

    // LC #283 Move Zeroes — Move all zeros to end, O(n) time
    // Explanation: Two pointers, one for current position, one for next non-zero
    public static void moveZeroes(int[] nums) {
        int insertPos = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) { nums[insertPos++] = nums[i]; }
        }
        while (insertPos < nums.length) nums[insertPos++] = 0;
    }

    // LC #41 First Missing Positive — Find smallest missing positive, O(n) time
    // Explanation: Place each number at its correct index, then find first mismatch
    public static int firstMissingPositive(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            while (nums[i] > 0 && nums[i] <= n && nums[nums[i] - 1] != nums[i]) {
                swap(nums, i, nums[i] - 1);
            }
        }
        for (int i = 0; i < n; i++) if (nums[i] != i + 1) return i + 1;
        return n + 1;
    }

    // LC #42 Trapping Rain Water — Calculate trapped water, O(n) time
    // Explanation: Two pointers, track max from left and right
    public static int trap(int[] height) {
        int left = 0, right = height.length - 1, leftMax = 0, rightMax = 0, water = 0;
        while (left < right) {
            if (height[left] < height[right]) {
                if (height[left] >= leftMax) leftMax = height[left];
                else water += leftMax - height[left];
                left++;
            } else {
                if (height[right] >= rightMax) rightMax = height[right];
                else water += rightMax - height[right];
                right--;
            }
        }
        return water;
    }

    // LC #11 Container With Most Water — Max area container, O(n) time
    // Explanation: Two pointers, move pointer with smaller height
    public static int maxArea(int[] height) {
        int left = 0, right = height.length - 1, maxArea = 0;
        while (left < right) {
            maxArea = Math.max(maxArea, Math.min(height[left], height[right]) * (right - left));
            if (height[left] < height[right]) left++; else right--;
        }
        return maxArea;
    }

    // LC #31 Next Permutation — Find next lexicographic permutation, O(n) time
    // Explanation: Find first decreasing element from right, swap with next greater, reverse suffix
    public static void nextPermutation(int[] nums) {
        int i = nums.length - 2;
        while (i >= 0 && nums[i] >= nums[i + 1]) i--;
        if (i >= 0) {
            int j = nums.length - 1;
            while (nums[j] <= nums[i]) j--;
            swap(nums, i, j);
        }
        reverse(nums, i + 1, nums.length - 1);
    }
    static void reverse(int[] nums, int start, int end) {
        while (start < end) { swap(nums, start++, end--); }
    }

    // LC #55 Jump Game — Can reach last index, O(n) time
    // Explanation: Track farthest reachable index
    public static boolean canJump(int[] nums) {
        int farthest = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i > farthest) return false;
            farthest = Math.max(farthest, i + nums[i]);
        }
        return true;
    }

    // LC #45 Jump Game II — Minimum jumps to reach end, O(n) time
    // Explanation: Greedy BFS-like approach
    public static int jump(int[] nums) {
        int jumps = 0, curEnd = 0, farthest = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            farthest = Math.max(farthest, i + nums[i]);
            if (i == curEnd) { jumps++; curEnd = farthest; }
        }
        return jumps;
    }

    // LC #134 Gas Station — Find starting station, O(n) time
    // Explanation: If total gas >= total cost, solution exists
    public static int canCompleteCircuit(int[] gas, int[] cost) {
        int total = 0, tank = 0, start = 0;
        for (int i = 0; i < gas.length; i++) {
            total += gas[i] - cost[i]; tank += gas[i] - cost[i];
            if (tank < 0) { start = i + 1; tank = 0; }
        }
        return total >= 0 ? start : -1;
    }

    // LC #128 Longest Consecutive Sequence — O(n) time
    // Explanation: Use HashSet, only start sequence from smallest element
    public static int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>(); for (int n : nums) set.add(n);
        int longest = 0;
        for (int n : set) {
            if (!set.contains(n - 1)) {
                int current = n, streak = 1;
                while (set.contains(current + 1)) { current++; streak++; }
                longest = Math.max(longest, streak);
            }
        }
        return longest;
    }

    public static void main(String[] args) {
        System.out.println(java.util.Arrays.toString(twoSum(new int[]{2,7,11,15}, 9)));      // [0,1]
        System.out.println(maxProfit(new int[]{7,1,5,3,6,4}));               // 5
        System.out.println(maxSubArray(new int[]{-2,1,-3,4,-1,2,1,-5,4}));    // 6
        System.out.println(java.util.Arrays.toString(productExceptSelf(new int[]{1,2,3,4}))); // [24,12,8,6]
        System.out.println(majorityElement(new int[]{2,2,1,1,1,2,2}));        // 2
        int[] zeros = new int[]{0,1,0,3,12}; moveZeroes(zeros); System.out.println(java.util.Arrays.toString(zeros)); // [1,3,12,0,0]
        System.out.println(firstMissingPositive(new int[]{3,4,-1,1}));       // 2
        System.out.println(trap(new int[]{0,1,0,2,1,0,1,3,2,1,2,1}));       // 6
        System.out.println(maxArea(new int[]{1,8,6,2,5,4,8,3,7}));          // 49
        int[] perm = new int[]{1,2,3}; nextPermutation(perm); System.out.println(java.util.Arrays.toString(perm)); // [1,3,2]
        System.out.println(canJump(new int[]{2,3,1,1,4}));                   // true
        System.out.println(jump(new int[]{2,3,1,1,4}));                      // 2
        System.out.println(canCompleteCircuit(new int[]{1,2,3,4,5}, new int[]{3,4,5,1,2})); // 3
        System.out.println(longestConsecutive(new int[]{100,4,200,1,3,2}));  // 4
    }
}