package com.trendcore.cache.locator;

import org.apache.geode.distributed.LocatorLauncher;

public class ApacheGeodeLocator {

    public static void main(String[] args) {
        LocatorLauncher locatorLauncher = new LocatorLauncher.Builder()
                .setMemberName("locator1")
                .setPort(13489)
                .build();

        locatorLauncher.start();
        locatorLauncher.waitOnLocator();
        System.out.println("Locator successfully started");
    }

}
