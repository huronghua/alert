package com.banmatrip.alert.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Department {
    private Integer id;

    private String name;

    private Integer parentId;

    private String type;

    private Integer chargeId;

    private Date createTime;

    private Date updateTime;

    private Integer createId;

    private Integer updateId;

    private int deleteFlag;

}