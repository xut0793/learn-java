---
# https://vitepress.dev/reference/default-theme-home-page
layout: home

hero:
  name: "Learn Java"
  text: ""
  tagline: Java 学习总结
  image:
    src: /java.svg
    alt: "java logo"
  actions:
    - theme: brand
      text: 开始
      link: /java_se/index.md
features:
  - title: Java SE
    details: 基础语法、数据类型、控制流程、面向对象、异常处理、IO 流、泛型、反射、注解等。
  - title: Java EE
    details: 数据库JDBC、Servlet、JSP、SSM(Spring、SpringMVC、Mybatis) 等。
---

<style>
:root {
  --vp-home-hero-name-color: transparent;
  --vp-home-hero-name-background: linear-gradient(120deg, #2365c4 30%, #ff1515);
  --vp-home-hero-image-background-image: linear-gradient(
    -45deg,
    #2365c4 60%,
    #ff1515 30%
  );
  --vp-home-hero-image-filter: blur(100px);
}

@media (min-width: 640px) {
  :root {
    --vp-home-hero-image-filter: blur(56px);
  }
}

@media (min-width: 960px) {
  :root {
    --vp-home-hero-image-filter: blur(68px);
  }
}
</style>
