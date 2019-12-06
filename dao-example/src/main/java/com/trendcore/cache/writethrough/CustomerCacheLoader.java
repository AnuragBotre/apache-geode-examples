package com.trendcore.cache.writethrough;

import com.trendcore.cache.regions.Customer;
import org.apache.geode.cache.CacheLoader;
import org.apache.geode.cache.CacheLoaderException;
import org.apache.geode.cache.LoaderHelper;

public class CustomerCacheLoader implements CacheLoader<String, Customer> {

    @Override
    public Customer load(LoaderHelper<String, Customer> helper) throws CacheLoaderException {
        System.out.println("CustomerCacheLoader::load method is called with key" + helper.getKey());
        Customer customer = CustomerDatabase.getInstance().find(helper.getKey());
        return customer;
    }
}
