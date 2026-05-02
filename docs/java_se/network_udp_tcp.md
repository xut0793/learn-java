# TCP / UDP

TCP / UDP 是实现计算机网络的核心协议，位于传输层层（Transport Layer），负责信息数据的传输。

## UDP

UDP (User Datagram Protocol) 一种无连接、不可靠但高效的传输层协议。它就像寄信，只管发出，不保证对方一定能收到，也不保证收到的顺序。

核心特性

- 无连接：发送数据前无需建立连接（无三次握手），直接发送，延迟极低。
- 不可靠：不保证数据包的送达、顺序，也无重传机制。如果数据包丢失或损坏，UDP协议本身不会处理。
- 面向数据报：保留应用层消息的边界。发送方调用一次sendto()，接收方就必须用一次recvfrom()来完整接收，否则数据可能被截断或丢弃。
- 开销小：协议头部固定为8字节，比TCP的20字节以上要小得多，传输效率高。
- 支持广播/多播：可以向网络中的多个或所有主机同时发送数据。
- 64KB限制：单个UDP数据报的最大长度为65535字节（包括8字节头部），实际可用数据约为64KB。传输更大数据需要在应用层手动分片。
- 可靠性需自行实现：如果业务需要可靠性，必须在应用层自行添加序列号、确认应答(ACK)和超时重传等机制。

## TCP

TCP (Transmission Control Protocol) 一种面向连接、可靠的、基于字节流的传输层协议。它就像打电话，通信前必须先建立连接，并保证通话内容准确无误地传达。

核心特性：

- 面向连接：通信前必须通过“三次握手”建立连接，通信结束后通过“四次挥手”断开连接。
- 可靠传输：通过序列号、确认应答(ACK)、超时重传等机制，确保数据无差错、不丢失、不重复且按序到达。
- 面向字节流：不保留应用层消息边界。发送方多次发送的数据，接收方可能一次性读取；反之，一次发送的大数据，接收方也可能分多次读取。这会导致“粘包”和“拆包”问题。
- 流量控制与拥塞控制：通过滑动窗口机制，防止发送方淹没接收方，并根据网络状况调整发送速率。

### 三次握手 (建立连接)：

客户端 -> 服务器 (SYN)：客户端发送一个SYN包，请求建立连接。
服务器 -> 客户端 (SYN+ACK)：服务器收到请求后，回复一个SYN+ACK包，表示同意建立连接。
客户端 -> 服务器 (ACK)：客户端收到服务器的同意后，再发送一个ACK包进行确认。至此，连接建立成功。

### 四次挥手 (断开连接)：

客户端 -> 服务器 (FIN)：客户端发送FIN包，表示数据已发送完毕，请求关闭连接。
服务器 -> 客户端 (ACK)：服务器收到FIN后，先回复一个ACK包进行确认。此时，服务器可能还有数据要发送。
服务器 -> 客户端 (FIN)：服务器数据发送完毕后，也发送一个FIN包，请求关闭连接。
客户端 -> 服务器 (ACK)：客户端收到服务器的FIN后，回复ACK包进行确认。连接正式关闭。

## Socket

Socket 就是网络通信的“插座”，应用程序（微信、浏览器等）装到电脑上，需要通过这个“插座”与操作系统的网络连接，就好比房子（操作系统）买了个家电（应用程序）需要插上电才能正常工作一样。

从本质上讲，Socket 是应用层与 TCP/IP 协议族通信的中间抽象层，表现为一组编程接口（API）。它屏蔽了底层网络硬件和协议（如三次握手、数据包分片）的复杂实现，为程序员提供了一个统一的接口来发送和接收数据。

- 进程间通信（IPC）：它不仅支持不同计算机（跨网络）的进程通信，也支持同一台计算机内部的进程通信（如 Unix 域套接字）。
- 数据传输的桥梁：它是应用程序（如 Python 代码）与操作系统内核（网络协议栈）之间的桥梁。应用层通过 Socket 将数据写入内核缓冲区，由操作系统负责将数据发送到网络。
- 抽象与解耦：它让开发者无需关心数据是如何在光纤、路由器之间传输的，只需关注“发给谁”和“发什么”。

