package com.unitechs.biz.domain.service;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.unitechs.biz.domain.entity.atomic.BmBizCompletedWsToNotice;
import com.unitechs.biz.domain.entity.atomic.BmBizWorksheet;
import com.unitechs.biz.domain.mapper.atomic.WsMsgQryMapper;
import com.unitechs.biz.web.req.GeneralInfo;
import com.unitechs.resource.exception.ServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WsMsgQryService {

	@Resource
	private WsMsgQryMapper wsMsgQryMapper;
	
	public String wsNbrIfExist(String wsCode, String servType) {
		String Flag = "";
		List<BmBizWorksheet> wsNbrList = wsMsgQryMapper.queryWsNbr(wsCode,servType);
		if(CollectionUtils.isEmpty(wsNbrList)) {
			Flag = "N";
		}
		else {
			Flag = "Y";
		}
		return Flag;
	}

	public String getAutoDeploy(String servType, String operType) {
		String autodeploy = wsMsgQryMapper.qryAutoDeploy(servType, operType);
		return autodeploy;
	}

	public List<GeneralInfo> qryCompletedWsTable() {
		return wsMsgQryMapper.qryCompletedWsTable();
		
	}

	public List<GeneralInfo> qryCallBackInfo(List<String> idList) {
		return wsMsgQryMapper.qryCallBackInfo(idList);

	}

	public void deleteCompletedWsRecord(String wsCode) {
		wsMsgQryMapper.deleteCompletedWsRecord(wsCode);
	}

	public List<BmBizCompletedWsToNotice> qryCompleteWS(){
		return wsMsgQryMapper.qryCompleteWS();
	}

	public void servModelIfExist(String servType,String servModel){
		 checkResult(wsMsgQryMapper.queryServModel(servType, servType+"_"+servModel),"ServModel");
	}

	public void servTypeIfExist(String servType){
		 checkResult(wsMsgQryMapper.queryServType(servType),"ServType");
	}

	public void operTypeIfExist(String servType,String operType){
		 checkResult(wsMsgQryMapper.queryOperType(servType,operType),"operType");
	}

	/**
	 * servType, servModel,operType only one
	 * @return
	 */
	public void checkResult(int result,String tip){
		if (result==0){
			throw new ServiceException("无效"+tip);
		} else if (result!=1){
			throw new ServiceException(tip+"存在多个");
		}
	}

}
