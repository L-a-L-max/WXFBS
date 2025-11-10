## cube-engine 部署指南

## 前置要求
请先完成 [公共环境部署](../common_deployment_guide.md) 中的所有步骤，包括 JDK、Maven 的安装和项目仓库克隆。

### 环境要求
- JDK 17
- Git
- Maven
- Windows 10系统及以上
- 建议内存：16GB (8GB会有卡顿现象)

## 第一阶段：基础服务部署

### 1. 构建与启动
- 进入项目目录：   
    ```bash
        cd cube-engine
    ```
- 执行打包：
    ```bash
        mvn clean package -DskipTests
    ```
- 启动服务：
  ```bash
      java -jar target/U3W.jar
  ```
  
### 2. 配置cube-engine服务连接
- 启动后按提示输入以下信息：
```bash
请输入地址：127.0.0.1:8081（或localhost:8081）
是否启用HTTPS/WSS？(y/n，默认n)：n
✅ 检测到可用端口：（默认8083）
请输入主机ID：[您添加至白名单的主机ID]
请输入CPU核心数（默认使用系统可用核心数，直接回车跳过）：跳过
请输入最大线程数（默认使用系统可用处理器数*2，直接回车跳过）：跳过
```

- 服务启动后，可通过 http://localhost:[检测到的可用端口]/swagger-ui/index.html 查看接口文档

### 3.后台项目启动
- 启动项目cube-ui
- 配置主机ID，登录元宝和豆包(确保后续能够正常咨询)

## 第二阶段：openAI集成与调用
### openAI集成
- 创建springboot项目,版本2.7.x以上
- 支持模型
  | 模型名称   | 模型引用名称        | 是否支持流式输出 |
  | ---------- | :------------ | ---------------- |
  | 百度AI     | baidu         | 否               |
  | DeepSeek   | deepseek      | 否               |
  | 豆包       | dou_bao       | **是**           |
  | 腾讯元宝T1 | yuan_bao_T1   | **是**           |
  | 腾讯元宝DS | yuan_bao_DS   | **是**           |
  | 知乎直答   | zhi_hu_zhi_da | 否               |
  | 秘塔       | metaso        | **是**           |

### openAI配置
- 引入springAI依赖
```xml
   <dependency>
       <groupId>org.springframework.ai</groupId>
       <artifactId>spring-ai-openai-spring-boot-starter</artifactId>
       <version>1.0.0-M6</version>
   </dependency>
```
- application.yml配置
```yaml
spring:
  ai:
    openai:
      # 配置你的AI服务基础地址（注意ip跟端口需要与你cube-admin中的配置一致）
      base-url: http://localhost:8081
      # 配置你的API密钥  示例 office01-ovZrQ673x1GGaP6cX5XUnfzu7TmE
      api-key: [主机ID]-[UnionID]
      # 聊天接口的默认参数配置
      chat:
        options:
          # 指定要调用的模型ID（需与服务中支持的模型ID一致）
          model: 模型引用名称 
          # 其他可选参数（根据需要配置）
          max-tokens: XXXX
server:
  port: 8080
```
### openAI代码调用示例
- 代码示例

  - AI配置
    ```
    @Configuration
    public class AIConfig {
        @Autowired
        private OpenAiChatModel openAiChatModel;
        @Bean
        public ChatClient chatClient(ChatMemory chatMemory) {
            return ChatClient
                    .builder(openAiChatModel)
                    .defaultSystem("你是一位名字为优立方的助手")
                    .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory)) // 添加会话记忆存储通知器
                    .build();
        }
        @Bean
        public ChatMemory chatMemory() {
            return new InMemoryChatMemory();
        }
    }
    ```
    
  - 测试代码
    ```
    @RestController
    public class AiController {
        @Autowired
        private ChatClient chatClient;
        @GetMapping(value = "/chat/stream/{msg}", produces = "text/html;charset=utf-8")
        public Flux<String> chat(@PathVariable("msg") String msg) {
            // 包装为Prompt
            return chatClient.prompt()
                    .user(msg)
                    .advisors(advisorSpec -> advisorSpec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, 1))
                    .stream()
                    .content()
                    .map(contents -> {
                        System.out.print(contents);
                        return contents;
                    });
        }
        @GetMapping(value = "/chat/{msg}")
        public String chatStream(@PathVariable("msg") String msg) {
            // 包装为Prompt
            String content = chatClient.prompt(msg)
                    .call()
                    .content();
            System.out.println(content);
            return content;
        }
    }
    ```
  - 访问测试(注意ip端口保持与你配置的一致)
    - 流式输出 http://localhost:8080/chat/stream/你好
    - 非流式输出 http://localhost:8080/chat/你好
  
## 注意事项、

- 主机ID必须唯一，避免与其他实例冲突
- 内网穿透工具的选择应考虑稳定性和安全性
- 定期检查服务状态和日志信息
- 如遇问题，可查看控制台日志或联系技术支持
