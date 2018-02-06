package com.banmatrip.alert.domain;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private String account;

    private Integer id;

    private String name;

    private String password;

    private String email;

    private Integer departmentId;

    private String cellphone;

    private String employeeId;

    private String ethnic;

    private String positionId;

    private Date createTime;

    private Date updateTime;

    private Integer createId;

    private Integer updateId;

    private String roleType;

    private String deleteFlag;
}