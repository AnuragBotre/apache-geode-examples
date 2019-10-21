package com.trendcore.cache.server;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.RegionFactory;
import org.apache.geode.distributed.ServerLauncher;

public class ApacheGeodeServer {

    public static void main(String[] args) {
        ServerLauncher serverLauncher = new ServerLauncher.Builder()
                .setMemberName("server1")
                .setServerPort(40000)
                //this property is important
                //which will add this server to this locator
                .set("locators","localhost[13489]")
                .build();

        serverLauncher.start();
        //Cache is the main object
        //using this object we can create regions on server
        Cache cache = serverLauncher.getCache();
        RegionFactory<Object, Object> regionFactory = cache.createRegionFactory();
        regionFactory.create("Customer");
        System.out.println("Cache Server started successfully." +serverLauncher.getServerPort());
    }

}
