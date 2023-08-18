package com.unitechs.biz.domain.thread;

import com.unitechs.biz.common.utils.SpringUtil;
import com.unitechs.biz.rpc.client.WsManagerRpc;
import com.unitechs.biz.rpc.dto.req.DeployReq;
import com.unitechs.framework.logger.Logger;
import com.unitechs.framework.logger.LoggerFactory;

/**
 * @author liwen
 * @since 2019/9/10
 */
public class WslssueAndReturnThread extends Thread {

    private static Logger logger = LoggerFactory.getLogger(WslssueAndReturnThread.class);
    private WsManagerRpc wsManagerRpc;

    private DeployReq deployReq = new DeployReq();
    public WslssueAndReturnThread(DeployReq req) {
        this.deployReq = req;
        this.wsManagerRpc= SpringUtil.getApplicationContext().getBean(WsManagerRpc.class);
    }

    @Override
    public void run() {
        try {
            logger.info("WslssueAndReturnThread is called...");
            /*调用服务进行下发*/
            wsManagerRpc.deploy(deployReq);
        } catch (Exception e) {
            logger.error(e);
        }
    }

}