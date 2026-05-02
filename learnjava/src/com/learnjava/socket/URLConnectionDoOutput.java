package com.learnjava.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

/**
 * 发送表单数据
 */
public class URLConnectionDoOutput {
    public static void main(String[] args) throws IOException, URISyntaxException {
        String propsFilename = "post.properties";
        Properties props = new Properties();
        try (Reader in = Files.newBufferedReader(Path.of(propsFilename), StandardCharsets.UTF_8)) {
            props.load(in);
        }

        String urlString = props.remove("url").toString();
        Object userAgent = props.remove("User-Agent");
        Object redirects = props.remove("redirects");
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        String result = doPost(new URI(urlString).toURL(), props, userAgent == null ? null : userAgent.toString(), redirects == null ? -1 : Integer.parseInt(redirects.toString()));
        System.out.println(result);
    }

    public static String doPost(URL url, Map<Object, Object> nameValuePairs, String userAgnet, int redirects) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        if (userAgnet != null) {
            connection.setRequestProperty("User-Agent", userAgnet);
        }

        if (redirects >= 0) {
            connection.setInstanceFollowRedirects(false);
        }

        // 开启数据提交功能
        connection.setDoOutput(true);

        // 附加请求体
        try (PrintWriter out = new PrintWriter(connection.getOutputStream())) {
            boolean first = true;
            for (Map.Entry<Object, Object> pair : nameValuePairs.entrySet()) {
                if (first) first = false;
                else out.print("&");
                String name = pair.getKey().toString();
                String value = pair.getValue().toString();
                out.print(name);
                out.print("=");
                out.print(URLEncoder.encode(value, StandardCharsets.UTF_8));
            }
        }

        String encoding = connection.getContentEncoding();
        if (encoding == null) encoding = "UTF_8";

        if (redirects > 0) {
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP || responseCode == HttpURLConnection.HTTP_SEE_OTHER) {
                String location = connection.getHeaderField("Location");
                if (location != null) {
                    URL base = connection.getURL();
                    connection.disconnect();
                    return doPost(new URL(base, location), nameValuePairs, userAgnet, redirects - 1);
                }
            }
        } else if (redirects == 0) {
            throw new IOException("Too many redirects");
        }

        StringBuilder response = new StringBuilder();
        try (Scanner in = new Scanner(connection.getInputStream(), encoding)) {
            while (in.hasNextLine()) {
                response.append(in.nextLine());
                response.append("\n");
            }
        } catch (IOException e) {
            InputStream err = connection.getErrorStream();
            if (err == null) throw e;

            try (Scanner in = new Scanner(err)) {
                response.append(in.nextLine());
                response.append("\n");
            }
        }

        return response.toString();

    }
}
