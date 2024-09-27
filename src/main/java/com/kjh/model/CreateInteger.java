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
        int operandCount = operatorCount + 1;
        int[] operand = new int[operandCount]; // 存储操作数

        int[] operatorIndex = indexOfOperator(operatorCount, 4, random); // 获取操作符的下标数组

        // 随机生成操作数
        for (int i = 0; i < operandCount; i++) {
            operand[i] = random.nextInt(range); // 生成范围内的随机数
        }

        String formula = stitchingFormula(operatorCount, operand, operatorIndex); // 拼接公式

        int res = algorithm(formula); // 使用 Calculator 计算结果
        String[] formulaRes = new String[2]; // 存储公式和结果的数组

        // 如果结果有效，则返回公式和结果；否则重新生成问题
        if (res > 0) {
            formulaRes[0] = formula;
            formulaRes[1] = String.valueOf(res);
        } else {
            return createIntegerProblem(range); // 递归调用以重新生成问题
        }
        return formulaRes; // 返回公式和结果
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
            operatorIndex[i] = random.nextInt(operatorTypes);
        }
        return operatorIndex; // 返回生成的下标数组
    }

    /**
     * 拼接式子
     */
    public String stitchingFormula(int operatorCount, int[] operand, int[] operatorIndex){
        int bracketForm = new Random().nextInt(2);//式子形态
        StringBuilder formula = new StringBuilder();
        switch (operatorCount){
            case 1:
                // 1+2型
                formula.append(operand[0]).append(" ").append(OPERATOR[operatorIndex[0]]).append(" ")
                        .append(operand[1]).append(" ").append("=");
                break;
            case 2:{
                // 1+2+3 型
                if (bracketForm == 0){
                    formula.append(operand[0]).append(" ").append(OPERATOR[operatorIndex[0]]).append(" ")
                            .append(operand[1]).append(" ").append(OPERATOR[operatorIndex[1]]).append(" ")
                            .append(operand[2]).append(" ").append("=");
                }else {
                    //1+(2+3)型
                    formula.append(operand[0]).append(" ").append(OPERATOR[operatorIndex[0]]).append(" ")
                            .append("(").append(" ").append(operand[1]).append(" ").append(OPERATOR[operatorIndex[1]])
                            .append(" ").append(operand[2]).append(" ").append(")").append(" ").append("=");
                }
                break;
            }
            case 3:{
                if (bracketForm == 0){
                    //1+((2+3)-4)型
                    formula.append(operand[0]).append(" ").append(OPERATOR[operatorIndex[0]]).append(" ")
                            .append("((").append(" ").append(operand[1]).append(" ").append(OPERATOR[operatorIndex[1]])
                            .append(" ").append(operand[2]).append(" ").append(")").append(" ")
                            .append(OPERATOR[operatorIndex[2]]).append(" ").append(operand[3]).append(" ")
                            .append(")").append(" ").append("=");
                }else {
                    //(1+2)+(3+4)型
                    formula.append("(").append(" ").append(operand[0]).append(" ").append(OPERATOR[operatorIndex[0]])
                            .append(" ").append(operand[1]).append(" ").append(")").append(" ")
                            .append(OPERATOR[operatorIndex[1]]).append(" ").append("(").append(" ")
                            .append(operand[2]).append(" ").append(OPERATOR[operatorIndex[2]]).append(" ")
                            .append(operand[3]).append(" ").append(")").append(" ").append("=");
                }
                break;
            }
        }
        return formula.toString();
    }
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
                            int result = performSingleOperation(b, a, stackTop); // 计算
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
                            int result = performSingleOperation(b, a, stackTop);
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
                                int result = performSingleOperation(b, a, stackTop); // 计算
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
     * 单操作符计算两个数的结果
     * @param a 第一个操作数
     * @param b 第二个操作数
     * @param operator 操作符
     * @return 计算结果，返回-1表示除数为0，返回-2表示结果为小数
     */
    private int performSingleOperation(int a, int b, String operator) {
        char ope = operator.charAt(0); // 获取操作符
        switch (ope) {
            case '+':
                return a + b; // 加法
            case '-':
                return a - b; // 减法
            case '*':
                return a * b; // 乘法
            case '÷':
                if (b == 0){
                    return -1; // 除数为0处理
                }
                if (a % b != 0) return -2; // 结果为小数处理
                return a / b; // 除法
            default:
                throw new IllegalArgumentException("不支持的操作符: " + operator); // 处理不支持的操作符
        }
    }
}