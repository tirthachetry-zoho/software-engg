import java.util.*;

/** DSA — Bit Manipulation (LeetCode). Each method has inline explanation.
 *  Run: javac BitManipulation.java && java BitManipulation */
public class BitManipulation {

    // LC #136 Single Number — XOR all numbers, pairs cancel out, O(n) time
    // Explanation: a ^ a = 0, a ^ 0 = a, so XOR of all gives the single number
    public static int singleNumber(int[] nums) {
        int res = 0;
        for (int n : nums) res ^= n; // XOR all numbers
        return res;
    }

    // LC #137 Single Number II — Every number appears three times except one, O(n) time
    // Explanation: Count bits modulo 3 using two bitmasks
    public static int singleNumberII(int[] nums) {
        int ones = 0, twos = 0; // ones = bits appeared once, twos = bits appeared twice
        for (int n : nums) {
            ones = (ones ^ n) & ~twos; // update ones
            twos = (twos ^ n) & ~ones; // update twos
        }
        return ones;
    }

    // LC #260 Single Number III — Two single numbers, others appear twice, O(n) time
    // Explanation: XOR all to get xor of two singles, then partition based on rightmost set bit
    public static int[] singleNumberIII(int[] nums) {
        int xor = 0; for (int n : nums) xor ^= n; // xor of the two singles
        int rightmost = xor & (-xor); // rightmost set bit
        int[] res = new int[2];
        for (int n : nums) {
            if ((n & rightmost) == 0) res[0] ^= n; // partition based on bit
            else res[1] ^= n;
        }
        return res;
    }

    // LC #191 Number of 1 Bits — Count set bits, O(1) time (32 bits)
    // Explanation: Brian Kernighan's algorithm: n & (n-1) clears rightmost set bit
    public static int hammingWeight(int n) {
        int count = 0;
        while (n != 0) { n &= (n - 1); count++; } // clear rightmost set bit
        return count;
    }

    // LC #338 Counting Bits — Count set bits for all numbers 0 to n, O(n) time
    // Explanation: dp[i] = dp[i >> 1] + (i & 1), reuse previous results
    public static int[] countBits(int n) {
        int[] dp = new int[n + 1]; dp[0] = 0;
        for (int i = 1; i <= n; i++) dp[i] = dp[i >> 1] + (i & 1); // count bits using previous
        return dp;
    }

    // LC #461 Hamming Distance — Number of differing bits, O(1) time
    // Explanation: XOR the numbers, then count set bits
    public static int hammingDistance(int x, int y) {
        int xor = x ^ y; int count = 0;
        while (xor != 0) { xor &= (xor - 1); count++; }
        return count;
    }

    // LC #476 Number Complement — Flip bits of positive integer, O(1) time
    // Explanation: Create mask of all 1s up to highest bit, XOR with it
    public static int findComplement(int num) {
        int mask = (Integer.highestOneBit(num) << 1) - 1; // mask with all 1s up to highest bit
        return num ^ mask; // flip bits
    }

    // LC #231 Power of Two — Check if n is power of 2, O(1) time
    // Explanation: Power of 2 has exactly one set bit: n & (n-1) == 0
    public static boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    // LC #342 Power of Four — Check if n is power of 4, O(1) time
    // Explanation: Power of 4 has one set bit in odd position
    public static boolean isPowerOfFour(int n) {
        return n > 0 && (n & (n - 1)) == 0 && (n & 0x55555555) != 0;
    }

    // LC #69 Sqrt(x) — Integer square root, O(log n) time
    // Explanation: Binary search on answer space
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

    // LC #29 Divide Two Integers — Divide without using multiplication/division/mod, O(log n) time
    // Explanation: Subtract powers of 2 times divisor from dividend
    public static int divide(int dividend, int divisor) {
        if (dividend == Integer.MIN_VALUE && divisor == -1) return Integer.MAX_VALUE;
        boolean negative = (dividend < 0) ^ (divisor < 0);
        long dvd = Math.abs((long) dividend), dvs = Math.abs((long) divisor), res = 0;
        while (dvd >= dvs) {
            long temp = dvs, count = 1;
            while (dvd >= (temp << 1)) { temp <<= 1; count <<= 1; }
            dvd -= temp; res += count;
        }
        return negative ? (int) -res : (int) res;
    }

