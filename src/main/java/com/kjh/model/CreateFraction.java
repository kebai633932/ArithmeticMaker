package com.kjh.model;

import com.kjh.util.Math;

import java.util.Random;

public class CreateFraction {

    private static final String[] OPERATOR = {"+", "-"}; // 操作符数组

    /**
     * 真分数生成器
     * @param range 范围，用于生成随机数
     * @return 包含生成的数学表达式和结果的字符串数组
     */
    public String[] createFractionProblem(int range) {
        Math math=new Math();
        Random random = new Random();
        int operatorCount = 1 + random.nextInt(3); // 操作符的个数为1到3

        CreateInteger create = new CreateInteger();
        int[] operatorIndex = create.indexOfOperator(operatorCount, 2, random); // 获取操作符的下标

        // 生成第一个操作数
        int[] coprimeNumber1 = math.createCoprimeNumbers(range, random);
        int x = coprimeNumber1[0]; // 分子
        int y = coprimeNumber1[1]; // 分母

        StringBuilder s = new StringBuilder(math.shamToProperFraction(x, y)); // 将假分数转化为真分数字符串

        // 循环生成剩下的操作数
        for (int i = 0; i < operatorCount; i++) {
            // 生成剩下的操作数
            int[] coprimeNumber = math.createCoprimeNumbers(range, random);
            int num_x = coprimeNumber[0]; // 新的分子
            int num_y = coprimeNumber[1]; // 新的分母

            String currentOperator = OPERATOR[operatorIndex[i]]; // 当前操作符

            // 处理加法
            // 分母相乘
            if (currentOperator.equals("+")) {
                x = x * num_y + y * num_x; // 分子相加
            } else { // 处理减法
                int count = 0;
                // 确保差为非负数
                while (x * num_y - y * num_x < 0) {
                    coprimeNumber = math.createCoprimeNumbers(range, random);
                    num_x = coprimeNumber[0];
                    num_y = coprimeNumber[1];
                    count++;
                    if (count >= 5) { // 防止无限循环
                        num_x = x - 1;
                        num_y = y;
                    }
                }
                x = x * num_y - y * num_x; // 分子相减
            }
            y = y * num_y; // 分母相乘

            String num = math.shamToProperFraction(num_x, num_y); // 转换为真分数字符串
            s.append(currentOperator).append(num); // 拼接表达式
        }

        int greatFactor = math.greatFactor_GCD(x, y); // 计算最大公因数
        if(greatFactor < 0)
            return createFractionProblem(range);//返回-1，让生成重新生成
        x /= greatFactor; // 结果化简
        y /= greatFactor;

        String res = math.shamToProperFraction(x, y); // 将最终结果转化为真分数字符串
        s.append("=");

        return new String[]{s.toString(), res}; // 返回表达式和结果
    }
}

