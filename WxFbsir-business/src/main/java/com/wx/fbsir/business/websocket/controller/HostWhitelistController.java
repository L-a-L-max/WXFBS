package com.wx.fbsir.business.websocket.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wx.fbsir.common.annotation.Log;
import com.wx.fbsir.common.core.controller.BaseController;
import com.wx.fbsir.common.core.domain.AjaxResult;
import com.wx.fbsir.common.enums.BusinessType;
import com.wx.fbsir.business.websocket.domain.WsHostWhitelist;
import com.wx.fbsir.business.websocket.mapper.WsHostWhitelistMapper;
import com.wx.fbsir.common.core.page.TableDataInfo;

/**
 * 主机ID白名单Controller
 * 
 * @author wxfbsir
 * @date 2025-12-23
 */
@RestController
@RequestMapping("/business/host/whitelist")
public class HostWhitelistController extends BaseController
{
    @Autowired
    private WsHostWhitelistMapper wsHostWhitelistMapper;

    @PreAuthorize("@ss.hasPermi('business:host:whitelist:query')")
    @GetMapping("/list")
    public TableDataInfo list(WsHostWhitelist wsHostWhitelist)
    {
        startPage();
        List<WsHostWhitelist> list = wsHostWhitelistMapper.selectList(wsHostWhitelist);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('business:host:whitelist:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wsHostWhitelistMapper.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('business:host:whitelist:add')")
    @Log(title = "主机ID白名单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WsHostWhitelist wsHostWhitelist)
    {
        wsHostWhitelist.setCreateBy(getUsername());
        return toAjax(wsHostWhitelistMapper.insert(wsHostWhitelist));
    }

    @PreAuthorize("@ss.hasPermi('business:host:whitelist:edit')")
    @Log(title = "主机ID白名单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WsHostWhitelist wsHostWhitelist)
    {
        wsHostWhitelist.setUpdateBy(getUsername());
        return toAjax(wsHostWhitelistMapper.update(wsHostWhitelist));
    }

    @PreAuthorize("@ss.hasPermi('business:host:whitelist:remove')")
    @Log(title = "主机ID白名单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        if (ids == null || ids.length == 0) {
            return error("请选择要删除的数据");
        }
        
        int count = wsHostWhitelistMapper.deleteByIds(ids);
        return count > 0 ? success("删除成功，共删除 " + count + " 条记录") : error("删除失败，未找到相关记录");
    }
}
