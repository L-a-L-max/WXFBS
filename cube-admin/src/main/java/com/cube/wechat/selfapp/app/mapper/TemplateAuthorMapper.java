package com.cube.wechat.selfapp.app.mapper;

import com.cube.wechat.selfapp.app.domain.TemplateAuthor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TemplateAuthorMapper {

    TemplateAuthor selectByUserId(@Param("userId") Long userId);

    int insertTemplateAuthor(TemplateAuthor author);

    int increaseReleaseTemplateCount(@Param("userId") Long userId, @Param("delta") int delta);
}

