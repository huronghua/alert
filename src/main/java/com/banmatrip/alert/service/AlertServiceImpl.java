package com.banmatrip.alert.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.banmatrip.alert.dao.*;
import com.banmatrip.alert.domain.AdminAccount;
import com.banmatrip.alert.domain.AlertConfig;
import com.banmatrip.alert.domain.Department;
import com.banmatrip.alert.domain.Tag;
import com.banmatrip.alert.dto.AlertMessageDetailDto;
import com.banmatrip.alert.interfaces.AlertService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eric-hu
 * @Description:
 * @create 2017-11-13 16:08
 * @Copyright: 2017 www.banmatrip.com All rights reserved.
 **/

@Service
public class AlertServiceImpl implements AlertService {

    @Autowired
    TagMapper tagMapper;

    @Autowired
    AdminAccountMapper adminAccountMapper;

    @Autowired
    DepartmentMapper departmentMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    AlertConfigMapper alertConfigMapper;

    @Autowired
    AlertNotifyMapper alertNotifyMapper;

    @Autowired
    SsoContrastIdMapper ssoContrastIdMapper;

    @Autowired
    AlertMessageDetailMapper alertMessageDetailMapper;

    @Autowired
    AlertMessageMapper alertMessageMapper;

    @Override
    public List<Tag> getTagList(List<String> destinationPermissionList) {

        return tagMapper.getTagList(destinationPermissionList);
    }

    @Override
    public List<AdminAccount> getContactList() {
        return adminAccountMapper.getContactList();
    }

