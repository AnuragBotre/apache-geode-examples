package com.trendcore.cache.peertopeer;

import com.trendcore.console.Console;
import com.trendcore.console.commands.Command;
import com.trendcore.console.commands.Context;
import com.trendcore.console.parsers.ArgumentParser;
import com.trendcore.core.domain.Person;
import com.trendcore.core.lang.IdentifierSequence;
import org.apache.geode.cache.*;
import org.apache.geode.cache.server.CacheServer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheFeatures {


    private Cache cache;

    private Region<String, Person> region;

    public CacheFeatures(Cache cache) {
        this.cache = cache;
    }

    public void utility() {

        RegionFactory<String, Person> regionFactory = this.cache.createRegionFactory(RegionShortcut.REPLICATE);
        region = regionFactory.create("Person");

        Console console = new Console();

        console.addCommand("listCacheServers", () -> {

            Command command = new Command() {
                @Override
                public void execute(String args, Context context) {
                    getCacheServerList();
                }

                @Override
                public String help() {
                    return "List Cache Servers";
                }
            };

            return command;
        });

        console.addCommand("insertPerson", () -> {

            Command command = new Command() {

                String firtname,lastname;
                @Override
                public void execute(String args, Context context) {
                    ArgumentParser argumentParser = new ArgumentParser();
                    argumentParser.bindArgument(this,args);
                    insertPersonRecord(firtname,lastname);
                }

                @Override
                public String help() {
                    return "insertPerson firtname=<firtname> lastname=<lastname>";
                }
            };

            return command;
        });

        console.addCommand("getPersonRecord", () -> {

            Command command = new Command() {

                String firstname;

                @Override
                public void execute(String args, Context context) {
                    ArgumentParser argumentParser = new ArgumentParser();
                    argumentParser.bindArgument(this,args);
                    showPersonRecord(firstname);
                }

                @Override
                public String help() {
                    return "getPersonRecord firstname=<firstname>";
                }
            };

            return command;
        });

        console.addCommand("executeTransaction", () -> {

            Command command = new Command() {

                @Override
                public void execute(String args, Context context) {
                    executeTransactions();
                }

                @Override
                public String help() {
                    return "Execute Transaction on Cache. \r\n executeTransaction";
                }
            };

            return command;
        });

        console.start();


        /*boolean flag = true;
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
        }*/
    }

    private void showPersonRecord(String firstName) {
        Person person = region.get(firstName);
        System.out.println(person);
    }

    private void insertPersonRecord(String firstName, String lastName) {
        Person person = createPerson(firstName, lastName);
        region.put(person.getFirstName(), person);
    }

    private void getCacheServerList() {
        System.out.println("Cache Servers");
        List<CacheServer> cacheServers = cache.getCacheServers();
        cacheServers.stream().forEach(System.out::println);
    }

    private void executeTransactions() {

        Map<String, Person> transactionData = new HashMap();
        int id = 100;
        for (int i = 0; i < 100; i++) {
            Person person = createPerson("Agent" + (i + id), "");
            transactionData.put(person.getFirstName(), person);
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
