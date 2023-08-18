package com.unitechs.biz.domain.entity.atomic;

import com.unitechs.framework.model.entity.BaseEntity;

/**
 * @author liwen
 * @since 2019/8/30
 */
public class BmBizWorksheet extends BaseEntity{
    private static final long serialVersionUID = 1L;

    private String wsnbr;
    private String servTypeId;
    private String operType;
    private String custId;
    private String servId;
    private String status;
    private String priorStatus;
    private String servOrderCode;
    private String createUser;
    private String createTime;
    private String modifyTime;
    private String operUser;
    private String operTime;
    private String remark;
    private String comptime;
    private String planDeployTime;
    private String approvalTime;
    private String workDept;
    private String planCompleteDate;
    private String custManagerName;
    private String cmContactInfo;
    private String servName;
    private String workSheetcode;
    private String clazz;
    private String servModelId;
    private String isDupByWsCode;
    private String custServTypeId;
    private String errorType;
    private String errorDetail;
    private String customerId;
    private String approver;
    private String batchWsId;
    private String isNeedApproved;
    private String priority;
    private String title;
    private String source;
    private String fileUser;
    private String mainDevId;
    private String peerDevId;
    private String isFromFk;
    private String lastModifyUser;
    private String lastDeployUser;
    private String isYuYue;
    private String deployType;
    private String servCode;
    private String newPortActiveStatus;
    private String oldPortDeactiveStatus;
    private String subErrorType;
    private String tid;
    private String create_time;
    private String modify_time;
    private String rbstatus;//回滚状态

    public String getRbstatus() {
        return rbstatus;
    }

    public void setRbstatus(String rbstatus) {
        this.rbstatus = rbstatus;
    }

    public String getWsnbr() {
        return wsnbr;
    }

    public void setWsnbr(String wsnbr) {
        this.wsnbr = wsnbr;
    }

    public String getServTypeId() {
        return servTypeId;
    }

    public void setServTypeId(String servTypeId) {
        this.servTypeId = servTypeId;
    }

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getServId() {
        return servId;
    }

    public void setServId(String servId) {
        this.servId = servId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriorStatus() {
        return priorStatus;
    }

    public void setPriorStatus(String priorStatus) {
        this.priorStatus = priorStatus;
    }

    public String getServOrderCode() {
        return servOrderCode;
    }

    public void setServOrderCode(String servOrderCode) {
        this.servOrderCode = servOrderCode;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getOperUser() {
        return operUser;
    }

    public void setOperUser(String operUser) {
        this.operUser = operUser;
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getComptime() {
        return comptime;
    }

    public void setComptime(String comptime) {
        this.comptime = comptime;
    }

    public String getPlanDeployTime() {
        return planDeployTime;
    }

    public void setPlanDeployTime(String planDeployTime) {
        this.planDeployTime = planDeployTime;
    }

    public String getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(String approvalTime) {
        this.approvalTime = approvalTime;
    }

    public String getWorkDept() {
        return workDept;
    }

    public void setWorkDept(String workDept) {
        this.workDept = workDept;
    }

    public String getPlanCompleteDate() {
        return planCompleteDate;
    }

    public void setPlanCompleteDate(String planCompleteDate) {
        this.planCompleteDate = planCompleteDate;
    }

    public String getCustManagerName() {
        return custManagerName;
    }

    public void setCustManagerName(String custManagerName) {
        this.custManagerName = custManagerName;
    }

    public String getCmContactInfo() {
        return cmContactInfo;
    }

    public void setCmContactInfo(String cmContactInfo) {
        this.cmContactInfo = cmContactInfo;
    }

    public String getServName() {
        return servName;
    }

    public void setServName(String servName) {
        this.servName = servName;
    }

    public String getWorkSheetcode() {
        return workSheetcode;
    }

    public void setWorkSheetcode(String workSheetcode) {
        this.workSheetcode = workSheetcode;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getServModelId() {
        return servModelId;
    }

    public void setServModelId(String servModelId) {
        this.servModelId = servModelId;
    }

    public String getIsDupByWsCode() {
        return isDupByWsCode;
    }

    public void setIsDupByWsCode(String isDupByWsCode) {
        this.isDupByWsCode = isDupByWsCode;
    }

    public String getCustServTypeId() {
        return custServTypeId;
    }

    public void setCustServTypeId(String custServTypeId) {
        this.custServTypeId = custServTypeId;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getBatchWsId() {
        return batchWsId;
    }

    public void setBatchWsId(String batchWsId) {
        this.batchWsId = batchWsId;
    }

    public String getIsNeedApproved() {
        return isNeedApproved;
    }

    public void setIsNeedApproved(String isNeedApproved) {
        this.isNeedApproved = isNeedApproved;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getFileUser() {
        return fileUser;
    }

    public void setFileUser(String fileUser) {
        this.fileUser = fileUser;
    }

    public String getMainDevId() {
        return mainDevId;
    }

    public void setMainDevId(String mainDevId) {
        this.mainDevId = mainDevId;
    }

    public String getPeerDevId() {
        return peerDevId;
    }

    public void setPeerDevId(String peerDevId) {
        this.peerDevId = peerDevId;
    }

    public String getIsFromFk() {
        return isFromFk;
    }

    public void setIsFromFk(String isFromFk) {
        this.isFromFk = isFromFk;
    }

    public String getLastModifyUser() {
        return lastModifyUser;
    }

    public void setLastModifyUser(String lastModifyUser) {
        this.lastModifyUser = lastModifyUser;
    }

    public String getLastDeployUser() {
        return lastDeployUser;
    }

    public void setLastDeployUser(String lastDeployUser) {
        this.lastDeployUser = lastDeployUser;
    }

    public String getIsYuYue() {
        return isYuYue;
    }

    public void setIsYuYue(String isYuYue) {
        this.isYuYue = isYuYue;
    }

    public String getDeployType() {
        return deployType;
    }

    public void setDeployType(String deployType) {
        this.deployType = deployType;
    }

    public String getServCode() {
        return servCode;
    }

    public void setServCode(String servCode) {
        this.servCode = servCode;
    }

    public String getNewPortActiveStatus() {
        return newPortActiveStatus;
    }

    public void setNewPortActiveStatus(String newPortActiveStatus) {
        this.newPortActiveStatus = newPortActiveStatus;
    }

    public String getOldPortDeactiveStatus() {
        return oldPortDeactiveStatus;
    }

    public void setOldPortDeactiveStatus(String oldPortDeactiveStatus) {
        this.oldPortDeactiveStatus = oldPortDeactiveStatus;
    }

    public String getSubErrorType() {
        return subErrorType;
    }

    public void setSubErrorType(String subErrorType) {
        this.subErrorType = subErrorType;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getModify_time() {
        return modify_time;
    }

    public void setModify_time(String modify_time) {
        this.modify_time = modify_time;
    }
}
