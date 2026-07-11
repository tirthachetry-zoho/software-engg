import java.util.*;

/** DSA — Backtracking & Misc (LeetCode). Run: javac Backtracking.java && java Backtracking */
public class Backtracking {

    // LC #46 Permutations — O(n * n!)
    public static List<List<Integer>> permute(int[] a) {
        List<List<Integer>> res = new ArrayList<>(); back(a, 0, res); return res;
    }
    static void back(int[] a, int i, List<List<Integer>> res) {
        if (i == a.length) { List<Integer> l = new ArrayList<>(); for (int x : a) l.add(x); res.add(l); return; }
        for (int j = i; j < a.length; j++) { swap(a, i, j); back(a, i + 1, res); swap(a, i, j); }
    }
    static void swap(int[] a, int i, int j) { int t = a[i]; a[i] = a[j]; a[j] = t; }

    // LC #78 Subsets — O(2^n)
    public static List<List<Integer>> subsets(int[] a) {
        List<List<Integer>> res = new ArrayList<>(); sub(a, 0, new ArrayList<>(), res); return res;
    }
    static void sub(int[] a, int i, List<Integer> cur, List<List<Integer>> res) {
        if (i == a.length) { res.add(new ArrayList<>(cur)); return; }
        cur.add(a[i]); sub(a, i + 1, cur, res); cur.remove(cur.size() - 1); sub(a, i + 1, cur, res);
    }

    // LC #39 Combination Sum — O(2^n)
    public static List<List<Integer>> comboSum(int[] a, int t) {
        List<List<Integer>> res = new ArrayList<>(); Arrays.sort(a); combo(a, t, 0, new ArrayList<>(), res); return res;
    }
    static void combo(int[] a, int t, int i, List<Integer> cur, List<List<Integer>> res) {
        if (t == 0) { res.add(new ArrayList<>(cur)); return; }
        for (int j = i; j < a.length; j++) { if (a[j] > t) break; cur.add(a[j]); combo(a, t - a[j], j, cur, res); cur.remove(cur.size() - 1); }
    }

