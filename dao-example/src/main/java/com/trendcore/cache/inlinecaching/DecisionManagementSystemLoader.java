package com.trendcore.cache.inlinecaching;

import org.apache.geode.cache.CacheLoader;
import org.apache.geode.cache.CacheLoaderException;
import org.apache.geode.cache.LoaderHelper;

public class DecisionManagementSystemLoader implements CacheLoader<Object, EligibilityDecision> {

    public DecisionManagementSystemLoader() {
    }

    @Override
    public EligibilityDecision load(LoaderHelper<Object, EligibilityDecision> helper) throws CacheLoaderException {

        EligibilityDecision eligibilityDecision = Database.getInstance().getDatabaseMap().put(helper.getKey(), (EligibilityDecision) helper.getArgument());

        return eligibilityDecision;
    }

}
