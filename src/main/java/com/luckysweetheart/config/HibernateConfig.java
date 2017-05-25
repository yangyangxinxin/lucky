package com.luckysweetheart.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by yangxin on 2017/5/25.
 */
//@Configuration
public class HibernateConfig {
    @Value("${spring.hibernate.packageScan}")
    String packageScan;
    @Value("${spring.jpa.properties.hibernate.dialect}")
    String dialect;
    @Value("${spring.jpa.show-sql}")
    String showSql;
    @Value("${spring.jpa.properties.hibernate.format_sql}")
    String formatSql;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    String ddlAuto;

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
        localSessionFactoryBean.setDataSource(dataSource);
        //localSessionFactoryBean.setPackagesToScan(packageScan);
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", dialect);
        properties.setProperty("hibernate.show_sql", showSql);
        properties.setProperty("hibernate.format_sql", formatSql);
        properties.setProperty("hibernate.hbm2ddl.auto", ddlAuto);
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

}

