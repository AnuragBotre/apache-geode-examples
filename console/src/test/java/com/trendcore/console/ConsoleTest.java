package com.trendcore.console;

import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ConsoleTest {

    @Test
    public void rufWork() {
        Arrays.stream("showCommands;".split(";")).forEach(s -> System.out.println(" Value :- " + s));
        Arrays.stream("showCommands ;".split(";")).forEach(s -> System.out.println(" Value :- " + s));
        Arrays.stream("showCommands \r\n;".split(";")).forEach(s -> System.out.println(" Value :- " + s));

        Arrays.stream("showCommands \r\n; sf  a;; asadsa das;;".split(";")).findFirst();
    }

    @Test
    public void rufWork2() {
        String args = Arrays.stream("p q r".split(" ")).skip(1).collect(Collectors.joining(" "));
        System.out.println(args);

        args = Arrays.stream("".split(" ")).skip(1).collect(Collectors.joining(" "));
        System.out.println(args);
    }

    @Test
    public void command() {
        Console console = new Console();
        console.runQuery("showCommands;");
    }
}