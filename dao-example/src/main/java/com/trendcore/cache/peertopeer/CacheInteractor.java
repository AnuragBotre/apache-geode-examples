package com.trendcore.cache.peertopeer;

import com.trendcore.console.Console;
import com.trendcore.console.commands.*;
import com.trendcore.console.parsers.ArgumentParser;
import com.trendcore.core.domain.Person;
import org.apache.geode.cache.control.RebalanceOperation;
import org.apache.geode.cache.server.CacheServer;
import org.apache.geode.distributed.DistributedMember;

import java.util.*;
import java.util.stream.Stream;

import static com.trendcore.lang.LangDLS.*;

public class CacheInteractor {

    CacheApplication cacheApplication;

    class ResultHolder {
        Result result;
    }

    public CacheInteractor(CacheApplication cacheApplication) {
        this.cacheApplication = cacheApplication;
    }

    public void cacheInteractorWithConsoleApp() {

        Console console = new Console();

        console.addCommand("listCacheServers", () -> {

            Command command = new Command() {
                @Override
                public Result execute(String args, Context context) {
                    Stream<CacheServer> cacheServersStream = cacheApplication.getCacheServersStream();

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
                    cacheApplication.insertPersonRecord(firtname, lastname);
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
                    Person personRecord = cacheApplication.getPersonRecord(firstname);

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
                    cacheApplication.executePersonTransactions(start);
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
                    Stream<DistributedMember> distributedMembers = cacheApplication.getDistributedMembers();

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

        console.addCommand("showPersonDataForThisMember", () -> {
            Command command = new Command() {
                @Override
                public Result execute(String args, Context context) {

                    Stream<Person> currentMembersPersonData = cacheApplication.showPersonDataForCurrentDistributedMember();

                    return iteratorableResults(currentMembersPersonData
                                    .map(p ->
                                            Arrays.asList(p.getFirstName(),
                                                    p.getLastName(),
                                                    p.toString()))
                            , "Firstname", "Lastname", "Object As String");
                }

                @Override
                public String help() {
                    return "showPersonDataForThisMember;";
                }
            };
            return command;
        });

        console.addCommand("rebalancePersonRegion", () -> {
            Command command = new Command() {
                @Override
                public Result execute(String args, Context context) {

                    ResultHolder resultHolder = new ResultHolder();

                    //TODO refactor this -> modifying global values should be avoided

                    Runnable performRebalanceOperation = () -> {
                        RebalanceOperation newRebalanceOperation = cacheApplication.performRebalanceOperation("Person");
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
                    return "rebalancePersonRegion;";
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
}
