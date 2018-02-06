package com.banmatrip.alert.dao;


import com.banmatrip.alert.domain.Department;
import com.banmatrip.alert.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    Integer getPeopleCount(List<Department> list);

    List<Map<String,Object>> selectDepUser();

    List<Integer> selectDepParId();

    List<Long> selectEchoUserId(@Param("config_id") int configId);
}