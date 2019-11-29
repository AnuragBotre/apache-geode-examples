package com.trendcore.cache.peer2peer;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.distributed.LocatorLauncher;
import org.junit.Test;

import java.util.Properties;
import java.util.Scanner;

public class PeeerToPeerConfiguration {


    @Test
    public void createLocator() {
        LocatorLauncher locatorLauncher = new LocatorLauncher.Builder()
                .setMemberName("locator1")
                .setPort(13489)
                .build();

        /*Thread t = new Thread(() -> {
            locatorLauncher.start();
        });*/


        //t.start();

        locatorLauncher.start();
        locatorLauncher.waitOnLocator();
    }


    @Test
    public void createCache1() {

        Properties properties = new Properties();
        properties.setProperty("locators", "localhost[13489]");
        properties.setProperty("mcast-address", "224.0.0.0");
        properties.setProperty("mcast-port", "0");
        CacheFactory cacheFactory = new CacheFactory(properties);
        Cache cache = cacheFactory.create();

        System.out.println(" Cache, Servers size :- " + cache.getCacheServers());

        Scanner scanner = new Scanner(System.in);
        scanner.next();
    }

    @Test
    public void createCache2() {

        Properties properties = new Properties();
        properties.setProperty("locators", "localhost[13489]");
        properties.setProperty("mcast-address", "224.0.0.0");
        properties.setProperty("mcast-port", "0");
        CacheFactory cacheFactory = new CacheFactory(properties);
        Cache cache = cacheFactory.create();

        System.out.println(" Another Cache, Servers size :- " + cache.getCacheServers());

        Scanner scanner = new Scanner(System.in);
        scanner.next();
    }
}
