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
        Stack<Integer> numStack = new Stack<>(); // 用于存放数字的栈
        Stack<String> operatorStack = new Stack<>(); // 用于存放操作符的栈
        HashMap<String, Integer> hashMap = new HashMap<>(); // 存放运算符优先级的哈希表
        hashMap.put("(", 0); // 左括号优先级最低
        hashMap.put("+", 1); // 加法优先级
        hashMap.put("-", 1); // 减法优先级
        hashMap.put("*", 2); // 乘法优先级
        hashMap.put("÷", 2); // 除法优先级

        String formula = s.replaceAll(" ", ""); // 去除输入字符串中的空格

        for (int i = 0; i < formula.length();) {
            StringBuilder digit = new StringBuilder(); // 当前数字的StringBuilder
            char c = formula.charAt(i); // 获取当前字符

            // 处理数字，收集连续的数字字符
            while (Character.isDigit(c)) { // 判断当前字符是否为数字
                digit.append(c); // 将数字字符加入digit
                i++;
                if (i < formula.length()) {
                    c = formula.charAt(i); // 获取下一个字符
                } else {
                    break; // 结束循环
                }
            }

            // 处理操作符
            if (digit.length() == 0) { // 如果没有数字，说明当前是操作符
                switch (c) {
                    case '(': {
                        operatorStack.push(String.valueOf(c)); // 压入左括号
                        break;
                    }
                    case ')': { // 处理右括号
                        String stmp = operatorStack.pop(); // 弹出操作符栈顶元素
                        while (!operatorStack.isEmpty() && !stmp.equals("(")) { // 计算直到遇到左括号
                            int a = numStack.pop(); // 弹出操作数a
                            int b = numStack.pop(); // 弹出操作数b
                            int result = performSingleOperation(b, a, stmp); // 进行计算
                            if (result < 0) return -1; // 错误处理
                            numStack.push(result); // 将结果压入数字栈
                            stmp = operatorStack.pop(); // 更新操作符
                        }
                        break;
                    }
                    case '=': { // 处理等号
                        String stmp;
                        while (!operatorStack.isEmpty()) { // 计算所有剩余操作符
                            stmp = operatorStack.pop(); // 弹出操作符
                            int a = numStack.pop(); // 弹出操作数a
                            int b = numStack.pop(); // 弹出操作数b
                            int result = performSingleOperation(b, a, stmp); // 进行计算
                            if (result < 0) return -1; // 错误处理
                            numStack.push(result); // 将结果压入数字栈
                        }
                        break;
                    }
                    default: { // 处理其他操作符
                        String stmp;
                        while (!operatorStack.isEmpty()) { // 当符号栈非空时
                            stmp = operatorStack.pop(); // 获取栈顶操作符
                            // 比较优先级
                            if (hashMap.get(stmp) >= hashMap.get(String.valueOf(c))) {
                                int a = numStack.pop(); // 弹出操作数a
                                int b = numStack.pop(); // 弹出操作数b
                                int result = performSingleOperation(b, a, stmp); // 进行计算
                                if (result < 0) return -1; // 错误处理
                                numStack.push(result); // 将结果压入数字栈
                            } else {
                                operatorStack.push(stmp); // 优先级高的操作符回压
                                break; // 退出循环
                            }
                        }
                        operatorStack.push(String.valueOf(c)); // 压入当前操作符
                        break;
                    }
                }
            } else { // 处理数字
                numStack.push(Integer.valueOf(digit.toString())); // 将数字压入数字栈
                continue; // 结束本次循环，回到for语句进行下一次循环
            }
            i++; // 移动到下一个字符
        }
        return numStack.peek(); // 返回栈顶的数字，即表达式的计算结果
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