package com.learnjava.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 半关闭(half-close)提供了这样一种能力：套接字连接的一端可以终止其输出，同时仍旧可以接收来自另一端的数据。
 */
public class HalfCloseSocket {
    public static void main(String[] args) throws IOException {
        String host = "time-a.nist.gov";
        int port = 13;
        try (Socket socket = new Socket(host, port)) {
            Scanner in = new Scanner(socket.getInputStream(), StandardCharsets.UTF_8);
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            // send request data
            writer.print("Hello! ");
            writer.print("半关闭(half-close)提供了这样一种能力: ");
            writer.print("套接字连接的一端可以终止其输出，");
            writer.print("同时仍旧可以接收来自另一端的数据。");
            socket.shutdownOutput();

            // now socket is half-closed
            // but still can read response data
            while (in.hasNextLine()) {
                String line = in.nextLine();
                System.out.println("response data: " + line);
            }
        }
    }
}
