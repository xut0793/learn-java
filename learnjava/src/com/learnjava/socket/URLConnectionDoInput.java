package com.learnjava.socket;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * This program connects to an URL and displays the response header data and the first 10 lines of the requested data.
 * Supply the URL and an options username and password (for HTTP basic authentication) on the command line.
 * 通过 URL 获取某个 web 资源
 * @version 1.0 2026年1月11日19:20:36
 * @author 03975
 */
public class URLConnectionDoInput {
    public static void main(String[] args) {
        try {
            String urlName = "http://horstmann.com";

            URL url = new URI(urlName).toURL();
            URLConnection connection = url.openConnection();

            //  use username and password
            if (false) {
                String username = "username";
                String password = "123";
                String input = username + ":" + password;
                Base64.Encoder encoder = Base64.getEncoder();
                String encoding = encoder.encodeToString(input.getBytes(StandardCharsets.UTF_8));
                connection.setRequestProperty("Authorization", "Basic " + encoding);
            }

            connection.connect();

            System.out.println("doInput: " + connection.getDoInput()); // doInput 默认为 true 可以接收服务器消息
            System.out.println("doOutput: " + connection.getDoOutput()); // doOutInput 默认 false，即不能主动向服务器发送消息

            // print header fields 获取所有响应头字段
            // 其中特殊的响应行 可以通过 connection.getHeaderFields.get(null) 获取，也就是说有一个字典key=null的value 就是响应行 eg: HTTP/1.1 301 Moved Permanently
            Map<String, List<String>> headers = connection.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                String key = entry.getKey();
                for (String value : entry.getValue()) {
                    System.out.println(key + ": " + value);
                }
            }

            // print convenience function 对某些特殊的请求头提供了便利的方法
            System.out.println("-----------------------");
            System.out.println("getContentType: " + connection.getContentType());
            System.out.println("getContentLength: " + connection.getContentLength());
            System.out.println("getContentEncoding: " + connection.getContentEncoding());
            System.out.println("getDate: " + connection.getDate());
            System.out.println("getExpiration: " + connection.getExpiration());
            System.out.println("getLastModified: " + connection.getLastModified());
            System.out.println("------------------------");

            String encoding = connection.getContentEncoding();
            if (encoding == null) encoding = "UTF-8";

            // 通过 getInputStream 获取响应体正文内容
            try (Scanner in = new Scanner(connection.getInputStream(), encoding)) {
                // print first ten lines of contents
                for (int n = 1; in.hasNextLine() && n <= 10 ; n++) {
                    System.out.println(in.nextLine());
                }
                if (in.hasNextLine()) System.out.println("...");
            }


        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
