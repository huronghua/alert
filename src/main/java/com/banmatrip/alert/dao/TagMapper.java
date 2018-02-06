package com.banmatrip.alert.dao;


import com.banmatrip.alert.domain.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TagMapper {
    List<Tag> getTagList(@Param("destinationPermissionList") List<String> destinationPermissionList);
}