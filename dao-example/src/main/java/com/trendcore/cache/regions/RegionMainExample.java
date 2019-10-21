package com.trendcore.cache.regions;

import org.apache.geode.cache.*;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.query.FunctionDomainException;
import org.apache.geode.cache.query.NameResolutionException;
import org.apache.geode.cache.query.QueryInvocationTargetException;
import org.apache.geode.cache.query.TypeMismatchException;

public class RegionMainExample {

    public static void main(String[] args) throws NameResolutionException, TypeMismatchException, QueryInvocationTargetException, FunctionDomainException {
        ClientCacheFactory clientCacheFactory = new ClientCacheFactory();
        clientCacheFactory.addPoolLocator("localhost",13489);
        ClientCache clientCache = clientCacheFactory.create();
        ClientRegionFactory<String,Customer> clientRegionFactory = clientCache.createClientRegionFactory(ClientRegionShortcut.PROXY);
        Region<String,Customer> customer = clientRegionFactory.create("Customer");

        Customer c = new Customer();
        c.setId("1");
        c.setFirstName("Anurag");
        c.setLastName("B");
        c.setPhoneNo("123-658-4568");
        customer.put("1",c);

        customer.get("1");
    }

}
