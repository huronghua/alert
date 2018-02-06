package com.banmatrip.alert.constant;

public enum ReturnCode {

    NOT_EXIST("E0001", "结果不存在"),

    PARAM_INVALID("E0002", "参数校验不通过"),

    UNKNOWN_REPORT_FORM_TYPE("E0003", "无法识别的表单类型"),

    INNER_EXCEPTION("E0004", "内部异常"),

    CONFIG_ERROR("E0005", "配置错误");

    private final String code;

    private final String message;

    ReturnCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ReturnCode getByCode(String code) {
        for (ReturnCode resultCode : ReturnCode.values()) {
            if (resultCode.getCode().equals(code)) {
                return resultCode;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
