package com.trendcore.cache.peertopeer;

import java.util.Properties;

import static org.apache.geode.distributed.ConfigurationProperties.NAME;

public class CacheServerOne {

    public static void main(String[] args) {

        CacheApplication cacheApplication = getCacheApplication();

        CacheInteractor cacheInteractor = new CacheInteractor(cacheApplication);
        cacheInteractor.cacheInteractorWithConsoleApp();
    }

    public static CacheApplication getCacheApplication() {
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
        return cacheApplication;
    }

}
