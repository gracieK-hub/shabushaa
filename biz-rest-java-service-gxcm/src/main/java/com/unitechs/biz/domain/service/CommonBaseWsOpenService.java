package com.unitechs.biz.domain.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.unitechs.biz.common.utils.IPv4ConverUtil;
import com.unitechs.biz.common.utils.JsonUtil;
import com.unitechs.biz.domain.entity.atomic.BmBizCompletedWsToNotice;
import com.unitechs.biz.domain.mapper.atomic.WsAndReturnMapper;
import com.unitechs.biz.rpc.dto.req.BmBizWorksheetext;
import com.unitechs.biz.rpc.dto.req.SaveWsReq;
import com.unitechs.biz.web.req.WorkSheetSaveReq;
import com.unitechs.resource.entity.atomic.CmResDevice;
import com.unitechs.resource.exception.ServiceException;
import com.unitechs.resource.rpc.client.IResDevRpc;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.net.util.IPAddressUtil;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author fandj
 */
@Service
@Slf4j
public abstract class CommonBaseWsOpenService extends BaseWsOpenService {

    private final String CFGDEPLOY = "cfgdeploy";
    private final String VIEWCONFIGLET = "viewconfiglet";
    @Resource
    protected WsAndReturnMapper wsAndReturnMapper;
    @Autowired
    protected WsMsgQryService wsMsgQryService;
    @Autowired
    private IResDevRpc resClient;

    /**
     * 根据设备ip获取设备id并保存工单参数,双层报文时设置路径.
     *
     * @param devIpStr 报文中ip的路径
     */
    protected void dealDevIdByIp(String devIpStr, String workSheetExtCode, JSONObject param, List<BmBizWorksheetext> wsList) {
        String devIp = JsonUtil.getStringFromJson(param, devIpStr);
        log.info("jsonUrl :{},devIp:{}", devIpStr, devIp);
        BmBizWorksheetext wsItem = new BmBizWorksheetext();
        wsItem.setWsItemValue(getDevIdByIp(devIp));
        wsItem.setWsItemCode(workSheetExtCode);
        wsList.add(wsItem);
    }

    /**
     * 根据ip获取devid并校验
     *
     * @param devIp
     * @return
     */
    protected String getDevIdByIp(String devIp) {
        if (StringUtils.isEmpty(devIp)) {
            throw new IllegalArgumentException(devIp + "为空");
        }
        List<String> devIdList = resClient.getdevidbydevip(devIp);
        if (CollectionUtils.isEmpty(devIdList)) {
            throw new IllegalArgumentException(devIp + "地址再网管查询不到");
        }
        if (devIdList.size() != 1) {
            throw new IllegalArgumentException(devIp + "找到多个对应设备");
        }
        return devIdList.get(0);
    }

    /**
     * 判断工单号是否已经存在
     *
     * @param wsCode
     * @param servType
     */
    protected void wsNbrIfExist(String wsCode, String servType) {
        String flag = wsMsgQryService.wsNbrIfExist(wsCode, servType);
        if ("Y".equals(flag)) {
            throw new ServiceException("工单号已经存在");
        }
    }

