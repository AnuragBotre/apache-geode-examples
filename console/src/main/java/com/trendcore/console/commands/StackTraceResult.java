package com.trendcore.console.commands;

public class StackTraceResult implements Result {

    private final Exception e;

    public StackTraceResult(Exception e) {
        this.e = e;
    }

    @Override
    public void showResult() {
        System.out.println("Stack Trace of Previous operation :-");
        e.printStackTrace();
    }
}
