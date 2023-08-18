package com.unitechs.biz.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.unitechs.biz.domain.service.CloudWsIssueService;
import com.unitechs.biz.web.resp.CloudWsIssueResponse;
import com.unitechs.framework.logger.Logger;
import com.unitechs.framework.logger.LoggerFactory;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liujie
 * @date 2022年06月02日 14:32:00
 * @description ID27224,27243,27284-四川移动-云专网PTN变更相关任务-王书存
 */
@RestController
public class CloudWsIssueController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 增加鉴权参数,默认为true
     */
    @Value("${wsapi.auth.enable:true}")
    private boolean wsApiAuthEnable;

    @Autowired
    private CloudWsIssueService cloudWsIssueService;

    //@PostMapping(GlobalConstant.INTERNET)
    @ApiOperation(notes = "云专网业务新装工单下发接口", value = "云专网业务新装工单下发接口")
    CloudWsIssueResponse cloudWsIssue(@RequestBody JSONObject param, HttpServletRequest request) {
        logger.info("method worksheetIssue is start... param is " + param);
        CloudWsIssueResponse response = new CloudWsIssueResponse();
        try {
            //根据配置参数判断是否需要鉴权
            if (wsApiAuthEnable) {
                //从header中获取username和ipAddress
                String username = request.getHeader("UserName");
                String ipAddress = request.getHeader("IpAddress");
                cloudWsIssueService.authentication(username, ipAddress);
                //不报异常鉴权成功,添加参数
                param.put("user_name", username);
            }

            response = cloudWsIssueService.cloudWsAddIssue(param);
            logger.info("method cloudWsIssue end, response is " + JSONObject.toJSONString(response));
        } catch (Exception exception) {
            logger.info("method worksheetIssue has Exception " + exception.getMessage());
        }

        return response;
    }
}
