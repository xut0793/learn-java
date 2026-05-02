package com.learnjava.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 支持多客户端连接的多线程服务
 * @version 1.0 2026年1月11日16:22:19
 * @author 03975
 */
public class ThreadedEchoServer {
    public static void main(String[] args) {
        try (ServerSocket s = new ServerSocket(8189)) {
            int i = 1;

            while (true) {
                Socket incoming = s.accept();
                System.out.println("Spawning " + i);
                Runnable r = new ThreadedEchoHandler(incoming);
                Thread t = new Thread(r);
                t.start();
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/**
 * This class handles the client input for one server socket connection
 */
class ThreadedEchoHandler implements Runnable {
    private final Socket incoming;

    /**
     * Constructs a handler.
     * @param incomingSocket the incoming socket
     */
    public ThreadedEchoHandler(Socket incomingSocket) {
        incoming = incomingSocket;
    }

    public void run() {
        try (InputStream inStream = incoming.getInputStream();
             OutputStream outStream = incoming.getOutputStream();
             Scanner in = new Scanner(inStream, StandardCharsets.UTF_8);
             OutputStreamWriter sw = new OutputStreamWriter(outStream, StandardCharsets.UTF_8);
             PrintWriter out = new PrintWriter(sw, true );
        ) {
            out.println("Hello! Enter BYE to exit.");

            // echo client input
            boolean done = false;
            while (!done && in.hasNextLine()) {
                String line = in.nextLine();
                out.println("Echo: " + line);

                if (line.strip().equals("BYE")) {
                    done = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
