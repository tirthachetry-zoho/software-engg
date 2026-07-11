import java.util.*;

/** DSA — Recursion (LeetCode). Each method has inline explanation.
 *  Run: javac Recursion.java && java Recursion */
public class Recursion {

    // LC #509 Fibonacci Number — Recursive with memoization, O(n) time
    // Explanation: F(n) = F(n-1) + F(n-2), use memo to avoid redundant calls
    public static int fib(int n) {
        return fibHelper(n, new HashMap<>());
    }
    static int fibHelper(int n, Map<Integer, Integer> memo) {
        if (n <= 1) return n;
        if (memo.containsKey(n)) return memo.get(n);
        int res = fibHelper(n - 1, memo) + fibHelper(n - 2, memo);
        memo.put(n, res);
        return res;
    }

    // LC #70 Climbing Stairs — Recursive with memoization, O(n) time
    // Explanation: Ways(n) = Ways(n-1) + Ways(n-2), can reach step n from n-1 or n-2
    public static int climbStairs(int n) {
        return climbHelper(n, new HashMap<>());
    }
    static int climbHelper(int n, Map<Integer, Integer> memo) {
        if (n <= 2) return n;
        if (memo.containsKey(n)) return memo.get(n);
        int res = climbHelper(n - 1, memo) + climbHelper(n - 2, memo);
        memo.put(n, res);
        return res;
    }

