package com.unitechs.biz.domain.service;

import com.alibaba.fastjson.JSONObject;
import com.unitechs.biz.enums.OperateEnum;
import com.unitechs.biz.rpc.dto.req.BmBizWorksheetext;
import com.unitechs.framework.service.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.unitechs.biz.constant.GlobalConstant.SERVMODEL_PE;

@Service
public class SpnCloudService extends CommonBaseWsOpenService {

    public void spnCloudWs(JSONObject para) throws Exception {
        if (!"SPNCLOUD".equals(para.getString("ServType"))){
            throw new ServiceException("无效业务类型！");
        }
        validatePara(para);
        List<BmBizWorksheetext> wsItem = new ArrayList<>();
        dealWorkSheetExt(para, wsItem);
        dealWorkSheet(para, wsItem, para);
    }

    @Override
    public void validatePara(JSONObject para) throws Exception {
        checkParaInRange(para,"OperType",Arrays.asList("add","del","modify"),"无效操作类型!");
        replaceOperType(para);
        checkCommonParam(para);
        String servModel = para.getString("ServModel");
        switch (servModel){
            case SERVMODEL_PE:
                validatePEPara(para);
                break;
            default:
                throw new ServiceException("无效业务模型!");
        }
    }

    /**
     * 操作类型为modify时更改其值
     * @param para
     */
    public void replaceOperType(JSONObject para){
        String operType = para.getString("OperType");
        String changeMode = para.getString("changeMode");
        if (OperateEnum.MODIFY.getCode().equals(operType)) {
            checkParaInRange(para, "changeMode", Arrays.asList("5", "4"), "无效变更类型!");
            switch (changeMode){
                case "4": operType = "modUserSubnet";break;
                case "5": operType = "modCloudSubnet";break;
                default:break;
            }
        }
        para.put("OperType",operType);
    }

    public void validatePEPara(JSONObject para) throws Exception {
        String operType = para.getString("OperType");
        checkParaIsNull(para, "VRFName");
        checkIpV4(para, "PE1InterIP", false);
        checkIpV4(para, "PE2InterIP", false);
        checkParaInRange(para, "ProtocolType", Arrays.asList("mpls"), "无效协议类型");
        if ("add,del,modUserSubnet".contains(operType)) {
            checkIpV4(para, "UserIP", true);
            checkDevice(para, "PE1IP", "PE1");
            checkParaIsNull(para, "PE1DownPort");
            checkDevice(para, "PE2IP", "PE2");
            checkParaIsNull(para, "PE2DownPort");
        }
        if ("add".equals(operType)) {
            checkParaIsNull(para, "RD");
            checkParaIsNull(para, "ExportRT");
            checkParaIsNull(para, "ImportRT");
            checkParaIsNull(para, "PE1Name");
            checkParaIsNull(para, "PE1PortDesc");
            checkParaIsNull(para, "PE2Name");
            checkParaIsNull(para, "CLOUDPE1PortDesc");
            checkParaIsNull(para, "CLOUDPE2PortDesc");
        }

        if ("add,del".contains(operType)) {
            checkParaInRange(para, "RouteProtocol", Arrays.asList("STATIC", "BGP", "DIRECT"), "无效路由协议");
            checkParaIsNull(para, "PE1VLAN");
            checkParaIsNull(para, "PE2VLAN");
            checkParaIsNull(para, "BGPASNO");
            checkParaIsNull(para, "CloudBGPASNO");
            checkIpV4(para, "CLOUDPE1InterIP", true);
            checkParaIsNull(para, "CLOUDPE1VLAN");
            checkIpV4(para, "CLOUDPE2InterIP", true);
            checkParaIsNull(para, "CLOUDPE2VLAN");

        }
        if ("modUserSubnet".equals(operType)|| "add".equals(operType)) {
            checkIpV4(para, "PE1NextHopIP", true);
            checkIpV4(para, "PE2NextHopIP", true);
        }
        if ("add,del,modCloudSubnet".contains(operType)) {
            checkIpV4(para, "CloudIP", true);
            checkDevice(para, "CLOUDPE1IP", "CLOUDPE1");
            checkParaIsNull(para, "CLOUDPE1Port");
            checkIpV4(para, "CLOUDPE1NextHopIP", true);
            checkDevice(para, "CLOUDPE2IP", "CLOUDPE2");
            checkParaIsNull(para, "CLOUDPE2Port");
            checkIpV4(para, "CLOUDPE2NextHopIP", true);
        }
    }
}
