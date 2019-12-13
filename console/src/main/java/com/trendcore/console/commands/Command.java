package com.trendcore.console.commands;

import java.util.Map;

public interface Command {

    Result execute(String args, Context context);

    default String help() {
        return "Command help";
    }
}
