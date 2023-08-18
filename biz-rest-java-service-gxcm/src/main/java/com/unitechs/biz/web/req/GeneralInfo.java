package com.unitechs.biz.web.req;

/**
 * @author liujie
 */
public class GeneralInfo {

	private String WsCode;
	private String WsStatus;
	private String StatusTime;
	private String WsMessage;
	private String OperType;
	private String servtypeid;
	public String getWsCode() {
		return WsCode;
	}
	public void setWsCode(String wsCode) {
		WsCode = wsCode;
	}
	public String getWsStatus() {
		return WsStatus;
	}
	public void setWsStatus(String wsStatus) {
		WsStatus = wsStatus;
	}
	public String getStatusTime() {
		return StatusTime;
	}
	public void setStatusTime(String statusTime) {
		StatusTime = statusTime;
	}
	public String getWsMessage() {
		return WsMessage;
	}
	public void setWsMessage(String wsMessage) {
		WsMessage = wsMessage;
	}
	public String getOperType() {
		return OperType;
	}
	public void setOperType(String operType) {
		OperType = operType;
	}

	public String getServtypeid() {
		return servtypeid;
	}

	public void setServtypeid(String servtypeid) {
		this.servtypeid = servtypeid;
	}
}
