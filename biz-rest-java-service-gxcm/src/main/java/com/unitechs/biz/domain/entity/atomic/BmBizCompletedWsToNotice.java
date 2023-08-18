package com.unitechs.biz.domain.entity.atomic;

import com.unitechs.framework.model.entity.BaseEntity;

import java.util.Date;

public class BmBizCompletedWsToNotice extends BaseEntity {

	private String wsnbr;
	private String workSheetCode;
	private String status;
	private String execResult;
	private Date operTime;
	public String getWsnbr() {
		return wsnbr;
	}
	public void setWsnbr(String wsnbr) {
		this.wsnbr = wsnbr;
	}
	public String getWorkSheetCode() {
		return workSheetCode;
	}
	public void setWorkSheetCode(String workSheetCode) {
		this.workSheetCode = workSheetCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getExecResult() {
		return execResult;
	}
	public void setExecResult(String execResult) {
		this.execResult = execResult;
	}
	public Date getOperTime() {
		return operTime;
	}
	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}
	
}
