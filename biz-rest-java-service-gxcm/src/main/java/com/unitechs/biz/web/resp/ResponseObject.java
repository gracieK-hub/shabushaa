package com.unitechs.biz.web.resp;

import com.alibaba.fastjson.annotation.JSONField;

public class ResponseObject {

	@JSONField(name = "DataParseResult")
	private String dataParseResult;
	
	@JSONField(name = "WsCode")
	private String wsCode;
	
	@JSONField(name = "Result")
	private String result;
	
	@JSONField(name = "ErrorMessage")
	private String errorMessage;
	
	public String getDataParseResult() {
		return dataParseResult;
	}
	public void setDataParseResult(String dataParseResult) {
		this.dataParseResult = dataParseResult;
	}
	public String getWsCode() {
		return wsCode;
	}
	public void setWsCode(String wsCode) {
		this.wsCode = wsCode;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
