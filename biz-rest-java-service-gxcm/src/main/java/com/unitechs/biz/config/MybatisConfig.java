package com.unitechs.biz.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.unitechs.framework.persistence.config.MybatisBaseConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.LinkedHashMap;

@Configuration
public class MybatisConfig extends MybatisBaseConfig
{
    public @Override LinkedHashMap<String, DataSource> getTargetDataSources()
    {
        LinkedHashMap<String, DataSource> targetDataSources = new LinkedHashMap<>();
        targetDataSources.put("defaultDataSource", defaultDataSource());
        return targetDataSources;
    }
    
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.druid")
    public @Override DataSource defaultDataSource()
    {
        return DruidDataSourceBuilder.create().build();
    }
}
