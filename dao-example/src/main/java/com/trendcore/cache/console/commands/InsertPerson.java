package com.trendcore.cache.console.commands;

import com.trendcore.cache.peertopeer.CacheApplication;
import com.trendcore.console.commands.Command;
import com.trendcore.console.commands.Context;
import com.trendcore.console.commands.Result;
import com.trendcore.console.commands.SimpleResult;
import com.trendcore.console.parsers.ArgumentParser;

public class InsertPerson implements Command {

    String firtname, lastname;

    private final CacheApplication cacheApplication;

    public InsertPerson(CacheApplication cacheApplication){
        this.cacheApplication = cacheApplication;
    }

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

}