    // LC #22 Generate Parentheses — Generate all valid combinations, O(4^n/sqrt(n)) time
    // Explanation: Add '(' if count < n, add ')' if close < open
    public static List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<>();
        generateHelper(n, 0, 0, new StringBuilder(), res);
        return res;
    }
    static void generateHelper(int n, int open, int close, StringBuilder sb, List<String> res) {
        if (sb.length() == 2 * n) { res.add(sb.toString()); return; }
        if (open < n) { sb.append('('); generateHelper(n, open + 1, close, sb, res); sb.deleteCharAt(sb.length() - 1); }
        if (close < open) { sb.append(')'); generateHelper(n, open, close + 1, sb, res); sb.deleteCharAt(sb.length() - 1); }
    }

    // LC #78 Subsets — Generate all subsets, O(2^n) time
    // Explanation: For each element, either include it or exclude it
    public static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        subsetHelper(nums, 0, new ArrayList<>(), res);
        return res;
    }
    static void subsetHelper(int[] nums, int idx, List<Integer> cur, List<List<Integer>> res) {
        if (idx == nums.length) { res.add(new ArrayList<>(cur)); return; }
        cur.add(nums[idx]); subsetHelper(nums, idx + 1, cur, res); cur.remove(cur.size() - 1); // include
        subsetHelper(nums, idx + 1, cur, res); // exclude
    }

    // LC #46 Permutations — Generate all permutations, O(n! * n) time
    // Explanation: Swap elements to generate all arrangements
    public static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        permuteHelper(nums, 0, res);
        return res;
    }
    static void permuteHelper(int[] nums, int idx, List<List<Integer>> res) {
        if (idx == nums.length) { List<Integer> perm = new ArrayList<>(); for (int n : nums) perm.add(n); res.add(perm); return; }
        for (int i = idx; i < nums.length; i++) { swap(nums, idx, i); permuteHelper(nums, idx + 1, res); swap(nums, idx, i); }
    }
    static void swap(int[] nums, int i, int j) { int t = nums[i]; nums[i] = nums[j]; nums[j] = t; }

    // LC #47 Permutations II — Permutations with duplicates, O(n! * n) time
    // Explanation: Skip duplicates by checking if element was used at this position
    public static List<List<Integer>> permuteUnique(int[] nums) {
        Arrays.sort(nums); List<List<Integer>> res = new ArrayList<>();
        permuteUniqueHelper(nums, 0, res);
        return res;
    }
    static void permuteUniqueHelper(int[] nums, int idx, List<List<Integer>> res) {
        if (idx == nums.length) { List<Integer> perm = new ArrayList<>(); for (int n : nums) perm.add(n); res.add(perm); return; }
        Set<Integer> used = new HashSet<>();
        for (int i = idx; i < nums.length; i++) {
            if (used.contains(nums[i])) continue; used.add(nums[i]);
            swap(nums, idx, i); permuteUniqueHelper(nums, idx + 1, res); swap(nums, idx, i);
        }
    }

    // LC #77 Combinations — Generate all k combinations, O(C(n,k)) time
    // Explanation: Choose k elements from n, backtrack after each choice
    public static List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> res = new ArrayList<>();
        combineHelper(n, k, 1, new ArrayList<>(), res);
        return res;
    }
    static void combineHelper(int n, int k, int start, List<Integer> cur, List<List<Integer>> res) {
        if (cur.size() == k) { res.add(new ArrayList<>(cur)); return; }
        for (int i = start; i <= n; i++) {
            cur.add(i); combineHelper(n, k, i + 1, cur, res); cur.remove(cur.size() - 1);
        }
    }

    // LC #39 Combination Sum — Combinations that sum to target, O(2^n) time
    // Explanation: Try each number, can reuse same number
    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(candidates); comboSumHelper(candidates, target, 0, new ArrayList<>(), res);
        return res;
    }
    static void comboSumHelper(int[] nums, int target, int idx, List<Integer> cur, List<List<Integer>> res) {
        if (target == 0) { res.add(new ArrayList<>(cur)); return; }
        for (int i = idx; i < nums.length; i++) {
            if (nums[i] > target) break;
            cur.add(nums[i]); comboSumHelper(nums, target - nums[i], i, cur, res); cur.remove(cur.size() - 1);
        }
    }

    // LC #40 Combination Sum II — Combinations with duplicates, O(2^n) time
    // Explanation: Similar to #39 but skip duplicates
    public static List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(candidates); comboSum2Helper(candidates, target, 0, new ArrayList<>(), res);
        return res;
    }
    static void comboSum2Helper(int[] nums, int target, int idx, List<Integer> cur, List<List<Integer>> res) {
        if (target == 0) { res.add(new ArrayList<>(cur)); return; }
        for (int i = idx; i < nums.length; i++) {
            if (i > idx && nums[i] == nums[i - 1]) continue; // skip duplicates
            if (nums[i] > target) break;
            cur.add(nums[i]); comboSum2Helper(nums, target - nums[i], i + 1, cur, res); cur.remove(cur.size() - 1);
        }
    }

    // LC #216 Combination Sum III — k numbers that sum to n, 1-9 only, O(C(9,k)) time
    // Explanation: Choose k distinct numbers from 1-9 that sum to n
    public static List<List<Integer>> combinationSum3(int k, int n) {
        List<List<Integer>> res = new ArrayList<>();
        comboSum3Helper(k, n, 1, new ArrayList<>(), res);
        return res;
    }
    static void comboSum3Helper(int k, int n, int start, List<Integer> cur, List<List<Integer>> res) {
        if (cur.size() == k && n == 0) { res.add(new ArrayList<>(cur)); return; }
        if (cur.size() >= k || n <= 0) return;
        for (int i = start; i <= 9; i++) {
            cur.add(i); comboSum3Helper(k, n - i, i + 1, cur, res); cur.remove(cur.size() - 1);
        }
    }

    // LC #17 Letter Combinations of a Phone Number, O(4^n) time
    // Explanation: Map digits to letters, recursively build combinations
    public static List<String> letterCombinations(String digits) {
        if (digits.isEmpty()) return new ArrayList<>();
        String[] mapping = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};
        List<String> res = new ArrayList<>();
        letterHelper(digits, 0, mapping, new StringBuilder(), res);
        return res;
    }
    static void letterHelper(String digits, int idx, String[] mapping, StringBuilder sb, List<String> res) {
        if (idx == digits.length()) { res.add(sb.toString()); return; }
        for (char c : mapping[digits.charAt(idx) - '0'].toCharArray()) {
            sb.append(c); letterHelper(digits, idx + 1, mapping, sb, res); sb.deleteCharAt(sb.length() - 1);
        }
    }

    // LC #79 Word Search — Find word in grid, O(m*n*4^L) time
    // Explanation: DFS from each cell, backtrack if path invalid
    public static boolean exist(char[][] board, String word) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (wordSearchHelper(board, word, i, j, 0)) return true;
            }
        }
        return false;
    }
    static boolean wordSearchHelper(char[][] board, String word, int i, int j, int idx) {
        if (idx == word.length()) return true;
        if (i < 0 || j < 0 || i >= board.length || j >= board[0].length || board[i][j] != word.charAt(idx)) return false;
        char temp = board[i][j]; board[i][j] = '#';
        boolean found = wordSearchHelper(board, word, i + 1, j, idx + 1) || wordSearchHelper(board, word, i - 1, j, idx + 1) ||
                        wordSearchHelper(board, word, i, j + 1, idx + 1) || wordSearchHelper(board, word, i, j - 1, idx + 1);
        board[i][j] = temp;
        return found;
    }

    // LC #51 N-Queens — Place n queens on n×n board, O(n!) time
    // Explanation: Place queens row by row, check conflicts
    public static List<List<String>> solveNQueens(int n) {
        List<List<String>> res = new ArrayList<>();
        char[][] board = new char[n][n]; for (char[] row : board) Arrays.fill(row, '.');
        nQueensHelper(board, 0, res);
        return res;
    }
    static void nQueensHelper(char[][] board, int row, List<List<String>> res) {
        if (row == board.length) { res.add(constructBoard(board)); return; }
        for (int col = 0; col < board.length; col++) {
            if (isValid(board, row, col)) { board[row][col] = 'Q'; nQueensHelper(board, row + 1, res); board[row][col] = '.'; }
        }
    }
    static boolean isValid(char[][] board, int row, int col) {
        for (int i = 0; i < row; i++) if (board[i][col] == 'Q') return false; // check column
        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) if (board[i][j] == 'Q') return false; // check diagonal
        for (int i = row - 1, j = col + 1; i >= 0 && j < board.length; i--, j++) if (board[i][j] == 'Q') return false;
        return true;
    }
    static List<String> constructBoard(char[][] board) {
        List<String> res = new ArrayList<>(); for (char[] row : board) res.add(new String(row)); return res;
    }

    // LC #52 N-Queens II — Count solutions, O(n!) time
    // Explanation: Same as #51 but just count
    public static int totalNQueens(int n) {
        return nQueensCountHelper(n, 0, 0, 0, 0);
    }
    static int nQueensCountHelper(int n, int row, int cols, int diag1, int diag2) {
        if (row == n) return 1;
        int count = 0;
        for (int col = 0; col < n; col++) {
            int d1 = row - col + n - 1, d2 = row + col;
            if ((cols & (1 << col)) == 0 && (diag1 & (1 << d1)) == 0 && (diag2 & (1 << d2)) == 0) {
                count += nQueensCountHelper(n, row + 1, cols | (1 << col), diag1 | (1 << d1), diag2 | (1 << d2));
            }
        }
        return count;
    }

    // LC #37 Sudoku Solver — Solve sudoku puzzle, O(9^(n*n)) time
    // Explanation: Try digits 1-9 in empty cells, backtrack if invalid
    public static void solveSudoku(char[][] board) {
        sudokuHelper(board);
    }
    static boolean sudokuHelper(char[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == '.') {
                    for (char c = '1'; c <= '9'; c++) {
                        if (isValidSudoku(board, i, j, c)) { board[i][j] = c; if (sudokuHelper(board)) return true; board[i][j] = '.'; }
                    }
                    return false;
                }
            }
        }
        return true;
    }
    static boolean isValidSudoku(char[][] board, int row, int col, char c) {
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == c) return false; // check column
            if (board[row][i] == c) return false; // check row
            if (board[3 * (row / 3) + i / 3][3 * (col / 3) + i % 3] == c) return false; // check 3x3 box
        }
        return true;
    }

    // LC #131 Palindrome Partitioning — Partition string into palindromes, O(n*2^n) time
    // Explanation: Try all partitions, check if each part is palindrome
    public static List<List<String>> partition(String s) {
        List<List<String>> res = new ArrayList<>();
        partitionHelper(s, 0, new ArrayList<>(), res);
        return res;
    }
    static void partitionHelper(String s, int idx, List<String> cur, List<List<String>> res) {
        if (idx == s.length()) { res.add(new ArrayList<>(cur)); return; }
        for (int i = idx; i < s.length(); i++) {
            if (isPalindrome(s, idx, i)) { cur.add(s.substring(idx, i + 1)); partitionHelper(s, i + 1, cur, res); cur.remove(cur.size() - 1); }
        }
    }
    static boolean isPalindrome(String s, int lo, int hi) {
        while (lo < hi) if (s.charAt(lo++) != s.charAt(hi--)) return false;
        return true;
    }

    // LC #93 Restore IP Addresses — Generate valid IP addresses, O(3^n) time
    // Explanation: Try placing dots at valid positions
    public static List<String> restoreIpAddresses(String s) {
        List<String> res = new ArrayList<>();
        ipHelper(s, 0, new ArrayList<>(), res);
        return res;
    }
    static void ipHelper(String s, int idx, List<String> parts, List<String> res) {
        if (parts.size() == 4 && idx == s.length()) { res.add(String.join(".", parts)); return; }
        if (parts.size() >= 4 || idx >= s.length()) return;
        for (int len = 1; len <= 3 && idx + len <= s.length(); len++) {
            String part = s.substring(idx, idx + len);
            if (isValidIPPart(part)) { parts.add(part); ipHelper(s, idx + len, parts, res); parts.remove(parts.size() - 1); }
        }
    }
    static boolean isValidIPPart(String s) {
        if (s.length() > 1 && s.charAt(0) == '0') return false;
        int val = Integer.parseInt(s); return val >= 0 && val <= 255;
    }

    // LC #241 Different Ways to Add Parentheses — All possible results, O(2^n) time
    // Explanation: Split at each operator, recursively compute left and right
    public static List<Integer> diffWaysToCompute(String expression) {
        return computeHelper(expression, new HashMap<>());
    }
    static List<Integer> computeHelper(String expr, Map<String, List<Integer>> memo) {
        if (memo.containsKey(expr)) return memo.get(expr);
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '+' || c == '-' || c == '*') {
                List<Integer> left = computeHelper(expr.substring(0, i), memo);
                List<Integer> right = computeHelper(expr.substring(i + 1), memo);
                for (int l : left) for (int r : right) {
                    if (c == '+') res.add(l + r); else if (c == '-') res.add(l - r); else res.add(l * r);
                }
            }
        }
        if (res.isEmpty()) res.add(Integer.parseInt(expr));
        memo.put(expr, res);
        return res;
    }

    // LC #491 Increasing Subsequences — Find all increasing subsequences, O(2^n) time
    // Explanation: Similar to subsets but ensure increasing order
    public static List<List<Integer>> findSubsequences(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        incSubseqHelper(nums, 0, new ArrayList<>(), res);
        return res;
    }
    static void incSubseqHelper(int[] nums, int idx, List<Integer> cur, List<List<Integer>> res) {
        if (cur.size() >= 2) res.add(new ArrayList<>(cur));
        Set<Integer> used = new HashSet<>();
        for (int i = idx; i < nums.length; i++) {
            if (used.contains(nums[i])) continue; used.add(nums[i]);
            if (cur.isEmpty() || nums[i] >= cur.get(cur.size() - 1)) {
                cur.add(nums[i]); incSubseqHelper(nums, i + 1, cur, res); cur.remove(cur.size() - 1);
            }
        }
    }

    // LC #306 Additive Number — Check if number is additive, O(3^n) time
    // Explanation: Try all possible first two numbers, check rest
    public static boolean isAdditiveNumber(String num) {
        return additiveHelper(num, 0, 0, 0, 0);
    }
    static boolean additiveHelper(String num, int idx, long prev1, long prev2, int count) {
        if (idx == num.length()) return count >= 3;
        for (int i = idx; i < num.length(); i++) {
            if (i > idx && num.charAt(idx) == '0') break; // no leading zeros
            long curr = Long.parseLong(num.substring(idx, i + 1));
            if (count >= 2 && curr != prev1 + prev2) continue;
            if (additiveHelper(num, i + 1, prev2, curr, count + 1)) return true;
        }
        return false;
    }

    public static void main(String[] a) {
        System.out.println(fib(10)); // 55
        System.out.println(climbStairs(5)); // 8
        System.out.println(generateParenthesis(3).size()); // 5
        System.out.println(subsets(new int[]{1,2,3}).size()); // 8
        System.out.println(permute(new int[]{1,2,3}).size()); // 6
        System.out.println(permuteUnique(new int[]{1,1,2}).size()); // 2
        System.out.println(combine(4, 2).size()); // 6
        System.out.println(combinationSum(new int[]{2,3,6,7}, 7).size()); // 2
        System.out.println(combinationSum2(new int[]{10,1,2,7,6,1,5}, 8).size()); // 4
        System.out.println(combinationSum3(3, 7).size()); // 1
        System.out.println(letterCombinations("23").size()); // 9
        System.out.println(exist(new char[][]{{'A','B','C','E'},{'S','F','C','S'},{'A','D','E','E'}}, "ABCCED")); // true
        System.out.println(solveNQueens(4).size()); // 2
        System.out.println(totalNQueens(4)); // 2
        System.out.println(partition("aab").size()); // 2
        System.out.println(restoreIpAddresses("25525511135").size()); // 2
        System.out.println(diffWaysToCompute("2-1-1").size()); // 2
        System.out.println(findSubsequences(new int[]{4,6,7,7}).size()); // 8
        System.out.println(isAdditiveNumber("112358")); // true
    }
}
