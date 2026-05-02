# URL

## URI / URL / URN

URI = Uniform Resource Identifier 统一资源标志符
URL = Uniform Resource Locator 统一资源定位符
URN = Uniform Resource Name 统一资源名称

看了全称大概也不好理解三者的区别。

在互联网上，一个文件，一张图片、一段语音都可以被称为一种资源，那服务器上海量的资源，如何快速找到它呢？不管用什么方法表示，只要能唯一标识一个资源，这个标识符 Identifier 就叫URI。

通常会有两种方法来实现定位，一种是用 URL 地址定位；另一种是用 URN 名称定位。

举个例子：去村子找个具体的人（URI），如果用地址：某村多少号房子第几间房的主人就是URL， 如果用身份证号+名字 去找就是URN了。

所以这三者概念上的关系是 uri 包括 url 和 urn。URI 只是一个抽象的定义，URL 和 RNN 是具体实现。

```
+------------------------------------------------+
|                                                |
|  URI(Uniform Resource Identifier)              |
|                                                |
|   +--------------------------------------+     |
|   |                                      |     |
|   | URL(Uniform Resource Locator)        |     |
|   | eg:ftp://192.168.0.111/index.html    |     |
|   | eg:https://blog.csdn.net/index.html  |     |
|   |                                      |     |
|   +--------------------------------------+     |
|                                                |
|   +---------------------------------------+    |
|   | URN(Uniform Resource Name)            |    |
|   | eg:isbn:7-5387-1705-6                 |    |
|   |                                       |    |
|   +---------------------------------------+    |
|                                                |
+------------------------------------------------+

```

只是在互联网上 urn 没流行起来，导致几乎目前所有的 uri 都是以 url 形式表示，比如定位服务器上的一个文件，如果是在本地环境下，可以使用 `ftp://192.168.0.111/index.html`，如果是在 web 环境下，可以使用 `https://blog.csdn.net/index.html`。

但是在现实场景中，urn 却被广泛使用，比如图书的 ISBN 编码就是 urn 的例子 `isbn:7-5387-1705-6`。

> 国际标准书号（International Standard Book Number），简称ISBN，是专门为识别图书等文献而设计的国际编号.2007年1月1日之前，ISBN由10位数字组成，分四个部分：组号（国家、地区、语言的代号），出版者号，书序号和检验码。中国的组号为7.

## URL 格式

通常情况下，一个 URL 是一个特定格式的字符串，它包含多个部分。基本格式如下：

```
scheme:[//[user:password@]host[:port]][/]path[?query][#fragment]

```

每一部分表示的名称如下：

```
"  https:   //    user   :   pass   @ sub.example.com : 8080   /p/a/t/h  ?  query=string   #fragment "
│          │  │          │          │                 │      │          │ │              │           │
│ scheme   │  │ username │ password │    hostname     │ port │ path     │ │    query     │ fragment  │
└──────────┴──┴──────────┴──────────┴────────────────────────┴──────────┴─┴──────────────┴───────────┘
```

## java.net.URI / URL

在 Java 语言中，`java.net.URI` 和 `java.net.URL` 类都提供对 URL 地址的解析功能。但它们的设计初衷和适用场景有着本质的区别。简单来说：

- URI 是为了“解析和操作字符串”；
- URL 是为了“发起网络请求”。

`java.net.URI` 类提供了丰富的方法来构建URI对象以及获取其各个组成部分的信息。

| 方法类别      | java.net.URI (侧重解析与操作)          | java.net.URL (侧重网络访问)              |
| :------------ | :------------------------------------- | :--------------------------------------- |
| 构造方法      | 支持多参数构造，语法严格，非法会抛异常 | 依赖协议处理器，对非法字符容忍度高       |
| 获取协议      | `getScheme()`                          | `getProtocol()`                          |
| 获取主机      | `getHost()`                            | `getHost()`                              |
| 获取端口      | `getPort()` (未指定返回 -1)            | `getPort()` (未指定返回 -1)              |
| 获取路径      | `getPath()` / `getRawPath()`           | `getPath()` / `getFile()` 会带上查询参数 |
| 获取查询参数  | `getQuery()` / `getRawQuery()`         | `getQuery()`                             |
| 获取锚点/片段 | `getFragment()` / `getRawFragment()`   | `getRef()`                               |
| 获取授权信息  | `getAuthority()` / `getUserInfo()`     | `getAuthority()` / `getUserInfo()`       |
| 网络操作      | 无                                     | `openConnection()` / `openStream()`      |
| 路径解析      | `resolve()` (处理相对路径)             | 无                                       |
| 对象转换      | `toURL()` (转为 URL 对象)              | `toURI()` (转为 URI 对象)                |
| 相等性判断    | 纯字符串比较，安全且快速               | 会触发 DNS 解析，严禁在集合中使用        |

