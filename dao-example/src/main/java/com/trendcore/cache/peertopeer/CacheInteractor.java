package com.trendcore.cache.peertopeer;

import com.trendcore.cache.console.commands.*;
import com.trendcore.console.Console;
import com.trendcore.console.commands.*;
import com.trendcore.console.parsers.ArgumentParser;
import com.trendcore.core.domain.Person;
import org.apache.geode.cache.control.RebalanceOperation;
import org.apache.geode.cache.server.CacheServer;
import org.apache.geode.distributed.DistributedMember;

import java.util.*;
import java.util.stream.Stream;

import static com.trendcore.console.ConsoleDSL.iterableResults;
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

        console.addCommand("listCacheServers", () -> new ListCacheServers(cacheApplication));

        console.addCommand("insertPerson", () -> new InsertPerson(cacheApplication));

        console.addCommand("get", () -> new Get(cacheApplication));

        console.addCommand("executePersonTransactions", () -> new ExecutePersonTransactions(cacheApplication));

        console.addCommand("showDistributedMembers", () -> new ShowDistributedMembersCommand(cacheApplication));

        console.addCommand("showPersonDataForThisMember", () -> new ShowPersonDataForThisMember(cacheApplication));

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
                                    resultHolder.result = iterableResults(s, "Rebalance Operation Status", "Is Done");
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

        console.addCommand("updatingUserBatch", () -> {

            Command command = new Command() {

                String start;

                @Override
                public Result execute(String args, Context context) {
                    ArgumentParser argumentParser = new ArgumentParser();
                    argumentParser.bindArgument(this, args);
                    cacheApplication.updatingUserBatch(start);
                    return new SimpleResult("Transaction executed successfully. !!!");
                }

                @Override
                public String help() {
                    return "Execute Transaction on Cache. Ex -> executeUserTransaction start=100";
                }
            };

            return command;
        });

        console.addCommand("executeUserTransaction", () -> {

            Command command = new Command() {

                String start;

                @Override
                public Result execute(String args, Context context) {
                    ArgumentParser argumentParser = new ArgumentParser();
                    argumentParser.bindArgument(this, args);
                    cacheApplication.executeUserTransaction(start);
                    return new SimpleResult("Transaction executed successfully. !!!");
                }

                @Override
                public String help() {
                    return "Execute Transaction on Cache. Ex -> executeUserTransaction start=100";
                }
            };

            return command;
        });

        console.start();
    }


}
