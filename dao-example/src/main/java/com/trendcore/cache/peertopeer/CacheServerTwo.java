package com.trendcore.cache.peertopeer;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;

import java.util.Properties;
import java.util.Scanner;

public class CacheServerTwo {

    public static void main(String[] args) {

        Properties properties = new Properties();
        //properties.setProperty("locators", "localhost[13489]");
        properties.setProperty("mcast-address", "224.0.0.0");
        properties.setProperty("mcast-port", "0");

        properties.setProperty("log-file", "CacheServerTwo.log");

        CacheFactory cacheFactory = new CacheFactory(properties);

        Cache cache = cacheFactory.create();

        Scanner scanner = new Scanner(System.in);
        scanner.next();

    }

}
