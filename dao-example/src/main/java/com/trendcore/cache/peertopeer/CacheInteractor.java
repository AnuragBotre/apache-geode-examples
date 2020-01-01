package com.trendcore.cache.peertopeer;

import com.trendcore.cache.console.commands.*;
import com.trendcore.console.Console;

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

        console.addCommand("updatingUserBatch", () -> new UpdatingUserBatch(cacheApplication));

        console.addCommand("executeUserTransaction", () -> new ExecuteUserTransaction(cacheApplication));

        console.addCommand("showUserDataForThisMember", () -> new ShowUserDataForThisMember(cacheApplication));

        console.addCommand("insertUser", () -> new InsertUser(cacheApplication));

        console.addCommand("insertRole", () -> new InsertRole(cacheApplication));

        console.start();
    }


}
