package com.ruoyi.system.number;

import com.ruoyi.system.NumberSequenceGenerator;
import com.ruoyi.system.exception.SequenceException;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Date;


/**
 * @author dss
 * @version 1.0.0
 * @description TODO
 * @className DbNumberSequenceGenerator.java
 * @createTime 2020年12月16日 10:33:00
 */
public class DbNumberSequenceGenerator implements NumberSequenceGenerator, InitializingBean, BeanNameAware {

    private static final Logger LOG = LoggerFactory.getLogger(DbNumberSequenceGenerator.class);
    /** 数据库名称ORACLE*/
    private static final String DATABASE_ORACLE = "ORACLE";
    /** 数据库名称DB2*/
    private static final String DATABASE_DB2 = "DB2";
    /** 数据库名称MYSQL*/
    private static final String DATABASE_MYSQL = "MYSQL";
    /** 创建序列号模板*/
    private static final String SEQUENCE_CREATE_TEMPLATE = "INSERT INTO %s (%s,%s,%s) VALUES (%s,%s,%s)";
    /** DB2查询模板*/
    private static final String SEQUENCE_CONFIG_QUERY_TEMPLATE_DB2 = "SELECT %s,%s FROM %s WHERE %s=? FOR UPDATE WITH RS";
    /** DB2更新模板*/
    private static final String SEQUENCE_CONFIG_UPDATE_TEMPLATE_DB2 = "UPDATE %s SET %s=? WHERE %s=?";
    /** ORACLE查询模板*/
    private static final String SEQUENCE_CONFIG_QUERY_TEMPLATE_ORACLE = "SELECT %s,%s FROM %s WHERE %s=? FOR UPDATE";
    /** ORACLE更新模板*/
    private static final String SEQUENCE_CONFIG_UPDATE_TEMPLATE_ORACLE = "UPDATE %s SET %s=? WHERE %s=?";
    /** MYSQL查询模板*/
    private static final String SEQUENCE_CONFIG_QUERY_TEMPLATE_MYSQL = "SELECT %s,%s FROM %s WHERE %s=? FOR UPDATE";
    /** MYSQL更新模板*/
    private static final String SEQUENCE_CONFIG_UPDATE_TEMPLATE_MYSQL = "UPDATE %s SET %s=? WHERE %s=?";
    /** DB2查询上次重置时间模板*/
    private static final String SEQUENCE_RESETTIME_QUERY_TEMPLATE_DB2 = "SELECT %s FROM %s WHERE %s=? FOR UPDATE WITH RS";
    /** DB2更新上次重置时间模板*/
    private static final String SEQUENCE_RESETTIME_UPDATE_TEMPLATE_DB2 = "UPDATE %s SET %s=?,%s=? WHERE %s=?";
    /** ORACLE查询上次重置时间模板*/
    private static final String SEQUENCE_RESETTIME_QUERY_TEMPLATE_ORACLE = "SELECT %s FROM %s WHERE %s=? FOR UPDATE";
    /** ORACLE更新上次重置时间模板*/
    private static final String SEQUENCE_RESETTIME_UPDATE_TEMPLATE_ORACLE = "UPDATE %s SET %s=?,%s=? WHERE %s=?";
    /** MYSQL查询上次重置时间模板*/
    private static final String SEQUENCE_RESETTIME_QUERY_TEMPLATE_MYSQL = "SELECT %s FROM %s WHERE %s=? FOR UPDATE";
    /** MYSQL更新上次重置时间模板*/
    private static final String SEQUENCE_RESETTIME_UPDATE_TEMPLATE_MYSQL = "UPDATE %s SET %s=?,%s=? WHERE %s=?";
    /** 序列号配置表列"上次重置时间"默认值*/
    private static final String DEFAULT_LASTRESET_TIME_KEY = "LASTRESET_TIME";
    /** 序列号配置表列"当前序列号"默认值*/
    private static final String DEFAULT_CURRENT_SQUENCE_KEY = "CURRENT";
    /** 序列号配置表列"最大序列号值"默认值*/
    private static final String DEFAULT_MAXIMUM_SQUENCE_KEY = "MAXIMUM";
    /** 序列号配置表列"序列号标识"默认值*/
    private static final String DEFAULT_ID_SQUENCE_KEY = "ID";
    /** 序列号配置表名称默认值*/
    private static final String DEFAULT_TABLE_NAME = "SEQUENCE_CONFIG";
    /** 事务定义*/
    private final DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
    /** 对象锁*/
    private final Object lock = new Object();
    /** 数据库源事务控制器*/
    private DataSourceTransactionManager dataSourceTransactionManager;
    /** 数据库序列号唯一标识*/
    private String id;
    /** 序列号步长,默认为5(集群部署时，不通实例可以不一样)*/
    private long step = 5L;
    /** 当前服务器实例内存产能中最大序列号*/
    private long maxSequenceInServer = 0L;
    /** 当前服务器实例内存产能中最近使用序列号*/
    private long currentSequenceInServer = 0L;
    /** 序列号配置表列"当前序列号值"*/
    private String currentSequenceKey = DEFAULT_CURRENT_SQUENCE_KEY;
    /** 序列号配置表列"最大序列号值"*/
    private String maximumSequenceKey = DEFAULT_MAXIMUM_SQUENCE_KEY;
    /** 序列号配置表列"序列号标识"*/
    private String idKey = DEFAULT_ID_SQUENCE_KEY;
    /** 序列号配置表名称*/
    private String tableName = DEFAULT_TABLE_NAME;
    /** 序列号配置表列"上次重置时间"*/
    private String lastRestTimeSequenceKey = DEFAULT_LASTRESET_TIME_KEY;
    /** 首次启动时,检查数据库是否有指定的序列号记录,如果没有则使用该语句进行创建 TODO*/
    private String sequencceCreateSql = null;
    /** 获取sequenceConfig 查询语句*/
    private String sequenceConfigQuery = null;
    /** 更新重置时间点*/
    private String lastResetTimeUpdate = null;
    /** 获取上次重置时间*/
    private String lastResetTimeQuery = null;
    /** 获取SequenceConfig更新语句*/
    private String sequenceConfigUpdate = null;
    /** 循环重置周期*/
    private String resetCronExp;
    /** 起始值*/
    private long minSequenceValue;
    /** 最大值*/
    private long maxSequenceValue;
    /** 记录本服务器实例重置时间点*/
    private Date lastUsedTime;
    /** 重置起始值*/
    private CronExpression exp;
    /** 查询模板*/
    private String SEQUENCE_QUERY_SQL_TEMPLATE = "SELECT %s,%s FROM %s WHERE %s=?";

