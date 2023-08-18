package com.unitechs.biz.domain.service;

import com.alibaba.fastjson.JSONObject;
import com.unitechs.biz.common.utils.HttpRemoteCallClient;
import com.unitechs.biz.domain.entity.callback.BizCallBackRestfulUrl;
import com.unitechs.biz.web.req.GeneralInfo;
import com.unitechs.framework.configitem.client.IConfigItemRpc;
import com.unitechs.framework.configitem.dto.domain.ConfigItem;
import com.unitechs.framework.logger.Logger;
import com.unitechs.framework.logger.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liujie
 * @description
 * @date 22022-06-14 13:58
 **/
@Service
public class BaseWsCallBackService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WsMsgQryService wsMsgQryService;

    @Autowired
    private IConfigItemRpc configItemRpc;


    public void callback(List<String> servTypeIds) {
        //获取回单数据
        List<GeneralInfo> returnWsList = wsMsgQryService.qryCallBackInfo(servTypeIds);
        //获取servtypeids 对应的回单接口url集
        BizCallBackRestfulUrl bizCallBackRestfulUrl = getAllServTypeCallBackUrlMap(servTypeIds);

        for (GeneralInfo generalInfo : returnWsList) {
            String url = bizCallBackRestfulUrl.getCallBackRestfulUrlByServTypeId(generalInfo.getServtypeid());
            if (StringUtils.isBlank(url)) {
                continue;
            }
            logger.info("回调请求地址：" + url);
            JSONObject ob = new JSONObject();
            ob.put("WsCode", dealNull(generalInfo.getWsCode()));
            ob.put("WsStatus", dealNull(generalInfo.getWsStatus()));
            ob.put("StatusTime", dealNull(generalInfo.getStatusTime()));
            ob.put("WsMessage", dealNull(generalInfo.getWsMessage()));
            ob.put("OperType", dealNull(generalInfo.getOperType()));

            logger.info("send context is :" + ob);
            String dataStr = HttpRemoteCallClient.postRemote(url, ob.toString());
            logger.info("accept context is :" + dataStr);
            JSONObject jsonObject = JSONObject.parseObject(dataStr);
            String result = jsonObject.getString("Result");
            if ("0".equals(dealNull(result))) {
                wsMsgQryService.deleteCompletedWsRecord(generalInfo.getWsCode());
            }
        }
    }

    private BizCallBackRestfulUrl getAllServTypeCallBackUrlMap(List<String> servTypeIds) {

        BizCallBackRestfulUrl bizCallBackRestfulUrl = new BizCallBackRestfulUrl();
        ConfigItem configItem = configItemRpc.queryConfigItem("WSCallBackUrl");
        if (configItem != null) {
            String url = configItem.getParaValue();
            bizCallBackRestfulUrl.set(BizCallBackRestfulUrl.COMMON, url);
        }

        for (String servTypeId : servTypeIds) {
            String callBackUrl = "WSCallBackUrl" + servTypeId;
            ConfigItem configItem2 = configItemRpc.queryConfigItem(callBackUrl);
            if (configItem2 != null) {
                String url2 = configItem2.getParaValue();
                bizCallBackRestfulUrl.set(servTypeId, url2);
            }
        }
        return bizCallBackRestfulUrl;
    }

    private String dealNull(Object str) {
        if (str == null) {
            return "";
        }
        return str.toString().trim();
    }
}
