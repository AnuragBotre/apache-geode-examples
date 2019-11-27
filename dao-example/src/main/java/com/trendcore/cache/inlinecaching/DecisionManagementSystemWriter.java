package com.trendcore.cache.inlinecaching;

import org.apache.geode.cache.CacheWriter;
import org.apache.geode.cache.CacheWriterException;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.RegionEvent;

public class DecisionManagementSystemWriter implements CacheWriter {

    @Override
    public void beforeUpdate(EntryEvent event) throws CacheWriterException {
        System.out.println("beforeUpdate :- ");
    }

    @Override
    public void beforeCreate(EntryEvent event) throws CacheWriterException {
        System.out.println("beforeCreate :- ");
    }

    @Override
    public void beforeDestroy(EntryEvent event) throws CacheWriterException {
        System.out.println("beforeDestroy :- ");
    }

    @Override
    public void beforeRegionDestroy(RegionEvent event) throws CacheWriterException {
        System.out.println("beforeRegionDestroy :- ");
    }

    @Override
    public void beforeRegionClear(RegionEvent event) throws CacheWriterException {
        System.out.println("beforeRegionClear :- ");
    }
}
