package com.trendcore.console;

public class GroovyConsole {

    public static void main(String[] args) {
        groovy.ui.Console console = new groovy.ui.Console();
        console.run();
    }

    public static void executeTransaction(String str) {
        System.out.println(str);
    }

}
