package com.trendcore.cache.writethrough;

import com.trendcore.cache.regions.Customer;
import org.apache.geode.cache.CacheWriterException;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.RegionEvent;

public class CustomerCacheWriter implements org.apache.geode.cache.CacheWriter<String, com.trendcore.cache.regions.Customer> {

    @Override
    public void beforeUpdate(EntryEvent<String, Customer> event) throws CacheWriterException {
        System.out.println("CustomerCacheWriter::beforeUpdate event called. Params Key :- " + event.getKey() + " , New Value " + event.getNewValue() + " , Old Value " + event.getOldValue());
        CustomerDatabase.getInstance().update(event.getNewValue());
    }

    @Override
    public void beforeCreate(EntryEvent<String, Customer> event) throws CacheWriterException {
        System.out.println("CustomerCacheWriter::beforeCreate event called. Params Key :- " + event.getKey() + " , New Value " + event.getNewValue() + " , Old Value " + event.getOldValue());
        CustomerDatabase.getInstance().insert(event.getNewValue());
    }

    @Override
    public void beforeDestroy(EntryEvent<String, Customer> event) throws CacheWriterException {
        System.out.println("CustomerCacheWriter::beforeDestroy Key :- " + event.getKey() + " , New Value " + event.getNewValue() + " , Old Value " + event.getOldValue());
    }

    @Override
    public void beforeRegionDestroy(RegionEvent<String, Customer> event) throws CacheWriterException {

    }

    @Override
    public void beforeRegionClear(RegionEvent<String, Customer> event) throws CacheWriterException {

    }
}
