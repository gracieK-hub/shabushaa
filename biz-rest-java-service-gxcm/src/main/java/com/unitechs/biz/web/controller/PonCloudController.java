package com.unitechs.biz.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.unitechs.biz.constant.GlobalConstant;
import com.unitechs.biz.domain.service.PonCloudService;
import com.unitechs.biz.web.resp.PonCloudResp;
import com.unitechs.framework.rest.controller.BaseRestController;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fandj
 * 云专网业务
 */
@RestController
public class PonCloudController extends BaseRestController {

    @Autowired
    private PonCloudService ponCloudService;

//    @PostMapping(GlobalConstant.PON_CLOUD)
    @ApiOperation(value = "云专网业务工单下发接口",notes = "云专网业务工单下发接口")
    public PonCloudResp ponCloud(@RequestBody JSONObject para, HttpServletRequest request){
         logger.info("uri:{},para is :{}",request.getRequestURI(),para);
        try {
            ponCloudService.ponCloudWs(para);
        } catch (Exception e) {
            logger.error(e);
            return PonCloudResp.error(para);
        }
        return PonCloudResp.success(para);
    }


}
