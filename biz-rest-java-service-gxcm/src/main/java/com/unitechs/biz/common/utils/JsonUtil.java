package com.unitechs.biz.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class JsonUtil {

    /**
     *  从json对象中获取指定路径对象，路径格式aaaa.bbbb.cccc
     * @param jsonObject
     * @param path
     * @return Object JSONObject/JSONArray/基础对象
     */
    public static Object getObjectFromJson(JSONObject jsonObject,String path){
        int pos = path.indexOf('.');

        if (jsonObject==null){
            return null;
        }

        if(pos==-1){
            return jsonObject.get(path);
        }
        String child = path.substring(0,pos);
        String left = path.substring(pos+1);
        Object obj=jsonObject.get(child);
        if(obj instanceof  JSONObject){
            return getObjectFromJson((JSONObject)obj, left);
        }
        else if(obj instanceof  JSONArray){
            if(((JSONArray)obj).size()!=0) {
                return getObjectFromJson(((JSONArray) obj).getJSONObject(0), left);
            }
        }
        return null;
    }

    /**
     *  从json对象中获取指定路径对象，路径格式aaaa.bbbb.cccc
     * @param jsonObject
     * @param path
     * @return Object JSONObject/JSONArray/基础对象---改进版，无视JSONArray内多对象问题造成无法获得相应对象
     */
    public static Object getObjectFromJsontwo(JSONObject jsonObject,String path){
        int pos = path.indexOf('.');

        if (jsonObject==null){
            return null;
        }

        if(pos==-1){
            return jsonObject.get(path);
        }
        String child = path.substring(0,pos);
        String left = path.substring(pos+1);
        Object obj=jsonObject.get(child);
        if(obj instanceof  JSONObject){
            return getObjectFromJson((JSONObject)obj, left);
        }
        else if(obj instanceof  JSONArray){
            if(((JSONArray)obj).size()!=0) {
                Object resultobj=null;
                for (int i=0;i<((JSONArray)obj).size();i++){
                    resultobj=getObjectFromJson(((JSONArray) obj).getJSONObject(i), left);
                    if(resultobj!=null){
                        return resultobj;
                    }
                }
                //return getObjectFromJson(((JSONArray) obj).getJSONObject(0), left);
            }
        }
        return null;
    }

    /**
     *  从json对象中获取指定路径字符串，路径格式aaaa.bbbb.cccc
     * @param jsonObject
     * @param path
     * @return String
     */
    public static String  getStringFromJson(JSONObject jsonObject,String path){
        Object o = getObjectFromJson(jsonObject, path);
        if(o!=null){
            return String.valueOf(o).trim();
        }
        return null;
    }

    /**
     * rule-index:1,2,3       --注，列表多个元素拼接，中间逗号
     * @param jsonArray json数组
     * @param key key   支持父元素.子元素，暂时支持一级
     * @return 例如1,2,3
     */
    public static String generateExtStr(JSONArray jsonArray, String key){
        String str = "";

        if(jsonArray!=null)
        for(int i = 0; i < jsonArray.size(); i++){
            if (i != 0) {
                str += ",";
            }

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String value = null;
            if (key.contains(".")) {
                String[] keyArray = key.split("\\.");
                if(keyArray.length==2) {
                    JSONObject parentObject = jsonObject.getJSONObject(keyArray[0]);
                    value = (parentObject != null) ? parentObject.getString(keyArray[1]) : null;
                }
                else{
                    JSONObject parentObject = jsonObject.getJSONObject(keyArray[0]);
                    if(parentObject!=null){
                        parentObject = parentObject.getJSONObject(keyArray[1]);
                        value = (parentObject != null) ? parentObject.getString(keyArray[2]) : null;
                    }

                }
            } else {
                value = jsonObject.getString(key);
            }

            if (value != null){  //若value值为null ， 先加上去""
                str += value;
            }

        }
        return str;
    }

    /**
     * (值为null时，不拼接进去) rule-index:1,2,3       --注，列表多个元素拼接，中间逗号
     * @param jsonArray json数组
     * @param key key   支持父元素.子元素，暂时支持一级
     * @return 例如1,2,3
     */
    public static String generateExtStrIgnoreNull(JSONArray jsonArray, String key){
        String str = "";
        if(jsonArray!=null)
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String value;
            if (key.contains(".")) {
                String[] keyArray = key.split("\\.");
                JSONObject parentObject = jsonObject.getJSONObject(keyArray[0]);
                value = (parentObject != null) ? parentObject.getString(keyArray[1]): null;
            } else {
                value = jsonObject.getString(key);
            }

            if (value != null){  //若value值为null ， 不拼接进去
                if (i != 0 && !"".equals(str)) {
                    str += ",";
                }
                str += value;
            }

        }
        return str;
    }

    public static Map<String, String> extMapnotnull(Map<String, String> extmap,String needcheckkey,String putkey,String value){

        if (StringUtils.isEmpty(extmap.get(needcheckkey))){
            return extmap;
        }else if(StringUtils.isEmpty(value)){
            return extmap;
        }
        else {
            extmap.put(putkey,value);
        }

        return extmap;
    }

    public static Map<String, String> extMaptrueorfalse(Map<String, String> extmap,String needcheckkey,String putkey){

        if (!"true".equals(extmap.get(putkey)) && StringUtils.isEmpty(extmap.get(needcheckkey))){
            extmap.put(putkey,"false");
            return extmap;
        }else {
            extmap.put(putkey,"true");
        }

        return extmap;
    }

    /**
     *  两个json合并，并且用后面json的值覆盖前面的值
     * @param src
     * @param added
     */
    public static void mergeJson2Left(JSONObject src, JSONObject added){
        if(added==null||added.isEmpty()){
            return ;
        }
        String[] keys = src.keySet().toArray(new String[0]);
        for(String key:keys){
            if(src.get(key) instanceof JSONObject && added.get(key) instanceof  JSONObject){
                mergeJson2Left(src.getJSONObject(key), added.getJSONObject(key));
                added.remove(key);
            }
        }
        src.putAll(added);
    }
   /* public static void main(String[] args){
        String json ="{\n" +
                "\t\t\"qos\": {\n" +
                "\t\t\t\"qos-profile\": {\n" +
                "\t\t\t\t\"chinaunicom-l3vpn-svc-ext:inbound-classes\": {\n" +
                "\t\t\t\t\t\"qos-class\": [\n" +
                "\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\"class-id\": \"GOLD\" \n" +
                "\t\t\t\t\t\t},\n" +
                "                        {\n" +
                "\t\t\t\t\t\t\t\"class-id\": \"GOLD\" \n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t\n" +
                "}";

        JSONObject jsonObject=JSONObject.parseObject(json);
        String x=generateExtStr((JSONArray) JsonUtil.getObjectFromJson(jsonObject,"qos.qos-profile.chinaunicom-l3vpn-svc-ext:inbound-classes.qos-class"),"class-id");

        System.out.println(x);
       // System.out.println(getObjectFromJson(jsonObject, "ietf-l3vpn-svc:l3vpn-svc.vpn-services.vpn-svc"));
       // System.out.println(getObjectFromJson(jsonObject, "ietf-l3vpn-svc:l3vpn-svc.vpn-services.vpn-svc.vpn-id"));
    }*/
}
