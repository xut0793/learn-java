# HTTP 在 Java 中的实现

在Java的早期版本中，我们主要依赖 java.net 包下的 URL、HttpURLConnection 来处理HTTP通信。虽然它作为JDK原生API一直存在，但在使用上相对繁琐，且不支持现代的异步非阻塞操作。

真正的转折点出现在 Java 11。JDK 引入了全新的标准化 java.net.http.HttpClient API。它原生支持 HTTP/2、WebSocket，提供了简洁的流式API，并且完美支持同步和异步请求，性能也大幅提升，成为了现代Java项目发起HTTP请求的首选。

到了 Java 21，随着虚拟线程（Project Loom）的正式落地，Java的并发编程迎来了革命。将现代 HttpClient 与虚拟线程结合，可以用写同步代码的简单方式，轻松实现极高并发的HTTP调用，彻底解决了传统线程在IO密集型任务中的性能瓶颈。

HTTP 通常使用请求（客户端）-响应（服务端）模型进行通信。

## HTTP 客户端

在 Java 中，实现 HTTP 客户端发起请求，通常有以下几种方式：

- `url.openConnection()`
- `url.openStream()`
- `java.net.HttpURLConnection`
- `java.net.httpClient`(Java 11+)

URL 的 openConnection() 方法

```java
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UrlOpenConnectionExample {
    public static void main(String[] args) throws Exception {
        URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");

        // 1. 调用 openConnection() 获取连接对象（此时还未真正建立网络连接）
        URLConnection connection = url.openConnection();

        // 2. 可以在此处进行一些基础配置（HttpURLConnection 支持更多）
        connection.setConnectTimeout(5000); // 设置连接超时
        connection.setRequestProperty("User-Agent", "Java-URL-Connection"); // 设置请求头

        // 3. 获取输入流并读取响应（调用 getInputStream 时才会真正发起请求）
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            System.out.println("openConnection 响应: " + response.toString());

            // 还可以获取响应头等信息
            System.out.println("内容类型: " + connection.getContentType());
        }
    }
}
```

在 90 年代中期，Java 的主打口号是“一次编写，到处运行”。当时的目标是让开发者（尤其是从 C/C++ 转过来处理繁琐 Socket 的开发者）能用最少的代码完成最常见的任务。并且那时的 web 网络极其简单，对于 80% 的日常需求（比如读取一个简单的网页文本、加载一张图片），开发者根本不需要设置超时、不需要改 User-Agent，也不需要处理 POST 请求。用 openConnection 发起请求，需要写 5-6 行样板代码（创建连接 -> 强转 -> 获取流 -> 处理异常）。所以为了方便开发者快速发起一个简单的 GET 请求， JDK 引入了 openStream，只需要 1 行代码。这种“一键直达”的便捷性，极大地降低了新手入门 Java 网络编程的门槛。

```java
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class UrlOpenStreamExample {
    public static void main(String[] args) throws Exception {
        // 只能发起GET请求，无法控制请求头和超时
        URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");

        // 直接获取输入流读取响应
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            System.out.println("openStream 响应: " + response.toString());
        }
    }
}
```

`openStream()` 最精妙的地方是它虽然简陋，但它直接返回了一个纯粹的 InputStream。这个“纯粹”的字节流，恰恰是 Java IO 强大扩展能力的最佳起点。你可以像搭积木一样，把这个简陋的流层层包装，瞬间赋予它各种高级功能：

```java
// 直接获取一个最基础的输入流
InputStream rawStream = url.openStream();

// 1. 想要带缓冲，提高读取效率？包一层 BufferedInputStream
// 2. 想要读取文本并处理编码？包一层 InputStreamReader
// 3. 想要按行读取？再包一层 BufferedReader
BufferedReader reader = new BufferedReader(
    new InputStreamReader(
        new BufferedInputStream(rawStream), "UTF-8")
);
```

`openStream()` 方法本质上是对 `openConnection().getInputStream()` 的简单封装，它只能发起 GET 请求，且无法设置任何请求头、超时时间或获取响应状态码。随着web2.0的发展，HTTP 已经被用来实现很多功能，如文件上传、下载、数据传输、数据同步、数据交换等等。`openStream` 简单的处理方式已经不能满足需要的场景，因此，Java 引入了 `HttpURLConnection` 类，用于实现 HTTP 请求。

