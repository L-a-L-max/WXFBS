<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">福帮手</h1>
<h4 align="center">助力中小团队深耕AI机遇</h4>
<p align="center">
	<a href="https://gitee.com/U3W-AI/RuoYi-Vue"><img src="https://img.shields.io/badge/WxFbsir-v1.1.9-brightgreen.svg"></a>
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

Playwright应用及Websocket集成框架，将RPA与AI一炉同炼，为人机协同和智能资产的挖掘提供更多创意空间。


### ✨ **特色模块之日更助手**


同时调用多个模型生成不同风格初稿，优化后文章到微信公众号草稿箱。保存所有生成文章，支持查看、编辑和删除。


### ✨ **特色模块之Gitee用户能力分析**

支持Gitee用户在首页授权登录，并通过Gitee接口，拉取在开源社区的参与情况数据，进行用户画像和能力分析。


### ✨ **特色功能之腾讯元器自动化工具链**

福帮手主机引擎支持Playwright能力管理，并提供Playwright实现示例，如登录状态检查、工作流导航等元器控制器。


## 快速开始

### 环境要求
- JDK 17+
- MySQL 8.0+
- Redis 6.0+（可选）
- Node.js 18+

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


文档更新日期：2026年1月日 7:55  文档版本：1.1.9B