形象类比：如果把网络通信比作打电话，IP 地址相当于“对方的电话号码”，端口号相当于“分机号”，而 Socket 就是你手中的“电话机”。你不需要知道电话线内部是如何传输信号的（底层协议细节），只需要拿起电话（调用 Socket 接口）说话（发送数据）即可。

一个 Socket 由 IP 地址 和 端口号 唯一标识，`Socket = (IP地址 : 端口号)`。

在 Java 语言中网络编程主要基于 `java.net` 包。TCP 和 UDP 是传输层的两大核心协议，Java 对它们提供了非常成熟且易用的 API 封装。

| 协议 | 核心类           | 角色          | 核心方法与作用                                                                                                   |
| :--- | :--------------- | :------------ | :--------------------------------------------------------------------------------------------------------------- |
| TCP  | `ServerSocket`   | 服务端        | `bind()`: 绑定端口<br>`accept()`: 阻塞监听并接收客户端连接，返回 `Socket` 对象                                   |
| TCP  | `Socket`         | 客户端        | `connect()`: 连接服务端<br>`getInputStream()`: 获取输入流（读数据）<br>`getOutputStream()`: 获取输出流（写数据） |
| UDP  | `DatagramSocket` | 服务端/客户端 | `send(packet)`: 发送数据报包<br>`receive(packet)`: 阻塞接收数据报包                                              |
| UDP  | `DatagramPacket` | 数据包对象    | 封装字节数组、长度、目标IP和端口。UDP通信依赖IP和Port，以及消息数据必须装在这个“信封”里。                        |

TCP 像打电话，连接建立后双方直接通过“流”对话，代码中不需要关心对方的 IP 和端口。
UDP 像寄信，每次发送数据（`socket.send`）时，都必须用 `DatagramPacket` 明确指定对方的 IP 和端口。

## UDP

UDP 编程不需要建立连接，核心在于将数据打包成 `DatagramPacket`，然后通过 `DatagramSocket` 扔出去或收进来。

1. UDP 接收端/服务端 (UDPServer.java)

