import java.util.*;

/** DSA — Math (LeetCode). Each method has inline explanation.
 *  Run: javac Math.java && java MathQ */
public class MathQ {

    // LC #7 Reverse Integer — Reverse digits with overflow check, O(log n) time
    // Explanation: Extract digits from end, rebuild reversed number, check overflow
    public static int reverse(int x) {
        int rev = 0;
        while (x != 0) {
            int pop = x % 10; x /= 10; // extract last digit
            if (rev > Integer.MAX_VALUE / 10 || (rev == Integer.MAX_VALUE / 10 && pop > 7)) return 0; // overflow check
            if (rev < Integer.MIN_VALUE / 10 || (rev == Integer.MIN_VALUE / 10 && pop < -8)) return 0;
            rev = rev * 10 + pop; // build reversed number
        }
        return rev;
    }

    // LC #9 Palindrome Number — Check if number is palindrome, O(log n) time
    // Explanation: Reverse half of the number and compare with other half
    public static boolean isPalindrome(int x) {
        if (x < 0 || (x % 10 == 0 && x != 0)) return false; // negative or ending with 0
        int rev = 0;
        while (x > rev) { rev = rev * 10 + x % 10; x /= 10; } // reverse half
        return x == rev || x == rev / 10; // check for even or odd length
    }

    // LC #13 Roman to Integer — Convert roman numeral to integer, O(n) time
    // Explanation: Add values, subtract if smaller value precedes larger value
    public static int romanToInt(String s) {
        Map<Character, Integer> map = Map.of('I', 1, 'V', 5, 'X', 10, 'L', 50, 'C', 100, 'D', 500, 'M', 1000);
        int res = 0;
        for (int i = 0; i < s.length(); i++) {
            if (i + 1 < s.length() && map.get(s.charAt(i)) < map.get(s.charAt(i + 1)))
                res -= map.get(s.charAt(i)); // subtractive case
            else res += map.get(s.charAt(i)); // additive case
        }
        return res;
    }

