import java.util.*;

/** DSA — Trees & BST (LeetCode). Run: javac Trees.java && java Trees */
class TreeNode { int val; TreeNode left, right; TreeNode(int v){val=v;} TreeNode(int v, TreeNode l, TreeNode r){val=v;left=l;right=r;} }

public class Trees {

    // LC #94 Inorder Traversal (iterative) — O(n)
    public static List<Integer> inorder(TreeNode r) {
        List<Integer> out = new ArrayList<>(); Deque<TreeNode> st = new ArrayDeque<>();
        while (r != null || !st.isEmpty()) {
            while (r != null) { st.push(r); r = r.left; }
            r = st.pop(); out.add(r.val); r = r.right;
        }
        return out;
    }

    // LC #104 Max Depth — O(n)
    public static int maxDepth(TreeNode r) {
        return r == null ? 0 : 1 + Math.max(maxDepth(r.left), maxDepth(r.right));
    }

    // LC #100 Same Tree — O(n)
    public static boolean isSame(TreeNode a, TreeNode b) {
        if (a == null || b == null) return a == b;
        return a.val == b.val && isSame(a.left, b.left) && isSame(a.right, b.right);
    }

    // LC #101 Symmetric Tree — O(n)
    public static boolean isSymmetric(TreeNode r) { return r == null || sym(r.left, r.right); }
    static boolean sym(TreeNode a, TreeNode b) {
        if (a == null || b == null) return a == b;
        return a.val == b.val && sym(a.left, b.right) && sym(a.right, b.left);
    }