    // LC #78 Subsets — Generate all subsets using bitmask, O(2^n) time
    // Explanation: Each subset corresponds to a bitmask from 0 to 2^n - 1
    public static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        int n = nums.length;
        for (int mask = 0; mask < (1 << n); mask++) { // iterate all bitmasks
            List<Integer> subset = new ArrayList<>();
            for (int i = 0; i < n; i++) if ((mask >> i & 1) == 1) subset.add(nums[i]); // if bit set, include
            res.add(subset);
        }
        return res;
    }

    // LC #784 Letter Case Permutation — Generate all letter case combinations, O(2^n) time
    // Explanation: Use bitmask to decide which letters to uppercase
    public static List<String> letterCasePermutation(String s) {
        List<String> res = new ArrayList<>();
        int letterCount = 0; for (char c : s.toCharArray()) if (Character.isLetter(c)) letterCount++;
        for (int mask = 0; mask < (1 << letterCount); mask++) {
            StringBuilder sb = new StringBuilder(); int bitIdx = 0;
            for (char c : s.toCharArray()) {
                if (Character.isLetter(c)) {
                    if ((mask >> bitIdx & 1) == 1) sb.append(Character.toUpperCase(c));
                    else sb.append(Character.toLowerCase(c));
                    bitIdx++;
                } else sb.append(c);
            }
            res.add(sb.toString());
        }
        return res;
    }

    // LC #201 Bitwise AND of Numbers Range — AND all numbers in range, O(1) time
    // Explanation: Find common prefix of m and n, shift right until equal
    public static int rangeBitwiseAnd(int m, int n) {
        int shift = 0;
        while (m < n) { m >>= 1; n >>= 1; shift++; } // shift right until equal
        return m << shift; // shift back
    }

    // LC #371 Sum of Two Integers — Add without + operator, O(1) time
    // Explanation: Use XOR for sum without carry, AND << 1 for carry
    public static int getSum(int a, int b) {
        while (b != 0) {
            int carry = a & b; // calculate carry
            a = a ^ b; // sum without carry
            b = carry << 1; // shift carry to left
        }
        return a;
    }

    // LC #190 Reverse Bits — Reverse bits of 32-bit unsigned integer, O(1) time
    // Explanation: Extract each bit and place in reverse position
    public static int reverseBits(int n) {
        int res = 0;
        for (int i = 0; i < 32; i++) {
            res <<= 1; res |= (n & 1); // shift left and add LSB
            n >>= 1; // shift right
        }
        return res;
    }

    // LC #393 UTF-8 Validation — Validate UTF-8 encoding, O(n) time
    // Explanation: Check byte patterns according to UTF-8 rules
    public static boolean validUtf8(int[] data) {
        int i = 0, n = data.length;
        while (i < n) {
            int bytes = 0;
            if ((data[i] >> 7) == 0b0) bytes = 1; // 1-byte char
            else if ((data[i] >> 5) == 0b110) bytes = 2; // 2-byte char
            else if ((data[i] >> 4) == 0b1110) bytes = 3; // 3-byte char
            else if ((data[i] >> 3) == 0b11110) bytes = 4; // 4-byte char
            else return false;
            if (i + bytes > n) return false; // not enough bytes
            for (int j = 1; j < bytes; j++) if ((data[i + j] >> 6) != 0b10) return false; // check continuation bytes
            i += bytes;
        }
        return true;
    }

    // LC #401 Binary Watch — All possible times, O(1) time (max 12*60 combinations)
    // Explanation: Enumerate all hour/minute combinations with correct number of LEDs
    public static List<String> readBinaryWatch(int turnedOn) {
        List<String> res = new ArrayList<>();
        for (int h = 0; h < 12; h++) {
            for (int m = 0; m < 60; m++) {
                if (Integer.bitCount(h) + Integer.bitCount(m) == turnedOn)
                    res.add(String.format("%d:%02d", h, m));
            }
        }
        return res;
    }

    // LC #477 Total Hamming Distance — Sum of hamming distances for all pairs, O(n) time
    // Explanation: For each bit position, count zeros and ones, add zeros*ones to result
    public static int totalHammingDistance(int[] nums) {
        int total = 0, n = nums.length;
        for (int i = 0; i < 32; i++) {
            int bitCount = 0;
            for (int num : nums) bitCount += (num >> i) & 1; // count set bits at position i
            total += bitCount * (n - bitCount); // pairs with different bits
        }
        return total;
    }

    // LC #318 Maximum Product of Word Lengths — Max product of words with no common letters, O(n^2) time
    // Explanation: Use bitmask to represent letters in each word
    public static int maxProduct(String[] words) {
        int n = words.length, max = 0;
        int[] masks = new int[n];
        for (int i = 0; i < n; i++) {
            for (char c : words[i].toCharArray()) masks[i] |= 1 << (c - 'a'); // create bitmask
        }
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if ((masks[i] & masks[j]) == 0) // no common letters
                    max = Math.max(max, words[i].length() * words[j].length());
            }
        }
        return max;
    }

    public static void main(String[] a) {
        System.out.println(singleNumber(new int[]{2,2,1})); // 1
        System.out.println(singleNumberII(new int[]{2,2,3,2})); // 3
        System.out.println(Arrays.toString(singleNumberIII(new int[]{1,2,1,3,2,5}))); // [3,5]
        System.out.println(hammingWeight(11)); // 3
        System.out.println(Arrays.toString(countBits(5))); // [0,1,1,2,1,2]
        System.out.println(hammingDistance(1, 4)); // 2
        System.out.println(findComplement(5)); // 2
        System.out.println(isPowerOfTwo(16)); // true
        System.out.println(isPowerOfFour(16)); // true
        System.out.println(mySqrt(8)); // 2
        System.out.println(divide(10, 3)); // 3
        System.out.println(subsets(new int[]{1,2,3}).size()); // 8
        System.out.println(rangeBitwiseAnd(5, 7)); // 4
        System.out.println(getSum(1, 2)); // 3
        System.out.println(reverseBits(43261596)); // 964176192
        System.out.println(validUtf8(new int[]{197,130,1})); // true
        System.out.println(readBinaryWatch(1)); // ["1:00","2:00",...]
        System.out.println(totalHammingDistance(new int[]{4,14,2})); // 6
        System.out.println(maxProduct(new String[]{"abcw","baz","foo","bar","xtfn","abcdef"})); // 16
    }
}
