package com.cube.web.controller.system;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.cube.common.annotation.Log;
import com.cube.common.core.controller.BaseController;
import com.cube.common.core.domain.AjaxResult;
import com.cube.common.core.page.TableDataInfo;
import com.cube.common.enums.BusinessType;
import com.cube.common.utils.poi.ExcelUtil;
import com.cube.system.domain.YuanqiAgentConfig;
import com.cube.system.service.IYuanqiAgentConfigService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 腾讯元器智能体配置Controller
 *
 * @author cube
 * @date 2025-11-26
 */
@RestController
@RequestMapping("/system/yuanqi-config")
public class YuanqiAgentConfigController extends BaseController
{
    @Autowired
    private IYuanqiAgentConfigService yuanqiAgentConfigService;

    /**
     * 查询腾讯元器智能体配置列表
     */
    @GetMapping("/list")
    public TableDataInfo list(YuanqiAgentConfig yuanqiAgentConfig)
    {
        startPage();
        List<YuanqiAgentConfig> list = yuanqiAgentConfigService.selectYuanqiAgentConfigList(yuanqiAgentConfig);
        return getDataTable(list);
    }

    /**
     * 获取当前用户的启用配置
     */
    @GetMapping("/myConfig")
    public AjaxResult myConfig()
    {
        Long userId = getUserId();
        YuanqiAgentConfig config = yuanqiAgentConfigService.selectActiveConfigByUserId(userId);
        return success(config);
    }

    /**
     * 导出腾讯元器智能体配置列表
     */
    @Log(title = "腾讯元器智能体配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, YuanqiAgentConfig yuanqiAgentConfig)
    {
        List<YuanqiAgentConfig> list = yuanqiAgentConfigService.selectYuanqiAgentConfigList(yuanqiAgentConfig);
        ExcelUtil<YuanqiAgentConfig> util = new ExcelUtil<YuanqiAgentConfig>(YuanqiAgentConfig.class);
        util.exportExcel(response, list, "腾讯元器智能体配置数据");
    }

    /**
     * 获取腾讯元器智能体配置详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(yuanqiAgentConfigService.selectYuanqiAgentConfigById(id));
    }

    /**
     * 新增腾讯元器智能体配置
     */
    @Log(title = "腾讯元器智能体配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody YuanqiAgentConfig yuanqiAgentConfig)
    {
        yuanqiAgentConfig.setUserId(getUserId());
        yuanqiAgentConfig.setCreateBy(getUsername());
        return toAjax(yuanqiAgentConfigService.insertYuanqiAgentConfig(yuanqiAgentConfig));
    }

    /**
     * 修改腾讯元器智能体配置
     */
    @Log(title = "腾讯元器智能体配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody YuanqiAgentConfig yuanqiAgentConfig)
    {
        yuanqiAgentConfig.setUpdateBy(getUsername());
        return toAjax(yuanqiAgentConfigService.updateYuanqiAgentConfig(yuanqiAgentConfig));
    }

    /**
     * 删除腾讯元器智能体配置
     */
    @Log(title = "腾讯元器智能体配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(yuanqiAgentConfigService.deleteYuanqiAgentConfigByIds(ids));
    }
}
