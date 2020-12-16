package com.ruoyi.web.core.config;

import com.ruoyi.system.number.DbNumberSequenceGenerator;
import com.ruoyi.system.string.DefaultStringSequenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * @author dss
 * @version 1.0.0
 * @description TODO
 * @className SequenceConfig.java
 * @createTime 2020年12月16日 21:22:00
 */
@Configuration
public class SequenceConfig {

    @Autowired
    private DataSourceTransactionManager transactionManager;
    @Bean(name = "exchOrderIdSequenceGenerator")
    public DefaultStringSequenceGenerator exchOrderIdSequenceGenerator() throws Exception {
        DefaultStringSequenceGenerator defaultStringSequenceGenerator = new DefaultStringSequenceGenerator();
        defaultStringSequenceGenerator.setLeftZeroPadding(true);
        defaultStringSequenceGenerator.setLength(6);

        DbNumberSequenceGenerator dbNumberSequenceGenerator = new DbNumberSequenceGenerator();
        dbNumberSequenceGenerator.setBeanName("exchOrderIdSequence");//数据库主键
        dbNumberSequenceGenerator.setIdkey("seq_name");//数据库主键字段
        dbNumberSequenceGenerator.setcurrentSequenceKey("seq_current");//设置数据库表当前序列字段
        dbNumberSequenceGenerator.setMaximumSequencekey("seq_maximum");//设置数据库表最大序列字段
        dbNumberSequenceGenerator.setTableName("sys_sequence");//设置数据库表名
        dbNumberSequenceGenerator.setDataSourceTransactionManager(transactionManager);
        dbNumberSequenceGenerator.setstep(5000);//设置步长
        dbNumberSequenceGenerator.afterPropertiesSet();//此种定义方式需要手动调用初始化
        defaultStringSequenceGenerator.setNumberSequenceGenerator(dbNumberSequenceGenerator);
        return defaultStringSequenceGenerator;
    }
}
