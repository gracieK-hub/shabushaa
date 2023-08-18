package com.unitechs.biz.web.req;

public class WsIssueReq {

	private String WsCode;
	private String ServType;
	private String ServModel;
	private String OperType;
	private String SR1IP;
	private String SR1Port;
	private String InterIP1;
	private String InterIP1Mask;
	private String UserInterIP1;
	private String AGSWIP1;
	private String AGSWUpPort1;
	private String AGSWDownPort1;
	private String UserIP;
	private String UserIPMask;
	private String VRRP;
	private String VRRPGrpNO;
	private String VRRPPriority1;
	private String VRRPPriority2;
	private String VRRPPVLAN;
	private String VRRPCVLAN;
	private String VRRPvGetway;
	private String SR2IP;
	private String SR2Port;
	private String InterIP2;
	private String InterIP2Mask;
	private String UserInterIP2;
	private String AGSWIP2;
	private String AGSWUpPort2;
	private String AGSWDownPort2;
	private String SRPortDesc;
	private String BindWidth;
	private String CVLAN;
	private String PVLAN;
	private String ACCSWIP;
	private String ACCSWIPUpPort1;
	private String ACCSWIPDownPort1;
	private String ACCSWIPUpPort2;
	private String PONUSERVLAN;
	private String ONUVLAN;
	private String ONUID;
	private String ONUSN;
	private String OLTIP;
	private String OLTUpPort1;
	private String OLTDownPort;
	private String ONUIP;
	private String ONUUpPort;
	private String ONUDownPort;
	private String OLTUpPort2;

	private String VRFName;
	private String VRFDesc;
	private String RD;
	private String ExportRT;
	private String ImportRT;
	private String RouteProtocol;

	private String BGPASNO;
	private String PE1IP;
	private String PE1Port;
	private String PE1PortDesc;
	private String PE1InterIP;
	private String PE1InterIPMask;
	private String PE1VLAN;
	private String PE1NextHopIP;
	private String PE2IP;
	private String PE2Port;
	private String PE2PortDesc;
	private String PE2InterIP;
	private String PE2InterIPMask;
	private String PE2VLAN;
	private String PE2NextHopIP;
	private String CloudIP;
	private String CloudIPMask;

	private String BAS1IP;
	private String BAS1Port;
	private String BAS2IP;
	private String BAS2Port;
	private String DomainName;

	private String PE1DownPort;
	private String PE1DownPortDesc;
	private String PE1DownInterIP;
	private String PE1DownInterIPMask;
	private String PE1DownVLAN;
	private String PE1DownNextHopIP;

	private String PE2DownPort;
	private String PE2DownPortDesc;
	private String PE2DownInterIP;
	private String PE2DownInterIPMask;
	private String PE2DownVLAN;
	private String PE2DownNextHopIP;

	private String UserBGPASNO;

	private String BASUserIP;
	private String ONUGateWay;

	private String IPPoolName;
	private String IPPoolGateWay;
	private String IPPoolGWMask;
	private String IPPoolBeginIP;
	private String IPPoolEndIP;

