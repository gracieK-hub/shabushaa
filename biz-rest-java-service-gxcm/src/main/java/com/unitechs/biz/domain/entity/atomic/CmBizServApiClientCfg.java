package com.unitechs.biz.domain.entity.atomic;

import com.unitechs.framework.model.entity.BaseEntity;

import java.util.Date;

/**
 * 业务调用客户端配置表
 * @author fandj
 * @Date 2021/12/14
 */
public class CmBizServApiClientCfg extends BaseEntity {

    private Integer id;
    /**客户端用户名*/
    private String username;

    private String ipAddress;
    /**失效时间*/
    private Date   expireTime;

    private char flag;

    private Date createTime;

    private Date modifyTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public char getFlag() {
        return flag;
    }

    public void setFlag(char flag) {
        this.flag = flag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
