package com.trendcore.cache.console.commands;

import com.trendcore.cache.peertopeer.CacheApplication;
import com.trendcore.console.ConsoleDSL;
import com.trendcore.console.commands.Command;
import com.trendcore.console.commands.Context;
import com.trendcore.console.commands.Result;
import org.apache.geode.cache.server.CacheServer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.trendcore.console.ConsoleDSL.iterableResults;

public class ListCacheServers implements Command {

    private final CacheApplication cacheApplication            ;

    public ListCacheServers(CacheApplication cacheApplication){
        this.cacheApplication = cacheApplication;
    }

    @Override
    public Result execute(String args, Context context) {
        Stream<CacheServer> cacheServersStream = cacheApplication.getCacheServersStream();

        Stream<List<String>> listStream = cacheServersStream.map(cacheServer ->
                Arrays.asList(cacheServer.toString()));

        return iterableResults(listStream, "CacheServer");
    }

    @Override
    public String help() {
        return "List Cache Servers. listCacheServers;";
    }

}