    // LC #12 Integer to Roman — Convert integer to roman numeral, O(1) time
    // Explanation: Greedy approach, subtract largest possible value
    public static String intToRoman(int num) {
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) { sb.append(symbols[i]); num -= values[i]; } // greedy subtraction
        }
        return sb.toString();
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

    // LC #50 Pow(x, n) — Calculate x^n, O(log n) time
    // Explanation: Binary exponentiation: x^n = x^(n/2) * x^(n/2) if n even, else x * x^(n-1)
    public static double myPow(double x, int n) {
        long N = n; // handle overflow
        if (N < 0) { x = 1 / x; N = -N; }
        double res = 1;
        while (N > 0) {
            if ((N & 1) == 1) res *= x; // if N is odd, multiply by x
            x *= x; N >>= 1; // square x and halve N
        }
        return res;
    }

    // LC #29 Divide Two Integers — Divide without *, /, %, O(log n) time
    // Explanation: Subtract powers of 2 times divisor from dividend
    public static int divide(int dividend, int divisor) {
        if (dividend == Integer.MIN_VALUE && divisor == -1) return Integer.MAX_VALUE;
        boolean negative = (dividend < 0) ^ (divisor < 0);
        long dvd = Math.abs((long) dividend), dvs = Math.abs((long) divisor), res = 0;
        while (dvd >= dvs) {
            long temp = dvs, count = 1;
            while (dvd >= (temp << 1)) { temp <<= 1; count <<= 1; } // double temp
            dvd -= temp; res += count;
        }
        return negative ? (int) -res : (int) res;
    }

    // LC #172 Factorial Trailing Zeroes — Count trailing zeros in n!, O(log n) time
    // Explanation: Count factors of 5 in n! (since 10 = 2*5, and 2s are more abundant)
    public static int trailingZeroes(int n) {
        int count = 0;
        while (n > 0) { n /= 5; count += n; } // count multiples of 5, 25, 125, etc.
        return count;
    }

    // LC #202 Happy Number — Check if number is happy, O(log n) time
    // Explanation: Use Floyd's cycle detection to detect loops
    public static boolean isHappy(int n) {
        int slow = n, fast = n;
        do {
            slow = squareSum(slow); // move slow by 1
            fast = squareSum(squareSum(fast)); // move fast by 2
        } while (slow != fast);
        return slow == 1;
    }
    static int squareSum(int n) {
        int sum = 0;
        while (n > 0) { int d = n % 10; sum += d * d; n /= 10; }
        return sum;
    }

    // LC #204 Count Primes — Count primes less than n, O(n log log n) time
    // Explanation: Sieve of Eratosthenes - mark multiples of each prime
    public static int countPrimes(int n) {
        if (n < 3) return 0;
        boolean[] isPrime = new boolean[n];
        for (int i = 2; i < n; i++) isPrime[i] = true;
        for (int i = 2; i * i < n; i++) {
            if (isPrime[i]) {
                for (int j = i * i; j < n; j += i) isPrime[j] = false; // mark multiples
            }
        }
        int count = 0; for (boolean p : isPrime) if (p) count++;
        return count;
    }

    // LC #149 Max Points on a Line — Maximum points on same line, O(n^2) time
    // Explanation: For each point, calculate slope with all other points, use hashmap
    public static int maxPoints(int[][] points) {
        if (points.length < 3) return points.length;
        int max = 0;
        for (int i = 0; i < points.length; i++) {
            Map<String, Integer> slopeCount = new HashMap<>();
            int duplicate = 1, curMax = 0;
            for (int j = i + 1; j < points.length; j++) {
                int dx = points[j][0] - points[i][0], dy = points[j][1] - points[i][1];
                if (dx == 0 && dy == 0) { duplicate++; continue; } // same point
                int gcd = gcd(dx, dy); dx /= gcd; dy /= gcd; // reduce to simplest form
                String key = dx + "/" + dy; // use string as key for slope
                slopeCount.put(key, slopeCount.getOrDefault(key, 0) + 1);
                curMax = Math.max(curMax, slopeCount.get(key));
            }
            max = Math.max(max, curMax + duplicate);
        }
        return max;
    }
    static int gcd(int a, int b) { return b == 0 ? a : gcd(b, a % b); }

    // LC #365 Water and Jug Problem — Can measure z liters using x and jugs, O(1) time
    // Explanation: z must be multiple of gcd(x, y) and <= x + y
    public static boolean canMeasureWater(int x, int y, int z) {
        if (z == 0) return true;
        if (x + y < z) return false;
        return z % gcd(x, y) == 0;
    }

    // LC #67 Add Binary — Add two binary strings, O(max(m,n)) time
    // Explanation: Add bit by bit from right to left with carry
    public static String addBinary(String a, String b) {
        StringBuilder sb = new StringBuilder();
        int i = a.length() - 1, j = b.length() - 1, carry = 0;
        while (i >= 0 || j >= 0 || carry > 0) {
            int sum = carry;
            if (i >= 0) sum += a.charAt(i--) - '0';
            if (j >= 0) sum += b.charAt(j--) - '0';
            sb.append(sum % 2); // append current bit
            carry = sum / 2; // update carry
        }
        return sb.reverse().toString();
    }

    // LC #43 Multiply Strings — Multiply two numbers as strings, O(m*n) time
    // Explanation: Multiply digit by digit, store results in array, handle carry
    public static String multiply(String num1, String num2) {
        int m = num1.length(), n = num2.length();
        int[] res = new int[m + n];
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                int mul = (num1.charAt(i) - '0') * (num2.charAt(j) - '0');
                int p1 = i + j, p2 = i + j + 1; // positions in result array
                int sum = mul + res[p2]; // add to existing value
                res[p2] = sum % 10; // current digit
                res[p1] += sum / 10; // carry
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int digit : res) if (!(sb.length() == 0 && digit == 0)) sb.append(digit);
        return sb.length() == 0 ? "0" : sb.toString();
    }

    // LC #66 Plus One — Add 1 to number represented as array, O(n) time
    // Explanation: Add 1 from rightmost digit, handle carry propagation
    public static int[] plusOne(int[] digits) {
        for (int i = digits.length - 1; i >= 0; i--) {
            if (digits[i] < 9) { digits[i]++; return digits; } // no carry needed
            digits[i] = 0; // set to 0 and continue carry
        }
        int[] res = new int[digits.length + 1]; res[0] = 1; // all digits were 9
        return res;
    }

    // LC #8 String to Integer (atoi) — Convert string to integer, O(n) time
    // Explanation: Handle whitespace, sign, digits, and overflow
    public static int myAtoi(String s) {
        int i = 0, n = s.length(), sign = 1, res = 0;
        while (i < n && s.charAt(i) == ' ') i++; // skip whitespace
        if (i < n && (s.charAt(i) == '+' || s.charAt(i) == '-')) { // handle sign
            sign = s.charAt(i) == '+' ? 1 : -1; i++;
        }
        while (i < n && Character.isDigit(s.charAt(i))) {
            int digit = s.charAt(i) - '0';
            if (res > Integer.MAX_VALUE / 10 || (res == Integer.MAX_VALUE / 10 && digit > 7))
                return sign == 1 ? Integer.MAX_VALUE : Integer.MIN_VALUE; // overflow
            res = res * 10 + digit; i++;
        }
        return res * sign;
    }

    // LC #415 Add Strings — Add two numbers as strings, O(max(m,n)) time
    // Explanation: Add digit by digit from right to left with carry
    public static String addStrings(String num1, String num2) {
        StringBuilder sb = new StringBuilder();
        int i = num1.length() - 1, j = num2.length() - 1, carry = 0;
        while (i >= 0 || j >= 0 || carry > 0) {
            int sum = carry;
            if (i >= 0) sum += num1.charAt(i--) - '0';
            if (j >= 0) sum += num2.charAt(j--) - '0';
            sb.append(sum % 10); carry = sum / 10;
        }
        return sb.reverse().toString();
    }

    // LC #223 Rectangle Area — Total area covered by two rectangles, O(1) time
    // Explanation: Sum of areas minus overlap area
    public static int computeArea(int ax1, int ay1, int ax2, int ay2, int bx1, int by1, int bx2, int by2) {
        int area1 = (ax2 - ax1) * (ay2 - ay1), area2 = (bx2 - bx1) * (by2 - by1);
        int overlapWidth = Math.min(ax2, bx2) - Math.max(ax1, bx1);
        int overlapHeight = Math.min(ay2, by2) - Math.max(ay1, by1);
        int overlapArea = (overlapWidth > 0 && overlapHeight > 0) ? overlapWidth * overlapHeight : 0;
        return area1 + area2 - overlapArea;
    }

    // LC #227 Basic Calculator II — Calculate expression with +,-,*,/, O(n) time
    // Explanation: Use stack to handle * and / before + and -
    public static int calculateII(String s) {
        Stack<Integer> stack = new Stack<>();
        int num = 0, n = s.length();
        char sign = '+';
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) num = num * 10 + (c - '0'); // build number
            if ((!Character.isDigit(c) && c != ' ') || i == n - 1) {
                if (sign == '+') stack.push(num);
                else if (sign == '-') stack.push(-num);
                else if (sign == '*') stack.push(stack.pop() * num);
                else if (sign == '/') stack.push(stack.pop() / num);
                sign = c; num = 0;
            }
        }
        int res = 0; while (!stack.isEmpty()) res += stack.pop();
        return res;
    }

    public static void main(String[] a) {
        System.out.println(reverse(123)); // 321
        System.out.println(isPalindrome(121)); // true
        System.out.println(romanToInt("MCMXCIV")); // 1994
        System.out.println(intToRoman(1994)); // MCMXCIV
        System.out.println(mySqrt(8)); // 2
        System.out.println(myPow(2.0, 10)); // 1024.0
        System.out.println(divide(10, 3)); // 3
        System.out.println(trailingZeroes(5)); // 1
        System.out.println(isHappy(19)); // true
        System.out.println(countPrimes(10)); // 4
        System.out.println(addBinary("11", "1")); // "100"
        System.out.println(multiply("2", "3")); // "6"
        System.out.println(Arrays.toString(plusOne(new int[]{1,2,3}))); // [1,2,4]
        System.out.println(myAtoi("42")); // 42
        System.out.println(addStrings("123", "456")); // "579"
        System.out.println(calculateII("3+2*2")); // 7
    }
}
