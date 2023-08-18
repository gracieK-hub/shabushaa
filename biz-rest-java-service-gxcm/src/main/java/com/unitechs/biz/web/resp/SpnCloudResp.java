package com.unitechs.biz.web.resp;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SpnCloudResp {

    @JSONField(name = "WsCode")
    private String wsCode;
    @JSONField(name = "ServModel")
    private String servModel;
    @JSONField(name = "ServType")
    private String servType;
    @JSONField(name = "OperType")
    private String operType;
    @JSONField(name = "WsStatus")
    private String wsStatus;
    @JSONField(name = "ProtocolType")
    private String protocolType;
    @JSONField(name = "changeMode")
    private String changeMode;

    public static SpnCloudResp success(JSONObject para){
        SpnCloudResp ponCloudResp = getPonCloudResp(para);
        ponCloudResp.setWsStatus("S");
        return ponCloudResp;
    }

    public static SpnCloudResp error(JSONObject para){
        SpnCloudResp ponCloudResp = getPonCloudResp(para);
        ponCloudResp.setWsStatus("F");
        return ponCloudResp;
    }

    private static SpnCloudResp getPonCloudResp(JSONObject para){
        SpnCloudResp resp = new SpnCloudResp();
        resp.setWsCode(para.getString("WsCode"));
        String operType = para.getString("OperType");
        if ("modUserSubnet".equals(operType)||
        "modCloudSubnet".equals(operType)){
            operType="modify";
        }
        resp.setOperType(operType);
        resp.setServModel(para.getString("ServModel"));
        resp.setServType(para.getString("ServType"));
        resp.setProtocolType(para.getString("ProtocolType"));
        resp.setChangeMode(para.getString("changeMode"));
        return resp;
    }

}