    /*    @Override
        public List<Map<String, Object>> getDepListWithEcho(Integer configId) {
            List<Map<String, Object>> list = redisService.findAll();
            List<Map<String, Object>> resultlist = new ArrayList<Map<String, Object>>();
            for (Map<String, Object> map : list
                    ) {
                *//*获取当前部门的下属所有部门*//*
            List<Department> departmentList = findChildDepartment((Integer) map.get("id"));
            *//*获取当前部门信息*//*
            Department department = departmentMapper.selectByPrimaryKey((Integer) map.get("id"));
            *//*当前部门及其下属部门*//*
            departmentList.add(department);
            *//*获取本部门及其下属部门的员工人数*//*
            Integer peopleCount = userMapper.getPeopleCount(departmentList);
            map.put("onlyName", map.get("name"));
            map.put("name", (String) map.get("name") + '(' + peopleCount + ')');
            JSONObject item = JSONObject.parseObject(JSON.toJSONString(map));
            resultlist.add(item);
        }
        //List<Map<String, Object>> user = userMapper.selectDepUser();
        List<Map<String, Object>> user = redisService.selectDepUser();
        if (null != configId) {
            List<Long> userIdEcho = userMapper.selectEchoUserId(configId);
            for (Map<String, Object> temp : user) {
                Long id = (Long) temp.get("id");
                if (userIdEcho.contains(id - 10000)) {
                    temp.put("checked", "true");
                }
                resultlist.add(JSONObject.parseObject(JSON.toJSONString(temp)));
            }
        } else {
            for (Map<String, Object> temp : user) {
                resultlist.add(JSONObject.parseObject(JSON.toJSONString(temp)));
            }
        }
        return resultlist;

    }*/
    @Override
    public List<Map<String, Object>> getDepListWithEcho(Integer configId) {
        //单个部门人数
        List<Map<String, Object>> list = departmentMapper.selectDepartmentUsrCount();
        //返回部门结果list
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        //部门最大层级
        Integer maxDepartmentType = departmentMapper.selectMaxDepartmentType();

        if (maxDepartmentType != null && maxDepartmentType.intValue() > 0) {
            //按部门层级顺序遍历部门，从层级最低的开始
            for (int i = maxDepartmentType; i >= 0; i--) {
                for (Map<String, Object> departmentInfo : list
                        ) {
                    if (String.valueOf(i).equals(departmentInfo.get("type"))) {
                        Integer peopleCount = Integer.parseInt(String.valueOf(departmentInfo.get("usrCount")));
                        //遍历已有部门结果，统计当前部门人数
                        for (Map<String, Object> resultInfo : resultList) {
                            //获取当前部门的下级部门人数
                            if (String.valueOf(departmentInfo.get("id")).equals(String.valueOf(resultInfo.get("pId")))) {
                                peopleCount = peopleCount + Integer.parseInt(String.valueOf(resultInfo.get("usrCount")));
                            }
                        }
                        //更新部门人数，部门名字
                        departmentInfo.put("usrCount", peopleCount);
                        departmentInfo.put("onlyName", departmentInfo.get("name"));
                        departmentInfo.put("name", String.valueOf(departmentInfo.get("name")) + '(' + peopleCount + ')');
                        JSONObject item = JSONObject.parseObject(JSON.toJSONString(departmentInfo));
                        resultList.add(item);
                    }
                }
            }
        }
        List<Map<String, Object>> user = userMapper.selectDepUser();
        //List<Map<String, Object>> user = redisService.selectDepUser();
        if (null != configId) {
            List<Long> userIdEcho = userMapper.selectEchoUserId(configId);
            for (Map<String, Object> temp : user) {
                Long id = (Long) temp.get("id");
                if (userIdEcho.contains(id - 10000)) {
                    temp.put("checked", "true");
                }
                resultList.add(JSONObject.parseObject(JSON.toJSONString(temp)));
            }
        } else {
            for (Map<String, Object> temp : user) {
                resultList.add(JSONObject.parseObject(JSON.toJSONString(temp)));
            }
        }
        return resultList;

    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void alertConfig(List<AlertConfig> alertConfigList, List<Integer> contactListOverHigh, List<Integer> contactListAbnormal) {
        /*如果是编辑则批量删除*/
        if (CollectionUtils.isNotEmpty(alertConfigList)) {
            Integer destinationId = alertConfigList.get(0).getDestinationId();
            alertConfigMapper.deleteByDestinationId(destinationId);
            /*批量插入*/
            for (AlertConfig alertConfig : alertConfigList) {
                alertConfigMapper.insertSelective(alertConfig);
            }
        /*获取已有的需要通知的config_id*/
            List<Integer> configIdList = alertConfigMapper.selectNotifyConfigIdByDestinationId(destinationId);
            if (CollectionUtils.isNotEmpty(configIdList)) {
                alertNotifyMapper.deleteByConfigId(configIdList);
            }

        /*获取需要通知的config_id*/
            Integer overHighConfigIdLevel2 = alertConfigMapper.selectOverHighConfigIdLevel2(destinationId);
            Integer overHighConfigIdLevel3 = alertConfigMapper.selectOverHighConfigIdLevel3(destinationId);
            Integer abnormalConfigIdLevel1 = alertConfigMapper.selectAbnormalConfigIdLevel1(destinationId);
        /*批量插入通知表*/
            if (CollectionUtils.isNotEmpty(contactListOverHigh)) {
                alertNotifyMapper.insertByConfigIdAndContactList(overHighConfigIdLevel2, contactListOverHigh);
                alertNotifyMapper.insertByConfigIdAndContactList(overHighConfigIdLevel3, contactListOverHigh);
            }
            if (CollectionUtils.isNotEmpty(contactListAbnormal)) {
                alertNotifyMapper.insertByConfigIdAndContactList(abnormalConfigIdLevel1, contactListAbnormal);
            }
        }
    }

    @Override
    public Integer selectDynamicCostOverHighConfigId(Integer destinationId) {
        return alertConfigMapper.selectOverHighConfigIdLevel2(destinationId);
    }

    @Override
    public Integer selectOrderCostAbnormalConfigId(Integer destinationId) {
        return alertConfigMapper.selectAbnormalConfigIdLevel1(destinationId);
    }

    @Override
    public Map<String, Object> getConfigInfoByDestinationId(Integer destinationId) {
        Map<String, Object> configInfo = new HashMap<>();
        List<AlertConfig> basicConfigInfo = alertConfigMapper.getConfigInfoByDestinationId(destinationId);
        if (CollectionUtils.isNotEmpty(basicConfigInfo)) {
            for (AlertConfig alertConfig : basicConfigInfo) {
                String configType = alertConfig.getConfigType();
                Integer level = alertConfig.getLevel();
                if ("1".equals(configType) && 1 == level) {
                    configInfo.put("over_cost_1", alertConfig.getConfigValue());
                }
                if ("1".equals(configType) && 2 == level) {
                    configInfo.put("over_cost_2", alertConfig.getConfigValue());
                }
                if ("1".equals(configType) && 3 == level) {
                    configInfo.put("over_cost_3", alertConfig.getConfigValue());
                }
                if ("2".equals(configType) && 1 == level) {
                    configInfo.put("lower_cost_1", alertConfig.getConfigValue());
                }
                if ("4".equals(configType) && 1 == level) {
                    configInfo.put("time_point_before", alertConfig.getConfigValue());
                }
                if ("4".equals(configType) && 2 == level) {
                    configInfo.put("time_point_after", alertConfig.getConfigValue());
                }
            }
        }
        return configInfo;
    }

    @Override
    public Map<String, List<AlertConfig>> getAlertConfigInfo() {
        Map<String, List<AlertConfig>> map = new HashMap<>();
        List<AlertConfig> configList = new ArrayList<>();
        List<Integer> destinationIdList = new ArrayList<>();
        destinationIdList = alertConfigMapper.getDestinationIdList();
        if (CollectionUtils.isNotEmpty(destinationIdList)) {
            for (Integer destinationId : destinationIdList) {
                configList = alertConfigMapper.getDynamicCostAlertConfigInfoByDestinationId(destinationId);
                if (CollectionUtils.isNotEmpty(configList)) {
                    for (AlertConfig alertConfig : configList) {
                        if ("2".equals(alertConfig.getConfigType())) {
                            alertConfig.setConfigValue("-" + alertConfig.getConfigValue());
                        }
                    }
                    map.put(String.valueOf(destinationId), configList);
                }
            }
        }
        return map;
    }

    @Override
    public Integer getAlertMessageCountByUserId(Integer userId) {
        return alertConfigMapper.getAlertMessageCountByUserId(userId);

    }

    @Override
    public List<AlertMessageDetailDto> getAlertOrderDetailInfo(Map<String, Object> queryParam) {
        return alertMessageDetailMapper.getAlertOrderDetailInfo(queryParam);
    }

    @Override
    public void setMessageStatusReaded(List<Integer> readedMessageIdList) {
        alertMessageMapper.setMessageStatusReaded(readedMessageIdList);
    }

    @Override
    public List<Map<String, Object>> downloadAlertOrderDetailInfo(Map<String, Object> queryParam) {
        return alertMessageDetailMapper.downloadAlertOrderDetailInfo(queryParam);
    }

    @Override
    public Integer getAlertOrderDetailInfoCount(Map<String, Object> queryParam) {
        return alertMessageDetailMapper.getAlertOrderDetailInfoCount(queryParam);
    }

    @Override
    public Integer updateAlertOrderInfo(Map<String, Object> queryParam) {
        return alertMessageDetailMapper.updateAlertOrderInfo(queryParam);
    }

    /*获取id部门的所有下级部门*/
    public List<Department> findChildDepartment(Integer id) {
        List tree = new ArrayList();
        List<Department> list = departmentMapper.findChild(id);
        if (CollectionUtils.isNotEmpty(list)) {
            tree.addAll(list);
            for (Department department : list
                    ) {
                Integer childId = department.getId();
                List childList = findChildDepartment(childId);
                if (CollectionUtils.isNotEmpty(childList)) {
                    tree.addAll(childList);
                }
            }
        }
        return tree;
    }

}