package com.trendcore.cache.peertopeer;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;

import java.util.Properties;

import static org.apache.geode.distributed.ConfigurationProperties.NAME;

public class CacheServerOne {

    public static void main(String[] args) {

        String file = CacheServerOne.class.getClassLoader().getResource("log4j2-cache1.xml").getFile();

        System.out.println(file);

        System.setProperty("logback.configurationFile",file);


        Properties properties = new Properties();
        properties.setProperty("locators", "localhost[13489]");
        properties.setProperty("mcast-address", "224.0.0.0");
        properties.setProperty("mcast-port", "0");
        properties.setProperty(NAME, "cacheServer1");

        CacheApplication cacheApplication = new CacheApplication(properties);
        cacheApplication.init();

        CacheInteractor cacheInteractor = new CacheInteractor(cacheApplication);
        cacheInteractor.cacheInteractorWithConsoleApp();
    }

}
