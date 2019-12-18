package com.trendcore.cache.console.commands;

import com.trendcore.cache.peertopeer.CacheApplication;
import com.trendcore.console.commands.Command;
import com.trendcore.console.commands.Context;
import com.trendcore.console.commands.Result;
import org.apache.geode.distributed.DistributedMember;

import java.util.Arrays;
import java.util.stream.Stream;

import static com.trendcore.console.ConsoleDSL.iterableResults;

public class ShowDistributedMembersCommand implements Command {

    CacheApplication cacheApplication;

    public ShowDistributedMembersCommand(CacheApplication cacheApplication) {
        this.cacheApplication = cacheApplication;
    }

    @Override
    public Result execute(String args, Context context) {
        Stream<DistributedMember> distributedMembers = cacheApplication.getDistributedMembers();

        return iterableResults(
                distributedMembers.map(distributedMember ->
                        Arrays.asList(distributedMember.getId(), distributedMember.getName())),
                "Id", "Name");
    }

    @Override
    public String help() {
        return "showDistributedMembers;";
    }

}
