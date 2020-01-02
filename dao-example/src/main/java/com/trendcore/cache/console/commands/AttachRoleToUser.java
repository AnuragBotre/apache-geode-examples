package com.trendcore.cache.console.commands;

import com.trendcore.cache.peertopeer.CacheApplication;
import com.trendcore.console.commands.Command;
import com.trendcore.console.commands.Context;
import com.trendcore.console.commands.Result;
import com.trendcore.console.commands.SimpleResult;
import com.trendcore.console.parsers.ArgumentParser;

public class AttachRoleToUser implements Command {

    private final CacheApplication cacheApplication;

    private String userId;

    private String roleId;

    public AttachRoleToUser(CacheApplication cacheApplication) {
        this.cacheApplication = cacheApplication;
    }

    @Override
    public Result execute(String args, Context context) {
        ArgumentParser argumentParser = new ArgumentParser();
        argumentParser.bindArgument(this, args);
        this.cacheApplication.attachRoleToUser(Long.parseLong(userId),Long.parseLong(roleId));
        return new SimpleResult("Role Attached successfully.");
    }

    @Override
    public String help() {
        return "attachRoleToUser userId=<userId> roleId=<roleId>";
    }
}
