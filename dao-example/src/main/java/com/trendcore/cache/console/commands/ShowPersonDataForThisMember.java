package com.trendcore.cache.console.commands;

import com.trendcore.cache.peertopeer.CacheApplication;
import com.trendcore.console.commands.Command;
import com.trendcore.console.commands.Context;
import com.trendcore.console.commands.Result;
import com.trendcore.core.domain.Person;

import java.util.Arrays;
import java.util.stream.Stream;

import static com.trendcore.console.ConsoleDSL.iterableResults;

public class ShowPersonDataForThisMember implements Command {

    CacheApplication cacheApplication;

    public ShowPersonDataForThisMember(CacheApplication cacheApplication) {
        this.cacheApplication = cacheApplication;
    }

    @Override
    public Result execute(String args, Context context) {

        Stream<Person> currentMembersPersonData = cacheApplication.showPersonDataForCurrentDistributedMember();

        return iterableResults(currentMembersPersonData
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
}
