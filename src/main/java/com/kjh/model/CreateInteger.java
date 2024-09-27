package com.kjh.model;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

public class CreateInteger {

    private static final String[] OPERATOR = {"+", "-", "*", "÷"}; // 操作符数组

    /**
     * 整数生成器
     * @param range 范围，用于生成随机数
     * @return 包含生成的数学表达式和结果的字符串数组
     */
    public String[] createIntegerProblem(int range) {
        Random random = new Random();
        int operatorCount = 1 + random.nextInt(3); // 随机生成操作符的个数（1到3个）
        int operandCount = operatorCount + 1; // 操作数的数量
        int[] operand = new int[operandCount]; // 存储操作数

        // 获取操作符的下标数组
        int[] operatorIndex = indexOfOperator(operatorCount, 4, random);

        // 随机生成操作数
        for (int i = 0; i < operandCount; i++) {
            operand[i] = random.nextInt(range); // 生成范围内的随机数
        }

        String problem = stitchingProblem(operatorCount, operand, operatorIndex); // 拼接公式

        int ans = calculator(problem); // 使用 calculator方法 计算结果

        // 如果结果有效，则返回公式和结果；否则重新生成问题
        if (ans > 0) {
            return new String[]{problem.toString(), String.valueOf(ans)}; // 返回公式和结果
        } else {
            return createIntegerProblem(range); // 递归调用以重新生成问题
        }

    }

    /**
     * 随机产生操作符的对应的字符数组的下标
     * @param operatorCount 操作符的数量
     * @param operatorTypes 操作符种类数量
     * @param random 随机数生成器
     * @return 随机生成的操作符下标数组
     */
    public int[] indexOfOperator(int operatorCount, int operatorTypes, Random random) {
        int[] operatorIndex = new int[operatorCount]; // 存储操作符下标
        // 随机生成操作符下标
        for (int i = 0; i < operatorCount; i++) {
            operatorIndex[i] = random.nextInt(operatorTypes); // 获取随机下标
        }
        return operatorIndex; // 返回生成的下标数组
    }

    /**
     * 拼接数学表达式
     * @param operatorCount 操作符数量
     * @param operand 操作数数组
     * @param operatorIndex 操作符下标数组
     * @return 拼接后的数学表达式字符串
     */
    public String stitchingProblem(int operatorCount, int[] operand, int[] operatorIndex) {
        int bracketForm = new Random().nextInt(2); // 随机决定是否使用括号
        StringBuilder problem = new StringBuilder(); // 用于拼接公式
        // 根据操作符数量拼接表达式
        switch (operatorCount) {
            case 1:// a+b型
                problem.append(String.format("%s %s %s =", operand[0], OPERATOR[operatorIndex[0]], operand[1]));
                break;
            case 2: {// a+b+c 型
                if (bracketForm == 0) {
                    problem.append(String.format("%s %s %s %s %s =",operand[0],OPERATOR[operatorIndex[0]],
                            operand[1],OPERATOR[operatorIndex[1]],operand[2]));

                } else {//a+(b+c)型
                    problem.append(String.format("%s %s ( %s %s %s ) =",operand[0],OPERATOR[operatorIndex[0]],
                            operand[1],OPERATOR[operatorIndex[1]],operand[2]));
                }
                break;
            }
            case 3: {
                if (bracketForm == 0) {//a+((b+c)-d)型
                    problem.append(String.format("%s %s (( %s %s %s ) %s %s) =",operand[0],OPERATOR[operatorIndex[0]],
                            operand[1],OPERATOR[operatorIndex[1]],operand[2],OPERATOR[operatorIndex[2]],operand[3]));
                }
                else {//(a+b)+(c+d)型
                    problem.append(String.format("( %s %s %s ) %s ( %s %s %s ) =",operand[0],OPERATOR[operatorIndex[0]],
                            operand[1],OPERATOR[operatorIndex[1]],operand[2],OPERATOR[operatorIndex[2]],operand[3]));
                }
                break;
            }//可添加更多类型，如a+b+c+d型,a+(b+c)-d型等等
        }
        return problem.toString(); // 返回拼接的数学表达式
    }

