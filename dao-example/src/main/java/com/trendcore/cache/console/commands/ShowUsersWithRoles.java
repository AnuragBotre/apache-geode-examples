package com.trendcore.cache.console.commands;

import com.trendcore.cache.peertopeer.CacheApplication;
import com.trendcore.console.commands.Command;
import com.trendcore.console.commands.Context;
import com.trendcore.console.commands.Result;

public class ShowUsersWithRoles implements Command {

    private CacheApplication cacheApplication;

    public ShowUsersWithRoles(CacheApplication cacheApplication) {
        this.cacheApplication = cacheApplication;
    }

    @Override
    public Result execute(String args, Context context) {
        this.cacheApplication.getAllUsers();
        return null;
    }

    @Override
    public String help() {
        return "showUsersWithRoles";
    }
}
