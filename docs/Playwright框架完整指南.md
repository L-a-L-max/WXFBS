# 🎭 Playwright 框架完整指南

> **目标读者**: 需要使用Playwright实现浏览器自动化任务的Engine端开发者  
> **文档用途**: 详细讲解浏览器池管理、会话复用、资源监控等核心机制  
> **更新日期**: 2025-12-23

---

## 📑 目录导航

### ⭐⭐⭐ 必读章节（核心架构）
1. [框架概述](#1-框架概述) - 理解整体架构和核心特性
2. [核心组件详解](#2-核心组件详解) - 浏览器池和会话管理
3. [使用指南](#3-使用指南) - 快速开始使用Playwright

### ⭐⭐ 进阶章节（开发必读）
4. [配置说明](#4-配置说明) - 配置参数详解
5. [资源管理与监控](#5-资源管理与监控) - 防止内存泄漏
6. [最佳实践](#6-最佳实践) - 开发规范和技巧

### ⭐ 参考章节（按需查阅）
7. [代码文件结构](#7-代码文件结构) - 文件组织结构
8. [业务示例](#8-业务示例) - 实际使用案例
9. [常见问题](#9-常见问题) - 疑难解答

---

## 1. 框架概述

### 1.1 架构图

```
┌─────────────────────────────────────────────────────────────────────┐
│                        WxFbsir-engine                                │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                    PlaywrightTaskExecutor                     │   │
│  │    ┌──────────┐  ┌──────────┐  ┌──────────┐                  │   │
│  │    │ 同步执行  │  │ 异步执行  │  │ 超时执行  │                  │   │
│  │    └────┬─────┘  └────┬─────┘  └────┬─────┘                  │   │
│  └─────────┼─────────────┼─────────────┼───────────────────────┘   │
│            │             │             │                            │
│            ▼             ▼             ▼                            │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                    BrowserPoolManager                         │   │
│  │    ┌─────────────┐    ┌─────────────┐    ┌─────────────┐    │   │
│  │    │ 持久化会话池  │    │  临时会话池   │    │  资源监控    │    │   │
│  │    │  (用户隔离)  │    │  (任务隔离)   │    │  (泄漏检测)  │    │   │
│  │    └──────┬──────┘    └──────┬──────┘    └─────────────┘    │   │
│  └───────────┼──────────────────┼──────────────────────────────┘   │
│              │                  │                                   │
│              ▼                  ▼                                   │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                      BrowserSession                           │   │
│  │    ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐  │   │
│  │    │ Browser  │  │ Context  │  │  Page    │  │ 资源计数  │  │   │
│  │    └──────────┘  └──────────┘  └──────────┘  └──────────┘  │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                              │                                      │
│                              ▼                                      │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                    PlaywrightManager                          │   │
│  │              (单例 Playwright 实例管理)                        │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
```

### 1.2 核心特性

| 特性 | 说明 |
|------|------|
| ✅ **池化管理** | 浏览器会话复用，避免频繁创建销毁 |
| ✅ **用户隔离** | 每个用户独立的浏览器上下文和数据目录 |
| ✅ **实例隔离** | 支持单用户多浏览器实例（instanceId） |
| ✅ **剪贴板隔离** | 每页面独立锁，防止并发冲突 |
| ✅ **截图隔离** | 每页面独立锁，防止并发冲突 |
| ✅ **资源监控** | 创建/销毁计数，泄漏检测告警 |
| ✅ **僵尸进程清理** | 定时清理残留的 Chrome 进程 |
| ✅ **动态配置** | 根据系统硬件自动计算最优参数 |

---

## 2. 核心组件详解

### 2.1 老项目问题分析

| 问题 | cube-engine 表现 | 本框架解决方案 |
|------|------------------|---------------|
| **内存泄漏** | 静态 HashMap 未清理、Playwright 实例不关闭 | 池化管理 + 自动过期清理 + 显式资源释放 |
| **线程泄漏** | 多个独立 ScheduledExecutorService | 统一线程池管理 + @PreDestroy 关闭 |
| **资源耗尽** | 无并发控制，无限创建浏览器 | Semaphore 限制最大会话数 |
| **僵尸进程** | 异常退出后 Chrome 进程残留 | 定时清理 + 锁文件检测 |
| **剪贴板冲突** | 多线程同时操作剪贴板 | 每页面独立 ReentrantLock |
| **截图冲突** | 多线程同时截图 | 每页面独立 ReentrantLock |
| **静默异常** | catch 块空处理或 e.printStackTrace() | 所有异常记录 SLF4J 日志 |
| **重试复杂** | 15次重试逻辑 | 简化为3次，快速失败 |

### 2.2 隔离性设计

#### 用户隔离

```
数据目录结构：
${user.home}/.wxfbsir/playwright-data/
├── persistent/
│   ├── user-001/
│   │   ├── baidu/          # 用户001的百度会话
│   │   │   └── Default/
│   │   └── deepseek/       # 用户001的DeepSeek会话
│   │       └── Default/
│   └── user-002/
│       └── baidu/          # 用户002的百度会话（完全隔离）
└── temporary/
    └── task-xxx/           # 临时任务会话
```

#### 单用户多实例隔离

```java
// 同一用户可以同时操作多个独立浏览器实例
BrowserSession session1 = browserPool.acquirePersistent("user-001", "baidu", "instance-1");
BrowserSession session2 = browserPool.acquirePersistent("user-001", "baidu", "instance-2");
// session1 和 session2 完全独立，互不影响
```

#### 剪贴板/截图锁隔离

```
锁结构：
ClipboardManager.pageLocks: Map<PageId, ReentrantLock>
ScreenshotUtil.pageLocks: Map<PageId, ReentrantLock>

用户A.Page1 ─── 独立锁1
用户A.Page2 ─── 独立锁2
用户B.Page1 ─── 独立锁3（与用户A完全隔离）
```

### 2.3 资源管理设计

| 机制 | 说明 |
|------|------|
| **创建计数** | 记录 Browser/Context/Page 创建次数 |
| **销毁计数** | 记录 Browser/Context/Page 销毁次数 |
| **泄漏检测** | `创建数 - 销毁数 - 活跃数 > 阈值` 时告警 |
| **定时清理** | 每10分钟清理僵尸进程和锁文件 |
| **优雅关闭** | @PreDestroy 时按序关闭所有资源 |

---

## 注：代码文件结构详情

```
WxFbsir-engine/src/main/java/com/wx/fbsir/engine/playwright/
│
├── config/
│   └── PlaywrightProperties.java      # 配置属性类
│       - 读取 application.yml 中的 playwright 配置
│       - 提供默认值和动态计算支持
│
├── core/
│   ├── PlaywrightManager.java         # Playwright 实例管理（单例）
│   │   - 懒加载创建 Playwright 实例
│   │   - ReentrantLock 保证线程安全
│   │   - 僵尸进程清理
│   │   - 锁文件清理
│   │   - 故障重建机制
│   │
│   └── PlaywrightTaskExecutor.java    # 任务执行器
│       - 同步/异步/超时任务执行
│       - 统一线程池管理
│       - 任务状态统计
│       - 支持 instanceId 隔离
│
├── pool/
│   └── BrowserPoolManager.java        # 浏览器会话池
│       - 持久化会话管理（用户数据保存）
│       - 临时会话管理（无痕模式）
│       - Semaphore 并发控制
│       - 定时清理过期会话
│       - 资源泄漏检测
│
├── session/
│   └── BrowserSession.java            # 会话抽象
│       - 封装 Browser/Context/Page
│       - 生命周期管理
│       - 资源计数（创建/关闭）
│       - 支持 instanceId 隔离
│
├── monitor/
│   └── ResourceMonitor.java           # 资源监控器
│       - 锁泄漏检测
│       - 线程泄漏检测
│       - 内存使用监控
│       - 定期状态报告
│
├── scheduler/
│   └── PlaywrightScheduledTasks.java  # 定时任务
│       - 每小时清理过期截图
│       - 每10分钟清理僵尸进程
│
├── util/
│   ├── ClipboardManager.java          # 剪贴板操作
│   │   - 每页面独立锁
│   │   - 10秒锁超时
│   │   - 支持降级方案 (execCommand)
│   │
│   ├── ScreenshotUtil.java            # 截图工具
│   │   - 每页面独立锁
│   │   - 30秒锁超时
│   │   - 支持页面/元素/二维码截图
│   │
│   └── SystemCapabilityDetector.java  # 系统能力检测
│       - 检测 CPU 核心数
│       - 检测可用内存
│       - 动态计算最优配置
│
└── package-info.java                  # 包说明文档
```

### 文件功能速查表

| 文件 | 职责 | 关键方法 |
|------|------|---------|
| `PlaywrightManager` | 管理 Playwright 单例 | `getPlaywright()`, `rebuild()`, `cleanupZombieProcesses()` |
| `PlaywrightTaskExecutor` | 任务执行入口 | `execute()`, `executeAsync()`, `executeWithTimeout()` |
| `BrowserPoolManager` | 会话池管理 | `acquirePersistent()`, `acquireTemporary()`, `releaseSession()` |
| `BrowserSession` | 会话操作 | `getOrCreatePage()`, `closePage()`, `destroy()` |
| `ResourceMonitor` | 资源监控 | `checkResources()`, `getResourceStatus()` |
| `ClipboardManager` | 剪贴板操作 | `write()`, `read()`, `pasteToElement()` |
| `ScreenshotUtil` | 截图操作 | `capture()`, `captureAsBase64()`, `captureQrCode()` |

---

## 四、配置说明

### 4.1 完整配置示例

```yaml
wxfbsir:
  engine:
    playwright:
      # 基础配置
      enabled: true                                    # 是否启用
      data-dir: ${user.home}/.wxfbsir/playwright-data  # 数据目录
      headless: false                                  # 默认无头模式
      dynamic-performance: true                        # 启用动态性能计算
      
      # 浏览器池配置
      pool:
        max-size: 0                    # 最大会话数（0=动态计算）
        min-idle: 0                    # 最小空闲数
        session-timeout: 3600000       # 会话超时（毫秒，默认1小时）
        cleanup-interval: 300000       # 清理间隔（毫秒，默认5分钟）
        acquire-timeout: 30000         # 获取超时（毫秒）
        
      # 浏览器启动配置
      browser:
        launch-timeout: 60000          # 启动超时
        navigation-timeout: 30000      # 导航超时
        viewport-width: 1280           # 视口宽度
        viewport-height: 720           # 视口高度
        disable-images: false          # 禁用图片（节省资源）
        disable-gpu: true              # 禁用GPU（服务器环境）
        max-retries: 3                 # 最大重试次数
        retry-interval: 2000           # 重试间隔（毫秒）
        
      # 线程池配置
      thread-pool:
        core-size: 0                   # 核心线程数（0=动态计算）
        max-size: 0                    # 最大线程数（0=动态计算）
        keep-alive-seconds: 60         # 空闲时间
        queue-capacity: 0              # 队列大小（0=动态计算）
```

### 4.2 动态配置说明

当 `dynamic-performance: true` 且对应配置为 `0` 时，系统会根据硬件自动计算：

| 配置项 | 计算公式 | 4核8G示例 | 8核16G示例 |
|--------|---------|----------|-----------|
| `pool.max-size` | min(CPU核心数, 可用内存GB/2) | 4 | 8 |
| `thread-pool.core-size` | CPU核心数 | 4 | 8 |
| `thread-pool.max-size` | CPU核心数 * 2 | 8 | 16 |
| `thread-pool.queue-capacity` | max-size * 10 | 80 | 160 |

### 4.3 低配置系统优化

对于 2核4G 以下的系统，框架会自动：
- 将池大小限制为 1-2
- 建议启用 `disable-images: true`
- 减少线程池大小

---

## 五、核心组件详解

### 5.1 PlaywrightManager

**职责**：管理 Playwright 单例实例的生命周期

```java
@Component
public class PlaywrightManager {
    
    // 获取 Playwright 实例（懒加载）
    public Playwright getPlaywright();
    
    // 故障恢复重建
    public void rebuild();
    
    // 清理僵尸进程（公开方法）
    public void cleanupZombieProcesses();
    
    // 检查是否可用
    public boolean isAvailable();
}
```

**关键设计**：
- 使用 `AtomicReference<Playwright>` + `ReentrantLock` 保证线程安全
- 创建失败时自动清理僵尸进程后重试
- `@PreDestroy` 时优雅关闭所有资源

### 5.2 BrowserPoolManager

**职责**：管理浏览器会话池

```java
@Component
public class BrowserPoolManager {
    
    // 获取持久化会话
    public BrowserSession acquirePersistent(String userId, String name);
    public BrowserSession acquirePersistent(String userId, String name, boolean headless);
    public BrowserSession acquirePersistent(String userId, String name, String instanceId);
    
    // 获取临时会话
    public BrowserSession acquireTemporary(String taskId);
    
    // 释放会话
    public void releaseSession(BrowserSession session);
    
    // 关闭指定会话
    public void closeSession(String userId, String name);
    
    // 获取池状态
    public Map<String, Object> getStatus();
    
    // 获取资源泄漏信息
    public String getResourceLeakInfo();
}
```

**会话 Key 格式**：
- 持久化会话：`persistent:{userId}:{name}` 或 `persistent:{userId}:{name}:{instanceId}`
- 临时会话：`temporary:{taskId}`

### 5.3 BrowserSession

**职责**：封装单个浏览器会话

```java
public class BrowserSession implements AutoCloseable {
    
    // 获取或创建页面
    public Page getOrCreatePage();
    
    // 关闭指定页面
    public void closePage(Page page);
    
    // 保持会话活跃
    public void touch();
    
    // 检查是否过期
    public boolean isExpired();
    
    // 获取资源泄漏信息
    public String getResourceLeakInfo();
    
    // 销毁会话（释放所有资源）
    public void destroy();
}
```

**资源释放顺序**：
1. 关闭所有 Page
2. 关闭 BrowserContext
3. 关闭 Browser（仅临时会话）

### 5.4 PlaywrightTaskExecutor

**职责**：统一任务执行入口

```java
@Component
public class PlaywrightTaskExecutor {
    
    // 同步执行（持久化会话）
    public <T> T execute(String userId, String name, 
                         Function<BrowserSession, T> task);
    
    // 同步执行（带实例ID）
    public <T> T execute(String userId, String name, String instanceId,
                         Function<BrowserSession, T> task);
    
    // 异步执行
    public <T> CompletableFuture<T> executeAsync(String userId, String name,
                                                  Function<BrowserSession, T> task);
    
    // 带超时执行
    public <T> T executeWithTimeout(String userId, String name,
                                    long timeout, TimeUnit unit,
                                    Function<BrowserSession, T> task);
    
    // 临时会话执行
    public <T> T executeTemporary(String taskId, 
                                   Function<BrowserSession, T> task);
    
    // 获取执行器状态
    public Map<String, Object> getStatus();
}
```

---

## 六、使用指南

### 6.1 基本使用

```java
@Service
public class MyService {

    @Autowired
    private BrowserPoolManager browserPool;

    public void basicUsage() {
        // 方式1：try-with-resources（推荐）
        try (BrowserSession session = browserPool.acquirePersistent("user-001", "baidu")) {
            Page page = session.getOrCreatePage();
            page.navigate("https://baidu.com");
            String title = page.title();
            System.out.println("页面标题: " + title);
        } // 自动归还到池

        // 方式2：手动管理
        BrowserSession session = browserPool.acquirePersistent("user-001", "baidu");
        try {
            Page page = session.getOrCreatePage();
            page.navigate("https://baidu.com");
        } finally {
            browserPool.releaseSession(session);
        }
    }
}
```

### 6.2 使用任务执行器

```java
@Service
public class TaskService {

    @Autowired
    private PlaywrightTaskExecutor taskExecutor;

    // 同步执行
    public String syncTask() {
        return taskExecutor.execute("user-001", "baidu", session -> {
            Page page = session.getOrCreatePage();
            page.navigate("https://example.com");
            return page.title();
        });
    }

    // 异步执行
    public CompletableFuture<String> asyncTask() {
        return taskExecutor.executeAsync("user-001", "baidu", session -> {
            Page page = session.getOrCreatePage();
            page.navigate("https://example.com");
            return page.title();
        });
    }

    // 带超时执行
    public String timeoutTask() {
        return taskExecutor.executeWithTimeout("user-001", "baidu", 
            30, TimeUnit.SECONDS, session -> {
                Page page = session.getOrCreatePage();
                page.navigate("https://example.com");
                return page.title();
            });
    }

    // 临时会话执行
    public String temporaryTask() {
        return taskExecutor.executeTemporary("task-001", session -> {
            Page page = session.getOrCreatePage();
            page.navigate("https://example.com");
            return page.title();
        });
    }
}
```

### 6.3 多实例隔离

```java
@Service
public class MultiInstanceService {

    @Autowired
    private PlaywrightTaskExecutor taskExecutor;

    // 同一用户同时操作多个独立浏览器
    public void multiInstance() {
        // 实例1
        CompletableFuture<String> future1 = taskExecutor.executeAsync(
            "user-001", "baidu", "instance-1", session -> {
                Page page = session.getOrCreatePage();
                page.navigate("https://baidu.com/search?q=java");
                return page.title();
            });

        // 实例2（与实例1完全独立）
        CompletableFuture<String> future2 = taskExecutor.executeAsync(
            "user-001", "baidu", "instance-2", session -> {
                Page page = session.getOrCreatePage();
                page.navigate("https://baidu.com/search?q=python");
                return page.title();
            });

        // 等待两个任务完成
        CompletableFuture.allOf(future1, future2).join();
    }
}
```

---

## 七、工具类使用

### 7.1 剪贴板操作

```java
@Service
public class ClipboardService {

    @Autowired
    private ClipboardManager clipboardManager;
    
    @Autowired
    private BrowserPoolManager browserPool;

    public void clipboardOps() {
        try (BrowserSession session = browserPool.acquirePersistent("user-001", "demo")) {
            Page page = session.getOrCreatePage();
            page.navigate("https://example.com");

            // 写入剪贴板
            boolean success = clipboardManager.write(page, "Hello World");

            // 读取剪贴板
            String content = clipboardManager.read(page);

            // 粘贴到元素
            clipboardManager.pasteToElement(page, "#input-field", "粘贴内容");

            // 从元素复制
            String copied = clipboardManager.copyFromElement(page, "#text-content");

            // 清空剪贴板
            clipboardManager.clear(page);
        }
    }
}
```

### 7.2 截图操作

```java
@Service
public class ScreenshotService {

    @Autowired
    private ScreenshotUtil screenshotUtil;
    
    @Autowired
    private BrowserPoolManager browserPool;

    public void screenshotOps() {
        try (BrowserSession session = browserPool.acquirePersistent("user-001", "demo")) {
            Page page = session.getOrCreatePage();
            page.navigate("https://example.com");

            // 截图保存到文件
            Path path = screenshotUtil.capture(page, "screenshot-name");

            // 全页面截图保存到文件
            Path fullPath = screenshotUtil.capture(page, "full-page", true);

            // 截图返回 Base64
            String base64 = screenshotUtil.captureAsBase64(page);

            // 截图返回 Data URL（可直接用于 img src）
            String dataUrl = screenshotUtil.captureAsDataUrl(page);

            // 截取指定元素
            String elementBase64 = screenshotUtil.captureElementAsBase64(page, "#qrcode");

            // 截取二维码（自动等待元素出现，失败降级为全页面截图）
            String qrBase64 = screenshotUtil.captureQrCode(page, ".qrcode-wrapper");
        }
    }
}
```

---

## 八、资源管理与监控

### 8.1 获取状态信息

```java
@RestController
@RequestMapping("/playwright")
public class PlaywrightStatusController {

    @Autowired
    private BrowserPoolManager browserPool;
    
    @Autowired
    private PlaywrightTaskExecutor taskExecutor;
    
    @Autowired
    private ResourceMonitor resourceMonitor;

    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        Map<String, Object> status = new HashMap<>();
        
        // 浏览器池状态
        status.put("pool", browserPool.getStatus());
        // {activeCount, persistentCount, temporaryCount, maxSize, availableSlots}
        
        // 任务执行器状态
        status.put("executor", taskExecutor.getStatus());
        // {activeTaskCount, completedTaskCount, failedTaskCount, poolSize, queueSize}
        
        // 资源监控状态
        status.put("resources", resourceMonitor.getResourceStatus());
        // {clipboardLocks, screenshotLocks, currentThreads, heapUsedMB, alertCount}
        
        // 资源泄漏信息
        status.put("leakInfo", browserPool.getResourceLeakInfo());
        
        return status;
    }
}
```

### 8.2 健康检查端点

```java
// 已内置于 HealthController
GET /health              # 基础健康检查
GET /health/ready        # 就绪检查
GET /health/detail       # 详细状态（包含资源监控）
```

### 8.3 资源监控告警

`ResourceMonitor` 每5分钟自动检查：
- **锁泄漏**：锁数量 > 50 时告警，> 100 时强制清理
- **线程泄漏**：线程增长 > 100 时告警
- **内存使用**：堆内存使用率 > 85% 时告警

---

## 九、业务示例

### 9.1 AI 登录二维码获取

```java
@Service
public class AiLoginService {

    @Autowired
    private BrowserPoolManager browserPool;
    
    @Autowired
    private ScreenshotUtil screenshotUtil;

    public LoginResult getBaiduQrCode(String userId) {
        try (BrowserSession session = browserPool.acquirePersistent(userId, "baidu", false)) {
            Page page = session.getOrCreatePage();
            
            // 导航到登录页
            page.navigate("https://ai.baidu.com/");
            page.waitForLoadState();
            
            // 检查是否已登录
            if (page.locator(".user-avatar").count() > 0) {
                return LoginResult.alreadyLoggedIn();
            }
            
            // 点击登录按钮
            page.locator("text=登录").click();
            page.waitForTimeout(2000);
            
            // 截取二维码
            String qrBase64 = screenshotUtil.captureQrCode(page, ".qrcode-wrapper");
            
            return LoginResult.needScan(qrBase64);
        } catch (Exception e) {
            return LoginResult.error(e.getMessage());
        }
    }

    public LoginResult checkLoginStatus(String userId) {
        try (BrowserSession session = browserPool.acquirePersistent(userId, "baidu")) {
            Page page = session.getOrCreatePage();
            
            // 检查是否已登录
            if (page.locator(".user-avatar").count() > 0) {
                return LoginResult.success();
            }
            
            return LoginResult.pending();
        }
    }
}
```

### 9.2 批量任务执行

```java
@Service
public class BatchTaskService {

    @Autowired
    private PlaywrightTaskExecutor taskExecutor;

    public List<TaskResult> batchFetch(List<String> urls) {
        List<CompletableFuture<TaskResult>> futures = new ArrayList<>();
        
        for (int i = 0; i < urls.size(); i++) {
            String url = urls.get(i);
            String taskId = "batch-" + i;
            
            CompletableFuture<TaskResult> future = taskExecutor.executeTemporaryAsync(
                taskId, session -> {
                    try {
                        Page page = session.getOrCreatePage();
                        page.navigate(url);
                        return TaskResult.success(url, page.title());
                    } catch (Exception e) {
                        return TaskResult.error(url, e.getMessage());
                    }
                });
            
            futures.add(future);
        }
        
        // 等待所有任务完成
        return futures.stream()
            .map(f -> {
                try {
                    return f.get(60, TimeUnit.SECONDS);
                } catch (Exception e) {
                    return TaskResult.error("unknown", e.getMessage());
                }
            })
            .collect(Collectors.toList());
    }
}
```

---

## 十、最佳实践

### 10.1 资源管理

```java
// ✅ 正确：使用 try-with-resources
try (BrowserSession session = browserPool.acquirePersistent("userId", "task")) {
    // 操作...
}

// ❌ 错误：可能导致资源泄漏
BrowserSession session = browserPool.acquirePersistent("userId", "task");
// 操作...
// 忘记关闭或发生异常
```

### 10.2 长时间任务

```java
// ✅ 正确：定期调用 touch() 保持会话活跃
try (BrowserSession session = browserPool.acquirePersistent("userId", "task")) {
    for (int i = 0; i < 30; i++) {
        session.touch();  // 防止会话超时
        page.waitForTimeout(2000);
        // 检查状态...
    }
}

// ✅ 正确：使用带超时的执行
taskExecutor.executeWithTimeout("userId", "task", 120, TimeUnit.SECONDS, session -> {
    // 长时间操作...
    return result;
});
```

### 10.3 会话类型选择

```java
// 需要保持登录状态 → 持久化会话
browserPool.acquirePersistent("userId", "baidu");

// 一次性任务、不需要保存状态 → 临时会话
browserPool.acquireTemporary("taskId");
```

### 10.4 异常处理

```java
// ✅ 正确：捕获并处理特定异常
try (BrowserSession session = browserPool.acquirePersistent("userId", "task")) {
    Page page = session.getOrCreatePage();
    page.navigate("https://example.com");
} catch (TimeoutError e) {
    log.warn("页面加载超时: {}", e.getMessage());
    // 重试或降级处理
} catch (PlaywrightException e) {
    log.error("Playwright 异常: {}", e.getMessage());
    // 可能需要重建会话
}
```

---

## 十一、常见问题

### Q1: 浏览器启动失败

**排查步骤**：
1. 检查是否安装了 Playwright 浏览器：
   ```bash
   mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install chromium"
   ```
2. 检查 `data-dir` 目录写入权限
3. 查看日志中的详细错误信息
4. 检查是否存在僵尸进程占用

### Q2: 会话数达到上限

**解决方案**：
1. 增加 `pool.max-size` 配置
2. 减少 `session-timeout` 让空闲会话更快释放
3. 确保代码中正确关闭会话（使用 try-with-resources）
4. 检查是否存在资源泄漏（调用 `getResourceLeakInfo()`）

### Q3: 剪贴板操作失败

**解决方案**：
1. 无头模式下某些剪贴板操作可能受限，框架会自动降级使用 `execCommand`
2. 如果仍然失败，考虑使用有头模式 `headless: false`
3. 检查是否存在锁超时（默认10秒）

### Q4: 截图返回空或异常

**排查步骤**：
1. 检查元素选择器是否正确
2. 检查页面是否加载完成（使用 `waitForLoadState()`）
3. 检查是否存在锁超时（默认30秒）
4. 查看日志中的详细错误信息

### Q5: 僵尸进程问题

**框架已内置解决方案**：
- 启动时清理僵尸进程
- 定时清理（每10分钟）
- 创建失败时清理后重试
- 关闭时清理所有残留进程

**手动清理命令**：
```bash
# macOS/Linux
ps aux | grep chromium | grep -v grep | awk '{print $2}' | xargs kill -9

# Windows
taskkill /F /IM chrome.exe
```

---

## 附录：配置参数速查

| 参数路径 | 默认值 | 说明 |
|----------|--------|------|
| `playwright.enabled` | true | 是否启用 |
| `playwright.data-dir` | `${user.home}/.wxfbsir/playwright-data` | 数据目录 |
| `playwright.headless` | false | 默认无头模式 |
| `playwright.dynamic-performance` | true | 动态性能计算 |
| `playwright.pool.max-size` | 0 (动态) | 最大会话数 |
| `playwright.pool.session-timeout` | 3600000 | 会话超时(ms) |
| `playwright.pool.cleanup-interval` | 300000 | 清理间隔(ms) |
| `playwright.pool.acquire-timeout` | 30000 | 获取超时(ms) |
| `playwright.browser.launch-timeout` | 60000 | 启动超时(ms) |
| `playwright.browser.max-retries` | 3 | 最大重试次数 |
| `playwright.thread-pool.core-size` | 0 (动态) | 核心线程数 |
| `playwright.thread-pool.max-size` | 0 (动态) | 最大线程数 |

---

*文档版本: v1.0.0B*  
*更新日期: 2025-12-26*
---


**维护者**: WxFbsir Team

## 📚 相关文档

- [快速上手指南](./快速上手指南.md) - 新手入门必读
- [WebSocket通信完整指南](./WebSocket通信完整指南.md) - WebSocket通信机制
- [WebSocket测试指南](./WebSocket测试指南.md) - 测试方法和工具
- [代码规范](./代码规范.md) - 代码编写规范
