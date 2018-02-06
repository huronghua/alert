package com.banmatrip.alert.dto;

import com.banmatrip.alert.domain.AlertMessageDetail;
import lombok.Data;

import java.util.List;

/**
 * @author Eric-hu
 * @Description:
 * @create 2017-11-22 15:59
 * @Copyright: 2017 www.banmatrip.com All rights reserved.
 **/
@Data
public class ResultPaginationVO {

    private Integer unHandleInfoCount;

    private Integer total;

    private List<AlertMessageDetailDto> rows;

}