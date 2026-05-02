package com.learnjava.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPChatServer {
    public static void main(String[] args) {
        try {
            // 1. 绑定本地 ip 和端口 9999
            DatagramSocket serverSocket = new DatagramSocket(9999);
            System.out.println("【UDP服务端】已启动，等待消息...（输入 'bye' 退出）");

            // 2。 开启子线程，专门负责接收消息
            new Thread(() -> {
                byte[] buf = new byte[1024];
                while (true) {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    try {
                        serverSocket.receive(packet); // 同步阻塞接收
                        String msg = new String(packet.getData(), 0, packet.getLength());

                        if ("bye".equalsIgnoreCase(msg)) break;
                        System.out.println("\n 【客户端】：" + msg);
                        System.out.println("【我】：");
                    } catch (Exception e) {
                        break;
                    }
                }
            }).start();

            // 3. 主线程负责读取键盘输入，并打包发送给客户端
            // 约定客户端运行在本地 8888 端口
            InetAddress clientAddress = InetAddress.getByName("127.0.0.1");
            int clientPort = 8888;
            Scanner scanner = new Scanner(System.in);

            while (true) {
                String msg = scanner.nextLine();
                byte[] data = msg.getBytes();
                // 每次发磅都要封装目标地址和端口，再带上数据
                DatagramPacket packet = new DatagramPacket(data, data.length, clientAddress, clientPort);
                serverSocket.send(packet);

                if ("bye".equalsIgnoreCase(msg)) break;
            }

            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
