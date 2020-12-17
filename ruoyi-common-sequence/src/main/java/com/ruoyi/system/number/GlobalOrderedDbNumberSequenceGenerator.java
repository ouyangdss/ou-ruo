package com.ruoyi.system.number;

import com.ruoyi.system.NumberSequenceGenerator;
import com.ruoyi.system.exception.SequenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author dss
 * @version 1.0.0
 * @description TODO
 * @className GlobalOrderedDbNumberSequenceGenerator.java
 * @createTime 2020年12月17日 14:16:00
 */
public class GlobalOrderedDbNumberSequenceGenerator implements NumberSequenceGenerator, InitializingBean, BeanNameAware {

    private static final Logger logger = LoggerFactory.getLogger(DbNumberSequenceGenerator.class);
    /** 数据库名称ORACLE*/
    private static final String DATABASE_ORACLE = "ORACLE";
    /** 数据库名称DB2*/
    private static final String DATABASE_DB2 = "DB2";
    /** 数据库名称MYSQL*/
    private static final String DATABASE_MYSQL = "MYSQL";
    /** 通用获取下一序列的sql*/
    private static final String GENERAL_SQL_TEMPLATE = "SELECT %s.nextval FROM %s";
    /** DB2获取下一序列的sql*/
    private static final String DB2_SQL_WITHOUT_TABLE_NAME_TEMPLATE = "VALUES NEXTVAL FOR %s";
    /** ORACLE获取下一序列的sql*/
    private static final String ORACLE_SQL_WITHOUT_TABLE_NAME_TEMPLATE = "SELECT %s.nextval FROM dual";
    /** MYSQL获取下一序列的sql*/
    private static final String MYSQL_SQL_TEMPLATE = "SELECT %s('%s')";

    private static final String SEQUENCE_TYPE_SAL = "sql";
    private static final String SEQUENCE_TYPE_DB_SEQUENCE = "dbSequence";
    private static final String SEQUENCE_TYPE_FUNC = "func";
    private static final String DEFAULT_FUNC_NAME = "nextval";

    private String sql;
    private DataSource dataSource;
    private String id;
    private String seqName;
    private String tableName;
    private String seqType;
    /** 是否使用事务*/
    private boolean useTransaction;

    @Override
    public long getNumberSequence() {
        Connection conn = null;
        try {
            conn = DataSourceUtils.getConnection(dataSource);
            Long sequence  = getNextSequence(conn);
            if(sequence == null){
                throw new SequenceException("get next sequence through sql ["+sql+"] failed!");
            }
            return sequence.longValue();
        } catch (Throwable cause) {
            throw new SequenceException(cause);
        } finally {
            DataSourceUtils.releaseConnection(conn,dataSource);
        }
    }

    private Long getNextSequence(Connection conn) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Long sequence = null;
        boolean autoCommit = conn.getAutoCommit();
        try {
            if(useTransaction && autoCommit){
                conn.setAutoCommit(false);
            }
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            if(rs.next()){
                sequence = rs.getLong(1);
            }
            if(useTransaction){
                conn.commit();
            }
        } catch (SQLException e) {
            if(useTransaction){
                conn.rollback();
            }
            throw e;
        } finally {
            if(rs != null){
                rs.close();
            }
            if(stmt != null){
                stmt.close();
            }
            if(conn != null && autoCommit && useTransaction){
                conn.setAutoCommit(autoCommit);
            }
        }
        return sequence;
    }
    /**
     * 根据DatabaseMetaData确定数据库类型
     * @return
     * @throws SQLException
     */
    private String resolveDatabaseName() throws SQLException {
        Connection connection = null;
        String databaseName = "";
        try {
            connection = DataSourceUtils.getConnection(dataSource);
            String databaseProductName = connection.getMetaData().getDatabaseProductName();
            String upperDatabaseProductName = databaseProductName.toUpperCase();
            if (upperDatabaseProductName. contains (DATABASE_DB2)) {
                databaseName = DATABASE_DB2;
            } else if (upperDatabaseProductName.contains (DATABASE_ORACLE)) {
                databaseName = DATABASE_ORACLE;
            } else if (upperDatabaseProductName. contains (DATABASE_MYSQL)) {
                databaseName = DATABASE_MYSQL;
            }
            else {
                throw new SequenceException("Unsupported databse: "+ databaseProductName);
            }
        }finally {
            if (connection != null) {
                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }
        return databaseName;

    }
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(dataSource);
        if(SEQUENCE_TYPE_DB_SEQUENCE.equals(seqType)){
            if(!StringUtils.hasText(tableName)){
                String databaseName = resolveDatabaseName();
                if(DATABASE_DB2.equals(databaseName)){
                    sql = String.format(DB2_SQL_WITHOUT_TABLE_NAME_TEMPLATE,new Object[]{seqName});
                } else if(DATABASE_ORACLE.equals(databaseName)){
                    sql = String.format(ORACLE_SQL_WITHOUT_TABLE_NAME_TEMPLATE,new Object[]{seqName});
                } else {
                    throw new SequenceException("unsupported the database ["+databaseName+"], and you can use sql config instead of dbSequence to try");
                }
            } else {
              sql = String.format(GENERAL_SQL_TEMPLATE,new Object[]{seqName,tableName});
            }
        }
    }


    @Override
    public void setBeanName(String name) {
        id = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setSeqName(String seqName) {
        this.seqName = seqName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setSeqType(String seqType) {
        this.seqType = seqType;
    }

    public void setUseTransaction(boolean useTransaction) {
        this.useTransaction = useTransaction;
    }
}
