package com.unitechs.biz.domain.mapper.atomic;

import com.unitechs.biz.domain.entity.atomic.BmBizCompletedWsToNotice;
import com.unitechs.framework.persistence.mapper.MyBatisMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description
 * @Author fanboy
 * @DATE 2021-11-18
 */
@Mapper
public interface WsAndReturnMapper extends MyBatisMapper<BmBizCompletedWsToNotice> {
      void saveWsC(BmBizCompletedWsToNotice bmBizCompletedWsToNotice);
      void updateWsC(BmBizCompletedWsToNotice bmBizCompletedWsToNotice);
}
