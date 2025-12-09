<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">微信福帮手</h1>
<h4 align="center">微信生态内容智能化工具</h4>
<p align="center">
	<a href="https://gitee.com/U3W-AI/RuoYi-Vue"><img src="https://img.shields.io/badge/WxFbsir-v1.1.2-brightgreen.svg"></a>
	<a href="https://wx.fbsir.com"><img src="https://img.shields.io/badge/website-wx.fbsir.com-blue.svg"></a>
</p>



## 项目简介

微信福帮手(WxFbsir)基于腾讯元器智能体开发平台及RuoYi等开源框架进行整合开发，助力中小团队快速高效实现微信生态的内容资产智能化。



### 特色功能：

作为本项目的基本模块，日更助手是一个基于AI大模型的公众号文章生成系统，通过集成腾讯元器智能体平台，帮助用户快速生成高质量的文章内容。

✅ 多模型协同生成
同时调用3个大模型（混元 2.0、DeepSeek 3.2、优图精调模型）生成不同风格的初稿，用户可自主选择参与的模型

✅ 智能内容整合
使用混元优化大模型整合多个版本，生成最优质的文章内容

✅ 异步处理架构
后台异步调用AI生成，前端轮询获取状态，用户无需等待

✅ 智能排版
支持调用腾讯元器排版工作流，自动优化文章格式和结构

✅ 公众号投递
一键投递优化后的文章到微信公众号草稿箱

✅ 历史记录管理
保存所有生成的文章，支持查看、编辑和删除

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
git clone https://gitee.com/U3W-AI/WFBS.git
cd WFBS

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

## 📚 文档中心

- **[部署文档](./部署文档.md)** - 完整的部署指南（包含元器工作流配置）
- **[项目结构说明](./项目结构说明.md)** - 项目目录结构和模块说明
- **[文档中心](./docs/README.md)** - 更多技术文档和开发规范

### 开发规范
- **[代码规范](./docs/代码规范.md)** - Java、Vue、数据库代码规范
- **[代码合并PR规范](./docs/代码合并PR规范.md)** - 代码提交和PR流程规范
- **[文档规范总结](./docs/文档规范总结.md)** - 文档编写规范

### 帮助文档
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

- **源码**: [https://gitee.com/U3W-AI/WFBS](https://gitee.com/U3W-AI/WFBS)

## 许可证

本项目采用 MIT 许可证，详情请查看 [LICENSE](LICENSE) 文件。

---

**感谢若依团队提供的优秀开源框架！**

本项目的后台管理系统基于 **若依(RuoYi)** 框架进行二次开发，感谢若依团队提供的优秀开源框架。
- 若依官网：[http://ruoyi.vip](http://ruoyi.vip)
- 若依源码：[https://gitee.com/y_project/RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue)

### 管理系统功能

#### 核心功能
- **用户管理**: 系统用户信息维护和权限配置
- **角色管理**: 三级权限体系（超级管理员、管理员、只读权限）
- **部门管理**: 组织架构管理，支持数据权限控制
- **菜单管理**: 动态菜单配置和权限控制

#### 系统管理
- **字典管理**: 系统数据字典维护
- **参数配置**: 系统参数动态配置
- **通知公告**: 系统公告信息管理
- **定时任务**: 在线任务调度管理

#### 监控日志
- **操作日志**: 系统操作记录和异常监控
- **登录日志**: 用户登录行为记录
- **在线用户**: 实时用户状态监控
- **系统监控**: CPU、内存、磁盘等系统资源监控

#### 开发工具
- **代码生成**: 一键生成CRUD代码（Java + Vue）
- **系统接口**: Swagger API文档自动生成
- **表单构建**: 在线表单设计器

#####  文档更新日期：2025年12月9日 17：00  文档版本：1.1.2
