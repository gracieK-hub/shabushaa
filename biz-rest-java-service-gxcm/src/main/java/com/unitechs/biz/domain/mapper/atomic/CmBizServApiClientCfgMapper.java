package com.unitechs.biz.domain.mapper.atomic;


import com.unitechs.biz.domain.entity.atomic.CmBizServApiClientCfg;
import com.unitechs.framework.persistence.mapper.MyBatisMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author fandj
 * @Date 2021/12/14
 */
@Mapper
public interface CmBizServApiClientCfgMapper extends MyBatisMapper<CmBizServApiClientCfg> {
      int authentication(@Param("username") String username,@Param("ipAddress") String ipAddress);
}
