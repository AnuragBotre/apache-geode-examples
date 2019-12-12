package com.trendcore.cache.peertopeer;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.distributed.ConfigurationProperties;

import java.util.Properties;

import static org.apache.geode.distributed.ConfigurationProperties.NAME;

public class CacheServerTwo {

    public static void main(String[] args) {

        String file = CacheServerOne.class.getClassLoader().getResource("log4j2-cache2.xml").getFile();

        System.out.println(file);

        System.setProperty("logback.configurationFile",file);

        Properties properties = new Properties();
        properties.setProperty("locators", "localhost[13489]");
        properties.setProperty("mcast-address", "224.0.0.0");
        properties.setProperty("mcast-port", "0");
        properties.setProperty(NAME, "cacheServer2");

        CacheFactory cacheFactory = new CacheFactory(properties);

        Cache cache = cacheFactory.create();

        CacheInteractor cacheFeatures = new CacheInteractor(cache);
        cacheFeatures.cacheInteractorWithConsoleApp();

    }

}
