package com.trendcore.console.commands;

public class SimpleResult implements Result{

    String message;

    public SimpleResult(String msg) {
        message = msg;
    }

    @Override
    public void showResult() {
        System.out.println(message);
    }
}
