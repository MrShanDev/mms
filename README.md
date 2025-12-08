<div align="center">
   <br/> 
   <a href="#">
     <img width="150" src="https://mmsadmin.cn/logo.png">
   </a>
   <h1>模块化管理系统</h1>
   <br/>
</div>

## ⚡️系统介绍

🔥🔥🔥模块化管理系统（Modular management system），简称：MMS，是一款基于多应用模块用户、商品、支付、订单、分销、日志、定时、通信、直播、广告、文章等多模块应用开源系统，可快速的应用与各类项目研发中，定期更新功能修复、上新、技术栈分享 (十年磨一剑，做最有价值的开源项目)！

> 项目代码、文档 均开源免费可商用 ,活到老写到老 为兴趣而开源 为学习而开源.

🍃系统演示: [传送门](https://mmsadmin.cn/index/demo.html)

🍃MMS文档: [mmsAdmin](https://mmsadmin.cn/)

## 🧩 Wings模块化架构

MMS采用Wings模块化架构设计理念，通过高度解耦的模块化设计，实现系统的灵活性和可扩展性。每个模块都是独立的功能单元，可以单独开发、测试和部署，同时又能无缝集成到整个系统中。

### 架构优势

- **高内聚低耦合**：每个模块专注于特定功能领域，职责清晰
- **灵活扩展**：可根据业务需求选择性启用或禁用模块
- **易于维护**：模块独立性使得系统维护更加简单
- **团队协作**：不同团队可以并行开发不同模块
- **技术多样性**：不同模块可以采用最适合的技术栈

## 🧩系统版本

<img src="https://img.shields.io/badge/MMS-V1.0.6--Beta-green"/>

| 名称     | 别名  |                     项目地址                     | 注意事项                                                                |
|--------|:---:|:--------------------------------------------:|---------------------------------------------------------------------|
| mms    | 标准版 |  - [Gitee](https://gitee.com/mmsAdmin/mms)   | 🙋功能齐全的手架系统 <br/> 📢完全具备高效的项目开发<br/> 📢完多租户模式灵活开启<br/>📢支持低代码自动生成模式 |
| mms-ui | 标准版 | - [Gitee](https://gitee.com/mmsAdmin/mms-ui) | 🙋适配mms后端系统的管理界面项目                                                  |

## 📦开发语言

<div style="text-align: center;float: left;width: 100%;">
   <img style="margin: 5px ;float: left;height: 20px" src="https://img.shields.io/badge/language-JAVA-<COLOR>.svg" alt=""/>
   <img style="margin: 5px ;float: left;height: 20px"  src="https://img.shields.io/badge/language-Docker-pink.svg" alt=""/>
   <img style="margin: 5px ;float: left;height: 20px"  src="https://img.shields.io/badge/language-Vue3.2-34495e?logo=vue.j" alt="vue" />
   <img style="margin: 5px ;float: left;height: 20px"  src="https://img.shields.io/badge/language-Vite4-646cff?logo=vite&logoColor=white" alt="vite" />
   <img style="margin: 5px ;float: left;height: 20px"  src="https://img.shields.io/badge/language-TypeScript4.9-blue?logo=typescript&logoColor=white" alt="typescript" />
   <img style="margin: 5px ;float: left;height: 20px"  src="https://img.shields.io/badge/language-Pinia2-yellow?logo=picpay&logoColor=white" alt="Pinia2" />
   <img style="margin: 5px ;float: left;height: 20px"  src="https://img.shields.io/badge/language-ESLint-4b32c3?logo=eslint&logoColor=white" alt="eslint" />
   <img style="margin: 5px ;float: left;height: 20px"  src="https://img.shields.io/badge/language-pnpm7-F69220?logo=pnpm&logoColor=white" alt="pnpm" />
   <img style="margin: 5px ;float: left;height: 20px"  src="https://img.shields.io/badge/language-Prettier-ef9421?logo=Prettier&logoColor=white" alt="Prettier">
   <img style="margin: 5px ;float: left;height: 20px"  src="https://img.shields.io/badge/language-Sass-1D365D?logo=Sass&logoColor=white" alt="Sass">
   <img style="margin: 5px ;float: left;height: 20px"  src="https://img.shields.io/badge/language-Wind%20CSS-06B6D4?logo=Tailwind%20CSS&logoColor=white" alt="WindCSS">
</div>

🙋高效安全、组件解耦、灵活扩展 模块化扩展内置代码生成引擎加速后台系统构建。

## 🍃部署方式
<img src="https://img.shields.io/docker/automated/tsund/tianchi_docker_practice.svg" alt=""/>

## 🤝模块介绍

| 序号 | 目录          | 子模块名称             | 模块名称                             | 备注    |
|----|-------------|-------------------|----------------------------------|-------|
| 1  | mms-admin   |                   | 系统管理启动模块                         | 已完成   |
| 1  | mms-docs    |                   | VitePress会员主题（知识付费）<br>MMS适配接口服务 | 已完成   |
| 5  | ｜________   | mms-doc-admin     | 会员主题后端模块                         | 已完成   |
| 6  | ｜________   | mms-doc-api       | 会员主题接口模块                         | 已完成   |
| 2  | mms-malls   |                   | 商城                               | 开发中   |
| 5  | ｜________   | mms-mall-admin    | 商城后端模块                           | 开发中   |
| 6  | ｜________   | mms-mall-api      | 商城接口模块                           | 开发中   |
| 5  | ｜________   | mms-mall-merchant | 商城商户模块                           | 开发中   |
| 6  | ｜________   | mms-doc-uni-x     | 商城移动模块                           | 开发中   |
| 4  | mms-modules |                   | MMS模块集                           | 持续增加中 |
| 5  | ｜________   | mms-ai            | 大模型模块                            | 已完成   |
| 5  | ｜________   | mms-aliyun        | 阿里云模块                            | 已完成   |
| 5  | ｜________   | mms-authority     | 安全认证模块                           | 已完成   |
| 5  | ｜________   | mms-common        | 公共模块                             | 已完成   |
| 5  | ｜________   | mms-datasource    | 数据源模块                            | 已完成   |
| 5  | ｜________   | mms-demo          | 演示模块                             | 已完成   |
| 5  | ｜________   | mms-email         | 邮箱模块                             | 已完成   |
| 6  | ｜________   | mms-framework     | 核心模块                             | 已完成   |
| 5  | ｜________   | mms-gen           | 代码生成模块                           | 已完成   |
| 7  | ｜________   | mms-mq            | 消息队列                             | 已完成   |
| 5  | ｜________   | mms-oss           | 对象存储模块                           | 已完成   |
| 5  | ｜________   | mms-redis         | 缓存模块                             | 已完成   |
| 5  | ｜________   | mms-sms           | 短信模块                             | 已完成   |
| 5  | ｜________   | mms-thymeleaf     | 模版引擎渲染模块                         | 已完成   |
| 8  | ｜________   | mms-websocket     | 长连接通信管理                          | 已完成   |
| 5  | ｜________   | mms-wx            | 微信模块                             | 已完成   |
| 9  | mms-zoom    |                   | 快速激增模块集                          | 已完成   |
| 10 | ｜________   | mms-monitor       | 安全监控                             | 已完成   |
| 11 | ｜________   | mms-powerjob      | 定时任务                             | 已完成   |
| 12 | script      |                   | 项目配置文件                           | 已完成   |

## 🔧 Wings架构核心模块

###  foundation层 (基础模块)
| 模块名称        | 功能描述                                   |
|-------------|----------------------------------------|
| mms-common  | 提供公共工具类、常量、枚举等基础组件                  |
| mms-framework | 核心框架模块，包含统一异常处理、拦截器、基础控制器等       |

### 🔐 认证授权层
| 模块名称           | 功能描述                          |
|----------------|-------------------------------|
| mms-authority  | 安全认证模块，基于Sa-Token实现权限控制      |
| mms-redis      | Redis缓存模块，支持分布式缓存和会话管理    |

### 💾 数据访问层
| 模块名称            | 功能描述                                |
|-----------------|-------------------------------------|
| mms-datasource  | 数据源模块，集成MyBatis-Plus和动态数据源    |
| mms-mq          | 消息队列模块，集成RabbitMQ等消息中间件        |

### ☁️ 云服务层
| 模块名称         | 功能描述                       |
|--------------|----------------------------|
| mms-aliyun   | 阿里云服务集成模块                |
| mms-oss      | 对象存储模块，支持多种云存储服务       |
| mms-sms      | 短信服务模块，集成多家短信服务商        |
| mms-email    | 邮件服务模块，支持多种邮件发送方式       |
| mms-wx       | 微信服务模块，集成微信公众号和小程序API  |

### 🛠 功能扩展层
| 模块名称          | 功能描述                          |
|---------------|-------------------------------|
| mms-gen       | 代码生成模块，支持低代码开发            |
| mms-ai        | AI大模型集成模块                   |
| mms-demo      | 演示模块，提供各种功能示例              |
| mms-websocket | WebSocket长连接通信模块             |
| mms-thymeleaf | Thymeleaf模板引擎模块              |

### 📊 监控运维层
| 模块名称           | 功能描述                    |
|----------------|-------------------------|
| mms-monitor    | 系统监控模块，集成Spring Boot Admin |
| mms-powerjob   | 分布式定时任务模块               |

## 📄软件架构

软件架构说明

| 框架                                                                   | 说明                    | 版本            | 说明       |
|----------------------------------------------------------------------|-----------------------|---------------|----------|
| [SpringBoot](https://spring.io/projects/spring-boot/#learn)          | 后端主框架                 | 3.2.6         | 后端主框架    |
| [Undertow](https://undertow.io/)                                     | 基于 XNIO 的高性能容器        | 2.7.6         | Web服务器   |
| [Sa-Token](https://sa-token.dev33.cn/)                               | Sa-Token、Jwt(强解耦、强扩展) | 1.35.0.RC     | 权限认证框架   |
| [MySQL](https://dev.mysql.com/)                                      | 关系数据库                 | 8.2.0         | 数据库      |
| [Redis](https://redis.io/)                                           | 缓存数据库                 | 6.X+          | 缓存数据库    |
| [Mybatis-Plus](https://baomidou.com/guide/)                          | 快速 CRUD 增加开发效率        | 3.5.7         | ORM框架    |
| [Vue](https://staging-cn.vuejs.org/)                                 | vue 框架                | 3.2.45        | 前端框架     |
| [Vite](https://cn.vitejs.dev//)                                      | 开发与构建工具               | 4.0.4         | 构建工具     |
| [Element Plus](https://element-plus.org/zh-CN/)                      | Element Plus          | 2.2.28        | UI组件库    |
| [TypeScript](https://www.typescriptlang.org/docs/)                   | JavaScript 的超集        | 4.9.4         | 编程语言     |
| [pinia](https://pinia.vuejs.org/)                                    | Vue 存储库 替代 vuex5      | 2.0.28        | 状态管理     |
| [vueuse](https://vueuse.org/)                                        | 常用工具集                 | 9.10.0        | 工具库      |
| [vxe-table](https://vxetable.cn/)                                    | vue 最强表单              | 4.3.7         | 表格组件     |
| [vue-i18n](https://kazupon.github.io/vue-i18n/zh/introduction.html/) | 国际化                   | 9.2.2         | 国际化      |
| [vue-router](https://router.vuejs.org/)                              | vue 路由                | 4.1.6         | 路由管理     |
| [windicss](https://cn.windicss.org/)                                 | 下一代工具优先的 CSS 框架       | 3.5.6         | CSS框架    |
| [iconify](https://icon-sets.iconify.design/)                         | 在线图标库                 | 3.0.1         | 图标库      |
| [wangeditor](https://www.wangeditor.com/)                            | 富文本编辑器                | 5.1.23        | 富文本编辑器   |

## 🎳演示图例

<table>
  <tr>
   <th><p>登录页面</p></th>
   <th><p>后台首页</p></th>
  </tr>
  <tr>
   <th><img src="https://mmsadmin.cn/images/mms/01.png"/></th>
   <th><img src="https://mmsadmin.cn/images/mms/02.png"/></th>
  </tr>
<tr>
   <th><p>用户管理</p></th>
   <th><p>新增用户</p></th>
  </tr>
  <tr>
   <th><img src="https://mmsadmin.cn/images/mms/03.png"/></th>
   <th><img src="https://mmsadmin.cn/images/mms/04.png"/></th>
  </tr>
<tr>
   <th><p>角色管理</p></th>
   <th><p>添加角色</p></th>
  </tr>
  <tr>
   <th><img src="https://mmsadmin.cn/images/mms/05.png"/></th>
   <th><img src="https://mmsadmin.cn/images/mms/06.png"/></th>
  </tr>
<tr>
   <th><p>菜单管理</p></th>
   <th><p>部门管理</p></th>
  </tr>
  <tr>
   <th><img src="https://mmsadmin.cn/images/mms/07.png"/></th>
   <th><img src="https://mmsadmin.cn/images/mms/08.png"/></th>
  </tr>
<tr>
   <th><p>字典管理</p></th>
   <th><p>添加字典</p></th>
  </tr>
  <tr>
   <th><img src="https://mmsadmin.cn/images/mms/09.png"/></th>
   <th><img src="https://mmsadmin.cn/images/mms/10.png"/></th>
  </tr>
<tr>
   <th><p>系统设置</p></th>
   <th><p>消息公告</p></th>
  </tr>
  <tr>
   <th><img src="https://mmsadmin.cn/images/mms/11.png"/></th>
   <th><img src="https://mmsadmin.cn/images/mms/12.png"/></th>
  </tr>
<tr>
   <th><p>代码生成1</p></th>
   <th><p>代码生成2</p></th>
  </tr>
  <tr>
   <th><img src="https://mmsadmin.cn/images/mms/13.png"/></th>
   <th><img src="https://mmsadmin.cn/images/mms/14.png"/></th>
  </tr>
<tr>
   <th><p>代码生成3</p></th>
   <th><p>代码生成4</p></th>
  </tr>
  <tr>
   <th><img src="https://mmsadmin.cn/images/mms/15.png"/></th>
   <th><img src="https://mmsadmin.cn/images/mms/16.png"/></th>
  </tr>
<tr>
   <th><p>定时任务</p></th>
   <th><p>对象存储</p></th>
  </tr>
  <tr>
   <th><img src="https://mmsadmin.cn/images/mms/17.png"/></th>
   <th><img src="https://mmsadmin.cn/images/mms/18.png"/></th>
  </tr>
<tr>
   <th><p>扩展工具</p></th>
   <th><p>个人中心</p></th>
  </tr>
  <tr>
   <th><img src="https://mmsadmin.cn/images/mms/19.png"/></th>
   <th><img src="https://mmsadmin.cn/images/mms/20.png"/></th>
  </tr>
</table>

# 😎 LICENSE

[https://gitee.com/mmsAdmin/mms/blob/master/LICENSE](https://gitee.com/mmsAdmin/mms/blob/master/LICENSE)

## ❌免责条款

&emsp;&emsp;您充分了解并同意，您必须为自己使用本服务及注册帐号下的一切行为负责，包括您所发表的任何内容以及由此产生的任何后果。您应对本服务中的内容自行加以判断，并自行承担因使用内容而引起的所有风险。

&emsp;&emsp;[mms](https://gitee.com/mmsAdmin/mms),[mms-ui](https://gitee.com/mmsAdmin/mms-ui) 对网站上所显示的信息或资料的准确性、内容、完整性、合法性、可靠性、可操作性或可用性不承担任何责任。

&emsp;&emsp;[mms](https://gitee.com/mmsAdmin/mms),[mms-ui](https://gitee.com/mmsAdmin/mms-ui) 二次开发使用者因为违法而触犯中华人民共和国法律的，一切后果自己负责，`mms 作者` 不承担任何责任。

&emsp;&emsp;本声明未涉及的问题参见国家有关法律法规，当本声明与国家法律法规冲突时，以国家法律法规为准。

## 🧪学习 & 商用

&emsp;&emsp;[mms](https://gitee.com/mmsAdmin/mms),[mms-ui](https://gitee.com/mmsAdmin/mms-ui) 是免费和开源的，可免费用于 `学习`、`商业使用` 。