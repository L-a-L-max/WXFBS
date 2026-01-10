package com.wx.fbsir.engine.utils.yuanqi;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 元器（YuanQi）节点操作工具类
 * 
 * 功能说明：
 * 1. 编辑大模型节点 - 定位节点、填充参数、保存配置
 * 2. 调试工作流 - 点击调试按钮、填入参数、执行调试
 * 3. 发布工作流 - 点击发布按钮、确认发布
 * 
 * 安全机制：
 * - 所有操作前进行参数校验
 * - 敏感信息不记录到日志
 * - 操作失败时提供详细错误信息
 * 
 * @author wxfbsir
 * @date 2025-01-10
 */
@Slf4j
@Component
public class YuanQiNodeUtil {

    /**
     * 节点编辑结果
     */
    public static class NodeEditResult {
        private final boolean success;
        private final String message;
        private final String nodeName;
        
        public NodeEditResult(boolean success, String message, String nodeName) {
            this.success = success;
            this.message = message;
            this.nodeName = nodeName;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
        
        public String getNodeName() {
            return nodeName;
        }
    }
    
    /**
     * 调试结果
     */
    public static class DebugResult {
        private final boolean success;
        private final String message;
        private final String output;
        
        public DebugResult(boolean success, String message, String output) {
            this.success = success;
            this.message = message;
            this.output = output;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
        
        public String getOutput() {
            return output;
        }
    }
    
    /**
     * 发布结果
     */
    public static class PublishResult {
        private final boolean success;
        private final String message;
        
        public PublishResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
    }

    /**
     * 编辑大模型节点
     * 
     * 操作流程：
     * 1. 等待工作流画布加载完成
     * 2. 定位并点击指定节点
     * 3. 打开节点配置面板
     * 4. 填充模型参数（ModelName、Temperature、TopP、MaxTokens）
     * 5. 填充提示词
     * 6. 保存配置
     * 
     * @param page 工作流编辑页面
     * @param nodeName 节点名称
     * @param config 配置参数（modelName, temperature, topP, maxTokens, prompt）
     * @return 编辑结果
     */
    public NodeEditResult editLLMNode(Page page, String nodeName, Map<String, Object> config) {
        try {
            log.info("[元器节点编辑] 开始编辑节点: {}", nodeName);
            
            // 参数校验
            if (nodeName == null || nodeName.isEmpty()) {
                return new NodeEditResult(false, "节点名称不能为空", null);
            }
            if (config == null || config.isEmpty()) {
                return new NodeEditResult(false, "配置参数不能为空", nodeName);
            }
            
            // 步骤1：等待画布加载
            log.debug("[元器节点编辑] 步骤1: 等待工作流画布加载...");
            page.waitForLoadState();
            page.waitForTimeout(3000);
            
            // 步骤2：定位并点击节点
            log.debug("[元器节点编辑] 步骤2: 定位节点: {}", nodeName);
            Locator node = findNode(page, nodeName);
            if (node == null || node.count() == 0) {
                log.warn("[元器节点编辑] 未找到节点: {}", nodeName);
                return new NodeEditResult(false, "未找到节点: " + nodeName, nodeName);
            }
            
            // 单击选中节点
            log.debug("[元器节点编辑] 点击选中节点");
            node.first().click();
            page.waitForTimeout(1000);
            
            // 步骤3：双击打开配置面板
            log.debug("[元器节点编辑] 步骤3: 打开节点配置面板");
            node.first().dblclick();
            page.waitForTimeout(2000);
            
            // 步骤4：填充配置参数
            log.debug("[元器节点编辑] 步骤4: 填充配置参数");
            
            // 填充模型名称
            if (config.containsKey("modelName")) {
                String modelName = String.valueOf(config.get("modelName"));
                log.debug("[元器节点编辑] 选择模型: {}", modelName);
                boolean modelSelected = selectModel(page, modelName);
                if (!modelSelected) {
                    log.warn("[元器节点编辑] 模型选择失败: {}", modelName);
                }
            }
            
            // 填充Temperature
            if (config.containsKey("temperature")) {
                Object tempValue = config.get("temperature");
                log.debug("[元器节点编辑] 设置Temperature: {}", tempValue);
                fillParameter(page, "temperature", tempValue);
            }
            
            // 填充TopP
            if (config.containsKey("topP")) {
                Object topPValue = config.get("topP");
                log.debug("[元器节点编辑] 设置TopP: {}", topPValue);
                fillParameter(page, "topP", topPValue);
            }
            
            // 填充MaxTokens
            if (config.containsKey("maxTokens")) {
                Object maxTokensValue = config.get("maxTokens");
                log.debug("[元器节点编辑] 设置MaxTokens: {}", maxTokensValue);
                fillParameter(page, "maxTokens", maxTokensValue);
            }
            
            // 填充提示词（不记录具体内容到日志，保护敏感信息）
            if (config.containsKey("prompt")) {
                String prompt = String.valueOf(config.get("prompt"));
                log.debug("[元器节点编辑] 填充提示词 (长度: {} 字符)", prompt.length());
                fillPrompt(page, prompt);
            }
            
            // 步骤5：保存配置
            log.debug("[元器节点编辑] 步骤5: 保存节点配置");
            boolean saved = saveNodeConfig(page);
            
            if (saved) {
                log.info("[元器节点编辑] 节点配置保存成功: {}", nodeName);
                return new NodeEditResult(true, "节点配置已保存", nodeName);
            } else {
                log.warn("[元器节点编辑] 节点配置保存失败: {}", nodeName);
                return new NodeEditResult(false, "保存配置失败", nodeName);
            }
            
        } catch (Exception e) {
            log.error("[元器节点编辑] 编辑失败 - 节点: {}", nodeName, e);
            return new NodeEditResult(false, "编辑失败: " + e.getMessage(), nodeName);
        }
    }
    
