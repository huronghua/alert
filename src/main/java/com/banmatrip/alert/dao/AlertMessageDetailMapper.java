package com.banmatrip.alert.dao;

import com.banmatrip.alert.domain.AlertMessageDetail;
import com.banmatrip.alert.domain.AlertTriggerConfig;
import com.banmatrip.alert.dto.AlertMessageDetailDto;
import com.banmatrip.alert.dto.OrderBasicInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AlertMessageDetailMapper {
    int deleteByPrimaryKey(Integer orderId);

    int insert(AlertMessageDetail record);

    int insertSelective(AlertMessageDetail record);

    AlertMessageDetail selectByPrimaryKey(Integer orderId);

    int updateByPrimaryKeySelective(AlertMessageDetail record);

    int updateByPrimaryKey(AlertMessageDetail record);

    List<AlertTriggerConfig> getBeforeLeaveOrderInfo();

    List<AlertTriggerConfig> getAfterBackOrderInfo();

    OrderBasicInfoVO selectOrderBasicInfoById(@Param(value = "orderId")Integer orderId);

    void insertByList(@Param(value = "alertMessageDetailList") List<AlertMessageDetail> alertMessageDetailList);

    List<AlertMessageDetailDto> getAlertOrderDetailInfo(@Param(value = "queryParam")Map<String,Object> queryParam);

    List<Map<String,Object>> downloadAlertOrderDetailInfo(@Param(value = "queryParam")Map<String,Object> queryParam);

    Integer getAlertOrderDetailInfoCount(@Param(value = "queryParam")Map<String,Object> queryParam);

    Integer updateAlertOrderInfo(@Param(value = "queryParam")Map<String,Object> queryParam);
}