    /**
     * 计算给定表达式的值
     * @param s 输入的数学表达式字符串
     * @return 计算结果，返回整数值
     */
    public int calculator(String s) {
        Stack<Integer> numStack = new Stack<>(); // 数字栈
        Stack<String> operatorStack = new Stack<>(); // 操作符栈
        HashMap<String, Integer> precedence = new HashMap<>(); // 运算符优先级
        precedence.put("(", 0); // 左括号优先级最高
        precedence.put("+", 1); // 四则运算优先级
        precedence.put("-", 1);
        precedence.put("*", 2);
        precedence.put("÷", 2);

        String problem = s.replaceAll(" ", ""); // 去除空格

        // 遍历每个字符
        for (int i = 0; i < problem.length();) {
            StringBuilder digit = new StringBuilder(); // 当前数字
            char c = problem.charAt(i); // 当前字符

            // 处理数字
            while (Character.isDigit(c)) {
                digit.append(c); // 收集数字
                i++;
                if (i < problem.length()) {
                    c = problem.charAt(i); // 下一个字符
                } else {
                    break; // 结束循环
                }
            }

            // 处理操作符
            if (digit.length() == 0) {
                switch (c) {
                    case '(':
                        operatorStack.push(String.valueOf(c)); // 压入操作符栈
                        break;
                    case ')': // 计算直到遇到左括号
                        String top = operatorStack.pop();
                        while (!operatorStack.isEmpty() && !top.equals("(")) {
                            int a = numStack.pop();
                            int b = numStack.pop();
                            int calculatedResult = performSingleOperation(b, a, top); // 计算
                            if (calculatedResult < 0) return -1; // 错误处理
                            numStack.push(calculatedResult);
                            top = operatorStack.pop(); // 下一个操作符
                        }
                        break;
                    case '=': // 计算所有操作符
                        while (!operatorStack.isEmpty()) {
                            String topOp = operatorStack.pop();
                            int a = numStack.pop();
                            int b = numStack.pop();
                            int calculatedResult = performSingleOperation(b, a, topOp);
                            if (calculatedResult < 0) return -1; // 错误处理
                            numStack.push(calculatedResult);
                        }
                        break;
                    default: // 处理其他操作符
                        while (!operatorStack.isEmpty()) {
                            String topOp = operatorStack.pop();
                            if (precedence.get(topOp) >= precedence.get(String.valueOf(c))) {
                                int a = numStack.pop();
                                int b = numStack.pop();
                                int calculatedResult = performSingleOperation(b, a, topOp);
                                if (calculatedResult < 0) return -1; // 错误处理
                                numStack.push(calculatedResult);
                            } else {
                                operatorStack.push(topOp); // 优先级高的操作符回压
                                operatorStack.push(String.valueOf(c)); // 压入当前操作符
                                break; // 退出循环
                            }
                        }
                        if (operatorStack.isEmpty()) operatorStack.push(String.valueOf(c)); // 压入操作符
                        break;
                }
            } else {
                numStack.push(Integer.parseInt(digit.toString())); // 压入数字栈
            }
        }
        return numStack.pop(); // 返回结果
    }

    /**
     * 执行单个操作的计算
     * @param a 操作数1
     * @param b 操作数2
     * @param operator 操作符
     * @return 计算结果
     */
    public int performSingleOperation(int a, int b, String operator) {
        switch (operator) {
            case "+":
                return a + b; // 加法
            case "-":
                return a - b; // 减法
            case "*":
                return a * b; // 乘法
            case "÷":
                if (b == 0) return -1; // 除数为零时返回错误
                return a / b; // 除法
            default:
                return -1; // 返回错误
        }
    }
}