import java.util.*;

/** DSA — Stack & Queue (LeetCode). Each method has inline explanation.
 *  Run: javac StackQueue.java && java StackQueue */
public class StackQueue {

    // LC #20 Valid Parentheses — Check if parentheses are valid, O(n) time
    // Explanation: Use stack, push opening brackets, pop and match closing brackets
    public static boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') stack.push(c); // push opening
            else {
                if (stack.isEmpty()) return false; // no matching opening
                char top = stack.pop();
                if ((c == ')' && top != '(') || (c == ']' && top != '[') || (c == '}' && top != '{')) return false;
            }
        }
        return stack.isEmpty();
    }

    // LC #739 Daily Temperatures — Days to wait for warmer temperature, O(n) time
    // Explanation: Use monotonic decreasing stack, pop when warmer day found
    public static int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length, res[] = new int[n];
        Deque<Integer> stack = new ArrayDeque<>(); // stores indices
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
                int idx = stack.pop(); res[idx] = i - idx; // calculate days waited
            }
            stack.push(i);
        }
        return res;
    }

    // LC #84 Largest Rectangle in Histogram — Max area rectangle, O(n) time
    // Explanation: Use monotonic increasing stack, calculate area when popping
    public static int largestRectangleArea(int[] heights) {
        int n = heights.length, max = 0;
        Deque<Integer> stack = new ArrayDeque<>(); // stores indices
        for (int i = 0; i <= n; i++) {
            int h = (i == n) ? 0 : heights[i]; // sentinel at end
            while (!stack.isEmpty() && h < heights[stack.peek()]) {
                int height = heights[stack.pop()]; int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                max = Math.max(max, height * width); // calculate area
            }
            stack.push(i);
        }
        return max;
    }

    // LC #85 Maximal Rectangle — Max rectangle of 1s in binary matrix, O(m*n) time
    // Explanation: Treat each row as histogram, use largest rectangle in histogram
    public static int maximalRectangle(char[][] matrix) {
        if (matrix.length == 0) return 0;
        int m = matrix.length, n = matrix[0].length, max = 0;
        int[] heights = new int[n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                heights[j] = (matrix[i][j] == '1') ? heights[j] + 1 : 0; // update histogram
            }
            max = Math.max(max, largestRectangleArea(heights)); // find max in this row
        }
        return max;
    }

    // LC #232 Implement Queue using Stacks — O(1) amortized time
    // Explanation: Use two stacks, one for push, one for pop
    static class MyQueue {
        Deque<Integer> in = new ArrayDeque<>(), out = new ArrayDeque<>();
        public void push(int x) { in.push(x); }
        public int pop() { if (out.isEmpty()) while (!in.isEmpty()) out.push(in.pop()); return out.pop(); }
        public int peek() { if (out.isEmpty()) while (!in.isEmpty()) out.push(in.pop()); return out.peek(); }
        public boolean empty() { return in.isEmpty() && out.isEmpty(); }
    }

    // LC #225 Implement Stack using Queues — O(n) time per operation
    // Explanation: Use single queue, rotate to push new element to front
    static class MyStack {
        Queue<Integer> q = new LinkedList<>();
        public void push(int x) { q.offer(x); for (int i = 0; i < q.size() - 1; i++) q.offer(q.poll()); }
        public int pop() { return q.poll(); }
        public int top() { return q.peek(); }
        public boolean empty() { return q.isEmpty(); }
    }

    // LC #155 Min Stack — Stack with O(1) min retrieval, O(1) time
    // Explanation: Maintain parallel stack of minimums
    static class MinStack {
        Deque<Integer> stack = new ArrayDeque<>(), minStack = new ArrayDeque<>();
        public void push(int val) { stack.push(val); minStack.push(Math.min(minStack.isEmpty() ? val : minStack.peek(), val)); }
        public void pop() { stack.pop(); minStack.pop(); }
        public int top() { return stack.peek(); }
        public int getMin() { return minStack.peek(); }
    }

    // LC #150 Evaluate Reverse Polish Notation — Calculate RPN expression, O(n) time
    // Explanation: Use stack, push numbers, pop two for operators
    public static int evalRPN(String[] tokens) {
        Deque<Integer> stack = new ArrayDeque<>();
        for (String t : tokens) {
            if (t.equals("+") || t.equals("-") || t.equals("*") || t.equals("/")) {
                int b = stack.pop(), a = stack.pop();
                if (t.equals("+")) stack.push(a + b);
                else if (t.equals("-")) stack.push(a - b);
                else if (t.equals("*")) stack.push(a * b);
                else stack.push(a / b);
            } else stack.push(Integer.parseInt(t));
        }
        return stack.pop();
    }

    // LC #71 Simplify Path — Simplify Unix file path, O(n) time
    // Explanation: Use stack, handle ., .., and directory names
    public static String simplifyPath(String path) {
        Deque<String> stack = new ArrayDeque<>();
        for (String dir : path.split("/")) {
            if (dir.isEmpty() || dir.equals(".")) continue; // skip empty and current dir
            if (dir.equals("..")) { if (!stack.isEmpty()) stack.pop(); } // go up one level
            else stack.push(dir); // go into directory
        }
        StringBuilder sb = new StringBuilder();
        for (String dir : stack) sb.append("/").append(dir);
        return sb.length() == 0 ? "/" : sb.toString();
    }

    // LC #394 Decode String — Decode encoded string, O(n) time
    // Explanation: Use stacks for counts and strings, build result recursively
    public static String decodeString(String s) {
        Deque<Integer> countStack = new ArrayDeque<>();
        Deque<StringBuilder> stringStack = new ArrayDeque<>();
        StringBuilder curr = new StringBuilder(); int k = 0;
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) k = k * 10 + (c - '0'); // build number
            else if (c == '[') { countStack.push(k); stringStack.push(curr); curr = new StringBuilder(); k = 0; }
            else if (c == ']') { int count = countStack.pop(); StringBuilder prev = stringStack.pop(); for (int i = 0; i < count; i++) prev.append(curr); curr = prev; }
            else curr.append(c);
        }
        return curr.toString();
    }

    // LC #496 Next Greater Element I — Next greater element in subset, O(n) time
    // Explanation: Use monotonic decreasing stack, build mapping
    public static int[] nextGreaterElement(int[] nums1, int[] nums2) {
        Map<Integer, Integer> nextGreater = new HashMap<>();
        Deque<Integer> stack = new ArrayDeque<>();
        for (int n : nums2) {
            while (!stack.isEmpty() && stack.peek() < n) nextGreater.put(stack.pop(), n);
            stack.push(n);
        }
        int[] res = new int[nums1.length];
        for (int i = 0; i < nums1.length; i++) res[i] = nextGreater.getOrDefault(nums1[i], -1);
        return res;
    }

    // LC #503 Next Greater Element II — Circular array, O(n) time
    // Explanation: Process array twice, use monotonic decreasing stack
    public static int[] nextGreaterElements(int[] nums) {
        int n = nums.length, res[] = new int[n]; Arrays.fill(res, -1);
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < 2 * n; i++) {
            int idx = i % n;
            while (!stack.isEmpty() && nums[stack.peek()] < nums[idx]) res[stack.pop()] = nums[idx];
            if (i < n) stack.push(idx);
        }
        return res;
    }

    // LC #42 Trapping Rain Water — Calculate trapped water, O(n) time
    // Explanation: Two pointers, track max from left and right
    public static int trap(int[] height) {
        int left = 0, right = height.length - 1, leftMax = 0, rightMax = 0, water = 0;
        while (left < right) {
            if (height[left] < height[right]) {
                if (height[left] >= leftMax) leftMax = height[left];
                else water += leftMax - height[left];
                left++;
            } else {
                if (height[right] >= rightMax) rightMax = height[right];
                else water += rightMax - height[right];
                right--;
            }
        }
        return water;
    }

    // LC #239 Sliding Window Maximum — Max in each sliding window, O(n) time
    // Explanation: Use monotonic decreasing deque, remove out-of-window elements
    public static int[] maxSlidingWindow(int[] nums, int k) {
        int n = nums.length, res[] = new int[n - k + 1];
        Deque<Integer> dq = new ArrayDeque<>(); // stores indices
        for (int i = 0; i < n; i++) {
            while (!dq.isEmpty() && dq.peekFirst() < i - k + 1) dq.pollFirst(); // remove out of window
            while (!dq.isEmpty() && nums[dq.peekLast()] < nums[i]) dq.pollLast(); // maintain decreasing
            dq.offerLast(i);
            if (i >= k - 1) res[i - k + 1] = nums[dq.peekFirst()];
        }
        return res;
    }

    // LC #649 Dota2 Senate — Predict senate winner, O(n) time
    // Explanation: Use two queues for Radiant and Dire, simulate voting
    public static String predictPartyVictory(String senate) {
        Queue<Integer> radiant = new LinkedList<>(), dire = new LinkedList<>();
        int n = senate.length();
        for (int i = 0; i < n; i++) {
            if (senate.charAt(i) == 'R') radiant.offer(i); else dire.offer(i);
        }
        while (!radiant.isEmpty() && !dire.isEmpty()) {
            int r = radiant.poll(), d = dire.poll();
            if (r < d) radiant.offer(r + n); // radiant bans dire
            else dire.offer(d + n); // dire bans radiant
        }
        return radiant.isEmpty() ? "Dire" : "Radiant";
    }

    // LC #346 Moving Average from Data Stream — O(1) time per operation
    // Explanation: Use queue with fixed size, maintain sum
    static class MovingAverage {
        Queue<Integer> q = new LinkedList<>(); double sum = 0; int size;
        public MovingAverage(int size) { this.size = size; }
        public double next(int val) {
            if (q.size() == size) sum -= q.poll(); q.offer(val); sum += val;
            return sum / q.size();
        }
    }

    // LC #622 Design Circular Queue — O(1) time per operation
    // Explanation: Use array with front and rear pointers
    static class MyCircularQueue {
        int[] q; int front, rear, size, capacity;
        public MyCircularQueue(int k) { q = new int[k]; front = 0; rear = -1; size = 0; capacity = k; }
        public boolean enQueue(int value) { if (isFull()) return false; rear = (rear + 1) % capacity; q[rear] = value; size++; return true; }
        public boolean deQueue() { if (isEmpty()) return false; front = (front + 1) % capacity; size--; return true; }
        public int Front() { return isEmpty() ? -1 : q[front]; }
        public int Rear() { return isEmpty() ? -1 : q[rear]; }
        public boolean isEmpty() { return size == 0; }
        public boolean isFull() { return size == capacity; }
    }

    // LC #641 Design Circular Deque — O(1) time per operation
    // Explanation: Extend circular queue to support both ends
    static class MyCircularDeque {
        int[] q; int front, rear, size, capacity;
        public MyCircularDeque(int k) { q = new int[k]; front = 0; rear = k - 1; size = 0; capacity = k; }
        public boolean insertFront(int value) { if (isFull()) return false; front = (front - 1 + capacity) % capacity; q[front] = value; size++; return true; }
        public boolean insertLast(int value) { if (isFull()) return false; rear = (rear + 1) % capacity; q[rear] = value; size++; return true; }
        public boolean deleteFront() { if (isEmpty()) return false; front = (front + 1) % capacity; size--; return true; }
        public boolean deleteLast() { if (isEmpty()) return false; rear = (rear - 1 + capacity) % capacity; size--; return true; }
        public int getFront() { return isEmpty() ? -1 : q[front]; }
        public int getRear() { return isEmpty() ? -1 : q[rear]; }
        public boolean isEmpty() { return size == 0; }
        public boolean isFull() { return size == capacity; }
    }

    public static void main(String[] a) {
        System.out.println(isValid("()[]{}")); // true
        System.out.println(Arrays.toString(dailyTemperatures(new int[]{73,74,75,71,69,72,76,73}))); // [1,1,4,2,1,1,0,0]
        System.out.println(largestRectangleArea(new int[]{2,1,5,6,2,3})); // 10
        System.out.println(evalRPN(new String[]{"2","1","+","3","*"})); // 9
        System.out.println(simplifyPath("/home/")); // "/home"
        System.out.println(decodeString("3[a]2[bc]")); // "aaabcbc"
        System.out.println(Arrays.toString(nextGreaterElement(new int[]{4,1,2}, new int[]{1,3,4,2}))); // [-1,3,-1]
        System.out.println(trap(new int[]{0,1,0,2,1,0,1,3,2,1,2,1})); // 6
        System.out.println(Arrays.toString(maxSlidingWindow(new int[]{1,3,-1,-3,5,3,6,7}, 3))); // [3,3,5,5,6,7]
        System.out.println(predictPartyVictory("RD")); // "Radiant"
    }
}
