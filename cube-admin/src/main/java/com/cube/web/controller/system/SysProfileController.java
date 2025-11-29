package com.cube.web.controller.system;

import com.cube.point.controller.PointsSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.cube.common.annotation.Log;
import com.cube.common.config.RuoYiConfig;
import com.cube.common.core.controller.BaseController;
import com.cube.common.core.domain.AjaxResult;
import com.cube.common.core.domain.entity.SysUser;
import com.cube.common.core.domain.model.LoginUser;
import com.cube.common.enums.BusinessType;
import com.cube.common.utils.SecurityUtils;
import com.cube.common.utils.StringUtils;
import com.cube.common.utils.file.FileUploadUtils;
import com.cube.common.utils.file.MimeTypeUtils;
import com.cube.common.core.redis.RedisCache;
import com.cube.framework.web.service.TokenService;
import com.cube.system.service.ISysUserService;

/**
 * 个人信息 业务处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PointsSystem pointsSystem;
    
    @Autowired(required = false)
    private RedisCache redisCache;
    
    private static final String POINTS_UPDATE_FLAG_PREFIX = "points:update:";
    
    /**
     * 从配置文件中读取上传文件访问URL
     */
    @Value("${upload.url}")
    private String uploadUrl;

    /**
     * 个人信息
     */
    @GetMapping
    public AjaxResult profile()
    {
        LoginUser loginUser = getLoginUser();
        Long userId = loginUser.getUser() != null ? loginUser.getUser().getUserId() : null;
        
        // 检查是否有积分更新标记，如果有则从数据库刷新积分
        if (userId != null && hasPointsUpdateFlag(userId.toString())) {
            SysUser latestUser = userService.selectUserById(userId);
            if (latestUser != null) {
                // 更新缓存中的用户信息（特别是积分）
                loginUser.setUser(latestUser);
                tokenService.setLoginUser(loginUser);
                // 清除积分更新标记
                clearPointsUpdateFlag(userId.toString());
            }
        }
        
        SysUser user = loginUser.getUser();
        AjaxResult ajax = AjaxResult.success(user);
        ajax.put("roleGroup", userService.selectUserRoleGroup(loginUser.getUsername()));
        ajax.put("postGroup", userService.selectUserPostGroup(loginUser.getUsername()));
        return ajax;
    }
    
    /**
     * 检查是否有积分更新标记
     */
    private boolean hasPointsUpdateFlag(String userId) {
        if (redisCache == null || userId == null) {
            return false;
        }
        try {
            String flagKey = POINTS_UPDATE_FLAG_PREFIX + userId;
            Object flag = redisCache.getCacheObject(flagKey);
            return flag != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 清除积分更新标记
     */
    private void clearPointsUpdateFlag(String userId) {
        if (redisCache == null || userId == null) {
            return;
        }
        try {
            String flagKey = POINTS_UPDATE_FLAG_PREFIX + userId;
            redisCache.deleteObject(flagKey);
        } catch (Exception e) {
            // 忽略清除失败
        }
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateProfile(@RequestBody SysUser user)
    {
        LoginUser loginUser = getLoginUser();
        SysUser currentUser = loginUser.getUser();
        currentUser.setNickName(user.getNickName());
        currentUser.setAvatar(user.getAvatar());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhonenumber(user.getPhonenumber());
        currentUser.setSex(user.getSex());
        currentUser.setCorpId(user.getCorpId());
        if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(currentUser))
        {
            return error("修改用户'" + loginUser.getUsername() + "'失败，手机号码已存在");
        }
        if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(currentUser))
        {
            return error("修改用户'" + loginUser.getUsername() + "'失败，邮箱账号已存在");
        }
        if (userService.updateUserProfile(currentUser) > 0)
        {
            // 更新缓存用户信息
            tokenService.setLoginUser(loginUser);
            return success();
        }

        return error("修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public AjaxResult updatePwd(String oldPassword, String newPassword)
    {
        LoginUser loginUser = getLoginUser();
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password))
        {
            return error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password))
        {
            return error("新密码不能与旧密码相同");
        }
        newPassword = SecurityUtils.encryptPassword(newPassword);
        if (userService.resetUserPwd(userName, newPassword) > 0)
        {
            // 更新缓存用户密码
            loginUser.getUser().setPassword(newPassword);
            tokenService.setLoginUser(loginUser);
            return success();
        }
        return error("修改密码异常，请联系管理员");
    }

    /**
     * 头像上传
     * 返回完整的URL，包含协议和域名
     */
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws Exception
    {
        if (!file.isEmpty())
        {
            LoginUser loginUser = getLoginUser();
            // 上传文件，返回相对路径（如：2025/11/05/avatar_xxx.png）
            String relativePath = FileUploadUtils.upload(RuoYiConfig.getAvatarPath(), file, MimeTypeUtils.IMAGE_EXTENSION);
            
            // 拼接完整的URL（包含协议、域名、端口）
            String fullAvatarUrl = uploadUrl + relativePath;
            
            // 数据库存储完整URL
            if (userService.updateUserAvatar(loginUser.getUsername(), fullAvatarUrl))
            {
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", fullAvatarUrl);  // 返回完整URL给前端
                
                // 更新缓存用户头像（也是完整URL）
                loginUser.getUser().setAvatar(fullAvatarUrl);
                tokenService.setLoginUser(loginUser);
                
                return ajax;
            }
        }
        return error("上传图片异常，请联系管理员");
    }
}
