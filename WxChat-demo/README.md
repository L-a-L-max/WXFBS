# WxChat-demo

企业微信登录 + 元器AI对话回显 Demo 项目

本项目演示如何实现：
1. **企业微信 OAuth 登录**：用户通过企业微信扫码/点击授权登录系统
2. **元器 AI 对话**：用户发送消息到腾讯元器智能体，并通过工作流回调实现消息回显

## 项目结构

```
WxChat-demo/
├── backend/                    # Spring Boot 后端
│   ├── src/main/java/
│   │   └── com/demo/wxchat/
│   │       ├── config/         # 配置类
│   │       ├── controller/     # 控制器
│   │       ├── domain/         # 实体类
│   │       ├── mapper/         # MyBatis Mapper
│   │       ├── service/        # 服务层
│   │       └── util/           # 工具类
│   ├── src/main/resources/
│   │   ├── mapper/             # MyBatis XML
│   │   └── application.yml     # 配置文件
│   └── pom.xml
├── frontend/                   # Vue 前端
│   ├── src/
│   │   ├── api/                # API 接口
│   │   ├── views/              # 页面组件
│   │   └── router/             # 路由配置
│   └── package.json
├── sql/                        # 数据库脚本
│   └── init.sql
└── README.md
```

## 快速开始

### 1. 准备工作

#### 1.1 企业微信配置
1. 登录 [企业微信管理后台](https://work.weixin.qq.com/)
2. 创建自建应用，获取：
   - `corpId`：企业ID
   - `agentId`：应用AgentId
   - `agentSecret`：应用Secret
3. 在应用设置中配置 OAuth 回调域名

#### 1.2 腾讯元器配置
1. 登录 [腾讯元器](https://yuanqi.tencent.com/)
2. 创建智能体/对话助手
3. 配置企业微信通道，获取：
   - `token`：消息验证Token
   - `encodingAesKey`：AES加密密钥（43位）
   - `targetUrl`：元器API地址
4. 在工作流中添加 HTTP 回调节点（详见下方配置）

### 2. 数据库初始化

```bash
mysql -u root -p < sql/init.sql
```

### 3. 后端配置

修改 `backend/src/main/resources/application.yml`：

```yaml
# 数据库配置
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/wxchat_demo?useUnicode=true&characterEncoding=utf8
    username: root
    password: your_password

# 企业微信配置
wework:
  corp-id: your_corp_id
  agent-id: your_agent_id
  agent-secret: your_agent_secret

# 元器AI配置
yuanqi:
  chat:
    token: your_token
    encoding-aes-key: your_encoding_aes_key
    corp-id: your_corp_id
    agent-id: your_agent_id
    target-url: https://yuanqi.tencent.com/openapi/...
```

### 4. 启动后端

```bash
cd backend
mvn spring-boot:run
```

### 5. 启动前端

```bash
cd frontend
npm install
npm run dev
```

### 6. 访问

打开浏览器访问 `http://localhost:5173`

## 元器工作流配置

为了实现 AI 回复回显，需要在元器工作流中添加 HTTP 回调节点：

### 工作流结构

```
[开始] → [代码节点] → [大模型] → [HTTP请求] → [结束]
```

### 代码节点（提取 sessionId）

由于消息格式为 `[SID:xxx]实际消息`，需要用代码节点提取：

```python
import re

def main(params: dict) -> dict:
    user_query = params.get('user_query', '') or params.get('UserQuery', '')
    
    if not user_query:
        for key in params:
            if 'query' in key.lower() or 'input' in key.lower():
                user_query = params.get(key, '')
                if user_query:
                    break
    
    session_id = ''
    real_message = str(user_query)
    
    match = re.match(r'^\[SID:([^\]]+)\]', real_message)
    if match:
        session_id = match.group(1)
        real_message = re.sub(r'^\[SID:[^\]]+\]', '', real_message)
    
    return {
        'sessionId': session_id,
        'realMessage': real_message,
        'success': True
    }
```

### HTTP 请求节点配置

| 配置项 | 值 |
|--------|-----|
| 请求方法 | POST |
| 请求地址 | `https://your-domain.com/api/chat/workflow/callback` |
| Content-Type | application/json |

**请求体**：
```json
{
  "sessionId": "{{代码节点.sessionId}}",
  "content": "{{大模型.content}}",
  "messageId": "{{SYS.RequestId}}"
}
```

## 核心功能说明

### 企业微信登录流程

```
用户访问 → 检测未登录 → 跳转企微授权 → 回调获取code → 换取用户信息 → 登录成功
```

### AI 对话回显流程

```
用户发送消息 → 后端存库 + 发到元器 → 元器工作流处理 → HTTP回调存库 → 前端轮询获取
```

## API 接口

### 企业微信登录

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/auth/authorize` | GET | 发起OAuth授权 |
| `/api/auth/callback` | GET | OAuth回调处理 |

### AI 对话

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/chat/send` | POST | 发送消息 |
| `/api/chat/poll` | GET | 轮询获取回复 |
| `/api/chat/workflow/callback` | POST | 工作流回调（匿名） |

## 注意事项

1. **回调地址必须外网可访问**：元器工作流需要能访问到你的后端接口
2. **sessionId 一致性**：发送和轮询必须使用相同的 sessionId
3. **HTTPS**：生产环境建议使用 HTTPS
4. **跨域配置**：前后端分离需要配置 CORS

## License

MIT
