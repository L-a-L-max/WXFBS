# 优立方 AI 主机开源版

优立方 AI 主机是一个基于 Spring Boot 和 Playwright 的开源项目，旨在提供一个可扩展的 AI 内容生成平台。它支持与多个 AI 平台（如腾讯元宝、豆包等）进行交互，并提供登录状态管理、内容生成、智能评分等功能。

## 项目结构

- **cube-admin**: 提供后台管理功能，包括用户管理、角色管理、菜单管理、系统配置等。
- **cube-engine**: 使用 Playwright 实现 AI 内容生成引擎，支持与多个 AI 平台交互。
- **cube-framework**: 提供通用框架功能，如权限控制、日志记录、数据源管理等。
- **cube-common**: 包含通用工具类、常量定义、异常处理等基础功能。

## 核心功能

### AI 交互功能

- **AI 内容生成**：支持与腾讯元宝、豆包等 AI 平台交互，生成所需内容。
- **智能评分**：调用豆包平台对内容进行评分并返回评分结果。
- **登录状态管理**：支持检查腾讯元宝、豆包等平台的登录状态，并获取登录二维码。

### 后台管理功能

- **用户管理**：支持用户注册、登录、权限分配等。
- **角色管理**：支持角色创建、权限分配、角色删除等。
- **菜单管理**：支持菜单项的添加、编辑、删除等操作。
- **系统配置**：支持系统参数的配置和管理。

### 日志与监控

- **操作日志**：记录用户操作行为，便于审计和追踪。
- **登录日志**：记录用户登录信息，支持导出和清理。
- **在线用户管理**：支持查看当前在线用户，并强制下线。

## 技术栈

- **后端框架**：Spring Boot + MyBatis
- **前端交互**：Playwright（用于浏览器自动化）
- **数据库**：MySQL + Redis
- **安全框架**：Spring Security + JWT
- **日志管理**：Logback + Slf4j
- **缓存管理**：Redis
- **任务调度**：Quartz

## 安装与部署

请参考以下部署指南：

- [cube-admin 部署指南](cube-admin/deployment_guide.md)
- [cube-engine 部署指南](cube-engine/deployment_guide.md)

## 使用说明

### AI 内容生成

#### 启动腾讯元宝内容生成

```java
@PostMapping("/startYB")
public String startYB(@RequestBody UserInfoRequest userInfoRequest)
```

#### 启动豆包 AI 生成

```java
@PostMapping("/startDB")
public String startDB(@RequestBody UserInfoRequest userInfoRequest)
```

#### 启动豆包智能评分

```java
@PostMapping("/startDBScore")
public String startDBScore(@RequestBody UserInfoRequest userInfoRequest)
```

### 登录状态管理

#### 检查腾讯元宝登录状态

```java
@GetMapping("/checkLogin")
public String checkLogin(@RequestParam("userId") String userId)
```

#### 获取腾讯元宝登录二维码

```java
@GetMapping("/getYBQrCode")
public String getYBQrCode(@RequestParam("userId") String userId)
```

#### 退出腾讯元宝登录

```java
@GetMapping("/loginOut")
public boolean loginOut(@RequestParam("userId") String userId)
```

### 后台管理接口

#### 用户管理

- 获取用户列表：`GET /system/user/list`
- 添加用户：`POST /system/user`
- 编辑用户：`PUT /system/user`
- 删除用户：`DELETE /system/user/{userIds}`

#### 角色管理

- 获取角色列表：`GET /system/role/list`
- 添加角色：`POST /system/role`
- 编辑角色：`PUT /system/role`
- 删除角色：`DELETE /system/role/{roleIds}`

#### 菜单管理

- 获取菜单列表：`GET /system/menu/list`
- 添加菜单：`POST /system/menu`
- 编辑菜单：`PUT /system/menu`
- 删除菜单：`DELETE /system/menu/{menuId}`

## 贡献指南

欢迎贡献代码和文档！请遵循以下步骤：

1. Fork 本项目。
2. 创建新分支 (`git checkout -b feature/new-feature`)
3. 提交更改 (`git commit -am 'Add new feature'`)
4. 推送分支 (`git push origin feature/new-feature`)
5. 创建 Pull Request

## 许可证

本项目采用 [MIT License](LICENSE)，请自由使用和修改。

## 联系我们

如有任何问题或建议，请提交 Issue 或联系项目维护者。