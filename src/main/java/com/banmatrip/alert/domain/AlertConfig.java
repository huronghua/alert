package com.banmatrip.alert.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AlertConfig {
    private Integer configId;

    private Integer destinationId;

    private String configName;

    private String configValue;

    private String configType;

    private Date createTime;

    private Date updateTime;

    private Integer createId;

    private Integer updateId;

    private Integer level;
}