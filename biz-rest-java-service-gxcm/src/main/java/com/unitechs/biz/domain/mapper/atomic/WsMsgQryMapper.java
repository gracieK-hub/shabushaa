package com.unitechs.biz.domain.mapper.atomic;

import com.unitechs.biz.domain.entity.atomic.BmBizCompletedWsToNotice;
import com.unitechs.biz.domain.entity.atomic.BmBizWorksheet;
import com.unitechs.biz.web.req.GeneralInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WsMsgQryMapper {

	List<BmBizWorksheet> queryWsNbr(@Param("WsCode")String wsCode, @Param("ServType")String servType);

	String qryAutoDeploy(@Param("ServType")String servType, @Param("OperType")String operType);

	List<GeneralInfo> qryCompletedWsTable();

	List<GeneralInfo> qryCallBackInfo(@Param("idList") List<String> idList);

	void deleteCompletedWsRecord(String wsCode);

	List<BmBizCompletedWsToNotice> qryCompleteWS();

	int queryServModel(@Param("servType") String servType,@Param("servModel") String servModel);

	int queryOperType(@Param("servType") String servType,@Param("operType") String operType);

	int queryServType(String servType);

}