    // LC #17 Letter Combinations of a Phone Number
    public static List<String> letterCombo(String d) {
        if (d.isEmpty()) return new ArrayList<>();
        String[] m = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};
        List<String> res = new ArrayList<>(); lc(d, 0, m, new StringBuilder(), res); return res;
    }
    static void lc(String d, int i, String[] m, StringBuilder sb, List<String> res) {
        if (i == d.length()) { res.add(sb.toString()); return; }
        for (char c : m[d.charAt(i) - '0'].toCharArray()) { sb.append(c); lc(d, i + 1, m, sb, res); sb.deleteCharAt(sb.length() - 1); }
    }

    // LC #79 Word Search — O(m*n*4^L)
    public static boolean wordSearch(char[][] b, String w) {
        for (int i = 0; i < b.length; i++) for (int j = 0; j < b[0].length; j++) if (dfs(b, w, i, j, 0)) return true;
        return false;
    }
    static boolean dfs(char[][] b, String w, int i, int j, int k) {
        if (k == w.length()) return true;
        if (i < 0 || j < 0 || i >= b.length || j >= b[0].length || b[i][j] != w.charAt(k)) return false;
        char t = b[i][j]; b[i][j] = '#';
        boolean f = dfs(b, w, i + 1, j, k + 1) || dfs(b, w, i - 1, j, k + 1) || dfs(b, w, i, j + 1, k + 1) || dfs(b, w, i, j - 1, k + 1);
        b[i][j] = t; return f;
    }

    // LC #22 Generate Parentheses — O(2^n)
    public static List<String> genParen(int n) {
        List<String> res = new ArrayList<>(); gp(n, 0, 0, new StringBuilder(), res); return res;
    }
    static void gp(int n, int o, int c, StringBuilder sb, List<String> res) {
        if (sb.length() == 2 * n) { res.add(sb.toString()); return; }
        if (o < n) { sb.append('('); gp(n, o + 1, c, sb, res); sb.deleteCharAt(sb.length() - 1); }
        if (c < o) { sb.append(')'); gp(n, o, c + 1, sb, res); sb.deleteCharAt(sb.length() - 1); }
    }

    // LC #130 Surround Regions (capture surrounded X) — BFS/DFS from border
    public static void solve(char[][] b) {
        int m = b.length, n = b[0].length;
        for (int i = 0; i < m; i++) { dfs(b, i, 0); dfs(b, i, n - 1); }
        for (int j = 0; j < n; j++) { dfs(b, 0, j); dfs(b, m - 1, j); }
        for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) b[i][j] = (b[i][j] == 'T') ? 'O' : 'X';
    }
    static void dfs(char[][] b, int i, int j) {
        if (i < 0 || j < 0 || i >= b.length || j >= b[0].length || b[i][j] != 'O') return;
        b[i][j] = 'T'; dfs(b, i + 1, j); dfs(b, i - 1, j); dfs(b, i, j + 1); dfs(b, i, j - 1);
    }

    // LC #200 Number of Islands — Count islands in grid, O(m*n) time
    // Explanation: DFS/BFS to mark visited cells
    public static int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1') { dfsIslands(grid, i, j); count++; }
            }
        }
        return count;
    }
    static void dfsIslands(char[][] grid, int i, int j) {
        if (i < 0 || j < 0 || i >= grid.length || j >= grid[0].length || grid[i][j] != '1') return;
        grid[i][j] = '0'; dfsIslands(grid, i + 1, j); dfsIslands(grid, i - 1, j); dfsIslands(grid, i, j + 1); dfsIslands(grid, i, j - 1);
    }

    // LC #417 Pacific Atlantic Water Flow — Cells flowing to both oceans, O(m*n) time
    // Explanation: DFS from both oceans, find intersection
    public static List<List<Integer>> pacificAtlantic(int[][] heights) {
        List<List<Integer>> res = new ArrayList<>(); if (heights == null || heights.length == 0) return res;
        int m = heights.length, n = heights[0].length;
        boolean[][] pacific = new boolean[m][n], atlantic = new boolean[m][n];
        for (int i = 0; i < m; i++) { dfsWater(heights, pacific, i, 0); dfsWater(heights, atlantic, i, n - 1); }
        for (int j = 0; j < n; j++) { dfsWater(heights, pacific, 0, j); dfsWater(heights, atlantic, m - 1, j); }
        for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) if (pacific[i][j] && atlantic[i][j]) res.add(Arrays.asList(i, j));
        return res;
    }
    static void dfsWater(int[][] heights, boolean[][] visited, int i, int j) {
        if (i < 0 || j < 0 || i >= heights.length || j >= heights[0].length || visited[i][j]) return;
        visited[i][j] = true;
        if (i > 0 && heights[i - 1][j] >= heights[i][j]) dfsWater(heights, visited, i - 1, j);
        if (i < heights.length - 1 && heights[i + 1][j] >= heights[i][j]) dfsWater(heights, visited, i + 1, j);
        if (j > 0 && heights[i][j - 1] >= heights[i][j]) dfsWater(heights, visited, i, j - 1);
        if (j < heights[0].length - 1 && heights[i][j + 1] >= heights[i][j]) dfsWater(heights, visited, i, j + 1);
    }

    // LC #133 Clone Graph — Deep copy of graph, O(V+E) time
    // Explanation: BFS with hashmap for cloned nodes
    static class GraphNode { int val; List<GraphNode> neighbors; GraphNode(int v) { val = v; neighbors = new ArrayList<>(); } }
    public static GraphNode cloneGraph(GraphNode node) {
        if (node == null) return null;
        Map<GraphNode, GraphNode> visited = new HashMap<>(); Queue<GraphNode> q = new LinkedList<>();
        q.offer(node); visited.put(node, new GraphNode(node.val));
        while (!q.isEmpty()) {
            GraphNode curr = q.poll();
            for (GraphNode neighbor : curr.neighbors) {
                if (!visited.containsKey(neighbor)) { visited.put(neighbor, new GraphNode(neighbor.val)); q.offer(neighbor); }
                visited.get(curr).neighbors.add(visited.get(neighbor));
            }
        }
        return visited.get(node);
    }

    // LC #207 Course Schedule — Can finish all courses (detect cycle), O(V+E) time
    // Explanation: Topological sort using Kahn's algorithm
    public static boolean canFinish(int numCourses, int[][] prerequisites) {
        List<List<Integer>> adj = new ArrayList<>(); for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());
        int[] indegree = new int[numCourses];
        for (int[] p : prerequisites) { adj.get(p[1]).add(p[0]); indegree[p[0]]++; }
        Queue<Integer> q = new LinkedList<>(); for (int i = 0; i < numCourses; i++) if (indegree[i] == 0) q.offer(i);
        int count = 0;
        while (!q.isEmpty()) { int curr = q.poll(); count++; for (int next : adj.get(curr)) if (--indegree[next] == 0) q.offer(next); }
        return count == numCourses;
    }

    // LC #210 Course Schedule II — Return course order, O(V+E) time
    // Explanation: Topological sort, return order if possible
    public static int[] findOrder(int numCourses, int[][] prerequisites) {
        List<List<Integer>> adj = new ArrayList<>(); for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());
        int[] indegree = new int[numCourses];
        for (int[] p : prerequisites) { adj.get(p[1]).add(p[0]); indegree[p[0]]++; }
        Queue<Integer> q = new LinkedList<>(); for (int i = 0; i < numCourses; i++) if (indegree[i] == 0) q.offer(i);
        int[] order = new int[numCourses]; int idx = 0;
        while (!q.isEmpty()) { int curr = q.poll(); order[idx++] = curr; for (int next : adj.get(curr)) if (--indegree[next] == 0) q.offer(next); }
        return idx == numCourses ? order : new int[0];
    }

    // LC #64 Minimum Path Sum (DP grid) — O(mn)
    public static int minPathSum(int[][] g) {
        int m = g.length, n = g[0].length;
        for (int i = 1; i < m; i++) g[i][0] += g[i - 1][0];
        for (int j = 1; j < n; j++) g[0][j] += g[0][j - 1];
        for (int i = 1; i < m; i++) for (int j = 1; j < n; j++) g[i][j] += Math.min(g[i - 1][j], g[i][j - 1]);
        return g[m - 1][n - 1];
    }

    // LC #198 House Robber — DP O(n)
    public static int rob(int[] a) {
        int prev = 0, prev2 = 0;
        for (int x : a) { int t = prev; prev = Math.max(prev2 + x, prev); prev2 = t; }
        return prev;
    }

    // LC #416 Partition Equal Subset Sum (0/1 knapsack)
    public static boolean canPartition(int[] a) {
        int sum = 0; for (int x : a) sum += x; if (sum % 2 != 0) return false; sum /= 2;
        boolean[] dp = new boolean[sum + 1]; dp[0] = true;
        for (int x : a) for (int s = sum; s >= x; s--) dp[s] |= dp[s - x];
        return dp[sum];
    }

    public static void main(String[] a) {
        System.out.println(permute(new int[]{1,2,3}).size()); // 6
        System.out.println(subsets(new int[]{1,2}).size());  // 4
        System.out.println(letterCombo("23").size());   // 9
        System.out.println(genParen(3).size());        // 5
        System.out.println(rob(new int[]{2,7,9,3,1})); // 12
        System.out.println(canPartition(new int[]{1,5,11,5})); // true
        char[][] board = {{'X','X','X','X'},{'X','O','O','X'},{'X','X','O','X'},{'X','O','X','X'}};
        solve(board); // modifies board in place
        System.out.println(numIslands(new char[][]{{'1','1','0','0','0'},{'1','1','0','0','0'},{'0','0','1','0','0'},{'0','0','0','1','1'}})); // 3
        System.out.println(canFinish(2, new int[][]{{1,0}})); // true
        System.out.println(Arrays.toString(findOrder(2, new int[][]{{1,0}}))); // [0,1]
    }
}