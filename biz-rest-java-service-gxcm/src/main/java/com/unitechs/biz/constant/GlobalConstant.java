package com.unitechs.biz.constant;

/**
 * @author liujie
 * @description 全局常量类
 * @date 2022-06-06 16:00
 **/
public class GlobalConstant {

    private static final String MOUDLE_INTENT = "intent";


    public static final String SERVTYPE_PONCLOUD = "PONCLOUD";
    public static final String SERVTYPE_IPONCLOUD = "IPONCLOUD";
    public static final String SERVTYPE_IPTNCLOUD = "IPTNCLOUD";
    public static final String SERVTYPE_PTNCLOUD = "PTNCLOUD";
    public static final String SERVMODEL_SRIP = "SRIP";
    public static final String SERVMODEL_CLOUDPE = "CLOUDPE";
    public static final String SERVMODEL_PE = "PE";
    public static final String SERVMODEL_BRAS = "BRAS";
    //wangc
    public static final String SERVMODEL_SR = "SR";
    public static final String SERVMODEL_CR = "CR";
    public static final String SERVMODEL_NETPE = "NETPE";

    /**
     * 云专网业务（PTN）带宽变更功能工单下发接口（北向调用接口）
     */
    public static final String INTERNET = MOUDLE_INTENT + "/internet";

    /**
     * 工单下发服务
     */
    public static final String WS_INTERNET = MOUDLE_INTENT + "/wsinternet";

    /**
     * 云专网
     */
    public static final String PON_CLOUD = MOUDLE_INTENT + "/ponCloud";

    /**
     * SPN云专线
     */
    public static final String SPN_CLOUD = MOUDLE_INTENT + "/spnCloud";

}
