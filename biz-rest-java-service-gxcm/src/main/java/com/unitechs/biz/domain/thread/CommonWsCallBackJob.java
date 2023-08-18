package com.unitechs.biz.domain.thread;

import com.unitechs.biz.domain.service.BaseWsCallBackService;
import com.unitechs.framework.logger.Logger;
import com.unitechs.framework.logger.LoggerFactory;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author liujie
 * @description
 * @date 2022-06-14 14:09
 **/
@Component
public class CommonWsCallBackJob {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private static boolean flag = true;

    @Autowired
    private BaseWsCallBackService baseWsCallBackService;


    @Scheduled(cron="0/10 * * * * ? ")
    @Async
    @ApiOperation(notes="工单返单接口",value="工单返单接口")
    public void worksheetReturn() {
        try{
            System.out.println("通用返单接口");
            if (flag) {
                flag = false;
                List<String> list = Arrays.asList("IPSL,MPLSVPN,PONCLOUD,PTNCLOUD,IPONCLOUD,IPTNCLOUD,IOTNCLOUD,SPNCLOUD".split(","));
                baseWsCallBackService.callback(list);
                flag = true;
            }
        }catch (Exception e){
            flag = true;
            logger.info("程序异常:\n"+e.getMessage());
            e.printStackTrace();
        }
    }

}