    private String sequenceQuerySql;

    @Override
    public void setBeanName(String name) {
        id = name;
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        String databaseName = resolveDatabaseName();
        prepareSql(databaseName, currentSequenceKey, maximumSequenceKey, idKey, tableName);
        checkDbSequence();
        if (StringUtils.hasText(resetCronExp)) {
            exp = new CronExpression (resetCronExp);
            lastUsedTime = new Date();
        }
        getSequenceBatchFromDb();
        if (maxSequenceInServer <= 0L) {
            throw new SequenceException("maxium sequence number should be greater then zero!");
        }
    }




    @Override
    public long getNumberSequence() {
        long numberSequence = 0L;
        synchronized (lock){
            boolean getFromServer = true;
            if (StringUtils.hasText(resetCronExp)) {
                Date nextComparedTime = exp.getNextValidTimeAfter(lastUsedTime);
                if (new Date().compareTo(nextComparedTime) >=0) {
                    getFromServer = false;
                }
            }

            if (getFromServer && (currentSequenceInServer < maxSequenceInServer)) {
                currentSequenceInServer = currentSequenceInServer + 1L;
                numberSequence = currentSequenceInServer;
            } else {
                getSequenceBatchFromDb();
                numberSequence = getNumberSequence();
            }

            return numberSequence;
        }
    }

