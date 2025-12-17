package com.demo.wxchat.mapper;

import com.demo.wxchat.domain.WeWorkMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 企业微信成员Mapper
 */
@Mapper
public interface WeWorkMemberMapper {

    /**
     * 根据企微用户ID查询
     */
    WeWorkMember selectByWeWorkUserId(@Param("weWorkUserId") String weWorkUserId);

    /**
     * 插入成员
     */
    int insert(WeWorkMember member);

    /**
     * 更新成员
     */
    int update(WeWorkMember member);
}
