package com.kjh.model;

import java.util.Random;

public class CreateFraction {

    private static final String[] OPERATOR = {"+", "-"}; // 操作符数组

    /**
     * 真分数生成器
     * @param range 范围，用于生成随机数
     * @return 包含生成的数学表达式和结果的字符串数组
     */
    public String[] createFractionProblem(int range) {
        Random random = new Random();
        int operatorCount = 1 + random.nextInt(3); // 操作符的个数为1到3

        CreateInteger create = new CreateInteger();
        int[] operatorIndex = create.indexOfOperator(operatorCount, 2, random); // 获取操作符的下标

        // 生成第一个操作数
        int[] coprimeNumber1 = createCoprimeNumbers(range, random);
        int x = coprimeNumber1[0]; // 分子
        int y = coprimeNumber1[1]; // 分母

        StringBuilder s = new StringBuilder(shamToProperFraction(x, y)); // 将假分数转化为真分数字符串

        // 循环生成剩下的操作数
        for (int i = 0; i < operatorCount; i++) {
            // 生成剩下的操作数
            int[] coprimeNumber = createCoprimeNumbers(range, random);
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
                    coprimeNumber = createCoprimeNumbers(range, random);
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

            String num = shamToProperFraction(num_x, num_y); // 转换为真分数字符串
            s.append(currentOperator).append(num); // 拼接表达式
        }

        int greatFactor = greatFactor(x, y); // 计算最大公因数
        x /= greatFactor; // 结果化简
        y /= greatFactor;

        String res = shamToProperFraction(x, y); // 将最终结果转化为真分数字符串
        s.append("=");

        return new String[]{s.toString(), res}; // 返回表达式和结果
    }

    /**
     * 求最大公因数
     * @param x 第一个数
     * @param y 第二个数
     * @return 最大公因数
     */
    public int greatFactor(int x, int y) {
        while (true) {
            if (x % y == 0) {
                return y; // 如果能整除，返回y
            }
            int temp = y; // 进行欧几里得算法
            y = x % y;
            x = temp;
        }
    }

    /**
     * 生成一对互质数
     * @param range 范围，用于生成随机数
     * @param random 随机数生成器
     * @return 互质数数组
     */
    public int[] createCoprimeNumbers(int range, Random random) {
        int x = 1 + random.nextInt(range); // 生成随机数
        int y = 1 + random.nextInt(range);
        int greatFactor = greatFactor(x, y); // 计算最大公因数
        x /= greatFactor; // 化简
        y /= greatFactor;
        return new int[]{x, y}; // 返回互质数
    }

    /**
     * 假分数转化为真分数
     * @param x 分子
     * @param y 分母
     * @return 真分数字符串
     */
    public String shamToProperFraction(int x, int y) {
        if (x > y) { // 如果假分数
            int n = x / y; // 整数部分
            x = (x - n * y); // 计算新的分子
            if (x == 0) {
                return String.valueOf(n); // 如果分子为0，返回整数部分
            }
            return n + "'" + x + "/" + y; // 返回带分数形式
        } else if (x == y) {
            return "1"; // 如果分子和分母相等
        } else if (y == 1) {
            return String.valueOf(x); // 如果分母为1
        } else if (x == 0) {
            return "0"; // 如果分子为0
        }
        return x + "/" + y; // 返回正常分数形式
    }
}

