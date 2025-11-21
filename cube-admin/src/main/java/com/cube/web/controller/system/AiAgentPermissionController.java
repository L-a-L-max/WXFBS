package com.cube.web.controller.system;

import com.cube.common.annotation.Log;
import com.cube.common.core.controller.BaseController;
import com.cube.common.core.domain.AjaxResult;
import com.cube.common.core.domain.entity.AiAgentPermission;
import com.cube.common.core.page.TableDataInfo;
import com.cube.common.enums.BusinessType;
import com.cube.common.utils.poi.ExcelUtil;
import com.cube.system.service.IAiAgentPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * AI智能体权限管理Controller
 *
 * @author cube
 * @date 2025-01-14
 */
@RestController
@RequestMapping("/system/aiagent/permission")
public class AiAgentPermissionController extends BaseController
{
    @Autowired
    private IAiAgentPermissionService aiAgentPermissionService;

    /**
     * 查询AI智能体权限管理列表
     */
    @GetMapping("/list")
    public TableDataInfo list(AiAgentPermission aiAgentPermission)
    {
        startPage();
        List<AiAgentPermission> list = aiAgentPermissionService.selectAiAgentPermissionList(aiAgentPermission);
        return getDataTable(list);
    }

    /**
     * 查询用户的所有AI权限
     */
    @GetMapping("/listUserPermissions")
    public AjaxResult listUserPermissions()
    {
        String userId = String.valueOf(getUserId());
        List<AiAgentPermission> list = aiAgentPermissionService.selectUserAllPermissions(userId);
        return success(list);
    }

    /**
     * 导出AI智能体权限管理列表
     */
    @Log(title = "AI智能体权限管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AiAgentPermission aiAgentPermission)
    {
        List<AiAgentPermission> list = aiAgentPermissionService.selectAiAgentPermissionList(aiAgentPermission);
        ExcelUtil<AiAgentPermission> util = new ExcelUtil<AiAgentPermission>(AiAgentPermission.class);
        util.exportExcel(response, list, "AI智能体权限管理数据");
    }

    /**
     * 获取AI智能体权限管理详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(aiAgentPermissionService.selectAiAgentPermissionById(id));
    }

    /**
     * 检查用户权限
     */
    @GetMapping("/check/{agentId}")
    public AjaxResult checkPermission(@PathVariable("agentId") Long agentId)
    {
        String userId = String.valueOf(getUserId());
        boolean hasPermission = aiAgentPermissionService.checkUserPermission(agentId, userId);
        return success(hasPermission);
    }

    /**
     * 新增AI智能体权限管理
     */
    @Log(title = "AI智能体权限管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AiAgentPermission aiAgentPermission)
    {
        return toAjax(aiAgentPermissionService.insertAiAgentPermission(aiAgentPermission));
    }

    /**
     * 修改AI智能体权限管理
     */
    @Log(title = "AI智能体权限管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody AiAgentPermission aiAgentPermission)
    {
        return toAjax(aiAgentPermissionService.updateAiAgentPermission(aiAgentPermission));
    }

    /**
     * 删除AI智能体权限管理
     */
    @Log(title = "AI智能体权限管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(aiAgentPermissionService.deleteAiAgentPermissionByIds(ids));
    }

    /**
     * 设置用户权限
     */
    @Log(title = "AI智能体权限管理", businessType = BusinessType.UPDATE)
    @PostMapping("/setUserPermission")
    public AjaxResult setUserPermission(@RequestParam Long agentId, 
                                       @RequestParam String userId, 
                                       @RequestParam Integer permissionAction)
    {
        return toAjax(aiAgentPermissionService.setUserPermission(agentId, userId, permissionAction));
    }

    /**
     * 设置角色权限
     */
    @Log(title = "AI智能体权限管理", businessType = BusinessType.UPDATE)
    @PostMapping("/setRolePermission")
    public AjaxResult setRolePermission(@RequestParam Long agentId, 
                                       @RequestParam String roleId, 
                                       @RequestParam Integer permissionAction)
    {
        return toAjax(aiAgentPermissionService.setRolePermission(agentId, roleId, permissionAction));
    }

    /**
     * 批量设置权限
     */
    @Log(title = "AI智能体权限管理", businessType = BusinessType.UPDATE)
    @PostMapping("/batchSetPermissions")
    public AjaxResult batchSetPermissions(@RequestBody List<AiAgentPermission> permissions)
    {
        return toAjax(aiAgentPermissionService.batchSetPermissions(permissions));
    }

    /**
     * 删除智能体的所有权限
     */
    @Log(title = "AI智能体权限管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/deleteByAgentId/{agentId}")
    public AjaxResult deleteByAgentId(@PathVariable Long agentId)
    {
        return toAjax(aiAgentPermissionService.deletePermissionByAgentId(agentId));
    }

    /**
     * 刷新权限缓存
     */
    @Log(title = "AI智能体权限管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public AjaxResult refreshCache(@RequestParam(required = false) String userId)
    {
        aiAgentPermissionService.refreshPermissionCache(userId);
        return success();
    }

    /**
     * 清除权限缓存
     */
    @Log(title = "AI智能体权限管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clearCache")
    public AjaxResult clearCache(@RequestParam(required = false) String userId)
    {
        aiAgentPermissionService.clearPermissionCache(userId);
        return success();
    }
}
