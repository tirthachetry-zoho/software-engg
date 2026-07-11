import java.util.*;

/** DSA — Linked List (LeetCode). Run: javac LinkedList.java && java LinkedList */
class ListNode { int val; ListNode next; ListNode(int v){val=v;} ListNode(int v, ListNode n){val=v;next=n;} }

public class LinkedList {

    // LC #206 Reverse Linked List (iterative) — O(n)
    public static ListNode reverse(ListNode h) {
        ListNode p = null, c = h, n;
        while (c != null) { n = c.next; c.next = p; p = c; c = n; }
        return p;
    }

    // LC #21 Merge Two Sorted Lists — O(n+m)
    public static ListNode merge(ListNode a, ListNode b) {
        ListNode d = new ListNode(0), t = d;
        while (a != null && b != null) {
            if (a.val <= b.val) { t.next = a; a = a.next; } else { t.next = b; b = b.next; }
            t = t.next;
        }
        t.next = (a != null) ? a : b;
        return d.next;
    }

    // LC #141 Linked List Cycle (Floyd) — O(n)
    public static boolean hasCycle(ListNode h) {
        ListNode s = h, f = h;
        while (f != null && f.next != null) { s = s.next; f = f.next.next; if (s == f) return true; }
        return false;
    }

    // LC #142 Cycle II (entry node) — O(n)
    public static ListNode cycleEntry(ListNode h) {
        ListNode s = h, f = h;
        while (f != null && f.next != null) {
            s = s.next; f = f.next.next;
            if (s == f) { s = h; while (s != f) { s = s.next; f = f.next; } return s; }
        }
        return null;
    }

    // LC #19 Remove Nth Node From End — O(n)
    public static ListNode removeNth(ListNode h, int n) {
        ListNode d = new ListNode(0, h), f = d, s = d;
        for (int i = 0; i <= n; i++) f = f.next;
        while (f != null) { s = s.next; f = f.next; }
        s.next = s.next.next;
        return d.next;
    }

    // LC #2 Add Two Numbers — O(max(n,m))
    public static ListNode addTwo(ListNode a, ListNode b) {
        ListNode d = new ListNode(0), t = d; int c = 0;
        while (a != null || b != null || c > 0) {
            int x = (a != null) ? a.val : 0, y = (b != null) ? b.val : 0;
            int s = x + y + c; t.next = new ListNode(s % 10); c = s / 10;
            t = t.next; if (a != null) a = a.next; if (b != null) b = b.next;
        }
        return d.next;
    }

    // LC #234 Palindrome Linked List — O(n)
    public static boolean isPalindrome(ListNode h) {
        ListNode s = h, f = h, prev = null;
        while (f != null && f.next != null) { f = f.next.next; ListNode n = s.next; s.next = prev; prev = s; s = n; }
        if (f != null) s = s.next; // odd length
        while (s != null) { if (s.val != prev.val) return false; s = s.next; prev = prev.next; }
        return true;
    }

    // LC #138 Copy List with Random Pointer
    public static class NodeR { int val; NodeR next, random; NodeR(int v){val=v;} }
    public static NodeR copyRandom(NodeR h) {
        if (h == null) return null;
        for (NodeR c = h; c != null; c = c.next.next) { NodeR n = new NodeR(c.val); n.next = c.next; c.next = n; }
        for (NodeR c = h; c != null; c = c.next.next) if (c.random != null) c.next.random = c.random.next;
        NodeR nh = h.next;
        for (NodeR c = h; c != null; c = c.next) { NodeR n = c.next; c.next = n.next; if (n.next != null) n.next = n.next.next; }
        return nh;
    }

    // LC #25 Reverse Nodes in k-Group — O(n)
    public static ListNode reverseKGroup(ListNode h, int k) {
        ListNode d = new ListNode(0, h); ListNode prev = d, cur = h;
        int len = 0; for (ListNode c = h; c != null; c = c.next) len++;
        while (len >= k) {
            ListNode tail = cur, nxt;
            for (int i = 1; i < k; i++) { nxt = cur.next; cur.next = nxt.next; nxt.next = prev.next; prev.next = nxt; }
            prev = tail; cur = tail.next; len -= k;
        }
        return d.next;
    }

