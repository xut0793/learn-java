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
      copyright: "Copyright © 2025-present",
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
          text: "Java SE",
          items: [
            { text: "Java 语言简介", link: "/java_se/java_intro.md" },
            { text: "Java 安装及环境配置", link: "/java_se/java_install.md" },
            {
              text: "Java 基本程序结构",
              link: "/java_se/java_basic_program.md",
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
