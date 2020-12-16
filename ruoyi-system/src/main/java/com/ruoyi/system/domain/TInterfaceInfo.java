package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 t_interface_info
 *
 * @author ruoyi
 * @date 2020-11-14
 */
public class TInterfaceInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 接口主键
     */
    private Long id;

    /**
     * 接口编码
     */
    @Excel(name = "接口编码")
    private String serverCode;

    /**
     * 接口名称
     */
    @Excel(name = "接口名称")
    private String serverName;

    /**
     * 接口模块
     */
    @Excel(name = "接口模块")
    private String serverView;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setServerCode(String serverCode) {
        this.serverCode = serverCode;
    }

    public String getServerCode() {
        return serverCode;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerView(String serverView) {
        this.serverView = serverView;
    }

    public String getServerView() {
        return serverView;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("serverCode", getServerCode())
                .append("serverName", getServerName())
                .append("serverView", getServerView())
                .append("createTime", getCreateTime())
                .toString();
    }
}
