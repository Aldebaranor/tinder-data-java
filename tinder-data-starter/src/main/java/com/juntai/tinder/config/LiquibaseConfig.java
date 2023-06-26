//package com.juntai.config;
//
//import liquibase.integration.spring.SpringLiquibase;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//
///**
// * @Description: 数据库构建$
// * @Author: nemo
// * @Date: 2023/4/7 14:33
// */
//@Configuration
//public class LiquibaseConfig {
//
//    @Qualifier("dataSource")
//    private DataSource dataSource;
//
//    @Bean
//    public SpringLiquibase liquibase() {
//        SpringLiquibase liquibase = new SpringLiquibase();
//        liquibase.setDataSource(dataSource);
//        liquibase.setChangeLog("classpath:liquibase/master.xml");
//        liquibase.setContexts("development,test,preproduction,production");
//        liquibase.setShouldRun(true);
//        return liquibase;
//    }
//
//    @Autowired
//    public void setDataSource(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }
//}
