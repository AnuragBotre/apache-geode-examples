package com.trendcore.cache.console.commands;

import com.trendcore.cache.peertopeer.CacheApplication;
import com.trendcore.cache.peertopeer.models.User;
import com.trendcore.console.commands.Command;
import com.trendcore.console.commands.Context;
import com.trendcore.console.commands.IterableResult;
import com.trendcore.console.commands.Result;
import com.trendcore.core.domain.Person;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static com.trendcore.console.ConsoleDSL.iterableResults;
import static java.util.Optional.ofNullable;

public class ShowUserDataForThisMember implements Command {

    private CacheApplication cacheApplication;

    public ShowUserDataForThisMember(CacheApplication cacheApplication) {
        this.cacheApplication = cacheApplication;
    }

    @Override
    public Result execute(String args, Context context) {

        Stream<User> userStream = cacheApplication.showUserDataForCurrentDistributedMember();

        return iterableResults(userStream
                        .map(user ->
                                Arrays.asList(ofNullable(user.getUsername()).orElse(""),
                                        ofNullable(user.getFirstName()).orElse(""),
                                        ofNullable(user.getLastName()).orElse(""),
                                        user.toString()))
                , "Username", "Firstname", "Lastname", "Object As String");
    }

    @Override
    public String help() {
        return "showUserDataForThisMember;";
    }
}
