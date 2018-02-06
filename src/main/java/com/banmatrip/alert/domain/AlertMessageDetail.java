package com.banmatrip.alert.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AlertMessageDetail {

    private Integer messageId;

    private Integer orderId;

    private Date alertTime;

    private Date departureDate;

    private Date returnDate;

    private BigDecimal estimateCost;

    private BigDecimal dynamicCost;

    private BigDecimal costDifference;

    private String costDifferenceRate;

    private String differenceReason;

    private String operatingStatus;

    private Date createTime;

    private Date updateTime;
}