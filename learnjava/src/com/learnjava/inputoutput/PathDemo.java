package com.learnjava.inputoutput;

import java.nio.file.Path;

public class PathDemo {
    public static void main(String[] args) {
        Path path = Path.of("src", "main", "java", "Demo.java");
        System.out.println("\n=== Path 接口路径信息 ===");
        System.out.println("原始路径: " + path);
        // 输出: src/main/java/Demo.java
        System.out.println("文件名: " + path.getFileName());
        // 输出: Demo.java
        System.out.println("父路径: " + path.getParent());
        // 输出: src/main/java

        // 路径拼接 (resolve)
        Path basePath = Path.of("/home/user");
        Path resolvedPath = basePath.resolve("downloads/music/song.mp3");
        System.out.println("拼接后的绝对路径: " + resolvedPath.toAbsolutePath());
        // 输出: /home/user/downloads/music/song.mp3

        // 路径规范化 (normalize) - 去除冗余的 . 和 ..
        Path messyPath = Path.of("/home/user/./docs/../downloads/");
        System.out.println("规范化后的路径: " + messyPath.normalize());
        // 输出: /home/user/downloads
    }
}
