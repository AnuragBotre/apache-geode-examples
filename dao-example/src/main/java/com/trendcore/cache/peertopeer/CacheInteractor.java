package com.trendcore.cache.peertopeer;

import com.trendcore.cache.console.commands.*;
import com.trendcore.console.Console;
import com.trendcore.console.commands.Command;
import com.trendcore.console.commands.Context;
import com.trendcore.console.commands.Result;
import com.trendcore.console.commands.SimpleResult;
import com.trendcore.console.parsers.ArgumentParser;

public class CacheInteractor {

    CacheApplication cacheApplication;



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

        console.addCommand("rebalancePersonRegion", () -> new RebalancePersonRegion(cacheApplication));


        console.addCommand("isRebalanceOperationIsRunning", () -> new IsRebalanceOperationIsRunning());

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
