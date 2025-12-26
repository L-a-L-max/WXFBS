# WxChat-demo 完整部署指南

## 项目简介


这是一个**独立部署的前后端分离测试应用**，专门用于演示**企业微信与腾讯元器AI的交互**。
完整部署后，可以打通腾讯元器智能体与企业微信应用的双向联系，便于将元器智能体的能力，在企业微信聊天工具栏等场景下使用。

**核心功能**：
- 🔐 **企业微信 OAuth 登录**：用户通过企业微信授权登录
- 🤖 **元器 AI 对话**：通过Nginx反向代理转发请求，实现与元器AI的实时对话
- 🔒 **安全可靠**：数据加密传输，支持多轮对话，自动保存历史

**技术架构**：
- 前端：Vue 3 + Element Plus
- 后端：Spring Boot + MyBatis
- 数据库：MySQL 5.7+
- 部署：Nginx 反向代理

---

## 目录

1. [部署前准备](#部署前准备)
2. [企业微信配置](#企业微信配置)
3. [服务器环境配置](#服务器环境配置)
4. [Nginx配置（第一阶段）](#nginx配置第一阶段)
5. [元器配置](#元器配置)
6. [Nginx配置（第二阶段）](#nginx配置第二阶段)
7. [后端配置与部署](#后端配置与部署)
8. [前端构建与部署](#前端构建与部署)
9. [验证部署](#验证部署)
10. [常见问题](#常见问题)

---

## 部署前准备

### 必需资源

| 资源 | 要求 | 说明 |
|------|------|------|
| **域名** | 必须与企业微信主体一致 | 需向企业微信管理员申请，用于OAuth回调 |
| **服务器** | 1核2G以上 | 需要公网IP，建议使用宝塔面板 |
| **SSL证书** | 必须 | 企业微信要求HTTPS，可使用Let's Encrypt免费证书 |
| **企业微信账号** | 管理员权限 | 用于创建应用和配置 |
| **元器账号** | 已注册 | 用于创建智能体 |

### 环境要求

| 环境 | 版本 |
|------|------|
| JDK | 17+ |
| Maven | 3.6+ |
| Node.js | 18+ |
| MySQL | 5.7+ / 8.0+ |
| Nginx | 任意版本 |

---

## 企业微信配置

### 步骤1：获取企业ID

1. 登录 [企业微信管理后台](https://work.weixin.qq.com/wework_admin/frame#/)
2. 点击左侧菜单 **我的企业**
3. 在页面底部找到 **企业ID (corpId)**，记录下来

### 步骤2：创建自建应用

1. 点击左侧菜单 **应用管理**
2. 点击 **自建** → **创建应用**
3. 填写应用信息：
   - 应用名称：如 "AI对话助手"
   - 应用Logo：上传图标
   - 可见范围：选择需要使用的部门或成员
4. 创建成功后，记录以下信息：
   - **AgentId**（应用ID）
   - **Secret**（应用密钥，点击"查看"获取）

---

## 服务器环境配置

### 步骤1：域名解析

1. 登录域名管理后台
2. 添加A记录：
   - 主机记录：`abc` 或 `123`
   - 记录类型：`A`
   - 记录值：服务器公网IP
   - TTL：默认

### 步骤2：安装宝塔面板（推荐）

[宝塔](https://www.bt.cn/u/PXnXfi)官网

### 步骤3：安装必需软件

在宝塔面板中安装：
- Nginx（任意版本）
- MySQL 5.7 或 8.0
- Java项目管理器（用于运行Spring Boot）

---

## Nginx配置（第一阶段）

**目的**：配置企业微信代理和元器回调代理，用于创建元器发布渠道

### 步骤1：创建网站

1. 宝塔面板 → **网站** → **添加站点**
2. 填写配置：
   - 域名：你的域名（如 `demo.example.com`）
   - 根目录：`/www/wwwroot/demo.example.com`
   - PHP版本：纯静态

### 步骤2：配置SSL证书

1. 点击网站 → **SSL**
2. 选择 **Let's Encrypt** 免费证书
3. 勾选域名 → **申请**
4. 开启 **强制HTTPS**

**⚠️ 必须配置SSL证书，否则企业微信无法回调！**

### 步骤3：配置Nginx反向代理

点击网站 → **配置文件**，在 `server` 块中添加以下配置：

```nginx
# 前端路由支持（Vue Router history模式）
location / {
    try_files $uri $uri/ /index.html;
}

# 企业微信API代理
location ~ ^/cgi-bin/ {
    proxy_pass              https://qyapi.weixin.qq.com;
    proxy_http_version      1.1;
    proxy_set_header        Connection "close";
    proxy_buffering         off;
    proxy_read_timeout      600s;
    proxy_send_timeout      600s;
}

# 元器回调代理（用于创建发布渠道）
location ~ ^/api/v2/channel/callback/wecom/message/ {
    proxy_pass              https://yuanqi.tencent.com;
    proxy_http_version      1.1;
    proxy_set_header        Connection "close";
    proxy_buffering         off;
    proxy_read_timeout      600s;
    proxy_send_timeout      600s;
}
```

保存并重启Nginx。

---

## 元器配置

### 步骤1：创建智能体

1. 登录 [腾讯元器](https://yuanqi.tencent.com/)
2. 点击 **创建智能体** → 选择 **对话式智能体**
3. 填写基本信息并创建

### 步骤2：导入工作流

1. 点击顶部 **工作流管理**
2. 点击 **导入工作流**
3. 上传 `export-处理企业微信对话.zip` 文件
4. 导入成功后，点击 **编辑**

### 步骤3：修改工作流节点

找到 **"向对话信息存储到数据库"** HTTP请求节点：

1. 修改请求地址：
   ```
   https://你的域名/api/chat/workflow/callback
   ```
2. 保存节点
3. 点击 **调试** 测试工作流
4. 测试成功后，点击 **返回** 并 **启用工作流**

### 步骤4：切换为单工作流模式

1. 返回智能体页面
2. 点击左上角 **标准模式** → 切换为 **单工作流模式**
3. 在 **应用设置** 中添加刚刚创建的工作流

### 步骤5：创建企业微信发布渠道

1. 进入 **应用发布** 页面
2. 点击 **发布渠道** → **创建发布渠道**
3. 选择 **企业微信应用**
4. 填写配置：
   - **企业ID (CorpId)**：之前记录的企业ID
   - **应用ID (AgentId)**：之前记录的应用AgentId
   - **应用密钥 (Secret)**：之前记录的Secret
   - **服务器地址 (URL)**：将 `{您的域名}` 替换为你的域名
     ```
     https://你的域名/api/v2/channel/callback/wecom/message/
     ```

5. 点击 **创建**

**⚠️ 常见错误**：
- **错误码 4506007**：域名的Nginx配置有问题，或SSL证书未配置
- **验证失败**：检查企业ID、应用ID、Secret是否正确

### 步骤6：获取元器配置信息

创建成功后，点击 **查看详情**，记录以下信息：

- **服务器地址 (URL)**：如 `https://你的域名/api/v2/channel/callback/wecom/message/2002988235509072256`
- **Token**：消息验证Token
- **EncodingAESKey**：AES加密密钥（43位）

**⚠️ 重点**：记录URL中 `/message/` 后面的数字（如 `2002988235509072256`），这是channelId

---

## Nginx配置（第二阶段）

**目的**：将元器回调代理改为后端API代理

### 步骤1：修改Nginx配置

点击网站 → **配置文件**，找到之前添加的元器代理配置，**删除**：

```nginx
# 删除这段配置
location ~ ^/api/v2/channel/callback/wecom/message/ {
    proxy_pass              https://yuanqi.tencent.com;
    ...
}
```

**替换为**后端API代理：

```nginx
# 后端API代理
location /api/ {
    proxy_pass              http://127.0.0.1:8083/api/;
    proxy_http_version      1.1;
    proxy_set_header        Host $host;
    proxy_set_header        X-Real-IP $remote_addr;
    proxy_set_header        X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header        X-Forwarded-Proto $scheme;
    proxy_set_header        REMOTE-HOST $remote_addr;
    
    # 通用超时配置
    proxy_connect_timeout   30s;
    proxy_read_timeout      86400s;
    proxy_send_timeout      30s;
    
    # 缓冲配置
    proxy_buffering         off;
}
```

**⚠️ 注意**：端口 `8083` 必须与后端配置的端口一致！

### 步骤2：配置企业微信验证文件

添加以下配置（用于企业微信网页授权验证）：

```nginx
location ~*\.txt$ {
    root /www/wwwroot/你的域名/dist/;
}
```

保存并重启Nginx。

---

## 后端配置与部署

### 步骤1：配置数据库

1. 宝塔面板 → **数据库** → **添加数据库**
2. 数据库名：`demo`
3. 用户名：自定义
4. 密码：自定义
5. 执行初始化脚本：

```bash
mysql -u root -p demo < sql/init.sql
```

### 步骤2：修改配置文件

编辑 `backend/src/main/resources/application.yml`：

```yaml
# 服务器配置
server:
  port: 8083  # 必须与Nginx配置的端口一致
  servlet:
    context-path: /api

# 数据库配置
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/demo?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8
    username: 你的数据库用户名
    password: 你的数据库密码

# 企业微信配置
wework:
  corp-id: 你的企业ID
  agent-id: 你的应用AgentId
  agent-secret: 你的应用Secret

# 元器AI配置
yuanqi:
  token: 元器提供的Token
  encoding-aes-key: 元器提供的EncodingAESKey（43位）
  target-url: https://yuanqi.tencent.com/api/v2/channel/callback/wecom/message/你的channelId
  corp-id: 你的企业ID
  agent-id: 你的应用AgentId
```

**⚠️ 重要配置说明**：

1. **target-url**：
   - 前面部分 `https://yuanqi.tencent.com/api/v2/channel/callback/wecom/message/` 不可更改
   - 只需将 `/message/` 后面的数字替换为你的channelId
   - 示例：`https://yuanqi.tencent.com/api/v2/channel/callback/wecom/message/2002988235509072256`


### 步骤3：本地打包

在本地开发环境执行：

```bash
cd backend
mvn clean package -DskipTests
```

打包完成后，在 `backend/target/` 目录下生成 `wxchat-demo-1.0.0.jar`

### 步骤4：上传到服务器

1. 宝塔面板 → **文件**
2. 创建目录：`/www/wwwroot/wxchat-demo/`
3. 上传 `wxchat-demo-1.0.0.jar`

### 步骤5：启动后端

1. 宝塔面板 → **Java项目管理器**
2. 添加项目：
   - 项目路径：`/www/wwwroot/wxchat-demo/`
   - JAR包：`wxchat-demo-1.0.0.jar`
   - 启动端口：`8083`
3. 点击 **启动**

### 步骤6：验证后端

```bash
curl http://127.0.0.1:8083/api/chat/poll?sessionId=test
```

返回JSON数据说明后端启动成功。

---

## 前端构建与部署

### 步骤1：本地构建

在本地开发环境执行：

```bash
cd frontend
npm install
npm run build
```

构建完成后，`dist` 目录下生成静态文件。

### 步骤2：上传到服务器

1. 宝塔面板 → **文件**
2. 进入网站目录：`/www/wwwroot/你的域名/`
3. 将 `dist` 目录下的 **所有文件** 上传到该目录

---

## 企业微信应用配置

### 步骤1：配置网页授权及JS-SDK

1. 进入企业微信应用详情页
2. 找到 **网页授权及JS-SDK** 区域
3. 点击 **设置可信域名**
4. 填入你的域名（不带协议和端口），如：`demo.example.com`
5. 下载验证文件（如 `WW_verify_xxx.txt`）
6. 将验证文件上传到 `/www/wwwroot/你的域名/dist/` 目录
7. 点击 **验证**

### 步骤2：配置OAuth回调域

1. 在应用详情页，找到 **企业微信授权登录** 区域
2. 点击 **设置授权回调域**
3. 填入你的域名（不带协议和端口）

### 步骤3：配置可信IP

1. 在应用详情页，找到 **企业可信IP** 区域
2. 点击 **配置**
3. 填入服务器公网IP

### 步骤4：配置应用首页

1. 在应用详情页，找到 **应用主页** 区域
2. 填入：`https://你的域名`
3. 保存

---

## 验证部署

### 1. 访问应用

在企业微信中打开应用，或直接访问 `https://你的域名`

### 2. 测试企业微信登录

1. 点击 **通过企业微信登录**
2. 扫码授权
3. 自动跳转到对话页面
4. 发送消息测试

### 3. 检查数据库

```sql
SELECT * FROM ai_chat_message ORDER BY create_time DESC LIMIT 10;
```

应该能看到用户消息和AI回复记录。

---

## 常见问题

### Q1: 元器发布渠道创建失败，错误码4506007？

**原因**：
- Nginx配置错误
- SSL证书未配置或已过期
- 域名解析未生效

**解决**：
1. 检查Nginx配置中的元器代理是否正确
2. 确认SSL证书已配置并开启强制HTTPS
3. 测试域名是否可以访问：`curl https://你的域名`

### Q2: 发送消息后一直没有回复？

**检查清单**：
1. 后端是否正常运行：`curl http://127.0.0.1:8083/api/chat/poll?sessionId=test`
2. 元器工作流是否已启用
3. 工作流HTTP节点的域名是否正确,是否正确添加HTTPS
4. 查看后端日志是否有 `[工作流回调] 收到AI回复`
5. 检查数据库 `ai_chat_message` 表中是否有AI回复（`role='assistant'`）

### Q3: Nginx配置后无法访问？

**检查**：
1. Nginx配置语法是否正确：`nginx -t`
2. 是否重启Nginx：`nginx -s reload`
3. 端口是否被占用：`netstat -tlnp | grep 8083`
4. 防火墙是否开放端口

---

## 完整配置示例

### application.yml

```yaml
server:
  port: 8083
  servlet:
    context-path: /api

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/demo?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8
    username: root
    password: MyPassword123

wework:
  corp-id: 
  agent-id: 
  agent-secret: 

yuanqi:
  token: 
  encoding-aes-key: 
  target-url: https://yuanqi.tencent.com/api/v2/channel/callback/wecom/message/20029882355
  corp-id: 
  agent-id: 
```

### Nginx完整配置

```nginx
server {
    listen 80;
    listen 443 ssl http2;
    server_name demo.example.com;
    
    # SSL证书配置
    ssl_certificate /www/server/panel/vhost/cert/demo.example.com/fullchain.pem;
    ssl_certificate_key /www/server/panel/vhost/cert/demo.example.com/privkey.pem;
    
    # 强制HTTPS
    if ($server_port !~ 443){
        rewrite ^(/.*)$ https://$host$1 permanent;
    }
    
    # 网站根目录
    root /www/wwwroot/demo.example.com;
    index index.html;
    
    # 前端路由支持
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    # 企业微信验证文件
    location ~*\.txt$ {
        root /www/wwwroot/demo.example.com/dist/;
    }
    
    # 后端API代理
    location /api/ {
        proxy_pass              http://127.0.0.1:8083/api/;
        proxy_http_version      1.1;
        proxy_set_header        Host $host;
        proxy_set_header        X-Real-IP $remote_addr;
        proxy_set_header        X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header        X-Forwarded-Proto $scheme;
        proxy_set_header        REMOTE-HOST $remote_addr;
        proxy_connect_timeout   30s;
        proxy_read_timeout      86400s;
        proxy_send_timeout      30s;
        proxy_buffering         off;
    }
    
    # 企业微信API代理
    location ~ ^/cgi-bin/ {
        proxy_pass              https://qyapi.weixin.qq.com;
        proxy_http_version      1.1;
        proxy_set_header        Connection "close";
        proxy_buffering         off;
        proxy_read_timeout      600s;
        proxy_send_timeout      600s;
    }
}
```

---

## 部署流程总结

```
1. 准备域名和服务器
   ↓
2. 企业微信创建应用，获取 corpId、agentId、secret
   ↓
3. 服务器配置域名解析、SSL证书
   ↓
4. Nginx第一阶段配置（企微代理 + 元器代理）
   ↓
5. 元器创建智能体、导入工作流、创建企微发布渠道
   ↓
6. 获取元器 token、encodingAesKey、channelId
   ↓
7. Nginx第二阶段配置（改为后端API代理）
   ↓
8. 配置后端 application.yml
   ↓
9. 打包并部署后端（Spring Boot）
   ↓
10. 构建并部署前端（Vue）
   ↓
11. 企业微信配置可信域名、OAuth回调域、可信IP、应用首页
   ↓
12. 测试验证
```

---

## 技术支持

如遇到问题，请检查：
1. 后端日志：`/www/wwwroot/wxchat-demo/logs/`
2. Nginx日志：`/www/wwwlogs/`
3. 数据库记录：`ai_chat_message` 表

---

## License

MIT
