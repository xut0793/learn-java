package com.learnjava.exception;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ThrowsExample {
  public static void readFile(String filename) throws FileNotFoundException {
    FileInputStream fis = new FileInputStream(filename);
    // int data = fis.read();
    // while (data != -1) {
    // System.out.print((char) data);
    // data = fis.read();
    // }
    // fis.close();
  }

  public static void main(String[] args) {
    try {
      readFile("nonexistent.txt");
    } catch (FileNotFoundException e) {
      System.out.println("文件未找到: " + e.getMessage());
    }
  }
}
