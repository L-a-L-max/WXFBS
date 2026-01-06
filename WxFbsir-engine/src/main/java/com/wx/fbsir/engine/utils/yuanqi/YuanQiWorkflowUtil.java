package com.wx.fbsir.engine.utils.yuanqi;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 元器（YuanQi）工作流编辑工具类
 * 
 * 功能说明：
 * 1. 导航到工作流编辑页面
 * 2. 工作流操作相关功能
 * 3. 智能体管理相关功能
 * 
 * @author wxfbsir
 * @date 2025-01-06
 */
@Slf4j
@Component
public class YuanQiWorkflowUtil {
    
    /**
     * 通用的文本内容点击方法
     * 使用文本内容识别元素并点击，适用于复杂DOM结构
     * 
     * @param page Playwright页面对象
     * @param text 要点击的文本内容
     * @param description 操作描述（用于日志）
     * @param waitMillis 点击后等待时间（毫秒）
     * @return 是否点击成功
     */
    public boolean clickByText(Page page, String text, String description, int waitMillis) {
        try {
            log.debug("[元器点击] {} - 查找文本: {}", description, text);
            
            // 使用 getByText 精确匹配文本内容
            Locator element = page.getByText(text, new Page.GetByTextOptions().setExact(true));
            
            if (element.count() == 0) {
                log.warn("[元器点击] {} - 未找到文本: {}", description, text);
                return false;
            }
            
            // 等待元素可见
            element.first().waitFor(new Locator.WaitForOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE).setTimeout(5000));
            
            log.info("[元器点击] {} - 点击: {}", description, text);
            element.first().click();
            
            // 等待页面加载
            if (waitMillis > 0) {
                page.waitForTimeout(waitMillis);
            }
            
            return true;
            
        } catch (Exception e) {
            log.error("[元器点击] {} - 失败", description, e);
            return false;
        }
    }
    
    /**
     * 工作流导航结果
     */
    public static class WorkflowNavigationResult {
        private final boolean success;
        private final String message;
        private final Page newPage;
        
        public WorkflowNavigationResult(boolean success, String message, Page newPage) {
            this.success = success;
            this.message = message;
            this.newPage = newPage;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
        
        public Page getNewPage() {
            return newPage;
        }
    }
    
    /**
     * 导航到指定工作流编辑页面
     * 
     * 导航流程：
     * 1. 点击"个人空间"按钮，展开空间/团队选择弹窗
     * 2. 根据 spaceName 点击对应的空间或团队（如"个人空间"、"福帮手开源"、"U3W优立方"）
     * 3. 点击"我的智能体"或"团队智能体"展开智能体列表
     * 4. 在智能体列表中，根据 agentName 点击对应的智能体卡片
     * 5. 进入智能体详情页后，点击"工作流管理"标签页
     * 6. 在工作流列表中，根据 workflowName 找到对应的工作流
     * 7. 点击该工作流的"编辑"按钮（会打开新窗口）
     * 
     * @param page Playwright页面对象
     * @param spaceName 空间/团队名称（如"个人空间"、"福帮手开源"、"U3W优立方"）
     * @param agentName 智能体名称（如"123"、"配置简化"、"潜力眼【内测】"）
     * @param workflowName 工作流名称（如"分析助手-高优先级-多模型"）
     * @return 导航结果（包含成功状态、消息和新打开的页面对象）
     */
    public WorkflowNavigationResult navigateToWorkflowEdit(Page page, String spaceName, String agentName, String workflowName) {
        try {
            log.info("[元器工作流导航] 开始导航 - 空间: {}, 智能体: {}, 工作流: {}", spaceName, agentName, workflowName);
            
            // 步骤1：点击"个人空间"按钮，展开选择弹窗
            log.debug("[元器工作流导航] 步骤1: 点击个人空间按钮");
            Locator spaceButton = page.locator("div.spaceBtn");
            if (spaceButton.count() == 0) {
                return new WorkflowNavigationResult(false, "失败: 未找到个人空间按钮", null);
            }
            spaceButton.first().click();
            page.waitForTimeout(1000); // 等待弹窗出现
            
            // 步骤2：点击指定的空间或团队
            log.debug("[元器工作流导航] 步骤2: 选择空间/团队 - {}", spaceName);
            if (!clickByText(page, spaceName, "选择空间/团队", 1500)) {
                return new WorkflowNavigationResult(false, "失败: 未找到空间/团队 - " + spaceName, null);
            }
            
            // 步骤3：点击"我的智能体"或"团队智能体"展开列表
            log.debug("[元器工作流导航] 步骤3: 展开智能体列表");
            boolean agentListExpanded = false;
            
            // 先尝试点击"我的智能体"
            Locator myAgentMenu = page.locator(".menu-item .menu-name:has-text('我的智能体')");
            if (myAgentMenu.count() > 0) {
                log.debug("[元器工作流导航] 点击'我的智能体'展开列表");
                myAgentMenu.first().click();
                page.waitForTimeout(1000);
                agentListExpanded = true;
            } else {
                // 如果没有"我的智能体"，尝试点击"团队智能体"
                Locator teamAgentMenu = page.locator(".menu-item .menu-name:has-text('团队智能体')");
                if (teamAgentMenu.count() > 0) {
                    log.debug("[元器工作流导航] 点击'团队智能体'展开列表");
                    teamAgentMenu.first().click();
                    page.waitForTimeout(1000);
                    agentListExpanded = true;
                }
            }
            
            if (!agentListExpanded) {
                return new WorkflowNavigationResult(false, "失败: 未找到'我的智能体'或'团队智能体'菜单", null);
            }
            
            // 步骤4：点击指定的智能体
            log.debug("[元器工作流导航] 步骤4: 选择智能体 - {}", agentName);
            // 智能体名称在 .content-title 中
            Locator agentCard = page.locator(".card-item .content-title:has-text('" + agentName + "')");
            if (agentCard.count() == 0) {
                return new WorkflowNavigationResult(false, "失败: 未找到智能体 - " + agentName, null);
            }
            
            log.info("[元器工作流导航] 点击智能体: {}", agentName);
            agentCard.first().click();
            page.waitForTimeout(2000); // 等待智能体详情页加载
            
            // 步骤5：点击"工作流管理"标签页
            log.debug("[元器工作流导航] 步骤5: 点击工作流管理标签");
            if (!clickByText(page, "工作流管理", "点击工作流管理标签", 1500)) {
                return new WorkflowNavigationResult(false, "失败: 未找到工作流管理标签", null);
            }
            
            // 步骤6：在工作流列表中找到指定工作流
            log.debug("[元器工作流导航] 步骤6: 查找工作流 - {}", workflowName);
            Locator workflowRow = page.locator(".v-table-body__tr:has(.row-name:has-text('" + workflowName + "'))");
            if (workflowRow.count() == 0) {
                return new WorkflowNavigationResult(false, "失败: 未找到工作流 - " + workflowName, null);
            }
            
            // 步骤7：点击该工作流的"编辑"按钮（会打开新窗口）
            log.debug("[元器工作流导航] 步骤7: 点击编辑按钮");
            Locator editButton = workflowRow.locator("a.operate:has-text('编辑')").first();
            if (editButton.count() == 0) {
                return new WorkflowNavigationResult(false, "失败: 未找到编辑按钮", null);
            }
            
            log.info("[元器工作流导航] 点击工作流编辑按钮: {}", workflowName);
            
            // 监听新页面打开事件
            Page newPage = page.context().waitForPage(() -> {
                editButton.click();
            });
            
            // 等待新页面加载完成
            newPage.waitForLoadState();
            newPage.waitForTimeout(2000);
            
            log.info("[元器工作流导航] 新窗口已打开 - URL: {}", newPage.url());
            log.info("[元器工作流导航] 导航成功 - 当前URL: {}", newPage.url());
            return new WorkflowNavigationResult(true, "成功: 已进入工作流编辑页面 - " + workflowName, newPage);
            
        } catch (Exception e) {
            log.error("[元器工作流导航] 导航失败", e);
            return new WorkflowNavigationResult(false, "失败: " + e.getMessage(), null);
        }
    }
}
