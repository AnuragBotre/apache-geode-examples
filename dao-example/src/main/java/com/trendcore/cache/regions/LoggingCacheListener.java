package com.trendcore.cache.regions;

import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.RegionEvent;

public class LoggingCacheListener implements org.apache.geode.cache.CacheListener {

    public void afterCreate(EntryEvent event) {
        System.out.println("afterCreate " + event);
    }

    public void afterUpdate(EntryEvent event) {
        System.out.println("afterUpdate " + event);
    }

    public void afterInvalidate(EntryEvent event) {
        System.out.println("afterInvalidate " + event);
    }

    public void afterDestroy(EntryEvent event) {
        System.out.println("afterDestroy " + event);
    }

    public void afterRegionInvalidate(RegionEvent event) {
        System.out.println("afterRegionInvalidate " + event);
    }

    public void afterRegionDestroy(RegionEvent event) {
        System.out.println("afterRegionDestroy " + event);
    }

    public void afterRegionClear(RegionEvent event) {
        System.out.println("afterRegionClear " + event);
    }

    public void afterRegionCreate(RegionEvent event) {
        System.out.println("afterRegionCreate " + event);
    }

    public void afterRegionLive(RegionEvent event) {
        System.out.println("afterRegionLive " + event);
    }
}