    /**
     * 获取数据库中序列号；在获取一个可用值后，会增加一个步长到数据库值。当序列号即将超过配置的最大值时，重置循环使用。
     * 本方法使用数据库事务，且扩散行为为强制开启一个新的事务，否则使用已有事务时线程情况下会出现序列号重复的问题。
     */
    private void getSequenceBatchFromDb() {
        long currentSequenceInServerBefore = currentSequenceInServer;
        long maxSequenceInServerBefore = maxSequenceInServer;

        transactionDefinition.setPropagationBehavior(TransactionDefinition. PROPAGATION_REQUIRES_NEW);
        TransactionStatus txstatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        DataSource dataSource = dataSourceTransactionManager.getDataSource();

        Connection connection = null;

        try {
            connection = DataSourceUtils.getConnection(dataSource);
            if (StringUtils.hasText(resetCronExp)) {
                Date lastResetTime = getLastResetTime(connection);
                if (lastResetTime == null || new Date() .compareTo(exp.getNextValidTimeAfter (lastResetTime)) >=0) {
                    updateResetTime(connection);//同时更新时间和初始值
                    lastUsedTime = new Date();
                }
            }
            SequenceConfig config = getSequenceConfig(connection);
            long currentInDb = config.getCurrent();
            long maximumInDb = config.getMaximum();
            long nextMaxInServer = currentInDb + step;

            if (nextMaxInServer <= maximumInDb) {
                currentSequenceInServer = currentInDb;
                maxSequenceInServer = nextMaxInServer;
            } else {
                //序列号即将用完，重置循环使用
                //这种处理方式也不是很好FIXME
                currentSequenceInServer = minSequenceValue;
                maxSequenceInServer = step;
            }
            boolean success = updateSequenceConfig(connection, id, maxSequenceInServer);
            if (!success) {
                throw new SequenceException("Fail to update SequenceConfig row for ID [" + id + ", CURRENT ["
                        + maxSequenceInServer + "].");
            }
            dataSourceTransactionManager.commit(txstatus);
        } catch (SequenceException tfe){
            //reset Local variables
            currentSequenceInServer = currentSequenceInServerBefore;
            maxSequenceInServer = maxSequenceInServerBefore;
            LOG.error(tfe.getMessage(), tfe);
            //如果下面rolLBack也出异常,则会淹没原来的异常
            dataSourceTransactionManager.rollback(txstatus);
            throw tfe;
        } catch (Exception e){
            //reset Local variables
            currentSequenceInServer = currentSequenceInServerBefore;
            maxSequenceInServer = maxSequenceInServerBefore;
            LOG.error(e.getMessage(), e);//如果下面rolLBack也出异常,则会淹没原来的异常
            dataSourceTransactionManager.rollback(txstatus);
            throw new SequenceException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }



    /**
     * 检查db是否有指定的序列号记录，如果没有则进行创建
     */
    private void checkDbSequence() throws SQLException {
        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus txstatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        DataSource dataSource = dataSourceTransactionManager. getDataSource();
        Connection connection = DataSourceUtils.getConnection (dataSource);
        PreparedStatement ps = null;
        ResultSet rs = null;
        Statement statement = null;
        boolean foundResult = false;
        try {
            //TODO
            ps = connection.prepareStatement(sequenceQuerySql);
            ps.setString(  1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (foundResult) {
                    throw new SequenceException("More than one SequenceConfig row found for id [\"+id + \"]. SequenceConfig id must be unique.");
                }
                long current = rs.getLong( 1);
                long maximum = rs.getLong(  2);
                if (current > maximum) {
                    throw new SequenceException("Illegal SequenceConfig row found for id [\" +id +\"]. Current value must less than maximum.");
                }
                foundResult = true;
            }
            if (!foundResult) {
                statement = connection.createStatement();
                //TODO
                int result = statement.executeUpdate(sequencceCreateSql);
                if (result == 0) {
                    throw new SequenceException("cannot create sequence record <" + id + "> in table "+ tableName);
                }
            }
            dataSourceTransactionManager.commit(txstatus);
        } catch (RuntimeException cause){
            //避免后续的数据库异常淹没此异常
            LOG.error (cause.getMessage(), cause);
            dataSourceTransactionManager.rollback(txstatus);
            throw cause;
        } catch(SQLException cause) {
            //避免后续的数据库异常淹没此异常
            LOG.error(cause.getMessage(), cause);
            dataSourceTransactionManager.rollback(txstatus);
            throw cause;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                    if (statement != null) {
                        statement.close();
                    }
                }
            } finally {
                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }
    }

