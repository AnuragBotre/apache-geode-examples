package com.trendcore.cache.peertopeer;

import org.apache.geode.distributed.LocatorLauncher;

public class Locator {

    public static void main(String[] args) {
        LocatorLauncher locatorLauncher = new LocatorLauncher.Builder()
                .setMemberName("locator1")
                .setPort(13489)
                .build();

        locatorLauncher.start();
        locatorLauncher.waitOnLocator();
    }

}
