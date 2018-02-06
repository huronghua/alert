package com.banmatrip.alert.controller;

import com.banmatrip.alert.domain.AlertConfig;
import com.banmatrip.alert.domain.AlertMessageDetail;
import com.banmatrip.alert.domain.AlertNotify;
import com.banmatrip.alert.dto.AlertMessageDetailDto;
import com.banmatrip.alert.dto.ResultPaginationVO;
import com.banmatrip.alert.interfaces.AlertService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.banmatrip.alert.constant.Constant.*;

/**
 * @author Eric-hu
 * @Description:
 * @create 2017-11-13 14:50
 * @Copyright: 2017 www.banmatrip.com All rights reserved.
 **/

@Controller
public class AlertController {
    @Autowired
    AlertService alertService;

    @Value("${oauthserver.getResource.function.url}")
    private String oauthServer_getResource_url;
    @Value("${oauthserver.getResource.dataFunction.url}")
    private String oauthServer_getDataFunction_url;
    @Value("${oauthserver.getResource.userinfo.url}")
    private String oauthServer_getUserInfo_url;
    @Value("${sso.tosignout.url}")
    String ssoTosignoutUrl;

    /**
     * 获取目的地列表
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/alertConfig")
    public String destinationAlertConfig(HttpServletRequest request,ModelMap modelMap) {
        Map<String,Object> dataFunction = (Map<String,Object>)request.getSession().getAttribute("dataFunction");
        List<String> destinationList = new ArrayList<>();
        if(!(((List<String>) dataFunction.get("destinationType")).get(0).equals("allPermissions"))){
            destinationList = (List<String>) dataFunction.get("destinationType");
        }
        modelMap.put("tagList", alertService.getTagList(destinationList));
        return "alertConfig";
    }

    /**
     * 预警订单详情页接口
     * @param userId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/alertMessage/{userId}")
    public String alertOrderDetailPage(@PathVariable(value = "userId") Integer userId,ModelMap modelMap) {
        modelMap.put("userId",userId);
        return "alertOrderDetail";
    }

    /**
     *获取预警订单详情
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getAlertMessageDetail")
    @ResponseBody
    public ResultPaginationVO alertOrderDetail(HttpServletRequest request,HttpServletResponse response) {

        Integer userId = Integer.valueOf(request.getParameter("userId"));
        Boolean operatingStatus = Boolean.valueOf(request.getParameter("operatingStatus"));
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");
        Integer page = Integer.valueOf(request.getParameter("page"));
        Integer pagesize = Integer.valueOf(request.getParameter("rows"));
        Integer startindex = (page-1)*pagesize;
        Map<String,Object> queryParam = new HashMap<>();
        queryParam.put("userId",userId);
        queryParam.put("operatingStatus",operatingStatus);
        queryParam.put("sort",sort);
        queryParam.put("order",order);
        queryParam.put("startindex",startindex);
        queryParam.put("pagesize",pagesize);
        ResultPaginationVO resultPaginationVO = new ResultPaginationVO();
        /*获取所有预警订单消息详情*/
        List<AlertMessageDetailDto> resultList = alertService.getAlertOrderDetailInfo(queryParam);
        resultPaginationVO.setRows(resultList);
        /*获取待处理消息数*/
        resultPaginationVO.setUnHandleInfoCount(alertService.getAlertMessageCountByUserId(userId));
        resultPaginationVO.setTotal(alertService.getAlertOrderDetailInfoCount(queryParam));
        /*获取未处理消息数*/
        /*对新消息做已读状态处理*/
        if(CollectionUtils.isNotEmpty(resultList)){
            List<Integer> readedMessageIdList = new ArrayList<>();
            for(AlertMessageDetailDto alertMessageDetailDto : resultList){
                readedMessageIdList.add(alertMessageDetailDto.getMessageId());
            }
            alertService.setMessageStatusReaded(readedMessageIdList);
        }
        return resultPaginationVO;
    }

    @RequestMapping(value = "/home")
    public String home() {
        return "index";
    }

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }


    /**
     * 登出
     *
     * @return
     */
    @RequestMapping("/tosignout")
    public String tosso(ModelMap modelMap, HttpServletRequest request,HttpServletResponse response) throws IOException {
        /*清空session以完全退出*/
        request.getSession().invalidate();
        Cookie[] cookies =  request.getCookies();
        for(Cookie cookie : cookies){
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        modelMap.put("ssoTosignoutUrl",ssoTosignoutUrl);
        return "tosignout";
    }

    /**
     * 初始化联系人树
     * @param configId 配置Id
     * @return
     */
    @RequestMapping(value = "/initContactsTree/{configId}")
    @ResponseBody
    public List<Map<String, Object>> initContactsTree(@RequestParam(value = "configId", required = false) Integer configId) {
        return alertService.getDepListWithEcho(configId);
    }

    /**
     *获取指定指定通知人的配置信息
     * @param destinationId 目的地Id
     * @return
     */
    @RequestMapping(value = "/getConfigInfo/{destinationId}")
    @ResponseBody
    public Map<String, Object> getConfigInfo(@PathVariable(value = "destinationId") Integer destinationId) {
        Map<String, Object> configInfo = new HashMap<>();
        configInfo.put("basicInfo", alertService.getConfigInfoByDestinationId(destinationId));
        Integer overHighConfigId = alertService.selectDynamicCostOverHighConfigId(destinationId);
        configInfo.put("overHighContacts", alertService.getDepListWithEcho(overHighConfigId));
        Integer abnormalConfigId = alertService.selectOrderCostAbnormalConfigId(destinationId);
        configInfo.put("abnormalContacts", alertService.getDepListWithEcho(abnormalConfigId));
        return configInfo;
    }

    /**
     * 保存预警配置信息
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/alertConfigSave", method = RequestMethod.POST)
    public void alertConfigSave(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<AlertConfig> alertConfigList = alertConfigFactory(request);

        String contactOverHigh = request.getParameter("contactListOverHigh");
        String contactAbnormal = request.getParameter("contactListAbnormal");
        List<String> contactListOverHighList = Arrays.asList(contactOverHigh.split(","));
        List<Integer> contactListOverHigh = new ArrayList<>();
        List<Integer> contactListAbnormal = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(contactListOverHighList)&&!"".equals(contactListOverHighList.get(0))) {
            for (String contact : contactListOverHighList) {
                contactListOverHigh.add(Integer.valueOf(contact) - 10000);
            }
        }
        List<String> contactListAbnormalList = Arrays.asList(contactAbnormal.split(","));
        if (CollectionUtils.isNotEmpty(contactListAbnormalList)&&!"".equals(contactListAbnormalList.get(0))) {
            for (String contact : contactListAbnormalList) {
                contactListAbnormal.add(Integer.valueOf(contact) - 10000);
            }
        }
        alertService.alertConfig(alertConfigList, contactListOverHigh, contactListAbnormal);

        response.sendRedirect("/alertConfig");
    }


    public List<AlertConfig> alertConfigFactory(HttpServletRequest request) {
        List<AlertConfig> alertConfigList = new ArrayList<>();
        String destinationId = request.getParameter("destination_id");
        String overCost1 = request.getParameter("over_cost_1");
        String overCost2 = request.getParameter("over_cost_2");
        String overCost3 = request.getParameter("over_cost_3");
        String lowerCost1 = request.getParameter("lower_cost_1");
        String timePointBefore = request.getParameter("time_point_before");
        String timePointAfter = request.getParameter("time_point_after");
        AlertConfig alertConfig1 = new AlertConfig();
        alertConfig1.setDestinationId(Integer.valueOf(destinationId));
        alertConfig1.setConfigName("动态成本过高");
        alertConfig1.setLevel(1);
        alertConfig1.setConfigType(DYNAMIC_COST_HIGH);
        alertConfig1.setConfigValue(overCost1);
        alertConfigList.add(alertConfig1);

        AlertConfig alertConfig2 = new AlertConfig();
        alertConfig2.setDestinationId(Integer.valueOf(destinationId));
        alertConfig2.setConfigName("动态成本过高");
        alertConfig2.setLevel(2);
        alertConfig2.setConfigType(DYNAMIC_COST_HIGH);
        alertConfig2.setConfigValue(overCost2);
        alertConfigList.add(alertConfig2);

        AlertConfig alertConfig3 = new AlertConfig();
        alertConfig3.setDestinationId(Integer.valueOf(destinationId));
        alertConfig3.setConfigName("动态成本过高");
        alertConfig3.setLevel(3);
        alertConfig3.setConfigType(DYNAMIC_COST_HIGH);
        alertConfig3.setConfigValue(overCost3);
        alertConfigList.add(alertConfig3);

        AlertConfig alertConfig4 = new AlertConfig();
        alertConfig4.setDestinationId(Integer.valueOf(destinationId));
        alertConfig4.setConfigName("动态成本过低");
        alertConfig4.setLevel(1);
        alertConfig4.setConfigType(DYNAMIC_COST_LOW);
        alertConfig4.setConfigValue(lowerCost1);
        alertConfigList.add(alertConfig4);

        AlertConfig alertConfig5 = new AlertConfig();
        alertConfig5.setDestinationId(Integer.valueOf(destinationId));
        alertConfig5.setConfigName("订单成本异常");
        alertConfig5.setLevel(1);
        alertConfig5.setConfigType(ORDER_COST_ABNORMAL);
        alertConfigList.add(alertConfig5);

        AlertConfig alertConfig6 = new AlertConfig();
        alertConfig6.setDestinationId(Integer.valueOf(destinationId));
        alertConfig6.setConfigName("预警时间节点");
        alertConfig6.setLevel(1);
        alertConfig6.setConfigType(TIME_POINT);
        alertConfig6.setConfigValue(timePointBefore);
        alertConfigList.add(alertConfig6);

        AlertConfig alertConfig7 = new AlertConfig();
        alertConfig7.setDestinationId(Integer.valueOf(destinationId));
        alertConfig7.setConfigName("预警时间节点");
        alertConfig7.setLevel(2);
        alertConfig7.setConfigType(TIME_POINT);
        alertConfig7.setConfigValue(timePointAfter);
        alertConfigList.add(alertConfig7);
        return alertConfigList;
    }


    /**
     * 获取该用户下的预警消息数
     * @param userId
     * @return
     */
    @GetMapping(value = "/alertMessageCount/{userId}")
    @ResponseBody
    public Integer getAlertMessageCount(@PathVariable(value = "userId") Integer userId){
       return alertService.getAlertMessageCountByUserId(userId);
    }
}