import java.util.*;

/** DSA — Greedy Algorithms (LeetCode). Each method has inline explanation.
 *  Run: javac Greedy.java && java Greedy */
public class Greedy {

    // LC #55 Jump Game — Can reach last index, O(n) time
    // Explanation: Track farthest reachable index, if current index > farthest, cannot proceed
    public static boolean canJump(int[] nums) {
        int farthest = 0; // farthest index reachable so far
        for (int i = 0; i < nums.length; i++) {
            if (i > farthest) return false; // cannot reach current index
            farthest = Math.max(farthest, i + nums[i]); // update farthest reachable
        }
        return true;
    }

    // LC #45 Jump Game II — Minimum jumps to reach end, O(n) time
    // Explanation: Greedy BFS-like approach, jump when reaching current boundary
    public static int jump(int[] nums) {
        int jumps = 0, curEnd = 0, farthest = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            farthest = Math.max(farthest, i + nums[i]); // track farthest reachable
            if (i == curEnd) { // reached boundary of current jump
                jumps++; curEnd = farthest; // make another jump
            }
        }
        return jumps;
    }

    // LC #121 Best Time to Buy and Sell Stock — Single transaction, O(n) time
    // Explanation: Track min price seen so far, max profit = max(price - min)
    public static int maxProfit(int[] prices) {
        int minPrice = Integer.MAX_VALUE, maxProfit = 0;
        for (int price : prices) {
            minPrice = Math.min(minPrice, price); // update min price
            maxProfit = Math.max(maxProfit, price - minPrice); // update max profit
        }
        return maxProfit;
    }

    // LC #122 Best Time to Buy and Sell Stock II — Multiple transactions, O(n) time
    // Explanation: Buy and sell whenever price increases (capture all positive differences)
    public static int maxProfitII(int[] prices) {
        int profit = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1]) profit += prices[i] - prices[i - 1]; // capture every increase
        }
        return profit;
    }

    // LC #134 Gas Station — Find starting station to complete circuit, O(n) time
    // Explanation: If total gas >= total cost, solution exists. Start where tank never goes negative
    public static int canCompleteCircuit(int[] gas, int[] cost) {
        int total = 0, tank = 0, start = 0;
        for (int i = 0; i < gas.length; i++) {
            total += gas[i] - cost[i]; // total surplus
            tank += gas[i] - cost[i]; // current tank
            if (tank < 0) { start = i + 1; tank = 0; } // restart from next station
        }
        return total >= 0 ? start : -1;
    }

    // LC #135 Candy — Minimum candies with rating constraints, O(n) time
    // Explanation: Two passes: left-to-right for increasing, right-to-left for decreasing
    public static int candy(int[] ratings) {
        int n = ratings.length, candies[] = new int[n];
        Arrays.fill(candies, 1); // everyone gets at least 1 candy
        for (int i = 1; i < n; i++) // left pass: handle increasing ratings
            if (ratings[i] > ratings[i - 1]) candies[i] = candies[i - 1] + 1;
        for (int i = n - 2; i >= 0; i--) // right pass: handle decreasing ratings
            if (ratings[i] > ratings[i + 1]) candies[i] = Math.max(candies[i], candies[i + 1] + 1);
        int sum = 0; for (int c : candies) sum += c;
        return sum;
    }

    // LC #406 Queue Reconstruction by Height — Reconstruct queue, O(n^2) time
    // Explanation: Sort by height descending, then insert at position k
    public static int[][] reconstructQueue(int[][] people) {
        Arrays.sort(people, (a, b) -> a[0] == b[0] ? a[1] - b[1] : b[0] - a[0]); // sort by height desc, k asc
        List<int[]> res = new ArrayList<>();
        for (int[] p : people) res.add(p[1], p); // insert at position k
        return res.toArray(new int[0][]);
    }

    // LC #621 Task Scheduler — Minimum intervals with cooldown, O(n) time
    // Explanation: Schedule most frequent tasks first, use formula
    public static int leastInterval(char[] tasks, int n) {
        int[] freq = new int[26]; int max = 0, maxCount = 0;
        for (char t : tasks) { freq[t - 'A']++; max = Math.max(max, freq[t - 'A']); }
        for (int f : freq) if (f == max) maxCount++; // count tasks with max frequency
        int partCount = max - 1; // number of full parts
        int partLength = n - (maxCount - 1); // length of each part
        int emptySlots = partCount * partLength; // total empty slots
        int availableTasks = tasks.length - max * maxCount; // tasks that can fill empty slots
        int idles = Math.max(0, emptySlots - availableTasks); // remaining idle slots
        return tasks.length + idles;
    }

    // LC #763 Partition Labels — Partition string into as many parts as possible, O(n) time
    // Explanation: Track last occurrence of each char, extend partition when reaching last index
    public static List<Integer> partitionLabels(String s) {
        int[] last = new int[26];
        for (int i = 0; i < s.length(); i++) last[s.charAt(i) - 'a'] = i; // last occurrence
        List<Integer> res = new ArrayList<>();
        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
            end = Math.max(end, last[s.charAt(i) - 'a']); // extend partition
            if (i == end) { res.add(end - start + 1); start = i + 1; } // partition complete
        }
        return res;
    }

    // LC #53 Maximum Subarray (Kadane) — O(n) time
    // Explanation: Keep track of current subarray sum, reset when negative
    public static int maxSubArray(int[] nums) {
        int maxSum = nums[0], curSum = nums[0];
        for (int i = 1; i < nums.length; i++) {
            curSum = Math.max(nums[i], curSum + nums[i]); // either start new or extend
            maxSum = Math.max(maxSum, curSum); // update global max
        }
        return maxSum;
    }

    // LC #56 Merge Intervals — O(n log n) time
    // Explanation: Sort by start time, merge overlapping intervals
    public static int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0])); // sort by start
        List<int[]> res = new ArrayList<>();
        for (int[] iv : intervals) {
            if (res.isEmpty() || res.get(res.size() - 1)[1] < iv[0]) res.add(iv); // no overlap
            else res.get(res.size() - 1)[1] = Math.max(res.get(res.size() - 1)[1], iv[1]); // merge
        }
        return res.toArray(new int[0][]);
    }

    // LC #435 Non-overlapping Intervals — Minimum removals, O(n log n) time
    // Explanation: Sort by end time, remove overlapping intervals
    public static int eraseOverlapIntervals(int[][] intervals) {
        if (intervals.length == 0) return 0;
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1])); // sort by end
        int count = 1, end = intervals[0][1];
        for (int[] iv : intervals) {
            if (iv[0] >= end) { count++; end = iv[1]; } // non-overlapping
        }
        return intervals.length - count;
    }

    // LC #452 Minimum Arrows to Burst Balloons — O(n log n) time
    // Explanation: Sort by end, arrow at end of first balloon bursts all overlapping
    public static int findMinArrowShots(int[][] points) {
        if (points.length == 0) return 0;
        Arrays.sort(points, (a, b) -> Integer.compare(a[1], b[1])); // sort by end
        int arrows = 1, end = points[0][1];
        for (int[] p : points) {
            if (p[0] > end) { arrows++; end = p[1]; } // need new arrow
        }
        return arrows;
    }

    // LC #1029 Two City Scheduling — Min cost to send 2n people to 2 cities, O(n log n) time
    // Explanation: Sort by difference (costA - costB), first n go to A, rest to B
    public static int twoCitySchedCost(int[][] costs) {
        Arrays.sort(costs, (a, b) -> (a[0] - a[1]) - (b[0] - b[1])); // sort by savings
        int minCost = 0, n = costs.length / 2;
        for (int i = 0; i < n; i++) minCost += costs[i][0] + costs[i + n][1]; // first n to A, rest to B
        return minCost;
    }

    // LC #870 Advantage Shuffle — Maximize advantage, O(n log n) time
    // Explanation: Two-pointer greedy, match smallest possible winning card
    public static int[] advantageCount(int[] A, int[] B) {
        int n = A.length;
        Arrays.sort(A); Arrays.sort(B);
        int[] res = new int[n];
        int[] sortedB = Arrays.copyOf(B, n);
        Map<Integer, Deque<Integer>> map = new HashMap<>(); // map value to indices
        for (int i = 0; i < n; i++) map.computeIfAbsent(B[i], k -> new ArrayDeque<>()).add(i);
        int i = 0, j = 0, k = n - 1;
        while (i < n) {
            if (A[i] > sortedB[j]) { // A[i] can beat B[j]
                int idx = map.get(sortedB[j]).poll(); res[idx] = A[i]; i++; j++;
            } else { // A[i] cannot beat any, sacrifice against largest B
                int idx = map.get(sortedB[k]).poll(); res[idx] = A[i]; i++; k--;
            }
        }
        return res;
    }

    // LC #334 Increasing Triplet Subsequence — O(n) time
    // Explanation: Track smallest and second smallest, find if third exists
    public static boolean increasingTriplet(int[] nums) {
        int first = Integer.MAX_VALUE, second = Integer.MAX_VALUE;
        for (int n : nums) {
            if (n <= first) first = n; // update smallest
            else if (n <= second) second = n; // update second smallest
            else return true; // found third
        }
        return false;
    }

    // LC #605 Can Place Flowers — O(n) time
    // Explanation: Check each spot, can plant if adjacent spots are empty
    public static boolean canPlaceFlowers(int[] flowerbed, int n) {
        int count = 0;
        for (int i = 0; i < flowerbed.length; i++) {
            if (flowerbed[i] == 0 && (i == 0 || flowerbed[i - 1] == 0) && (i == flowerbed.length - 1 || flowerbed[i + 1] == 0)) {
                flowerbed[i] = 1; count++;
                if (count >= n) return true;
            }
        }
        return count >= n;
    }

    // LC #392 Is Subsequence — O(n) time
    // Explanation: Two pointers, match characters in order
    public static boolean isSubsequence(String s, String t) {
        int i = 0, j = 0;
        while (i < s.length() && j < t.length()) {
            if (s.charAt(i) == t.charAt(j)) i++; // match found
            j++;
        }
        return i == s.length();
    }

    // LC #846 Hand of Straights — O(n log n) time
    // Explanation: Count cards, greedily form groups starting from smallest
    public static boolean isNStraightHand(int[] hand, int groupSize) {
        if (hand.length % groupSize != 0) return false;
        TreeMap<Integer, Integer> count = new TreeMap<>();
        for (int h : hand) count.put(h, count.getOrDefault(h, 0) + 1);
        while (!count.isEmpty()) {
            int first = count.firstKey();
            for (int i = 0; i < groupSize; i++) {
                int curr = first + i;
                if (!count.containsKey(curr)) return false;
                int c = count.get(curr);
                if (c == 1) count.remove(curr);
                else count.put(curr, c - 1);
            }
        }
        return true;
    }

    // LC #659 Split Array into Consecutive Subsequences — O(n) time
    // Explanation: Greedy with hashmap, try to extend existing sequences first
    public static boolean isPossible(int[] nums) {
        Map<Integer, Integer> freq = new HashMap<>(), append = new HashMap<>();
        for (int n : nums) freq.put(n, freq.getOrDefault(n, 0) + 1);
        for (int n : nums) {
            if (freq.get(n) == 0) continue;
            freq.put(n, freq.get(n) - 1);
            if (append.getOrDefault(n - 1, 0) > 0) { // can append to existing sequence
                append.put(n - 1, append.get(n - 1) - 1);
                append.put(n, append.getOrDefault(n, 0) + 1);
            } else if (freq.getOrDefault(n + 1, 0) > 0 && freq.getOrDefault(n + 2, 0) > 0) { // start new sequence
                freq.put(n + 1, freq.get(n + 1) - 1);
                freq.put(n + 2, freq.get(n + 2) - 1);
                append.put(n + 2, append.getOrDefault(n + 2, 0) + 1);
            } else return false;
        }
        return true;
    }

    public static void main(String[] a) {
        System.out.println(canJump(new int[]{2,3,1,1,4})); // true
        System.out.println(jump(new int[]{2,3,1,1,4})); // 2
        System.out.println(maxProfit(new int[]{7,1,5,3,6,4})); // 5
        System.out.println(maxProfitII(new int[]{7,1,5,3,6,4})); // 7
        System.out.println(canCompleteCircuit(new int[]{1,2,3,4,5}, new int[]{3,4,5,1,2})); // 3
        System.out.println(candy(new int[]{1,0,2})); // 5
        System.out.println(leastInterval(new char[]{'A','A','A','B','B','B'}, 2)); // 8
        System.out.println(partitionLabels("ababcbacadefegdehijhklij")); // [9,7,8]
        System.out.println(maxSubArray(new int[]{-2,1,-3,4,-1,2,1,-5,4})); // 6
        System.out.println(eraseOverlapIntervals(new int[][]{{1,2},{2,3},{3,4},{1,3}})); // 1
        System.out.println(findMinArrowShots(new int[][]{{10,16},{2,8},{1,6},{7,12}})); // 2
        System.out.println(twoCitySchedCost(new int[][]{{10,20},{30,200},{400,50},{30,20}})); // 110
        System.out.println(increasingTriplet(new int[]{1,2,3,4,5})); // true
        System.out.println(canPlaceFlowers(new int[]{1,0,0,0,1}, 1)); // true
        System.out.println(isSubsequence("abc", "ahbgdc")); // true
        System.out.println(isNStraightHand(new int[]{1,2,3,6,2,3,4,7,8}, 3)); // true
        System.out.println(isPossible(new int[]{1,2,3,3,4,5})); // true
    }
}
