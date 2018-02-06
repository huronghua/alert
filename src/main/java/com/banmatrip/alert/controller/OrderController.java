package com.banmatrip.alert.controller;

import com.alibaba.fastjson.JSON;
import com.banmatrip.alert.constant.*;
import com.banmatrip.alert.domain.AlertTriggerConfig;
import com.banmatrip.alert.dto.AlertMessageDetailVO;
import com.banmatrip.alert.interfaces.AlertService;
import com.banmatrip.alert.interfaces.OrderService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Eric-hu
 * @Description:
 * @create 2017-11-20 9:51
 * @Copyright: 2017 www.banmatrip.com All rights reserved.
 **/

@Controller
@RequestMapping(value = "/order")
public class OrderController {

    private static final Logger log = LogManager.getLogger(OrderController.class);

    @Autowired
    OrderService orderService;

    @Autowired
    AlertService alertService;


    /**
     * 订单异常提醒配置信息入库
     * @param param
     */
    @PostMapping(value = "/saveOrderAbnormal")
    @ResponseBody
    public RestResponse saveOrderAbnormal(@RequestBody List<Map<String,Object>> param){
        try {
            log.info("订单异常提醒请求++++**时间:" + new Date());
            orderService.saveOrderAbnormalInfo(param);
            return new RestResponse(true,"消息落地成功","200",null);
        }catch (Exception e){
            log.error("消息落地失败");
            return new RestResponse(false, e.getMessage(), ReturnCode.INNER_EXCEPTION.getCode(), null);
        }
    }

    /**
     * 下载预警订单详情
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping(value = "/downloadAlertOrderDetail")
    public void downloadAlertOrderDetail(HttpServletRequest request,HttpServletResponse response) throws Exception {
        Boolean operatingStatus = Boolean.valueOf(request.getParameter("operating_status"));
        Integer userId = Integer.valueOf(request.getParameter("userId"));
        Map<String,Object> queryParam = new HashMap<>();
        queryParam.put("userId",userId);
        queryParam.put("operatingStatus",operatingStatus);
        alertService.downloadAlertOrderDetailInfo(queryParam);
        List<String> resultLine = new ArrayList();
        List<Map<String, Object>> result = alertService.downloadAlertOrderDetailInfo(queryParam);
        if(result.size()>0){
            Map<String, Object> head = result.get(0);
            resultLine = ExcelHeadAssemble.alertOrderDetailHeadAssemble(head);
        }
        /*返回结果*/
        String fileName = FileUtils.fileNameUtil("预警订单详情",".xls");//文件名
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", fileName);
        DownloadNotByTemplate.getInstance().exportObjectsToExcel(result,resultLine,false,response.getOutputStream());
        response.getOutputStream().close();
    }

    /**
     * 更新预警订单信息
     * @param request
     * @param response
     * @return
     * @throws ParseException
     */
    @PostMapping(value = "/updateAlertOrderInfo")
    @ResponseBody
    public String updateAlertOrderInfo(HttpServletRequest request,HttpServletResponse response) throws ParseException {
        try {
            Integer orderId = Integer.valueOf(request.getParameter("orderId"));
            String operatingStatus = request.getParameter("operatingStatus");
            String differenceReason = request.getParameter("differenceReason");
            String date = request.getParameter("alertTime");
            Date alertDate = null;
            /**空保护**/
            if (!StringUtils.isEmpty(date)) {
                alertDate = new Date(Long.parseLong(date));
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String alertTime = simpleDateFormat.format(alertDate);
            Map<String,Object> queryParam = new HashMap<>();
            queryParam.put("orderId",orderId);
            queryParam.put("operatingStatus",operatingStatus);
            queryParam.put("differenceReason",differenceReason);
            queryParam.put("alertTime",alertTime);
            alertService.updateAlertOrderInfo(queryParam);
            return JSON.toJSONString("success");
        }catch (Exception e){
            return JSON.toJSONString("failed");
        }

    }


}