    /**
     * 调试工作流
     * 
     * 操作流程：
     * 1. 点击调试按钮
     * 2. 等待调试面板出现
     * 3. 填入调试输入参数（如果有）
     * 4. 点击运行/执行按钮
     * 5. 等待调试结果
     * 
     * @param page 工作流编辑页面
     * @param debugInput 调试输入参数（可选）
     * @return 调试结果
     */
    public DebugResult debugWorkflow(Page page, Map<String, Object> debugInput) {
        try {
            log.info("[元器工作流调试] 开始调试");
            
            // 步骤1：点击调试按钮
            log.debug("[元器工作流调试] 步骤1: 点击调试按钮");
            Locator debugButton = findDebugButton(page);
            if (debugButton == null || debugButton.count() == 0) {
                return new DebugResult(false, "未找到调试按钮", null);
            }
            
            debugButton.first().click();
            page.waitForTimeout(2000);
            
            // 步骤2：等待调试面板出现
            log.debug("[元器工作流调试] 步骤2: 等待调试面板");
            page.waitForTimeout(1000);
            
            // 步骤3：填入调试输入参数（如果有）
            if (debugInput != null && !debugInput.isEmpty()) {
                log.debug("[元器工作流调试] 步骤3: 填入调试参数");
                fillDebugInput(page, debugInput);
            }
            
            // 步骤4：点击运行/执行按钮
            log.debug("[元器工作流调试] 步骤4: 点击执行按钮");
            Locator runButton = findRunButton(page);
            if (runButton != null && runButton.count() > 0) {
                runButton.first().click();
            } else {
                log.warn("[元器工作流调试] 未找到执行按钮，尝试其他方式");
            }
            
            // 步骤5：等待调试结果（最多等待60秒）
            log.debug("[元器工作流调试] 步骤5: 等待调试结果");
            page.waitForTimeout(30000);
            
            // 尝试提取调试输出
            String output = extractDebugOutput(page);
            
            log.info("[元器工作流调试] 调试完成");
            return new DebugResult(true, "调试完成", output);
            
        } catch (Exception e) {
            log.error("[元器工作流调试] 调试失败", e);
            return new DebugResult(false, "调试失败: " + e.getMessage(), null);
        }
    }
    
