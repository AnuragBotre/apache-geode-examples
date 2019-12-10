package com.trendcore.console.commands;

import java.util.Map;

public interface Command {

    void execute(String args, Context context);

    default String help() {
        return "Command help";
    }
}
