package com.unitechs.biz.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author liujie
 * @description 操作类型枚举常量
 * @date 2022-06-06 16:06
 **/
@Getter
@RequiredArgsConstructor
public enum OperateEnum {

    /**
     *
     */
    ADD("add", "开通"),
    DEL("del", "销毁"),
    MODIFY("modify", "宽带变更"),
    ACTIVE("active", "激活"),
    DE_ACTIVE("deactive", "未激活");

    private final String code;

    private final String value;
}
