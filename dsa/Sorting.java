import java.util.*;

/** DSA — Sorting Algorithms (LeetCode). Each method has inline explanation.
 *  Run: javac Sorting.java && java Sorting */
public class Sorting {

    // LC #912 Sort an Array — Merge sort implementation, O(n log n) time
    // Explanation: Divide and conquer, merge sorted halves
    public static int[] sortArray(int[] nums) {
        mergeSort(nums, 0, nums.length - 1);
        return nums;
    }
    static void mergeSort(int[] nums, int lo, int hi) {
        if (lo >= hi) return;
        int mid = lo + (hi - lo) / 2;
        mergeSort(nums, lo, mid); mergeSort(nums, mid + 1, hi);
        merge(nums, lo, mid, hi);
    }
    static void merge(int[] nums, int lo, int mid, int hi) {
        int[] temp = Arrays.copyOfRange(nums, lo, hi + 1);
        int i = 0, j = mid - lo + 1, k = lo;
        while (i < mid - lo + 1 && j < hi - lo + 1) nums[k++] = (temp[i] <= temp[j]) ? temp[i++] : temp[j++];
        while (i < mid - lo + 1) nums[k++] = temp[i++];
        while (j < hi - lo + 1) nums[k++] = temp[j++];
    }

    // LC #75 Sort Colors — Dutch national flag, O(n) time
    // Explanation: Three-way partition: 0s, 1s, 2s
    public static void sortColors(int[] nums) {
        int lo = 0, mid = 0, hi = nums.length - 1;
        while (mid <= hi) {
            if (nums[mid] == 0) { swap(nums, lo++, mid++); } // move 0 to left
            else if (nums[mid] == 1) { mid++; } // 1 stays in middle
            else { swap(nums, mid, hi--); } // move 2 to right
        }
    }
    static void swap(int[] nums, int i, int j) { int t = nums[i]; nums[i] = nums[j]; nums[j] = t; }

    // LC #169 Majority Element — Boyer-Moore voting, O(n) time
    // Explanation: Cancel out different elements, majority remains
    public static int majorityElement(int[] nums) {
        int count = 0, candidate = 0;
        for (int num : nums) {
            if (count == 0) candidate = num;
            count += (num == candidate) ? 1 : -1;
        }
        return candidate;
    }

    // LC #215 Kth Largest Element in an Array — Quickselect, O(n) average time
    // Explanation: Partition around pivot, recursively search in relevant half
    public static int findKthLargest(int[] nums, int k) {
        return quickSelect(nums, 0, nums.length - 1, nums.length - k);
    }
    static int quickSelect(int[] nums, int lo, int hi, int k) {
        if (lo == hi) return nums[lo];
        int pivotIdx = partition(nums, lo, hi);
        if (k == pivotIdx) return nums[k];
        if (k < pivotIdx) return quickSelect(nums, lo, pivotIdx - 1, k);
        return quickSelect(nums, pivotIdx + 1, hi, k);
    }
    static int partition(int[] nums, int lo, int hi) {
        int pivot = nums[hi], i = lo;
        for (int j = lo; j < hi; j++) if (nums[j] <= pivot) swap(nums, i++, j);
        swap(nums, i, hi); return i;
    }