	public String getWsCode() {
		return WsCode;
	}
	public void setWsCode(String wsCode) {
		WsCode = wsCode;
	}
	public String getServType() {
		return ServType;
	}
	public void setServType(String servType) {
		ServType = servType;
	}
	public String getServModel() {
		return ServModel;
	}
	public void setServModel(String servModel) {
		ServModel = servModel;
	}
	public String getOperType() {
		return OperType;
	}
	public void setOperType(String operType) {
		OperType = operType;
	}
	public String getSR1IP() {
		return SR1IP;
	}
	public void setSR1IP(String sR1IP) {
		SR1IP = sR1IP;
	}
	public String getSR1Port() {
		return SR1Port;
	}
	public void setSR1Port(String sR1Port) {
		SR1Port = sR1Port;
	}
	public String getInterIP1() {
		return InterIP1;
	}
	public void setInterIP1(String interIP1) {
		InterIP1 = interIP1;
	}
	public String getInterIP1Mask() {
		return InterIP1Mask;
	}
	public void setInterIP1Mask(String interIP1Mask) {
		InterIP1Mask = interIP1Mask;
	}
	public String getUserInterIP1() {
		return UserInterIP1;
	}
	public void setUserInterIP1(String userInterIP1) {
		UserInterIP1 = userInterIP1;
	}
	public String getAGSWIP1() {
		return AGSWIP1;
	}
	public void setAGSWIP1(String aGSWIP1) {
		AGSWIP1 = aGSWIP1;
	}
	public String getAGSWUpPort1() {
		return AGSWUpPort1;
	}
	public void setAGSWUpPort1(String aGSWUpPort1) {
		AGSWUpPort1 = aGSWUpPort1;
	}
	public String getAGSWDownPort1() {
		return AGSWDownPort1;
	}
	public void setAGSWDownPort1(String aGSWDownPort1) {
		AGSWDownPort1 = aGSWDownPort1;
	}
	public String getUserIP() {
		return UserIP;
	}
	public void setUserIP(String userIP) {
		UserIP = userIP;
	}
	public String getUserIPMask() {
		return UserIPMask;
	}
	public void setUserIPMask(String userIPMask) {
		UserIPMask = userIPMask;
	}
	public String getVRRP() {
		return VRRP;
	}
	public void setVRRP(String vRRP) {
		VRRP = vRRP;
	}
	public String getVRRPGrpNO() {
		return VRRPGrpNO;
	}
	public void setVRRPGrpNO(String vRRPGrpNO) {
		VRRPGrpNO = vRRPGrpNO;
	}
	public String getVRRPPriority1() {
		return VRRPPriority1;
	}
	public void setVRRPPriority1(String vRRPPriority1) {
		VRRPPriority1 = vRRPPriority1;
	}
	public String getVRRPPriority2() {
		return VRRPPriority2;
	}
	public void setVRRPPriority2(String vRRPPriority2) {
		VRRPPriority2 = vRRPPriority2;
	}
	public String getVRRPPVLAN() {
		return VRRPPVLAN;
	}
	public void setVRRPPVLAN(String vRRPPVLAN) {
		VRRPPVLAN = vRRPPVLAN;
	}
	public String getVRRPCVLAN() {
		return VRRPCVLAN;
	}
	public void setVRRPCVLAN(String vRRPCVLAN) {
		VRRPCVLAN = vRRPCVLAN;
	}
	public String getVRRPvGetway() {
		return VRRPvGetway;
	}
	public void setVRRPvGetway(String vRRPvGetway) {
		VRRPvGetway = vRRPvGetway;
	}
	public String getSR2IP() {
		return SR2IP;
	}
	public void setSR2IP(String sR2IP) {
		SR2IP = sR2IP;
	}
	public String getSR2Port() {
		return SR2Port;
	}
	public void setSR2Port(String sR2Port) {
		SR2Port = sR2Port;
	}
	public String getInterIP2() {
		return InterIP2;
	}
	public void setInterIP2(String interIP2) {
		InterIP2 = interIP2;
	}
	public String getInterIP2Mask() {
		return InterIP2Mask;
	}
	public void setInterIP2Mask(String interIP2Mask) {
		InterIP2Mask = interIP2Mask;
	}
	public String getUserInterIP2() {
		return UserInterIP2;
	}
	public void setUserInterIP2(String userInterIP2) {
		UserInterIP2 = userInterIP2;
	}
	public String getAGSWIP2() {
		return AGSWIP2;
	}
	public void setAGSWIP2(String aGSWIP2) {
		AGSWIP2 = aGSWIP2;
	}
	public String getAGSWUpPort2() {
		return AGSWUpPort2;
	}
	public void setAGSWUpPort2(String aGSWUpPort2) {
		AGSWUpPort2 = aGSWUpPort2;
	}
	public String getAGSWDownPort2() {
		return AGSWDownPort2;
	}
	public void setAGSWDownPort2(String aGSWDownPort2) {
		AGSWDownPort2 = aGSWDownPort2;
	}
	public String getSRPortDesc() {
		return SRPortDesc;
	}
	public void setSRPortDesc(String sRPortDesc) {
		SRPortDesc = sRPortDesc;
	}
	public String getBindWidth() {
		return BindWidth;
	}
	public void setBindWidth(String bindWidth) {
		BindWidth = bindWidth;
	}
	public String getCVLAN() {
		return CVLAN;
	}
	public void setCVLAN(String cVLAN) {
		CVLAN = cVLAN;
	}
	public String getPVLAN() {
		return PVLAN;
	}
	public void setPVLAN(String pVLAN) {
		PVLAN = pVLAN;
	}
	public String getACCSWIP() {
		return ACCSWIP;
	}
	public void setACCSWIP(String aCCSWIP) {
		ACCSWIP = aCCSWIP;
	}
	public String getACCSWIPUpPort1() {
		return ACCSWIPUpPort1;
	}
	public void setACCSWIPUpPort1(String aCCSWIPUpPort1) {
		ACCSWIPUpPort1 = aCCSWIPUpPort1;
	}
	public String getACCSWIPDownPort1() {
		return ACCSWIPDownPort1;
	}
	public void setACCSWIPDownPort1(String aCCSWIPDownPort1) {
		ACCSWIPDownPort1 = aCCSWIPDownPort1;
	}
	public String getACCSWIPUpPort2() {
		return ACCSWIPUpPort2;
	}
	public void setACCSWIPUpPort2(String aCCSWIPUpPort2) {
		ACCSWIPUpPort2 = aCCSWIPUpPort2;
	}
	public String getPONUSERVLAN() {
		return PONUSERVLAN;
	}
	public void setPONUSERVLAN(String pONUSERVLAN) {
		PONUSERVLAN = pONUSERVLAN;
	}
	public String getONUVLAN() {
		return ONUVLAN;
	}
	public void setONUVLAN(String oNUVLAN) {
		ONUVLAN = oNUVLAN;
	}
	public String getONUID() {
		return ONUID;
	}
	public void setONUID(String oNUID) {
		ONUID = oNUID;
	}
	public String getONUSN() {
		return ONUSN;
	}
	public void setONUSN(String oNUSN) {
		ONUSN = oNUSN;
	}
	public String getOLTIP() {
		return OLTIP;
	}
	public void setOLTIP(String oLTIP) {
		OLTIP = oLTIP;
	}
	public String getOLTUpPort1() {
		return OLTUpPort1;
	}
	public void setOLTUpPort1(String oLTUpPort1) {
		OLTUpPort1 = oLTUpPort1;
	}
	public String getOLTDownPort() {
		return OLTDownPort;
	}
	public void setOLTDownPort(String oLTDownPort) {
		OLTDownPort = oLTDownPort;
	}
	public String getONUIP() {
		return ONUIP;
	}
	public void setONUIP(String oNUIP) {
		ONUIP = oNUIP;
	}
	public String getONUUpPort() {
		return ONUUpPort;
	}
	public void setONUUpPort(String oNUUpPort) {
		ONUUpPort = oNUUpPort;
	}
	public String getONUDownPort() {
		return ONUDownPort;
	}
	public void setONUDownPort(String oNUDownPort) {
		ONUDownPort = oNUDownPort;
	}
	public String getOLTUpPort2() {
		return OLTUpPort2;
	}
	public void setOLTUpPort2(String oLTUpPort2) {
		OLTUpPort2 = oLTUpPort2;
	}

