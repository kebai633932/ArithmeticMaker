package com.kjh.util;

import java.util.Random;

public class Math {
    /**
     * 用辗转相除法求最大公因数
     * @param x 第一个数
     * @param y 第二个数
     * @return 最大公因数
     */
    public int greatFactor_GCD(int x, int y) {
        if (x == 0||y == 0)
            return -1;//返回-1，让生成重新生成
        while (y != 0) {
            int temp = y;
            y = x % y;
            x = temp;
        }
        return x; // 当y为0时，x就是最大公因数
    }

    /**
     * 生成一对互质数
     * @param range 随机数范围
     * @param random 随机数生成器
     * @return 互质数数组
     */
    public int[] createCoprimeNumbers(int range, Random random) {
        int x, y;
        // 循环直到找到一对互质数
        do {
            x = 1 + random.nextInt(range); // 生成随机数 x
            y = 1 + random.nextInt(range); // 生成随机数 y
        } while (greatFactor_GCD(x, y) != 1); // 检查 x 和 y 是否互质
        return new int[]{x, y}; // 返回互质数对
    }


    /**
     * 将假分数转换为真分数
     * @param x 分子
     * @param y 分母
     * @return 真分数字符串
     */
    public String shamToProperFraction(int x, int y) {
        // 判断是否为假分数
        if (x > y) {
            int n = x / y; // 计算整数部分
            x = x % y; // 计算新的分子

            // 如果分子为0，返回整数部分
            if (x == 0) {
                return String.valueOf(n);
            }
            // 返回带分数形式，例如 "1'2/3"
            return n + "'" + x + "/" + y;
        }
        // 如果分子和分母相等，返回1
        else if (x == y) {
            return "1";
        }
        // 如果分母为1，返回分子
        else if (y == 1) {
            return String.valueOf(x);
        }
        // 如果分子为0，返回0
        else if (x == 0) {
            return "0";
        }

        // 返回正常分数形式，例如 "2/3"
        return x + "/" + y;
    }
}
