package com.trendcore.cache.console.commands;

import com.trendcore.cache.peertopeer.CacheApplication;
import com.trendcore.cache.peertopeer.models.Role;
import com.trendcore.cache.peertopeer.models.User;
import com.trendcore.console.commands.Command;
import com.trendcore.console.commands.Context;
import com.trendcore.console.commands.Result;

import java.util.Arrays;
import java.util.stream.Stream;

import static com.trendcore.console.ConsoleDSL.iterableResults;
import static java.util.Optional.ofNullable;

public class ShowRoleDataForThisMember implements Command {

    private final CacheApplication cacheApplication;

    public ShowRoleDataForThisMember(CacheApplication cacheApplication) {
        this.cacheApplication = cacheApplication;
    }

    @Override
    public Result execute(String args, Context context) {
        Stream<Role> roleStream = cacheApplication.showRoleDataForCurrentDistributedMember();

        return iterableResults(roleStream
                        .map(role ->
                                Arrays.asList(ofNullable(role.getRoleName()).orElse(""),
                                        ofNullable(role.getRoleDesc()).orElse(""),
                                        role.toString()))
                , "Role Name", "Role Desc", "Object As String");
    }

    @Override
    public String help() {
        return "showRoleDataForThisMember;";
    }
}
