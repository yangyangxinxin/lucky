package com.luckysweetheart.config;

import com.luckysweetheart.dal.DatabaseNamingStrategy;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.beans.factory.config.PropertiesFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by yangxin on 2017/5/25.
 */
@Configuration
public class HibernateConfig {

    @Value("${spring.hibernate.packageScan}")
    private String packageScan;

    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String dialect;

    @Value("${spring.jpa.show-sql}")
    private String showSql;

    @Value("${spring.jpa.properties.hibernate.format_sql}")
    private String formatSql;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource, DatabaseNamingStrategy databaseNamingStrategy) {
        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();

        localSessionFactoryBean.setDataSource(dataSource);
        localSessionFactoryBean.setPackagesToScan(packageScan);
        localSessionFactoryBean.setNamingStrategy(databaseNamingStrategy);

        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();

        Properties properties = new Properties();

        properties.setProperty("hibernate.dialect", dialect);
        properties.setProperty("hibernate.show_sql", showSql);
        properties.setProperty("hibernate.format_sql", formatSql);
        properties.setProperty("hibernate.hbm2ddl.auto", ddlAuto);
        properties.setProperty("hibernate.current_session_context_class","org.springframework.orm.hibernate4.SpringSessionContext");

        propertiesFactoryBean.setProperties(properties);
        propertiesFactoryBean.setLocalOverride(true);

        localSessionFactoryBean.setHibernateProperties(properties);

        return localSessionFactoryBean;
    }

    //配置hibernate事务处理
    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        return transactionManager;
    }

    /**
     * 数据库命名策略
     * @return
     */
    @Bean
    public DatabaseNamingStrategy databaseNamingStrategy() {
        DatabaseNamingStrategy databaseNamingStrategy = new DatabaseNamingStrategy();
        databaseNamingStrategy.setIsAddUnderscores(true);
        databaseNamingStrategy.setTablePrefix("lucky_");
        databaseNamingStrategy.setMaxLength(64);
        return databaseNamingStrategy;
    }

}

