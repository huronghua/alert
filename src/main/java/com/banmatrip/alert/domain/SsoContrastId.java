package com.banmatrip.alert.domain;

public class SsoContrastId {
    private Integer id;

    private Integer userId;

    private Integer contrastId;

    private String contrastAccount;

    private String contrastName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getContrastId() {
        return contrastId;
    }

    public void setContrastId(Integer contrastId) {
        this.contrastId = contrastId;
    }

    public String getContrastAccount() {
        return contrastAccount;
    }

    public void setContrastAccount(String contrastAccount) {
        this.contrastAccount = contrastAccount == null ? null : contrastAccount.trim();
    }

    public String getContrastName() {
        return contrastName;
    }

    public void setContrastName(String contrastName) {
        this.contrastName = contrastName == null ? null : contrastName.trim();
    }
}