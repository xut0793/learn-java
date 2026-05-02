package com.learnjava.network;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressDemo {
    public static void main(String[] args) {
       try {
           InetAddress address = InetAddress.getByName("www.baidu.com");
           System.out.println("主机名：" + address.getHostName()); // 主机名：www.baidu.com
           System.out.println("IP地址：" + address.getHostAddress()); // IP地址：223.109.82.212

           InetAddress localAddress = InetAddress.getLocalHost();
           System.out.println("本机IP：" + localAddress.getHostAddress()); // 本机IP：192.168.1.3
       } catch (UnknownHostException e) {
           System.err.println("域名解析失败: " + e.getMessage());
       }
    }
}
