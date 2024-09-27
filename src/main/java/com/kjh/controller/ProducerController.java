package com.kjh.controller;

import com.kjh.model.CreateFraction;
import com.kjh.model.CreateInteger;
import com.kjh.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class ProducerController {
    /**
     * 提示用户生成题目
     * @throws InputMismatchException 输入匹配错误异常
     */
    public void inputProblemSettings () {
        System.out.println("四则运算生成器生成题目\n");
        Scanner scanner = null;
        try {
            scanner = new Scanner(System.in);

            System.out.print("请输入生成题目个数："); // 输入题目个数
            int numberOfProblems  = scanner.nextInt();
            if (numberOfProblems  <= 0) {
                System.out.println("生成题目个数必须为正整数。\n");
                inputProblemSettings (); // 重新调用以获取有效输入
                return;
            }
            System.out.print("请输入最大自然数："); // 输入自然数范围
            int maxRange = scanner.nextInt();
            if (maxRange <= 0) {
                System.out.println("最大自然数必须为正整数。\n");
                inputProblemSettings (); // 重新调用以获取有效输入
                return;
            }

            generateArithmeticProblem(numberOfProblems , maxRange); // 生成题目
        } catch (InputMismatchException e) {
            System.out.println("输入数据错误，请输入数字。\n\n\n"); // 输入格式错误提示
            if (scanner != null) {
                scanner.nextLine(); // 清除错误输入
            }
            inputProblemSettings (); // 重新调用以获取有效输入
        } catch (IOException e) {
            System.out.println("文件创建失败"); // 文件创建异常处理
        }
    }

    /**
     * 生成并输出题目到文件
     * @param numberOfProblems 题目个数
     * @param maxRange 最大自然数范围
     * @throws IOException 文件异常
     */
    public void generateArithmeticProblem(int numberOfProblems, int maxRange) throws IOException {
        FileUtil fileUtil = new FileUtil();
        // 创建题目和答案文件
        File exercisesFile = new File("Exercises.txt");
        File answersFile = new File("Answers.txt");

        // 删除已存在的文件
        if (exercisesFile.exists()){
            boolean flag = exercisesFile.delete(); // 删除已有文件
            if (!flag) {
                System.out.println("无法删除旧的题目文件"); // 删除失败时输出提示
            }
        }
        if (answersFile.exists()){
            boolean flag = answersFile.delete(); // 删除已有文件
            if (!flag) {
                System.out.println("无法删除旧的题目文件"); // 删除失败时输出提示
            }
        }

        // 创建新文件
        try (PrintStream exercisesPrintStream = new PrintStream(Files.newOutputStream(exercisesFile.toPath()));
             PrintStream answersPrintStream = new PrintStream(Files.newOutputStream(answersFile.toPath()))) {

            Random random = new Random(); // 随机数生成器
            CreateFraction createFraction = new CreateFraction(); // 分数题目生成器
            CreateInteger createInteger = new CreateInteger(); // 整数题目生成器

            for (int i = 1; i <= numberOfProblems; i++) {
                boolean isFraction = random.nextBoolean(); // 返回true或false
                String[] problem;
                if (isFraction) {
                    problem = createFraction.createFractionProblem(maxRange); // 生成分数题目
                } else {
                    problem = createInteger.createIntegerProblem(maxRange); // 生成整数题目
                }

                fileUtil.outputFile(i, problem, exercisesPrintStream, answersPrintStream); // 输出题目和答案
            }
            System.out.println("题目和与之对应的答案文件创建成功。"); // 成功提示
        }
    }
}
