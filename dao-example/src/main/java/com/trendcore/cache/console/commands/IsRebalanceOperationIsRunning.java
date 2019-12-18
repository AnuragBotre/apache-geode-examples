package com.trendcore.cache.console.commands;

import com.trendcore.console.commands.Command;
import com.trendcore.console.commands.Context;
import com.trendcore.console.commands.Result;
import org.apache.geode.cache.control.RebalanceOperation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.trendcore.console.ConsoleDSL.iterableResults;
import static com.trendcore.lang.LangDLS.notNull;

public class IsRebalanceOperationIsRunning implements Command {

    class ResultHolder {
        Result result;
    }

    @Override
    public Result execute(String args, Context context) {

        ResultHolder resultHolder = new ResultHolder();

        notNull(context.getValue("rebalanceOperation", RebalanceOperation.class),
                rebalanceOperation -> {

                    try {
                        System.out.println("Rebalance Operation Status :- " + rebalanceOperation.getResults().toString() + " " + rebalanceOperation.isDone());
                        List<String> strings = Arrays.asList(rebalanceOperation.getResults().toString(), "" + rebalanceOperation.isDone());
                        Stream<List<String>> s = Arrays.asList(strings).stream();
                        resultHolder.result = iterableResults(s, "Rebalance Operation Status", "Is Done");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        return resultHolder.result;
    }

    @Override
    public String help() {
        return "isRebalanceOperationIsRunning;";
    }
}
