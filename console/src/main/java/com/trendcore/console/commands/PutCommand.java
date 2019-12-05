package com.trendcore.console.commands;

import java.util.Map;

public class PutCommand implements Command {

    private String firstname;

    private String lastname;

    @Override
    public void execute(String args, Map<String, Object> context) {

        System.out.println(firstname + " " + lastname);
    }
}