    // LC #102 Level Order (BFS) — O(n)
    public static List<List<Integer>> levelOrder(TreeNode r) {
        List<List<Integer>> out = new ArrayList<>();
        if (r == null) return out;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(r);
        while (!q.isEmpty()) {
            int n = q.size(); List<Integer> lvl = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                TreeNode c = q.poll(); lvl.add(c.val);
                if (c.left != null) q.offer(c.left);
                if (c.right != null) q.offer(c.right);
            }
            out.add(lvl);
        }
        return out;
    }

    // LC #98 Validate BST — O(n)
    public static boolean isValidBST(TreeNode r) { return valid(r, Long.MIN_VALUE, Long.MAX_VALUE); }
    static boolean valid(TreeNode n, long lo, long hi) {
        if (n == null) return true;
        if (n.val <= lo || n.val >= hi) return false;
        return valid(n.left, lo, n.val) && valid(n.right, n.val, hi);
    }

    // LC #108 Sorted Array -> BST (balanced) — O(n)
    public static TreeNode sortedArrayToBST(int[] a) { return build(a, 0, a.length - 1); }
    static TreeNode build(int[] a, int l, int r) {
        if (l > r) return null;
        int m = (l + r) / 2;
        return new TreeNode(a[m], build(a, l, m - 1), build(a, m + 1, r));
    }

    // LC #236 Lowest Common Ancestor — O(n)
    public static TreeNode lca(TreeNode r, TreeNode p, TreeNode q) {
        if (r == null || r == p || r == q) return r;
        TreeNode L = lca(r.left, p, q), R = lca(r.right, p, q);
        return L != null && R != null ? r : (L != null ? L : R);
    }

    // LC #105 Build Tree from Preorder+Inorder — O(n)
    public static TreeNode buildTree(int[] pre, int[] in) {
        Map<Integer, Integer> m = new HashMap<>();
        for (int i = 0; i < in.length; i++) m.put(in[i], i);
        return build(pre, in, m, 0, 0, in.length - 1);
    }
    static int pi = 0;
    static TreeNode build(int[] pre, int[] in, Map<Integer, Integer> m, int inL, int inR) {
        if (inL > inR) return null;
        TreeNode n = new TreeNode(pre[pi++]);
        int idx = m.get(n.val);
        n.left = build(pre, in, m, inL, idx - 1);
        n.right = build(pre, in, m, idx + 1, inR);
        return n;
    }

    // LC #199 Binary Tree Right Side View — O(n)
    public static List<Integer> rightSide(TreeNode r) {
        List<Integer> out = new ArrayList<>();
        if (r == null) return out;
        Queue<TreeNode> q = new LinkedList<>(); q.offer(r);
        while (!q.isEmpty()) {
            int n = q.size();
            for (int i = 0; i < n; i++) {
                TreeNode c = q.poll();
                if (i == n - 1) out.add(c.val);
                if (c.left != null) q.offer(c.left);
                if (c.right != null) q.offer(c.right);
            }
        }
        return out;
    }

    // LC #124 Binary Tree Max Path Sum — O(n)
    static int best = Integer.MIN_VALUE;
    public static int maxPathSum(TreeNode r) { best = Integer.MIN_VALUE; dfs(r); return best; }
    static int dfs(TreeNode n) {
        if (n == null) return 0;
        int L = Math.max(0, dfs(n.left)), R = Math.max(0, dfs(n.right));
        best = Math.max(best, L + R + n.val);
        return Math.max(L, R) + n.val;
    }

    // LC #230 Kth Smallest Element in a BST — O(n) time
    // Explanation: Inorder traversal gives sorted order, find kth element
    public static int kthSmallest(TreeNode root, int k) {
        Stack<TreeNode> stack = new Stack<>();
        while (root != null || !stack.isEmpty()) {
            while (root != null) { stack.push(root); root = root.left; }
            root = stack.pop(); k--;
            if (k == 0) return root.val;
            root = root.right;
        }
        return -1;
    }

    // LC #103 Binary Tree Zigzag Level Order — O(n) time
    // Explanation: BFS with direction flag, reverse alternate levels
    public static List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>(); if (root == null) return res;
        Queue<TreeNode> q = new LinkedList<>(); q.offer(root); boolean leftToRight = true;
        while (!q.isEmpty()) {
            int size = q.size(); List<Integer> level = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode node = q.poll(); level.add(node.val);
                if (node.left != null) q.offer(node.left);
                if (node.right != null) q.offer(node.right);
            }
            if (!leftToRight) Collections.reverse(level);
            res.add(level); leftToRight = !leftToRight;
        }
        return res;
    }

    // LC #297 Serialize and Deserialize Binary Tree — O(n) time
    // Explanation: Use preorder traversal with markers for null nodes
    public static String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder(); serializeHelper(root, sb);
        return sb.toString();
    }
    static void serializeHelper(TreeNode node, StringBuilder sb) {
        if (node == null) { sb.append("#"); sb.append(","); return; }
        sb.append(node.val); sb.append(",");
        serializeHelper(node.left, sb); serializeHelper(node.right, sb);
    }
    public static TreeNode deserialize(String data) {
        Queue<String> nodes = new LinkedList<>(Arrays.asList(data.split(",")));
        return deserializeHelper(nodes);
    }
    static TreeNode deserializeHelper(Queue<String> nodes) {
        String val = nodes.poll(); if (val.equals("#")) return null;
        TreeNode node = new TreeNode(Integer.parseInt(val));
        node.left = deserializeHelper(nodes); node.right = deserializeHelper(nodes);
        return node;
    }

    // LC #337 House Robber III — Cannot rob adjacent nodes, O(n) time
    // Explanation: For each node, return max of (rob this node + skip children) vs (skip this node + rob children)
    public static int rob(TreeNode root) { return robHelper(root)[0]; }
    static int[] robHelper(TreeNode node) {
        if (node == null) return new int[]{0, 0}; // [rob, skip]
        int[] left = robHelper(node.left), right = robHelper(node.right);
        int rob = node.val + left[1] + right[1]; // rob this, skip children
        int skip = Math.max(left[0], left[1]) + Math.max(right[0], right[1]); // skip this, rob best of children
        return new int[]{rob, skip};
    }

    // LC #114 Flatten Binary Tree to Linked List — O(n) time
    // Explanation: Flatten left subtree, move it to right, attach original right
    public static void flatten(TreeNode root) {
        if (root == null) return;
        flatten(root.left); flatten(root.right);
        TreeNode right = root.right; root.right = root.left; root.left = null;
        while (root.right != null) root = root.right;
        root.right = right;
    }

    // LC #987 Vertical Order Traversal — O(n log n) time
    // Explanation: BFS with (col, row, val) sorting
    public static List<List<Integer>> verticalTraversal(TreeNode root) {
        TreeMap<Integer, List<int[]>> map = new TreeMap<>(); // col -> list of [row, val]
        Queue<TreeNode> q = new LinkedList<>(); Queue<int[]> cols = new LinkedList<>();
        q.offer(root); cols.offer(new int[]{0, 0}); // [col, row]
        while (!q.isEmpty()) {
            TreeNode node = q.poll(); int[] colRow = cols.poll();
            map.computeIfAbsent(colRow[0], k -> new ArrayList<>()).add(new int[]{colRow[1], node.val});
            if (node.left != null) { q.offer(node.left); cols.offer(new int[]{colRow[0] - 1, colRow[1] + 1}); }
            if (node.right != null) { q.offer(node.right); cols.offer(new int[]{colRow[0] + 1, colRow[1] + 1}); }
        }
        List<List<Integer>> res = new ArrayList<>();
        for (List<int[]> list : map.values()) {
            list.sort((a, b) -> a[0] == b[0] ? a[1] - b[1] : a[0] - b[0]); // sort by row, then val
            List<Integer> col = new ArrayList<>(); for (int[] x : list) col.add(x[1]);
            res.add(col);
        }
        return res;
    }

    // LC #116/117 Populating Next Right Pointers — O(n) time
    // Explanation: Use previously established next pointers
    static class Node { int val; Node left, right, next; Node(int v) { val = v; } }
    public static Node connect(Node root) {
        if (root == null) return null;
        Node levelStart = root;
        while (levelStart.left != null) {
            Node curr = levelStart;
            while (curr != null) {
                curr.left.next = curr.right;
                if (curr.next != null) curr.right.next = curr.next.left;
                curr = curr.next;
            }
            levelStart = levelStart.left;
        }
        return root;
    }

    public static void main(String[] a) {
        TreeNode r = new TreeNode(1, new TreeNode(2), new TreeNode(3));
        System.out.println(inorder(r));      // [2,1,3]
        System.out.println(maxDepth(r));     // 2
        System.out.println(isValidBST(r)); // true
        System.out.println(levelOrder(r)); // [[1],[2,3]]
        System.out.println(kthSmallest(r, 2)); // 2
        System.out.println(zigzagLevelOrder(r)); // [[1],[3,2]]
        System.out.println(maxPathSum(r)); // 6
        System.out.println(rob(r)); // 4
    }
}