    /**
     * 1.根据统一前缀替换 wsItemCode
     * 2.根据 WORKSHEET_EXT_MAP替换 wsItemCode
     * 3.wsItemCode设置为传入的报文键值
     *
     * @param param
     * @param wsItemList
     * @param CodePre    工单参数统一前缀
     * @param intoNull   false:参数为空不入.
     */
    protected void dealWorkSheetExt(JSONObject param, List<BmBizWorksheetext> wsItemList, String CodePre, boolean intoNull, boolean change) {
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            Object val = entry.getValue();
            String Key = entry.getKey();
            //双层报文处理,值为空
            if (val instanceof JSONObject ||
                    ((null == val || StringUtils.isBlank((String) val))
                            && !intoNull)) {
                continue;
            }
            BmBizWorksheetext wsItem = new BmBizWorksheetext();
            if (StringUtils.isNotBlank(CodePre) && !Key.startsWith(CodePre)) {
                wsItem.setWsItemCode(CodePre + Key);
            } else if (StringUtils.isBlank(CodePre) && WsIssueAndReturnService.WORKSHEET_EXT_MAP.containsKey(Key) && change) {
                //没有统一的前缀，根据WORKSHEET_EXT_MAP是否更换工单参数code.
                wsItem.setWsItemCode(WsIssueAndReturnService.WORKSHEET_EXT_MAP.get(Key));
            } else {
                wsItem.setWsItemCode(Key);
            }
            wsItem.setWsItemValue(String.valueOf(val));
            wsItemList.add(wsItem);
        }
    }

    /**
     * 不入空
     *
     * @param param
     * @param wsItemList
     */
    protected void dealWorkSheetExt(JSONObject param, List<BmBizWorksheetext> wsItemList) {
        dealWorkSheetExt(param, wsItemList, "", false, false);
    }

    /**
     * 处理工单
     *
     * @param param
     * @param wsItemList
     * @throws ServiceException
     */
    protected void dealWorkSheet(JSONObject param, List<BmBizWorksheetext> wsItemList, String taskType) throws ServiceException {
        WorkSheetSaveReq saveWsReq = convertParaToWsReq(param, wsItemList);
        if (!saveWorkSheet(saveWsReq)) {
            throw new ServiceException("save ws is error...");
        }
        saveCompletedWsToNotice(saveWsReq);
        String autoDeploy = wsMsgQryService.getAutoDeploy(saveWsReq.getServTypeId(), saveWsReq.getOperType());
        if (StringUtils.isNotBlank(taskType)) {
            deployWorkSheet(saveWsReq.getWsnbr(), taskType);
        } else {
            if ("Y".equals(autoDeploy)) {
                deployWorkSheet(saveWsReq.getWsnbr(), CFGDEPLOY);
            }
        }
    }

    protected void dealWorkSheet(JSONObject param, List<BmBizWorksheetext> wsItemList, JSONObject para) {
        dealWorkSheet(param, wsItemList, para.getString("TASKTYPE"));
    }


    /**
     * 保存
     *
     * @param param
     * @param wsItemList
     * @return
     * @throws ServiceException
     */
    protected WorkSheetSaveReq convertParaToWsReq(JSONObject param, List<BmBizWorksheetext> wsItemList) throws ServiceException {
        log.info("save ws is start");
        SaveWsReq saveWsReq = new SaveWsReq();
        saveWsReq.setWsnbr(getNewWsnbr());
        String servType = dealParaNull(param.getString("ServType"));
        saveWsReq.setServTypeId(servType);
        saveWsReq.setWorksheetcode(dealParaNull(param.getString("WsCode")));
        saveWsReq.setOperType(dealParaNull(param.getString("OperType")));
        saveWsReq.setSource("auto");
//        saveWsReq.setServModelId(servType + "_" + dealParaNull(param.getString("ServModel")));
        saveWsReq.setServModelId(param.getString("ServModel"));
        String username = param.getString("user_name");
        saveWsReq.setCreateUser(StringUtils.isNotBlank(username) ? username : "admin");
        saveWsReq.setWsItemList(wsItemList);
        //wangc 2023年8月14日
        WorkSheetSaveReq workSheetSaveReq = new WorkSheetSaveReq();
        BeanUtils.copyProperties(saveWsReq, workSheetSaveReq);
        workSheetSaveReq.setServcode(dealParaNull(param.getString("WsCode")));
        workSheetSaveReq.setSource("自动工单");
        workSheetSaveReq.setStatus("A");
        workSheetSaveReq.setCreateUser("admin");
        workSheetSaveReq.setOperUser("admin");
        String string = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        workSheetSaveReq.setCreateTime(string);
        workSheetSaveReq.setModifytime(string);
        workSheetSaveReq.setOpertime(string);
        workSheetSaveReq.setWsItemList(wsItemList);
        log.info("save ws is end");
        return workSheetSaveReq;
    }

    @Override
    public SaveWsReq convertParaToWsReq(JSONObject param) throws Exception {
        return null;
    }

    /**
     * 回单
     *
     * @param saveWsReq
     */
    protected void saveCompletedWsToNotice(SaveWsReq saveWsReq) {
        log.info("save CompletedWsToNotice is start");
        BmBizCompletedWsToNotice bmBizCompletedWsToNotice = new BmBizCompletedWsToNotice();
        bmBizCompletedWsToNotice.setWsnbr(saveWsReq.getWsnbr());
        bmBizCompletedWsToNotice.setWorkSheetCode(saveWsReq.getWorksheetcode());
        bmBizCompletedWsToNotice.setOperTime(new Date());
        bmBizCompletedWsToNotice.setStatus("U");
        wsAndReturnMapper.saveWsC(bmBizCompletedWsToNotice);
        log.info("save CompletedWsToNotice is end");
    }


    /**
     * 参数为空报错
     *
     * @param param
     * @param key
     * @return
     * @throws ServiceException
     */
    protected String isNullThrowException(JSONObject param, String key) throws ServiceException {
        String value = param.getString(key);
        if (StringUtils.isBlank(value)) {
            throw new ServiceException(key + "不能为空");
        }
        return value;
    }

    protected String isNullThrowException(String param, String tip) throws ServiceException {
        if (StringUtils.isBlank(param)) {
            throw new ServiceException(tip);
        }
        return param;
    }

    /**
     * 校验ip地址
     *
     * @param ip
     * @param tip
     * @throws ServiceException
     */
    protected void checkIPv4(String ip, String tip) throws ServiceException {
        if (!IPAddressUtil.isIPv4LiteralAddress(ip)) {
            throw new ServiceException(tip);
        }
    }

    /**
     * 根据devName查询devId
     *
     * @param devName
     * @return devId
     */
    protected String getDevIdByDevName(String devName) {
        CmResDevice cmResDevice = new CmResDevice();
        cmResDevice.setDevName(devName);
        List<CmResDevice> devices = resClient.getCmResDeviceByCond(cmResDevice);
        if (devices == null || devices.size() == 0) {
            throw new ServiceException("BRAS地址在网管查询不到");
        } else if (devices.size() > 1) {
            throw new ServiceException("BRAS地址找到多个对应值");
        }
        return devices.get(0).getDevId();
    }

    /**
     * 校验ServType ,servmodel,opertype
     *
     * @param param
     * @throws ServiceException
     */
    protected void checkCommonParam(JSONObject param) throws ServiceException {
        String servType = isNullThrowException(param, "ServType");
        String servModel = isNullThrowException(param, "ServModel");
        String operType = isNullThrowException(param, "OperType");
        String wsCode = isNullThrowException(param, "WsCode");
        wsMsgQryService.servTypeIfExist(servType);
        wsMsgQryService.servModelIfExist(servType, servModel);
        wsMsgQryService.operTypeIfExist(servType, operType);
        wsNbrIfExist(wsCode, servType);
    }

    /**
     * 替换值
     *
     * @param para
     * @return
     */
    protected String replacePara(String para) {
        switch (para) {
            case "mod_5":
                para = "mod_add";
                break;
            case "mod_6":
                para = "mod_del";
                break;
        }
        return para;
    }

    /**
     * 替换报文中key对应的值
     *
     * @param para
     * @param key
     * @return
     */
    protected void replacePara(JSONObject para, String key) {
        para.put(key, replacePara(para.getString(key)));
    }

    /**
     * 校验掩码
     *
     * @return
     */
    protected boolean isMask(String mask) {
        String[] split = mask.split("\\.");
        StringBuilder sb = new StringBuilder();
        for (String s : split) {
            if (s.trim().equals("")) {
                return false;
            }
            int i = Integer.parseInt(s);
            //如果有数字大于255，则直接返回false
            if (i > 255) {
                return false;
            }
            String binary = Integer.toBinaryString(i);
            //如果长度小于8，则在前面补0
            while (binary.length() < 8) {
                binary = "0".concat(binary);
            }
            sb.append(binary);
        }
        //32位二进制数中需要同时存在0和1，且不存在01
        return sb.toString().contains("1") && sb.toString().contains("0") && !sb.toString().contains("01");
    }

    protected void checkMask(JSONObject para, String key, boolean checkNull) {
        String mask = para.getString(key);
        if (checkNull && StringUtils.isBlank(mask)) {
            throw new ServiceException(key + "为空");
        }
        if (!isMask(mask)) {
            throw new ServiceException(key + "掩码格式非法");
        }
    }

    protected void checkIpV4AndMask(JSONObject para, String key) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(para.getString(key))) {
            String ipSegment = para.getString(key);
            try {
                String[] split = ipSegment.split("/");
                if (!IPAddressUtil.isIPv4LiteralAddress(split[0])) {
                    throw new ServiceException(split[0] + "ip地址格式非法");
                }
                // 定义正整数正则表达式模式
                String integerPattern = "^[1-9]\\d*$";

                // 使用Pattern类进行匹配
                Pattern pattern = Pattern.compile(integerPattern);
                if (!pattern.matcher(split[1]).matches()) {
                    throw new ServiceException(key + "掩码格式不正确");
                }
            } catch (Exception e) {
                throw new ServiceException(key + "格式非法");
            }
        }
    }
    protected void checkIpV4AndMask(String key) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(key)) {
            try {
                String[] split = key.split("/");
                if (!IPAddressUtil.isIPv4LiteralAddress(split[0])) {
                    throw new ServiceException(split[0] + "ip地址格式非法");
                }
                // 定义正整数正则表达式模式
                String integerPattern = "^[1-9]\\d*$";

                // 使用Pattern类进行匹配
                Pattern pattern = Pattern.compile(integerPattern);
                if (!pattern.matcher(split[1]).matches()) {
                    throw new ServiceException(key + "掩码格式不正确");
                }
            } catch (Exception e) {
                throw new ServiceException(key + "格式非法");
            }
        }
    }

}
