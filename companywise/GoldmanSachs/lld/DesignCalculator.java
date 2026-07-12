package companywise.GoldmanSachs.lld;

import java.util.*;

/**
 * Low-Level Design — Expression Calculator (Goldman Sachs LLD)
 *
 * Evaluates arithmetic expressions with +, -, *, /, parentheses and unary minus
 * using Dijkstra's Shunting-Yard algorithm to convert infix -> postfix (RPN),
 * then evaluates the postfix form with a stack.
 *
 * Complexity: O(n) time and O(n) space for an expression of length n.
 */
public class DesignCalculator {

    private static final Set<Character> OPERATORS = Set.of('+', '-', '*', '/');
    private static final Map<Character, Integer> PRECEDENCE = Map.of('+', 1, '-', 1, '*', 2, '/', 2);

    /** Public API: evaluate an infix expression string. */
    public static double evaluate(String expr) {
        List<String> postfix = toPostfix(expr);
        return evalPostfix(postfix);
    }

    /** Shunting-yard: infix -> Reverse Polish Notation tokens. */
    static List<String> toPostfix(String expr) {
        List<String> output = new ArrayList<>();
        Deque<Character> ops = new ArrayDeque<>();
        boolean expectOperand = true; // for unary minus detection

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == ' ') continue;

            if (Character.isDigit(c) || c == '.') {
                StringBuilder num = new StringBuilder();
                while (i < expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.'))
                    num.append(expr.charAt(i++));
                i--; // step back for the for-loop increment
                output.add(num.toString());
                expectOperand = false;
            } else if (c == '(') {
                ops.push(c);
                expectOperand = true;
            } else if (c == ')') {
                while (!ops.isEmpty() && ops.peek() != '(') output.add(String.valueOf(ops.pop()));
                if (ops.isEmpty()) throw new IllegalArgumentException("Mismatched parentheses");
                ops.pop(); // remove '('
                expectOperand = false;
            } else if (OPERATORS.contains(c)) {
                // Unary minus/plus when an operand is expected
                if (expectOperand && (c == '-' || c == '+')) {
                    output.add("0"); // rewrite "-x" as "0 - x"
                }
                while (!ops.isEmpty() && ops.peek() != '('
                        && PRECEDENCE.get(ops.peek()) >= PRECEDENCE.get(c)) {
                    output.add(String.valueOf(ops.pop()));
                }
                ops.push(c);
                expectOperand = true;
            } else {
                throw new IllegalArgumentException("Invalid character: " + c);
            }
        }
        while (!ops.isEmpty()) {
            char op = ops.pop();
            if (op == '(' || op == ')') throw new IllegalArgumentException("Mismatched parentheses");
            output.add(String.valueOf(op));
        }
        return output;
    }

    /** Evaluate a postfix (RPN) token list. */
    static double evalPostfix(List<String> tokens) {
        Deque<Double> stack = new ArrayDeque<>();
        for (String t : tokens) {
            if (t.equals("+") || t.equals("-") || t.equals("*") || t.equals("/")) {
                double b = stack.pop(), a = stack.pop();
                stack.push(switch (t) {
                    case "+" -> a + b;
                    case "-" -> a - b;
                    case "*" -> a * b;
                    case "/" -> a / b;
                    default -> 0.0;
                });
            } else {
                stack.push(Double.parseDouble(t));
            }
        }
        return stack.pop();
    }

    public static void main(String[] args) {
        String[] tests = {
                "3 + 4 * 2",          // 11
                "(3 + 4) * 2",        // 14
                "10 - 2 * 3",         // 4
                "-5 + 3",             // -2
                "((2 + 3) * (4 - 1)) / 3", // 5
                "2 * (3 + 4) - 6 / 2" // 11
        };
        for (String t : tests) {
            System.out.printf("%-30s = %s%n", t, evaluate(t));
        }
    }
}