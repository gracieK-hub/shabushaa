package com.unitechs.biz.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.unitechs.biz.constant.GlobalConstant;
import com.unitechs.biz.domain.service.SpnCloudService;
import com.unitechs.biz.web.resp.SpnCloudResp;
import com.unitechs.framework.rest.controller.BaseRestController;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * SPN云专线业务
 */
@RestController
public class SpnCloudController extends BaseRestController {

    @Autowired
    private SpnCloudService spnCloudService;

    @PostMapping(GlobalConstant.SPN_CLOUD)
    @ApiOperation(value = "SPN云专线业务",notes = "SPN云专线业务")
    public SpnCloudResp spnCloud(@RequestBody JSONObject para, HttpServletRequest request){
       logger.info("uri:{}, para:{}",request.getRequestURI(),para);
        try {
            spnCloudService.spnCloudWs(para);
        } catch (Exception e) {
            logger.error(e);
            return SpnCloudResp.error(para);
        }
        return SpnCloudResp.success(para);
    }

}