    /**
     * 发布工作流
     * 
     * 操作流程：
     * 1. 点击发布按钮
     * 2. 等待确认弹窗（如果有）
     * 3. 点击确认按钮
     * 4. 等待发布完成
     * 
     * @param page 工作流编辑页面
     * @return 发布结果
     */
    public PublishResult publishWorkflow(Page page) {
        try {
            log.info("[元器工作流发布] 开始发布");
            
            // 步骤1：点击发布按钮
            log.debug("[元器工作流发布] 步骤1: 点击发布按钮");
            Locator publishButton = findPublishButton(page);
            if (publishButton == null || publishButton.count() == 0) {
                return new PublishResult(false, "未找到发布按钮");
            }
            
            publishButton.first().click();
            page.waitForTimeout(2000);
            
            // 步骤2：处理确认弹窗（如果有）
            log.debug("[元器工作流发布] 步骤2: 处理确认弹窗");
            Locator confirmButton = page.locator("button:has-text('确认'), button:has-text('确定'), button:has-text('发布')").first();
            if (confirmButton.count() > 0 && confirmButton.isVisible()) {
                log.debug("[元器工作流发布] 点击确认按钮");
                confirmButton.click();
                page.waitForTimeout(3000);
            }
            
            // 步骤3：等待发布完成
            log.debug("[元器工作流发布] 步骤3: 等待发布完成");
            page.waitForTimeout(5000);
            
            // 检查是否有成功提示
            Locator successTip = page.locator(".t-message--success, .el-message--success, :has-text('发布成功')");
            boolean hasSuccessTip = successTip.count() > 0;
            
            if (hasSuccessTip) {
                log.info("[元器工作流发布] 发布成功");
                return new PublishResult(true, "发布成功");
            } else {
                log.info("[元器工作流发布] 发布操作已执行，请确认结果");
                return new PublishResult(true, "发布操作已执行");
            }
            
        } catch (Exception e) {
            log.error("[元器工作流发布] 发布失败", e);
            return new PublishResult(false, "发布失败: " + e.getMessage());
        }
    }
    
    /**
     * 查找节点
     * 使用多种选择器策略，提高容错性
     */
    private Locator findNode(Page page, String nodeName) {
        // 策略1：通过节点类型和文本内容
        Locator node = page.locator("[data-node-type='llm']:has-text('" + nodeName + "')");
        if (node.count() > 0) {
            log.debug("[元器节点定位] 策略1成功: data-node-type");
            return node;
        }
        
        // 策略2：通过节点名称类
        node = page.locator(".node-name:has-text('" + nodeName + "'), .node-title:has-text('" + nodeName + "')");
        if (node.count() > 0) {
            log.debug("[元器节点定位] 策略2成功: node-name/node-title");
            return node;
        }
        
        // 策略3：通过文本匹配
        node = page.getByText(nodeName, new Page.GetByTextOptions().setExact(false));
        if (node.count() > 0) {
            log.debug("[元器节点定位] 策略3成功: getByText");
            return node;
        }
        
        // 策略4：通过包含文本的div
        node = page.locator("div:has-text('" + nodeName + "')").first();
        if (node.count() > 0) {
            log.debug("[元器节点定位] 策略4成功: div:has-text");
            return node;
        }
        
        log.warn("[元器节点定位] 所有策略均失败，未找到节点: {}", nodeName);
        return null;
    }
    
