package com.unitechs.biz.domain.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.unitechs.biz.common.utils.IPFormat;
import com.unitechs.biz.common.utils.IPv6ConverUtil;
import com.unitechs.biz.rpc.client.WsManagerRpc;
import com.unitechs.biz.rpc.common.entity.RpcRestResponse;
import com.unitechs.biz.rpc.dto.req.DeployReq;
import com.unitechs.biz.rpc.dto.req.SaveWsReq;
import com.unitechs.biz.web.req.WorkSheetSaveReq;
import com.unitechs.framework.logger.Logger;
import com.unitechs.framework.logger.LoggerFactory;
import com.unitechs.resource.rpc.client.IResDevRpc;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: zhangnt
 * @date: 22/6/11 19:53
 * @desc:
 *  需要实现
 *      参数校验
 *      参数转换为工单参数接口
 */
public abstract class BaseWsOpenService {

    private static final Logger logger =  LoggerFactory.getLogger(BaseWsOpenService.class);

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    protected IResDevRpc resClient;
    @Autowired
    protected WsManagerRpc wsManagerRpc;

    //校验参数
    public abstract void validatePara(JSONObject para) throws Exception;

    //转换参数到工单保存参数
    public abstract SaveWsReq convertParaToWsReq(JSONObject param) throws Exception;
    /**
     * @author: zhangnt
     * @date: 22/6/11 20:41
     * @desc: 保存工单
     */
    public boolean saveWorkSheet(SaveWsReq saveWsReq){
        logger.info("saveWorkSheet start: SaveWsReq " + JSONObject.toJSONString(saveWsReq));
        try {
            RpcRestResponse resp = wsManagerRpc.saveWs(saveWsReq);
            if (resp != null) {
                String code = resp.getCode();
                logger.info("saveWorkSheet end (0000 is success ): code: " + code);
                return StringUtils.isNotBlank(code) && code.contains("0000");
            }
        }catch (Exception e){
            logger.info("saveWorkSheet exception: ", e);
            return false;
        }
        return false;
    }
    // 重载
    public boolean saveWorkSheet(WorkSheetSaveReq saveWsReq){
        logger.info("saveWorkSheet start: WorkSheetSaveReq " + JSONObject.toJSONString(saveWsReq));
        try {
            RpcRestResponse resp = wsManagerRpc.saveWs(saveWsReq);
            if (resp != null) {
                String code = resp.getCode();
                logger.info("saveWorkSheet end (0000 is success ): code: " + code);
                return StringUtils.isNotBlank(code) && code.contains("0000");
            }
        }catch (Exception e){
            logger.info("saveWorkSheet exception: ", e);
            return false;
        }
        return false;
    }

    /**
     * @author: zhangnt
     * @date: 22/6/11 17:16
     * @desc: 部署工单
     */
    protected void deployWorkSheet(String wsnbr, String supportConfigType) {
        executorService.submit(()->{
            logger.info("deployWorkSheet start: wsnbr[" + wsnbr + "] supportConfigType [" + supportConfigType +"]");
            DeployReq deployWsReq = new DeployReq();
            deployWsReq.setWsnbr(Collections.singletonList(wsnbr));
            deployWsReq.setTaskType(supportConfigType);
            deployWsReq.setIsSync("true");
            try {
                RpcRestResponse resp = wsManagerRpc.deploy(deployWsReq);
                if (resp != null) {
                    String code = resp.getCode();
                    logger.info("deployWorkSheet end (0000 is success ): code: " + code);
                    logger.info("code: " + code);
                }
            }catch (Exception e){
                logger.info("deployWorkSheet exception:", e);
            }
        });
    }

