package com.trendcore.console.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Context {

    Map<String,Object> values = new HashMap<>();

    public Scanner getScanner() {
        return new Scanner(System.in);
    }

    public void setValue(String name,Object object){
        values.put(name,object);
    }

    public <T> T getValue(String rebalanceOperation, Class<T> rebalanceOperationClass) {
        return (T) values.get(rebalanceOperation);
    }
}
