# WxFbsir-Engine 开发指南

> **Engine端核心模块** - 基于Playwright的浏览器自动化任务引擎


---

## 📖 目录

1. [项目简介](#项目简介)
2. [快速开始](#快速开始)
3. [能力测试与调试](#能力测试与调试)
4. [开发新能力](#开发新能力)
5. [参考文档](#参考文档)
6. [项目结构](#项目结构)

---

## 项目简介

WxFbsir-Engine 是一个基于 **Playwright** 的浏览器自动化任务引擎，通过 **WebSocket** 与业务端（WxFbsir-business）通信，接收任务请求并返回执行结果。

### 核心特性

- ✅ **浏览器自动化** - 基于Playwright，支持Chromium/Firefox/WebKit
- ✅ **会话管理** - 持久化会话、状态保存、资源池管理
- ✅ **流式输出** - 支持进度推送、日志推送、截图推送
- ✅ **单次输出** - 支持快速返回的简单任务
- ✅ **截图上传** - 自动截图并上传到业务端
- ✅ **异常处理** - 完整的错误处理和资源清理机制

### 技术栈

- **Spring Boot** - 应用框架
- **Playwright** - 浏览器自动化
- **WebSocket** - 与业务端通信
- **Jackson** - JSON序列化

---

## 快速开始

### 1. 环境要求

- **JDK 17+**
- **Maven 3.6+**
- **Playwright** - 首次运行会自动下载浏览器

### 2. 配置文件

编辑 `application.yml`：

```yaml
# WebSocket连接配置
websocket:
  admin:
    url: ws://localhost:8080/ws/engine  # 业务端WebSocket地址
    engineId: engine-001                # Engine唯一标识
    reconnect:
      enabled: true
      maxAttempts: 10
      initialDelay: 1000

# Playwright配置
playwright:
  browser:
    type: chromium                      # 浏览器类型
    headless: true                      # 无头模式
  pool:
    maxSize: 5                          # 浏览器池最大数量
```

### 3. 启动Engine

```bash
# 编译
mvn clean package

# 运行
java -jar target/WxFbsir-engine.jar
```

启动成功后，Engine会自动连接到业务端WebSocket服务。

---

## 能力测试与调试

### 方式一：使用前端调试工具（推荐）

部署好前端后，登录系统，进入 **主机管理 > WebSocket调试** 页面进行能力测试。

#### 优势
- ✅ 图形化界面，操作简单
- ✅ 自动格式化JSON
- ✅ 实时查看消息流
- ✅ 支持消息导出

#### 测试步骤

1. **登录前端系统**
   ```
   访问: http://localhost:80
   账号: admin
   密码: admin123
   ```

2. **进入调试页面**
   ```
   导航: 主机管理 > WebSocket调试
   ```

3. **发送测试消息**
   
   **示例1：健康检查（单次输出）**
   ```json
   {
     "type": "SIMPLE_HEALTH_CHECK_DEMO",
     "engineId": "engine-001",
     "payload": {
       "includeDetails": true
     }
   }
   ```

   **示例2：百度热搜（流式输出）**
   ```json
   {
     "type": "BAIDU_HOT_SEARCH_DEMO",
     "engineId": "engine-001",
     "payload": {
       "clickIndex": 0,
       "needScreenshot": true
     }
   }
   ```

   **示例3：复杂任务**
   ```json
   {
     "type": "COMPLEX_TASK",
     "engineId": "engine-001",
     "payload": {
       "config": {
         "timeout": 30000,
         "retry": true
       },
       "filters": [
         {"field": "status", "value": "active"}
       ],
       "metadata": {
         "tags": ["tag1", "tag2"]
       }
     }
   }
   ```

4. **查看返回结果**
   - 消息输出区域会实时显示所有消息
   - 发送消息显示为绿色
   - 接收消息显示为蓝色
   - JSON自动高亮显示

### 方式二：使用websocat命令行工具

```bash
# 安装websocat
brew install websocat  # macOS
# 或
cargo install websocat  # Rust

# 连接WebSocket
TOKEN="your_jwt_token"
websocat "ws://localhost:8080/ws/client?clientType=web&token=${TOKEN}"

# 发送测试消息（单行JSON）
{"type":"SIMPLE_HEALTH_CHECK_DEMO","engineId":"engine-001","payload":{"includeDetails":true}}
```

---

## 开发新能力

### 步骤1：查看演示代码

推荐先阅读 `src/main/java/com/wx/fbsir/engine/controller/demo/README.md`，了解：

- **BaiduHotSearchDemoController** - 流式输出完整示例
- **SimpleHealthCheckDemoController** - 单次输出完整示例

### 步骤2：创建Controller

在 `src/main/java/com/wx/fbsir/engine/controller/` 下创建新的Controller类：

```java
package com.wx.fbsir.engine.controller;

import com.wx.fbsir.engine.capability.annotation.StreamCapability;
import com.wx.fbsir.engine.capability.base.StreamTaskHelper;
import com.wx.fbsir.engine.websocket.message.EngineMessage;
import org.springframework.stereotype.Controller;

@Controller
public class MyNewController extends StreamTaskHelper {
    
    @StreamCapability(
        type = "MY_NEW_TASK",
        description = "我的新任务",
        timeout = 60000L,
        progressInterval = 3000L  // 可选：自动心跳间隔
    )
    public void handleMyTask(EngineMessage message) {
        String userId = message.getUserId();
        String requestId = message.getPayloadValue("requestId");
        
        // 创建流式任务
        StreamTask task = startStreamTask(userId, requestId);
        
        try {
            // 推送进度日志
            task.sendLog("正在处理任务...");
            
            // 执行业务逻辑
            // ...
            
            // 发送成功结果
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("result", "success");
            task.sendSuccess("任务完成", resultData);
            
        } catch (Exception e) {
            task.sendError("任务失败: " + e.getMessage());
        } finally {
            task.stop();  // 停止心跳
        }
    }
}
```

### 步骤3：选择实现方式

#### 单次输出（适合快速任务）

- 不继承 `StreamTaskHelper`
- 使用 `@OnceCapability` 注解
- 直接返回最终结果
- 适用场景：数据查询、状态检查（< 5秒）

#### 流式输出（适合长时间任务）

- 继承 `StreamTaskHelper`
- 使用 `@StreamCapability` 注解
- 支持进度推送、日志推送、截图推送
- 适用场景：爬虫、AI对话、文件处理（> 5秒）

### 步骤4：测试新能力

1. 重启Engine
2. 在前端调试工具中发送测试消息
3. 观察消息流和返回结果

---

## 参考文档

### 核心文档

1. **[Playwright框架完整指南](../docs/Playwright框架完整指南.md)**
   - 浏览器自动化开发指南
   - 会话管理、资源池、截图上传
   - 最佳实践和常见问题

2. **[WebSocket通信完整指南](../docs/WebSocket通信完整指南.md)**
   - WebSocket消息协议
   - 流式输出与单次输出对比
   - 消息类型说明和示例代码

3. **[演示Controller完整指南](src/main/java/com/wx/fbsir/engine/controller/demo/README.md)**
   - 完整的代码示例
   - 单次输出 vs 流式输出
   - 开发建议和最佳实践

### 快速索引

| 需求 | 参考文档 | 章节 |
|------|---------|------|
| 快速入门Playwright | Playwright框架完整指南 | 第0章 |
| 理解消息协议 | WebSocket通信完整指南 | 第3章 |
| 查看代码示例 | 演示Controller完整指南 | 全文 |
| 会话管理 | Playwright框架完整指南 | 第2章 |
| 截图上传 | Playwright框架完整指南 | 附录A |
| 流式输出 | WebSocket通信完整指南 | 第3.2节 |

---

## 项目结构

```
WxFbsir-engine/
├── src/main/java/com/wx/fbsir/engine/
│   ├── capability/              # 能力注册与管理
│   │   ├── annotation/          # 注解定义
│   │   │   ├── OnceCapability.java      # 单次输出注解
│   │   │   └── StreamCapability.java    # 流式输出注解
│   │   ├── base/                # 基础类
│   │   │   └── StreamTaskHelper.java    # 流式任务辅助类
│   │   └── registry/            # 能力注册器
│   ├── controller/              # 业务Controller
│   │   └── demo/                # 演示Controller
│   │       ├── BaiduHotSearchDemoController.java
│   │       ├── SimpleHealthCheckDemoController.java
│   │       └── README.md        # 演示文档
│   ├── playwright/              # Playwright封装
│   │   ├── pool/                # 浏览器池管理
│   │   ├── session/             # 会话管理
│   │   └── util/                # 工具类
│   ├── websocket/               # WebSocket客户端
│   │   ├── client/              # WebSocket客户端
│   │   ├── message/             # 消息定义
│   │   └── util/                # 工具类
│   └── EngineApplication.java   # 启动类
├── src/main/resources/
│   ├── application.yml          # 配置文件
│   └── logback-spring.xml       # 日志配置
├── docs/                        # 文档目录（项目根目录）
│   ├── Playwright框架完整指南.md
│   └── WebSocket通信完整指南.md
└── README.md                    # 本文档
```

---

## 常见问题

### Q1: Engine无法连接到业务端？

**检查清单**:
- ✅ 业务端是否已启动？
- ✅ WebSocket地址是否正确？（默认 `ws://localhost:8080/ws/engine`）
- ✅ Engine ID是否在白名单中？
- ✅ 防火墙是否阻止了连接？

### Q2: 浏览器启动失败？

**解决方案**:
```bash
# 手动安装Playwright浏览器
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install chromium"
```

### Q3: 如何查看详细日志？

修改 `logback-spring.xml`：
```xml
<logger name="com.wx.fbsir.engine" level="DEBUG"/>
```

### Q4: 如何调试WebSocket消息？

使用前端调试工具（推荐）或查看Engine日志：
```
[WebSocket] 收到消息: {"type":"SIMPLE_HEALTH_CHECK_DEMO",...}
[WebSocket] 发送消息: {"type":"TASK_RESULT",...}
```

---

## 开发建议

### 1. 代码规范

- ✅ Controller类名以 `Controller` 结尾
- ✅ 能力方法名以 `handle` 开头
- ✅ 使用 `@Controller` 注解标记
- ✅ 继承 `StreamTaskHelper`（流式任务）
- ✅ 注入 `WebSocketClientManager`（单次任务）

### 2. 异常处理

```java
try {
    // 业务代码
    task.sendSuccess("完成", resultData);
} catch (Exception e) {
    log.error("任务失败", e);
    task.sendError("任务失败: " + e.getMessage());
} finally {
    task.stop();  // 停止心跳
    if (session != null) session.destroy();  // 释放资源
}
```

### 3. 资源管理

```java
BrowserSession session = null;
try {
    session = browserPool.acquirePersistent(userId, "key", false);
    // 使用session...
} finally {
    if (session != null) session.destroy();  // 必须释放
}
```

### 4. 参数提取

```java
// 带默认值
Integer count = message.getPayloadValue("count");
if (count == null) count = 10;

// 类型转换异常处理
try {
    List<String> items = message.getPayloadValue("items");
} catch (ClassCastException e) {
    task.sendError("参数items必须是数组");
    return;
}
```

---

## 贡献指南

欢迎提交Issue和Pull Request！

### 提交规范

- **feat**: 新功能
- **fix**: 修复bug
- **docs**: 文档更新
- **refactor**: 代码重构
- **test**: 测试相关

---


最近更新：2025年12月29日 18:42