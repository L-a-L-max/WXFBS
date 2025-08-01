package com.playwright.utils;

import com.microsoft.playwright.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KimiUtil {
    @Autowired
    private LogMsgUtil logInfo;

    public String waitKimiResponse(Page page, String userId, String aiName) {
        try {

            page.waitForSelector("span:has-text('复制')", new Page.WaitForSelectorOptions()
                    .setTimeout(1000000)
            );
            page.locator("span:has-text('分享')").click();
            Thread.sleep(500);
            page.locator("span:has-text('复制文本')").click();
            // 获取剪贴板内容（需要浏览器权限）
            String clipboardText = (String) page.evaluate("async () => { return await navigator.clipboard.readText(); }");
            System.out.println("获取的文本为：" + clipboardText);
            Thread.sleep(500);
            page.locator("span:has-text('生成图片')").click();

            logInfo.sendTaskLog(aiName + " 内容已提取完成", userId, aiName);

            return clipboardText;

//            //获取内容后，截图到本地
//            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("D:\\新建文件夹\\KIMI.png")));
//
//            // 定位目标 div（通过 CSS 选择器、id 或 class）
//            Locator talkImg = page.locator("div.modal-content");
//
//            // 截图该 div（自动裁剪为 div 的尺寸）
//            talkImg.screenshot(new Locator.ScreenshotOptions()
//                    .setPath(Paths.get("D:\\新建文件夹\\talkImg.png")));

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        return "获取内容失败";
    }

}
