package com.banmatrip.alert.controller;

import com.banmatrip.alert.constant.RestResponse;
import com.banmatrip.alert.constant.ReturnCode;
import com.banmatrip.alert.domain.AlertConfig;
import com.banmatrip.alert.domain.AlertTriggerConfig;
import com.banmatrip.alert.dto.AlertMessageDetailVO;
import com.banmatrip.alert.interfaces.AlertService;
import com.banmatrip.alert.interfaces.OrderService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author Eric-hu
 * @Description:
 * @create 2018-01-19 21:12
 * @Copyright: 2018 www.banmatrip.com All rights reserved.
 **/
@Controller
@RequestMapping(value = "/rest")
public class RestController {

    private static final Logger log = LogManager.getLogger(OrderController.class);

    @Autowired
    OrderService orderService;

    @Autowired
    AlertService alertService;

    /**
     * 动态成本提醒配置信息入库
     * @param alertMessageDetailVOList
     * @return
     * @throws ParseException
     */
    @PostMapping(value = "/saveAlertOrderInfo")
    @ResponseBody
    public RestResponse saveAlertOrderInfo(@RequestBody List<AlertMessageDetailVO> alertMessageDetailVOList) throws ParseException {
        try {
            orderService.saveAlertOrderInfo(alertMessageDetailVOList);
            return new RestResponse(true,"消息落地成功","200",null);
        } catch (Exception e) {
            log.error("消息落地失败");
            return new RestResponse(false, e.getMessage(), ReturnCode.INNER_EXCEPTION.getCode(), null);
        }
    }

    @GetMapping(value = "/getAlertConfigInfo")
    @ResponseBody
    public Map<String,List<AlertConfig>> getAlertConfigInfo(){
        return alertService.getAlertConfigInfo();
    }


    /**
     * 获取出发前订单基本信息
     * @return
     */
    @GetMapping(value = "/getBeforeLeaveOrderInfo")
    @ResponseBody
    public List<AlertTriggerConfig> getBeforeLeaveOrderInfo(){
        return orderService.getBeforeLeaveOrderInfo();
    }


    /**
     * 获取出发后订单基本信息
     * @return
     */
    @GetMapping(value = "/getAfterBackOrderInfo")
    @ResponseBody
    public List<AlertTriggerConfig> getAfterBackOrderInfo(){
        return orderService.getAfterBackOrderInfo();
    }


}