package com.trendcore.cache.writethrough;

import com.trendcore.cache.peertopeer.CacheServerOne;
import com.trendcore.cache.regions.Customer;
import org.apache.geode.cache.*;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        setLogger();

        Cache cache = createCache();

        Region<String, Customer> customerRegion = getRegion(cache, "customer");

        initInMemoryCustomerDatabase();

        Customer customer1 = customerRegion.get("1");
        System.out.println("Get customer from cache " + customer1);

        executeTransaction(cache, customerRegion);

    }

    private static void executeTransaction(Cache cache, Region<String, Customer> customerRegion) {
        Map<String,Customer> transactionData = new HashMap();
        int id = 100;
        for(int i = 0 ; i < 100 ; i++) {
            Customer c = new Customer();
            c.setId(""+(id+i));
            c.setFirstName("Agent-"+(id+1));
            c.setLastName("Agent-"+(id+1));
            transactionData.put(c.getId(),c);
        }

        CacheTransactionManager cacheTransactionManager = cache.getCacheTransactionManager();

        cacheTransactionManager.setWriter(event -> {
            System.out.println("TransactionWriter::beforeCommit event is called.");
            event.getEvents().stream().forEach(cacheEvent -> {
                System.out.println("\t"+cacheEvent.getOperation() + " " + cacheEvent.getCallbackArgument());
            });
        });

        cacheTransactionManager.addListener(new TransactionListener() {
            @Override
            public void afterCommit(TransactionEvent event) {
                System.out.println("After commit event is called." + event);
                event.getEvents().stream().forEach(cacheEvent -> {
                    System.out.println("\t"+cacheEvent.getOperation() + " " + cacheEvent.getCallbackArgument());
                });
            }

            @Override
            public void afterFailedCommit(TransactionEvent event) {
                System.out.println("After commit failed event is called." + event);
                event.getEvents().stream().forEach(cacheEvent -> {
                    System.out.println("\t"+cacheEvent.getOperation() + " " + cacheEvent.getCallbackArgument());
                });
            }

            @Override
            public void afterRollback(TransactionEvent event) {
                System.out.println("After rollback event is called." + event);
                event.getEvents().stream().forEach(cacheEvent -> {
                    System.out.println("\t"+cacheEvent.getOperation() + " " + cacheEvent.getCallbackArgument());
                });
            }
        });

        cacheTransactionManager.begin();
        customerRegion.putAll(transactionData);
        cacheTransactionManager.commit();
    }

    private static void initInMemoryCustomerDatabase() {
        CustomerDatabase instance = CustomerDatabase.getInstance();
        for(int i = 1 ; i <= 10 ; i++) {
            Customer c = new Customer();
            c.setId(""+i);
            c.setPhoneNo("101-202-3333");
            c.setFirstName("Agent-" + (i+16));
            c.setLastName("Agent-" + (i+16));
            instance.insert(c);
        }
    }

    private static Region<String, Customer> getRegion(Cache cache, String regionName) {
        RegionFactory<String, Customer> regionFactory = cache.createRegionFactory();
        regionFactory.setCacheLoader(new CustomerCacheLoader());
        regionFactory.setCacheWriter(new CustomerCacheWriter());
        return regionFactory.create(regionName);
    }

    private static Cache createCache() {
        CacheFactory cacheFactory = new CacheFactory();
        return cacheFactory.create();
    }

    private static void setLogger() {
        String file = CacheServerOne.class.getClassLoader().getResource("log4j2.xml").getFile();
        System.out.println(file);
        System.setProperty("logback.configurationFile",file);
    }

}
