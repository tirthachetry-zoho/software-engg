import java.util.*;

/** DSA — Two Pointers (LeetCode). Each method has inline explanation.
 *  Run: javac TwoPointers.java && java TwoPointers */
public class TwoPointers {

    // LC #125 Valid Palindrome — Check if string is palindrome, O(n) time
    // Explanation: Two pointers from ends, skip non-alphanumeric
    public static boolean isPalindrome(String s) {
        int left = 0, right = s.length() - 1;
        while (left < right) {
            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) left++;
            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) right--;
            if (Character.toLowerCase(s.charAt(left++)) != Character.toLowerCase(s.charAt(right--))) return false;
        }
        return true;
    }

    // LC #167 Two Sum II — Input array is sorted, O(n) time
    // Explanation: Two pointers from ends, move based on sum comparison
    public static int[] twoSum(int[] numbers, int target) {
        int left = 0, right = numbers.length - 1;
        while (left < right) {
            int sum = numbers[left] + numbers[right];
            if (sum == target) return new int[]{left + 1, right + 1};
            if (sum < target) left++; else right--;
        }
        return new int[]{};
    }

    // LC #15 3Sum — Find triplets summing to 0, O(n^2) time
    // Explanation: Sort array, use two pointers for each element
    public static List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums); List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            int left = i + 1, right = nums.length - 1;
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                if (sum == 0) { res.add(Arrays.asList(nums[i], nums[left], nums[right])); while (left < right && nums[left] == nums[left + 1]) left++; while (left < right && nums[right] == nums[right - 1]) right--; left++; right--; }
                else if (sum < 0) left++; else right--;
            }
        }
        return res;
    }

    // LC #16 3Sum Closest — Find triplet closest to target, O(n^2) time
    // Explanation: Similar to 3Sum, track closest sum
    public static int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums); int closest = nums[0] + nums[1] + nums[2];
        for (int i = 0; i < nums.length - 2; i++) {
            int left = i + 1, right = nums.length - 1;
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                if (Math.abs(sum - target) < Math.abs(closest - target)) closest = sum;
                if (sum < target) left++; else if (sum > target) right--; else return target;
            }
        }
        return closest;
    }

    // LC #18 4Sum — Find quadruplets summing to target, O(n^3) time
    // Explanation: Sort, use two pointers for each pair
    public static List<List<Integer>> fourSum(int[] nums, int target) {
        Arrays.sort(nums); List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < nums.length - 3; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            for (int j = i + 1; j < nums.length - 2; j++) {
                if (j > i + 1 && nums[j] == nums[j - 1]) continue;
                int left = j + 1, right = nums.length - 1;
                while (left < right) {
                    long sum = (long)nums[i] + nums[j] + nums[left] + nums[right];
                    if (sum == target) { res.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right])); while (left < right && nums[left] == nums[left + 1]) left++; while (left < right && nums[right] == nums[right - 1]) right--; left++; right--; }
                    else if (sum < target) left++; else right--;
                }
            }
        }
        return res;
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

    // LC #42 Trapping Rain Water — Calculate trapped water, O(n) time
    // Explanation: Two pointers, track max from left and right
    public static int trap(int[] height) {
        int left = 0, right = height.length - 1, leftMax = 0, rightMax = 0, water = 0;
        while (left < right) {
            if (height[left] < height[right]) {
                if (height[left] >= leftMax) leftMax = height[left]; else water += leftMax - height[left];
                left++;
            } else {
                if (height[right] >= rightMax) rightMax = height[right]; else water += rightMax - height[right];
                right--;
            }
        }
        return water;
    }

    // LC #75 Sort Colors — Dutch national flag, O(n) time
    // Explanation: Three-way partition with three pointers
    public static void sortColors(int[] nums) {
        int low = 0, mid = 0, high = nums.length - 1;
        while (mid <= high) {
            if (nums[mid] == 0) { swap(nums, low++, mid++); }
            else if (nums[mid] == 1) { mid++; }
            else { swap(nums, mid, high--); }
        }
    }
    static void swap(int[] nums, int i, int j) { int t = nums[i]; nums[i] = nums[j]; nums[j] = t; }

    // LC #283 Move Zeroes — Move all zeros to end, O(n) time
    // Explanation: Two pointers, one for current position, one for next non-zero
    public static void moveZeroes(int[] nums) {
        int insertPos = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) { nums[insertPos++] = nums[i]; }
        }
        while (insertPos < nums.length) nums[insertPos++] = 0;
    }

    // LC #26 Remove Duplicates from Sorted Array — O(n) time
    // Explanation: Two pointers, one for current, one for unique position
    public static int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;
        int i = 0;
        for (int j = 1; j < nums.length; j++) {
            if (nums[j] != nums[i]) { i++; nums[i] = nums[j]; }
        }
        return i + 1;
    }

    // LC #80 Remove Duplicates from Sorted Array II — Allow at most 2 duplicates, O(n) time
    // Explanation: Similar to #26 but with count check
    public static int removeDuplicatesII(int[] nums) {
        if (nums.length <= 2) return nums.length;
        int i = 2;
        for (int j = 2; j < nums.length; j++) {
            if (nums[j] != nums[i - 2]) { nums[i++] = nums[j]; }
        }
        return i;
    }

    // LC #88 Merge Sorted Array — Merge two sorted arrays, O(m+n) time
    // Explanation: Merge from end to avoid overwriting
    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        int i = m - 1, j = n - 1, k = m + n - 1;
        while (i >= 0 && j >= 0) {
            if (nums1[i] > nums2[j]) nums1[k--] = nums1[i--];
            else nums1[k--] = nums2[j--];
        }
        while (j >= 0) nums1[k--] = nums2[j--];
    }

    // LC #209 Minimum Size Subarray Sum — Smallest subarray with sum >= target, O(n) time
    // Explanation: Sliding window with two pointers
    public static int minSubArrayLen(int target, int[] nums) {
        int left = 0, sum = 0, minLen = Integer.MAX_VALUE;
        for (int right = 0; right < nums.length; right++) {
            sum += nums[right];
            while (sum >= target) {
                minLen = Math.min(minLen, right - left + 1);
                sum -= nums[left++];
            }
        }
        return minLen == Integer.MAX_VALUE ? 0 : minLen;
    }

    // LC #3 Longest Substring Without Repeating Characters — O(n) time
    // Explanation: Sliding window with hashmap for last occurrence
    public static int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> lastSeen = new HashMap<>();
        int maxLen = 0, left = 0;
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (lastSeen.containsKey(c) && lastSeen.get(c) >= left) {
                left = lastSeen.get(c) + 1;
            }
            lastSeen.put(c, right);
            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }

    // LC #76 Minimum Window Substring — Minimum window containing all chars, O(n) time
    // Explanation: Sliding window with character count
    public static String minWindow(String s, String t) {
        if (s.length() < t.length()) return "";
        Map<Character, Integer> need = new HashMap<>(); int required = distinctiveChars(t, need);
        int left = 0, formed = 0, minLen = Integer.MAX_VALUE, minLeft = 0;
        Map<Character, Integer> window = new HashMap<>();
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            window.put(c, window.getOrDefault(c, 0) + 1);
            if (need.containsKey(c) && window.get(c).intValue() == need.get(c).intValue()) formed++;
            while (left <= right && formed == required) {
                if (right - left + 1 < minLen) { minLen = right - left + 1; minLeft = left; }
                char leftChar = s.charAt(left); window.put(leftChar, window.get(leftChar) - 1);
                if (need.containsKey(leftChar) && window.get(leftChar) < need.get(leftChar)) formed--;
                left++;
            }
        }
        return minLen == Integer.MAX_VALUE ? "" : s.substring(minLeft, minLeft + minLen);
    }
    static int distinctiveChars(String t, Map<Character, Integer> map) {
        for (char c : t.toCharArray()) map.put(c, map.getOrDefault(c, 0) + 1);
        return map.size();
    }

    // LC #392 Is Subsequence — Check if s is subsequence of t, O(n) time
    // Explanation: Two pointers, match characters in order
    public static boolean isSubsequence(String s, String t) {
        int i = 0, j = 0;
        while (i < s.length() && j < t.length()) {
            if (s.charAt(i) == t.charAt(j)) i++;
            j++;
        }
        return i == s.length();
    }

    // LC #633 Sum of Square Numbers — Check if c = a^2 + b^2, O(sqrt(c)) time
    // Explanation: Two pointers from 0 to sqrt(c)
    public static boolean judgeSquareSum(int c) {
        long left = 0, right = (long)Math.sqrt(c);
        while (left <= right) {
            long sum = left * left + right * right;
            if (sum == c) return true;
            if (sum < c) left++; else right--;
        }
        return false;
    }

    // LC #680 Valid Palindrome II — Can be palindrome after at most one deletion, O(n) time
    // Explanation: Two pointers, try skipping one char when mismatch
    public static boolean validPalindrome(String s) {
        int left = 0, right = s.length() - 1;
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return isPalindromeRange(s, left + 1, right) || isPalindromeRange(s, left, right - 1);
            }
            left++; right--;
        }
        return true;
    }
    static boolean isPalindromeRange(String s, int left, int right) {
        while (left < right) {
            if (s.charAt(left++) != s.charAt(right--)) return false;
        }
        return true;
    }

    // LC #524 Longest Word in Dictionary through Deleting — Longest word that is subsequence, O(n*m) time
    // Explanation: For each word, check if it's subsequence, track longest
    public static String findLongestWord(String s, List<String> dictionary) {
        String longest = "";
        for (String word : dictionary) {
            if (isSubsequence(word, s)) {
                if (word.length() > longest.length() || (word.length() == longest.length() && word.compareTo(longest) < 0)) {
                    longest = word;
                }
            }
        }
        return longest;
    }
    static boolean isSubsequence(String word, String s) {
        int i = 0, j = 0;
        while (i < word.length() && j < s.length()) {
            if (word.charAt(i) == s.charAt(j)) i++;
            j++;
        }
        return i == word.length();
    }

    public static void main(String[] a) {
        System.out.println(isPalindrome("A man, a plan, a canal: Panama")); // true
        System.out.println(Arrays.toString(twoSum(new int[]{2,7,11,15}, 9))); // [1,2]
        System.out.println(threeSum(new int[]{-1,0,1,2,-1,-4}).size()); // 3
        System.out.println(threeSumClosest(new int[]{-1,2,1,-4}, 1)); // 2
        System.out.println(maxArea(new int[]{1,8,6,2,5,4,8,3,7})); // 49
        System.out.println(trap(new int[]{0,1,0,2,1,0,1,3,2,1,2,1})); // 6
        int[] colors = new int[]{2,0,2,1,1,0}; sortColors(colors); System.out.println(Arrays.toString(colors)); // [0,0,1,1,2,2]
        int[] zeros = new int[]{0,1,0,3,12}; moveZeroes(zeros); System.out.println(Arrays.toString(zeros)); // [1,3,12,0,0]
        System.out.println(removeDuplicates(new int[]{1,1,2})); // 2
        System.out.println(removeDuplicatesII(new int[]{1,1,1,2,2,3})); // 5
        System.out.println(minSubArrayLen(7, new int[]{2,3,1,2,4,3})); // 2
        System.out.println(lengthOfLongestSubstring("abcabcbb")); // 3
        System.out.println(minWindow("ADOBECODEBANC", "ABC")); // "BANC"
        System.out.println(isSubsequence("abc", "ahbgdc")); // true
        System.out.println(judgeSquareSum(5)); // true
        System.out.println(validPalindrome("abca")); // true
        System.out.println(findLongestWord("abpcplea", Arrays.asList("ale","apple","monkey","plea"))); // "apple"
    }
}
