package com.trendcore.cache.server;

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
        System.out.println("Cache Server started successfully." +serverLauncher.getServerPort());
    }

}