    // LC #160 Intersection of Two Lists — O(n)
    public static ListNode getIntersection(ListNode a, ListNode b) {
        ListNode p = a, q = b;
        while (p != q) { p = (p == null) ? b : p.next; q = (q == null) ? a : q.next; }
        return p;
    }

    // LC #83 Remove Duplicates from Sorted List
    public static ListNode deleteDuplicates(ListNode h) {
        ListNode c = h;
        while (c != null && c.next != null) { if (c.val == c.next.val) c.next = c.next.next; else c = c.next; }
        return h;
    }

    // LC #82 Remove Duplicates from Sorted List II — Remove all duplicates, O(n) time
    // Explanation: Use dummy node, skip all duplicates
    public static ListNode deleteDuplicatesII(ListNode head) {
        ListNode dummy = new ListNode(0); dummy.next = head; ListNode prev = dummy, curr = head;
        while (curr != null) {
            boolean duplicate = false;
            while (curr.next != null && curr.val == curr.next.val) { duplicate = true; curr = curr.next; }
            if (duplicate) prev.next = curr.next; else prev = prev.next;
            curr = curr.next;
        }
        return dummy.next;
    }

    // LC #143 Reorder List — O(n) time
    // Explanation: Find middle, reverse second half, merge
    public static void reorderList(ListNode head) {
        if (head == null || head.next == null) return;
        ListNode slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) { slow = slow.next; fast = fast.next.next; }
        ListNode second = reverse(slow.next); slow.next = null;
        ListNode first = head;
        while (second != null) { ListNode temp1 = first.next, temp2 = second.next; first.next = second; second.next = temp1; first = temp1; second = temp2; }
    }

    // LC #61 Rotate List — Rotate right by k places, O(n) time
    // Explanation: Find new head after rotation
    public static ListNode rotateRight(ListNode head, int k) {
        if (head == null || head.next == null) return head;
        int len = 1; ListNode tail = head;
        while (tail.next != null) { tail = tail.next; len++; }
        k = k % len; if (k == 0) return head;
        ListNode newTail = head; for (int i = 0; i < len - k - 1; i++) newTail = newTail.next;
        ListNode newHead = newTail.next; newTail.next = null; tail.next = head;
        return newHead;
    }

    // LC #86 Partition List — Partition around value x, O(n) time
    // Explanation: Two lists for less and greater/equal nodes
    public static ListNode partition(ListNode head, int x) {
        ListNode lessHead = new ListNode(0), less = lessHead;
        ListNode greaterHead = new ListNode(0), greater = greaterHead;
        while (head != null) {
            if (head.val < x) { less.next = head; less = less.next; }
            else { greater.next = head; greater = greater.next; }
            head = head.next;
        }
        greater.next = null; less.next = greaterHead.next;
        return lessHead.next;
    }

    // LC #148 Sort List — Sort linked list using merge sort, O(n log n) time
    // Explanation: Split list, sort halves, merge
    public static ListNode sortList(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode slow = head, fast = head, prev = null;
        while (fast != null && fast.next != null) { prev = slow; slow = slow.next; fast = fast.next.next; }
        prev.next = null;
        ListNode l1 = sortList(head), l2 = sortList(slow);
        return merge(l1, l2);
    }

    public static void main(String[] a) {
        ListNode h = new ListNode(1, new ListNode(2, new ListNode(3, null)));
        ListNode r = reverse(h);
        System.out.println(r.val); // 3
        System.out.println(hasCycle(new ListNode(1, new ListNode(2, null)))); // false
        ListNode dup = new ListNode(1, new ListNode(1, new ListNode(2, null)));
        ListNode cleaned = deleteDuplicatesII(dup);
        System.out.println(cleaned.val); // 2
    }
}