```java
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpURLConnectionExample {
    public static void main(String[] args) throws Exception {
        URL url = new URL("https://jsonplaceholder.typicode.com/posts");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // 配置请求方法、超时和请求头
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true); // 允许输出请求体

        // 手动写入请求体
        String jsonBody = "{\"title\": \"Java HTTP\", \"body\": \"HttpURLConnection\", \"userId\": 1}";
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // 处理响应
        int responseCode = conn.getResponseCode();
        System.out.println("HttpURLConnection 响应状态码: " + responseCode);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            System.out.println("HttpURLConnection 响应体: " + response.toString());
        }

        conn.disconnect(); // 释放连接
    }
}
```

`HttpURLConnection` 大部分内部与 `URLConnection` 类一致，支持设置请求方法（GET/POST）、请求头、超时时间以及获取响应状态码。但它的API设计非常冗长，且没有内置连接池，手动处理输入输出流极其繁琐。

在 JDK 11 中，提供了现代化的 HTTP 客户端 API，`java.net.http.HttpClient`，使用起来非常简单，原生支持HTTP/2、异步请求（基于 CompletableFuture），流式 API，并且内置了高效的连接池。

```java
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ModernHttpClientExample {
    public static void main(String[] args) throws Exception {
        // 1. 创建一个 HttpClient 实例（可复用，线程安全，内置连接池）
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        String jsonBody = "{\"title\": \"Java HTTP\", \"body\": \"Modern HttpClient\", \"userId\": 1}";

        // 2. 构建请求（流式API非常优雅）
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // 3. 发送请求并获取响应（同步阻塞）
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("HttpClient 响应状态码: " + response.statusCode());
        System.out.println("HttpClient 响应体: " + response.body());

        // 4. 发送异步请求（异步非阻塞）
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(body -> System.out.println("HttpClient 异步响应体: " + body))
                .join();
    }
}
```

到了 Java 21，随着虚拟线程（Project Loom）的正式落地，Java的并发编程迎来了革命。将现代 HttpClient 与虚拟线程结合，可以用写同步代码的简单方式，轻松实现百万级的并发调用，而不再需要依赖复杂的 CompletableFuture 异步链式编排，彻底解决了传统线程在IO密集型任务中的性能瓶颈。

```java
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualThreadHttpClient {
    // HttpClient 是线程安全的，建议作为全局单例复用，避免重复创建连接池
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static void main(String[] args) {
        // 模拟需要并发请求的 10 个 URL
        List<String> urls = List.of(
                "https://jsonplaceholder.typicode.com/posts/1",
                "https://jsonplaceholder.typicode.com/posts/2",
                "https://jsonplaceholder.typicode.com/posts/3"
                // ... 可以轻松扩展到成千上万个 URL
        );

        // 创建虚拟线程执行器（每个任务都会分配一个独立的虚拟线程）
        // 使用 try-with-resources 确保所有虚拟线程执行完毕后自动优雅关闭
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (String url : urls) {
                // 提交任务：这里直接写同步阻塞的 httpClient.send 代码即可
                executor.submit(() -> {
                    try {
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(url))
                                .GET()
                                .build();

                        // 这里的 send() 是同步阻塞的。
                        // 当虚拟线程执行到这里发生网络 IO 等待时，
                        // JVM 会自动将其从底层的平台线程（载体线程）上“卸载”，
                        // 让出 CPU 去执行其他虚拟线程的任务。
                        HttpResponse<String> response = httpClient.send(request,
                                HttpResponse.BodyHandlers.ofString());

                        System.out.println("URL: " + url + " -> 状态码: " + response.statusCode());

                    } catch (Exception e) {
                        System.err.println("请求失败: " + url + "，错误: " + e.getMessage());
                    }
                });
            }
        } // 离开 try 块时，主线程会自动等待所有虚拟线程任务完成

        System.out.println("所有 HTTP 请求处理完毕！");
    }
}
```

在传统模式下，为了不阻塞昂贵的操作系统线程（平台线程），我们被迫使用 sendAsync 配合回调或 CompletableFuture。但在虚拟线程中，httpClient.send() 虽然写法上是同步阻塞的，但当它遇到网络 IO 等待时，JVM 会自动把这个虚拟线程“挂起”（卸载），把底层的平台线程释放出来去执行别的任务。等 IO 响应回来后，JVM 再找个空闲的平台线程把它“唤醒”（挂载）继续执行。

