package com.banmatrip.alert.dao;

import com.banmatrip.alert.domain.SsoContrastId;
import org.apache.ibatis.annotations.Param;

public interface SsoContrastIdMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SsoContrastId record);

    int insertSelective(SsoContrastId record);

    SsoContrastId selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SsoContrastId record);

    int updateByPrimaryKey(SsoContrastId record);

    Integer transformIdToOrangeUserId(@Param(value = "userId") Integer userId);
}