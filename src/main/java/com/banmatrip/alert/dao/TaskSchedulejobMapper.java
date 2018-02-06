package com.banmatrip.alert.dao;

import com.banmatrip.alert.domain.TaskSchedulejob;

public interface TaskSchedulejobMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TaskSchedulejob record);

    int insertSelective(TaskSchedulejob record);

    TaskSchedulejob selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TaskSchedulejob record);

    int updateByPrimaryKey(TaskSchedulejob record);
}