<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">福帮手</h1>
<h4 align="center">助力中小团队深耕AI机遇</h4>
<p align="center">
	<a href="https://gitee.com/U3W-AI/RuoYi-Vue"><img src="https://img.shields.io/badge/WxFbsir-v1.1.8-brightgreen.svg"></a>
	<a href="https://www.fbsir.com"><img src="https://img.shields.io/badge/website-www.fbsir.com-blue.svg"></a>
</p>



## 项目简介


项目定位：以AI赋能团队建设，推动智能原生企业更快涌现。福帮手FBSir，福润百业，智生万象。

## 系统特色


**完善的运营支撑体系**

内置积分系统：支持积分规则配置、积分实现和用户积分管理

三级权限管理体系：超级管理员、管理员、只读权限分级控制

完整的后台管理系统：基于若依(RuoYi)框架二次开发


🏗️ **技术架构特点**

项目采用异步处理架构，实现后台异步调用AI生成，前端轮询获取状态，确保用户体验的流畅性。

同时集成Swagger API文档自动生成，提升开发效率。

PlayWright应用及WebSocket集成框架，将RPA与AI一炉同炼，为人机协同和智能资产的挖掘提供更多创意空间。


### ✨ **特色模块之日更助手**


同时调用多个模型生成不同风格初稿，优化后文章到微信公众号草稿箱。保存所有生成文章，支持查看、编辑和删除。


### ✨ **特色模块之Gitee用户能力分析**

支持Gitee用户在首页授权登录，并通过Gitee接口，拉取在开源社区的参与情况数据，进行用户画像和能力分析。


### ✨ **特色模块之潜力眼（内测）**

基于元器工作流智能体，实现简历分析功能，支持简历上传和解析，支持企业微信侧以工作台应用模式进行双向对话。


## 快速开始

### 环境要求
- JDK 17+
- MySQL 8.0+
- Redis 6.0+（可选）
- Node.js 18+

### 技术选型与版本要求

| 层级 | 技术选型 | 版本要求 | 用途 |
|------|----------|----------|------|
| 语言 | Java | 17+ | 后端开发 |
| 构建 | Maven | 3.8+ | 后端构建 |
| 数据 | MySQL | 8.0+ | 主数据库 |
| 缓存 | Redis | 6.0+ | 缓存 / 会话（可选） |
| 运行环境 | Node.js | 18+ | 前端构建/运行 |
| 包管理 | npm / pnpm | 9+ / 8+ | 前端依赖管理 |
| 后端框架 | Spring Boot | 3.5.4（主服务），3.3.6（engine） | 应用框架 |
| ORM | MyBatis Spring Boot Starter | 3.0.4 | ORM 框架 |
| 分页 | PageHelper | 2.1.1 | 分页插件 |
| 数据源 | Druid | 1.2.23 | 连接池 |
| 安全 | Spring Security | 随 Spring Boot BOM | 安全框架 |
| 认证 | JJWT | 0.9.1 | Token 认证 |
| 文档 | SpringDoc OpenAPI | 2.8.9 | API 文档 |
| 验证码 | Kaptcha | 2.3.3 | 图形验证码 |
| JSON | Fastjson2 | 2.0.58 | JSON 处理 |
| WebSocket | Spring Boot WebSocket | 随 Spring Boot BOM | 实时通信 |
| 自动化 | Playwright | 1.49.0 | 引擎自动化 |
| WebSocket | Java-WebSocket | 1.5.7 | WebSocket 客户端 |
| 前端框架 | Vue.js | 3.5.16 | 前端框架 |
| 路由 | Vue Router | 4.5.1 | 前端路由 |
| 状态管理 | Pinia | 3.0.2 | 状态管理 |
| UI | Element Plus | 2.10.7 | 组件库 |
| 构建工具 | Vite | 6.3.5 | 前端构建 |
| 图表 | ECharts | 5.6.0 | 可视化图表 |
| 富文本 | @vueup/vue-quill | 1.2.0 | 富文本编辑 |
| HTTP | Axios | 1.9.0 | HTTP 请求 |

### 快速部署

详细部署步骤请查看 [部署文档](./部署文档.md)

```bash
# 1. 克隆项目
git clone https://gitee.com/U3W-AI/U3W-AI.git
cd U3W-AI

# 2. 初始化数据库
mysql -u root -p wxfbsir < sql/wxfbsir.sql

# 3. 启动后端
cd WxFbsir-admin
mvn clean install
mvn spring-boot:run

# 4. 启动前端
cd WxFbsir-ui
npm install
npm run dev
```

### 默认账户
- 用户名：`admin`
- 密码：`admin123`

## 文档中心

- **[部署文档](./部署文档.md)** - 完整的部署指南（包含元器工作流配置）
- **[项目结构说明](./项目结构说明.md)** - 项目目录结构和模块说明
- **[README.md](docs/README.md)** - 文档中心
- **[常见问题 (FAQ)](./docs/FAQ.md)** - 常见问题解答

## 贡献指南

欢迎贡献代码和文档！请遵循以下步骤：

1. Fork 本仓库到你的账号
2. 创建功能分支：`git checkout -b feature/your-feature`
3. 提交代码：`git commit -m "feat: 添加新功能 (2025-12-05)"`
4. 推送到分支：`git push origin feature/your-feature`
5. 提交 Pull Request

详细规范请查看 [代码合并PR规范](./docs/代码合并PR规范.md)

## 技术支持

- **源码**: [https://gitee.com/U3W-AI/U3W-AI](https://gitee.com/U3W-AI/U3W-AI)

---

本项目后台管理系统基于 **若依(RuoYi)** 框架进行二次开发，感谢若依团队提供的优秀开源框架。


文档更新日期：2026年1月7日 15：00  文档版本：1.1.9D
