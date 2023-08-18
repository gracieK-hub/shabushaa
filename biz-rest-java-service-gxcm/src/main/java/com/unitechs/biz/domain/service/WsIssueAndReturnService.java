package com.unitechs.biz.domain.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.unitechs.biz.common.utils.HttpRemoteCallClient;
import com.unitechs.biz.common.utils.IPv4ConverUtil;
import com.unitechs.biz.domain.entity.atomic.BmBizCompletedWsToNotice;
import com.unitechs.biz.domain.mapper.atomic.CmBizServApiClientCfgMapper;
import com.unitechs.biz.domain.mapper.atomic.WsAndReturnMapper;
import com.unitechs.biz.domain.thread.WslssueAndReturnThread;
import com.unitechs.biz.rpc.client.WsManagerRpc;
import com.unitechs.biz.rpc.common.entity.RpcRestResponse;
import com.unitechs.biz.rpc.dto.req.BmBizWorksheetext;
import com.unitechs.biz.rpc.dto.req.DeployReq;
import com.unitechs.biz.rpc.dto.req.SaveWsReq;
import com.unitechs.biz.web.req.GeneralInfo;
import com.unitechs.framework.logger.Logger;
import com.unitechs.framework.logger.LoggerFactory;
import com.unitechs.resource.rpc.client.IResDevRpc;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


@Service
public class WsIssueAndReturnService {

	@Autowired
	private WsMsgQryService wsMsgQryService;

	@Resource
	private CmBizServApiClientCfgMapper cmBizServApiClientCfgMapper;

	@Autowired
    private IResDevRpc resClient;

	@Autowired
	private WsManagerRpc wsManagerRpc;

	@Resource
	private WsAndReturnMapper wsAndReturnMapper;

	@Autowired
	private PTNCLOUDService ptncloudService;

	@Autowired
	private CloudWsIssueService cloudWsIssueService;

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	public static Map<String,String> WORKSHEET_EXT_MAP=new HashMap<String, String>();
	static {
//		WORKSHEET_EXT_MAP.put("PVLAN", "PVLAN");
		WORKSHEET_EXT_MAP.put("CVLAN", "CVLAN");
		WORKSHEET_EXT_MAP.put("BindWidth", "BandWidth");
		WORKSHEET_EXT_MAP.put("SRPortDesc", "PortDescr");
//		WORKSHEET_EXT_MAP.put("VRRP", "enableVRRP");

		WORKSHEET_EXT_MAP.put("UserIP", "UserIP");
		WORKSHEET_EXT_MAP.put("UserIPMask", "UserIPMask");
		WORKSHEET_EXT_MAP.put("SR1Port", "SR1_SubPort");
		WORKSHEET_EXT_MAP.put("InterIP1", "IntIP1");
		WORKSHEET_EXT_MAP.put("InterIP1Mask", "IntIPMask1");

		WORKSHEET_EXT_MAP.put("UserInterIP1", "CustomerInterfaceAddr1");
		WORKSHEET_EXT_MAP.put("AGSWUpPort1", "HJSW1_UplinkPort");
		WORKSHEET_EXT_MAP.put("AGSWDownPort1", "HJSW1_DownPort");
		WORKSHEET_EXT_MAP.put("SR2Port", "SR2_SubPort");
		WORKSHEET_EXT_MAP.put("InterIP2", "IntIP2");

		WORKSHEET_EXT_MAP.put("InterIP2Mask", "IntIPMask2");
		WORKSHEET_EXT_MAP.put("UserInterIP2", "CustomerInterfaceAddr2");
		WORKSHEET_EXT_MAP.put("AGSWUpPort2", "HJSW2_UplinkPort");
		WORKSHEET_EXT_MAP.put("AGSWDownPort2", "HJSW2_DownPort");
		WORKSHEET_EXT_MAP.put("VRRPPVLAN", "VRRPManagePVLAN");

		WORKSHEET_EXT_MAP.put("VRRPCVLAN", "VRRPManageCVLAN");
		WORKSHEET_EXT_MAP.put("VRRPvGetway", "VRRPVirtualGW");
		WORKSHEET_EXT_MAP.put("VRRPGrpNO", "VRRPGrpNO");
		WORKSHEET_EXT_MAP.put("VRRPPriority1", "VRRPPriority1");
		WORKSHEET_EXT_MAP.put("VRRPPriority2", "VRRPPriority2");

		WORKSHEET_EXT_MAP.put("PONUSERVLAN", "USERVLAN");
		WORKSHEET_EXT_MAP.put("ONUVLAN", "ONUManageVLAN");
		WORKSHEET_EXT_MAP.put("ONUID", "ONUID");
		WORKSHEET_EXT_MAP.put("ONUSN", "SN");
		WORKSHEET_EXT_MAP.put("OLTUpPort1", "OLT_UplinkPort1");

		WORKSHEET_EXT_MAP.put("OLTDownPort", "OLT_DownPort");
		WORKSHEET_EXT_MAP.put("ONUUpPort", "ONU_UplinkPort");
		WORKSHEET_EXT_MAP.put("ONUDownPort", "ONU_DownPort");
		WORKSHEET_EXT_MAP.put("OLTUpPort2", "OLT_UplinkPort2");
		WORKSHEET_EXT_MAP.put("ONUIP", "ONUIP");

		WORKSHEET_EXT_MAP.put("ACCSWIPUpPort1", "JRSW_UplinkPort1");
		WORKSHEET_EXT_MAP.put("ACCSWIPDownPort1", "JRSW_DownPort");
		WORKSHEET_EXT_MAP.put("ACCSWIPUpPort2", "JRSW_UplinkPort2");

		WORKSHEET_EXT_MAP.put("VRFName", "VRFName");
		WORKSHEET_EXT_MAP.put("VRFDesc", "VRFDescr");
		WORKSHEET_EXT_MAP.put("RD", "RD");
		WORKSHEET_EXT_MAP.put("ExportRT", "ExportRT");
		WORKSHEET_EXT_MAP.put("ImportRT", "ImportRT");
		WORKSHEET_EXT_MAP.put("RouteProtocol", "RoutingProtocol");


		WORKSHEET_EXT_MAP.put("BGPASNO", "BGPASNO");
		WORKSHEET_EXT_MAP.put("PE1Port", "PE1Port");
		WORKSHEET_EXT_MAP.put("PE1PortDesc", "PE1PortDesc");
		WORKSHEET_EXT_MAP.put("PE1InterIP", "PE1InterIP");
		WORKSHEET_EXT_MAP.put("PE1InterIPMask", "PE1InterIPMask");
		WORKSHEET_EXT_MAP.put("PE1VLAN", "PE1VLAN");
		WORKSHEET_EXT_MAP.put("PE1NextHopIP", "PE1NextHopIP");

		WORKSHEET_EXT_MAP.put("PE2Port", "PE2Port");
		WORKSHEET_EXT_MAP.put("PE2PortDesc", "PE2PortDesc");
		WORKSHEET_EXT_MAP.put("PE2InterIP", "PE2InterIP");
		WORKSHEET_EXT_MAP.put("PE2InterIPMask", "PE2InterIPMask");
		WORKSHEET_EXT_MAP.put("PE2VLAN", "PE2VLAN");
		WORKSHEET_EXT_MAP.put("PE2NextHopIP", "PE2NextHopIP");
		WORKSHEET_EXT_MAP.put("CloudIP", "CloudIP");
		WORKSHEET_EXT_MAP.put("CloudIPMask", "CloudIPMask");


		WORKSHEET_EXT_MAP.put("BAS1Port", "BAS1Port");
		WORKSHEET_EXT_MAP.put("BAS2Port", "BAS2Port");
		WORKSHEET_EXT_MAP.put("DomainName", "DomainName");

		WORKSHEET_EXT_MAP.put("PE1DownPort", "PE1DownPort");
		WORKSHEET_EXT_MAP.put("PE1DownPortDesc", "PE1DownPortDesc");
		WORKSHEET_EXT_MAP.put("PE1DownInterIP", "PE1DownInterIP");
		WORKSHEET_EXT_MAP.put("PE1DownInterIPMask", "PE1DownInterIPMask");
		WORKSHEET_EXT_MAP.put("PE1DownVLAN", "PE1DownVLAN");
		WORKSHEET_EXT_MAP.put("PE1DownNextHopIP", "PE1DownNextHopIP");

		WORKSHEET_EXT_MAP.put("PE2DownPort", "PE2DownPort");
		WORKSHEET_EXT_MAP.put("PE2DownPortDesc", "PE2DownPortDesc");
		WORKSHEET_EXT_MAP.put("PE2DownInterIP", "PE2DownInterIP");
		WORKSHEET_EXT_MAP.put("PE2DownInterIPMask", "PE2DownInterIPMask");
		WORKSHEET_EXT_MAP.put("PE2DownVLAN", "PE2DownVLAN");
		WORKSHEET_EXT_MAP.put("PE2DownNextHopIP", "PE2DownNextHopIP");

		WORKSHEET_EXT_MAP.put("UserBGPASNO", "UserBGPASNO");

		WORKSHEET_EXT_MAP.put("ONUGateWay", "ONUGW");
		WORKSHEET_EXT_MAP.put("BASUserIP", "BASUserIP");

		WORKSHEET_EXT_MAP.put("IPPoolName", "IPPoolName");
		WORKSHEET_EXT_MAP.put("IPPoolGateWay", "IPPoolGateWay");
		WORKSHEET_EXT_MAP.put("IPPoolGWMask", "IPPoolGWMask");
		WORKSHEET_EXT_MAP.put("IPPoolBeginIP", "IPPoolBeginIP");
		WORKSHEET_EXT_MAP.put("IPPoolEndIP", "IPPoolEndIP");


		WORKSHEET_EXT_MAP.put("PE1Port2", "PE1Port2");
		WORKSHEET_EXT_MAP.put("PE1PortDesc2", "PE1PortDesc2");
		WORKSHEET_EXT_MAP.put("PE1InterIP2", "PE1InterIP2");
		WORKSHEET_EXT_MAP.put("PE1InterIPMask2", "PE1InterIPMask2");
		WORKSHEET_EXT_MAP.put("PE1VLAN2", "PE1VLAN2");
		WORKSHEET_EXT_MAP.put("PE1NextHopIP2", "PE1NextHopIP2");
		WORKSHEET_EXT_MAP.put("PE2Port2", "PE2Port2");
		WORKSHEET_EXT_MAP.put("PE2PortDesc2", "PE2PortDesc2");
		WORKSHEET_EXT_MAP.put("PE2InterIP2", "PE2InterIP2");
		WORKSHEET_EXT_MAP.put("PE2InterIPMask2", "PE2InterIPMask2");
		WORKSHEET_EXT_MAP.put("PE2VLAN2", "PE2VLAN2");
		WORKSHEET_EXT_MAP.put("PE2NextHopIP2", "PE2NextHopIP2");
		WORKSHEET_EXT_MAP.put("NETPE1IP", "NETPE1IP");
		WORKSHEET_EXT_MAP.put("NETPE2IP", "NETPE2IP");
		WORKSHEET_EXT_MAP.put("BgpPassword", "BgpPassword");

		//wangc 2023年8月17日
		WORKSHEET_EXT_MAP.put("SR1","SR1");
		WORKSHEET_EXT_MAP.put("SR2","SR2");
		WORKSHEET_EXT_MAP.put("CR1","CR1");
		WORKSHEET_EXT_MAP.put("CR2","CR2");
		WORKSHEET_EXT_MAP.put("PE1","PE1");
		WORKSHEET_EXT_MAP.put("PE2","PE2");
		WORKSHEET_EXT_MAP.put("CLOUDPE1","CLOUDPE1");
		WORKSHEET_EXT_MAP.put("CLOUDPE2","CLOUDPE2");
	}

