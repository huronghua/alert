package com.banmatrip.alert.constant;

import java.io.Serializable;
import java.util.Map;

/**
 * 通用说明：显示参数
 *
 */
public class RestResponse implements Serializable {

	//成功/失败标志
    private boolean             success;
    //成功/失败描述信息
    protected String            message;
    /**
     * 结果代码
     * <ul>
     * <li>success==true 暂不需要code</li>
     * <li>success==false && code==9999 : 为系统异常，跳转到错误页面(如404)</li>
     * <li>success==false && code==其他值 : 为错误提示信息，用于在页面显示，如认证未通过</li>
     * </ul>
     */
    protected String            code;
    private Map<String, Object> data;

    public RestResponse() {
    }
    public RestResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    public RestResponse(boolean success, String message, Map<String, Object> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    public RestResponse(boolean success, String message , String code, Map<String, Object> data) {
        this.success = success;
        this.message = message;
        this.code = code;
        this.data = data;
    }
    
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
	@Override
	public String toString() {
		return "RestResponse [success=" + success + ", message=" + message + ", code=" + code + ", data=" + data + "]";
	}
}
