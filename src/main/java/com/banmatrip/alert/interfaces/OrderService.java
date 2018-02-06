package com.banmatrip.alert.interfaces;


import com.banmatrip.alert.domain.AlertTriggerConfig;
import com.banmatrip.alert.dto.AlertMessageDetailVO;
import com.banmatrip.alert.dto.OrderBasicInfoVO;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface OrderService {

    List<AlertTriggerConfig> getBeforeLeaveOrderInfo();

    List<AlertTriggerConfig> getAfterBackOrderInfo();

    OrderBasicInfoVO selectOrderBasicInfoById(Integer orderId);

    void saveAlertOrderInfo(List<AlertMessageDetailVO> alertMessageDetailVOList) throws ParseException;

    void saveOrderAbnormalInfo(List<Map<String,Object>> param);
}
