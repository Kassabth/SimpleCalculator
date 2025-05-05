package calculator;

import java.util.Stack;

public class Calculator {

    public double evaluateMathExpression(String expression) {
        return evaluate(expression.replaceAll(" ", ""));
    }

    private double evaluate(String expr) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        int i = 0;
        while (i < expr.length()) {
            char ch = expr.charAt(i);

            if (Character.isDigit(ch) || ch == '.') {
                StringBuilder sb = new StringBuilder();
                while (i < expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    sb.append(expr.charAt(i));
                    i++;
                }
                numbers.push(Double.parseDouble(sb.toString()));
                continue;
            }

            if (ch == '-' && (i == 0 || !Character.isDigit(expr.charAt(i - 1)) && expr.charAt(i - 1) != ')')) {
                // Negative number
                i++;
                StringBuilder sb = new StringBuilder();
                sb.append('-');
                while (i < expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    sb.append(expr.charAt(i));
                    i++;
                }
                numbers.push(Double.parseDouble(sb.toString()));
                continue;
            }

            if (ch == '(') {
                operators.push(ch);
            } else if (ch == ')') {
                while (operators.peek() != '(') {
                    applyTopOperator(numbers, operators);
                }
                operators.pop(); // Remove '('
            } else if (isOperator(ch)) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(ch)) {
                    applyTopOperator(numbers, operators);
                }
                operators.push(ch);
            }

            i++;
        }

        while (!operators.isEmpty()) {
            applyTopOperator(numbers, operators);
        }

        return numbers.pop();
    }

    private void applyTopOperator(Stack<Double> numbers, Stack<Character> operators) {
        double b = numbers.pop();
        double a = numbers.pop();
        char op = operators.pop();

        switch (op) {
            case '+': numbers.push(a + b); break;
            case '-': numbers.push(a - b); break;
            case '*': numbers.push(a * b); break;
        }
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*';
    }

    private int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*') return 2;
        return 0;
    }
}
