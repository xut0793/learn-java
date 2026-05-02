package com.learnjava.socket;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * This program show how to interrupt a socket channel.
 * @version 1.0 2026年1月11日16:55:15
 * @author 03975
 */
public class InterruptibleSocket {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Frame frame = new InterruptibleSocketFrame();
            frame.setTitle("InterruptibleSocketTest");
            // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

class InterruptibleSocketFrame extends Frame {
    private Scanner in;
    private JButton interruptibleButton;
    private JButton blockingButton;
    private JButton cancelButton;
    private JTextArea messages;
    private TestServer server;
    private Thread connectThread;

    public InterruptibleSocketFrame() {
        JPanel northPanel = new JPanel();
        add(northPanel, BorderLayout.NORTH);

        final int TEST_ROWS = 20;
        final int TEXT_COLUMNS = 60;
        messages = new JTextArea(TEST_ROWS, TEXT_COLUMNS);
        add(new JScrollPane(messages));

        interruptibleButton = new JButton("Interruptible");
        blockingButton = new JButton("Blocking");

        northPanel.add(interruptibleButton);
        northPanel.add(blockingButton);

        interruptibleButton.addActionListener(event -> {
            interruptibleButton.setEnabled(false);
            blockingButton.setEnabled(false);
            cancelButton.setEnabled(true);
            connectThread = new Thread(() -> {
            try {
                connectInterruptibly();
            } catch (IOException e) {
                messages.append("\nInterruptibleSocketTest.connectInterruptibly: " + e);
            }
            });
            connectThread.start();
        });

        blockingButton.addActionListener(event -> {
            interruptibleButton.setEnabled(false);
            blockingButton.setEnabled(false);
            cancelButton.setEnabled(true);
            connectThread = new Thread(() -> {
                try {
                    connectBlocking();
                } catch (IOException e) {
                    messages.append("\nInterruptibleScoketTest.connectBlocking: " + e);
                }
            });
            connectThread.start();
        });

        cancelButton = new JButton("Cancel");
        cancelButton.setEnabled(false);
        northPanel.add(cancelButton);
        cancelButton.addActionListener(event -> {
            connectThread.interrupt();
            cancelButton.setEnabled(false);
        });

        server = new TestServer();
        new Thread(server).start();
        pack();
    }

    /**
     * Connects to the test server, using interruptible I/O
     */
    public void connectInterruptibly() throws IOException {
        messages.append("Interruptible:\n");
        try (SocketChannel channel = SocketChannel.open(new InetSocketAddress("localhost", 8189))) {
            in = new Scanner(channel, StandardCharsets.UTF_8);

            while (!Thread.currentThread().isInterrupted()) {
                messages.append("Reading");

                if (in.hasNextLine()) {
                    String line = in.nextLine();
                    messages.append(line);
                    messages.append("\n");
                }
            }
        } finally {
            EventQueue.invokeLater(() -> {
                messages.append("Channel closed\n");
                interruptibleButton.setEnabled(true);
                blockingButton.setEnabled(true);
            });
        }
    }

    /**
     * Connect to the test server, using blocking I/O
     */
    public void connectBlocking() throws IOException {
        messages.append("Blocking:\n");
        try (Socket socket = new Socket("localhost", 8189)) {
            in = new Scanner(socket.getInputStream(), StandardCharsets.UTF_8);

            while(!Thread.currentThread().isInterrupted()) {
                messages.append("Reading ");

                if (in.hasNextLine()) {
                    String line = in.nextLine();
                    messages.append(line);
                    messages.append("\n");
                }
            }
        } finally {
            EventQueue.invokeLater(() -> {
                messages.append("Socket closed\n");
                interruptibleButton.setEnabled(true);
                blockingButton.setEnabled(true);
            });
        }
    }
    /**
     * A multithreaded server that listens to port 8189 and sends numbers to the client, simulating a hanging server after 18 numbers.
     * 一个监听 8189 端口并向客户端发送数字的多线程服务器，在发送 18 个数字后模拟服务器挂起。
     */
    class TestServer implements Runnable {
        public void run() {
            try (ServerSocket server = new ServerSocket(8189)) {
                while (true) {
                    Socket incoming = server.accept();
                    Runnable r = new TestServerHandler(incoming);
                    new Thread(r).start();
                }
            } catch (IOException e) {
                messages.append("\nTestServer.run: " + e);
            }
        }
    }

    /**
     * This class handler the client input for one server socket connection
     */
    class TestServerHandler implements Runnable {
        private Socket incoming;
        private int counter;

        /**
         * Constructs a handler
         * @param i the incoming socket
         */
        public TestServerHandler(Socket i) {
            incoming = i;
        }

        public void run() {
            try {
                try {
                    OutputStream outStream = incoming.getOutputStream();
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outStream, StandardCharsets.UTF_8);
                    PrintWriter out = new PrintWriter(outputStreamWriter, true);

                    while (counter < 100) {
                        counter++;
                        if (counter <= 10) out.println(counter);
                        Thread.sleep(100);
                    }
                } finally {
                    incoming.close();
                    messages.append("Closing server\n");
                }
            } catch (Exception e) {
                messages.append("\nTestServerHandler.run: " + e);
            }
        }
    }
}

