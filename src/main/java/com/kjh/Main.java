package com.kjh;

import com.kjh.controller.AnswerValidator;
import com.kjh.controller.ProducerController;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // 创建Scanner实例
        int choose;

        do {
            // 显示功能菜单
            showMenu();
            // 读取用户输入
            choose = scanner.nextInt();

            // 根据用户选择执行相应的操作
            switch (choose) {
                case 1: // 四则运算题目生成
                    new ProducerController().inputProblemSettings (); // 生成运算题目
                    break;
                case 2: // 答案比较
                    new AnswerValidator().validateAnswers(); // 开始答案验证
                    break;
                case 0: // 退出
                    System.out.println("感谢使用，再见！"); // 退出提示
                    break;
                default: // 如果用户输入不正确
                    System.out.println("输入数据不正确，请输入1或2或0"); // 提示用户输入无效
                    break;
            }
        } while (choose != 0); // 循环直到用户选择退出
    }
    // 显示功能菜单
    private static void showMenu() {
        System.out.println("请选择功能：");
        System.out.println("    1. 四则运算题目生成"); // 四则运算题目生成
        System.out.println("    2. 答案对比"); // 答案比较
        System.out.println("    0. 退出"); // 退出
        System.out.print("请输入你的选择："); // 提示用户输入选择
    }
}