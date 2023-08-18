package com.unitechs.biz.common.utils;

import java.util.UUID;

/**
 * 字符串工具类
 *
 * @author wenzy
 * @since 2020/06/12
 */
public class StringUtil {

    /**
     * 生成36位uuid
     *
     * @return uuid
     */
    public static String genUuid() {
        return UUID.randomUUID().toString();    //.replace("-", "");
    }
}
