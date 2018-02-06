package com.banmatrip.alert.domain;

import java.util.Date;

public class AlertNotify {
    private Integer notifyId;

    private Integer notifyUserId;

    private Integer configId;

    private Date createTime;

    private Date updateTime;

    private Integer createId;

    private Integer updateId;

    public Integer getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(Integer notifyId) {
        this.notifyId = notifyId;
    }

    public Integer getNotifyUserId() {
        return notifyUserId;
    }

    public void setNotifyUserId(Integer notifyUserId) {
        this.notifyUserId = notifyUserId;
    }

    public Integer getConfigId() {
        return configId;
    }

    public void setConfigId(Integer configId) {
        this.configId = configId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getCreateId() {
        return createId;
    }

    public void setCreateId(Integer createId) {
        this.createId = createId;
    }

    public Integer getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Integer updateId) {
        this.updateId = updateId;
    }
}