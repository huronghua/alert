package com.banmatrip.alert.dao;

import com.banmatrip.alert.domain.AlertConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AlertConfigMapper {
    int deleteByPrimaryKey(Integer configId);

    int insert(AlertConfig record);

    int insertSelective(AlertConfig record);

    AlertConfig selectByPrimaryKey(Integer configId);

    int updateByPrimaryKeySelective(AlertConfig record);

    int updateByPrimaryKey(AlertConfig record);

    void deleteByDestinationId(@Param(value = "destinationId") Integer destinationId);

    List<Integer> selectNotifyConfigIdByDestinationId(@Param(value = "destinationId") Integer destinationId);

    Integer selectOverHighConfigIdLevel2(@Param(value = "destinationId") Integer destinationId);

    Integer selectOverHighConfigIdLevel3(@Param(value = "destinationId") Integer destinationId);

    Integer selectAbnormalConfigIdLevel1(@Param(value = "destinationId") Integer destinationId);

    List<AlertConfig> getConfigInfoByDestinationId(@Param(value = "destinationId") Integer destinationId);

    List<Integer> getDestinationIdList();

    List<AlertConfig> getDynamicCostAlertConfigInfoByDestinationId(@Param(value = "destinationId") Integer destinationId);

    List<AlertConfig> getOrderCostAlertConfigInfoByDestinationId(@Param(value = "destinationId") Integer destinationId);

    Integer getAlertMessageCountByUserId(@Param(value = "userId") Integer userId);
}