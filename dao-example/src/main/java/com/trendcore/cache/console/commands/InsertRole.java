package com.trendcore.cache.console.commands;

import com.trendcore.cache.peertopeer.CacheApplication;
import com.trendcore.console.commands.Command;
import com.trendcore.console.commands.Context;
import com.trendcore.console.commands.Result;
import com.trendcore.console.commands.SimpleResult;

public class InsertRole implements Command {

    private CacheApplication cacheApplication;

    private String roleName;

    public InsertRole(CacheApplication cacheApplication) {
        this.cacheApplication = cacheApplication;
    }

    @Override
    public Result execute(String args, Context context) {
        cacheApplication.insertRole(roleName);
        return new SimpleResult("Role inserted successfully.");
    }

    @Override
    public String help() {
        return "insertRole <roleName>;";
    }
}
