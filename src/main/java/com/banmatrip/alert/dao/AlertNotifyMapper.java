package com.banmatrip.alert.dao;

import com.banmatrip.alert.domain.AlertNotify;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AlertNotifyMapper {
    int deleteByPrimaryKey(Integer notifyId);

    int insert(AlertNotify record);

    int insertSelective(AlertNotify record);

    AlertNotify selectByPrimaryKey(Integer notifyId);

    int updateByPrimaryKeySelective(AlertNotify record);

    int updateByPrimaryKey(AlertNotify record);

    void deleteByConfigId(@Param(value = "configIdList") List<Integer> configIdList);

    void insertByConfigIdAndContactList(@Param(value = "configId") Integer configId,@Param(value = "contactList") List<Integer> contactList);

    List<Integer> selectContactsListByConfigId(Integer configId);

    List<Integer> selectOrderAbnormalContactByDestinationId(Integer destinationId);
}