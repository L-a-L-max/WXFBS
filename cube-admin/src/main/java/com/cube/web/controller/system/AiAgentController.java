package com.cube.web.controller.system;

import com.cube.common.annotation.Log;
import com.cube.common.core.controller.BaseController;
import com.cube.common.core.domain.AjaxResult;
import com.cube.common.core.domain.entity.AiAgent;
import com.cube.common.core.page.TableDataInfo;
import com.cube.common.enums.BusinessType;
import com.cube.common.utils.poi.ExcelUtil;
import com.cube.system.service.IAiAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * AI智能体Controller
 *
 * @author cube
 * @date 2025-01-14
 */
@RestController
@RequestMapping("/system/aiagent")
public class AiAgentController extends BaseController
{
    @Autowired
    private IAiAgentService aiAgentService;

    /**
     * 查询AI智能体列表
     */
    @GetMapping("/list")
    public TableDataInfo list(AiAgent aiAgent)
    {
        startPage();
        List<AiAgent> list = aiAgentService.selectAiAgentList(aiAgent);
        return getDataTable(list);
    }

    /**
     * 查询所有上架的AI智能体列表（不分页）
     */
    @GetMapping("/listActive")
    public AjaxResult listActive()
    {
        List<AiAgent> list = aiAgentService.selectActiveAgentList();
        return success(list);
    }

    /**
     * 查询用户可用的AI智能体列表
     */
    @GetMapping("/listUserAvailable")
    public AjaxResult listUserAvailable()
    {
        String userId = String.valueOf(getUserId());
        List<AiAgent> list = aiAgentService.selectUserAvailableAgents(userId);
        return success(list);
    }

    /**
     * 导出AI智能体列表
     */
    @Log(title = "AI智能体", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AiAgent aiAgent)
    {
        List<AiAgent> list = aiAgentService.selectAiAgentList(aiAgent);
        ExcelUtil<AiAgent> util = new ExcelUtil<AiAgent>(AiAgent.class);
        util.exportExcel(response, list, "AI智能体数据");
    }

    /**
     * 获取AI智能体详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(aiAgentService.selectAiAgentById(id));
    }

    /**
     * 根据智能体代码获取AI智能体详细信息
     */
    @GetMapping(value = "/code/{agentCode}")
    public AjaxResult getInfoByCode(@PathVariable("agentCode") String agentCode)
    {
        return success(aiAgentService.selectAiAgentByCode(agentCode));
    }

    /**
     * 新增AI智能体
     */
    @Log(title = "AI智能体管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AiAgent aiAgent)
    {
        if (!aiAgentService.checkAgentCodeUnique(aiAgent))
        {
            return error("新增AI智能体'" + aiAgent.getAgentName() + "'失败，智能体代码已存在");
        }
        return toAjax(aiAgentService.insertAiAgent(aiAgent));
    }

    /**
     * 修改AI智能体
     */
    @Log(title = "AI智能体管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody AiAgent aiAgent)
    {
        if (!aiAgentService.checkAgentCodeUnique(aiAgent))
        {
            return error("修改AI智能体'" + aiAgent.getAgentName() + "'失败，智能体代码已存在");
        }
        return toAjax(aiAgentService.updateAiAgent(aiAgent));
    }

    /**
     * 删除AI智能体
     */
    @Log(title = "AI智能体管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(aiAgentService.deleteAiAgentByIds(ids));
    }

    /**
     * 更新AI在线状态
     */
    @Log(title = "AI智能体状态管理", businessType = BusinessType.UPDATE)
    @PutMapping("/updateOnlineStatus")
    public AjaxResult updateOnlineStatus(@RequestBody AiAgent aiAgent)
    {
        return toAjax(aiAgentService.updateOnlineStatus(aiAgent.getAgentCode(), aiAgent.getOnlineStatus()));
    }

    /**
     * 批量更新AI在线状态
     */
    @Log(title = "AI智能体状态管理", businessType = BusinessType.UPDATE)
    @PutMapping("/batchUpdateOnlineStatus")
    public AjaxResult batchUpdateOnlineStatus(@RequestBody List<String> agentCodes, @RequestParam Integer onlineStatus)
    {
        return toAjax(aiAgentService.batchUpdateOnlineStatus(agentCodes, onlineStatus));
    }

    /**
     * 检查用户权限
     */
    @GetMapping("/checkPermission/{agentCode}")
    public AjaxResult checkPermission(@PathVariable("agentCode") String agentCode)
    {
        String userId = String.valueOf(getUserId());
        boolean hasPermission = aiAgentService.checkUserPermission(userId, agentCode);
        return success(hasPermission);
    }

    /**
     * 刷新缓存
     */
    @Log(title = "AI智能体缓存管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public AjaxResult refreshCache()
    {
        aiAgentService.refreshCache();
        return success();
    }

    /**
     * 清除缓存
     */
    @Log(title = "AI智能体缓存管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clearCache")
    public AjaxResult clearCache(@RequestParam(required = false) String agentCode)
    {
        aiAgentService.clearCache(agentCode);
        return success();
    }
}
