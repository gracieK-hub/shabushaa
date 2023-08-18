package com.unitechs.biz.domain.service;

import com.alibaba.fastjson.JSONObject;
import com.unitechs.biz.constant.GlobalConstant;
import com.unitechs.biz.domain.entity.atomic.BmBizCompletedWsToNotice;
import com.unitechs.biz.domain.mapper.atomic.CmBizServApiClientCfgMapper;
import com.unitechs.biz.domain.mapper.atomic.WsAndReturnMapper;
import com.unitechs.biz.rpc.dto.req.BmBizWorksheetext;
import com.unitechs.biz.rpc.dto.req.SaveWsReq;
import com.unitechs.biz.web.req.WorkSheetSaveReq;
import com.unitechs.biz.web.resp.CloudWsIssueResponse;
import com.unitechs.framework.logger.Logger;
import com.unitechs.framework.logger.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liujie
 * @date 2022年06月02日 14:39:00
 */
@Service
public class CloudWsIssueService extends BaseWsOpenService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CmBizServApiClientCfgMapper cmBizServApiClientCfgMapper;

    @Autowired
    private WsMsgQryService wsMsgQryService;


    @Resource
    private WsAndReturnMapper wsAndReturnMapper;

    public CloudWsIssueResponse cloudWsAddIssue(JSONObject param) throws Exception {
        CloudWsIssueResponse response = new CloudWsIssueResponse();
        logger.info("start method cloudWsAddIssue");
        if (null == param) {
            return response;
        }

        response.setWsCode(dealParaNull(param.getString("WsCode")));
        response.setServModel(dealParaNull(param.getString("ServModel")));
        response.setServType(dealParaNull(param.getString("ServType")));
        response.setOperType(dealParaNull(param.getString("OperType")));
        response.setChangeMode(dealParaNull(param.getString("changeMode")));
        response.setDataParseResult("0");
        response.setResult("0");
        String errMsg = "";
        try {
            // 校验参数
            validatePara(param);
            logger.info("参数校验正确");
        } catch (Exception exception) {
            errMsg = exception.getMessage();
            response.setWsStatus("F");
            response.setErrorMessage(errMsg);
            response.setDataParseResult("1");
            response.setResult("1");
            logger.info("参数校验有误, errMsg is " + errMsg);
        }

        if (StringUtils.isNotEmpty(errMsg)) {
            return response;
        }

        try {
            // 生成工单
            generateWorkSheet(param);
            logger.info("生成工单成功");
            response.setWsStatus("S");
        } catch (Exception exception) {
            errMsg = exception.getMessage();
            response.setWsStatus("F");
            response.setErrorMessage("生成工单失败");
            response.setDataParseResult("1");
            response.setResult("1");
            logger.info("生成工单失败, errMsg is " + errMsg);
        }

        logger.info("end method cloudWsAddIssue, response is " + JSONObject.toJSONString(response));
        return response;
    }

    @Override
    public void validatePara(JSONObject para) throws Exception {
        logger.info("validateInputInfo start!");
        // 通用参数校验开始
        String wsCode = dealParaNull(para.getString("WsCode"));
        String servType = dealParaNull(para.getString("ServType"));
        String OperType = dealParaNull(para.getString("OperType"));

        checkParaIsNull(para, "WsCode");
        checkParaIsNull(para, "ServModel");
        checkParaIsNull(para, "OperType");
        checkFixedValue(para, "ServType", GlobalConstant.SERVTYPE_PTNCLOUD);

        if (StringUtils.equals("Y", wsMsgQryService.wsNbrIfExist(wsCode, servType))) {
            throw new Exception("相同工单号工单不能多次下发");
        }
        if (GlobalConstant.SERVTYPE_PTNCLOUD.equals(servType)) {
            checkFixedValue(para, "ServModel", GlobalConstant.SERVMODEL_PE);
        }
        // 通用参数校验结束
        switch (OperType) {
            case "mod_1":
                validateChangeModeOne(para);
                break;
            case "mod_2":
                validateChangeModeTwo(para);
                break;
            case "mod_3":
                validateChangeModeThree(para);
                break;
            default:
                throw new IllegalArgumentException("变更类型不支持");
        }
        logger.info("validateInputInfo end!");
    }

    @Override
    public SaveWsReq convertParaToWsReq(JSONObject param) throws Exception {
        try {
            SaveWsReq saveWsReq = new SaveWsReq();
            String wsnbr = getNewWsnbr();
            saveWsReq.setWsnbr(wsnbr);
            String servType = dealParaNull(param.getString("ServType"));
            saveWsReq.setServTypeId(servType);

            String servModel = dealParaNull(param.getString("ServModel"));
            /*if (StringUtils.equals(GlobalConstant.SERVTYPE_IPONCLOUD, servType)) {
                if (StringUtils.equals(GlobalConstant.SERVMODEL_SRIP, servModel)) {
                    saveWsReq.setServModelId("IPONCLOUD_SRIP");
                }

                if (StringUtils.equals(GlobalConstant.SERVMODEL_CLOUDPE, servModel)) {
                    saveWsReq.setServModelId("IPONCLOUD_CLOUDPE");
                }

                if (StringUtils.equals(GlobalConstant.SERVMODEL_PE, servModel)) {
                    saveWsReq.setServModelId("IPONCLOUD_CLOUDPE");
                }
            }

            if (StringUtils.equals(GlobalConstant.SERVTYPE_IPTNCLOUD, servType)
                    && StringUtils.equals(GlobalConstant.SERVMODEL_CLOUDPE, servModel)) {
                saveWsReq.setServModelId("IPTNCLOUD_PE");
            }

            if (StringUtils.equals(GlobalConstant.SERVTYPE_PONCLOUD, servType)) {
                if (StringUtils.equals(GlobalConstant.SERVMODEL_SRIP, servModel)) {
                    saveWsReq.setServModelId("PONCLOUD_SRIP");
                }
                if (StringUtils.equals(GlobalConstant.SERVMODEL_PE, servModel)) {
                    saveWsReq.setServModelId("PONCLOUD_PE");
                }
            }

            if (StringUtils.equals(GlobalConstant.SERVTYPE_PTNCLOUD, servModel)) {
                if (StringUtils.equals(GlobalConstant.SERVMODEL_PE, servModel)) {
                    saveWsReq.setServModelId("PTNCLOUD_PE");
                }
            }*/
            saveWsReq.setServModelId(servType+"_"+servModel);
            String operType = dealParaNull(param.getString("OperType"));
            saveWsReq.setOperType(operType);

            saveWsReq.setWorksheetcode(dealParaNull(param.getString("WsCode")));
            String username = param.getString("user_name");
            saveWsReq.setCreateUser(StringUtils.isNotBlank(username)?username:"admin");
            saveWsReq.setSource("auto");

            List<BmBizWorksheetext> wsItemList = new ArrayList<>();
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                String wsItemCode = entry.getKey();
                Object wsItemValue = entry.getValue();
                if (wsItemValue != null && StringUtils.isNotEmpty(String.valueOf(wsItemValue))) {
                    BmBizWorksheetext wsItem = new BmBizWorksheetext();
                    wsItem.setWsItemCode(wsItemCode);
                    String value = checkUserOrCloudIps(wsItemCode, String.valueOf(wsItemValue), param);
                    wsItem.setWsItemValue(value);
                    wsItem.setServTypeId(servType);
                    wsItemList.add(wsItem);
                }
            }

            logger.info("wsItemList is " + JSONObject.toJSONString(wsItemList));
            saveWsReq.setWsItemList(wsItemList);
            return saveWsReq;
        } catch (Exception e) {
            logger.info("转换工单参数异常");
            throw e;
        }
    }

    /**
     * 增加鉴权功能
     *
     * @param username
     * @param ipAddress
     * @return
     */
    public void authentication(String username, String ipAddress) {
        logger.info("鉴权参数:username==>{}, ipAddress==>{}", username, ipAddress);
        if (StringUtils.isBlank(username) || StringUtils.isBlank(ipAddress)) {
            throw new IllegalArgumentException("调用方鉴权失败");
        }
        int authentication = cmBizServApiClientCfgMapper.authentication(username, ipAddress);
        if (authentication == 0) {
            throw new IllegalArgumentException("调用方鉴权失败");
        }
    }


    /**
     * 生成工单
     *
     * @param param
     * @throws Exception
     */
    private void generateWorkSheet(JSONObject param) throws Exception {

        logger.info("开始执行生成工单方法");
        SaveWsReq saveWsReq = convertParaToWsReq(param);
        String wsnbr = saveWsReq.getWsnbr();
        boolean flag = saveWorkSheet(saveWsReq);
        if (!flag) {
            throw new Exception("生成工单失败");
        }
        logger.info("生成工单成功");
        BmBizCompletedWsToNotice bmBizCompletedWsToNotice = new BmBizCompletedWsToNotice();
        bmBizCompletedWsToNotice.setWsnbr(wsnbr);
        bmBizCompletedWsToNotice.setWorkSheetCode(dealParaNull(param.getString("WsCode")));
        bmBizCompletedWsToNotice.setOperTime(new Date());
        bmBizCompletedWsToNotice.setStatus("U");
        wsAndReturnMapper.saveWsC(bmBizCompletedWsToNotice);

        String taskType = dealParaNull(param.getString("TASKTYPE"));
        if (StringUtils.isNotBlank(taskType)) {
            deployWorkSheet(saveWsReq.getWsnbr(), taskType);
        } else {
            String autoDeployFlag = wsMsgQryService.getAutoDeploy(dealParaNull(param.getString("ServType")),
                    dealParaNull(param.getString("OperType")));
            logger.info("autoDeployFlag is " + autoDeployFlag);
            if (StringUtils.equals("Y", autoDeployFlag)) {
                deployWorkSheet(saveWsReq.getWsnbr(), "cfgdeploy");
            }
        }

        logger.info("结束执行生成工单方法");
    }


    /**
     * 校验 changeMode = 1时参数是否正确
     *
     * @param param
     * @throws Exception
     */
    private void validateChangeModeOne(JSONObject param) throws Exception {
        logger.info("validateChangeModeOne start!");

        // 校验网 PE 参数
        checkParaIsNull(param, "PE1DownPort");
        checkParaIsNull(param, "bandwidth");
        checkParaIsNull(param, "Nbandwidth");
        checkIpV4(param, "PE1IP", true);
        checkDevice(param, "PE1IP", "PE1");

        String pe2Ip = dealParaNull(param.getString("PE2IP"));
        if (StringUtils.isNotEmpty(pe2Ip)) {
            checkIpV4(param, "PE2IP", false);
            checkDevice(param, "PE2IP", "PE2");
        }
        logger.info("validateChangeModeOne end!");
    }

    /**
     * 校验 changeMode = 2时参数是否正确
     *
     * @param param
     * @throws Exception
     */
    private void validateChangeModeTwo(JSONObject param) throws Exception {
        logger.info("validateChangeModeTwo start!");

        checkIpV4(param, "UserIP", true);
        checkIpV4(param, "NUserIP", true);
        checkParaIsNull(param, "VRFName");
        // 校验网 PE 参数
        checkIpV4(param, "PE1IP", true);
        checkDevice(param, "PE1IP", "PE1");
        checkParaIsNull(param, "PE1DownPort");
        checkIpV4(param, "PE1DownNextHopIP", true);
        String pe2Ip = dealParaNull(param.getString("PE2IP"));
        checkIpV4(param, "PE2IP", false);
        if (StringUtils.isNotBlank(pe2Ip)) {
            checkDevice(param, "PE2IP", "PE2");
        }
        checkIpV4(param, "PE2DownNextHopIP", false);
        logger.info("validateChangeModeTwo end!");
    }



    /**
     * 校验 changeMode = 3时参数是否正确
     *
     * @param param
     * @throws Exception
     */
    private void validateChangeModeThree(JSONObject param) throws Exception {
        logger.info("validateChangeModeThree start!");

        checkParaIsNull(param, "VRFName");
        checkIpV4(param, "CloudIP", true);
        checkIpV4(param, "CLOUDPE1IP", true);
        checkIpV4(param, "CLOUDPE2IP", true);
        checkDevice(param,"CLOUDPE1IP","CLOUDPE1");
        checkDevice(param,"CLOUDPE2IP","CLOUDPE2");
        checkParaIsNull(param, "CLOUDPE1Port");
        checkParaIsNull(param, "CLOUDPE2Port");

        checkIpV4(param, "CLOUDPE1NextHopIP", true);
        checkIpV4(param, "CLOUDPE2NextHopIP", true);
        checkIpV4(param, "NCloudIP", true);
        logger.info("validateChangeModeThree end!");
    }



    /**
     * worksheet表增加参数储存-UserIp和NUserIp
     *
     * @param key
     * @param value
     * @throws Exception
     * @description 取值方式-需存入worksheetext表的值
     * UserIP: 填入接口的NUserIP
     * NUserIP: 需要根据接口的NUserIP减去接口的UserIP
     * CloudIP: 接口的NCloudIP
     * NCloudIP: 需要根据接口的NCloudIP减去接口的CloudIP
     */
    private String checkUserOrCloudIps(String key, String value, JSONObject param) {
        if (null == param) {
            throw new IllegalArgumentException("请检查请求参数，不能为空");
        }
        if (StringUtils.equals("UserIP", key)) {
            return param.getString("NUserIP");
        } else if (StringUtils.equals("NUserIP", key)) {
            String nUserIp = param.getString("NUserIP");
            String userIp = param.getString("UserIP");
            List<String> collect = Arrays.stream(userIp.replaceAll(" ", "").split(","))
                    .collect(Collectors.toList());
            return Arrays.stream(nUserIp.replaceAll(" ", "").split(","))
                    .filter(e -> !collect.contains(e)).collect(Collectors.joining(","));
        } else if (StringUtils.equals("CloudIP", key)) {
            return param.getString("NCloudIP");
        } else if (StringUtils.equals("NCloudIP", key)) {
            String nCloudIp = param.getString("NCloudIP");
            String cloudIp = param.getString("CloudIP");
            List<String> collect = Arrays.stream(cloudIp.replaceAll(" ", "")
                    .split(",")).collect(Collectors.toList());
            return Arrays.stream(nCloudIp.replaceAll(" ", "").split(","))
                    .filter(e -> !collect.contains(e)).collect(Collectors.joining(","));
        } else {
            return value;
        }
    }
}
