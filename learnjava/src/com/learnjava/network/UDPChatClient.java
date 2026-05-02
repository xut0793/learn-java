package com.learnjava.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPChatClient {
    public static void main(String[] args) {
        try {
            DatagramSocket clientSocket = new DatagramSocket(8888);
            System.out.println("【UDP客户端】已启动，开始聊天吧！（输入 'bye' 退出）");

            // 开启子线程，专门负责接收服务端发来的消息
            new Thread(() -> {
                byte[] buf = new byte[1024];
                while (true) {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    try {
                        clientSocket.receive(packet);
                        String msg = new String(packet.getData(), 0, packet.getLength());
                        if ("bye".equalsIgnoreCase(msg)) break;
                        System.out.println("\n【服务端】：" + msg);
                        System.out.println("【我】：");
                    } catch (Exception e) {
                        break;
                    }
                }
            }).start();

            // 3. 主线程负责读取键盘输入，并打包发送给客户端
            // 约定客户端运行在本地 9999 端口
            InetAddress serverAddress  = InetAddress.getByName("127.0.0.1");
            int serverPort = 999;
            Scanner scanner = new Scanner(System.in);

            while (true) {
                String msg = scanner.nextLine();
                byte[] data = msg.getBytes();
                // 每次发磅都要封装目标地址和端口，再带上数据
                DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);
                clientSocket.send(packet);

                if ("bye".equalsIgnoreCase(msg)) break;
            }

            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
