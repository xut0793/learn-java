package com.learnjava.socket;

import java.io.IOException;
import java.net.InetAddress;

/**
 * 获取域名的IP地址
 * @version 1.0 2026年1月11日15:53:39
 * @author tao.xu
 */
public class InetAddressTest {
    public static void main(String[] args) throws IOException {
        String host = "www.baidu.com";
        InetAddress[] addresses = InetAddress.getAllByName(host);
        for (InetAddress a : addresses) {
            System.out.println(a);
        }

        // 获取本机 localhost
        InetAddress localHostAddress = InetAddress.getLocalHost();
        System.out.println("Localhost: " + localHostAddress);
        System.out.println("Localhost address: " + localHostAddress.getHostAddress());
        System.out.println("Localhost hostName: " + localHostAddress.getHostName());
    }
}
