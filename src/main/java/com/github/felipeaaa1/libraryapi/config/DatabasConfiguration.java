package com.github.felipeaaa1.libraryapi.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabasConfiguration {

    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String user;
    @Value("${spring.datasource.password}")
    String password;
    @Value("${spring.datasource.driver-class-name}")
    String driver;

   // @Bean
    public DataSource DataSource(){
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl(url);
        ds.setUsername(user);
        ds.setPassword(password);
        ds.setDriverClassName(driver);

        return ds;
    }
    @Bean
    public DataSource hikariDataSource(){

        HikariDataSource config = new HikariDataSource();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setDriverClassName(driver);

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(1);
        config.setPoolName("library-db-pool");
        config.setMaxLifetime(6000000);
        config.setConnectionTimeout(100000);
        config.setConnectionTestQuery("select 1");

        return new HikariDataSource(config);
    }
}
