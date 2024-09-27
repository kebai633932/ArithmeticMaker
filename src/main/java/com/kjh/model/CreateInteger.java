package com.kjh.model;
import java.util.Random;

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
        int[] operand = new int[operatorCount + 1]; // 存储操作数
        int[] operatorIndex = indexOfOperator(operatorCount, 4, random); // 获取操作符的下标数组

        // 随机生成操作数
        for (int i = 0; i < operatorCount + 1; i++) {
            operand[i] = random.nextInt(range); // 生成范围内的随机数
        }

        String formula = stitchingFormula(operatorCount, operand, operatorIndex); // 拼接公式

        // 计算结果
        ExpressionCalculator calculator = new ExpressionCalculator();
        int res = calculator.algorithm(formula); // 使用 Calculator 计算结果
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
     * 随机产生操作符的下标数组
     * @param operatorCount 操作符的数量
     * @param operatorTotal 操作符的总数量
     * @param random 随机数生成器
     * @return 随机生成的操作符下标数组
     */
    public int[] indexOfOperator(int operatorCount, int operatorTotal, Random random) {
        int similar = 0; // 用于计数相同下标的操作符
        int[] operatorIndex = new int[operatorCount]; // 存储操作符下标

        // 随机生成操作符下标
        for (int i = 0; i < operatorCount; i++) {
            operatorIndex[i] = random.nextInt(operatorTotal);
        }

        // 统计相同下标的数量
        for (int i : operatorIndex) {
            if (operatorIndex[0] == i) {
                similar++;
            }
        }

        // 如果所有操作符都相同且数量大于1，则重新生成
        if (similar == operatorCount && operatorCount != 1) {
            return indexOfOperator(operatorCount, operatorTotal, random);
        } else {
            return operatorIndex; // 返回生成的下标数组
        }
    }

    /**
     * 拼接式子
     * @param operatorCount 操作符数量
     * @param operand 操作数数组
     * @param operatorIndex 操作符下标数组
     * @return 拼接后的数学公式字符串
     */
    public String stitchingFormula(int operatorCount, int[] operand, int[] operatorIndex) {
        int bracketForm = new Random().nextInt(2); // 随机决定是否使用括号
        StringBuilder formula = new StringBuilder(); // 用于拼接公式字符串

        // 拼接第一个操作数
        formula.append(operand[0]).append(" ");

        // 根据操作符数量构建公式
        for (int i = 0; i < operatorCount; i++) {
            formula.append(OPERATOR[operatorIndex[i]]).append(" ");

            // 对于最后一个操作符，拼接等号
            if (i < operatorCount - 1) {
                formula.append(operand[i + 1]).append(" ");
            } else {
                formula.append("(").append(operand[i + 1]).append(") ");
            }
        }

        // 根据操作符数量处理括号情况
        if (operatorCount == 2) {
            if (bracketForm == 1) {
                formula.insert(formula.indexOf(" ") + 1, "("); // 添加左括号
                formula.insert(formula.lastIndexOf(" ") + 1, ")"); // 添加右括号
            }
        } else if (operatorCount == 3) {
            if (bracketForm == 1) {
                formula.insert(formula.indexOf(" ") + 1, "("); // 添加左括号
                formula.insert(formula.lastIndexOf(" ") + 1, ")"); // 添加右括号
            }
        }

        formula.append("="); // 添加等号
        return formula.toString(); // 返回拼接后的公式字符串
    }
}
//
//    /**
//     * 拼接式子
//     * @param operatorCount 操作符数量
//     * @param operand 操作数数组
//     * @param operatorIndex 操作符下标数组
//     * @return 拼接后的数学公式字符串
//     */
//    public String stitchingFormula(int operatorCount, int operand[], int[] operatorIndex) {
//        int bracketForm = new Random().nextInt(2); // 随机决定是否使用括号
//        StringBuilder formula = new StringBuilder(); // 用于拼接公式字符串
//
//        switch (operatorCount) {
//            case 1: // 处理只有一个操作符的情况
//                formula.append(operand[0])
//                        .append(" ")
//                        .append(OPERATOR[operatorIndex[0]])
//                        .append(" ")
//                        .append(operand[1])
//                        .append(" ")
//                        .append("="); // 1 + 2 型
//                break;
//            case 2: { // 处理有两个操作符的情况
//                if (bracketForm == 0) { // 不使用括号
//                    formula.append(operand[0])
//                            .append(" ")
//                            .append(OPERATOR[operatorIndex[0]])
//                            .append(" ")
//                            .append(operand[1])
//                            .append(" ")
//                            .append(OPERATOR[operatorIndex[1]])
//                            .append(" ")
//                            .append(operand[2])
//                            .append(" ")
//                            .append("="); // 1 + 2 + 3 型
//                } else { // 使用括号
//                    formula.append(operand[0])
//                            .append(" ")
//                            .append(OPERATOR[operatorIndex[0]])
//                            .append(" ")
//                            .append("(")
//                            .append(" ")
//                            .append(operand[1])
//                            .append(" ")
//                            .append(OPERATOR[operatorIndex[1]])
//                            .append(" ")
//                            .append(operand[2])
//                            .append(" ")
//                            .append(")")
//                            .append(" ")
//                            .append("="); // 1 + (2 + 3) 型
//                }
//                break;
//            }
//            case 3: { // 处理有三个操作符的情况
//                if (bracketForm == 0) { // 不使用括号
//                    formula.append(operand[0])
//                            .append(" ")
//                            .append(OPERATOR[operatorIndex[0]])
//                            .append(" ")
//                            .append("((")
//                            .append(" ")
//                            .append(operand[1])
//                            .append(" ")
//                            .append(OPERATOR[operatorIndex[1]])
//                            .append(" ")
//                            .append(operand[2])
//                            .append(" ")
//                            .append(")")
//                            .append(" ")
//                            .append(OPERATOR[operatorIndex[2]])
//                            .append(" ")
//                            .append(operand[3])
//                            .append(" ")
//                            .append(")")
//                            .append(" ")
//                            .append("="); // 1 + ((2 + 3) - 4) 型
//                } else { // 使用括号
//                    formula.append("(")
//                            .append(" ")
//                            .append(operand[0])
//                            .append(" ")
//                            .append(OPERATOR[operatorIndex[0]])
//                            .append(" ")
//                            .append(operand[1])
//                            .append(" ")
//                            .append(")")
//                            .append(" ")
//                            .append(OPERATOR[operatorIndex[1]])
//                            .append(" ")
//                            .append("(")
//                            .append(" ")
//                            .append(operand[2])
//                            .append(" ")
//                            .append(OPERATOR[operatorIndex[2]])
//                            .append(" ")
//                            .append(operand[3])
//                            .append(" ")
//                            .append(")")
//                            .append(" ")
//                            .append("="); // (1 + 2) + (3 + 4) 型
//                }
//                break;
//            }
//        }
//        return formula.toString(); // 返回拼接后的公式字符串
//    }
//}
