package com.trendcore.cache.console.commands;

import com.trendcore.cache.peertopeer.CacheApplication;
import com.trendcore.console.commands.Command;
import com.trendcore.console.commands.Context;
import com.trendcore.console.commands.Result;
import com.trendcore.console.commands.SimpleResult;
import com.trendcore.console.parsers.ArgumentParser;

public class InsertUser implements Command {

    private final CacheApplication cacheApplication;

    private String userName;
    private String firstName;
    private String lastName;

    public InsertUser(CacheApplication cacheApplication) {
        this.cacheApplication = cacheApplication;
    }

    @Override
    public Result execute(String args, Context context) {
        ArgumentParser argumentParser = new ArgumentParser();
        argumentParser.bindArgument(this, args);
        cacheApplication.insertUser(userName, firstName, lastName);
        return new SimpleResult("Record inserted successfully.");
    }

    @Override
    public String help() {
        return "Insert User; insertUser <firstName>,<lastName>,<userName>;";
    }
}
