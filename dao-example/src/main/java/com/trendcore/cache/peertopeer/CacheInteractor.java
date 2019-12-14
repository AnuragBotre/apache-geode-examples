package com.trendcore.cache.peertopeer;

import com.trendcore.console.Console;
import com.trendcore.console.commands.*;
import com.trendcore.console.parsers.ArgumentParser;
import com.trendcore.core.domain.Person;
import com.trendcore.core.lang.IdentifierSequence;
import org.apache.geode.cache.*;
import org.apache.geode.cache.control.RebalanceFactory;
import org.apache.geode.cache.control.RebalanceOperation;
import org.apache.geode.cache.partition.PartitionRegionHelper;
import org.apache.geode.cache.server.CacheServer;
import org.apache.geode.distributed.DistributedMember;

import java.util.*;
import java.util.stream.Stream;

import static com.trendcore.lang.LangDLS.*;

public class CacheInteractor {


    public static final String PERSON_REGION = "Person";
    private Cache cache;

    private Region<String, Person> region;

    class ResultHolder {
        Result result;
    }

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

        region = regionFactory.setPartitionAttributes(partitionAttributes).create(PERSON_REGION);

        Console console = new Console();

        console.addCommand("listCacheServers", () -> {

            Command command = new Command() {
                @Override
                public Result execute(String args, Context context) {
                    Stream<CacheServer> cacheServersStream = getCacheServersStream();

                    Stream<List<String>> listStream = cacheServersStream.map(cacheServer ->
                            Arrays.asList(cacheServer.toString()));

                    return iteratorableResults(listStream, "CacheServer");
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
                public Result execute(String args, Context context) {
                    ArgumentParser argumentParser = new ArgumentParser();
                    argumentParser.bindArgument(this, args);
                    insertPersonRecord(firtname, lastname);
                    return new SimpleResult("Record inserted successfully.");
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
                public Result execute(String args, Context context) {
                    ArgumentParser argumentParser = new ArgumentParser();
                    argumentParser.bindArgument(this, args);
                    Person personRecord = getPersonRecord(firstname);

                    Stream<List<String>> listStream = Arrays.asList(personRecord)
                            .stream()
                            .filter(person -> person != null)
                            .map(person -> Arrays.asList(person.toString()));

                    return iteratorableResults(listStream, "Firstname", "Person");
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
                public Result execute(String args, Context context) {
                    ArgumentParser argumentParser = new ArgumentParser();
                    argumentParser.bindArgument(this, args);
                    executeTransactions(start);
                    return new SimpleResult("Transaction executed successfully. !!!");
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
                public Result execute(String args, Context context) {
                    Stream<DistributedMember> distributedMembers = getDistributedMembers();

                    return iteratorableResults(
                            distributedMembers.map(distributedMember ->
                                    Arrays.asList(distributedMember.getId(), distributedMember.getName())),
                            "Id", "Name");
                }

                @Override
                public String help() {
                    return "showDistributedMembers;";
                }
            };
            return command;
        });

        console.addCommand("showDataForThisMember", () -> {
            Command command = new Command() {
                @Override
                public Result execute(String args, Context context) {
                    Region<String, Person> localData = PartitionRegionHelper.getLocalData(region);

                    return iteratorableResults(localData.values()
                                    .stream()
                                    .map(p ->
                                            Arrays.asList(p.getFirstName(),
                                                    p.getLastName(),
                                                    p.toString()))
                            , "Firstname", "Lastname", "Object As String");
                }

                @Override
                public String help() {
                    return "showDataForThisMember;";
                }
            };
            return command;
        });

        console.addCommand("rebalance", () -> {
            Command command = new Command() {
                @Override
                public Result execute(String args, Context context) {


                    ResultHolder resultHolder = new ResultHolder();

                    //TODO refactor this -> modifying global

                    Runnable performRebalanceOperation = () -> {
                        RebalanceOperation newRebalanceOperation = performRebalanceOperation();
                        context.setValue("rebalanceOperation", newRebalanceOperation);
                        resultHolder.result = new SimpleResult("Rebalance Operation initiated. !!!");
                    };

                    RebalanceOperation r = context.getValue("rebalanceOperation", RebalanceOperation.class);
                    ifPresentOrElse(Optional.ofNullable(r),
                            rebalanceOperation -> isTrue(rebalanceOperation.isDone(),
                                    performRebalanceOperation,
                                    () -> {
                                        resultHolder.result = new SimpleResult("Previous rebalance Operation is not completed.");
                                    }
                            ),
                            () -> performRebalanceOperation.run()
                    );

                    return resultHolder.result;
                }

                @Override
                public String help() {
                    return "rebalance;";
                }
            };
            return command;
        });


        console.addCommand("isRebalanceOperationIsRunning", () -> {
            Command command = new Command() {
                @Override
                public Result execute(String args, Context context) {

                    ResultHolder resultHolder = new ResultHolder();

                    notNull(context.getValue("rebalanceOperation", RebalanceOperation.class),
                            rebalanceOperation -> {

                                try {
                                    System.out.println("Rebalance Operation Status :- " + rebalanceOperation.getResults().toString() + " " + rebalanceOperation.isDone());
                                    List<String> strings = Arrays.asList(rebalanceOperation.getResults().toString(), "" + rebalanceOperation.isDone());
                                    Stream<List<String>> s = Arrays.asList(strings).stream();
                                    resultHolder.result = iteratorableResults(s, "Rebalance Operation Status", "Is Done");
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    );
                    return resultHolder.result;
                }

                @Override
                public String help() {
                    return "isRebalanceOperationIsRunning;";
                }
            };
            return command;
        });


        console.start();
    }

    private static IterableResult iteratorableResults(Stream<List<String>> listStream, String... columns) {
        IterableResult iterableResult = new IterableResult();
        iterableResult.data(listStream);
        iterableResult.columns(columns);
        return iterableResult;
    }

    private RebalanceOperation performRebalanceOperation() {
        Set<String> regions = new HashSet<>();
        regions.add("Person");
        RebalanceFactory rebalanceFactory = cache.getResourceManager()
                .createRebalanceFactory()
                .includeRegions(regions);

        return rebalanceFactory.start();
    }

    private Stream<DistributedMember> getDistributedMembers() {

        return cache.getDistributedSystem().
                getAllOtherMembers().stream();
                /*forEach(
                        distributedMember ->
                                System.out.println(distributedMember.getId() + " --- " + distributedMember.getName() + " --- " + cache.isServer())
                );*/
    }

    private Person getPersonRecord(String firstName) {
        return region.get(firstName);
    }

    private void insertPersonRecord(String firstName, String lastName) {
        Person person = createPerson(firstName, lastName);
        region.put(person.getFirstName(), person);
    }

    private Stream<CacheServer> getCacheServersStream() {
        List<CacheServer> cacheServers = cache.getCacheServers();
        return cacheServers.stream();
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
