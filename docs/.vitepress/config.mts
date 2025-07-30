import { defineConfig } from "vitepress"
import markdownItTaskLists from "markdown-it-task-lists"

// https://vitepress.dev/reference/site-config
export default defineConfig({
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
      message: "PowerBy tao.xu",
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
          text: "Java SE",
          items: [
            {
              text: "如何学习编程",
              link: "/java_se/how_to_learn_programming_language.md",
            },
            { text: "Java 语言简介", link: "/java_se/java_intro.md" },
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