	public String getVRFName() {
		return VRFName;
	}

	public void setVRFName(String VRFName) {
		this.VRFName = VRFName;
	}

	public String getVRFDesc() {
		return VRFDesc;
	}

	public void setVRFDesc(String VRFDesc) {
		this.VRFDesc = VRFDesc;
	}

	public String getRD() {
		return RD;
	}

	public void setRD(String RD) {
		this.RD = RD;
	}

	public String getExportRT() {
		return ExportRT;
	}

	public void setExportRT(String exportRT) {
		ExportRT = exportRT;
	}

	public String getImportRT() {
		return ImportRT;
	}

	public void setImportRT(String importRT) {
		ImportRT = importRT;
	}

	public String getRouteProtocol() {
		return RouteProtocol;
	}

	public void setRouteProtocol(String routeProtocol) {
		RouteProtocol = routeProtocol;
	}

	public String getBGPASNO() {
		return BGPASNO;
	}

	public void setBGPASNO(String BGPASNO) {
		this.BGPASNO = BGPASNO;
	}

	public String getPE1IP() {
		return PE1IP;
	}

	public void setPE1IP(String PE1IP) {
		this.PE1IP = PE1IP;
	}

	public String getPE2IP() {
		return PE2IP;
	}

	public void setPE2IP(String PE2IP) {
		this.PE2IP = PE2IP;
	}

