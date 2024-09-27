package com.kjh.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    // 读取题目文件
    public List<String> readExerciseFile(String path) throws IOException {
        BufferedReader exerciseReader = new BufferedReader(new FileReader(path));
        String exercise;
        List<String> exercises = new ArrayList<>();

        // 按行读取文件内容
        while ((exercise = exerciseReader.readLine()) != null) {
            String[] split = exercise.split("=");
            //String[] split = exercise.split("[=.]");
            if (split.length >= 2) {
                exercises.add(split[1].trim()); // 提取并去除空白
                //System.out.println(split[1].trim());
            } else {
                exercises.add(" "); // 如果格式不对，添加空格
                System.out.println(split[0]+"答案未输入或写入文件的题目格式错误。");
            }
        }
        return exercises; // 返回题目列表
    }

    // 读取答案文件
    public List<String> readAnswerFile(String path) throws IOException {
        BufferedReader answerReader = new BufferedReader(new FileReader(path));
        String answer;
        List<String> answers = new ArrayList<>();

        // 按行读取文件内容
        while ((answer = answerReader.readLine()) != null) {
            String[] split = answer.split(" ");
            if (split.length >= 2) {
                answers.add(split[1].trim()); // 提取并去除空白
                //System.out.println(split[1].trim());
                //trim() 方法用于去掉字符串两端的空格
            } else {
                answers.add(" "); // 如果格式不对，添加空格
                System.out.println(split[0]+"写入文件的答案格式错误。");
            }
        }
        return answers; // 返回答案列表
    }

    // 写入成绩文件
    public void writeGradeFile(List<String> correct, List<String> wrong) throws IOException {
        File grade = new File("Grade.txt");
        if (grade.exists()) {
            boolean flag = grade.delete(); // 删除已有文件
            if (!flag) {
                System.out.println("无法删除成绩文件"); // 删除失败时输出提示
            }
        }

        if (grade.createNewFile()) {
            try (FileOutputStream gradeOutput = new FileOutputStream(grade);
                 PrintStream gradePrintStream = new PrintStream(gradeOutput)) {
                // 写入正确题号
                String corrects = String.join(",", correct);
                gradePrintStream.println("Correct：" + correct.size() + " (" + corrects + ")");
                // 写入错误题号
                String wrongs = String.join(",", wrong);
                gradePrintStream.println("Wrong：" + wrong.size() + " (" + wrongs + ")");
            }
        }
    }
    /**
     * 输出题目和答案到指定的PrintStream。
     *
     * @param index   题目编号
     * @param problem 题目和答案数组
     * @param streams 可变参数，用于传入输出流
     */
    public void outputFile(int index, String[] problem, PrintStream... streams) {
        // 确保至少有两个输出流
        if (streams.length != 2) {
            System.out.println("必须提供两个输出流：一个用于题目，一个用于答案。");
            return;
        }
        try {
            // 输出题目到第一个PrintStream
            streams[0].println(index + ". " + problem[0]); // 输出题目
            // 输出答案到第二个PrintStream
            streams[1].println(index + ". " + problem[1]); // 输出答案
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("数组越界。"); // 数组越界异常处理
        }
    }
}
