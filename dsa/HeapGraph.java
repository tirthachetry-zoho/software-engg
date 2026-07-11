import java.util.*;

/** DSA — Heap, Graph, Trie, Union-Find (LeetCode). Run: javac HeapGraph.java && java HeapGraph */
public class HeapGraph {

    // LC #215 Kth Largest Element — min-heap O(n log k)
    public static int kthLargest(int[] a, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int x : a) { pq.offer(x); if (pq.size() > k) pq.poll(); }
        return pq.peek();
    }

    // LC #347 Top K Frequent — bucket O(n)
    public static int[] topKFrequent(int[] a, int k) {
        Map<Integer, Integer> f = new HashMap<>();
        for (int x : a) f.put(x, f.getOrDefault(x, 0) + 1);
        List<Integer>[] b = new List[a.length + 1];
        for (var e : f.entrySet()) {
            int c = e.getValue();
            if (b[c] == null) b[c] = new ArrayList<>();
            b[c].add(e.getKey());
        }
        int[] res = new int[k]; int idx = 0;
        for (int i = b.length - 1; i >= 0 && idx < k; i--)
            if (b[i] != null) for (int v : b[i]) res[idx++] = v;
        return res;
    }

    // LC #23 Merge k Sorted Lists — min-heap O(N log k)
    static class ListNode { int val; ListNode next; ListNode(int v){val=v;} ListNode(int v, ListNode n){val=v;next=n;} }
    public static ListNode mergeKLists(ListNode[] lists) {
        PriorityQueue<ListNode> pq = new PriorityQueue<>((x, y) -> x.val - y.val);
        for (ListNode l : lists) if (l != null) pq.offer(l);
        ListNode d = new ListNode(0), t = d;
        while (!pq.isEmpty()) { ListNode n = pq.poll(); t.next = n; t = t.next; if (n.next != null) pq.offer(n.next); }
        return d.next;
    }

    // LC #200 Number of Islands — DFS O(mn)
    public static int numIslands(char[][] g) {
        int c = 0;
        for (int i = 0; i < g.length; i++)
            for (int j = 0; j < g[0].length; j++)
                if (g[i][j] == '1') { dfs(g, i, j); c++; }
        return c;
    }
    static void dfs(char[][] g, int i, int j) {
        if (i < 0 || j < 0 || i >= g.length || j >= g[0].length || g[i][j] != '1') return;
        g[i][j] = '0';
        dfs(g, i + 1, j); dfs(g, i - 1, j); dfs(g, i, j + 1); dfs(g, i, j - 1);
    }

    // LC #207 Course Schedule — topo sort (Kahn) O(V+E)
    public static boolean canFinish(int n, int[][] p) {
        int[] ind = new int[n]; List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int[] e : p) { adj.get(e[1]).add(e[0]); ind[e[0]]++; }
        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < n; i++) if (ind[i] == 0) q.offer(i);
        int seen = 0;
        while (!q.isEmpty()) {
            int u = q.poll(); seen++;
            for (int v : adj.get(u)) if (--ind[v] == 0) q.offer(v);
        }
        return seen == n;
    }

    // LC #208 Implement Trie
    static class TrieNode { TrieNode[] c = new TrieNode[26]; boolean end; }
    public static class Trie {
        TrieNode root = new TrieNode();
        public void insert(String w) { TrieNode n = root; for (char ch : w.toCharArray()) { if (n.c[ch-'a'] == null) n.c[ch-'a'] = new TrieNode(); n = n.c[ch-'a']; } n.end = true; }
        public boolean search(String w) { TrieNode n = root; for (char ch : w.toCharArray()) { if (n.c[ch-'a'] == null) return false; n = n.c[ch-'a']; } return n.end; }
        public boolean startsWith(String p) { TrieNode n = root; for (char ch : p.toCharArray()) { if (n.c[ch-'a'] == null) return false; n = n.c[ch-'a']; } return true; }
    }

    // LC #323 Number of Connected Components (Union-Find)
    static class UF { int[] p, r; UF(int n){ p = new int[n]; r = new int[n]; for (int i=0;i<n;i++) p[i]=i; }
        int find(int x){ while (p[x] != x){ p[x] = p[p[x]]; x = p[x]; } return x; }
        void union(int a, int b){ int ra=find(a), rb=find(b); if (ra==rb) return; if (r[ra]<r[rb]) p[ra]=rb; else if (r[ra]>r[rb]) p[rb]=ra; else { p[rb]=ra; r[ra]++; } } }
    public static int countComponents(int n, int[][] e) {
        UF uf = new UF(n);
        for (int[] x : e) uf.union(x[0], x[1]);
        int c = 0; for (int i = 0; i < n; i++) if (uf.find(i) == i) c++;
        return c;
    }

    // LC #994 Rotting Oranges — Time to rot all oranges, O(m*n) time
    // Explanation: BFS from all rotten oranges simultaneously
    public static int orangesRotting(int[][] grid) {
        Queue<int[]> q = new LinkedList<>(); int fresh = 0, time = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 2) q.offer(new int[]{i, j});
                else if (grid[i][j] == 1) fresh++;
            }
        }
        if (fresh == 0) return 0;
        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
        while (!q.isEmpty()) {
            int size = q.size(); boolean rotted = false;
            for (int i = 0; i < size; i++) {
                int[] curr = q.poll();
                for (int[] d : dirs) {
                    int x = curr[0] + d[0], y = curr[1] + d[1];
                    if (x >= 0 && x < grid.length && y >= 0 && y < grid[0].length && grid[x][y] == 1) {
                        grid[x][y] = 2; fresh--; q.offer(new int[]{x, y}); rotted = true;
                    }
                }
            }
            if (rotted) time++;
        }
        return fresh == 0 ? time : -1;
    }

    // LC #127 Word Ladder — Shortest transformation sequence, O(n*m^2) time
    // Explanation: BFS with word pattern matching
    public static int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> dict = new HashSet<>(wordList); if (!dict.contains(endWord)) return 0;
        Queue<String> q = new LinkedList<>(); q.offer(beginWord); dict.remove(beginWord);
        int level = 1;
        while (!q.isEmpty()) {
            int size = q.size();
            for (int i = 0; i < size; i++) {
                String word = q.poll(); if (word.equals(endWord)) return level;
                char[] chars = word.toCharArray();
                for (int j = 0; j < chars.length; j++) {
                    char original = chars[j];
                    for (char c = 'a'; c <= 'z'; c++) {
                        if (c == original) continue; chars[j] = c;
                        String newWord = new String(chars);
                        if (dict.contains(newWord)) { q.offer(newWord); dict.remove(newWord); }
                    }
                    chars[j] = original;
                }
            }
            level++;
        }
        return 0;
    }

    // LC #286 Walls and Gates — Fill each empty room with distance to nearest gate, O(m*n) time
    // Explanation: BFS from all gates simultaneously
    public static void wallsAndGates(int[][] rooms) {
        Queue<int[]> q = new LinkedList<>();
        for (int i = 0; i < rooms.length; i++) {
            for (int j = 0; j < rooms[0].length; j++) {
                if (rooms[i][j] == 0) q.offer(new int[]{i, j});
            }
        }
        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
        while (!q.isEmpty()) {
            int[] curr = q.poll();
            for (int[] d : dirs) {
                int x = curr[0] + d[0], y = curr[1] + d[1];
                if (x >= 0 && x < rooms.length && y >= 0 && y < rooms[0].length && rooms[x][y] == Integer.MAX_VALUE) {
                    rooms[x][y] = rooms[curr[0]][curr[1]] + 1; q.offer(new int[]{x, y});
                }
            }
        }
    }

    // LC #332 Reconstruct Itinerary — Reconstruct itinerary in lexical order, O(n log n) time
    // Explanation: DFS with post-order processing (Eulerian path)
    public static List<String> findItinerary(List<List<String>> tickets) {
        Map<String, PriorityQueue<String>> graph = new HashMap<>();
        for (List<String> ticket : tickets) {
            graph.computeIfAbsent(ticket.get(0), k -> new PriorityQueue<>()).offer(ticket.get(1));
        }
        LinkedList<String> result = new LinkedList<>();
        dfsItinerary("JFK", graph, result);
        return result;
    }
    static void dfsItinerary(String airport, Map<String, PriorityQueue<String>> graph, LinkedList<String> result) {
        PriorityQueue<String> destinations = graph.get(airport);
        while (destinations != null && !destinations.isEmpty()) {
            dfsItinerary(destinations.poll(), graph, result);
        }
        result.addFirst(airport);
    }

    // LC #743 Network Delay Time (Dijkstra) — O((V+E) log V)
    public static int networkDelay(int[][] times, int n, int k) {
        Map<Integer, List<int[]>> g = new HashMap<>();
        for (int[] t : times) { if (!g.containsKey(t[0])) g.put(t[0], new ArrayList<>()); g.get(t[0]).add(new int[]{t[1], t[2]}); }
        int[] dist = new int[n + 1]; Arrays.fill(dist, Integer.MAX_VALUE); dist[k] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]); pq.offer(new int[]{k, 0});
        while (!pq.isEmpty()) {
            int[] cur = pq.poll(); int u = cur[0], d = cur[1];
            if (d > dist[u]) continue;
            for (int[] e : g.getOrDefault(u, new ArrayList<>())) if (d + e[1] < dist[e[0]]) { dist[e[0]] = d + e[1]; pq.offer(new int[]{e[0], dist[e[0]]}); }
        }
        int max = 0; for (int i = 1; i <= n; i++) { if (dist[i] == Integer.MAX_VALUE) return -1; max = Math.max(max, dist[i]); }
        return max;
    }

    public static void main(String[] a) {
        System.out.println(kthLargest(new int[]{3,2,1,5,6,4}, 2)); // 5
        char[][] g = {{'1','1','0'},{'0','1','0'},{'0','0','1'}};
        System.out.println(numIslands(g)); // 2
        System.out.println(canFinish(2, new int[][]{{1,0}})); // true
        System.out.println(countComponents(5, new int[][]{{0,1},{1,2},{3,4}})); // 2
        System.out.println(orangesRotting(new int[][]{{2,1,1},{1,1,0},{0,1,1}})); // 4
        System.out.println(ladderLength("hit", "cog", Arrays.asList("hot","dot","dog","lot","log","cog"))); // 5
        List<List<String>> tickets = Arrays.asList(Arrays.asList("MUC","LHR"), Arrays.asList("JFK","MUC"), Arrays.asList("SFO","SJC"), Arrays.asList("LHR","SFO"));
        System.out.println(findItinerary(tickets)); // [JFK, MUC, LHR, SFO, SJC]
    }
}