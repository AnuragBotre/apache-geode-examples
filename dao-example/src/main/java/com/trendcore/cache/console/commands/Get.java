package com.trendcore.cache.console.commands;

import com.trendcore.cache.peertopeer.CacheApplication;
import com.trendcore.console.commands.Command;
import com.trendcore.console.commands.Context;
import com.trendcore.console.commands.Result;
import com.trendcore.console.parsers.ArgumentParser;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.trendcore.console.ConsoleDSL.iterableResults;

public class Get implements Command {

    String region;

    Object key;

    private CacheApplication cacheApplication;

    public Get(CacheApplication cacheApplication){
        this.cacheApplication = cacheApplication;
    }

    @Override
    public Result execute(String args, Context context) {
        ArgumentParser argumentParser = new ArgumentParser();
        argumentParser.bindArgument(this, args);
        Object record = cacheApplication.getRecord(region, key);

        Stream<List<String>> listStream = Arrays.asList(record)
                .stream()
                .filter(r -> r != null)
                .map(r -> Arrays.asList(r.toString()));

        return iterableResults(listStream, "Firstname", "Person");
    }

    @Override
    public String help() {
        return "get region=Person key=<firstname>;" +
                "get region=User key=<id>";
    }

}