    // LC #56 Merge Intervals — Sort and merge, O(n log n) time
    // Explanation: Sort by start time, merge overlapping intervals
    public static int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        List<int[]> res = new ArrayList<>();
        for (int[] iv : intervals) {
            if (res.isEmpty() || res.get(res.size() - 1)[1] < iv[0]) res.add(iv);
            else res.get(res.size() - 1)[1] = Math.max(res.get(res.size() - 1)[1], iv[1]);
        }
        return res.toArray(new int[0][]);
    }

    // LC #252 Meeting Rooms — Check if meetings overlap, O(n log n) time
    // Explanation: Sort by start time, check if any meeting starts before previous ends
    public static boolean canAttendMeetings(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] < intervals[i - 1][1]) return false;
        }
        return true;
    }

    // LC #253 Meeting Rooms II — Minimum meeting rooms, O(n log n) time
    // Explanation: Sort start and end times separately, use two pointers
    public static int minMeetingRooms(int[][] intervals) {
        int[] starts = new int[intervals.length], ends = new int[intervals.length];
        for (int i = 0; i < intervals.length; i++) { starts[i] = intervals[i][0]; ends[i] = intervals[i][1]; }
        Arrays.sort(starts); Arrays.sort(ends);
        int rooms = 0, endIdx = 0;
        for (int start : starts) {
            if (start < ends[endIdx]) rooms++; // need new room
            else endIdx++; // reuse room
        }
        return rooms;
    }

    // LC #179 Largest Number — Form largest number from array, O(n log n) time
    // Explanation: Custom comparator: a+b > b+a means a should come before b
    public static String largestNumber(int[] nums) {
        String[] strs = new String[nums.length];
        for (int i = 0; i < nums.length; i++) strs[i] = String.valueOf(nums[i]);
        Arrays.sort(strs, (a, b) -> (b + a).compareTo(a + b)); // descending order
        if (strs[0].equals("0")) return "0"; // handle all zeros
        return String.join("", strs);
    }

    // LC #164 Maximum Gap — Maximum difference between consecutive elements, O(n) time
    // Explanation: Bucket sort / pigeonhole principle
    public static int maximumGap(int[] nums) {
        if (nums.length < 2) return 0;
        int min = nums[0], max = nums[0];
        for (int n : nums) { min = Math.min(min, n); max = Math.max(max, n); }
        int gap = (max - min) / (nums.length - 1) + 1, m = nums.length;
        int[] bucketsMin = new int[m], bucketsMax = new int[m];
        Arrays.fill(bucketsMin, Integer.MAX_VALUE); Arrays.fill(bucketsMax, Integer.MIN_VALUE);
        for (int n : nums) {
            int idx = (n - min) / gap;
            bucketsMin[idx] = Math.min(bucketsMin[idx], n);
            bucketsMax[idx] = Math.max(bucketsMax[idx], n);
        }
        int maxGap = 0, prev = min;
        for (int i = 0; i < m; i++) {
            if (bucketsMin[i] == Integer.MAX_VALUE) continue;
            maxGap = Math.max(maxGap, bucketsMin[i] - prev);
            prev = bucketsMax[i];
        }
        return maxGap;
    }

    // LC #242 Valid Anagram — Check if two strings are anagrams, O(n log n) time
    // Explanation: Sort both strings and compare
    public static boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;
        char[] sArr = s.toCharArray(), tArr = t.toCharArray();
        Arrays.sort(sArr); Arrays.sort(tArr);
        return Arrays.equals(sArr, tArr);
    }

    // LC #49 Group Anagrams — Group anagrams together, O(n k log k) time
    // Explanation: Sort each string, use as key in hashmap
    public static List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String s : strs) {
            char[] chars = s.toCharArray(); Arrays.sort(chars);
            String key = new String(chars);
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }
        return new ArrayList<>(map.values());
    }

    // LC #347 Top K Frequent Elements — Bucket sort, O(n) time
    // Explanation: Count frequency, use bucket sort by frequency
    public static int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums) freq.put(n, freq.getOrDefault(n, 0) + 1);
        List<Integer>[] buckets = new List[nums.length + 1];
        for (int key : freq.keySet()) {
            int f = freq.get(key);
            if (buckets[f] == null) buckets[f] = new ArrayList<>();
            buckets[f].add(key);
        }
        int[] res = new int[k]; int idx = 0;
        for (int i = buckets.length - 1; i >= 0 && idx < k; i--) {
            if (buckets[i] != null) for (int num : buckets[i]) res[idx++] = num;
        }
        return res;
    }

    // LC #451 Sort Characters By Frequency — Sort by frequency, O(n log n) time
    // Explanation: Count frequency, sort characters by frequency
    public static String frequencySort(String s) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : s.toCharArray()) freq.put(c, freq.getOrDefault(c, 0) + 1);
        List<Character> chars = new ArrayList<>(freq.keySet());
        chars.sort((a, b) -> freq.get(b) - freq.get(a)); // sort by frequency descending
        StringBuilder sb = new StringBuilder();
        for (char c : chars) for (int i = 0; i < freq.get(c); i++) sb.append(c);
        return sb.toString();
    }

    // LC #973 K Closest Points to Origin — Quickselect or sort, O(n log n) time
    // Explanation: Sort by distance squared, return first k
    public static int[][] kClosest(int[][] points, int k) {
        Arrays.sort(points, (a, b) -> Integer.compare(a[0]*a[0] + a[1]*a[1], b[0]*b[0] + b[1]*b[1]));
        return Arrays.copyOfRange(points, 0, k);
    }

    // LC #324 Wiggle Sort II — Sort and interleave, O(n log n) time
    // Explanation: Sort, then split and interleave from middle
    public static void wiggleSort(int[] nums) {
        int[] sorted = nums.clone(); Arrays.sort(sorted);
        int n = nums.length, left = (n - 1) / 2, right = n - 1;
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) nums[i] = sorted[left--]; // smaller elements at even indices
            else nums[i] = sorted[right--]; // larger elements at odd indices
        }
    }

    // LC #280 Wiggle Sort — O(n) time
    // Explanation: Swap adjacent elements if they don't satisfy wiggle condition
    public static void wiggleSortSimple(int[] nums) {
        for (int i = 1; i < nums.length; i++) {
            if ((i % 2 == 1) == (nums[i] < nums[i - 1])) { swap(nums, i, i - 1); }
        }
    }

    // LC #321 Create Maximum Number — Create max number from two arrays, O((m+n)^3) time
    // Explanation: Greedy + merge, find max subsequence from each array
    public static int[] maxNumber(int[] nums1, int[] nums2, int k) {
        int n = nums1.length, m = nums2.length;
        int[] res = new int[k];
        for (int i = Math.max(0, k - m); i <= Math.min(k, n); i++) {
            int[] sub1 = maxSubsequence(nums1, i), sub2 = maxSubsequence(nums2, k - i);
            int[] merged = merge(sub1, sub2);
            if (greater(merged, 0, res, 0)) res = merged;
        }
        return res;
    }
    static int[] maxSubsequence(int[] nums, int k) {
        int n = nums.length; int[] res = new int[k]; int idx = 0;
        for (int i = 0; i < n; i++) {
            while (idx > 0 && n - i + idx > k && nums[i] > res[idx - 1]) idx--;
            if (idx < k) res[idx++] = nums[i];
        }
        return res;
    }
    static int[] merge(int[] nums1, int[] nums2) {
        int[] res = new int[nums1.length + nums2.length]; int i = 0, j = 0, k = 0;
        while (i < nums1.length || j < nums2.length) {
            if (greater(nums1, i, nums2, j)) res[k++] = nums1[i++];
            else res[k++] = nums2[j++];
        }
        return res;
    }
    static boolean greater(int[] nums1, int i, int[] nums2, int j) {
        while (i < nums1.length && j < nums2.length && nums1[i] == nums2[j]) { i++; j++; }
        return j == nums2.length || (i < nums1.length && nums1[i] > nums2[j]);
    }

    // LC #767 Reorganize String — Rearrange string so no adjacent chars same, O(n log n) time
    // Explanation: Use max heap, always place most frequent char
    public static String reorganizeString(String s) {
        int[] freq = new int[26]; for (char c : s.toCharArray()) freq[c - 'a']++;
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> b[1] - a[1]);
        for (int i = 0; i < 26; i++) if (freq[i] > 0) pq.offer(new int[]{i, freq[i]});
        StringBuilder sb = new StringBuilder();
        while (pq.size() > 1) {
            int[] first = pq.poll(), second = pq.poll();
            sb.append((char) (first[0] + 'a')); sb.append((char) (second[0] + 'a'));
            if (--first[1] > 0) pq.offer(first);
            if (--second[1] > 0) pq.offer(second);
        }
        if (!pq.isEmpty()) {
            int[] last = pq.poll();
            if (last[1] > 1) return ""; // cannot place
            sb.append((char) (last[0] + 'a'));
        }
        return sb.toString();
    }

    // LC #1051 Height Checker — Students not in correct position, O(n log n) time
    // Explanation: Sort and compare with original
    public static int heightChecker(int[] heights) {
        int[] sorted = heights.clone(); Arrays.sort(sorted);
        int count = 0;
        for (int i = 0; i < heights.length; i++) if (heights[i] != sorted[i]) count++;
        return count;
    }

    // LC #1122 Relative Sort Array — Sort according to another array, O(n log n) time
    // Explanation: Count frequency, then place according to order2
    public static int[] relativeSortArray(int[] arr1, int[] arr2) {
        int[] count = new int[1001]; for (int n : arr1) count[n]++;
        int[] res = new int[arr1.length]; int idx = 0;
        for (int n : arr2) while (count[n]-- > 0) res[idx++] = n;
        for (int i = 0; i < 1001; i++) while (count[i]-- > 0) res[idx++] = i;
        return res;
    }

    // LC #1365 How Many Numbers Are Smaller Than the Current Number, O(n log n) time
    // Explanation: Sort and use binary search
    public static int[] smallerNumbersThanCurrent(int[] nums) {
        int[] sorted = nums.clone(); Arrays.sort(sorted);
        int[] res = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            res[i] = lowerBound(sorted, nums[i]);
        }
        return res;
    }
    static int lowerBound(int[] nums, int target) {
        int lo = 0, hi = nums.length;
        while (lo < hi) { int mid = lo + (hi - lo) / 2; if (nums[mid] < target) lo = mid + 1; else hi = mid; }
        return lo;
    }

    // LC #1331 Rank Transform of an Array — Replace elements with their rank, O(n log n) time
    // Explanation: Sort unique values, map to rank
    public static int[] arrayRankTransform(int[] arr) {
        int[] sorted = arr.clone(); Arrays.sort(sorted);
        Map<Integer, Integer> rankMap = new HashMap<>();
        int rank = 1;
        for (int n : sorted) if (!rankMap.containsKey(n)) rankMap.put(n, rank++);
        int[] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++) res[i] = rankMap.get(arr[i]);
        return res;
    }

    // LC #1509 Minimum Difference Between Largest and Smallest Value in Three Moves, O(n log n) time
    // Explanation: Sort, try removing 3 elements from ends
    public static int minDifference(int[] nums) {
        if (nums.length <= 4) return 0;
        Arrays.sort(nums);
        int n = nums.length;
        return Math.min(Math.min(nums[n - 1] - nums[3], nums[n - 4] - nums[0]),
            Math.min(nums[n - 2] - nums[2], nums[n - 3] - nums[1]));
    }

    // LC #1637 Widest Vertical Area Between Two Points, O(n log n) time
    // Explanation: Sort by x-coordinate, find max gap
    public static int maxWidthOfVerticalArea(int[][] points) {
        Arrays.sort(points, (a, b) -> Integer.compare(a[0], b[0]));
        int maxGap = 0;
        for (int i = 1; i < points.length; i++) maxGap = Math.max(maxGap, points[i][0] - points[i - 1][0]);
        return maxGap;
    }

    public static void main(String[] a) {
        System.out.println(Arrays.toString(sortArray(new int[]{5,2,3,1}))); // [1,2,3,5]
        int[] colors = new int[]{2,0,2,1,1,0}; sortColors(colors); System.out.println(Arrays.toString(colors)); // [0,0,1,1,2,2]
        System.out.println(majorityElement(new int[]{3,2,3})); // 3
        System.out.println(findKthLargest(new int[]{3,2,1,5,6,4}, 2)); // 5
        System.out.println(canAttendMeetings(new int[][]{{0,30},{5,10},{15,20}})); // false
        System.out.println(minMeetingRooms(new int[][]{{0,30},{5,10},{15,20}})); // 2
        System.out.println(largestNumber(new int[]{10,2})); // "210"
        System.out.println(maximumGap(new int[]{3,6,9,1})); // 3
        System.out.println(isAnagram("anagram", "nagaram")); // true
        System.out.println(groupAnagrams(new String[]{"eat","tea","tan","ate","nat","bat"}).size()); // 3
        System.out.println(Arrays.toString(topKFrequent(new int[]{1,1,1,2,2,3}, 2))); // [1,2]
        System.out.println(frequencySort("tree")); // "eert"
        System.out.println(reorganizeString("aab")); // "aba"
        System.out.println(heightChecker(new int[]{1,1,4,2,1,3})); // 3
        System.out.println(Arrays.toString(relativeSortArray(new int[]{2,3,1,3,2,4,6,7,9,2,19}, new int[]{2,1,4,3,6,7})));
        System.out.println(Arrays.toString(smallerNumbersThanCurrent(new int[]{8,1,2,2,3}))); // [4,0,1,1,3]
        System.out.println(Arrays.toString(arrayRankTransform(new int[]{40,10,20,30}))); // [4,1,2,3]
        System.out.println(minDifference(new int[]{5,3,2,4,1,2})); // 0
        System.out.println(maxWidthOfVerticalArea(new int[][]{{8,7},{9,9},{7,4},{9,7}})); // 1
    }
}
