package com.trendcore.cache.peertopeer;

import com.trendcore.core.domain.Person;
import com.trendcore.core.lang.IdentifierSequence;
import org.apache.geode.cache.*;
import org.apache.geode.cache.server.CacheServer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CacheFeatures {


    private Cache cache;

    private Region<String, Person> region;

    public CacheFeatures(Cache cache) {
        this.cache = cache;
    }

    public void utility() {

        Scanner scanner = new Scanner(System.in);

        RegionFactory<String, Person> regionFactory = this.cache.createRegionFactory(RegionShortcut.REPLICATE);
        region = regionFactory.create("Person");

        boolean flag = true;
        while (flag) {
            int options = scanner.nextInt();

            switch (options) {
                case 1: {
                    System.out.println("Cache Servers");
                    List<CacheServer> cacheServers = cache.getCacheServers();
                    cacheServers.stream().forEach(System.out::println);
                    break;
                }
                case 2: {
                    String firstName = scanner.next();
                    String lastName = scanner.next();
                    Person person = createPerson(firstName, lastName);
                    region.put(person.getFirstName(), person);
                }
                break;
                case 3: {
                    String firstName = scanner.next();
                    Person person = region.get(firstName);
                    System.out.println(person);
                }
                break;
                case 4:{
                    //transactions
                    try {
                        executeTransactions();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
                case 0: {
                    flag = false;
                    break;
                }
            }
        }
    }

    private void executeTransactions() {

        Map<String, Person> transactionData = new HashMap();
        int id = 100;
        for(int i = 0 ; i < 100 ; i++) {
            Person person = createPerson("Agent"+ (i + id), "");
            transactionData.put(person.getFirstName(),person);
        }

        CacheTransactionManager cacheTransactionManager = cache.getCacheTransactionManager();
        cacheTransactionManager.begin();
        region.putAll(transactionData);
        cacheTransactionManager.commit();

    }

    private Person createPerson(String firstname, String lastname) {
        Person person = new Person(firstname, lastname);
        IdentifierSequence.INSTANCE.setSequentialLongId(person);
        return person;
    }

}
