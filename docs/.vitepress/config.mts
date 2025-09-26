import { defineConfig } from "vitepress"
import markdownItTaskLists from "markdown-it-task-lists"

// https://vitepress.dev/reference/site-config
export default defineConfig({
  base: "/learn-java/", // 配合 github pages 配置路径前缀
  title: "Learn Java",
  description: "Java 自学总结",
  lang: "zh-CN",
  head: [["link", { rel: "icon", href: "/java.svg" }]],
  ignoreDeadLinks: true,
  themeConfig: {
    // https://vitepress.dev/reference/default-theme-config
    logo: "/java.svg",
    siteTitle: "Learn Java",
    socialLinks: [
      { icon: "github", link: "https://github.com/xut0793/learn-java.git" },
    ],
    footer: {
      message: "PowerBy xut0793",
      copyright: "Copyright ©  2025年7月30日18:32:16-present",
    },
    nav: [
      { text: "Home", link: "/" },
      { text: "Java SE", link: "/java_se/index.md" },
      { text: "Java EE", link: "/java_ee/index.md" },
    ],

    sidebar: {
      "/java_se/": [
        {
          text: "导论",
          items: [
            {
              text: "如何学习编程",
              link: "/java_se/how_to_learn_programming_language.md",
            },
          ],
        },
        {
          text: "Java 概要",
          items: [
            { text: "Java 语言简介", link: "/java_se/java_intro.md" },
            { text: "Java 安装及环境配置", link: "/java_se/java_install.md" },
            {
              text: "Java 基本程序结构",
              link: "/java_se/java_basic_program.md",
            },
          ],
        },
        {
          text: "数据的表达",
          items: [
            { text: "数据类型", link: "/java_se/data_type.md" },
            { text: "数据引用", link: "/java_se/data_reference.md" },
            { text: "数据转换", link: "/java_se/data_conversion.md" },
            { text: "数据运算", link: "/java_se/data_operation.md" },
            {
              text: "数据抽象（数据结构）",
              link: "/java_se/data_structure.md",
            },
            {
              text: "数据的真相：二进制",
              items: [
                {
                  text: "整数",
                  link: "/java_se/data_truth_about_int.md",
                },
                {
                  text: "小数",
                  link: "/java_se/data_truth_about_float.md",
                },
                {
                  text: "字符",
                  link: "/java_se/data_truth_about_char.md",
                },
              ],
            },
          ],
        },
        {
          text: "流程控制",
          items: [
            {
              text: "条件语句 if",
              link: "/java_se/control_statement_condition_if.md",
            },
            {
              text: "条件语句 switch",
              link: "/java_se/control_statement_condition_switch.md",
            },
            { text: "循环语句", link: "/java_se/control_statement_loop.md" },
            { text: "跳转语句", link: "/java_se/control_statement_jump.md" },
            {
              text: "流程控制的真相",
              link: "/java_se/control_statement_truth.md",
            },
          ],
        },
        {
          text: "面向对象编程",
          items: [
            {
              text: "理解面向对象",
              link: "/java_se/oop_index.md",
            },
            {
              text: "类 Class",
              link: "/java_se/oop_class.md",
            },
            {
              text: "对象：引用类型",
              link: "/java_se/oop_reference.md",
            },
            {
              text: "构造器",
              link: "/java_se/oop_constructor.md",
            },
            {
              text: "成员变量",
              link: "/java_se/oop_field.md",
            },
            {
              text: "方法",
              link: "/java_se/oop_method.md",
            },
            {
              text: "封装 Encapsulation",
              link: "/java_se/oop_encapsulation.md",
            },
            {
              text: "继承 Inheritance",
              link: "/java_se/oop_inheritance.md",
            },
            {
              text: "多态 Polymorphism",
              link: "/java_se/oop_polymorphism.md",
            },
            {
              text: "组合 Composition",
              link: "/java_se/oop_composition.md",
            },
            {
              text: "抽象类 Abstract",
              link: "/java_se/oop_abstract.md",
            },
            {
              text: "接口 Interface",
              link: "/java_se/oop_interface.md",
            },
            {
              text: "内部类 Inner Class",
              link: "/java_se/oop_inner_class.md",
            },
            {
              text: "内部接口 Inner Interface",
              link: "/java_se/oop_inner_interface.md",
            },
            {
              text: "泛型 Generic",
              link: "/java_se/oop_generic.md",
            },
            {
              text: "反射 Reflection",
              link: "/java_se/oop_reflection.md",
            },
            {
              text: "代理 Proxy",
              link: "/java_se/oop_proxy.md",
            },
            {
              text: "注解 Annotation",
              link: "/java_se/oop_annotation.md",
            },
          ],
        },
        {
          text: "异常",
          items: [
            {
              text: "异常：抛出、捕获、处理",
              link: "/java_se/exception.md",
            },
            {
              text: "日志：记录错误",
              link: "/java_se/exception_log.md",
            },
            {
              text: "调试：排查错误",
              link: "/java_se/exception_debug.md",
            },
            {
              text: "测试：预防错误",
              link: "/java_se/exception_test.md",
            },
          ],
        },
        {
          text: "工程化",
          items: [
            {
              text: "包 package",
              link: "/java_se/project_package.md",
            },
            {
              text: "jar",
              link: "/java_se/project_jar.md",
            },
            {
              text: "类路径 Classpath",
              link: "/java_se/project_classpath.md",
            },
            {
              text: "模块 module",
              link: "/java_se/project_module.md",
            },
            {
              text: "文档 Javadoc",
              link: "/java_se/project_javadoc.md",
            },
            {
              text: "Maven",
              link: "/java_se/project_maven.md",
            },
            {
              text: "Gradle",
              link: "/java_se/project_gradle.md",
            },
          ],
        },
        {
          text: "内置类",
          items: [
            {
              text: "包装类",
              link: "/java_se/builtin_class_wrapper.md",
            },
            {
              text: "String 字符串",
              link: "/java_se/builtin_string.md",
            },
            {
              text: "Array 数组",
              link: "/java_se/builtin_array.md",
            },
            {
              text: "Enum 枚举",
              link: "/java_se/builtin_enum.md",
            },
            {
              text: "Record 记录",
              link: "/java_se/builtin_record.md",
            },
            {
              text: "Sealed Classes 密封类",
              link: "/java_se/builtin_sealed_classes.md",
            },
            {
              text: "Collection 集合",
              link: "/java_se/builtin_collection.md",
            },
            // 工具类
            {
              text: "Date & Time 日期时间",
              link: "/java_se/builtin_date_time.md",
            },
            {
              text: "Math 数学",
              link: "/java_se/builtin_math.md",
            },
            {
              text: "Random 随机数",
              link: "/java_se/builtin_random.md",
            },
            {
              text: "Regexp 正则表达式",
              link: "/java_se/builtin_regexp.md",
            },
          ],
        },
      ],
      "/java_ee/": [
        {
          text: "Java EE",
          items: [],
        },
      ],
    },
  },
  markdown: {
    config: (md) => {
      md.use(markdownItTaskLists)
    },
  },
})
