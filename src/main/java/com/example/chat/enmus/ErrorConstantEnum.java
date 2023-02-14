package com.example.chat.enmus;

import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;
import org.springframework.util.ObjectUtils;

import java.util.function.Predicate;

/**
 * 错误码枚举
 * 2xxxx 业务错误
 * 9xxxx 系统错误
 */
public enum ErrorConstantEnum {

    REQUEST_FREQUENTLY(501, "请求频繁"),
    FAILURE(500, "系统繁忙，请稍后再试"),
    SUCCESS(200, "请求成功"),
    REQUEST_PARAM_EXCEPTION(90000, "请求参数异常"),
    ;


    private Integer errCode;
    private String errMsg;
    private Predicate predicate;


    ErrorConstantEnum(Integer errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    ErrorConstantEnum(Integer errCode, String errMsg, Predicate predicate) {
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.predicate = predicate;

    }

    public Integer getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public boolean verify(Object object) {
        return predicate.test(object);
    }

    public boolean isSuccess() {
        return ErrorConstantEnum.SUCCESS.getErrCode().equals(this.getErrCode());
    }

    public static ErrorConstantEnum getErrorByMsg(String msg, ErrorConstantEnum defaultErrorEnum) {
        if (StringUtils.isNullOrEmpty(msg)) {
            return defaultErrorEnum;
        }
        for (ErrorConstantEnum errorConstantEnum : ErrorConstantEnum.values()) {
            if (errorConstantEnum.getErrMsg().contains(msg)) {
                return errorConstantEnum;
            }
        }
        return defaultErrorEnum;
    }

    @Override
    public String toString() {
        return "ErrorConstantEnum{" + "errCode=" + errCode + ", errMsg='" + errMsg + '\'' + '}';
    }

    public static String info(ErrorConstantEnum errorConstantEnum) {
        return info(null, errorConstantEnum, null);
    }


    public static String info(ErrorConstantEnum errorConstantEnum, Object request) {
        return info(null, errorConstantEnum, request);
    }

    public static String info(String prefix, ErrorConstantEnum errorConstantEnum, Object request) {
        StringBuffer info = new StringBuffer();
        if (StringUtils.isNullOrEmpty(prefix)) {
            info.append(prefix).append(":");
        }
        if (null != errorConstantEnum) {
            info.append("errorCode=").append(errorConstantEnum.getErrCode()).append(",errorMessage=").append(errorConstantEnum.getErrMsg()).append(";");
        }
        if (ObjectUtils.isEmpty(request)) {
            info.append("request=").append(JSONObject.toJSONString(request));
        }
        return info.toString();
    }

    public static ErrorConstantEnum getByCode(Integer code) {
        for (ErrorConstantEnum value : ErrorConstantEnum.values()) {
            if (value.getErrCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
