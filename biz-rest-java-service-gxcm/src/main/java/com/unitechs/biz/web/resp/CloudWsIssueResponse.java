package com.unitechs.biz.web.resp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

/**
 * @author liujie
 * @createTime 2022年06月02日 14:41:00
 */
@Data
@ToString
public class CloudWsIssueResponse {

    /**
     * 工单号
     */
    @JSONField(name = "WsCode")
    private String wsCode;

    /**
     * 开通模型：SRIP/CLOUDPE/PE
     */
    @JSONField(name = "ServModel")
    private String servModel;

    /**
     * 业务类型：PONCLOUD/PTNCLOUD
     */
    @JSONField(name = "ServType")
    private String servType;

    /**
     * 业务操作类型
     */
    @JSONField(name = "OperType")
    private String operType;

    /**
     * 变更类型
     */
    @JSONField(name = "changeMode")
    private String changeMode;

    /**
     * 工单状态：S(生成工单成功) F(生成工单失败)
     */
    @JSONField(name = "WsStatus")
    private String wsStatus;

    /**
     * 工单错误类型
     */
    @JSONField(name = "ErrorMessage")
    private String errorMessage;

    @JSONField(name = "Result")
    private String result;

    @JSONField(name = "DataParseResult")
    private String dataParseResult;

}