	public String getPE1Port() {
		return PE1Port;
	}

	public void setPE1Port(String PE1Port) {
		this.PE1Port = PE1Port;
	}

	public String getPE1PortDesc() {
		return PE1PortDesc;
	}

	public void setPE1PortDesc(String PE1PortDesc) {
		this.PE1PortDesc = PE1PortDesc;
	}

	public String getPE1InterIP() {
		return PE1InterIP;
	}

	public void setPE1InterIP(String PE1InterIP) {
		this.PE1InterIP = PE1InterIP;
	}

	public String getPE1InterIPMask() {
		return PE1InterIPMask;
	}

	public void setPE1InterIPMask(String PE1InterIPMask) {
		this.PE1InterIPMask = PE1InterIPMask;
	}

	public String getPE1VLAN() {
		return PE1VLAN;
	}

	public void setPE1VLAN(String PE1VLAN) {
		this.PE1VLAN = PE1VLAN;
	}

	public String getPE1NextHopIP() {
		return PE1NextHopIP;
	}

	public void setPE1NextHopIP(String PE1NextHopIP) {
		this.PE1NextHopIP = PE1NextHopIP;
	}

	public String getPE2Port() {
		return PE2Port;
	}

	public void setPE2Port(String PE2Port) {
		this.PE2Port = PE2Port;
	}

	public String getPE2PortDesc() {
		return PE2PortDesc;
	}

	public void setPE2PortDesc(String PE2PortDesc) {
		this.PE2PortDesc = PE2PortDesc;
	}

	public String getPE2InterIP() {
		return PE2InterIP;
	}

	public void setPE2InterIP(String PE2InterIP) {
		this.PE2InterIP = PE2InterIP;
	}

	public String getPE2InterIPMask() {
		return PE2InterIPMask;
	}

	public void setPE2InterIPMask(String PE2InterIPMask) {
		this.PE2InterIPMask = PE2InterIPMask;
	}

	public String getPE2VLAN() {
		return PE2VLAN;
	}

	public void setPE2VLAN(String PE2VLAN) {
		this.PE2VLAN = PE2VLAN;
	}

	public String getPE2NextHopIP() {
		return PE2NextHopIP;
	}

	public void setPE2NextHopIP(String PE2NextHopIP) {
		this.PE2NextHopIP = PE2NextHopIP;
	}

	public String getCloudIP() {
		return CloudIP;
	}

	public void setCloudIP(String cloudIP) {
		CloudIP = cloudIP;
	}

	public String getCloudIPMask() {
		return CloudIPMask;
	}

	public void setCloudIPMask(String cloudIPMask) {
		CloudIPMask = cloudIPMask;
	}

	public String getBAS1IP() {
		return BAS1IP;
	}

	public void setBAS1IP(String BAS1IP) {
		this.BAS1IP = BAS1IP;
	}

	public String getBAS1Port() {
		return BAS1Port;
	}

	public void setBAS1Port(String BAS1Port) {
		this.BAS1Port = BAS1Port;
	}

	public String getBAS2IP() {
		return BAS2IP;
	}

	public void setBAS2IP(String BAS2IP) {
		this.BAS2IP = BAS2IP;
	}

	public String getBAS2Port() {
		return BAS2Port;
	}

	public void setBAS2Port(String BAS2Port) {
		this.BAS2Port = BAS2Port;
	}

	public String getDomainName() {
		return DomainName;
	}

	public void setDomainName(String domainName) {
		DomainName = domainName;
	}

	public String getPE1DownPort() {
		return PE1DownPort;
	}

	public void setPE1DownPort(String PE1DownPort) {
		this.PE1DownPort = PE1DownPort;
	}

