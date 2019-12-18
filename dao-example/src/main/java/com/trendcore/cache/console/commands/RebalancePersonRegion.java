package com.trendcore.cache.console.commands;

import com.trendcore.cache.peertopeer.CacheApplication;
import com.trendcore.cache.peertopeer.CacheInteractor;
import com.trendcore.console.commands.Command;
import com.trendcore.console.commands.Context;
import com.trendcore.console.commands.Result;
import com.trendcore.console.commands.SimpleResult;
import org.apache.geode.cache.control.RebalanceOperation;

import java.util.Optional;

import static com.trendcore.lang.LangDLS.ifPresentOrElse;
import static com.trendcore.lang.LangDLS.isTrue;

public class RebalancePersonRegion implements Command {

    private CacheApplication cacheApplication;

    public RebalancePersonRegion(CacheApplication cacheApplication) {
        this.cacheApplication = cacheApplication;
    }

    class ResultHolder {
        Result result;
    }

    @Override
    public Result execute(String args, Context context) {

        ResultHolder resultHolder = new ResultHolder();

        //TODO refactor this -> modifying global values should be avoided

        Runnable performRebalanceOperation = () -> {
            RebalanceOperation newRebalanceOperation = cacheApplication.performRebalanceOperation("Person");
            context.setValue("rebalanceOperation", newRebalanceOperation);
            resultHolder.result = new SimpleResult("Rebalance Operation initiated. !!!");
        };

        RebalanceOperation r = context.getValue("rebalanceOperation", RebalanceOperation.class);
        ifPresentOrElse(Optional.ofNullable(r),
                rebalanceOperation -> isTrue(rebalanceOperation.isDone(),
                        performRebalanceOperation,
                        () -> {
                            resultHolder.result = new SimpleResult("Previous rebalance Operation is not completed.");
                        }
                ),
                () -> performRebalanceOperation.run()
        );

        return resultHolder.result;
    }

    @Override
    public String help() {
        return "rebalancePersonRegion;";
    }

}
