package com.trendcore.cache.springboot;


import com.trendcore.core.domain.Person;
import org.apache.geode.cache.Cache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.CacheFactoryBean;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;

import java.util.Properties;

@Configuration
public class GemFireConfiguration {


    @Bean
    public Properties gemfireProperties() {
        Properties gemfireProperties = new Properties();

        gemfireProperties.setProperty("name", DaoCommandLineRunner.class.getSimpleName());
        gemfireProperties.setProperty("mcast-port", "0");
        gemfireProperties.setProperty("log-level", "warning");

        return gemfireProperties;
    }

    @Bean
    public CacheFactoryBean gemfireCache() {
        CacheFactoryBean cacheFactoryBean = new CacheFactoryBean();

        cacheFactoryBean.setProperties(gemfireProperties());

        return cacheFactoryBean;
    }

    @Bean(name = "People")
    public ReplicatedRegionFactoryBean<Long, Person> people(Cache gemfireCache) {
        ReplicatedRegionFactoryBean<Long, Person> regionFactoryBean = new ReplicatedRegionFactoryBean<>();

        regionFactoryBean.setCache(gemfireCache);
        regionFactoryBean.setName("People");
        regionFactoryBean.setPersistent(false);

        return regionFactoryBean;
    }


}