```java
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {
    public static void main(String[] args) {
        // 1. 创建 DatagramSocket 并绑定端口 9999
        try (DatagramSocket socket = new DatagramSocket(9999)) {
            System.out.println("UDP 服务端已启动，等待接收数据包...");

            // 2. 准备一个空的数据包（信封），用来装接收到的数据
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            // 3. 阻塞接收数据 (receive 会一直等待直到有数据包进来)
            socket.receive(packet);

            // 4. 解析数据包中的内容
            String receivedMsg = new String(packet.getData(), 0, packet.getLength());
            System.out.println("收到客户端消息：" + receivedMsg);
            System.out.println("消息来自：" + packet.getAddress() + ":" + packet.getPort());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

1. UDP 发送端/客户端 (UDPClient.java)

```java
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
    public static void main(String[] args) {
        // 1. 创建 DatagramSocket (客户端可以不指定端口，由系统随机分配)
        try (DatagramSocket socket = new DatagramSocket()) {

            String msg = "你好，我是UDP客户端！";
            byte[] data = msg.getBytes();

            // 2. 封装数据包（指定目标IP、目标端口）
            InetAddress address = InetAddress.getByName("127.0.0.1");
            DatagramPacket packet = new DatagramPacket(data, data.length, address, 9999);

            // 3. 发送数据包
            socket.send(packet);
            System.out.println("消息已发送！");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## TCP

TCP 编程的核心在于建立连接后，通过输入输出流（Stream）进行数据传输。

以下是一个经典的“客户端发一个消息，服务端回一个消息”的示例：

1. TCP 服务端 (TCPServer.java)

```java
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    public static void main(String[] args) {
        // 1. 创建 ServerSocket 并绑定端口 8888
        try (ServerSocket serverSocket = new ServerSocket(8888)) {
            System.out.println("TCP 服务端已启动，等待客户端连接...");

            // 2. 阻塞监听，等待客户端连接 (accept 会一直等待直到有连接进来)
            Socket clientSocket = serverSocket.accept();
            System.out.println("客户端已连接：" + clientSocket.getInetAddress());

            // 3. 获取输入流，读取客户端发来的消息
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String clientMsg = in.readLine();
            System.out.println("收到客户端消息：" + clientMsg);

            // 4. 获取输出流，给客户端回信
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("服务端已收到你的消息：" + clientMsg);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

1. TCP 客户端 (TCPClient.java)

```java
import java.io.*;
import java.net.Socket;

public class TCPClient {
    public static void main(String[] args) {
        // 1. 创建 Socket 并连接服务端的 IP 和端口
        try (Socket socket = new Socket("127.0.0.1", 8888)) {

            // 2. 获取输出流，向服务端发送消息
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("你好，我是TCP客户端！");

            // 3. 获取输入流，接收服务端的回信
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String serverMsg = in.readLine();
            System.out.println("收到服务端回信：" + serverMsg);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

在实际的企业级开发中，仅仅写出上面的 Demo 是远远不够的，以下几点是生产环境中必须注意的：

- TCP 的粘包与拆包问题：TCP 是字节流协议，没有消息边界。如果客户端连续发送两个小包，TCP 可能会把它们合并成一个大包发送（粘包）；或者把一个大包拆成几个小包发送（拆包）。
  - 解决方案：在应用层定义协议格式，例如使用“固定长度”、“特殊分隔符（如 \n）”或“消息头+消息体（在消息头中指定长度）”的方式来解决。
- 资源管理与优雅关闭：无论是 Socket 还是 ServerSocket、DatagramSocket，都涉及操作系统底层的文件描述符和端口占用。
  - 解决方案：务必使用 try-with-resources 语法（如上述代码所示）或在 finally 块中确保它们被关闭，防止端口耗尽。
- 高并发场景的选型：上述 TCP 示例是传统的 BIO（阻塞式 I/O）模型，一个连接需要一个线程去处理。如果并发量达到几千上万，线程开销会直接压垮服务器。
  - 解决方案：在高并发场景下，请使用 Java NIO (Non-blocking I/O) 或者成熟的 NIO 框架（如 Netty），通过多路复用（Selector）机制，用少量线程就能处理海量连接。

## 实现一个基于UDP的可靠的传输服务

UDP 本身是一个无连接且不可靠的协议，它只负责“尽力而为”地发送数据，不保证数据能到达、不保证顺序，也不防止重复。

要实现基于 UDP 的可靠传输（通常被称为 RUDP），核心思路就是在应用层手动模拟 TCP 的可靠性机制。简单来说，就是在你自己的 Java 代码里，把 TCP 协议栈帮你做的事情重新实现一遍。

以下是构建一个可靠 UDP 传输协议必须实现的四大核心机制：

### 序列号（Sequence Number）：解决“乱序”和“重复”

UDP 数据包在网络中可能会走不同的路径，导致先发的包后到。

- 实现原理：发送方在封装 DatagramPacket 时，给每个数据包打上一个递增的唯一编号（如 1, 2, 3...）。
- 接收端处理：接收方根据序列号对数据包进行排序。如果发现收到了重复的序列号，直接丢弃；如果发现序列号不连续（比如收到 1 和 3，缺了 2），则知道发生了丢包，可以将乱序的包暂时缓存在一个缓冲区中，等待缺失的包到来。

### 确认应答（ACK）：解决“丢失”

发送方必须知道对方是否真的收到了数据。

- 实现原理：接收方每收到一个有效的数据包，就立刻（或延迟一小段时间）回发一个极小的 UDP 数据包作为 ACK（确认包），里面包含已收到的序列号。
- 发送端处理：发送方维护一个“已发送但未确认”的数据包队列。

### 超时重传（Timeout & Retransmission）：解决“彻底丢失”

如果网络差，数据包或 ACK 都有可能半路丢失。

- 实现原理：发送方在发出一个数据包的同时，启动一个定时器（可以使用 Java 的 ScheduledExecutorService 或简单的延时线程）。
- 触发重传：如果在设定的时间（RTT，往返时间）内没有收到对应的 ACK，发送方就认为该包丢失，自动重新发送该数据包，并重置定时器。

### 滑动窗口（Sliding Window）：提升“传输效率”

如果发一个包就必须等一个 ACK（停等协议），网络带宽会被极大浪费。

- 实现原理：允许发送方在未收到 ACK 的情况下，连续发送多个数据包（例如最多连续发 10 个）。这 10 个包的容量就是“窗口”。
- 窗口滑动：每当收到一个 ACK，窗口就向前滑动，允许发送下一个新的数据包。这能极大地提高管道利用率。

### 示例代码

在 Java 中，你需要设计一个自定义的数据包结构（比如在真实数据前拼接 8 个字节的头部：4字节序列号 + 4字节校验码等）。以下是一个简化的逻辑框架：

```java
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class ReliableUdpSender {
    private DatagramSocket socket;
    // 记录已发送但未收到ACK的包及其发送时间
    private final ConcurrentHashMap<Integer, PacketData> pendingPackets = new ConcurrentHashMap<>();
    private int sequenceNumber = 0;
    private static final int TIMEOUT_MS = 1000; // 超时时间1秒

    public ReliableUdpSender(int port) throws SocketException {
        this.socket = new DatagramSocket(port);
        // 启动一个后台线程专门处理超时重传
        startRetransmissionThread();
    }

    // 发送可靠数据的方法
    public void sendReliable(String message, InetAddress targetAddr, int targetPort) throws IOException {
        int seq = sequenceNumber++;
        byte[] data = message.getBytes();

        // 1. 封装自定义协议包 (这里简化处理，实际需将seq和data拼接)
        byte[] packetData = buildCustomPacket(seq, data);

        DatagramPacket packet = new DatagramPacket(packetData, packetData.length, targetAddr, targetPort);

        // 2. 发送数据包
        socket.send(packet);
        System.out.println("发送数据包，序列号: " + seq);

        // 3. 存入待确认队列，等待ACK
        pendingPackets.put(seq, new PacketData(packet, System.currentTimeMillis()));
    }

    // 接收ACK并移除待重传队列中的包
    public void receiveAck() throws IOException {
        byte[] buf = new byte[1024];
        DatagramPacket ackPacket = new DatagramPacket(buf, buf.length);
        socket.receive(ackPacket);

        int ackSeq = extractSeqFromAck(ackPacket.getData()); // 解析ACK中的序列号
        pendingPackets.remove(ackSeq); // 收到ACK，从待重传队列中删除
        System.out.println("收到ACK，序列号: " + ackSeq);
    }

    // 后台重传线程
    private void startRetransmissionThread() {
        new Thread(() -> {
            while (true) {
                long currentTime = System.currentTimeMillis();
                for (ConcurrentHashMap.Entry<Integer, PacketData> entry : pendingPackets.entrySet()) {
                    PacketData pd = entry.getValue();
                    // 如果超过设定时间未收到ACK，则重传
                    if (currentTime - pd.sendTime > TIMEOUT_MS) {
                        try {
                            socket.send(pd.packet);
                            pd.sendTime = currentTime; // 更新发送时间
                            System.out.println("超时重传，序列号: " + entry.getKey());
                        } catch (IOException e) { e.printStackTrace(); }
                    }
                }
                try { Thread.sleep(100); } catch (InterruptedException e) {}
            }
        }).start();
    }

    // 辅助类：保存数据包和发送时间
    private static class PacketData {
        DatagramPacket packet;
        long sendTime;
        PacketData(DatagramPacket packet, long sendTime) {
            this.packet = packet;
            this.sendTime = sendTime;
        }
    }

    // 简化的封包与解包方法（实际开发中需严谨处理字节流）
    private byte[] buildCustomPacket(int seq, byte[] data) { return new byte[0]; }
    private int extractSeqFromAck(byte[] data) { return 0; }
}
```

在实际生产环境中，从零手写一套完美的 RUDP 极其复杂（还要考虑连接管理、大文件分包和重组、拥塞控制、流量控制等）。通常直接使用成熟的框架，例如 Netty（配合 EpollDatagramChannel 等）或者 Google 的 QUIC 协议的开源实现（如 quiche 或 Java 下的 netty-incubator-codec-quic），它们已经完美解决了在 UDP 之上实现可靠、安全、多路复用传输的所有难题。
