package com.banmatrip.alert.service;

import com.banmatrip.alert.dao.*;
import com.banmatrip.alert.domain.AlertConfig;
import com.banmatrip.alert.domain.AlertMessage;
import com.banmatrip.alert.domain.AlertMessageDetail;
import com.banmatrip.alert.domain.AlertTriggerConfig;
import com.banmatrip.alert.dto.AlertMessageDetailVO;
import com.banmatrip.alert.dto.OrderBasicInfoVO;
import com.banmatrip.alert.interfaces.OrderService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Eric-hu
 * @Description:
 * @create 2017-11-17 14:33
 * @Copyright: 2017 www.banmatrip.com All rights reserved.
 **/

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    AlertMessageMapper alertMessageMapper;

    @Autowired
    AlertMessageDetailMapper alertMessageDetailMapper;

    @Autowired
    AlertConfigMapper alertConfigMapper;

    @Autowired
    AlertNotifyMapper alertNotifyMapper;

    @Autowired
    AdminAccountMapper adminAccountMapper;

    private static Logger logger= LoggerFactory.getLogger(OrderServiceImpl.class);


    @Override
    public List<AlertTriggerConfig> getBeforeLeaveOrderInfo() {
        return alertMessageDetailMapper.getBeforeLeaveOrderInfo();
    }

    @Override
    public List<AlertTriggerConfig> getAfterBackOrderInfo() {
        return alertMessageDetailMapper.getAfterBackOrderInfo();
    }

    @Override
    public OrderBasicInfoVO selectOrderBasicInfoById(Integer orderId) {
        return alertMessageDetailMapper.selectOrderBasicInfoById(orderId);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveAlertOrderInfo(List<AlertMessageDetailVO> alertMessageDetailVOList) throws ParseException {
        List<AlertMessageDetail> alertMessageDetailList = new ArrayList<>();
        List<AlertMessage> alertMessageList = new ArrayList<>();
        //找出顾大鹏的userId
        Integer bossId = adminAccountMapper.selectUserIdByName("顾大鹏");
        if (CollectionUtils.isNotEmpty(alertMessageDetailVOList)){
            for (AlertMessageDetailVO alertMessageDetailVO : alertMessageDetailVOList) {
                OrderBasicInfoVO orderBasicInfoVO = alertMessageDetailMapper.selectOrderBasicInfoById(Integer.valueOf(alertMessageDetailVO.getOrderId()));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                ParsePosition pos = new ParsePosition(0);
                Date alertTime = simpleDateFormat.parse(alertMessageDetailVO.getAlertTime(), pos);
                //通知消息入库

                Integer configId = Integer.valueOf(alertMessageDetailVO.getConfigId());
                Integer pmId = orderBasicInfoVO.getPmId();
                List<Integer> contactsList = new ArrayList<>();
                contactsList.add(pmId);
                AlertConfig alertConfig = alertConfigMapper.selectByPrimaryKey(configId);
                String configType = alertConfig.getConfigType();
                Integer level = alertConfig.getLevel();
                if ("1".equals(configType)) {
                    if (2 == level || level == 3) {
                        contactsList.addAll(alertNotifyMapper.selectContactsListByConfigId(configId));
                    }
                    if (3 == level) {
                        contactsList.add(bossId);
                    }
                }
                for (Integer userId : contactsList) {
                    AlertMessage alertMessage = new AlertMessage();
                    alertMessage.setReceiveId(userId);
                    alertMessage.setMessageType("order");
                    alertMessage.setMessageSource("schedule");
                    alertMessage.setMessageTypeId(alertMessageDetailVO.getOrderId());
                    alertMessage.setMessageStatus("1");
                    alertMessage.setWarnTime(alertTime);
                    alertMessageList.add(alertMessage);

                    AlertMessageDetail alertMessageDetail = new AlertMessageDetail();
                    alertMessageDetail.setOrderId(Integer.valueOf(alertMessageDetailVO.getOrderId()));
                    alertMessageDetail.setAlertTime(alertTime);
                    alertMessageDetail.setDynamicCost(alertMessageDetailVO.getDynamicCost());
                    alertMessageDetail.setEstimateCost(alertMessageDetailVO.getEstimateCost());
                    alertMessageDetail.setCostDifference(alertMessageDetailVO.getCostDifference());
                    alertMessageDetail.setCostDifferenceRate(alertMessageDetailVO.getCostDifferenceRate());
                    alertMessageDetail.setOperatingStatus("1");
                    alertMessageDetail.setDifferenceReason("0");
                    alertMessageDetail.setDepartureDate(orderBasicInfoVO.getDepartureDate());
                    alertMessageDetail.setReturnDate(orderBasicInfoVO.getReturnDate());
                    alertMessageDetailList.add(alertMessageDetail);
                }
            }
    }
        if(CollectionUtils.isNotEmpty(alertMessageList)) {
            alertMessageMapper.insertByList(alertMessageList);
            for (int i = 0; i < alertMessageList.size(); i++) {
                alertMessageDetailList.get(i).setMessageId(alertMessageList.get(i).getMessageId());
            }
            alertMessageDetailMapper.insertByList(alertMessageDetailList);
        }

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveOrderAbnormalInfo(List<Map<String,Object>> param) {

        List<AlertMessageDetail> alertMessageDetailList = new ArrayList<>();
        List<AlertMessage> alertMessageList = new ArrayList<>();
        for (Map<String, Object> data : param) {
            Integer orderId = Integer.valueOf(String.valueOf(data.get("order_id")));
            Integer destinationId = Integer.valueOf(String.valueOf(data.get("destination_id")));

            OrderBasicInfoVO orderBasicInfoVO = alertMessageDetailMapper.selectOrderBasicInfoById(orderId);
            List<Integer> contactList = alertNotifyMapper.selectOrderAbnormalContactByDestinationId(destinationId);
            if(orderBasicInfoVO!=null){
                Integer pmId = orderBasicInfoVO.getPmId();
                if(pmId!=null){
                    contactList.add(pmId);
                }
            }
            if(CollectionUtils.isNotEmpty(contactList)) {
                Date date = new Date();
                for (Integer userId : contactList) {
                    AlertMessage alertMessage = new AlertMessage();
                    alertMessage.setWarnTime(date);
                    alertMessage.setMessageType("order");
                    alertMessage.setMessageSource("orange");
                    alertMessage.setMessageTypeId(String.valueOf(orderId));
                    alertMessage.setMessageStatus("1");
                    alertMessage.setReceiveId(userId);
                    alertMessageList.add(alertMessage);

                    AlertMessageDetail alertMessageDetail = new AlertMessageDetail();
                    alertMessageDetail.setOrderId(orderId);
                    alertMessageDetail.setOperatingStatus("1");
                    alertMessageDetail.setDifferenceReason("0");
                    alertMessageDetail.setAlertTime(date);
                    alertMessageDetail.setDepartureDate(orderBasicInfoVO.getDepartureDate());
                    alertMessageDetail.setReturnDate(orderBasicInfoVO.getReturnDate());
                    alertMessageDetail.setDynamicCost(orderBasicInfoVO.getDynamicCost());
                    alertMessageDetail.setEstimateCost(orderBasicInfoVO.getEstimateCost());
                    alertMessageDetail.setCostDifference(orderBasicInfoVO.getCostDifference());
                    alertMessageDetail.setCostDifferenceRate(orderBasicInfoVO.getCostDifferenceRate());
                    alertMessageDetailList.add(alertMessageDetail);
                }
            }
        }
        if(CollectionUtils.isNotEmpty(alertMessageDetailList)){
            alertMessageMapper.insertByList(alertMessageList);
            for(int i = 0;i<alertMessageList.size();i++){
                alertMessageDetailList.get(i).setMessageId(alertMessageList.get(i).getMessageId());
            }
            alertMessageDetailMapper.insertByList(alertMessageDetailList);
        }
        logger.info("#############保存订单信息：" + alertMessageList.get(0).getMessageTypeId());
    }

}