极高的并发量，传统的平台线程栈内存开销通常在 MB 级别，开几千个线程服务器内存就可能溢出（OOM）。而虚拟线程的栈内存开销仅为 KB 级别，因此我们可以轻松创建数十万甚至上百万个虚拟线程来并发处理 HTTP 请求，完全不需要再手动维护复杂的固定大小线程池。

## HTTP 服务端

在 Java 中，实现 HTTP 服务端接收请求，通常有以下几种方式：

- 手动处理：基于 TCP 的 `ServerSocket` 监听TCP端口，并按照HTTP协议规范解析请求报文（请求行、请求头、请求体），再组装响应报文（状态行、响应头、响应体）返回给客户端。
- 使用 `com.sun.net.httpserver.HttpServer`
- 使用第三方框架 `tomcat` / `jetty` 配合 `servlet` 实现

当你需要自己写一个Web服务器来接收别人的HTTP请求并返回响应时，底层原理是基于 Java Socket (ServerSocket) 监听TCP端口，并按照HTTP协议规范解析请求报文（请求行、请求头、请求体），再组装响应报文（状态行、响应头、响应体）返回给客户端。

在 JDK 1.6 时期，JDK 中就自带了一个轻量级的 HTTP 服务器。在 JDK 9 之后，它被进一步规范化，作为一个标准的模块直接提供给了开发者。它位于 com.sun.net.httpserver 包中，核心类就是 HttpServer。

```java
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class JdkBuiltinHttpServer {
    public static void main(String[] args) throws IOException {
        // 1. 创建服务器，绑定本地 8080 端口
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // 2. 为指定路径注册一个处理器（Handler）
        server.createContext("/hello", new MyHandler());

        // 3. 启动服务器
        server.start();
        System.out.println("JDK 内置服务器已启动，监听端口 8080...");
    }

    // 自定义请求处理器
    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "Hello from JDK built-in HttpServer!";
            // 手动设置响应头和状态码
            exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.getBytes().length);

            // 手动将响应内容写入输出流
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
```

JDK 早就内置了 HTTP Server，为什么在大家的日常认知和绝大多数企业级项目中，几乎从来不用它，反而觉得 Java “没有内置” 呢？核心原因在于：它太“简陋”了，无法满足现代 Web 开发的需求。

JDK 内置的 HttpServer 定位非常明确：它是一个轻量级、用于测试或嵌入式场景的工具。

- 教学与演示：非常适合用来向学生或初学者演示 HTTP 协议底层的请求与响应是如何工作的，代码量极少，没有复杂的框架配置。
- 轻量级测试桩（Mock Server）：在写单元测试时，如果你需要一个临时的 HTTP 服务来模拟第三方接口返回数据，用它可以几行代码快速启动一个，测试完即停，非常方便。
- 嵌入式/工具类应用：比如你写了一个本地的 Java 桌面小工具，想通过浏览器访问 localhost:8080 来查看一些本地状态或进行简单控制，引入一个庞大的 Tomcat 显然太重了，内置的 HttpServer 就刚刚好。

它和主流的 Servlet 容器（如 Tomcat、Jetty）相比，差距非常大：

- 功能极度缺失：它仅仅实现了最基础的 HTTP 协议解析和请求分发。它不支持 JSP、不支持 Servlet 规范、没有成熟的 Session 管理、没有复杂的过滤器链（Filter Chain），甚至连读取 POST 请求的参数都需要开发者自己手动去解析输入流。
- 性能与并发瓶颈：虽然它底层基于 Java NIO，但在高并发场景下的性能、连接池管理、线程模型优化等方面，远不如专门为生产环境打磨多年的 Tomcat 或 Netty。
- 生态隔离：现代 Java Web 开发完全建立在 Servlet 规范 之上（包括我们熟悉的 Spring Boot）。JDK 内置的 HttpServer 并不支持 Servlet 标准，这意味着如果你用了它，就无法使用 Spring MVC 等任何主流框架，只能纯手工编写业务逻辑。

