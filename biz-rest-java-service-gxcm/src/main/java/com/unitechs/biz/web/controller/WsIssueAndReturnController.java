package com.unitechs.biz.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.unitechs.biz.constant.GlobalConstant;
import com.unitechs.biz.domain.service.PonCloudService;
import com.unitechs.biz.domain.service.WsIssueAndReturnService;
import com.unitechs.biz.web.resp.CloudWsIssueResponse;
import com.unitechs.biz.web.resp.PonCloudResp;
import com.unitechs.biz.web.resp.ResponseObject;
import com.unitechs.framework.configitem.client.IConfigItemRpc;
import com.unitechs.framework.configitem.dto.domain.ConfigItem;
import com.unitechs.framework.logger.Logger;
import com.unitechs.framework.logger.LoggerFactory;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liujie
 */
@RestController
public class WsIssueAndReturnController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 增加鉴权参数,默认为true
     */
    @Value("${wsapi.auth.enable:true}")
    private boolean wsApiAuthEnable;

    @Autowired
    private IConfigItemRpc configItemRpc;

    @Autowired
    private PonCloudService ponCloudService;

    @Autowired
    private WsIssueAndReturnService wsIssueAndReturnService;

    @PostMapping(GlobalConstant.INTERNET)
    @ApiOperation(notes = "工单下发接口", value = "工单下发接口")
    Object worksheetIssue(@RequestBody JSONObject param, HttpServletRequest request) {
        logger.info("worksheetIssueController is start...");
        ResponseObject response = new ResponseObject();
        try {
            //根据配置参数判断是否需要鉴权
            if (wsApiAuthEnable) {
                //从header中获取username和ipAddress
                String username = request.getHeader("UserName");
                String ipAddress = request.getHeader("IpAddress");
                wsIssueAndReturnService.authentication(username, ipAddress);
                //不报异常鉴权成功,添加参数
                param.put("user_name", username);
            }
            if ("PONCLOUD".equals(param.getString("ServType"))) {
                try {
                    ponCloudService.ponCloudWs(param);
                    return PonCloudResp.success(param);
                } catch (Exception e) {
                    logger.error(e);
                    return PonCloudResp.error(param);
                }
            }
            Object o = wsIssueAndReturnService.worksheetIssue(param);
            if (o instanceof CloudWsIssueResponse){
                return o;
            }
            response.setDataParseResult("0");
            response.setWsCode(param.getString("WsCode"));
            response.setResult("0");
            response.setErrorMessage("");
        } catch (Exception e) {
            logger.error("worksheetIssueController is error...", e);
            response.setDataParseResult("1");
            response.setErrorMessage(e.getMessage());
        }
        logger.info("worksheetIssueController is end...");
        return response;
    }


    @Scheduled(cron = "0 */1 * * * ?")
    @Async
    @ApiOperation(notes = "工单返单接口", value = "工单返单接口")
    public void worksheetReturn() {
        try {
            ConfigItem configItem = configItemRpc.queryConfigItem("IPSLCallbackRestfulUrl");
            if (configItem != null) {
                String url = configItem.getParaValue();
                wsIssueAndReturnService.worksheetReturn(url);
            }
        } catch (Exception e) {
            logger.info("程序异常:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Scheduled(cron="0 */1 * * * ?")
    @Async
    @ApiOperation(notes = "回单定时任务",value = "查询北向工单下发状态")
    public void wsReturn(){
        logger.info("查询工单下发状态异步定时任务执行中......");
        wsIssueAndReturnService.wsReturn();
    }

}
