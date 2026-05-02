package com.learnjava.socket;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandlers;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class HttpServerTest {
    public static void main(String[] args) throws IOException {
        // String host = "localhost";
        int port = 8189;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        server.createContext("/", HttpHandlers.of(301, Headers.of("Location", "https://horstmann.com/corejava"), ""));
        server.createContext("/echo", (HttpExchange exchange) -> {
            var headers = new StringBuilder();
            exchange.getRequestHeaders().forEach((k, vs) -> vs.forEach(v -> headers.append("%s: %s\n".formatted(k, v))));
            String requestBody = new String(exchange.getRequestBody().readAllBytes());
            String response = "%s %s\n%s\n%s\n".formatted(
                    exchange.getRequestMethod(),
                    exchange.getRequestURI(),
                    headers,
                    requestBody
            );
            byte[] responseBytes = response.getBytes();

            exchange.sendResponseHeaders(200, responseBytes.length);
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(responseBytes);
            responseBody.close();
        });
        server.start();
    }
}
