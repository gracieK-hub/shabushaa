package com.unitechs.biz.domain.service;

import com.alibaba.fastjson.JSONObject;
import com.unitechs.biz.rpc.dto.req.BmBizWorksheetext;
import com.unitechs.framework.service.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.unitechs.biz.constant.GlobalConstant.*;

/**
 * @author fandj
 */
@Service
public class PonCloudService extends CommonBaseWsOpenService{


    public void ponCloudWs(JSONObject para) throws Exception{
        if (!"PONCLOUD".equals(para.getString("ServType"))){
            throw new ServiceException("无效业务类型！");
        }
        validatePara(para);
        List<BmBizWorksheetext> wsItem = new ArrayList<>();
        dealWorkSheetExt(para,wsItem);
        dealWorkSheet(para,wsItem,para);
    }


    @Override
    public void validatePara(JSONObject para) throws Exception {
        checkCommonParam(para);
        String servModel = para.getString("ServModel");
        switch (servModel){
            case SERVMODEL_SRIP:
                validateSRIPPara(para);
                break;
            case SERVMODEL_SR:
                validateSRIPPara(para);
                break;
            case SERVMODEL_CR:
                validateSRIPPara(para);
                break;
            case SERVMODEL_NETPE:
                validateSRIPPara(para);
                break;
            case SERVMODEL_CLOUDPE:
                validateSRIPPara(para);
                break;
            default:
                throw new ServiceException("无效业务模型!");
        }
    }

    public void validateSRIPPara(JSONObject para) throws Exception {
        checkParaInRange(para,"OperType",Arrays.asList("add","del"),"无效操作类型!");
        String operType = para.getString("OperType");
        checkDevice(para,"SR1IP","SR1");
        checkParaIsNull(para,"SR1Port");
        checkIpV4(para,"UserInterIP1",true);
        checkMask(para,"UserInterIP1Mask",true);
        if(StringUtils.isNotBlank(para.getString("UserIP"))){
            String[] split = para.getString("UserIP").split(";");
            for (String s : split) {
                checkIpV4AndMask(s);
            }
        }else{
            throw new ServiceException("UserIP 参数不能为空");
        }
        checkMask(para,"UserIPMask",true);
        checkParaIsNull(para,"BindWidth");
        checkParaIsNull(para,"PVLAN");
        checkParaIsNull(para,"VRFName");
        //wangc 2023年8月16日
        checkParaInRange(para,"RouteProtocol", Arrays.asList("STATIC","BGP","DIRECT"),"无效路由协议");
        checkParaIsNull(para,"BGPASNO");
        checkIpV4AndMask(para,"IpSegment");
        //新增参数
        if (SERVMODEL_SR.equals(para.getString("ServModel"))){
            checkParaIsNull(para,"SR1Name");
            checkParaIsNull(para,"SR1DownPort");
            checkIpV4AndMask(para,"SR1InterIP");
            checkIpV4(para,"SR1NextHopIP",false);
            checkParaIsNull(para,"SR2Name");
            checkDevice(para,"SR2IP","SR2");
            checkParaIsNull(para,"SR2DownPort");
            checkIpV4(para,"SR2InterIP",false);
            checkIpV4(para,"SR2NextHopIP",false);
        }
        if(SERVMODEL_CR.equals(para.getString("ServModel"))){
            checkParaIsNull(para,"CR1Name");
            checkDevice(para,"CR1IP","CR1");
            checkParaIsNull(para,"CR1DownPort");
            checkIpV4AndMask(para,"CR1InterIP");
            checkIpV4(para,"CR1NextHopIP",false);
            checkParaIsNull(para,"CR2Name");
            checkDevice(para,"CR2IP","CR2");
            checkParaIsNull(para,"CR2DownPort");
            checkIpV4(para,"CR2InterIP",false);
            checkIpV4(para,"CR2NextHopIP",false);
        }
        if(SERVMODEL_NETPE.equals(para.getString("ServModel"))){
            checkDevice(para,"PE1IP","PE1");
            checkIpV4AndMask(para,"PE1InterIP");
            checkIpV4(para,"PE1NextHopIP",false);
            checkDevice(para,"PE2IP","PE2");
            checkIpV4AndMask(para,"PE2InterIP");
            checkIpV4(para,"PE2NextHopIP",false);
        }
        if(SERVMODEL_CLOUDPE.equals(para.getString("ServModel"))){
            checkIpV4AndMask(para,"CloudIP");
            checkDevice(para,"CLOUDPE1IP","CLOUDPE1");
            checkIpV4AndMask(para,"CLOUDPE1InterIP");
            checkIpV4AndMask(para,"CLOUDPE1NextHopIP");
            checkDevice(para,"CLOUDPE2IP","CLOUDPE2");
            checkIpV4AndMask(para,"CLOUDPE2InterIP");
            checkIpV4AndMask(para,"CLOUDPE2NextHopIP");
        }
        //wangc 2023年8月14日 -------------------------------------
        if ("add".equals(operType)){
            checkIpV4(para,"InterIP1",true);
            checkMask(para,"InterIP1Mask",true);
            checkParaIsNull(para,"SRPortDesc");
            checkParaIsNull(para,"CVLAN");
            checkParaIsNull(para,"VRFDesc");
            checkParaIsNull(para,"RD");
            checkParaIsNull(para,"ExportRT");
            checkParaIsNull(para,"ImportRT");
        }
    }

}
