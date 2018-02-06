package com.banmatrip.alert.interfaces;


import com.banmatrip.alert.domain.AdminAccount;
import com.banmatrip.alert.domain.AlertConfig;
import com.banmatrip.alert.domain.AlertMessageDetail;
import com.banmatrip.alert.domain.Tag;
import com.banmatrip.alert.dto.AlertMessageDetailDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface AlertService {

    List<Tag> getTagList(List<String> destinationPermissionList);

    List<AdminAccount> getContactList();

    List<Map<String,Object>> getDepListWithEcho(Integer configId);

    void alertConfig(List<AlertConfig> alertConfigList,List<Integer> contactListOverHigh,List<Integer> contactListAbnormal);

    Integer selectDynamicCostOverHighConfigId(Integer destinationId);

    Integer selectOrderCostAbnormalConfigId(Integer destinationId);

    Map<String,Object> getConfigInfoByDestinationId(Integer destinationId);

    Map<String,List<AlertConfig>> getAlertConfigInfo();

    Integer getAlertMessageCountByUserId(Integer userId);

    List<AlertMessageDetailDto> getAlertOrderDetailInfo(Map<String,Object> queryParam);

    Integer getAlertOrderDetailInfoCount(Map<String,Object> queryParam);

    List<Map<String,Object>> downloadAlertOrderDetailInfo(Map<String,Object> queryParam);

    void setMessageStatusReaded(List<Integer> readedMessageIdList);

    Integer updateAlertOrderInfo(Map<String,Object> queryParam);
}
