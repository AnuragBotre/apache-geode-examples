package com.trendcore.cache.console.commands;

import com.trendcore.cache.peertopeer.CacheApplication;
import com.trendcore.console.commands.Command;
import com.trendcore.console.commands.Context;
import com.trendcore.console.commands.Result;
import com.trendcore.console.commands.SimpleResult;
import com.trendcore.console.parsers.ArgumentParser;

public class ExecutePersonTransactions implements Command {

    CacheApplication cacheApplication;

    String start;

    public ExecutePersonTransactions(CacheApplication cacheApplication) {
        this.cacheApplication = cacheApplication;
    }

    @Override
    public Result execute(String args, Context context) {
        ArgumentParser argumentParser = new ArgumentParser();
        argumentParser.bindArgument(this, args);
        cacheApplication.executePersonTransactions(start);
        return new SimpleResult("Transaction executed successfully. !!!");
    }

    @Override
    public String help() {
        return "Execute Transaction on Cache. Ex -> executePersonTransactions start=100";
    }
}
