package com.banmatrip.alert.dao;

import com.banmatrip.alert.domain.AlertMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AlertMessageMapper {
    int deleteByPrimaryKey(Integer messageId);

    int insert(AlertMessage record);

    int insertSelective(AlertMessage record);

    AlertMessage selectByPrimaryKey(Integer messageId);

    int updateByPrimaryKeySelective(AlertMessage record);

    int updateByPrimaryKey(AlertMessage record);

    void insertByList(List<AlertMessage> list);

    void setMessageStatusReaded(@Param(value = "readedMessageIdList") List<Integer> readedMessageIdList);
}