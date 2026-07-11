import java.util.*;

/** DSA — Searching Algorithms (LeetCode). Each method has inline explanation.
 *  Run: javac Searching.java && java Searching */
public class Searching {

    // LC #33 Search in Rotated Sorted Array — Binary search with rotation, O(log n) time
    // Explanation: Determine which half is sorted, check if target is in sorted half
    public static int search(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] == target) return mid;
            if (nums[lo] <= nums[mid]) { // left half is sorted
                if (target >= nums[lo] && target < nums[mid]) hi = mid - 1; // target in left
                else lo = mid + 1; // target in right
            } else { // right half is sorted
                if (target > nums[mid] && target <= nums[hi]) lo = mid + 1; // target in right
                else hi = mid - 1; // target in left
            }
        }
        return -1;
    }

    // LC #81 Search in Rotated Sorted Array II — With duplicates, O(log n) average
    // Explanation: Similar to #33, handle duplicates by shrinking search space
    public static boolean searchII(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] == target) return true;
            if (nums[lo] == nums[mid] && nums[mid] == nums[hi]) { lo++; hi--; } // handle duplicates
            else if (nums[lo] <= nums[mid]) {
                if (target >= nums[lo] && target < nums[mid]) hi = mid - 1;
                else lo = mid + 1;
            } else {
                if (target > nums[mid] && target <= nums[hi]) lo = mid + 1;
                else hi = mid - 1;
            }
        }
        return false;
    }

    // LC #153 Find Minimum in Rotated Sorted Array — Binary search, O(log n) time
    // Explanation: Minimum is at pivot point where nums[mid] > nums[mid+1]
    public static int findMin(int[] nums) {
        int lo = 0, hi = nums.length - 1;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] > nums[hi]) lo = mid + 1; // min is in right half
            else hi = mid; // min is in left half (including mid)
        }
        return nums[lo];
    }

    // LC #154 Find Minimum in Rotated Sorted Array II — With duplicates, O(log n) average
    // Explanation: Similar to #153, handle duplicates by shrinking search space
    public static int findMinII(int[] nums) {
        int lo = 0, hi = nums.length - 1;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] > nums[hi]) lo = mid + 1;
            else if (nums[mid] < nums[hi]) hi = mid;
            else hi--; // nums[mid] == nums[hi], cannot determine
        }
        return nums[lo];
    }

    // LC #35 Search Insert Position — Binary search for insertion point, O(log n) time
    // Explanation: Standard binary search, return lo when not found
    public static int searchInsert(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] == target) return mid;
            if (nums[mid] < target) lo = mid + 1; else hi = mid - 1;
        }
        return lo; // insertion position
    }

    // LC #34 Find First and Last Position of Element — Binary search with bounds, O(log n) time
    // Explanation: Find leftmost and rightmost positions using modified binary search
    public static int[] searchRange(int[] nums, int target) {
        return new int[]{findLeft(nums, target), findRight(nums, target)};
    }
    static int findLeft(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1, idx = -1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] >= target) { hi = mid - 1; if (nums[mid] == target) idx = mid; }
            else lo = mid + 1;
        }
        return idx;
    }
    static int findRight(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1, idx = -1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] <= target) { lo = mid + 1; if (nums[mid] == target) idx = mid; }
            else hi = mid - 1;
        }
        return idx;
    }

    // LC #69 Sqrt(x) — Integer square root, O(log n) time
    // Explanation: Binary search on answer space [0, x]
    public static int mySqrt(int x) {
        if (x < 2) return x;
        int lo = 2, hi = x / 2;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            long sq = (long) mid * mid;
            if (sq == x) return mid;
            if (sq < x) lo = mid + 1; else hi = mid - 1;
        }
        return hi;
    }

    // LC #367 Valid Perfect Square — Check if n is perfect square, O(log n) time
    // Explanation: Binary search, check if mid*mid == n
    public static boolean isPerfectSquare(int num) {
        if (num < 2) return true;
        long lo = 2, hi = num / 2;
        while (lo <= hi) {
            long mid = lo + (hi - lo) / 2, sq = mid * mid;
            if (sq == num) return true;
            if (sq < num) lo = mid + 1; else hi = mid - 1;
        }
        return false;
    }

    // LC #74 Search a 2D Matrix — Matrix is sorted row-wise and column-wise, O(log(m*n)) time
    // Explanation: Treat as 1D sorted array, use binary search
    public static boolean searchMatrix(int[][] matrix, int target) {
        if (matrix.length == 0 || matrix[0].length == 0) return false;
        int m = matrix.length, n = matrix[0].length;
        int lo = 0, hi = m * n - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2, row = mid / n, col = mid % n;
            if (matrix[row][col] == target) return true;
            if (matrix[row][col] < target) lo = mid + 1; else hi = mid - 1;
        }
        return false;
    }

    // LC #240 Search a 2D Matrix II — Each row/col sorted, O(m+n) time
    // Explanation: Start from top-right, eliminate row or column each step
    public static boolean searchMatrixII(int[][] matrix, int target) {
        if (matrix.length == 0 || matrix[0].length == 0) return false;
        int m = matrix.length, n = matrix[0].length, row = 0, col = n - 1;
        while (row < m && col >= 0) {
            if (matrix[row][col] == target) return true;
            if (matrix[row][col] > target) col--; // eliminate column
            else row++; // eliminate row
        }
        return false;
    }

    // LC #378 Kth Smallest Element in a Sorted Matrix — Binary search on value, O(n log range) time
    // Explanation: Binary search on value range, count elements <= mid
    public static int kthSmallest(int[][] matrix, int k) {
        int n = matrix.length, lo = matrix[0][0], hi = matrix[n - 1][n - 1];
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2, count = 0;
            for (int i = 0; i < n; i++) {
                int j = n - 1; while (j >= 0 && matrix[i][j] > mid) j--; count += (j + 1);
            }
            if (count < k) lo = mid + 1; else hi = mid;
        }
        return lo;
    }

    // LC #4 Median of Two Sorted Arrays — O(log(min(m,n))) time
    // Explanation: Binary search on smaller array, partition both arrays
    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) return findMedianSortedArrays(nums2, nums1);
        int m = nums1.length, n = nums2.length, lo = 0, hi = m;
        while (lo <= hi) {
            int partitionX = (lo + hi) / 2, partitionY = (m + n + 1) / 2 - partitionX;
            int maxLeftX = (partitionX == 0) ? Integer.MIN_VALUE : nums1[partitionX - 1];
            int minRightX = (partitionX == m) ? Integer.MAX_VALUE : nums1[partitionX];
            int maxLeftY = (partitionY == 0) ? Integer.MIN_VALUE : nums2[partitionY - 1];
            int minRightY = (partitionY == n) ? Integer.MAX_VALUE : nums2[partitionY];
            if (maxLeftX <= minRightY && maxLeftY <= minRightX) {
                if ((m + n) % 2 == 0) return (Math.max(maxLeftX, maxLeftY) + Math.min(minRightX, minRightY)) / 2.0;
                return Math.max(maxLeftX, maxLeftY);
            } else if (maxLeftX > minRightY) hi = partitionX - 1; else lo = partitionX + 1;
        }
        throw new IllegalArgumentException();
    }

    // LC #875 Koko Eating Bananas — Binary search on answer, O(n log max) time
    // Explanation: Binary search on eating speed, check if can finish in h hours
    public static int minEatingSpeed(int[] piles, int h) {
        int lo = 1, hi = 0; for (int p : piles) hi = Math.max(hi, p);
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (canFinish(piles, mid, h)) hi = mid; else lo = mid + 1;
        }
        return lo;
    }
    static boolean canFinish(int[] piles, int k, int h) {
        long hours = 0; for (int p : piles) hours += (p + k - 1) / k; return hours <= h;
    }

    // LC #1011 Capacity To Ship Packages Within D Days — Binary search on answer, O(n log sum) time
    // Explanation: Binary search on capacity, check if can ship within days
    public static int shipWithinDays(int[] weights, int days) {
        int lo = 0, hi = 0; for (int w : weights) { lo = Math.max(lo, w); hi += w; }
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (canShip(weights, mid, days)) hi = mid; else lo = mid + 1;
        }
        return lo;
    }
    static boolean canShip(int[] weights, int cap, int days) {
        int cur = 0, needed = 1; for (int w : weights) {
            if (cur + w > cap) { cur = w; needed++; } else cur += w;
            if (needed > days) return false;
        }
        return true;
    }

    // LC #410 Split Array Largest Sum — Binary search on answer, O(n log sum) time
    // Explanation: Binary search on max subarray sum, check if can split into m parts
    public static int splitArray(int[] nums, int m) {
        int lo = 0, hi = 0; for (int n : nums) { lo = Math.max(lo, n); hi += n; }
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (canSplit(nums, mid, m)) hi = mid; else lo = mid + 1;
        }
        return lo;
    }
    static boolean canSplit(int[] nums, int max, int m) {
        int sum = 0, parts = 1; for (int n : nums) {
            if (sum + n > max) { sum = n; parts++; } else sum += n;
            if (parts > m) return false;
        }
        return true;
    }

    // LC #1283 Find the Smallest Divisor — Binary search on answer, O(n log max) time
    // Explanation: Binary search on divisor, check if sum of quotients <= threshold
    public static int smallestDivisor(int[] nums, int threshold) {
        int lo = 1, hi = 0; for (int n : nums) hi = Math.max(hi, n);
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (canDivide(nums, mid, threshold)) hi = mid; else lo = mid + 1;
        }
        return lo;
    }
    static boolean canDivide(int[] nums, int d, int threshold) {
        int sum = 0; for (int n : nums) sum += (n + d - 1) / d; return sum <= threshold;
    }

    // LC #1482 Minimum Number of Days to Make m Bouquets — Binary search on answer, O(n log max) time
    // Explanation: Binary search on days, check if can make m bouquets
    public static int minDays(int[] bloomDay, int m, int k) {
        if ((long) m * k > bloomDay.length) return -1;
        int lo = 1, hi = 0; for (int d : bloomDay) hi = Math.max(hi, d);
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (canMakeBouquets(bloomDay, mid, m, k)) hi = mid; else lo = mid + 1;
        }
        return lo;
    }
    static boolean canMakeBouquets(int[] bloomDay, int day, int m, int k) {
        int bouquets = 0, flowers = 0;
        for (int d : bloomDay) {
            if (d <= day) { flowers++; if (flowers == k) { bouquets++; flowers = 0; } }
            else flowers = 0;
            if (bouquets >= m) return true;
        }
        return bouquets >= m;
    }

    // LC #1552 Magnetic Force Between Two Balls — Binary search on answer, O(n log max) time
    // Explanation: Binary search on min distance, check if can place all balls
    public static int maxDistance(int[] position, int m) {
        Arrays.sort(position);
        int lo = 1, hi = position[position.length - 1] - position[0];
        while (lo < hi) {
            int mid = lo + (hi - lo + 1) / 2;
            if (canPlace(position, mid, m)) lo = mid; else hi = mid - 1;
        }
        return lo;
    }
    static boolean canPlace(int[] position, int dist, int m) {
        int count = 1, last = position[0];
        for (int i = 1; i < position.length; i++) {
            if (position[i] - last >= dist) { count++; last = position[i]; if (count >= m) return true; }
        }
        return false;
    }

    // LC #162 Find Peak Element — Binary search, O(log n) time
    // Explanation: Compare mid with mid+1, go towards higher value
    public static int findPeakElement(int[] nums) {
        int lo = 0, hi = nums.length - 1;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] < nums[mid + 1]) lo = mid + 1; else hi = mid;
        }
        return lo;
    }

    // LC #852 Peak Index in a Mountain Array — Binary search, O(log n) time
    // Explanation: Similar to #162, find where array stops increasing
    public static int peakIndexInMountainArray(int[] arr) {
        int lo = 0, hi = arr.length - 1;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (arr[mid] < arr[mid + 1]) lo = mid + 1; else hi = mid;
        }
        return lo;
    }

    // LC #1095 Find in Mountain Array — Search in mountain array, O(log n) time
    // Explanation: Find peak, then binary search in left (increasing) and right (decreasing) halves
    static interface MountainArray { int get(int index); int length(); }
    public static int findInMountainArray(int target, MountainArray mountainArr) {
        int n = mountainArr.length(), lo = 0, hi = n - 1, peak = 0;
        while (lo < hi) { int mid = lo + (hi - lo) / 2; if (mountainArr.get(mid) < mountainArr.get(mid + 1)) lo = peak = mid + 1; else hi = mid; }
        lo = 0; hi = peak;
        while (lo <= hi) { int mid = lo + (hi - lo) / 2, val = mountainArr.get(mid); if (val == target) return mid; if (val < target) lo = mid + 1; else hi = mid - 1; }
        lo = peak; hi = n - 1;
        while (lo <= hi) { int mid = lo + (hi - lo) / 2, val = mountainArr.get(mid); if (val == target) return mid; if (val > target) lo = mid + 1; else hi = mid - 1; }
        return -1;
    }

    public static void main(String[] a) {
        System.out.println(search(new int[]{4,5,6,7,0,1,2}, 0)); // 4
        System.out.println(searchII(new int[]{2,5,6,0,0,1,2}, 0)); // true
        System.out.println(findMin(new int[]{3,4,5,1,2})); // 1
        System.out.println(searchInsert(new int[]{1,3,5,6}, 5)); // 2
        System.out.println(Arrays.toString(searchRange(new int[]{5,7,7,8,8,10}, 8))); // [3,4]
        System.out.println(mySqrt(8)); // 2
        System.out.println(isPerfectSquare(16)); // true
        System.out.println(searchMatrix(new int[][]{{1,3,5,7},{10,11,16,20},{23,30,34,60}}, 3)); // true
        System.out.println(searchMatrixII(new int[][]{{1,4,7,11,15},{2,5,8,12,19},{3,6,9,16,22}}, 5)); // true
        System.out.println(kthSmallest(new int[][]{{1,5,9},{10,11,13},{12,13,15}}, 8)); // 13
        System.out.println(findMedianSortedArrays(new int[]{1,3}, new int[]{2})); // 2.0
        System.out.println(minEatingSpeed(new int[]{3,6,7,11}, 8)); // 4
        System.out.println(shipWithinDays(new int[]{1,2,3,4,5,6,7,8,9,10}, 5)); // 15
        System.out.println(splitArray(new int[]{7,2,5,10,8}, 2)); // 18
        System.out.println(smallestDivisor(new int[]{1,2,5,9}, 6)); // 5
        System.out.println(minDays(new int[]{1,10,3,10,2}, 3, 2)); // 3
        System.out.println(maxDistance(new int[]{1,2,8,4,9}, 3)); // 3
        System.out.println(findPeakElement(new int[]{1,2,3,1})); // 2
        System.out.println(peakIndexInMountainArray(new int[]{0,1,0})); // 1
    }
}