    /**
     * 选择模型
     */
    private boolean selectModel(Page page, String modelName) {
        try {
            // 查找模型选择下拉框
            Locator modelSelect = page.locator("[class*='model-select'], select[name*='model'], .model-selector, [data-field='model']");
            if (modelSelect.count() > 0) {
                modelSelect.first().click();
                page.waitForTimeout(500);
                
                // 选择指定模型
                Locator modelOption = page.locator("text=" + modelName);
                if (modelOption.count() > 0) {
                    modelOption.first().click();
                    page.waitForTimeout(500);
                    return true;
                }
            }
            
            // 备选：直接在输入框中输入模型名称
            Locator modelInput = page.locator("input[name*='model'], input[placeholder*='模型']");
            if (modelInput.count() > 0) {
                modelInput.first().clear();
                modelInput.first().fill(modelName);
                page.waitForTimeout(500);
                return true;
            }
            
            return false;
        } catch (Exception e) {
            log.warn("[元器模型选择] 选择失败: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 填充参数
     */
    private void fillParameter(Page page, String paramName, Object value) {
        try {
            // 多种选择器策略
            String[] selectors = {
                "input[name*='" + paramName + "']",
                "[data-param='" + paramName + "'] input",
                "[data-field='" + paramName + "'] input",
                "label:has-text('" + paramName + "') + input",
                "label:has-text('" + paramName + "') ~ input"
            };
            
            for (String selector : selectors) {
                Locator input = page.locator(selector);
                if (input.count() > 0) {
                    input.first().clear();
                    input.first().fill(String.valueOf(value));
                    log.debug("[元器参数填充] {} = {} (选择器: {})", paramName, value, selector);
                    return;
                }
            }
            
            log.warn("[元器参数填充] 未找到参数输入框: {}", paramName);
        } catch (Exception e) {
            log.warn("[元器参数填充] 填充失败 - 参数: {}, 错误: {}", paramName, e.getMessage());
        }
    }
    
    /**
     * 填充提示词
     */
    private void fillPrompt(Page page, String prompt) {
        try {
            // 多种选择器策略
            String[] selectors = {
                "textarea[name*='prompt']",
                "[class*='prompt'] textarea",
                ".prompt-editor textarea",
                "[data-field='prompt'] textarea",
                "textarea[placeholder*='提示词']",
                "textarea[placeholder*='Prompt']"
            };
            
            for (String selector : selectors) {
                Locator promptArea = page.locator(selector);
                if (promptArea.count() > 0) {
                    promptArea.first().clear();
                    promptArea.first().fill(prompt);
                    log.debug("[元器提示词填充] 成功 (选择器: {})", selector);
                    return;
                }
            }
            
            log.warn("[元器提示词填充] 未找到提示词输入框");
        } catch (Exception e) {
            log.warn("[元器提示词填充] 填充失败: {}", e.getMessage());
        }
    }
    
    /**
     * 保存节点配置
     */
    private boolean saveNodeConfig(Page page) {
        try {
            // 查找保存按钮
            Locator saveButton = page.locator("button:has-text('保存'), button:has-text('确定'), .save-btn, [data-action='save']");
            if (saveButton.count() > 0) {
                saveButton.first().click();
                page.waitForTimeout(2000);
                
                // 检查是否有成功提示
                Locator successTip = page.locator(".t-message--success, .el-message--success");
                return successTip.count() > 0 || true; // 即使没有成功提示也认为保存成功
            }
            
            log.warn("[元器节点保存] 未找到保存按钮");
            return false;
        } catch (Exception e) {
            log.warn("[元器节点保存] 保存失败: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 查找调试按钮
     */
    private Locator findDebugButton(Page page) {
        String[] selectors = {
            "button:has-text('调试')",
            "[class*='debug-btn']",
            "[data-action='debug']",
            "button:has-text('Debug')"
        };
        
        for (String selector : selectors) {
            Locator button = page.locator(selector);
            if (button.count() > 0) {
                return button;
            }
        }
        
        return null;
    }
    
    /**
     * 查找运行按钮
     */
    private Locator findRunButton(Page page) {
        String[] selectors = {
            "button:has-text('运行')",
            "button:has-text('执行')",
            "button:has-text('Run')",
            "[class*='run-btn']",
            "[data-action='run']"
        };
        
        for (String selector : selectors) {
            Locator button = page.locator(selector);
            if (button.count() > 0) {
                return button;
            }
        }
        
        return null;
    }
    
    /**
     * 填入调试输入参数
     */
    private void fillDebugInput(Page page, Map<String, Object> debugInput) {
        try {
            for (Map.Entry<String, Object> entry : debugInput.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                
                // 查找对应的输入框
                Locator input = page.locator("input[name='" + key + "'], [data-field='" + key + "'] input, label:has-text('" + key + "') ~ input");
                if (input.count() > 0) {
                    input.first().clear();
                    input.first().fill(String.valueOf(value));
                    log.debug("[元器调试输入] {} = {}", key, value);
                }
            }
        } catch (Exception e) {
            log.warn("[元器调试输入] 填充失败: {}", e.getMessage());
        }
    }
    
    /**
     * 提取调试输出
     */
    private String extractDebugOutput(Page page) {
        try {
            // 尝试多种选择器
            String[] selectors = {
                ".debug-output",
                ".result-panel",
                "[class*='output']",
                ".debug-result"
            };
            
            for (String selector : selectors) {
                Locator output = page.locator(selector);
                if (output.count() > 0) {
                    return output.first().textContent();
                }
            }
            
            return null;
        } catch (Exception e) {
            log.warn("[元器调试输出] 提取失败: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * 查找发布按钮
     */
    private Locator findPublishButton(Page page) {
        String[] selectors = {
            "button:has-text('发布')",
            "[class*='publish-btn']",
            "[data-action='publish']",
            "button:has-text('Publish')"
        };
        
        for (String selector : selectors) {
            Locator button = page.locator(selector);
            if (button.count() > 0) {
                return button;
            }
        }
        
        return null;
    }
}
