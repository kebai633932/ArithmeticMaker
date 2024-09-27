package com.kjh.model;

import com.kjh.util.Math;

import java.util.Random;

public class CreateFraction {

    private static final String[] OPERATOR = {"+", "-"}; // 操作符数组

    /**
     * 真分数公式与答案生成器，分为加法与减法
     * @param maxRange 范围，用于生成随机数
     * @return 包含生成的数学表达式和结果的字符串数组
     */
    public String[] createFractionProblem(int maxRange) {
        Math math = new Math(); // 创建数学工具类实例
        Random random = new Random(); // 创建随机数生成器
        int operatorCount = 1 + random.nextInt(3); // 操作符的个数为1到3

        CreateInteger create = new CreateInteger(); // 创建整数生成器实例
        int[] operatorIndex = create.indexOfOperator(operatorCount, 2, random); // 获取操作符的下标

        // 生成第一个操作数
        int[] coprimeNumber1 = math.createCoprimeNumbers(maxRange, random); // 生成互质数
        int x = coprimeNumber1[0]; // 分子
        int y = coprimeNumber1[1]; // 分母

        // 将假分数转化为真分数字符串
        StringBuilder expression = new StringBuilder(math.shamToProperFraction(x, y));

        // 循环生成剩下的操作数
        for (int i = 0; i < operatorCount; i++) {
            // 生成剩下的操作数
            int[] coprimeNumber2 = math.createCoprimeNumbers(maxRange, random); // 生成新的互质数
            int num_x = coprimeNumber2[0]; // 新的分子
            int num_y = coprimeNumber2[1]; // 新的分母

            String currentOperator = OPERATOR[operatorIndex[i]]; // 获取当前操作符

            // 处理加法
            if (currentOperator.equals("+")) {
                // 分母相乘，分子相加
                x = x * num_y + y * num_x;
            }
            else { // 处理减法
                int count = 0; // 用于防止无限循环的计数器
                // 确保差为非负数
                while (x * num_y - y * num_x < 0) {
                    // 重新生成互质数
                    coprimeNumber2 = math.createCoprimeNumbers(maxRange, random);
                    num_x = coprimeNumber2[0]; // 更新新的分子
                    num_y = coprimeNumber2[1]; // 更新新的分母
                    count++;
                    if (count >= 10) { // 防止无限循环
                        num_x = x - 1; // 设置一个默认值
                        num_y = y; // 保持分母不变
                    }
                }
                // 分母相乘，分子相减
                x = x * num_y - y * num_x;
            }
            // 更新分母
            y = y * num_y;

            // 转换当前操作数为真分数字符串并拼接表达式
            String num = math.shamToProperFraction(num_x, num_y);
            expression.append(currentOperator).append(num);
        }

        // 计算最大公因数以简化结果
        int gcd = math.greatFactor_GCD(x, y);
        if (gcd < 0) {
            return createFractionProblem(maxRange); // 返回-1，让生成重新生成
        }
        // 化简结果
        x /= gcd;
        y /= gcd;

        // 将最终结果转化为真分数字符串并拼接等号
        String ans = math.shamToProperFraction(x, y);
        expression.append("=");

        // 返回生成的表达式和结果
        return new String[]{expression.toString(), ans};
    }
}