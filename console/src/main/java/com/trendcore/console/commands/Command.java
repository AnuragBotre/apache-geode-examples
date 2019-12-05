package com.trendcore.console.commands;

import java.util.Map;

public interface Command {

    void execute(String args, Map<String, Object> context);

}
