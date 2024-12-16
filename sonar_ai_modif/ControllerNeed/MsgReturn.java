package com.unitechs.wisdom.core.pojo;

public class MsgReturn {
    private Object data;
    private String msg;
    private boolean success;
    private int code;
    public MsgReturn() {
    }
    public MsgReturn(String msg){
        this.msg = msg;
        this.success = false;
        this.data = "";
        this.code = 0;
    }
    public MsgReturn(boolean success, String msg, Object data){
        this.success = success;
        this.msg = msg;
        this.data = data;
        this.code = 0;
    }
    public MsgReturn(boolean success, String msg){
        this.success = success;
        this.msg = msg;
        this.data = "";
        this.code = 0;
    }
    public MsgReturn(boolean success, String msg, int code) {
        this.msg = msg;
        this.success = success;
        this.code = code;
    }
    public MsgReturn(boolean success, String msg, Object data, int code) {
        this.data = data;
        this.msg = msg;
        this.success = success;
        this.code = code;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
}