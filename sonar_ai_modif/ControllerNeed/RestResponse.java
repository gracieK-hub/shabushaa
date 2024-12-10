package com.unitechs.framework.rest.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unitechs.framework.i18n.I18nMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

@Api("通用返回对象包装")
public final class RestResponse<T> extends RestBaseBean {
    private static final transient long serialVersionUID = 3593827217136880822L;
    private static final transient String DEFAULT_TIP_SUCCESS = "成功";
    public static final transient String DEFAULT_TIP_ERROR = "失败";
    private static final transient String CODE_SUCCESS = "0000";
    public static final transient String CODE_ERROR = "0";
    @ApiModelProperty(
        value = "返回编码",
        notes = "返回编码",
        example = "0000"
    )
    private String code;
    @ApiModelProperty(
        value = "返回信息",
        notes = "返回信息",
        example = "success"
    )
    private String msg;
    @ApiModelProperty(
        value = "用户提示信息",
        notes = "用户提示信息",
        example = "操作成功"
    )
    private String tip;
    @ApiModelProperty(
        value = "业务数据",
        notes = "业务数据",
        example = ""
    )
    private T data;

    public RestResponse() {
        this.code = "0000";
        this.tip = "成功";
    }

    private RestResponse(String code, String tip) {
        this.code = code;
        this.tip = tip;
    }

    private RestResponse(String code, String tip, T data) {
        this.code = code;
        this.tip = tip;
        this.data = data;
    }

    public static <T> RestResponse<T> error(String i18NKey) {
        String tip = I18nMessage.getMsg(i18NKey);
        return new RestResponse("0", tip);
    }

    public static <T> RestResponse<T> error(T data) {
        return new RestResponse("0", "失败", data);
    }

    public static <T> RestResponse<T> error(String code, String i18NKey) {
        String tip = I18nMessage.getMsg(i18NKey);
        return new RestResponse(code, tip);
    }

    public static <T> RestResponse<T> error(String code, String i18NKey, T data) {
        String tip = I18nMessage.getMsg(i18NKey);
        return new RestResponse(code, tip, data);
    }
    public static <T> RestResponse<T> error(String code, String i18NKey, Object[] i18NKeyArgs) {
        String tip = I18nMessage.getMsg(i18NKey, i18NKeyArgs);
        return new RestResponse(code, tip);
    }
    public static <T> RestResponse<T> error(String code, String i18NKey, Object[] i18NKeyArgs, T data) {
        String tip = I18nMessage.getMsg(i18NKey, i18NKeyArgs);
        return new RestResponse(code, tip, data);
    }
    public static <T> RestResponse<T> addError(String tip) {
        return new RestResponse("0", tip);
    }
    public static <T> RestResponse<T> addError(String code, String tip) {
        return new RestResponse(code, tip);
    }
    public static <T> RestResponse<T> addError(String code, String tip, T data) {
        return new RestResponse(code, tip, data);
    }
    public static <T> RestResponse<T> ok() {
        return new RestResponse("0000", "成功");
    }
    public static <T> RestResponse<T> ok(T data) {
        RestResponse<T> restResponse = new RestResponse("0000", "成功");
        restResponse.data = data;
        return restResponse;
    }
    @JSONField(
        serialize = false
    )
    @JsonIgnore
    public boolean isSuccess() {
        return "0000".equals(this.code);
    }
}
