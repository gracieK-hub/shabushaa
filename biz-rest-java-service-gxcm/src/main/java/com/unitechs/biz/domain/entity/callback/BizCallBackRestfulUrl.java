package com.unitechs.biz.domain.entity.callback;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liujie
 * @description
 * @date 2022-06-14 13:58
 **/
public class BizCallBackRestfulUrl {
    public static final String COMMON = "common";

    private Map<String,String> callbackRestfulUrlMap = new HashMap<>();

    public void set(String key,String url){
        callbackRestfulUrlMap.put(key,url);
    }

    public String getCallBackRestfulUrlByServTypeId(String servTypeId){
        if(callbackRestfulUrlMap.containsKey(servTypeId)){
            return callbackRestfulUrlMap.get(servTypeId);
        }
        else{
            return callbackRestfulUrlMap.get(COMMON);
        }
    }
}
