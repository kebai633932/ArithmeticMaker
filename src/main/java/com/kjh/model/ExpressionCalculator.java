package com.kjh.model;

import java.util.HashMap;
import java.util.Stack;

/**
 * 整式计算器
 */
public class ExpressionCalculator {

    /**
     * 计算给定表达式的值
     * @param s 输入的数学表达式字符串
     * @return 计算结果，返回整数值
     */
    public int algorithm(String s) {
        Stack<Integer> numStack = new Stack<>(); // 存放数字的栈
        Stack<String> operatorStack = new Stack<>(); // 存放操作符的栈
        HashMap<String, Integer> hashMap = new HashMap<>(); // 存放运算符优先级
        hashMap.put("(", 0);
        hashMap.put("+", 1);
        hashMap.put("-", 1);
        hashMap.put("*", 2);
        hashMap.put("÷", 2);

        String formula = s.replaceAll(" ", ""); // 去除公式中的空格

        // 遍历公式中的每个字符
        for (int i = 0; i < formula.length();) {
            StringBuilder digit = new StringBuilder();  // 用于存放当前数字
            char c = formula.charAt(i); // 获取当前字符

            // 判断字符是否为数字
            while (Character.isDigit(c)) {
                digit.append(c); // 将数字添加到digit中
                i++;
                if (i < formula.length()) {
                    c = formula.charAt(i); // 获取下一个字符
                } else {
                    break;
                }
            }

            // 如果digit为空，说明当前字符是操作符
            if (digit.length() == 0) {
                switch (c) {
                    case '(': {
                        operatorStack.push(String.valueOf(c)); // 如果是'(', 压入操作符栈
                        break;
                    }
                    case ')': { // 遇到右括号，进行计算
                        String stackTop = operatorStack.pop(); // 弹出操作符栈顶元素
                        // 直到遇到左括号为止
                        while (!operatorStack.isEmpty() && !stackTop.equals("(")) {
                            int a = numStack.pop();  // 取出操作数a
                            int b = numStack.pop();  // 取出操作数b
                            int result = calculate(b, a, stackTop); // 计算
                            if (result < 0) return -1; // 处理错误情况
                            numStack.push(result); // 将结果压入数字栈
                            stackTop = operatorStack.pop(); // 弹出下一个操作符
                        }
                        break;
                    }
                    case '=': { // 遇到等号，进行计算
                        String stackTop;
                        // 继续计算直到操作符栈为空
                        while (!operatorStack.isEmpty()) {
                            stackTop = operatorStack.pop();
                            int a = numStack.pop();
                            int b = numStack.pop();
                            int result = calculate(b, a, stackTop);
                            if (result < 0) return -1; // 处理错误情况
                            numStack.push(result);
                        }
                        break;
                    }
                    default: { // 处理其他操作符
                        String stackTop;
                        while (!operatorStack.isEmpty()) { // 检查操作符栈
                            stackTop = operatorStack.pop(); // 获取栈顶操作符
                            // 比较当前操作符与栈顶操作符的优先级
                            if (hashMap.get(stackTop) >= hashMap.get(String.valueOf(c))) {
                                int a = numStack.pop();
                                int b = numStack.pop();
                                int result = calculate(b, a, stackTop); // 计算
                                if (result < 0) return -1; // 处理错误情况
                                numStack.push(result); // 将结果压入数字栈
                            } else {
                                operatorStack.push(stackTop); // 优先级较低，放回栈
                                break;
                            }
                        }
                        operatorStack.push(String.valueOf(c)); // 将当前操作符压入栈
                        break;
                    }
                }
            } else { // 处理数字，直接压入数字栈
                numStack.push(Integer.valueOf(digit.toString())); // 将数字转换为整数并压入栈
                continue; // 结束本次循环，继续下一次
            }
            i++;
        }
        return numStack.peek();  // 返回栈顶的数字，即计算结果
    }

    /**
     * 计算给定操作符下的两个数的结果
     * @param a 第一个操作数
     * @param b 第二个操作数
     * @param operator 操作符
     * @return 计算结果
     */
    private int calculate(int a, int b, String operator) {
        int res = 0; // 存储计算结果
        char s = operator.charAt(0); // 获取操作符
        switch (s) {
            case '+': {
                res = a + b; // 加法
                break;
            }
            case '-': {
                res = a - b; // 减法
                break;
            }
            case '*': {
                res = a * b; // 乘法
                break;
            }
            case '÷': {
                if (b == 0) return -1; // 除数为0处理
                else if (a % b != 0) return -2; // 结果为小数处理
                else res = a / b; // 除法
                break;
            }
        }
        return res; // 返回计算结果
    }
}
