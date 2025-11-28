package com.cube.wechat.selfapp.app.service.helper;

import com.cube.common.core.domain.entity.SysRole;
import com.cube.system.domain.SysUserRole;
import com.cube.system.mapper.SysRoleMapper;
import com.cube.system.mapper.SysUserRoleMapper;
import com.cube.wechat.selfapp.app.domain.TemplateAuthor;
import com.cube.wechat.selfapp.app.mapper.TemplateAuthorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * 负责为首发作者授予“模板作者”身份
 */
@Component
public class TemplateAuthorIdentityManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateAuthorIdentityManager.class);
    private static final String AUTHOR_ROLE_KEY = "author";
    private static final String AUTHOR_ROLE_NAME = "模板作者";
    private static final int AUTHOR_ROLE_SORT = 90;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private TemplateAuthorMapper templateAuthorMapper;

    /**
     * 确保用户拥有模板作者角色
     */
    public void grantAuthorRole(Long userId) {
        if (userId == null) {
            return;
        }
        Long roleId = findOrCreateAuthorRoleId();
        if (roleId == null) {
            LOGGER.warn("Author role not found and failed to create, skip granting identity.");
            return;
        }
        List<Long> roleIds = sysRoleMapper.selectRoleListByUserId(userId);
        if (!CollectionUtils.isEmpty(roleIds) && roleIds.contains(roleId)) {
            ensureAuthorRecord(userId);
            return;
        }
        SysUserRole relation = new SysUserRole();
        relation.setUserId(userId);
        relation.setRoleId(roleId);
        sysUserRoleMapper.batchUserRole(Collections.singletonList(relation));
        ensureAuthorRecord(userId);
    }

    private Long findOrCreateAuthorRoleId() {
        SysRole role = sysRoleMapper.checkRoleKeyUnique(AUTHOR_ROLE_KEY);
        if (role != null) {
            return role.getRoleId();
        }
        role = new SysRole();
        role.setRoleName(AUTHOR_ROLE_NAME);
        role.setRoleKey(AUTHOR_ROLE_KEY);
        role.setRoleSort(AUTHOR_ROLE_SORT);
        role.setStatus("0");
        role.setRemark("系统自动创建的模板作者角色");
        role.setCreateBy("system");
        sysRoleMapper.insertRole(role);
        return role.getRoleId();
    }

    private void ensureAuthorRecord(Long userId) {
        TemplateAuthor exists = templateAuthorMapper.selectByUserId(userId);
        if (exists != null) {
            return;
        }
        TemplateAuthor author = new TemplateAuthor();
        author.setUserId(userId);
        author.setIncomeTotal(BigDecimal.ZERO);
        author.setReleaseTemplateCount(0);
        author.setLevel(1);
        templateAuthorMapper.insertTemplateAuthor(author);
    }
}