	public Object worksheetIssue(JSONObject param) throws Exception {
		String result = "C";
		logger.info("worksheetIssue is start...");
		logger.info("worksheetIssue request param: "+param.toJSONString());
		//接口通用参数校验
		String ServType = dealNull(param.getString("ServType"));
		String WsCode = dealNull(param.getString("WsCode"));
		String ServModel = dealNull(param.getString("ServModel"));
		String OperType = dealNull(param.getString("OperType"));
		String VRRP = dealNull(param.getString("VRRP"));
		String PVLAN = dealNull(param.getString("PVLAN"));
		if(StringUtils.isBlank(ServType)) {
			throw new Exception("ServType为空");
		}else {
			if(!ServType.equals("IPSL")&&!ServType.equals("MPLSVPN")&&!ServType.equals("PONCLOUD")&&!ServType.equals("PTNCLOUD")
			&&!ServType.equals("PONCLOUD_MP")) {
				throw new Exception("业务类型ServType不支持");
			}
		}
		if(StringUtils.isBlank(WsCode)){
			throw new Exception("WsCode为空");
		}else {
			String Flag = wsMsgQryService.wsNbrIfExist(WsCode, ServType);
			if(Flag.equals("Y")) {
				throw new Exception("相同工单号工单不能多次下发");
			}
		}
		if(StringUtils.isBlank(ServModel)) {
			throw new Exception("ServModel为空!");
		}else {
			if(ServType.equals("IPSL")){
				if(!ServModel.equals("SRIP")&&!ServModel.equals("PON")&&!ServModel.equals("SW")
						&&!ServModel.equals("BASIP")&&!ServModel.equals("BSW")
						&&!ServModel.equals("BAS")) {
					throw new Exception("无效ServModel！");
				}
			}
			if(ServType.equals("MPLSVPN")){
				//暂定三种开通模型SRIP/PON/SW
				if(!ServModel.equals("SRIP")&&!ServModel.equals("PON")&&!ServModel.equals("SW")&&!ServModel.equals("CRBRAS")) {
					throw new Exception("无效ServModel！");
				}
			}
			//如果ServType=PONCLOUD,判断开通模型SRIP/PON/SW/PE
			if(ServType.equals("PONCLOUD")||ServType.equals("PONCLOUD_MP")){
				if(!ServModel.equals("SRIP")&&!ServModel.equals("PON")&&!ServModel.equals("SW")
						&&!ServModel.equals("PE")&&!ServModel.equals("PENX")&&!ServModel.equals("APEJT")) {
					throw new Exception("无效ServModel！");
				}
			}
			if(ServType.equals("PTNCLOUD")){
				if(!ServModel.equals("PE")&&!ServModel.equals("BRAS")){
					throw new Exception("无效ServModel！");
				}
			}
		}
		if(StringUtils.isBlank(OperType)) {
			throw new Exception("OperType为空!");
		}else {
			if((ServType.equals("IPSL") || ServType.equals("MPLSVPN") || ServType.equals("PONCLOUD"))
					&& (ServModel.equals("PON") || ServModel.equals("SW")) ){
				if(!OperType.equals("modspeed")&&!OperType.equals("active")&&!OperType.equals("deactive")
						&&!OperType.equals("add")&&!OperType.equals("del")) {
					/*throw new Exception("无效OperType!");*/
				}
			}else{
				if(!OperType.equals("add")&&!OperType.equals("del")) {
				/*	throw new Exception("无效OperType!");*/
				}
			}
		}
        //任务27289 PTNCLOUD操作类型变换
		/*if ("PTNCLOUD".equals(ServType)&&"PE".equals(ServModel)){
			if ("mod_6".equals(OperType)||"mod_5".equals(OperType)){
				ptncloudService.PTNCLOUDWs(param);
				return result;
			}
			if ("mod_1,mod_2,mod_3".contains(OperType)){
				return cloudWsIssueService.cloudWsAddIssue(param);
			}
		}*/

		//条件必填参数校验
		String SR1IP = dealNull(param.getString("SR1IP"));
		String SR1Port = dealNull(param.getString("SR1Port"));
		String InterIP1 = dealNull(param.getString("InterIP1"));
		String InterIP1Mask = dealNull(param.getString("InterIP1Mask"));
		String UserInterIP1 = dealNull(param.getString("UserInterIP1"));
		String AGSWIP1 = dealNull(param.getString("AGSWIP1"));
		String AGSWUpPort1 = dealNull(param.getString("AGSWUpPort1"));
		String AGSWDownPort1 = dealNull(param.getString("AGSWDownPort1"));
		String UserIP = dealNull(param.getString("UserIP"));
		String UserIPMask = dealNull(param.getString("UserIPMask"));
		String SRPortDesc = dealNull(param.getString("SRPortDesc"));
		String BindWidth = dealNull(param.getString("BindWidth"));
		String CVLAN = dealNull(param.getString("CVLAN"));
		String VRRPGrpNO = dealNull(param.getString("VRRPGrpNO"));
		String VRRPPriority1 = dealNull(param.getString("VRRPPriority1"));
		String VRRPPriority2 = dealNull(param.getString("VRRPPriority2"));
		String VRRPPVLAN = dealNull(param.getString("VRRPPVLAN"));
		String VRRPCVLAN = dealNull(param.getString("VRRPCVLAN"));
		String VRRPvGetway = dealNull(param.getString("VRRPvGetway"));
		String InterIP2 = dealNull(param.getString("InterIP2"));
		String InterIP2Mask = dealNull(param.getString("InterIP2Mask"));
		String SR2IP = dealNull(param.getString("SR2IP"));
		String SR2Port = dealNull(param.getString("SR2Port"));
		String UserInterIP2 = dealNull(param.getString("UserInterIP2"));
		String AGSWIP2 = dealNull(param.getString("AGSWIP2"));
		String AGSWUpPort2 = dealNull(param.getString("AGSWUpPort2"));
		String AGSWDownPort2 = dealNull(param.getString("AGSWDownPort2"));
		String PONUSERVLAN = dealNull(param.getString("PONUSERVLAN"));
		String ONUVLAN = dealNull(param.getString("ONUVLAN"));
		String ONUID = dealNull(param.getString("ONUID"));
		String ONUSN = dealNull(param.getString("ONUSN"));
		String OLTIP =dealNull(param.getString("OLTIP"));
		String OLTUpPort1 = dealNull(param.getString("OLTUpPort1"));
		String OLTDownPort = dealNull(param.getString("OLTDownPort"));
		String ONUIP = dealNull(param.getString("ONUIP"));
		String ONUUpPort = dealNull(param.getString("ONUUpPort"));
		String ONUDownPort = dealNull(param.getString("ONUDownPort"));
		String OLTUpPort2 = dealNull(param.getString("OLTUpPort2"));
		String ACCSWIP = dealNull(param.getString("ACCSWIP"));
		String ACCSWIPUpPort1 = dealNull(param.getString("ACCSWIPUpPort1"));
		String ACCSWIPDownPort1 = dealNull(param.getString("ACCSWIPDownPort1"));
		String ACCSWIPUpPort2 = dealNull(param.getString("ACCSWIPUpPort2"));

		String SR1IPDevId = "";
		String SR1PortVal = "";

		String AGSWIP1DevId = "";
		String SR2IPDevId = "";
		String SR2PortVal ="";
		String AGSWIP2DevId = "";
		String OLTIPDevId = "";
		String ACCSWIPDevId = "";

		String VRFName = dealNull(param.getString("VRFName"));
		String VRFDesc = dealNull(param.getString("VRFDesc"));
		String RD = dealNull(param.getString("RD"));
		String ExportRT = dealNull(param.getString("ExportRT"));
		String ImportRT = dealNull(param.getString("ImportRT"));
		String RouteProtocol = dealNull(param.getString("RouteProtocol"));

		String BGPASNO = dealNull(param.getString("BGPASNO"));
		String PE1IP = dealNull(param.getString("PE1IP"));
		String PE1Port = dealNull(param.getString("PE1Port"));
		String PE1PortDesc = dealNull(param.getString("PE1PortDesc"));
		String PE1InterIP = dealNull(param.getString("PE1InterIP"));
		String PE1InterIPMask = dealNull(param.getString("PE1InterIPMask"));
		String PE1VLAN = dealNull(param.getString("PE1VLAN"));
		String PE1NextHopIP = dealNull(param.getString("PE1NextHopIP"));
		String PE2IP = dealNull(param.getString("PE2IP"));
		String PE2Port =dealNull(param.getString("PE2Port"));
		String PE2PortDesc = dealNull(param.getString("PE2PortDesc"));
		String PE2InterIP = dealNull(param.getString("PE2InterIP"));
		String PE2InterIPMask = dealNull(param.getString("PE2InterIPMask"));
		String PE2VLAN = dealNull(param.getString("PE2VLAN"));
		String PE2NextHopIP = dealNull(param.getString("PE2NextHopIP"));
		String CloudIP = dealNull(param.getString("CloudIP"));
		String CloudIPMask = dealNull(param.getString("CloudIPMask"));

		String PE1IPDevID = "";
		String PE2IPDevID = "";

		String BAS1IP = dealNull(param.getString("BAS1IP"));
		String BAS1Port = dealNull(param.getString("BAS1Port"));
		String BAS2IP = dealNull(param.getString("BAS2IP"));
		String BAS2Port = dealNull(param.getString("BAS2Port"));
		String DomainName = dealNull(param.getString("DomainName"));
		String BAS1IPDevID = "";
		String BAS2IPDevID = "";

		String PE1DownPort = dealNull(param.getString("PE1DownPort"));
		String PE1DownPortDesc = dealNull(param.getString("PE1DownPortDesc"));
		String PE1DownInterIP = dealNull(param.getString("PE1DownInterIP"));
		String PE1DownInterIPMask = dealNull(param.getString("PE1DownInterIPMask"));
		String PE1DownVLAN = dealNull(param.getString("PE1DownVLAN"));
		String PE1DownNextHopIP = dealNull(param.getString("PE1DownNextHopIP"));

		String PE2DownPort = dealNull(param.getString("PE2DownPort"));
		String PE2DownPortDesc = dealNull(param.getString("PE2DownPortDesc"));
		String PE2DownInterIP = dealNull(param.getString("PE2DownInterIP"));
		String PE2DownInterIPMask = dealNull(param.getString("PE2DownInterIPMask"));
		String PE2DownVLAN = dealNull(param.getString("PE2DownVLAN"));
		String PE2DownNextHopIP = dealNull(param.getString("PE2DownNextHopIP"));

		String UserBGPASNO = dealNull(param.getString("UserBGPASNO"));

		String ONUGateWay = dealNull(param.getString("ONUGateWay"));
		String BASUserIP = dealNull(param.getString("BASUserIP"));

		String IPPoolName = dealNull(param.getString("IPPoolName"));
		String IPPoolGateWay = dealNull(param.getString("IPPoolGateWay"));
		String IPPoolGWMask = dealNull(param.getString("IPPoolGWMask"));
		String IPPoolBeginIP = dealNull(param.getString("IPPoolBeginIP"));
		String IPPoolEndIP = dealNull(param.getString("IPPoolEndIP"));

		String BgpPassword = dealNull(param.getString("BgpPassword"));
		String NETPE1IP = dealNull(param.getString("NETPE1IP"));
		String NETPE2IP = dealNull(param.getString("NETPE2IP"));

		String AGSWBridgePort1 = dealNull(param.getString("AGSWBridgePort1"));
		String AGSWBridgePort2 = dealNull(param.getString("AGSWBridgePort2"));

		String CLOUDPE1="";
		String CLOUDPE2="";

		IPv4ConverUtil ipv4Format=new IPv4ConverUtil();

		String CLOUDPE1IP = param.getString("CLOUDPE1IP");
		if(StringUtils.isNotEmpty(CLOUDPE1IP)){
			List<String> devIdList = resClient.getdevidbydevip(CLOUDPE1IP);
			if(CollectionUtils.isEmpty(devIdList)) {
				//throw new Exception("SR1IP在网管查询不到");
			}else if(devIdList.size()!=1){
				//throw new Exception("SR1IP找到多个对应设备");
			}else{
				CLOUDPE1 = devIdList.get(0);
			}
		}
		String CLOUDPE2IP = param.getString("CLOUDPE2IP");
		if(StringUtils.isNotEmpty(CLOUDPE2IP)){
			List<String> devIdList = resClient.getdevidbydevip(CLOUDPE2IP);
			if(CollectionUtils.isEmpty(devIdList)) {
				//throw new Exception("SR1IP在网管查询不到");
			}else if(devIdList.size()!=1){
				//throw new Exception("SR1IP找到多个对应设备");
			}else{
				CLOUDPE2 = devIdList.get(0);
			}
		}

		if(ServModel.equals("SRIP")||ServModel.equals("CRBRAS")) {   //PON接入IP侧
//			if(OperType.equals("add")||OperType.equals("del")) {
//				if(StringUtils.isBlank(VRRP)) {
//					throw new Exception("VRRP为空!");
//				}else {
//					if(!VRRP.equals("Y")&&!VRRP.equals("N")) {
//						throw new Exception("无效VRRP!");
//					}
//				}
//				if(StringUtils.isBlank(PVLAN)) {
//					throw new Exception("PVLAN为空!");
//				}
//			}

			if(StringUtils.isBlank(SR1IP)) {
				//throw new Exception("SR1IP为空!");
			}else {
				List<String> devIdList = resClient.getdevidbydevip(SR1IP);
				if(CollectionUtils.isEmpty(devIdList)) {
					//throw new Exception("SR1IP在网管查询不到");
				}else if(devIdList.size()!=1){
					//throw new Exception("SR1IP找到多个对应设备");
				}else{
					SR1IPDevId = devIdList.get(0);
				}
			}
//			if(StringUtils.isBlank(SR1Port)) {
//				throw new Exception("SR1Port为空!");
//			}else {
//				SR1PortVal=SR1Port.substring(0, SR1Port.indexOf("."));
//				String Flag = resClient.portifexist(SR1IPDevId, SR1PortVal);
//				if(Flag.equals("N")) {
//					throw new Exception("SR1Port不存在");
//				}
//			}
			if(OperType.equals("add")) {
//				if(StringUtils.isBlank(InterIP1)||StringUtils.isBlank(InterIP1Mask)) {
//					throw new Exception("InterIP1/InterIP1Mask为空");
//				}else {
//					String isIPLegal = ipv4Format.validateSimpleIPv4(InterIP1);
//					if(isIPLegal.equals("0")) {
//						throw new Exception("InterIP1格式非法");
//					}
//					String isInterIP1MaskLegal = ipv4Format.validateSimpleIPv4(InterIP1Mask);
//					if(isInterIP1MaskLegal.equals("0")) {
//						throw new Exception("InterIP1Mask格式非法");
//					}
//				}
//				if(StringUtils.isBlank(SRPortDesc)) {
//					throw new Exception("SRPortDesc为空");
//				}
//				if(StringUtils.isBlank(BindWidth)) {
//					throw new Exception("BindWidth为空");
//				}



//				if(ServType.equals("MPLSVPN") || ServType.equals("PONCLOUD")||ServType.equals("PONCLOUD_MP")){
//					if(StringUtils.isBlank(VRFDesc)) {
//						throw new Exception("VRFDesc为空");
//					}
//					if(StringUtils.isBlank(RD)) {
//						throw new Exception("RD为空");
//					}
//					if(StringUtils.isBlank(ExportRT)) {
//						throw new Exception("ExportRT为空");
//					}
//					if(StringUtils.isBlank(ImportRT)) {
//						throw new Exception("ImportRT为空");
//					}
//				}

			}
//			if(ServType.equals("IPSL")){
//				if(StringUtils.isBlank(CVLAN)) {
//					throw new Exception("CVLAN为空");
//				}
//			}

//			if(ServType.equals("MPLSVPN") || ServType.equals("PONCLOUD")||ServType.equals("PONCLOUD_MP")) {
//				if(StringUtils.isBlank(VRFName)) {
//					throw new Exception("VRFName为空");
//				}
//				if(StringUtils.isBlank(RouteProtocol)) {
//					throw new Exception("RouteProtocol为空");
//				}else{
//					if(!RouteProtocol.equals("STATIC")&&!RouteProtocol.equals("BGP")){
//						throw new Exception("无效RouteProtocol");
//					}
//				}
//				if(RouteProtocol.equals("BGP")){
//					if(StringUtils.isBlank(BGPASNO)){
//						throw new Exception("BGPASNO为空");
//					}
//				}
//			}

//			if((ServType.equals("MPLSVPN") || ServType.equals("PONCLOUD") ||ServType.equals("PONCLOUD_MP")) && RouteProtocol.equals("STATIC")){
//				if(StringUtils.isBlank(UserInterIP1)) {
//					throw new Exception("UserInterIP1为空");
//				}else {
//					String isIPLegal = ipv4Format.validateSimpleIPv4(UserInterIP1);
//					if(isIPLegal.equals("0")) {
//						throw new Exception("UserInterIP1格式非法");
//					}
//				}
//
//				if(StringUtils.isBlank(UserIP)||StringUtils.isBlank(UserIPMask)) {
//					throw new Exception("UserIP/UserIPMask为空");
//				}else {
//					String isIPLegal = ipv4Format.validateSimpleIPv4(UserIP);
//					if(isIPLegal.equals("0")) {
//						throw new Exception("UserIP格式非法");
//					}
//					String isUserIPMaskLegal = ipv4Format.validateSimpleIPv4(UserIPMask);
//					if(isUserIPMaskLegal.equals("0")) {
//						throw new Exception("UserIPMask格式非法");
//					}
//				}
//			}

			if(StringUtils.isBlank(AGSWIP1)) {
				//throw new Exception("AGSWIP1为空");
			}else {
				List<String> devIdList = resClient.getdevidbydevip(AGSWIP1);
				if(CollectionUtils.isEmpty(devIdList)){
					//throw new Exception("AGSWIP1在网管查询不到");
				}else if(devIdList.size()!=1) {
					//throw new Exception("AGSWIP1找到多个对应设备");
				}else {
					AGSWIP1DevId = devIdList.get(0);
				}
			}
//			if(StringUtils.isBlank(AGSWUpPort1)) {
//				throw new Exception("AGSWUpPort1为空!");
//			}else {
//				String Flag = resClient.portifexist(AGSWIP1DevId, AGSWUpPort1);
//				if(Flag.equals("N")) {
//					throw new Exception("AGSWUpPort1不存在");
//				}
//			}
//			if(StringUtils.isBlank(AGSWDownPort1)) {
////				throw new Exception("AGSWDownPort1为空!");
//			}else {
//				String Flag = resClient.portifexist(AGSWIP1DevId, AGSWDownPort1);
//				if(Flag.equals("N")) {
//					throw new Exception("AGSWDownPort1不存在");
//				}
//			}

			if(OperType.equals("add")||OperType.equals("del")){
				if(VRRP.equals("Y")) {
					if(OperType.equals("add")) {
//						if(StringUtils.isBlank(VRRPGrpNO)) {
//							throw new Exception("VRRPGrpNO为空!");
//						}
//						if(StringUtils.isBlank(VRRPPriority1)) {
//							throw new Exception("VRRPPriority1为空!");
//						}
//						if(StringUtils.isBlank(VRRPPriority2)) {
//							throw new Exception("VRRPPriority2为空!");
//						}

//						if(StringUtils.isBlank(VRRPvGetway)) {
//							throw new Exception("VRRPvGetway为空!");
//						}
//						if(StringUtils.isBlank(InterIP2)||StringUtils.isBlank(InterIP2Mask)) {
//							throw new Exception("InterIP2/InterIP2Mask为空");
//						}else {
//							String isIPLegal = ipv4Format.validateSimpleIPv4(InterIP2);
//							if(isIPLegal.equals("0")) {
//								throw new Exception("InterIP2格式非法");
//							}
//							String isInterIP2MaskLegal = ipv4Format.validateSimpleIPv4(InterIP2Mask);
//							if(isInterIP2MaskLegal.equals("0")) {
//								throw new Exception("InterIP2Mask格式非法");
//							}
//						}

//						if(ServType.equals("IPSL")){
//							if(StringUtils.isBlank(VRRPPVLAN)) {
//								throw new Exception("VRRPPVLAN为空!");
//							}
//							if(StringUtils.isBlank(VRRPCVLAN)) {
//								throw new Exception("VRRPCVLAN为空!");
//							}
//						}

					}
					if(StringUtils.isBlank(SR2IP)) {
						//throw new Exception("SR2IP为空!");
					}else {
						List<String> devIdList = resClient.getdevidbydevip(SR2IP);
						if(CollectionUtils.isEmpty(devIdList)){
							//throw new Exception("SR2IP在网管查询不到");
						}else if(devIdList.size()!=1) {
							//throw new Exception("SR2IP找到多个对应设备");
						}else {
							SR2IPDevId = devIdList.get(0);
						}
					}
					if(StringUtils.isBlank(SR2Port)) {
						//throw new Exception("SR2Port为空!");
					}else {
//						SR2PortVal=SR2Port.substring(0, SR2Port.indexOf("."));
//						String Flag = resClient.portifexist(SR1IPDevId, SR2PortVal);
//						if(Flag.equals("N")) {
//							throw new Exception("SR2Port不存在");
//						}
					}
//					if((ServType.equals("MPLSVPN") || ServType.equals("PONCLOUD")||ServType.equals("PONCLOUD_MP")) && RouteProtocol.equals("STATIC")){
//						if(StringUtils.isBlank(UserInterIP2)) {
//							throw new Exception("UserInterIP2为空!");
//						}else {
//							String isIPLegal = ipv4Format.validateSimpleIPv4(UserInterIP2);
//							if(isIPLegal.equals("0")) {
//								throw new Exception("UserInterIP2格式非法");
//							}
//						}
//					}
					if(StringUtils.isBlank(AGSWIP2)) {
						//throw new Exception("AGSWIP2为空!");
					}else {
						List<String> devIdList = resClient.getdevidbydevip(AGSWIP2);
						if(CollectionUtils.isEmpty(devIdList)){
							//throw new Exception("AGSWIP2在网管查询不到");
						}else if(devIdList.size()!=1) {
							//throw new Exception("AGSWIP2找到多个对应设备");
						}else {
							AGSWIP2DevId = devIdList.get(0);
						}
					}
					if(StringUtils.isBlank(AGSWUpPort2)) {
						//throw new Exception("AGSWUpPort2为空!");
					}else {
//						String Flag = resClient.portifexist(AGSWIP2DevId, AGSWUpPort2);
//						if(Flag.equals("N")) {
//							throw new Exception("AGSWUpPort2不存在");
//						}
					}
					if(StringUtils.isBlank(AGSWDownPort2)) {
//						throw new Exception("AGSWDownPort2为空!");
					}else {
//						String Flag = resClient.portifexist(AGSWIP2DevId, AGSWDownPort2);
//						if(Flag.equals("N")) {
//							throw new Exception("AGSWDownPort2不存在");
//						}
					}
				}
			}

//			if(StringUtils.isBlank(AGSWUpPort1)){
//				throw new Exception("AGSWUpPort1为空!");
//			}
//			if(StringUtils.isBlank(AGSWBridgePort1)){
//				throw new Exception("AGSWBridgePort1为空!");
//			}
//			if(StringUtils.isBlank(AGSWDownPort1)){
//				throw new Exception("AGSWDownPort1为空!");
//			}

//			if(VRRP.equals("Y")){
//				if(StringUtils.isBlank(AGSWUpPort2)){
//					throw new Exception("AGSWUpPort2为空!");
//				}
//				if(StringUtils.isBlank(AGSWBridgePort2)){
//					throw new Exception("AGSWBridgePort2为空!");
//				}
//			}


		}//PON接入IP侧校验结束

		if(ServModel.equals("PON")) {  //PON接入PON侧
//			if(OperType.equals("add")||OperType.equals("del")) {
//				if(StringUtils.isBlank(VRRP)) {
//					throw new Exception("VRRP为空!");
//				}else {
//					if(!VRRP.equals("Y")&&!VRRP.equals("N")) {
//						throw new Exception("无效VRRP!");
//					}
//				}
//
//				if(StringUtils.isBlank(PVLAN)) {
//					throw new Exception("PVLAN为空!");
//				}
//
//				if(StringUtils.isBlank(ONUVLAN)) {
//					throw new Exception("ONUVLAN为空!");
//				}
//
//			}

//			if(ServType.equals("IPSL") && (OperType.equals("add")||OperType.equals("del"))){
//				if(StringUtils.isBlank(CVLAN)) {
//					throw new Exception("CVLAN为空");
//				}
//			}
//
//			if(OperType.equals("add")||OperType.equals("modspeed")){
//				if(StringUtils.isBlank(BindWidth)) {
//					throw new Exception("BindWidth为空!");
//				}
//			}

			if(OperType.equals("add")) {

//				if(StringUtils.isBlank(PONUSERVLAN)) {
//					throw new Exception("PONUSERVLAN为空!");
//				}
//				if(StringUtils.isBlank(SRPortDesc)) {
//					throw new Exception("SRPortDesc为空!");
//				}

//				if(ServType.equals("MPLSVPN")){
//					if(StringUtils.isBlank(VRFDesc)) {
//						throw new Exception("VRFDesc为空");
//					}
//					if(StringUtils.isBlank(RD)) {
//						throw new Exception("RD为空");
//					}
//					if(StringUtils.isBlank(ExportRT)) {
//						throw new Exception("ExportRT为空");
//					}
//					if(StringUtils.isBlank(ImportRT)) {
//						throw new Exception("ImportRT为空");
//					}
//				}

//				if(StringUtils.isBlank(ONUGateWay)) {
//					throw new Exception("ONUGateWay为空!");
//				}else{
//					String isIPLegal = ipv4Format.validateSimpleIPv4(ONUGateWay);
//					if(isIPLegal.equals("0")) {
//						throw new Exception("ONUGateWay格式非法");
//					}
//				}
			}
//			if(StringUtils.isBlank(ONUSN)) {
//				throw new Exception("ONUSN为空!");
//			}

//			if(ServType.equals("MPLSVPN")) {
//				if(StringUtils.isBlank(VRFName)) {
//					throw new Exception("VRFName为空");
//				}
//				if(StringUtils.isBlank(RouteProtocol)) {
//					throw new Exception("RouteProtocol为空");
//				}
//			}

			if(StringUtils.isBlank(OLTIP)) {
				//throw new Exception("OLTIP为空!");
			}else {
				List<String> devIdList = resClient.getdevidbydevip(OLTIP);
				if(CollectionUtils.isEmpty(devIdList)){
					//throw new Exception("OLTIP在网管查询不到");
				}else if(devIdList.size()!=1) {
					//throw new Exception("OLTIP找到多个对应设备");
				}else {
					OLTIPDevId = devIdList.get(0);
				}
			}

			if(OperType.equals("add")||OperType.equals("del")) {
//				if(StringUtils.isBlank(OLTUpPort1)) {
//					throw new Exception("OLTUpPort1为空!");
//				}
				/*else {
					String Flag = resClient.portifexist(OLTIPDevId, OLTUpPort1);
					if(Flag.equals("N")) {
						throw new Exception("OLTUpPort1不存在");
					}
				}*/
			}

//			if(StringUtils.isBlank(OLTDownPort)) {
//				throw new Exception("OLTDownPort为空!");
//			}
//			else {
//				String Flag = resClient.portifexist(OLTIPDevId, OLTDownPort);
//				if(Flag.equals("N")) {
//					throw new Exception("OLTDownPort不存在");
//				}
//			}
//			if(StringUtils.isBlank(ONUIP)) {
//				throw new Exception("ONUIP为空!");
//			}

//			if(OperType.equals("add")||OperType.equals("del")) {
//				if(StringUtils.isBlank(ONUUpPort)) {
//					throw new Exception("ONUUpPort为空!");
//				}
//			}
//
//			if(StringUtils.isBlank(ONUDownPort)) {
//				throw new Exception("ONUDownPort为空!");
//			}

//			if(OperType.equals("add")||OperType.equals("del")) {
//				if(VRRP.equals("Y")) {
//					if(StringUtils.isBlank(OLTUpPort2)) {
//						throw new Exception("OLTUpPort2为空!");
//					}
//					else {
//						String Flag = resClient.portifexist(OLTIPDevId, OLTUpPort2);
//						if(Flag.equals("N")) {
//							throw new Exception("OLTUpPort2不存在");
//						}
//					}

//				}
//			}
		}//PON接入PON侧校验结束

		if(ServModel.equals("SW")) {   //SW接入校验
			if(OperType.equals("add")||OperType.equals("del")) {
//				if (StringUtils.isBlank(VRRP)) {
//					throw new Exception("VRRP为空!");
//				} else {
//					if (!VRRP.equals("Y") && !VRRP.equals("N")) {
//						throw new Exception("无效VRRP!");
//					}
//				}
//
//				if (StringUtils.isBlank(PVLAN)) {
//					throw new Exception("PVLAN为空!");
//				}

				if(StringUtils.isBlank(SR1IP)) {
					//throw new Exception("SR1IP为空!");
				}else {
					List<String> dataList = resClient.getdevidbydevip(SR1IP);
					if(CollectionUtils.isEmpty(dataList)){
						//throw new Exception("SR1IP在网管查询不到");
					}else if(dataList.size()!=1) {
						//throw new Exception("SR1IP找到多个对应设备");
					}else {
						SR1IPDevId = dataList.get(0);
					}
				}

				if(StringUtils.isBlank(SR1Port)) {
					//throw new Exception("SR1Port为空!");
				}else {
					SR1PortVal=SR1Port.substring(0, SR1Port.indexOf("."));
//					String Flag = resClient.portifexist(SR1IPDevId, SR1PortVal);
//					if(Flag.equals("N")) {
//						throw new Exception("SR1Port不存在");
//					}
				}

				if(StringUtils.isBlank(AGSWIP1)) {
					//throw new Exception("AGSWIP1为空!");
				}else {
					List<String> devIdList = resClient.getdevidbydevip(AGSWIP1);
					if(CollectionUtils.isEmpty(devIdList)){
						//throw new Exception("AGSWIP1在网管查询不到");
					}else if(devIdList.size()!=1) {
						//throw new Exception("AGSWIP1找到多个对应设备");
					}else {
						AGSWIP1DevId = devIdList.get(0);
					}
				}

//				if(StringUtils.isBlank(AGSWUpPort1)) {
//					throw new Exception("AGSWUpPort1为空!");
//				}else {
//					String Flag = resClient.portifexist(AGSWIP1DevId, AGSWUpPort1);
//					if(Flag.equals("N")) {
//						throw new Exception("AGSWUpPort1不存在");
//					}
//				}
//				if(StringUtils.isBlank(AGSWDownPort1)) {
////					throw new Exception("AGSWDownPort1为空!");
//				}else {
//					String Flag = resClient.portifexist(AGSWIP1DevId, AGSWDownPort1);
//					if(Flag.equals("N")) {
//						throw new Exception("AGSWDownPort1不存在");
//					}
//				}

			}

//			if(OperType.equals("add") || OperType.equals("modspeed")){
//				if(StringUtils.isBlank(BindWidth)) {
//					throw new Exception("BindWidth为空");
//				}
//			}

			if(OperType.equals("add")){
//				if(StringUtils.isBlank(InterIP1)||StringUtils.isBlank(InterIP1Mask)) {
//					throw new Exception("InterIP1/InterIP1Mask为空");
//				}else {
//					String isIPLegal = ipv4Format.validateSimpleIPv4(InterIP1);
//					if(isIPLegal.equals("0")) {
//						throw new Exception("InterIP1格式非法");
//					}
//					String isInterIP1MaskLegal = ipv4Format.validateSimpleIPv4(InterIP1Mask);
//					if(isInterIP1MaskLegal.equals("0")) {
//						throw new Exception("InterIP1Mask格式非法");
//					}
//				}
//				if(StringUtils.isBlank(SRPortDesc)) {
//					throw new Exception("SRPortDesc为空");
//				}


//				if(ServType.equals("MPLSVPN") || ServType.equals("PONCLOUD")||ServType.equals("PONCLOUD_MP")){
//					if(StringUtils.isBlank(VRFDesc)) {
//						throw new Exception("VRFDesc为空");
//					}
//					if(StringUtils.isBlank(RD)) {
//						throw new Exception("RD为空");
//					}
//					if(StringUtils.isBlank(ExportRT)) {
//						throw new Exception("ExportRT为空");
//					}
//					if(StringUtils.isBlank(ImportRT)) {
//						throw new Exception("ImportRT为空");
//					}
//				}
			}
//			if(ServType.equals("IPSL") && (OperType.equals("add")||OperType.equals("del"))){
//				if(StringUtils.isBlank(CVLAN)) {
//					throw new Exception("CVLAN为空");
//				}
//			}

			if(ServType.equals("MPLSVPN") || ServType.equals("PONCLOUD")||ServType.equals("PONCLOUD_MP")) {
//				if(OperType.equals("add")||OperType.equals("del")){
//					if(StringUtils.isBlank(VRFName)) {
//						throw new Exception("VRFName为空");
//					}
//				}

//				if(StringUtils.isBlank(RouteProtocol)) {
//					throw new Exception("RouteProtocol为空");
//				}else{
//					if(!RouteProtocol.equals("STATIC")&&!RouteProtocol.equals("BGP")){
//						throw new Exception("无效RouteProtocol");
//					}
//				}
//				if(RouteProtocol.equals("BGP") && (OperType.equals("add")||OperType.equals("del"))){
//					if(StringUtils.isBlank(BGPASNO)){
//						throw new Exception("BGPASNO为空");
//					}
//				}
			}

			if((ServType.equals("MPLSVPN") || ServType.equals("PONCLOUD") ||ServType.equals("PONCLOUD_MP"))
					&& RouteProtocol.equals("STATIC") && (OperType.equals("add") || OperType.equals("del"))){
//				if(StringUtils.isBlank(UserInterIP1)) {
//					throw new Exception("UserInterIP1为空");
//				}else {
//					String isIPLegal = ipv4Format.validateSimpleIPv4(UserInterIP1);
//					if(isIPLegal.equals("0")) {
//						throw new Exception("UserInterIP1格式非法");
//					}
//				}

//				if(StringUtils.isBlank(UserIP)||StringUtils.isBlank(UserIPMask)) {
//					throw new Exception("UserIP/UserIPMask为空");
//				}else {
//					String isIPLegal = ipv4Format.validateSimpleIPv4(UserIP);
//					if(isIPLegal.equals("0")) {
//						throw new Exception("UserIP格式非法");
//					}
//					String isUserIPMaskLegal = ipv4Format.validateSimpleIPv4(UserIPMask);
//					if(isUserIPMaskLegal.equals("0")) {
//						throw new Exception("UserIPMask格式非法");
//					}
//				}
			}

			if(StringUtils.isBlank(ACCSWIP)) {
				//throw new Exception("ACCSWIP为空!");
			}else {
				List<String> devIdList = resClient.getdevidbydevip(ACCSWIP);
				if(CollectionUtils.isEmpty(devIdList)){
					//throw new Exception("ACCSWIP在网管查询不到");
				}else if(devIdList.size()!=1) {
					//throw new Exception("ACCSWIP找到多个对应设备");
				}else {
					ACCSWIPDevId = devIdList.get(0);
				}
			}
//			if(StringUtils.isBlank(ACCSWIPUpPort1)) {
//				throw new Exception("ACCSWIPUpPort1为空!");
//			}else {
//				String Flag = resClient.portifexist(ACCSWIPDevId, ACCSWIPUpPort1);
//				if(Flag.equals("N")) {
//					throw new Exception("ACCSWIPUpPort1不存在");
//				}
//			}
//			if(StringUtils.isBlank(ACCSWIPDownPort1)) {
//				throw new Exception("ACCSWIPDownPort1为空!");
//			}else {
//				String Flag = resClient.portifexist(ACCSWIPDevId, ACCSWIPDownPort1);
//				if(Flag.equals("N")) {
//					throw new Exception("ACCSWIPDownPort1不存在");
//				}
//			}

			if(OperType.equals("add")||OperType.equals("del")) {
				if(VRRP.equals("Y")) {
					if(OperType.equals("add")){
//						if(StringUtils.isBlank(VRRPGrpNO)) {
//							throw new Exception("VRRPGrpNO为空");
//						}
//						if(StringUtils.isBlank(VRRPPriority1)) {
//							throw new Exception("VRRPPriority1为空");
//						}
//						if(StringUtils.isBlank(VRRPPriority2)) {
//							throw new Exception("VRRPPriority2为空");
//						}
//						if(StringUtils.isBlank(VRRPvGetway)) {
//							throw new Exception("VRRPvGetway为空");
//						}
//						if(StringUtils.isBlank(InterIP2)||StringUtils.isBlank(InterIP2Mask)) {
//							throw new Exception("InterIP2/InterIP2Mask为空");
//						}else {
//							String isIPLegal = ipv4Format.validateSimpleIPv4(InterIP2);
//							if(isIPLegal.equals("0")) {
//								throw new Exception("InterIP2格式非法");
//							}
//							String isInterIP2MaskLegal = ipv4Format.validateSimpleIPv4(InterIP2Mask);
//							if(isInterIP2MaskLegal.equals("0")) {
//								throw new Exception("InterIP2Mask格式非法");
//							}
//						}
//
//						if(ServType.equals("IPSL")){
//							if(StringUtils.isBlank(VRRPPVLAN)) {
//								throw new Exception("VRRPPVLAN为空!");
//							}
//							if(StringUtils.isBlank(VRRPCVLAN)) {
//								throw new Exception("VRRPCVLAN为空!");
//							}
//						}
					}
					if(StringUtils.isBlank(SR2IP)) {
						//throw new Exception("SR2IP为空");
					}else {
						List<String> devIdList = resClient.getdevidbydevip(SR2IP);
						if(CollectionUtils.isEmpty(devIdList)){
							//throw new Exception("SR2IP在网管查询不到");
						}else if(devIdList.size()!=1) {
							//throw new Exception("SR2IP找到多个对应设备");
						}else {
							SR2IPDevId = devIdList.get(0);
						}
					}
					if(StringUtils.isBlank(SR2Port)) {
						//throw new Exception("SR2Port为空!");
					}else {
						SR2PortVal=SR2Port.substring(0, SR2Port.indexOf("."));
//						String Flag = resClient.portifexist(SR1IPDevId, SR2PortVal);
//						if(Flag.equals("N")) {
//							throw new Exception("SR2Port不存在");
//						}
					}

//					if((ServType.equals("MPLSVPN") || ServType.equals("PONCLOUD")||ServType.equals("PONCLOUD_MP")) && RouteProtocol.equals("STATIC")){
//						if(StringUtils.isBlank(UserInterIP2)) {
//							throw new Exception("UserInterIP2为空");
//						}else {
//							String isIPLegal = ipv4Format.validateSimpleIPv4(UserInterIP2);
//							if(isIPLegal.equals("0")) {
//								throw new Exception("UserInterIP2格式非法");
//							}
//						}
//					}

					if(StringUtils.isBlank(AGSWIP2)) {
						//throw new Exception("AGSWIP2为空");
					}else {
						List<String> devIdList = resClient.getdevidbydevip(AGSWIP2);
						if(CollectionUtils.isEmpty(devIdList)){
							//throw new Exception("AGSWIP2在网管查询不到");
						}else if(devIdList.size()!=1) {
							//throw new Exception("AGSWIP2找到多个对应设备");
						}else {
							AGSWIP2DevId = devIdList.get(0);
						}
					}
//					if(StringUtils.isBlank(AGSWUpPort2)) {
//						throw new Exception("AGSWUpPort2为空!");
//					}else {
//						String Flag = resClient.portifexist(AGSWIP2DevId, AGSWUpPort2);
//						if(Flag.equals("N")) {
//							throw new Exception("AGSWUpPort2不存在");
//						}
//					}
//					if(StringUtils.isBlank(AGSWDownPort2)) {
////						throw new Exception("AGSWDownPort2为空!");
//					}else {
//						String Flag = resClient.portifexist(AGSWIP2DevId, AGSWDownPort2);
//						if(Flag.equals("N")) {
//							throw new Exception("AGSWDownPort2不存在");
//						}
//					}
//					if(StringUtils.isBlank(ACCSWIPUpPort2)) {
//						throw new Exception("ACCSWIPUpPort2为空!");
//					}else {
//						String Flag = resClient.portifexist(ACCSWIPDevId, ACCSWIPUpPort2);
//						if(Flag.equals("N")) {
//							throw new Exception("ACCSWIPUpPort2不存在");
//						}
//					}
				}
			}
		}//SW接入校验结束

		if(ServModel.equals("PE")) {   //PE配置校验
			if(ServType.equals("PONCLOUD") || ServType.equals("PTNCLOUD")||ServType.equals("PONCLOUD_MP")) {
//				if(StringUtils.isBlank(VRFName)) {
//					throw new Exception("VRFName为空");
//				}
//
//				if(OperType.equals("add")) {
//					if(StringUtils.isBlank(VRFDesc)) {
//						throw new Exception("VRFDesc为空");
//					}
//					if(StringUtils.isBlank(RD)) {
//						throw new Exception("RD为空");
//					}
//					if(StringUtils.isBlank(ExportRT)) {
//						throw new Exception("ExportRT为空");
//					}
//					if(StringUtils.isBlank(ImportRT)) {
//						throw new Exception("ImportRT为空");
//					}
//				}

//				if(StringUtils.isBlank(BGPASNO)) {
//					throw new Exception("BGPASNO为空");
//				}

				if(StringUtils.isBlank(PE1IP)){
					//throw new Exception("PE1IP为空");
				}else{
					List<String> devIdList = resClient.getdevidbydevip(PE1IP);
					if(CollectionUtils.isEmpty(devIdList)) {
						//throw new Exception("PE1IP在网管查询不到");
					}else if(devIdList.size()!=1){
						//throw new Exception("PE1IP找到多个对应设备");
					}else{
						PE1IPDevID = devIdList.get(0);
					}
				}

//				if(StringUtils.isBlank(PE1Port)){
//					throw new Exception("PE1Port为空");
//				}else{
//					String peport = PE1Port;
//					if(peport.contains(".")){
//						int index = peport.indexOf(".");
//						peport = peport.substring(0,index);
//					}
//					String Flag = resClient.portifexist(PE1IPDevID, peport);
//					if(Flag.equals("N")) {
//						throw new Exception("PE1Port不存在");
//					}
//				}

//				if(OperType.equals("add")){
//					if(StringUtils.isBlank(PE1PortDesc)){
//						throw new Exception("PE1PortDesc为空");
//					}
//					if(StringUtils.isBlank(PE1InterIP)){
//						throw new Exception("PE1InterIP为空");
//					}
//					if(StringUtils.isBlank(PE1InterIPMask)){
//						throw new Exception("PE1InterIPMask为空");
//					}
//					if(StringUtils.isBlank(PE1VLAN)){
//						throw new Exception("PE1VLAN为空");
//					}
//
//					String isPE1IPLegal = ipv4Format.validateSimpleIPv4(PE1InterIP);
//					if(isPE1IPLegal.equals("0")) {
//						throw new Exception("PE1InterIP格式非法");
//					}
//
//					String pe1InterIPMaskLegal = ipv4Format.validateSimpleIPv4(PE1InterIPMask);
//					if(pe1InterIPMaskLegal.equals("0")) {
//						throw new Exception("PE1InterIPMask格式非法");
//					}
//				}

//				if(StringUtils.isBlank(PE1NextHopIP)){
//					throw new Exception("PE1NextHopIP为空");
//				}else{
//					String isPE1IPLegal = ipv4Format.validateSimpleIPv4(PE1NextHopIP);
//					if(isPE1IPLegal.equals("0")) {
//						throw new Exception("PE1NextHopIP格式非法");
//					}
//				}

//				if(StringUtils.isBlank(CloudIP)){
//					throw new Exception("CloudIP为空");
//				}
//				String isCloudIPLegal = ipv4Format.validateSimpleIPv4(CloudIP);
//				if(isCloudIPLegal.equals("0")) {
//					throw new Exception("CloudIP格式非法");
//				}
//				if(StringUtils.isBlank(CloudIPMask)){
//					throw new Exception("CloudIPMask为空");
//				}
//				String isCloudIPMaskLegal = ipv4Format.validateSimpleIPv4(CloudIPMask);
//				if(isCloudIPMaskLegal.equals("0")) {
//					throw new Exception("CloudIPMask格式非法");
//				}
			}

			if(ServType.equals("PTNCLOUD")){
//				if(StringUtils.isBlank(RouteProtocol)){
//					throw new Exception("RouteProtocol为空");
//				}else{
//					if(!RouteProtocol.equals("STATIC")&&!RouteProtocol.equals("BGP")){
//						throw new Exception("无效RouteProtocol");
//					}
//				}
//				if(StringUtils.isBlank(PE1DownPort)){
//					throw new Exception("PE1DownPort为空");
//				}else{
//					String peport = PE1DownPort;
//					if(peport.contains(".")){
//						int index = peport.indexOf(".");
//						peport = peport.substring(0,index);
//					}
//					String Flag = resClient.portifexist(PE1IPDevID, peport);
//					if(Flag.equals("N")) {
//						throw new Exception("PE1DownPort不存在");
//					}
//				}

//				if(OperType.equals("add")){
//					if(StringUtils.isBlank(PE1DownPortDesc)){
//						throw new Exception("PE1DownPortDesc为空");
//					}
//					if(StringUtils.isBlank(PE1DownInterIP)){
//						throw new Exception("PE1DownInterIP为空");
//					}
//					if(StringUtils.isBlank(PE1DownInterIPMask)){
//						throw new Exception("PE1DownInterIPMask为空");
//					}
//					if(StringUtils.isBlank(PE1DownVLAN)){
//						throw new Exception("PE1DownVLAN为空");
//					}
//					String pE1DownInterIPResult = ipv4Format.validateSimpleIPv4(PE1DownInterIP);
//					if(pE1DownInterIPResult.equals("0")) {
//						throw new Exception("PE1DownInterIP格式非法");
//					}
//					String PE1DownInterIPMaskResult = ipv4Format.validateSimpleIPv4(PE1DownInterIPMask);
//					if(PE1DownInterIPMaskResult.equals("0")) {
//						throw new Exception("PE1DownInterIPMask格式非法");
//					}
//				}

//				if(StringUtils.isBlank(PE1DownNextHopIP)){
//					throw new Exception("PE1DownNextHopIP为空");
//				}else{
//					String PE1DownNextHopIPReslut = ipv4Format.validateSimpleIPv4(PE1DownNextHopIP);
//					if(PE1DownNextHopIPReslut.equals("0")) {
//						throw new Exception("PE1DownNextHopIP格式非法");
//					}
//				}

			}

//			if(ServType.equals("PONCLOUD")||ServType.equals("PONCLOUD_MP")){
				if(StringUtils.isBlank(PE2IP)){
					//throw new Exception("PE2IP为空");
				}else{
					List<String> devIdList = resClient.getdevidbydevip(PE2IP);
					if(CollectionUtils.isEmpty(devIdList)) {
						//throw new Exception("PE2IP在网管查询不到");
					}else if(devIdList.size()!=1){
						//throw new Exception("PE2IP找到多个对应设备");
					}else{
						PE2IPDevID = devIdList.get(0);
					}
				}
//			}
			if(ServType.equals("PONCLOUD") || (ServType.equals("PTNCLOUD") && StringUtils.isNotBlank(PE2IPDevID))
					||ServType.equals("PONCLOUD_MP") ){
//				if(StringUtils.isBlank(PE2Port)){
//					throw new Exception("PE2Port为空");
//				}else{
//					String peport = PE2Port;
//					if(peport.contains(".")){
//						int index = peport.indexOf(".");
//						peport = peport.substring(0,index);
//					}
//					String Flag = resClient.portifexist(PE2IPDevID, peport);
//					if(Flag.equals("N")) {
//						throw new Exception("PE2Port不存在");
//					}
//				}

			}

			if((ServType.equals("PONCLOUD") && OperType.equals("add")) ||
					(ServType.equals("PTNCLOUD") && StringUtils.isNotBlank(PE2IPDevID) && OperType.equals("add"))||
					(ServType.equals("PONCLOUD_MP") && OperType.equals("add"))

			){
//				if(StringUtils.isBlank(PE2PortDesc)){
//					throw new Exception("PE2PortDesc为空");
//				}
//				if(StringUtils.isBlank(PE2InterIP)){
//					throw new Exception("PE2InterIP为空");
//				}
//				if(StringUtils.isBlank(PE2InterIPMask)){
//					throw new Exception("PE2InterIPMask为空");
//				}
//				if(StringUtils.isBlank(PE2VLAN)){
//					throw new Exception("PE2VLAN为空");
//				}
//				String isPE2InterIPLegal = ipv4Format.validateSimpleIPv4(PE2InterIP);
//				if(isPE2InterIPLegal.equals("0")) {
//					throw new Exception("PE2InterIP格式非法");
//				}
//
//				String isPE2InterIPMaskegal = ipv4Format.validateSimpleIPv4(PE2InterIPMask);
//				if(isPE2InterIPMaskegal.equals("0")) {
//					throw new Exception("PE2InterIPMask格式非法");
//				}
			}
			if(ServType.equals("PONCLOUD") || (ServType.equals("PTNCLOUD") && StringUtils.isNotBlank(PE2IPDevID)) ||ServType.equals("PONCLOUD_MP")){
//				if(StringUtils.isBlank(PE2NextHopIP)){
//					throw new Exception("PE2NextHopIP为空");
//				}else{
//					String isPE2IPLegal = ipv4Format.validateSimpleIPv4(PE2NextHopIP);
//					if(isPE2IPLegal.equals("0")) {
//						throw new Exception("PE2NextHopIP格式非法");
//					}
//				}
			}

			if(ServType.equals("PTNCLOUD") && StringUtils.isNotBlank(PE2IPDevID)){
//				if(StringUtils.isBlank(PE2DownPort)){
//					throw new Exception("PE2DownPort为空");
//				}else{
//					String peport = PE2DownPort;
//					if(peport.contains(".")){
//						int index = peport.indexOf(".");
//						peport = peport.substring(0,index);
//					}
//					String Flag = resClient.portifexist(PE2IPDevID, peport);
//					if(Flag.equals("N")) {
//						throw new Exception("PE2DownPort不存在");
//					}
//				}

//				if(OperType.equals("add")){
//					if(StringUtils.isBlank(PE2DownPortDesc)){
//						throw new Exception("PE2DownPortDesc为空");
//					}
//					if(StringUtils.isBlank(PE2DownInterIP)){
//						throw new Exception("PE2DownInterIP为空");
//					}
//					if(StringUtils.isBlank(PE2DownInterIPMask)){
//						throw new Exception("PE2DownInterIPMask为空");
//					}
//					if(StringUtils.isBlank(PE2DownVLAN)){
//						throw new Exception("PE2DownVLAN为空");
//					}
//					String PE2DownInterIPResult = ipv4Format.validateSimpleIPv4(PE2DownInterIP);
//					if(PE2DownInterIPResult.equals("0")) {
//						throw new Exception("PE2DownInterIP格式非法");
//					}
//					String PE2DownInterIPMaskResult = ipv4Format.validateSimpleIPv4(PE2DownInterIPMask);
//					if(PE2DownInterIPMaskResult.equals("0")) {
//						throw new Exception("PE2DownInterIPMask格式非法");
//					}
//
//					if(StringUtils.isBlank(PE2DownNextHopIP)){
//						throw new Exception("PE2DownNextHopIP为空");
//					}else{
//						String PE2DownNextHopIPReslut = ipv4Format.validateSimpleIPv4(PE2DownNextHopIP);
//						if(PE2DownNextHopIPReslut.equals("0")) {
//							throw new Exception("PE2DownNextHopIP格式非法");
//						}
//					}
//				}
			}

			if(ServType.equals("PTNCLOUD")){
//				if(RouteProtocol.equals("BGP")){
//					if(StringUtils.isBlank(UserBGPASNO)){
//						throw new Exception("UserBGPASNO为空");
//					}
//				}
//
//				if(RouteProtocol.equals("STATIC")){
//
//					if(StringUtils.isBlank(UserIP)){
//						throw new Exception("UserIP为空");
//					}else{
//						String UserIPReslut = ipv4Format.validateSimpleIPv4(UserIP);
//						if(UserIPReslut.equals("0")) {
//							throw new Exception("UserIP格式非法");
//						}
//					}
//					if(StringUtils.isBlank(UserIPMask)){
//						throw new Exception("UserIPMask为空");
//					}else{
//						String UserIPMaskReslut = ipv4Format.validateSimpleIPv4(UserIPMask);
//						if(UserIPMaskReslut.equals("0")) {
//							throw new Exception("UserIPMask格式非法");
//						}
//					}
//				}
			}

			if(ServType.equals("PONCLOUD_MP")){

//				if(StringUtils.isBlank(NETPE1IP)){
//					throw new Exception("NETPE1IP为空");
//				}else if(ipv4Format.validateSimpleIPv4(NETPE1IP).equals("0")){
//					throw new Exception("NETPE1IP格式非法");
//				}
//
//				if(StringUtils.isBlank(NETPE2IP)){
//					throw new Exception("NETPE2IP为空");
//				}else if(ipv4Format.validateSimpleIPv4(NETPE2IP).equals("0")){
//					throw new Exception("NETPE2IP格式非法");
//				}
//
//				if(OperType.equals("add")){
//					String[]  PEnotnull = {"PE1Port2","PE1PortDesc2","PE1InterIP2","PE1InterIPMask2","PE1VLAN2","PE1NextHopIP2","PE2Port2","PE2PortDesc2","PE2InterIP2",
//							"PE2InterIPMask2","PE2VLAN2","PE2NextHopIP2"};
//					for(int i=0;i<PEnotnull.length;i++){
//						String PEnotnullStr = PEnotnull[i];
//						String value = dealNull(param.getString(PEnotnullStr));
//						if(StringUtils.isBlank(value)){
//							throw new Exception(PEnotnullStr+"为空");
//						}else if((",PE1InterIP2,PE1InterIPMask2,PE1NextHopIP2,PE2InterIP2,PE2InterIPMask2,PE2NextHopIP2,").indexOf(","+PEnotnullStr+",")>-1){
//							String isLegal = ipv4Format.validateSimpleIPv4(value);
//							if(isLegal.equals("0")) {
//								throw new Exception(PEnotnullStr+"格式非法");
//							}
//						}
//					}
//					if(StringUtils.isBlank(BgpPassword)){
//						throw new Exception("BgpPassword为空");
//					}
//				}

//				if(OperType.equals("del")){
//					String[]  PEnotnull = {"PE1Port2","PE1NextHopIP2","PE2Port2","PE2NextHopIP2"};
//					for(int i=0;i<PEnotnull.length;i++){
//						String PEnotnullStr = PEnotnull[i];
//						String value = dealNull(param.getString(PEnotnullStr));
//						if(StringUtils.isBlank(value)){
//							throw new Exception(PEnotnullStr+"为空");
//						}else if((",PE1NextHopIP2,PE2NextHopIP2,").indexOf(","+PEnotnullStr+",")>-1){
//							String isLegal = ipv4Format.validateSimpleIPv4(value);
//							if(isLegal.equals("0")) {
//								throw new Exception(PEnotnullStr+"格式非法");
//							}
//						}
//					}
//				}


			}



		}//PE配置校验结束


		if(ServModel.equals("BASIP")||ServModel.equals("BAS")){	//PON接入(IP侧-BAS)校验

			if(ServType.equals("IPSL")){

				if(OperType.equals("add")){

				}
			}

			if(StringUtils.isBlank(BAS1IP)){
				//throw new Exception("BAS1IP为空");
			}else{
				List<String> devIdList = resClient.getdevidbydevip(BAS1IP);
				if(CollectionUtils.isEmpty(devIdList)) {
					//throw new Exception("BAS1IP在网管查询不到");
				}else if(devIdList.size()!=1){
					//throw new Exception("BAS1IP找到多个对应设备");
				}else{
					BAS1IPDevID = devIdList.get(0);
				}
			}


			if(StringUtils.isBlank(AGSWIP1)) {
				//throw new Exception("AGSWIP1为空!");
			}else {
				List<String> devIdList = resClient.getdevidbydevip(AGSWIP1);
				if(CollectionUtils.isEmpty(devIdList)) {
					//throw new Exception("AGSWIP1在网管查询不到");
				}else if(devIdList.size()!=1){
					//throw new Exception("AGSWIP1找到多个对应设备");
				}else{
					AGSWIP1DevId = devIdList.get(0);
				}
			}


			if(!StringUtils.isBlank(BAS2IP)){
				List<String> devIdList = resClient.getdevidbydevip(BAS2IP);
				if(CollectionUtils.isEmpty(devIdList)) {
					//throw new Exception("BAS2IP在网管查询不到");
				}else if(devIdList.size()!=1){
					//throw new Exception("BAS2IP找到多个对应设备");
				}else{
					BAS2IPDevID = devIdList.get(0);
				}
			}


			if(ServType.equals("IPSL") && StringUtils.isNotBlank(BAS2IP)){

				if(StringUtils.isBlank(AGSWIP2)) {
					//throw new Exception("AGSWIP2为空!");
				}else {
					List<String> devIdList = resClient.getdevidbydevip(AGSWIP2);
					if(CollectionUtils.isEmpty(devIdList)) {
						//throw new Exception("AGSWIP2在网管查询不到");
					}else if(devIdList.size()!=1){
						//throw new Exception("AGSWIP2找到多个对应设备");
					}else{
						AGSWIP2DevId = devIdList.get(0);
					}
				}

			}

		} //PON接入(IP侧-BAS)校验结束

		if(ServModel.equals("BSW")) {     //SW接入(BAS) 校验

			if(StringUtils.isBlank(BAS1IP)){
				//throw new Exception("BAS1IP为空");
			}else{
				List<String> devIdList = resClient.getdevidbydevip(BAS1IP);
				if(CollectionUtils.isEmpty(devIdList)) {
					//throw new Exception("BAS1IP在网管查询不到");
				}else if(devIdList.size()!=1){
					//throw new Exception("BAS1IP找到多个对应设备");
				}else{
					BAS1IPDevID = devIdList.get(0);
				}
			}


			if(StringUtils.isBlank(AGSWIP1)) {
				//throw new Exception("AGSWIP1为空!");
			}else {
				List<String> devIdList = resClient.getdevidbydevip(AGSWIP1);
				if(CollectionUtils.isEmpty(devIdList)) {
					//throw new Exception("AGSWIP1在网管查询不到");
				}else if(devIdList.size()!=1){
					//throw new Exception("AGSWIP1找到多个对应设备");
				}else{
					AGSWIP1DevId = devIdList.get(0);
				}
			}


			if(StringUtils.isBlank(ACCSWIP)) {
				//throw new Exception("ACCSWIP为空!");
			}else {
				List<String> devIdList = resClient.getdevidbydevip(ACCSWIP);
				if(CollectionUtils.isEmpty(devIdList)) {
					//throw new Exception("ACCSWIP在网管查询不到");
				}else if(devIdList.size()!=1){
					//throw new Exception("ACCSWIP找到多个对应设备");
				}else{
					ACCSWIPDevId = devIdList.get(0);
				}
			}


			if(!StringUtils.isBlank(BAS2IP)){
				List<String> devIdList = resClient.getdevidbydevip(BAS2IP);
				if(CollectionUtils.isEmpty(devIdList)) {
					//throw new Exception("BAS2IP在网管查询不到");
				}else if(devIdList.size()!=1){
					//throw new Exception("BAS2IP找到多个对应设备");
				}else{
					BAS2IPDevID = devIdList.get(0);
				}
			}

			if(ServType.equals("IPSL") && StringUtils.isNotBlank(BAS2IP)) {

				if(StringUtils.isBlank(AGSWIP2)) {
					//throw new Exception("AGSWIP2为空!");
				}else {
					List<String> devIdList = resClient.getdevidbydevip(AGSWIP2);
					if(CollectionUtils.isEmpty(devIdList)) {
						//throw new Exception("AGSWIP2在网管查询不到");
					}else if(devIdList.size()!=1){
						//throw new Exception("AGSWIP2找到多个对应设备");
					}else{
						AGSWIP2DevId = devIdList.get(0);
					}
				}
			}
		}

		try {
			SaveWsReq saveWsReq = new SaveWsReq();
			//工单保存，远程调用获取工单号 .
			/*通过httpProxyService调用远程服务获取工单*/
			logger.info("远程调用服务获取工单 is start......");
			String wsnbr=null;
			RpcRestResponse<String> wsnbrResponse = wsManagerRpc.getWsnbr();
			wsnbr = wsnbrResponse.getData();
			logger.info("wsnbr====>{}",wsnbr);
			if (wsnbr.isEmpty()){
				throw new Exception(wsnbrResponse.getMsg());
			}
			logger.info("远程调用服务获取工单 is end......");
			saveWsReq.setWsnbr(wsnbr);
			saveWsReq.setServTypeId(ServType);
			saveWsReq.setOperType(OperType);
			saveWsReq.setWorksheetcode(WsCode);
			String username = param.getString("user_name");
			//添加创建人.没有默认为admin.
			if (StringUtils.isBlank(username)){
				saveWsReq.setCreateUser("admin");
			}else {
				saveWsReq.setCreateUser(username);
			}
			String servModelPre = "";
			if(ServType.equals("IPSL")){
				servModelPre = "IPSL_";
			}else if(ServType.equals("MPLSVPN")) {
				servModelPre = "MPLSVPN_";
			}else if(ServType.equals("PONCLOUD")){
				servModelPre = "PONCLOUD_";
			}else if(ServType.equals("PTNCLOUD")){
				servModelPre = "PTNCLOUD_";
			}else if(ServType.equals("PONCLOUD_MP")){
				servModelPre = "PONCLOUD_MP_";
			}
			if(ServModel.equals("SRIP")) {
				saveWsReq.setServModelId(servModelPre+"SRIP");
			}else if(ServModel.equals("PON")) {
				saveWsReq.setServModelId(servModelPre+"PON");
			}else if(ServModel.equals("SW")){
				saveWsReq.setServModelId(servModelPre+"SW");
			}else if(ServModel.equals("PE")){
				saveWsReq.setServModelId(servModelPre+"PE");
			}else if(ServModel.equals("BASIP")){
				saveWsReq.setServModelId(servModelPre+"BASIP");
			}else if(ServModel.equals("BSW")){
				saveWsReq.setServModelId(servModelPre+"BSW");
			}else if (servModelPre.equals("CRBRAS")){
				saveWsReq.setServModelId(servModelPre+"CRBRAS");
			} else {
				saveWsReq.setServModelId(servModelPre+ServModel);
			}

			List<BmBizWorksheetext> wsItemList = new ArrayList<>();
			for (Map.Entry<String, Object> entry : param.entrySet()) {
				Object val=entry.getValue();
				String Key = entry.getKey();
				if(WORKSHEET_EXT_MAP.containsKey(Key) && !String.valueOf(val).equals("")){
					BmBizWorksheetext wsItem = new BmBizWorksheetext();
					wsItem.setWsItemCode(WORKSHEET_EXT_MAP.get(Key));
					wsItem.setWsItemValue(String.valueOf(val));
					wsItemList.add(wsItem);
					if (!Key.equalsIgnoreCase(WORKSHEET_EXT_MAP.get(Key))){
						BmBizWorksheetext wsItem1 = new BmBizWorksheetext();
						wsItem1.setWsItemCode(Key);
						wsItem1.setWsItemValue(String.valueOf(val));
						wsItemList.add(wsItem1);
					}
				}

			}
			if(!SR1IPDevId.equals("")){
				//设备IP对应的设备ID不为空，就将IP从请求参数中移除
				param.remove("SR1IP");
				BmBizWorksheetext sr1 = new BmBizWorksheetext();
				sr1.setWsItemCode("SR1");
				sr1.setWsItemValue(SR1IPDevId);
				wsItemList.add(sr1);
			}

			if(!SR1PortVal.equals("")){
				BmBizWorksheetext sr1_phyport = new BmBizWorksheetext();
				sr1_phyport.setWsItemCode("SR1_PhyPort");
				sr1_phyport.setWsItemValue(SR1PortVal);
				wsItemList.add(sr1_phyport);
			}

			if(!AGSWIP1DevId.equals("")){
				param.remove("AGSWIP1");
				BmBizWorksheetext hjsw1 = new BmBizWorksheetext();
				hjsw1.setWsItemCode("HJSW1");
				hjsw1.setWsItemValue(AGSWIP1DevId);
				wsItemList.add(hjsw1);
			}

			if(!SR2IPDevId.equals("")){
				param.remove("SR2IP");
				BmBizWorksheetext sr2 = new BmBizWorksheetext();
				sr2.setWsItemCode("SR2");
				sr2.setWsItemValue(SR2IPDevId);
				wsItemList.add(sr2);
			}

			if(!SR2PortVal.equals("")){
				BmBizWorksheetext sr2_phyport = new BmBizWorksheetext();
				sr2_phyport.setWsItemCode("SR2_PhyPort");
				sr2_phyport.setWsItemValue(SR2PortVal);
				wsItemList.add(sr2_phyport);
			}

			if(!"".equals(AGSWIP2DevId)){
				param.remove("AGSWIP2");
				BmBizWorksheetext hjsw2 = new BmBizWorksheetext();
				hjsw2.setWsItemCode("HJSW2");
				hjsw2.setWsItemValue(AGSWIP2DevId);
				wsItemList.add(hjsw2);
			}

			if(!OLTIPDevId.equals("")){
				param.remove("OLTIP");
				BmBizWorksheetext olt = new BmBizWorksheetext();
				olt.setWsItemCode("OLT");
				olt.setWsItemValue(OLTIPDevId);
				wsItemList.add(olt);
			}



			if(!ACCSWIPDevId.equals("")){
				param.remove("ACCSWIP");
				BmBizWorksheetext jrsw = new BmBizWorksheetext();
				jrsw.setWsItemCode("JRSW");
				jrsw.setWsItemValue(ACCSWIPDevId);
				wsItemList.add(jrsw);
			}

			if(!StringUtils.isBlank(PVLAN)) {
				String wsCode = "";
				if(ServType.equals("IPSL")){
					param.remove("PVLAN");
					wsCode = "PVLAN";
				}else if(ServType.equals("MPLSVPN") || ServType.equals("PONCLOUD")) {
					param.remove("VLAN");
					wsCode = "VLAN";
				}
				BmBizWorksheetext vlan = new BmBizWorksheetext();
				vlan.setWsItemCode(wsCode);
				vlan.setWsItemValue(PVLAN);
				wsItemList.add(vlan);
			}

			if(!StringUtils.isBlank(PE1IPDevID)){
				param.remove("PE1IP");
				BmBizWorksheetext pe1Dev = new BmBizWorksheetext();
				pe1Dev.setWsItemCode("PE1");
				pe1Dev.setWsItemValue(PE1IPDevID);
				wsItemList.add(pe1Dev);
			}

			if(!StringUtils.isBlank(PE2IPDevID)){
				param.remove("PE2IP");
				BmBizWorksheetext pe2Dev = new BmBizWorksheetext();
				pe2Dev.setWsItemCode("PE2");
				pe2Dev.setWsItemValue(PE2IPDevID);
				wsItemList.add(pe2Dev);
			}

			String enableVRRP = "";
			if(ServType.equals("IPSL") && (ServModel.equals("BASIP") || ServModel.equals("BSW"))
					&& StringUtils.isNotBlank(BAS2IP)){
				enableVRRP = "Y";
			}else{
				enableVRRP = StringUtils.isBlank(VRRP)?"N":VRRP;
			}
			if (StringUtils.isBlank(param.getString("enableVRRP"))){
				BmBizWorksheetext enableVRRPSheet = new BmBizWorksheetext();
				enableVRRPSheet.setWsItemCode("enableVRRP");
				enableVRRPSheet.setWsItemValue(enableVRRP);
				wsItemList.add(enableVRRPSheet);
			}

			if(!StringUtils.isBlank(BAS1IPDevID)){
				param.remove("BAS1IP");
				BmBizWorksheetext bas1Dev = new BmBizWorksheetext();
				bas1Dev.setWsItemCode("BAS1");
				bas1Dev.setWsItemValue(BAS1IPDevID);
				wsItemList.add(bas1Dev);
			}

			if(!StringUtils.isBlank(BAS1Port)){
				String temp = BAS1Port;
				int index = temp.indexOf(".");
				if (index!=-1){
					temp = temp.substring(0,index);
				}
				BmBizWorksheetext bas1port = new BmBizWorksheetext();
				bas1port.setWsItemCode("BAS1PhyPort");
				bas1port.setWsItemValue(temp);
				wsItemList.add(bas1port);
			}

			if(!StringUtils.isBlank(BAS2IPDevID)){
				param.remove("BAS2IP");
				BmBizWorksheetext bas2Dev = new BmBizWorksheetext();
				bas2Dev.setWsItemCode("BAS2");
				bas2Dev.setWsItemValue(BAS2IPDevID);
				wsItemList.add(bas2Dev);
			}
			if(!StringUtils.isBlank(BAS2Port)){
				String temp = BAS2Port;
				int index = temp.indexOf(".");
				temp = temp.substring(0,index);
				BmBizWorksheetext bas2port = new BmBizWorksheetext();
				bas2port.setWsItemCode("BAS2PhyPort");
				bas2port.setWsItemValue(temp);
				wsItemList.add(bas2port);
			}
			if(!StringUtils.isBlank(CLOUDPE1)){
				param.remove("CLOUDPE1");
				BmBizWorksheetext CLOUDPE1Dev = new BmBizWorksheetext();
				CLOUDPE1Dev.setWsItemCode("CLOUDPE1");
				CLOUDPE1Dev.setWsItemValue(CLOUDPE1);
				wsItemList.add(CLOUDPE1Dev);
			}
			if(!StringUtils.isBlank(CLOUDPE2)){
				param.remove("CLOUDPE2");
				BmBizWorksheetext CLOUDPE2Dev = new BmBizWorksheetext();
				CLOUDPE2Dev.setWsItemCode("CLOUDPE2");
				CLOUDPE2Dev.setWsItemValue(CLOUDPE2);
				wsItemList.add(CLOUDPE2Dev);
			}

			for (Map.Entry<String, Object> entry : param.entrySet()) {
				Object val = entry.getValue();
				String key = entry.getKey();
				if(!WORKSHEET_EXT_MAP.containsKey(key) && !String.valueOf(val).equals("")){
					BmBizWorksheetext wsItem = new BmBizWorksheetext();
					wsItem.setWsItemCode(key);
					wsItem.setWsItemValue(String.valueOf(val));
					wsItemList.add(wsItem);
				}
			}

			logger.info(wsItemList.toString());
			saveWsReq.setWsItemList(wsItemList);
			saveWsReq.setSource("auto");
			logger.info("save is start......request param: " + JSON.toJSONString(saveWsReq));
			//调用接口保存
			RpcRestResponse myRestResponse = wsManagerRpc.saveWs(saveWsReq);
			if(myRestResponse!=null){
				if(!"0000".equals(myRestResponse.getCode())){
					throw new Exception(myRestResponse.getMsg());
				}
			}

			BmBizCompletedWsToNotice bmBizCompletedWsToNotice = new BmBizCompletedWsToNotice();
			bmBizCompletedWsToNotice.setWsnbr(wsnbr);
			bmBizCompletedWsToNotice.setWorkSheetCode(WsCode);
			bmBizCompletedWsToNotice.setOperTime(new Date());
			bmBizCompletedWsToNotice.setStatus("U");
			wsAndReturnMapper.saveWsC(bmBizCompletedWsToNotice);
			String taskType = dealNull(param.getString("TASKTYPE"));
			DeployReq deployReq = new DeployReq();
			List<String> wsnbrList = new ArrayList<>();
			wsnbrList.add(wsnbr);
			deployReq.setWsnbr(wsnbrList);
			deployReq.setIsSync("true");
			deployReq.setPrilvl("0");
			deployReq.setIsCfgCheck("Y");
			if (StringUtils.isNotBlank(taskType)){
				deployReq.setTaskType(taskType);
				WslssueAndReturnThread wslssueAndReturnThread = new WslssueAndReturnThread(deployReq);
				wslssueAndReturnThread.start();
			}else {
				String autoDeployFlag = wsMsgQryService.getAutoDeploy(ServType, OperType);
				if(autoDeployFlag.equals("Y")) {
					deployReq.setTaskType("cfgdeploy");
					logger.info("WslssueAndReturnThread is start...");
					WslssueAndReturnThread wslssueAndReturnThread = new WslssueAndReturnThread(deployReq);
					wslssueAndReturnThread.start();
				}
			}
		}catch (Exception e){
			throw new Exception(e.getMessage());
		}
		logger.info("worksheetIssue is end...");
		return result;
	}

