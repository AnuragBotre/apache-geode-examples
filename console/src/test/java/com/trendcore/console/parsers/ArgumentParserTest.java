package com.trendcore.console.parsers;

import com.trendcore.console.commands.Command;
import com.trendcore.console.commands.Context;
import org.junit.Test;
import org.testng.Assert;

import static org.junit.Assert.*;

public class ArgumentParserTest {

    @Test
    public void bindArguments() {
        ArgumentParser argumentParser = new ArgumentParser();

        String holder[] = new String[2];

        Command command = new Command() {

            String firstname;

            String lastname;

            @Override
            public void execute(String args, Context context) {
                System.out.println(firstname + " " + lastname);
                holder[0] = firstname;
                holder[1] = lastname;
            }
        };

        String args = "firstname=anurag lastname=b";

        argumentParser.bindArgument(command,args);

        command.execute(args,new Context());

        Assert.assertEquals("anurag",holder[0]);
        Assert.assertEquals("b",holder[1]);
    }
}