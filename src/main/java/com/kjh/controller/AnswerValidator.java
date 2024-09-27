package com.kjh.controller;

import com.kjh.util.FileUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AnswerValidator {

    // 启动方法，开始验证答案的过程
    public void validateAnswers() {
        FileUtil fileUtil = new FileUtil();
        Scanner scanner = new Scanner(System.in);

        // 提示用户输入题目文件路径
        System.out.print("请输入题目文件路径：");
        String exerciseFilePath = scanner.next();

        // 提示用户输入待验证答案文件路径
        System.out.print("请输入待验证答案文件路径：");
        String answerFilePath = scanner.next();

        try {
            // 读取题目文件和答案文件
            List<String> exercises = fileUtil.readExerciseFile(exerciseFilePath);
            List<String> answers = fileUtil.readAnswerFile(answerFilePath);

            // 存储正确和错误的题目编号
            List<String> correctAnswers = new ArrayList<>();
            List<String> incorrectAnswers = new ArrayList<>();

            // 获取最小答案数量以避免越界
            int min = Math.min(exercises.size(), answers.size());
            int num = 1;
            for (int i = 0; i < min; i++){
                if (exercises.get(i).equals(answers.get(i))){
                    correctAnswers.add(String.valueOf(num++));
                }else {
                    incorrectAnswers.add(String.valueOf(num++));
                }
            }

            // 写入成绩文件
            fileUtil.writeGradeFile(correctAnswers, incorrectAnswers);
            // 完成判定后输出信息
            System.out.println("判定完成");

        } catch (FileNotFoundException e) {
            System.out.println("文件不存在");
        } catch (IOException e) {
            System.out.println("文件读入异常");
        }
    }
}
