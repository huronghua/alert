package com.banmatrip.alert.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Eric-hu
 * @Description:
 * @create 2017-11-21 10:34
 * @Copyright: 2017 www.banmatrip.com All rights reserved.
 **/

@Data
public class AlertOrderDetail {
    List<AlertMessageDetailVO> alertMessageDetailVOList;
}