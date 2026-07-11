import java.util.*;

/** DSA — Strings (LeetCode). Run: javac Strings.java && java Strings */
public class Strings {

    // LC #3 Longest Substring Without Repeating — sliding window O(n)
    public static int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> m = new HashMap<>();
        int max = 0, l = 0;
        for (int r = 0; r < s.length(); r++) {
            char c = s.charAt(r);
            if (m.containsKey(c) && m.get(c) >= l) l = m.get(c) + 1;
            m.put(c, r);
            max = Math.max(max, r - l + 1);
        }
        return max;
    }

    // LC #5 Longest Palindromic Substring — expand around center O(n^2)
    public static String longestPalindrome(String s) {
        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
            int a = expand(s, i, i), b = expand(s, i, i + 1);
            int len = Math.max(a, b);
            if (len > end - start) { start = i - (len - 1) / 2; end = i + len / 2; }
        }
        return s.substring(start, end + 1);
    }
    static int expand(String s, int l, int r) {
        while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) { l--; r++; }
        return r - l - 1;
    }

    // LC #20 Valid Parentheses — stack O(n)
    public static boolean isValid(String s) {
        Deque<Character> st = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') st.push(c);
            else { if (st.isEmpty()) return false;
                char t = st.pop();
                if ((c == ')' && t != '(') || (c == ']' && t != '[') || (c == '}' && t != '{')) return false;
            }
        }
        return st.isEmpty();
    }

    // LC #49 Group Anagrams — hash by sorted key O(n k log k)
    public static List<List<String>> groupAnagrams(String[] a) {
        Map<String, List<String>> m = new HashMap<>();
        for (String s : a) {
            char[] c = s.toCharArray(); java.util.Arrays.sort(c);
            m.computeIfAbsent(String.valueOf(c), k -> new ArrayList<>()).add(s);
        }
        return new ArrayList<>(m.values());
    }

    // LC #76 Minimum Window Substring — sliding window O(|s|+|t|)
    public static String minWindow(String s, String t) {
        int[] need = new int[128]; int req = 0;
        for (char c : t.toCharArray()) { if (need[c]++ == 0) req++; }
        int l = 0, have = 0, bestL = 0, best = Integer.MAX_VALUE;
        for (int r = 0; r < s.length(); r++) {
            if (need[s.charAt(r)]-- > 0) have++;
            while (have == req) {
                if (r - l + 1 < best) { best = r - l + 1; bestL = l; }
                if (++need[s.charAt(l++)] > 0) have--;
            }
        }
        return best == Integer.MAX_VALUE ? "" : s.substring(bestL, bestL + best);
    }

    // LC #14 Longest Common Prefix — vertical scan O(n*m)
    public static String longestCommonPrefix(String[] a) {
        if (a.length == 0) return "";
        for (int i = 0; i < a[0].length(); i++)
            for (int j = 1; j < a.length; j++)
                if (i >= a[j].length() || a[j].charAt(i) != a[0].charAt(i)) return a[0].substring(0, i);
        return a[0];
    }

    // LC #125 Valid Palindrome — two pointer O(n)
    public static boolean isPalindrome(String s) {
        int l = 0, r = s.length() - 1;
        while (l < r) {
            while (l < r && !Character.isLetterOrDigit(s.charAt(l))) l++;
            while (l < r && !Character.isLetterOrDigit(s.charAt(r))) r--;
            if (Character.toLowerCase(s.charAt(l++)) != Character.toLowerCase(s.charAt(r--))) return false;
        }
        return true;
    }

    // LC #387 First Unique Char — count then scan O(n)
    public static int firstUniqChar(String s) {
        int[] c = new int[26];
        for (char x : s.toCharArray()) c[x - 'a']++;
        for (int i = 0; i < s.length(); i++) if (c[s.charAt(i) - 'a'] == 1) return i;
        return -1;
    }

    // LC #344 Reverse String (in place)
    public static void reverseString(char[] a) {
        for (int l = 0, r = a.length - 1; l < r; l++, r--) { char t = a[l]; a[l] = a[r]; a[r] = t; }
    }

    // LC #242 Is Anagram
    public static boolean isAnagram(String a, String b) {
        if (a.length() != b.length()) return false;
        int[] c = new int[26];
        for (char x : a.toCharArray()) c[x - 'a']++;
        for (char x : b.toCharArray()) if (--c[x - 'a'] < 0) return false;
        return true;
    }

    // LC #43 Multiply Strings — Multiply two numbers as strings, O(m*n) time
    // Explanation: Multiply digit by digit, handle carry
    public static String multiply(String num1, String num2) {
        int m = num1.length(), n = num2.length(); int[] res = new int[m + n];
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                int mul = (num1.charAt(i) - '0') * (num2.charAt(j) - '0');
                int p1 = i + j, p2 = i + j + 1, sum = mul + res[p2];
                res[p2] = sum % 10; res[p1] += sum / 10;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int digit : res) if (!(sb.length() == 0 && digit == 0)) sb.append(digit);
        return sb.length() == 0 ? "0" : sb.toString();
    }

    // LC #151 Reverse Words in a String — O(n) time
    // Explanation: Split by whitespace, reverse array, join
    public static String reverseWords(String s) {
        String[] words = s.trim().split("\\s+");
        Collections.reverse(Arrays.asList(words));
        return String.join(" ", words);
    }

    // LC #165 Compare Version Numbers — O(n) time
    // Explanation: Split by '.', compare each revision numerically
    public static int compareVersion(String version1, String version2) {
        String[] v1 = version1.split("\\."), v2 = version2.split("\\.");
        for (int i = 0; i < Math.max(v1.length, v2.length); i++) {
            int n1 = i < v1.length ? Integer.parseInt(v1[i]) : 0;
            int n2 = i < v2.length ? Integer.parseInt(v2[i]) : 0;
            if (n1 != n2) return Integer.compare(n1, n2);
        }
        return 0;
    }

    // LC #443 String Compression — O(n) time
    // Explanation: Count consecutive characters, build compressed string
    public static int compress(char[] chars) {
        int index = 0, count = 1;
        for (int i = 1; i < chars.length; i++) {
            if (chars[i] == chars[i - 1]) count++;
            else { chars[index++] = chars[i - 1]; if (count > 1) for (char c : String.valueOf(count).toCharArray()) chars[index++] = c; count = 1; }
        }
        chars[index++] = chars[chars.length - 1]; if (count > 1) for (char c : String.valueOf(count).toCharArray()) chars[index++] = c;
        return index;
    }

    // LC #557 Reverse Words in a String III — O(n) time
    // Explanation: Reverse each word individually
    public static String reverseWordsIII(String s) {
        String[] words = s.split(" "); StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length; i++) { sb.append(new StringBuilder(words[i]).reverse()); if (i < words.length - 1) sb.append(" "); }
        return sb.toString();
    }

    // LC #686 Repeated String Match — O(n*m) time
    // Explanation: Build string until it can contain b, check if b is substring
    public static int repeatedStringMatch(String a, String b) {
        int count = 1; StringBuilder sb = new StringBuilder(a);
        while (sb.length() < b.length()) { sb.append(a); count++; }
        if (sb.indexOf(b) >= 0) return count;
        if (sb.append(a).indexOf(b) >= 0) return count + 1;
        return -1;
    }

    public static void main(String[] a) {
        System.out.println(lengthOfLongestSubstring("abcabcbb")); // 3
        System.out.println(longestPalindrome("babad"));        // bab
        System.out.println(isValid("()[]{}"));               // true
        System.out.println(longestCommonPrefix(new String[]{"flower","flow","flight"})); // fl
        System.out.println(isPalindrome("A man, a plan, a canal: Panama")); // true
        System.out.println(multiply("2", "3"));              // "6"
        System.out.println(reverseWords("the sky is blue"));   // "blue is sky the"
        System.out.println(compareVersion("1.01", "1.001"));  // 0
        System.out.println(reverseWordsIII("Let's take LeetCode contest")); // "s'teL ekat edoCteeL tsetnoc"
        System.out.println(repeatedStringMatch("abcd", "cdabcdab")); // 3
    }
}