    /**
     * @author: zhangnt
     * @date: 22/6/11 20:45
     * @desc: 获取工单id
     */
    public String getNewWsnbr() throws IllegalStateException{
        logger.info("getNewWsnbr start");
        String wsnbr = "";
        try {
            RpcRestResponse<String> resp = wsManagerRpc.getWsnbr();
            if (resp != null) {
                wsnbr = resp.getData();
                logger.info("getNewWsnbr end: wsnbr[" + wsnbr + "]");
            }
        }catch (Exception e){
            logger.info("save ws exception:", e);
            throw new IllegalStateException("获取工单号异常,无法建单");
        }
        return wsnbr;
    }
    /**
     * @author: zhangnt
     * @date: 22/6/11 20:43
     * @desc: 检查参数是否是空
     */
    protected void checkParaIsNull(JSONObject param, String key)
            throws IllegalArgumentException{
        String value = param.getString(key);
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException(key + "参数不得为空");
        }
    }

    /**
     * @author: zhangnt
     * @date: 22/6/11 20:43
     * @desc: 检查参数是否在范围内
     * 包括判空
     */
    protected void checkParaInRange(JSONObject param, String key,
                                    List<String> rangeList,
                                    String errMsg)
            throws IllegalArgumentException{
        String value = param.getString(key);
        if(!rangeList.contains(value)){
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * @author: zhangnt
     * @date: 22/6/11 19:18
     * @desc: 检查设备是否可以 并且进行转换
     */
    protected void checkDevice(JSONObject param, String devIpKey, String devKey) throws IllegalArgumentException{
        String devIp = dealParaNull(param.getString(devIpKey));
        if (StringUtils.isEmpty(devIp)) {
            throw new IllegalArgumentException(devIpKey + "参数不得为空");
        }
        List<String> devIdList = resClient.getdevidbydevip(devIp);
        if (CollectionUtils.isEmpty(devIdList)) {
            throw new IllegalArgumentException(devIp + "地址再网管查询不到");
        }
        if (devIdList.size() != 1) {
            throw new IllegalArgumentException(devIp + "找到多个对应设备");
        }
        String deviceId = devIdList.get(0);
        param.put(devKey,deviceId);
    }

    /**
     * @author: zhangnt
     * @date: 22/6/11 20:43
     * @desc: 将null处理为空值字符串
     */
    protected String dealParaNull(Object str) {
        if (str == null) {
            return "";
        }
        return str.toString().trim();
    }

    /**
     * 检查参数是否为固定的某个值
     * @param param
     * @param key
     * @param fixedValue
     * @throws IllegalArgumentException
     */
    protected void checkFixedValue(JSONObject param, String key, String fixedValue)
            throws IllegalArgumentException {
        String value = param.getString(key);
        if (StringUtils.isBlank(value) || !StringUtils.equals(value, fixedValue)) {
            throw new IllegalArgumentException(key + "参数不得为空");
        }
    }

    /**
     * 检查设备端口是否存在
     * @param param
     * @param ipKey
     * @param portKey
     * @throws Exception
     */
    protected void checkPort(JSONObject param, String ipKey, String portKey) throws Exception {
        String devIp = dealParaNull(param.getString(ipKey));
        String port = dealParaNull(param.getString(portKey));
        if (StringUtils.isBlank(devIp)) {
            throw new IllegalArgumentException(ipKey + "参数不得为空");
        }

        List<String> devIdList = resClient.getdevidbydevip(devIp);
        if (CollectionUtils.isEmpty(devIdList)) {
            throw new IllegalArgumentException(devIp + "地址再网管查询不到");
        }
        if (devIdList.size() != 1) {
            throw new IllegalArgumentException(devIp + "找到多个对应设备");
        }

        String deviceId = devIdList.get(0);
        String subStr = port.substring(0, port.indexOf("."));
        String flagPe1Port = resClient.portifexist(deviceId, subStr);
        if (StringUtils.equals(flagPe1Port, "N")) {
            throw new IllegalArgumentException("PE1Port不存在");
        }
    }


    /**
     * 检验ip4地址格式
     * @param param
     * @param key
     * @param checkNull 是否非空检验
     * @throws Exception
     */
    protected void checkIpV4(JSONObject param, String key, boolean checkNull) throws Exception {
        String ip = param.getString(key);
        if (StringUtils.isBlank(ip)) {
            if (checkNull) {
                throw new IllegalArgumentException(key + "参数不得为空");
            } else {
                return;
            }
        }
        for (String ipV4 : ip.trim().split(",")) {
            if (StringUtils.contains(ipV4, "/")) {
                ipV4 = ipV4.substring(0, ipV4.indexOf("/"));
            }
            if (!IPFormat.checkIpValid(ipV4)) {
                throw new IllegalArgumentException(ip + "地址格式不正确");
            }
        }
    }

    /**
     * 检验ip6地址格式
     * @param param
     * @param key
     * @param checkNull 是否非空检验
     * @throws Exception
     */
    protected void checkIpV6(JSONObject param, String key, boolean checkNull) throws Exception {
        IPv6ConverUtil iPv6ConverUtil = new IPv6ConverUtil();
        String ip = param.getString(key);
        if (StringUtils.isBlank(ip)) {
            if (checkNull) {
                throw new IllegalArgumentException(key + "参数不得为空");
            } else {
                return;
            }
        }
        String[] ipArr = ip.trim().split(",");
        for (String ipV6 : ipArr) {
            if (!StringUtils.equals("true", iPv6ConverUtil.validateIPv6Format(ipV6))) {
                throw new IllegalArgumentException(ip + "地址格式不正确");
            }
        }

    }

}