	public String getPE1DownPortDesc() {
		return PE1DownPortDesc;
	}

	public void setPE1DownPortDesc(String PE1DownPortDesc) {
		this.PE1DownPortDesc = PE1DownPortDesc;
	}

	public String getPE1DownInterIP() {
		return PE1DownInterIP;
	}

	public void setPE1DownInterIP(String PE1DownInterIP) {
		this.PE1DownInterIP = PE1DownInterIP;
	}

	public String getPE1DownInterIPMask() {
		return PE1DownInterIPMask;
	}

	public void setPE1DownInterIPMask(String PE1DownInterIPMask) {
		this.PE1DownInterIPMask = PE1DownInterIPMask;
	}

	public String getPE1DownVLAN() {
		return PE1DownVLAN;
	}

	public void setPE1DownVLAN(String PE1DownVLAN) {
		this.PE1DownVLAN = PE1DownVLAN;
	}

	public String getPE1DownNextHopIP() {
		return PE1DownNextHopIP;
	}

	public void setPE1DownNextHopIP(String PE1DownNextHopIP) {
		this.PE1DownNextHopIP = PE1DownNextHopIP;
	}

	public String getPE2DownPort() {
		return PE2DownPort;
	}

	public void setPE2DownPort(String PE2DownPort) {
		this.PE2DownPort = PE2DownPort;
	}

	public String getPE2DownPortDesc() {
		return PE2DownPortDesc;
	}

	public void setPE2DownPortDesc(String PE2DownPortDesc) {
		this.PE2DownPortDesc = PE2DownPortDesc;
	}

	public String getPE2DownInterIP() {
		return PE2DownInterIP;
	}

	public void setPE2DownInterIP(String PE2DownInterIP) {
		this.PE2DownInterIP = PE2DownInterIP;
	}

	public String getPE2DownInterIPMask() {
		return PE2DownInterIPMask;
	}

	public void setPE2DownInterIPMask(String PE2DownInterIPMask) {
		this.PE2DownInterIPMask = PE2DownInterIPMask;
	}

	public String getPE2DownVLAN() {
		return PE2DownVLAN;
	}

	public void setPE2DownVLAN(String PE2DownVLAN) {
		this.PE2DownVLAN = PE2DownVLAN;
	}

	public String getPE2DownNextHopIP() {
		return PE2DownNextHopIP;
	}

	public void setPE2DownNextHopIP(String PE2DownNextHopIP) {
		this.PE2DownNextHopIP = PE2DownNextHopIP;
	}

	public String getUserBGPASNO() {
		return UserBGPASNO;
	}

	public void setUserBGPASNO(String userBGPASNO) {
		UserBGPASNO = userBGPASNO;
	}

	public String getBASUserIP() {
		return BASUserIP;
	}

	public void setBASUserIP(String BASUserIP) {
		this.BASUserIP = BASUserIP;
	}

	public String getONUGateWay() {
		return ONUGateWay;
	}

	public void setONUGateWay(String ONUGateWay) {
		this.ONUGateWay = ONUGateWay;
	}

	public String getIPPoolName() {
		return IPPoolName;
	}

	public void setIPPoolName(String IPPoolName) {
		this.IPPoolName = IPPoolName;
	}

	public String getIPPoolGateWay() {
		return IPPoolGateWay;
	}

	public void setIPPoolGateWay(String IPPoolGateWay) {
		this.IPPoolGateWay = IPPoolGateWay;
	}

	public String getIPPoolGWMask() {
		return IPPoolGWMask;
	}

	public void setIPPoolGWMask(String IPPoolGWMask) {
		this.IPPoolGWMask = IPPoolGWMask;
	}

	public String getIPPoolBeginIP() {
		return IPPoolBeginIP;
	}

	public void setIPPoolBeginIP(String IPPoolBeginIP) {
		this.IPPoolBeginIP = IPPoolBeginIP;
	}

	public String getIPPoolEndIP() {
		return IPPoolEndIP;
	}

	public void setIPPoolEndIP(String IPPoolEndIP) {
		this.IPPoolEndIP = IPPoolEndIP;
	}
}
