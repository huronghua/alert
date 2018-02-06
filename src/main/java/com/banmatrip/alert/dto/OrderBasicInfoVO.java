package com.banmatrip.alert.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderBasicInfoVO {
    private Integer orderId;

    private Date departureDate;

    private Date returnDate;

    private Integer pmId;

    private BigDecimal estimateCost;

    private BigDecimal dynamicCost;

    private BigDecimal costDifference;

    private String costDifferenceRate;

}