    /**
     * 查询序列号配置
     * @param connection
     * @return
     */
    private SequenceConfig getSequenceConfig(Connection connection) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Loop over results - although we are only expecting one result, since id should be unique
        boolean foundResult = false;
        SequenceConfig config = new SequenceConfig(id);
        try {
            //TODO
            ps = connection.prepareStatement (sequenceConfigQuery);
            ps.setString(  1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (foundResult) {
                    throw new SequenceException ("More than one SequenceConfig row found for id [" + id+ "]. SequenceConfig id must be unique.");
                }
                long current = rs.getLong(  1);
                long maximum = rs.getLong( 2);
                if (current> maximum) {
                    throw new SequenceException ("Illegal SequenceConfig row found for id ["+id +"]. current value must less than maximum.");
                }
                config.setCurrent (current);
                config. setMaximum(maximum);
                foundResult = true;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (!foundResult) {
                throw new SequenceException("No SequenceConfig row found for id [" + id + "].");
            }
            return config;
        }
    }

    /**
     * 更新当前可用序列号到服务器
     * @param connection
     * @param id
     * @param current
     * @return
     */
    private boolean updateSequenceConfig(Connection connection, String id, long current) throws SQLException {
        PreparedStatement ps = null;
        try {
            //TODO
            ps = connection.prepareStatement(sequenceConfigUpdate);
            ps.setLong(  1, current);
            ps.setString( 2, id);
            int affected = ps.executeUpdate();
            return affected == 1 ? true: false;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * 更新序列号重置时间到服务器
     * @param connection
     */
    private boolean updateResetTime(Connection connection)  throws SQLException {
        PreparedStatement ps = null;
        try {
            //TODO
            ps = connection.prepareStatement(lastResetTimeUpdate);
            ps.setTimestamp( 1, new Timestamp(System.currentTimeMillis()));
            ps. setLong(  2, minSequenceValue);
            ps.setString(  3, id);
            int affected = ps.executeUpdate();
            return affected == 1 ? true : false;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * 查询上次重置时间
     * @param connection
     * @return
     */
    private Date getLastResetTime(Connection connection) throws SQLException {
        Timestamp lastResetTime = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean foundResult = false;
        try {
            //TODO
            ps = connection.prepareStatement(lastResetTimeQuery);
            ps.setString(  1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (foundResult) {
                    throw new SequenceException ("More than one SequenceConfig row found for id [" + id + "]. SequenceConfig id must be unique.");
                }
                lastResetTime =rs.getTimestamp(  1);
                foundResult =true;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        if (!foundResult) {
            throw new SequenceException("No SequenceConfig row found for id [" + id +"].");
        }
        return lastResetTime;
    }

    /**
     * 根据DatabaseMetaData确定数据库类型
     * @return
     * @throws SQLException
     */
    private String resolveDatabaseName() throws SQLException {
        Connection connection = null;
        String databaseName = "";
        DataSource dataSource = dataSourceTransactionManager.getDataSource();
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

    /**
     * 根据数据库类型和配置准备查询语句和更新语句
     * @param databaseName
     * @param currentSequenceKey
     * @param maximumSequenceKey
     * @param idKey
     * @param tableName
     * @throws SQLException
     */
    private void prepareSql(String databaseName, String currentSequenceKey, String maximumSequenceKey, String idKey, String tableName) throws SQLException {
        sequencceCreateSql = String.format(SEQUENCE_CREATE_TEMPLATE, tableName, idKey, currentSequenceKey, maximumSequenceKey, "'"+
                        id+"'", minSequenceValue,maxSequenceValue);
        if (DATABASE_DB2. equals(databaseName)) {
            sequenceConfigQuery = String.format (SEQUENCE_CONFIG_QUERY_TEMPLATE_DB2, currentSequenceKey, maximumSequenceKey, tableName, idKey);
            sequenceConfigUpdate = String.format(SEQUENCE_CONFIG_UPDATE_TEMPLATE_DB2, tableName, currentSequenceKey, idKey);
            lastResetTimeQuery = String.format(SEQUENCE_RESETTIME_QUERY_TEMPLATE_DB2, lastRestTimeSequenceKey, tableName, idKey);
            lastResetTimeUpdate = String.format(SEQUENCE_RESETTIME_UPDATE_TEMPLATE_DB2, tableName, lastRestTimeSequenceKey, currentSequenceKey, idKey);
        } else if (DATABASE_ORACLE.equals (databaseName)) {
            sequenceConfigQuery = String.format(SEQUENCE_CONFIG_QUERY_TEMPLATE_ORACLE, currentSequenceKey, maximumSequenceKey, tableName, idKey);
            sequenceConfigUpdate = String.format(SEQUENCE_CONFIG_UPDATE_TEMPLATE_ORACLE, tableName, currentSequenceKey, idKey);
            lastResetTimeQuery = String.format(SEQUENCE_RESETTIME_QUERY_TEMPLATE_ORACLE, lastRestTimeSequenceKey, tableName, idKey);
            lastResetTimeUpdate = String.format (SEQUENCE_RESETTIME_UPDATE_TEMPLATE_ORACLE, tableName, lastRestTimeSequenceKey, currentSequenceKey, idKey);
        } else if (DATABASE_MYSQL . equals(databaseName)) {
            sequenceConfigQuery = String.format (SEQUENCE_CONFIG_QUERY_TEMPLATE_MYSQL, currentSequenceKey, maximumSequenceKey, tableName, idKey);
            sequenceConfigUpdate = String.format(SEQUENCE_CONFIG_UPDATE_TEMPLATE_MYSQL, tableName, currentSequenceKey, idKey);
            lastResetTimeQuery = String.format(SEQUENCE_RESETTIME_QUERY_TEMPLATE_MYSQL, lastRestTimeSequenceKey, tableName, idKey);
            lastResetTimeUpdate = String.format(SEQUENCE_RESETTIME_UPDATE_TEMPLATE_MYSQL, tableName, lastRestTimeSequenceKey,
                    currentSequenceKey, idKey);
        }
        if (!StringUtils.hasText(sequenceConfigQuery)) {
            throw new SequenceException ("sequenceConfigQuery is empty");
        }
        if (!StringUtils.hasText(sequenceConfigUpdate)) {
            throw new SequenceException ("sequenceConfigUpdate is empty");
        }
        if (!StringUtils.hasText(lastResetTimeQuery)) {
            throw new SequenceException("lastResetTimeQuery is empty");
        }
        if (!StringUtils.hasText(lastResetTimeUpdate)) {
            throw new SequenceException("lastResetTimeUpdate is empty");
        }
        sequenceQuerySql = String.format(SEQUENCE_QUERY_SQL_TEMPLATE, currentSequenceKey, maximumSequenceKey, tableName, idKey);

    }

    public void setDataSourceTransactionManager (DataSourceTransactionManager dataSourceTransactionManager) {
        Assert.notNull(dataSourceTransactionManager,"dataSourceTransactionManager is empty");
        this.dataSourceTransactionManager = dataSourceTransactionManager;
    }

    public String getId() {
        return id;
    }
    public void setstep(int step) {
        Assert.isTrue(step > 0);
        this.step = step;
    }
    public void setcurrentSequenceKey(String currentSequenceKey) {
        this.currentSequenceKey = currentSequenceKey;
    }

    public void setMaximumSequencekey(String maximumSequenceKey) {
        this.maximumSequenceKey = maximumSequenceKey;
    }
    public void setIdkey(String idkey) {
        this.idKey = idkey;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public void setLastRestTimeSequenceKey(String lastRestTimeSequencekey) {
        this.lastRestTimeSequenceKey = lastRestTimeSequencekey;
    }
    public void setResetCronExp(String resetCronExp) {
        this.resetCronExp = resetCronExp;
    }
    public void setMinSequenceValue (long minSequenceValue) {
        this.minSequenceValue = minSequenceValue;
    }
    public void setMaxSequenceValue (long maxSequencevalue) {
        this.maxSequenceValue = maxSequenceValue;
    }
}
