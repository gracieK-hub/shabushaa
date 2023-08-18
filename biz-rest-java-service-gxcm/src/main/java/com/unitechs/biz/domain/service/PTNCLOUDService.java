package com.unitechs.biz.domain.service;

import com.alibaba.fastjson.JSONObject;
import com.unitechs.biz.rpc.dto.req.BmBizWorksheetext;
import com.unitechs.biz.rpc.dto.req.SaveWsReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PTNCLOUDService extends CommonBaseWsOpenService {

    public void PTNCLOUDWs(JSONObject para) throws Exception {
        //替换操作类型值
        validatePara(para);
        replacePara(para,"OperType");
        List<BmBizWorksheetext> wsItem=new ArrayList<>();
        dealWorkSheetExt(para,wsItem);
        dealWorkSheet(para,wsItem,para);
    }

    @Override
    public void validatePara(JSONObject para) throws Exception {
        String operType = para.getString("OperType");
        String servModel = para.getString("ServModel");
        if ("PE".equals(servModel)){
            isNullThrowException(para.getString("VRFName"), "VPN实例名称不能为空");
            //isNullThrowException(para.getString("RouteProtocol"), "路由协议不能为空");
            String PE1IP = isNullThrowException(para.getString("PE1IP"), "网PE地址（主）-新值不能为空");
            String PE2IP = isNullThrowException(para.getString("PE2IP"), "网PE地址（备）-新值不能为空");
            if ("mod_5".equals(operType)){
                isNullThrowException(para.getString("BGPASNO"), "BGP AS号不能为空");
                isNullThrowException(para.getString("Nbandwidth"), "带宽新值不能为空");
                isNullThrowException(para.getString("NPE1DownPort"),"网PE互联子接口(主)-新值不能为空");
                isNullThrowException(para.getString("NPE2DownPort"),"网PE互联子接口(备)-新值不能为空");
                isNullThrowException(para.getString("NPE1DownPortDesc"),"网PE互联子接口描述(主)-新值不能为空");
                isNullThrowException(para.getString("NPE2DownPortDesc"),"网PE互联子接口描述(备)-新值不能为空");
                isNullThrowException(para.getString("NPE1DownInterIPMask"),"网PE互联地址掩码(主)-新值不能为空");
                isNullThrowException(para.getString("NPE2DownInterIPMask"),"网PE互联地址掩码(备)-新值不能为空");
                isNullThrowException(para.getString("NPE1DownVLAN"),"网PE对接CE外层VLAN（主）-新值不能为空");
                isNullThrowException(para.getString("NPE2DownVLAN"),"网PE对接CE外层VLAN（备）-新值不能为空");

                String NPE1DownInterIP = isNullThrowException(para.getString("NPE1DownInterIP"), "网PE互联子接口ipv4地址(主)-新值不能为空");
                String NPE2DownInterIP = isNullThrowException(para.getString("NPE2DownInterIP"), "网PE互联子接口ipv4地址(备)不能为空");
                checkIPv4(NPE1DownInterIP,"网PE互联子接口ipv4地址(主)-新值格式非法");
                checkIPv4(NPE2DownInterIP,"网PE互联子接口ipv4地址(备)格式非法");
                checkIpV4(para,"NPE1DownNextHopIP",true);
                checkIpV4(para,"NPE2DownNextHopIP",true);
                checkIPv4(PE1IP,"网PE地址（主）-新值格式非法");
                checkIPv4(PE2IP,"网PE地址（备）-新值格式非法");
                //checkIpV4(para,"NUserIP",true);
                checkIpV4(para,"UserIP",true);
            }
            else if ("mod_6".equals(operType)){
                isNullThrowException(para.getString("PE1DownPort"),"网PE互联子接口(主)不能为空");
                isNullThrowException(para.getString("PE2DownPort"),"网PE互联子接口(备)不能为空");
                String PE1DownNextHopIP = isNullThrowException(para.getString("PE1DownNextHopIP"), "CE用户侧互联IPv4地址（主）不能为空");
                String PE2DownNextHopIP = isNullThrowException(para.getString("PE2DownNextHopIP"), "CE用户侧互联IPv4地址（备）不能为空");
                checkIPv4(PE1DownNextHopIP.split("/")[0],"CE用户侧互联IPv4地址（主）格式非法");
                checkIPv4(PE2DownNextHopIP.split("/")[0],"CE用户侧互联IPv4地址（备）格式非法");
                checkIpV4(para,"UserIP",true);
            }
            //checkParaInRange(para,"RouteProtocol", Arrays.asList("static"),"无效路由协议");
            checkDevice(para,"PE1IP","PE1");
            checkDevice(para,"PE2IP","PE2");
        }
    }

    @Override
    public SaveWsReq convertParaToWsReq(JSONObject param) throws Exception {
        return null;
    }
}
