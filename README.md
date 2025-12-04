<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">FBSIR 企业级管理系统 v1.0.0</h1>
<h4 align="center">基于若依框架二次开发的企业级后台管理系统</h4>
<p align="center">
	<a href="https://gitee.com/U3W-AI/RuoYi-Vue"><img src="https://img.shields.io/badge/FBSIR-v1.0.0-brightgreen.svg"></a>
	<a href="https://fbsir.com"><img src="https://img.shields.io/badge/website-fbsir.com-blue.svg"></a>
</p>

## 项目简介

FBSIR是基于若依框架进行二次开发的企业级后台管理系统，针对企业实际需求进行了全面优化和定制。

### 技术栈
* **前端**: Vue 3.5.16 + Element Plus 2.10.7 + Vite 6.3.5
* **后端**: Spring Boot 3.5.4 + Spring Security + MyBatis 3.0.4 + Redis
* **数据库**: MySQL 8.2.0
* **权限控制**: JWT + RBAC

### 项目特色
* 基于成熟的若依框架，稳定可靠
* 企业级权限管理体系（超级管理员、管理员、只读权限）
* 简洁专业的用户界面，移除冗余功能
* 完整的代码生成器，支持一键生成CRUD
* 专业的技术支持和维护服务

### 原始框架
本项目基于 **若依(RuoYi)** 框架进行二次开发，感谢若依团队提供的优秀开源框架。
- 若依官网：[http://ruoyi.vip](http://ruoyi.vip)
- 若依源码：[https://gitee.com/y_project/RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue)

## 系统功能

### 核心功能
- **用户管理**: 系统用户信息维护和权限配置
- **角色管理**: 三级权限体系（超级管理员、管理员、只读权限）
- **部门管理**: 组织架构管理，支持数据权限控制
- **菜单管理**: 动态菜单配置和权限控制

### 系统管理
- **字典管理**: 系统数据字典维护
- **参数配置**: 系统参数动态配置
- **通知公告**: 系统公告信息管理
- **定时任务**: 在线任务调度管理

### 监控日志
- **操作日志**: 系统操作记录和异常监控
- **登录日志**: 用户登录行为记录
- **在线用户**: 实时用户状态监控
- **系统监控**: CPU、内存、磁盘等系统资源监控

### 开发工具
- **代码生成**: 一键生成CRUD代码（Java + Vue）
- **系统接口**: Swagger API文档自动生成
- **表单构建**: 在线表单设计器

## 快速开始

### 环境要求
- JDK 17
- MySQL 8.0
- Redis 6.0
- Node.js 18

### 安装部署

1. **克隆项目**
```bash
git clone https://gitee.com/U3W-AI/RuoYi-Vue.git
cd RuoYi-Vue
```

2. **数据库初始化**
```sql
-- 直接导入SQL文件（已包含数据库创建语句）
source sql/fbsir.sql
source sql/quartz.sql
```

3. **后端启动**
```bash
cd fbsir-admin
mvn clean install
mvn spring-boot:run
```

4. **前端启动**
```bash
cd fbsir-ui
npm install
npm run dev
```

### 默认账户
- 用户名：`admin`
- 密码：`admin123`

## 技术支持

- **官网**: [https://fbsir.com](https://fbsir.com)
- **源码**: [https://gitee.com/U3W-AI/RuoYi-Vue](https://gitee.com/U3W-AI/RuoYi-Vue)

## 许可证

本项目采用 MIT 许可证，详情请查看 [LICENSE](LICENSE) 文件。

---

**感谢若依团队提供的优秀开源框架！**
