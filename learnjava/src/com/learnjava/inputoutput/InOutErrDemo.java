package com.learnjava.inputoutput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class InOutErrDemo {
    public static void main(String[] args) {
        // 创建 Scanner 对象，监听键盘的标准输入 System.in
        Scanner sc = new Scanner(System.in);

        System.out.println("==== 欢迎使用 Java 控制台演示标准输入输出 ====");
        System.out.print("请输入您的年龄："); // 不换行

        // 读取输入，此时阻塞一直到用户输入
        int age = sc.nextInt();

        if (age <= 0) {
            // 标准错误输出
            System.err.println("年龄不能是负数");
        } else {
            System.out.printf("您输入的年龄是： %d", age);
        }

        // 程序结束，需要关闭监听资源
        sc.close();
    }

    public void resolveSystemIn() {
        // InputStreamReader 将字节流 System.in 转换为字符流
        InputStreamReader input = new InputStreamReader(System.in);
        // 为字符输入创建缓存区
        BufferedReader reader = new BufferedReader(input);
        try {
            // 读取输入，此时阻塞一直到用户输入
            String str = reader.readLine();
            // 将字符串转换为整数
            int age = Integer.parseInt(str);
            System.out.printf("您输入的年龄是： %d", age);
        } catch (IOException e) {
            // 使用 BufferedReader 读取输入时，必须处理可能发生的 IO 异常。
            System.err.println("读取输入时发生错误： " + e.getMessage());
        } catch (NumberFormatException e) {
            // 因为 Integer.parseInt() 在遇到非数字字符串时会抛出异常
            System.err.println("输入格式错误，请输入有效的整数！");
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                System.err.println("关闭资源时发生错误：" + e.getMessage());
            }
        }
    }
}