在实际的企业级开发中，我们不会去手写Socket解析HTTP协议，HttpServer 也不能满足需求，所以通常会使用遵循 Servlet 规范的专业 Web 容器。比如 Tomcat、Jetty，统一通过 HttpServletRequest 和 HttpServletResponse 对象来轻松获取请求数据和构建响应内容。

```java
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

// 使用注解映射请求路径
@WebServlet("/hello")
public class SimpleHttpServlet extends HttpServlet {

    // 处理 GET 请求
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 获取请求参数
        String name = req.getParameter("name");
        if (name == null) name = "World";

        // 2. 设置响应状态和响应头
        resp.setStatus(HttpServletResponse.SC_OK); // 200 OK
        resp.setContentType("text/html;charset=UTF-8");

        // 3. 向响应体中写入数据返回给客户端
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1>Hello, " + name + "!</h1>");
        out.println("</body></html>");
    }

    // 处理 POST 请求
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 处理业务逻辑...
        resp.setStatus(HttpServletResponse.SC_CREATED); // 201 Created
    }
}
```

Tomcat 和 Jetty 这类专业的 Servlet 容器，其底层核心确实就是基于 TCP（Socket）实现网络通信，然后由手动（代码逻辑）去解析和组装 HTTP 协议规范。它们并不是依赖 JDK 里那个简陋的 HttpServer，而是自己从零开始打造了一套高性能的“网络通信 + 协议解析”引擎。

我们可以把这个过程拆解为两个核心步骤来看：

1. 基于 TCP 的网络通信层（负责“收发快递”）

无论是 Tomcat 还是 Jetty，它们的第一步都是启动一个底层的 TCP 服务（监听 Socket 端口），负责与客户端（比如浏览器）建立连接，并接收或发送最原始的字节流（Byte Stream）。
为了应对高并发，它们不会傻傻地用一个线程死守一个连接，而是采用了高效的 I/O 模型：

- Tomcat：在连接器（Connector）中，通过 Endpoint 组件来处理底层的 TCP 通信。现代 Tomcat 默认使用 Java NIO（非阻塞 I/O） 模型，通过一个专门的 Acceptor 线程来接收连接，然后把具体的读写任务交给线程池处理。
- Jetty：同样基于 NIO 实现。它通过 Connector 和 SelectorManager 来管理大量的 Socket 连接，利用多路复用机制，用少量的线程就能管理成千上万的 TCP 连接。

2. 手动处理 HTTP 协议规范（负责“拆包打包”）

当 TCP 层接收到一串原始的字节流后，Tomcat 和 Jetty 内部都有专门的组件，用纯 Java 代码按照 HTTP 协议的标准（如 RFC 7230 等文档）去“翻译”这些字节。

- 解析请求（拆包）：它们会逐字节地读取 TCP 传过来的数据，识别出哪里是请求行（比如 GET /index.html HTTP/1.1），哪里是请求头（比如 Host: localhost），哪里是请求体。
  - 在 Tomcat 中，这个工作由 Processor 组件完成。它把字节流解析成 Tomcat 内部定义的 Request 对象。
  - 在 Jetty 中，则由 HttpConnection 内部的 HttpParser 来完成，把字节流解析成 Jetty 的 Request 对象。
- 组装响应（打包）：当你的业务代码（比如 Spring Boot 的 Controller）处理完请求，返回一个结果后，容器又会把 Response 对象里的状态码、响应头、响应体，按照 HTTP 协议的格式，手动拼接成符合规范的字节流，再通过 TCP 连接发回给浏览器。

为什么要自己造轮子，而不直接优化 JDK 的 HttpServer？

正是因为“手动处理 HTTP 协议”给了它们极大的优化空间：

- 极致的性能优化：它们可以针对 HTTP 协议做各种深度的性能优化，比如对象池化（避免频繁创建 Request/Response 对象）、零拷贝技术、更高效的缓冲区管理等。
- 支持最新协议：JDK 内置的 HttpServer 更新很慢。而 Tomcat 和 Jetty 可以快速跟进，手动实现 HTTP/2（多路复用、头部压缩）甚至最新的 HTTP/3（基于 UDP 的 QUIC 协议）。
- 完美适配 Servlet 规范：它们在解析完 HTTP 协议后，会把这些数据完美地封装成符合 Servlet 标准的 HttpServletRequest 和 HttpServletResponse 对象，这也是 Spring MVC 等所有 Java Web 框架能够运行的基石。
