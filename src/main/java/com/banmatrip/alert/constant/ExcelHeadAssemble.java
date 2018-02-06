package com.banmatrip.alert.constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelHeadAssemble {
    //将数据库查出来的字段转换为中文显示
    //产品总表表头
    public static List<String> alertOrderDetailHeadAssemble(Map<String, Object> map){
        List<String> resultLine = new ArrayList();
            for (String key : map.keySet()){
                if("order_id".equals(key)){
                    key = "斑马订单号";
                }
                if("alert_time".equals(key)){
                    key = "预警日期";
                }
                if("departure_date".equals(key)){
                    key = "出发日期";
                }
                if("return_date".equals(key)){
                    key = "归来日期";
                }
                if("estimate_cost".equals(key)){
                    key = "预估成本";
                }
                if("dynamic_cost".equals(key)){
                    key = "动态成本";
                }
                if("cost_difference".equals(key)){
                    key = "成本差异额";
                }
                if("cost_difference_rate".equals(key)){
                    key = "成本差异百分比";
                }
                if("difference_reason".equals(key)){
                    key = "差异原因";
                }
                if("operating_status".equals(key)){
                    key = "操作处理";
                }
                resultLine.add(key);
            }
            return resultLine;
    }
}
