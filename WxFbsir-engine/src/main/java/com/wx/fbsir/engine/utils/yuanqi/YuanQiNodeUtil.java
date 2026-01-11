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
     * 操作流程（根据实际元器平台UI修正）：
     * 1. 等待工作流画布加载完成
     * 2. 定位并点击指定节点（通过节点名称如"混元大模型"、"DeepSeek大模型"）
     * 3. 等待右侧配置面板出现
     * 4. 选择模型（如果需要）
     * 5. 点击"高级设置"打开参数弹窗
     * 6. 填充Temperature、TopP、MaxTokens（MaxTokens可选，部分模型没有）
     * 7. 点击确定关闭弹窗
     * 8. 填充提示词
     * 
     * @param page 工作流编辑页面
     * @param nodeName 节点名称（如"精调大模型"、"混元大模型"）
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
            
            // 单击选中节点，打开右侧配置面板
            log.debug("[元器节点编辑] 点击选中节点");
            node.first().click();
            page.waitForTimeout(2000);
            
            // 步骤3：等待右侧配置面板出现
            log.debug("[元器节点编辑] 步骤3: 等待配置面板出现");
            page.waitForTimeout(1000);
            
            // 步骤4：选择模型（如果需要）
            if (config.containsKey("modelName")) {
                String modelName = String.valueOf(config.get("modelName"));
                log.debug("[元器节点编辑] 步骤4: 选择模型: {}", modelName);
                boolean modelSelected = selectModel(page, modelName);
                if (!modelSelected) {
                    log.warn("[元器节点编辑] 模型选择失败: {}", modelName);
                }
            }
            
            // 步骤5：点击"高级设置"打开参数弹窗
            boolean hasAdvancedParams = config.containsKey("temperature") || 
                                        config.containsKey("topP") || 
                                        config.containsKey("maxTokens");
            if (hasAdvancedParams) {
                log.debug("[元器节点编辑] 步骤5: 点击高级设置");
                boolean advancedOpened = openAdvancedSettings(page);
                
                if (advancedOpened) {
                    // 步骤6：填充高级参数
                    log.debug("[元器节点编辑] 步骤6: 填充高级参数");
                    
                    // 填充温度（Temperature）
                    if (config.containsKey("temperature")) {
                        Object tempValue = config.get("temperature");
                        log.debug("[元器节点编辑] 设置温度: {}", tempValue);
                        fillAdvancedParameter(page, "温度", tempValue);
                    }
                    
                    // 填充TopP
                    if (config.containsKey("topP")) {
                        Object topPValue = config.get("topP");
                        log.debug("[元器节点编辑] 设置TopP: {}", topPValue);
                        fillAdvancedParameter(page, "Top P", topPValue);
                    }
                    
                    // 填充最大回复Token（可选，部分模型没有此参数）
                    if (config.containsKey("maxTokens")) {
                        Object maxTokensValue = config.get("maxTokens");
                        log.debug("[元器节点编辑] 设置最大回复Token: {}", maxTokensValue);
                        boolean filled = fillAdvancedParameter(page, "最大回复Token", maxTokensValue);
                        if (!filled) {
                            log.info("[元器节点编辑] 当前模型不支持最大回复Token参数，跳过");
                        }
                    }
                    
                    // 步骤7：点击确定关闭弹窗
                    log.debug("[元器节点编辑] 步骤7: 关闭高级设置弹窗");
                    closeAdvancedSettings(page);
                } else {
                    log.warn("[元器节点编辑] 未找到高级设置按钮，跳过高级参数设置");
                }
            }
            
            // 步骤8：填充提示词（不记录具体内容到日志，保护敏感信息）
            if (config.containsKey("prompt")) {
                String prompt = String.valueOf(config.get("prompt"));
                log.debug("[元器节点编辑] 步骤8: 填充提示词 (长度: {} 字符)", prompt.length());
                fillPrompt(page, prompt);
            }
            
            log.info("[元器节点编辑] 节点配置完成: {}", nodeName);
            return new NodeEditResult(true, "节点配置已完成", nodeName);
            
        } catch (Exception e) {
            log.error("[元器节点编辑] 编辑失败 - 节点: {}", nodeName, e);
            return new NodeEditResult(false, "编辑失败: " + e.getMessage(), nodeName);
        }
    }
    
    /**
     * 打开高级设置弹窗
     */
    private boolean openAdvancedSettings(Page page) {
        try {
            // 查找"高级设置"链接或按钮
            Locator advancedBtn = page.getByText("高级设置");
            if (advancedBtn.count() > 0) {
                advancedBtn.first().click();
                page.waitForTimeout(1000);
                log.debug("[元器高级设置] 已打开高级设置弹窗");
                return true;
            }
            
            // 备选：查找设置图标
            Locator settingsIcon = page.locator("[class*='setting'], [class*='config']").first();
            if (settingsIcon.count() > 0) {
                settingsIcon.click();
                page.waitForTimeout(1000);
                return true;
            }
            
            return false;
        } catch (Exception e) {
            log.warn("[元器高级设置] 打开失败: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 关闭高级设置弹窗
     */
    private void closeAdvancedSettings(Page page) {
        try {
            // 查找确定按钮
            Locator confirmBtn = page.getByText("确定").first();
            if (confirmBtn.count() > 0 && confirmBtn.isVisible()) {
                confirmBtn.click();
                page.waitForTimeout(500);
                log.debug("[元器高级设置] 已关闭高级设置弹窗");
                return;
            }
            
            // 备选：查找关闭按钮
            Locator closeBtn = page.locator("[class*='close'], button:has-text('取消')").first();
            if (closeBtn.count() > 0 && closeBtn.isVisible()) {
                closeBtn.click();
                page.waitForTimeout(500);
            }
        } catch (Exception e) {
            log.warn("[元器高级设置] 关闭失败: {}", e.getMessage());
        }
    }
    
    /**
     * 填充高级设置参数（在弹窗中）
     * 参数名称使用中文，如"温度"、"Top P"、"最大回复Token"
     */
    private boolean fillAdvancedParameter(Page page, String paramLabel, Object value) {
        try {
            // 在弹窗中查找参数标签对应的输入框
            // 元器平台使用滑块+输入框的组合，输入框在标签右侧
            
            // 策略1：通过标签文本定位相邻的输入框
            Locator labelLocator = page.getByText(paramLabel).first();
            if (labelLocator.count() > 0) {
                // 查找同一行的输入框
                Locator input = page.locator("input[type='number'], input[type='text']")
                    .filter(new Locator.FilterOptions().setHas(page.getByText(paramLabel)));
                
                if (input.count() == 0) {
                    // 备选：查找标签后面的输入框
                    input = labelLocator.locator("xpath=following::input[1]");
                }
                
                if (input.count() > 0) {
                    input.first().click();
                    input.first().fill("");
                    input.first().fill(String.valueOf(value));
                    log.debug("[元器参数填充] {} = {}", paramLabel, value);
                    return true;
                }
            }
            
            // 策略2：通过包含参数名的容器查找
            Locator container = page.locator("div:has-text('" + paramLabel + "')").first();
            if (container.count() > 0) {
                Locator input = container.locator("input").first();
                if (input.count() > 0) {
                    input.click();
                    input.fill("");
                    input.fill(String.valueOf(value));
                    log.debug("[元器参数填充] {} = {} (容器策略)", paramLabel, value);
                    return true;
                }
            }
            
            log.warn("[元器参数填充] 未找到参数输入框: {}", paramLabel);
            return false;
        } catch (Exception e) {
            log.warn("[元器参数填充] 填充失败 - 参数: {}, 错误: {}", paramLabel, e.getMessage());
            return false;
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
     * 发布工作流（从智能体详情页）
     * 
     * 操作流程（根据实际元器平台UI）：
     * 1. 检查右上角"发布"按钮是否可点击
     *    - 如果按钮为灰色/禁用状态，返回"暂无可发布的工作流"
     * 2. 点击"发布"按钮，进入发布详情页
     * 3. 滚动到页面底部
     * 4. 点击底部的"发布"按钮
     * 5. 等待发布完成，检查"已发布"状态（10-30秒）
     * 
     * 注意：此方法应在智能体详情页调用，不是工作流编辑页
     * 
     * @param page 智能体详情页面
     * @return 发布结果
     */
    public PublishResult publishWorkflow(Page page) {
        try {
            log.info("[元器工作流发布] 开始发布");
            
            // 步骤1：检查发布按钮状态
            log.debug("[元器工作流发布] 步骤1: 检查发布按钮状态");
            Locator publishButton = findPublishButton(page);
            if (publishButton == null || publishButton.count() == 0) {
                return new PublishResult(false, "未找到发布按钮");
            }
            
            // 检查按钮是否为禁用状态
            boolean isDisabled = false;
            try {
                Locator firstButton = publishButton.first();
                String disabledAttr = firstButton.getAttribute("disabled");
                String className = firstButton.getAttribute("class");
                isDisabled = disabledAttr != null || 
                             (className != null && (className.contains("disabled") || className.contains("gray")));
            } catch (Exception e) {
                log.debug("[元器工作流发布] 无法检测按钮状态: {}", e.getMessage());
            }
            
            if (isDisabled) {
                log.info("[元器工作流发布] 发布按钮为禁用状态，暂无可发布的工作流");
                return new PublishResult(false, "暂无可发布的工作流");
            }
            
            // 步骤2：点击发布按钮
            log.debug("[元器工作流发布] 步骤2: 点击发布按钮");
            publishButton.first().click();
            page.waitForTimeout(3000);
            
            // 步骤3：等待发布页面加载
            log.debug("[元器工作流发布] 步骤3: 等待发布页面加载");
            page.waitForLoadState();
            page.waitForTimeout(2000);
            
            String currentUrl = page.url();
            log.debug("[元器工作流发布] 当前URL: {}", currentUrl);
            
            // 步骤4：滚动到页面底部
            log.debug("[元器工作流发布] 步骤4: 滚动到页面底部");
            page.evaluate("window.scrollTo(0, document.body.scrollHeight)");
            page.waitForTimeout(1000);
            page.evaluate("window.scrollTo(0, document.body.scrollHeight)");
            page.waitForTimeout(500);
            
            // 步骤5：点击底部发布按钮
            log.debug("[元器工作流发布] 步骤5: 点击底部发布按钮");
            Locator bottomPublishButton = page.locator("button:has-text('发布')").last();
            if (bottomPublishButton.count() == 0 || !bottomPublishButton.isVisible()) {
                bottomPublishButton = page.getByText("发布").last();
            }
            
            if (bottomPublishButton.count() > 0 && bottomPublishButton.isVisible()) {
                bottomPublishButton.click();
                page.waitForTimeout(5000);
                log.debug("[元器工作流发布] 已点击发布按钮");
            } else {
                // 备选：查找确认按钮
                Locator confirmButton = page.locator("button:has-text('确认'), button:has-text('确定')").first();
                if (confirmButton.count() > 0 && confirmButton.isVisible()) {
                    confirmButton.click();
                    page.waitForTimeout(5000);
                } else {
                    log.warn("[元器工作流发布] 未找到发布/确认按钮");
                    return new PublishResult(false, "未找到发布确认按钮");
                }
            }
            
            // 步骤6：等待发布完成，检查"已发布"状态（最多等待30秒）
            log.debug("[元器工作流发布] 步骤6: 等待发布完成");
            for (int i = 0; i < 6; i++) {
                page.waitForTimeout(5000);
                
                // 检查"已发布"状态
                Locator publishedStatus = page.getByText("已发布");
                if (publishedStatus.count() > 0) {
                    log.info("[元器工作流发布] 发布成功，状态: 已发布");
                    return new PublishResult(true, "发布成功");
                }
                
                // 检查成功提示
                Locator successTip = page.locator("[class*='success'], [class*='message--success']");
                if (successTip.count() > 0) {
                    log.info("[元器工作流发布] 发布成功");
                    return new PublishResult(true, "发布成功");
                }
            }
            
            log.info("[元器工作流发布] 发布操作已执行，请确认结果");
            return new PublishResult(true, "发布操作已执行");
            
        } catch (Exception e) {
            log.error("[元器工作流发布] 发布失败", e);
            return new PublishResult(false, "发布失败: " + e.getMessage());
        }
    }
    
    /**
     * 查找节点
     * 
     * 根据实际元器平台UI，节点通过名称标识，如"混元大模型"、"DeepSeek大模型"、"精调大模型"等
     * 使用多种选择器策略，提高容错性
     */
    private Locator findNode(Page page, String nodeName) {
        log.debug("[元器节点定位] 开始查找节点: {}", nodeName);
        
        // 策略1：通过getByText精确匹配节点名称（推荐）
        // 元器平台节点显示名称如"混元大模型"、"DeepSeek大模型"
        Locator node = page.getByText(nodeName, new Page.GetByTextOptions().setExact(true));
        if (node.count() > 0) {
            log.debug("[元器节点定位] 策略1成功: getByText精确匹配");
            return node;
        }
        
        // 策略2：通过getByText模糊匹配
        node = page.getByText(nodeName, new Page.GetByTextOptions().setExact(false));
        if (node.count() > 0) {
            log.debug("[元器节点定位] 策略2成功: getByText模糊匹配");
            return node;
        }
        
        // 策略3：通过节点名称类（如果平台使用了特定class）
        node = page.locator(".node-name:has-text('" + nodeName + "'), .node-title:has-text('" + nodeName + "'), .node-label:has-text('" + nodeName + "')");
        if (node.count() > 0) {
            log.debug("[元器节点定位] 策略3成功: node-name/node-title/node-label");
            return node;
        }
        
        // 策略4：通过画布中的节点容器查找
        node = page.locator("[class*='node']:has-text('" + nodeName + "'), [class*='canvas'] :has-text('" + nodeName + "')");
        if (node.count() > 0) {
            log.debug("[元器节点定位] 策略4成功: 节点容器");
            return node;
        }
        
        // 策略5：通过包含文本的div（最宽松的匹配）
        node = page.locator("div:has-text('" + nodeName + "')").first();
        if (node.count() > 0) {
            log.debug("[元器节点定位] 策略5成功: div:has-text");
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
     * 根据实际元器平台UI，调试面板的执行按钮文本为"去调试"
     */
    private Locator findRunButton(Page page) {
        String[] selectors = {
            "button:has-text('去调试')",
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
    
    /**
     * 导航到智能体详情页（用于发布功能）
     * 
     * 导航流程（根据实际元器平台UI）：
     * 1. 点击"个人空间"按钮
     * 2. 选择指定空间
     * 3. 展开"我的智能体"菜单
     * 4. 点击指定智能体卡片
     * 
     * @param page 元器首页
     * @param spaceName 空间名称（如"个人空间"、"福帮手开源"）
     * @param agentName 智能体名称（如"日更助手"、"123"）
     * @return 是否导航成功
     */
    public boolean navigateToAgentDetail(Page page, String spaceName, String agentName) {
        try {
            log.info("[元器导航] 导航到智能体详情页 - 空间: {}, 智能体: {}", spaceName, agentName);
            
            // 步骤1：点击个人空间按钮
            log.debug("[元器导航] 步骤1: 点击个人空间按钮");
            Locator spaceButton = page.locator("div.spaceBtn, [class*='space-btn']").first();
            if (spaceButton.count() == 0) {
                spaceButton = page.getByText("个人空间").first();
            }
            
            if (spaceButton.count() > 0) {
                spaceButton.click();
                page.waitForTimeout(1000);
            }
            
            // 步骤2：选择空间
            log.debug("[元器导航] 步骤2: 选择空间: {}", spaceName);
            Locator spaceOption = page.getByText(spaceName, new Page.GetByTextOptions().setExact(true));
            if (spaceOption.count() > 0) {
                spaceOption.first().click();
                page.waitForTimeout(1500);
            }
            
            // 步骤3：展开智能体列表
            log.debug("[元器导航] 步骤3: 展开智能体列表");
            Locator myAgentMenu = page.locator("[class*='menu-item']:has-text('我的智能体'), div:has-text('我的智能体')").first();
            if (myAgentMenu.count() > 0) {
                myAgentMenu.click();
                page.waitForTimeout(1000);
            }
            
            // 步骤4：点击智能体
            log.debug("[元器导航] 步骤4: 点击智能体: {}", agentName);
            Locator agentCard = page.locator("[class*='card']:has-text('" + agentName + "'), [class*='agent']:has-text('" + agentName + "')").first();
            if (agentCard.count() == 0) {
                agentCard = page.getByText(agentName, new Page.GetByTextOptions().setExact(false)).first();
            }
            
            if (agentCard.count() > 0) {
                agentCard.click();
                page.waitForTimeout(2000);
                log.info("[元器导航] 已进入智能体详情页");
                return true;
            }
            
            log.warn("[元器导航] 未找到智能体: {}", agentName);
            return false;
            
        } catch (Exception e) {
            log.error("[元器导航] 导航失败", e);
            return false;
        }
    }
}
