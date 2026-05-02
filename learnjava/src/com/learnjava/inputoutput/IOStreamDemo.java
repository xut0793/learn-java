package com.learnjava.inputoutput;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class IOStreamDemo {
    public static void main(String[] args) {
        String filePath = "example.txt";
        String textToWrite = "Hello, Java IO!\n";

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            bw.write(textToWrite);
            System.out.println("Write Success!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;

            System.out.println("Reading Content...");
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
