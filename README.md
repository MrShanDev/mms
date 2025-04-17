<div align="center">
   <br/> 
   <a href="#">
     <img width="150" src="https://sxpcwlkj.oss-accelerate-overseas.aliyuncs.com/doc/logo.png" alt="">
   </a>
   <h1>模块化管理系统</h1>
   <br/>
</div>

## ⚡️系统介绍

🔥🔥🔥模块化管理系统（Modular management
system），简称：MMS，是一款基于多应用模块用户、商品、支付、订单、分销、日志、定时、通信、直播、广告、文章等多模块应用开源系统，可快速的应用与各类项目研发中，定期更新功能修复、上新、技术栈分享 (
十年磨一剑，做最有价值的开源项目)！


> MMS 是借鉴优秀的 RuoYi-Vue-Plus 项目架构思想，80%的架构重构，结合自己多年的后端系统开发打造的一款功能丰富的模块化系统（更懂程序员的系统）

> 项目代码、文档 均开源免费可商用 ,活到老写到老 为兴趣而开源 为学习而开源.

系统演示: [传送门](https://demo.mmsadmin.cn)

后端项目地址: [mms](https://gitee.com/mmsAdmin/mms)

前端项目地址: [mms-ui](https://gitee.com/mmsAdmin/mms-ui)

文档地址: [mms-doc](https://mmsadmin.cn)


## 🧩系统版本

<img src="https://img.shields.io/badge/MMS-V1.X-green"/>

| 名称      | 别名  |                      项目地址                      | 注意事项                                                                                                       |
|---------|:---:|:----------------------------------------------:|------------------------------------------------------------------------------------------------------------|
| mms     | 基础版 |   - [Gitee](https://gitee.com/mmsAdmin/mms)    | 适用：项目快速管理系统开发的脚手架系统                                                                                        |
| mmsMall | 商城版 | - [Gitee](https://gitee.com/mmsAdmin/mms-mall) | 适用：具备商城项目常用功能App、小程序、公众号  <br/>🙋‍♂️若遇到问题请联系我们（备注：mms）<br>📢微信号：qq942879858<br>📢Q&nbsp;&nbsp;Q号：942879858 |

## 📦开发语言

<div style="text-align: center;float: left;width: 100%">
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

## 🍃部署方式
<img src="https://img.shields.io/docker/automated/tsund/tianchi_docker_practice.svg" alt=""/>

## 🤝模块介绍

| 序号 | 目录           | 子模块名称         | 模块名称      | 备注    |
|----|--------------|---------------|-----------|-------|
| 1  | mms-admin    |               | 系统管理启动模块  | 已完成   |
| 2  | mms-merchant |               | 商户管理启动模块  | 待完成   |
| 3  | mms-mobile   |               | 移动端接口启动模块 | 已完成   |
| 4  | mms-modules  |               | MMS模块集    | 持续增加中 |
| 5  | ｜________    | mms-common    | 公共模块      | 已完成   |
| 6  | ｜________    | mms-framework | 核心模块      | 已完成   |
| 7  | ｜________    | mms-mq        | 消息队列      | 已完成   |
| 8  | ｜________    | mms-system    | 系统管理      | 已完成   |
| 9  | mms-patulous |               | 第三方模块集    | 已完成   |
| 10 | ｜________    | mms-generator | 代码生成      | 已完成   |
| 11 | ｜________    | mms-powerjob  | 定时任务      | 已完成   |
| 12 | ui-admin     |               | 系统管理前端    | 已完成   |
| 13 | ui-mobile    |               | 移动端       | 已完成   |
| 14 | ui-tenant    |               | 商户管理前端    | 待完成   |

## 📄软件架构

软件架构说明

| 框架                                                                   | 说明                    | 版本            | 说明    |
|----------------------------------------------------------------------|-----------------------|---------------|-------|
| [SpringBoot](https://spring.io/projects/spring-boot/#learn)          | 后端主框架                 | 3.X           | 3.1.5 
| [Undertow](https://undertow.io/)                                     | 基于 XNIO 的高性能容器        | 2.7.6         |
| [Sa-Token](https://sa-token.dev33.cn/)                               | Sa-Token、Jwt(强解耦、强扩展) | 1.33.0        |
| [MySQL](https://dev.mysql.com/)                                      | 关系数据库                 | 适配 8.X 最低 5.7 |
| [Redis](https://redis.io/)                                           | 缓存数据库                 | 适配 6.X 最低 4.X |
| [Mybatis-Plus](https://baomidou.com/guide/)                          | 快速 CRUD 增加开发效率        | 3.5.4         |
| [Vue](https://staging-cn.vuejs.org/)                                 | vue 框架                | 3.2.45        |
| [Vite](https://cn.vitejs.dev//)                                      | 开发与构建工具               | 4.0.4         |
| [Element Plus](https://element-plus.org/zh-CN/)                      | Element Plus          | 2.2.28        |
| [TypeScript](https://www.typescriptlang.org/docs/)                   | JavaScript 的超集        | 4.9.4         |
| [pinia](https://pinia.vuejs.org/)                                    | Vue 存储库 替代 vuex5      | 2.0.28        |
| [vueuse](https://vueuse.org/)                                        | 常用工具集                 | 9.10.0        |
| [vxe-table](https://vxetable.cn/)                                    | vue 最强表单              | 4.3.7         |
| [vue-i18n](https://kazupon.github.io/vue-i18n/zh/introduction.html/) | 国际化                   | 9.2.2         |
| [vue-router](https://router.vuejs.org/)                              | vue 路由                | 4.1.6         |
| [windicss](https://cn.windicss.org/)                                 | 下一代工具优先的 CSS 框架       | 3.5.6         |
| [iconify](https://icon-sets.iconify.design/)                         | 在线图标库                 | 3.0.1         |
| [wangeditor](https://www.wangeditor.com/)                            | 富文本编辑器                | 5.1.23        |

## 🎳演示图例

<table>
  <tr>
   <th><p>登录页面</p></th>
   <th><p>后台首页</p></th>
  </tr>
  <tr>
   <th><img src="https://www.mmsadmin.cn/images/mms/01.png" alt=""/></th>
   <th><img src="https://www.mmsadmin.cn/images/mms/02.png" alt=""/></th>
  </tr>
<tr>
   <th><p>用户管理</p></th>
   <th><p>新增用户</p></th>
  </tr>
  <tr>
   <th><img src="https://www.mmsadmin.cn/images/mms/03.png" alt=""/></th>
   <th><img src="https://www.mmsadmin.cn/images/mms/04.png" alt=""/></th>
  </tr>
<tr>
   <th><p>角色管理</p></th>
   <th><p>添加角色</p></th>
  </tr>
  <tr>
   <th><img src="https://www.mmsadmin.cn/images/mms/05.png" alt=""/></th>
   <th><img src="https://www.mmsadmin.cn/images/mms/06.png" alt=""/></th>
  </tr>
<tr>
   <th><p>菜单管理</p></th>
   <th><p>部门管理</p></th>
  </tr>
  <tr>
   <th><img src="https://www.mmsadmin.cn/images/mms/07.png" alt=""/></th>
   <th><img src="https://www.mmsadmin.cn/images/mms/08.png" alt=""/></th>
  </tr>
<tr>
   <th><p>字典管理</p></th>
   <th><p>添加字典</p></th>
  </tr>
  <tr>
   <th><img src="https://www.mmsadmin.cn/images/mms/09.png" alt=""/></th>
   <th><img src="https://www.mmsadmin.cn/images/mms/10.png" alt=""/></th>
  </tr>
<tr>
   <th><p>系统设置</p></th>
   <th><p>消息公告</p></th>
  </tr>
  <tr>
   <th><img src="https://www.mmsadmin.cn/images/mms/11.png" alt=""/></th>
   <th><img src="https://www.mmsadmin.cn/images/mms/12.png" alt=""/></th>
  </tr>
<tr>
   <th><p>代码生成1</p></th>
   <th><p>代码生成2</p></th>
  </tr>
  <tr>
   <th><img src="https://www.mmsadmin.cn/images/mms/13.png" alt=""/></th>
   <th><img src="https://www.mmsadmin.cn/images/mms/14.png" alt=""/></th>
  </tr>
<tr>
   <th><p>代码生成3</p></th>
   <th><p>代码生成4</p></th>
  </tr>
  <tr>
   <th><img src="https://www.mmsadmin.cn/images/mms/15.png" alt=""/></th>
   <th><img src="https://www.mmsadmin.cn/images/mms/16.png" alt=""/></th>
  </tr>
<tr>
   <th><p>定时任务</p></th>
   <th><p>对象存储</p></th>
  </tr>
  <tr>
   <th><img src="https://www.mmsadmin.cn/images/mms/17.png" alt=""/></th>
   <th><img src="https://www.mmsadmin.cn/images/mms/18.png" alt=""/></th>
  </tr>
<tr>
   <th><p>扩展工具</p></th>
   <th><p>个人中心</p></th>
  </tr>
  <tr>
   <th><img src="https://www.mmsadmin.cn/images/mms/19.png" alt=""/></th>
   <th><img src="https://www.mmsadmin.cn/images/mms/20.png" alt=""/></th>
  </tr>
</table>

# 😎 LICENSE

::: tip MIT License
[https://gitee.com/mmsAdmin/mms/blob/master/LICENSE](https://gitee.com/mmsAdmin/mms/blob/master/LICENSE)
:::

## ❌免责条款

&emsp;&emsp;您充分了解并同意，您必须为自己使用本服务及注册帐号下的一切行为负责，包括您所发表的任何内容以及由此产生的任何后果。您应对本服务中的内容自行加以判断，并自行承担因使用内容而引起的所有风险。

&emsp;&emsp;[mms](https://gitee.com/mmsAdmin/mms),[mms-ui](https://gitee.com/mmsAdmin/mms-ui) 对网站上所显示的信息或资料的准确性、内容、完整性、合法性、可靠性、可操作性或可用性不承担任何责任。

&emsp;&emsp;[mms](https://gitee.com/mmsAdmin/mms),[mms-ui](https://gitee.com/mmsAdmin/mms-ui) 二次开发使用者因为违法而触犯中华人民共和国法律的，一切后果自己负责，`mms 作者` 不承担任何责任。

&emsp;&emsp;本声明未涉及的问题参见国家有关法律法规，当本声明与国家法律法规冲突时，以国家法律法规为准。

## 🧪学习 & 商用

&emsp;&emsp;[mms](https://gitee.com/mmsAdmin/mms),[mms-ui](https://gitee.com/mmsAdmin/mms-ui) 是免费和开源的，可免费用于 `学习`、`商业使用` 。