```java
import java.net.URI;

public class URIParserDemo {
    public static void main(String[] args) {
        try {
            // 包含完整结构的 URL 示例
            String urlString = "https://www.example.com:443/path/to/resource?name=John&age=25#section";
            URL url = new URI(urlString);

            System.out.println("协议 (getScheme): " + url.getScheme()); // https
            System.out.println("主机 (Host): " + url.getHost());         // www.example.com
            System.out.println("端口 (Port): " + url.getPort());         // 443 (未指定时返回 -1)
            System.out.println("路径 (Path): " + url.getPath());         // /path/to/resource
            System.out.println("查询 (Query): " + url.getQuery());       // name=John&age=25
            System.out.println("锚点 (getFragment): " + url.getFragment());           // section
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

URI 类的部分方法有带 `Raw` 和不带 `Raw` 两种版本（例如 `getQuery()` 和 `getRawQuery()`）。

- 不带 Raw：会自动对百分号编码的字符进行解码（例如将 %E4%B8%AD 变为“中”）。
- 带 Raw：返回原始的、未解码的字符串（例如保留 q=hello%20world）。

这在处理中文参数或特殊符号时非常有用，能让你明确知道当前获取的是编码前还是编码后的内容。

### URI 和 URL 的区别

| 维度         | java.net.URI                            | java.net.URL                                       |
| :----------- | :-------------------------------------- | :------------------------------------------------- |
| 核心定位     | 纯粹的字符串解析、验证与操作            | 网络资源定位与内容获取（发起网络请求）             |
| 标准兼容性   | 严格遵循 RFC 2396/3986 标准，解析更严谨 | 相对宽松，更偏向于应用层协议的实际需求             |
| 性能与安全性 | 纯内存操作，快速且安全                  | `equals()` 和 `hashCode()` 会触发 DNS 解析，易阻塞 |
| 包含关系     | 包含 URL 和 URN（统一资源名称）         | URI 的一个子集，必须包含访问协议（如 http）        |

在实际开发中，最容易踩的坑就是使用 java.net.URL 的 `equals()` 方法。

URL 类的 `equals()` 方法在比较两个对象时，不仅会对比协议、主机名、端口等字符串信息时，如果发现主机名不同，会尝试通过 DNS 解析去判断它们是否指向同一个 IP 地址。
这意味着，如果你把 URL 对象放进 HashMap 或 HashSet 中（这些数据结构底层依赖 `equals()` 和 `hashCode()`），一旦遇到网络波动或 DNS 服务器响应慢，你的程序就会发生严重的性能阻塞，甚至卡死。而 URI 类只进行纯粹的字符串比较，完全没有这个风险。因此，永远不要把 URL 对象当作 HashMap 的 Key 或者放入 HashSet 中，请使用 URI 来代替。

## 编码和解码

URL中只能包含特定的ASCII字符，如果路径或查询参数中包含中文、空格或其他特殊字符，必须进行编码。

- 编码工具：使用 java.net.URLEncoder.encode(String, "UTF-8") 对参数值进行百分号编码（Percent-Encoding）。
- 解码工具：使用 java.net.URLDecoder.decode(String, "UTF-8") 将接收到的编码字符串还原。
- 注意事项：通常只对URL中的查询参数（Query Parameter）部分进行整体编码，避免对协议头（://）或路径分隔符（/）进行重复编码导致链接失效。

```java
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class URLCodingDemo {
    public static void main(String[] args) {
        try {
            String originalParam = "Java 开发工程师 & URL 处理";

            // URL 编码 (将空格、特殊字符、中文等转换为 %XX 格式)
            String encodedParam = URLEncoder.encode(originalParam, StandardCharsets.UTF_8.toString());
            System.out.println("编码后: " + encodedParam);
            // 输出类似: Java+%E5%BC%80%E5%8F%91%E5%B7%A5%E7%A8%8B%E5%B8%88+%26+URL+%E5%A4%84%E7%90%86

            // URL 解码 (将 %XX 格式还原为原始字符)
            String decodedParam = URLDecoder.decode(encodedParam, StandardCharsets.UTF_8.toString());
            System.out.println("解码后: " + decodedParam);
            // 输出: Java 开发工程师 & URL 处理
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## 查询参数处理

目前 Java 中没有内置的解析查询参数的方法。如果需要解析查询参数，需要自行实现，或者引入外部包进行处理。

如果手动从 URL 字符串中提取参数，需要的处理流程是：获取 Query 字符串 -> 按 & 分割 -> 按 = 分割 -> URL 解码。

```java
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class QueryParser {
    public static void main(String[] args) throws Exception {
        String urlStr = "https://www.example.com/path?name=John%20Doe&age=30&city=%E5%8C%97%E4%BA%AC";

        // 1. 推荐使用 URI 来获取 Query 字符串（比 URL 类解析更严格、标准）
        URI uri = new URI(urlStr);
        String query = uri.getQuery(); // 获取到：name=John%20Doe&age=30&city=%E5%8C%97%E4%BA%AC

        Map<String, String> paramMap = new HashMap<>();
        if (query != null) {
            // 2. 按 & 分割出各个键值对
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                // 3. 提取 key 和 value，并进行 URL 解码（处理中文和特殊字符）
                String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
                String value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
                paramMap.put(key, value);
            }
        }

        System.out.println("name: " + paramMap.get("name")); // 输出：John Doe
        System.out.println("city: " + paramMap.get("city")); // 输出：北京
    }
}
```

在实际的企业级开发中，为了避免重复造轮子以及处理各种复杂的边界情况（比如参数缺失、特殊符号等），通常会引入成熟的工具库。

- Spring 框架内提供了 `HttpServletRequest` 类提供了对应的方法获取，比如 `getParameter("name")`。
- Apache HttpClient 提供了 `org.apache.http.client.utils.URIBuilder` 类，用于构建 URL。
