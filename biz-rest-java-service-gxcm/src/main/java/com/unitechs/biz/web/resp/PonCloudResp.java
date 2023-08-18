package com.unitechs.biz.web.resp;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PonCloudResp {

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

    public static PonCloudResp success(JSONObject para){
        PonCloudResp ponCloudResp = getPonCloudResp(para);
        ponCloudResp.setWsStatus("S");
        return ponCloudResp;
    }

    public static PonCloudResp error(JSONObject para){
        PonCloudResp ponCloudResp = getPonCloudResp(para);
        ponCloudResp.setWsStatus("F");
        return ponCloudResp;
    }

    private static PonCloudResp getPonCloudResp(JSONObject para){
        PonCloudResp resp = new PonCloudResp();
        resp.setWsCode(para.getString("WsCode"));
        resp.setOperType(para.getString("OperType"));
        resp.setServModel(para.getString("ServModel"));
        resp.setServType(para.getString("ServType"));
        return resp;
    }

}
