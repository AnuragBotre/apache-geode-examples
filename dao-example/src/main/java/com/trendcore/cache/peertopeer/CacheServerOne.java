package com.trendcore.cache.peertopeer;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.server.CacheServer;

import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class CacheServerOne {

    public static void main(String[] args) {

        Properties properties = new Properties();
        //properties.setProperty("locators", "localhost[13489]");
        properties.setProperty("mcast-address", "224.0.0.0");
        properties.setProperty("mcast-port", "0");
        CacheFactory cacheFactory = new CacheFactory(properties);

        Cache cache = cacheFactory.create();

        Scanner scanner = new Scanner(System.in);

        boolean flag = true;
        while (flag) {
            int options = scanner.nextInt();

            switch (options) {
                case 1:
                    System.out.println("Cache Servers");
                    List<CacheServer> cacheServers = cache.getCacheServers();
                    cacheServers.stream().forEach(System.out::println);
                    break;
                case 2:
                    break;
                case 0:
                    flag = false;
                    break;
            }
        }
    }

}
