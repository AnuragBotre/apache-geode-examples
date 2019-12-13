package com.trendcore.cache.peertopeer;

import com.trendcore.console.Console;
import com.trendcore.console.commands.Command;
import com.trendcore.console.commands.Context;
import com.trendcore.console.parsers.ArgumentParser;
import com.trendcore.core.domain.Person;
import com.trendcore.core.lang.IdentifierSequence;
import org.apache.geode.cache.*;
import org.apache.geode.cache.partition.PartitionRegionHelper;
import org.apache.geode.cache.server.CacheServer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheInteractor {


    private Cache cache;

    private Region<String, Person> region;

    public CacheInteractor(Cache cache) {
        this.cache = cache;
    }

    public void cacheInteractorWithConsoleApp() {

        RegionFactory<String, Person> regionFactory = this.cache.createRegionFactory(RegionShortcut.PARTITION);

        /**
         * In case of Partition region partition resolver is required for bulk insert in case of transaction.
         */
        PartitionResolver resolver = new StandardPartitionResolver();

        PartitionAttributesFactory partitionAttributesFactory = new PartitionAttributesFactory();
        //partitionAttributesFactory.setRedundantCopies(2);
        partitionAttributesFactory.setPartitionResolver(resolver);
        PartitionAttributes partitionAttributes = partitionAttributesFactory.create();

        //regionFactory.setEvictionAttributes(EvictionAttributes.createLRUEntryAttributes(200));

        region = regionFactory.setPartitionAttributes(partitionAttributes).create("Person");

        Console console = new Console();

        console.addCommand("listCacheServers", () -> {

            Command command = new Command() {
                @Override
                public void execute(String args, Context context) {
                    printCacheServerList();
                }

                @Override
                public String help() {
                    return "List Cache Servers. listCacheServers;";
                }
            };

            return command;
        });

        console.addCommand("insertPerson", () -> {

            Command command = new Command() {

                String firtname, lastname;

                @Override
                public void execute(String args, Context context) {
                    ArgumentParser argumentParser = new ArgumentParser();
                    argumentParser.bindArgument(this, args);
                    insertPersonRecord(firtname, lastname);
                }

                @Override
                public String help() {
                    return "insertPerson firtname=<firtname> lastname=<lastname>;";
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
                    argumentParser.bindArgument(this, args);
                    showPersonRecord(firstname);
                }

                @Override
                public String help() {
                    return "getPersonRecord firstname=<firstname>;";
                }
            };

            return command;
        });

        console.addCommand("executeTransaction", () -> {

            Command command = new Command() {

                String start;

                @Override
                public void execute(String args, Context context) {
                    ArgumentParser argumentParser = new ArgumentParser();
                    argumentParser.bindArgument(this, args);
                    executeTransactions(start);
                }

                @Override
                public String help() {
                    return "Execute Transaction on Cache. Ex -> executeTransaction start=100";
                }
            };

            return command;
        });

        console.addCommand("showDistributedMembers", () -> {
            Command command = new Command() {
                @Override
                public void execute(String args, Context context) {
                    printDistributedMembers();
                }

                @Override
                public String help() {
                    return "showDistributedMembers;";
                }
            };
            return command;
        });

        console.addCommand("showDataForThisMember" , () -> {
            Command command = new Command() {
                @Override
                public void execute(String args, Context context) {
                    Region<String, Person> localData = PartitionRegionHelper.getLocalData(region);
                    localData.values().stream().forEach(System.out::println);
                }

                @Override
                public String help() {
                    return "showDataForThisMember;";
                }
            };
            return command;
        });


        console.start();
    }

    private void printDistributedMembers() {

        cache.getDistributedSystem().
                getAllOtherMembers().
                forEach(
                        distributedMember ->
                                System.out.println(distributedMember.getId() + " --- " + distributedMember.getName() + " --- " + cache.isServer())
                );
    }

    private void showPersonRecord(String firstName) {
        Person person = region.get(firstName);
        System.out.println(person);
    }

    private void insertPersonRecord(String firstName, String lastName) {
        Person person = createPerson(firstName, lastName);
        region.put(person.getFirstName(), person);
    }

    private void printCacheServerList() {
        System.out.println("Cache Servers");
        List<CacheServer> cacheServers = cache.getCacheServers();
        cacheServers.stream().forEach(System.out::println);
    }

    private void executeTransactions(String start) {

        Map<String, Person> transactionData = new HashMap();
        int id = Integer.parseInt(start);
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