	private String dealNull(Object str)
	{
		if(str==null)
		{
			return "";
		}
		return str.toString().trim();
	}

	public void worksheetReturn(String url) {
		logger.info("url:" + url);
		List<GeneralInfo> returnWsList = wsMsgQryService.qryCompletedWsTable();
		for(int i=0;i < returnWsList.size();i++) {
			GeneralInfo generalInfo = returnWsList.get(i);
			JSONObject ob = new JSONObject();
			ob.put("WsCode",dealNull(generalInfo.getWsCode()));
			ob.put("WsStatus",dealNull(generalInfo.getWsStatus()));
			ob.put("StatusTime",dealNull(generalInfo.getStatusTime()));
			ob.put("WsMessage",dealNull(generalInfo.getWsMessage()));
			ob.put("OperType",dealNull(generalInfo.getOperType()));
			logger.info("send context is :" + ob.toString());
			String dataStr = HttpRemoteCallClient.postRemote(url, ob.toString());
			logger.info("accept context is :" + dataStr);
			JSONObject jsonObject = JSONObject.parseObject(dataStr);
			String result = jsonObject.getString("Result");
			if(dealNull(result).equals("0")) {
				wsMsgQryService.deleteCompletedWsRecord(generalInfo.getWsCode());
			}
		}
	}

	/**
	 * 查询北向工单状态定时任务
	 */
	public void wsReturn(){
		List<BmBizCompletedWsToNotice> wscs = wsMsgQryService.qryCompleteWS();
		logger.info("获取到工单状态更新开始....,wscs:{}",wscs);
		if (wscs!=null&&wscs.size()>0) {
			wscs.forEach(wsc -> {
				if (wsc.getStatus().equalsIgnoreCase("C") || wsc.getStatus().equalsIgnoreCase("F")) {
					wsAndReturnMapper.updateWsC(wsc);
				}
			});
		}
	}

	/**
	 * 增加鉴权功能
	 * @param username
	 * @param ipAddress
	 * @return
	 */
	public void authentication(String username,String ipAddress){
		logger.info("鉴权参数:username==>{},ipAddress==>{}",username,ipAddress);
		if (StringUtils.isBlank(username)||StringUtils.isBlank(ipAddress)) {
			throw  new IllegalArgumentException("调用方鉴权失败");
		}
		int authentication = cmBizServApiClientCfgMapper.authentication(username, ipAddress);
		if (authentication==0){
			throw  new IllegalArgumentException("调用方鉴权失败");
		}
	}
}
