package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 数据库doc配置对象 db_data_doc_conf
 *
 * @author ruoyi
 * @date 2020-12-11
 */
public class DbDataDocConf extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 参数主键
     */
    private Integer configId;

    /**
     * db名称
     */
    @Excel(name = "db名称")
    private String dbName;

    /**
     * 驱动名称
     */
    @Excel(name = "驱动名称")
    private String driverClassName;

    /**
     * db url
     */
    @Excel(name = "db url")
    private String jdbcUrl;

    /**
     * 用户名
     */
    @Excel(name = "用户名")
    private String userName;

    /**
     * 密码
     */
    @Excel(name = "密码")
    private String passWord;

    /**
     * schema名称
     */
    @Excel(name = "schema名称")
    private String schemaName;

    /**
     * 导出文件的地址
     */
    @Excel(name = "导出文件的地址")
    private String fileOutputDir;

    public void setConfigId(Integer configId) {
        this.configId = configId;
    }

    public Integer getConfigId() {
        return configId;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setFileOutputDir(String fileOutputDir) {
        this.fileOutputDir = fileOutputDir;
    }

    public String getFileOutputDir() {
        return fileOutputDir;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("configId", getConfigId())
                .append("dbName", getDbName())
                .append("driverClassName", getDriverClassName())
                .append("jdbcUrl", getJdbcUrl())
                .append("userName", getUserName())
                .append("passWord", getPassWord())
                .append("schemaName", getSchemaName())
                .append("fileOutputDir", getFileOutputDir())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
