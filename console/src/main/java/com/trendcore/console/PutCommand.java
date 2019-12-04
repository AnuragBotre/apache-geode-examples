package com.trendcore.console;

public class PutCommand implements Command {

    private String firstname;

    private String lastname;

    @Override
    public void execute() {
        System.out.println(firstname + " " + lastname);
    }
}
