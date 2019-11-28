package com.trendcore.cache.inlinecaching;

import org.apache.geode.cache.CacheLoader;
import org.apache.geode.cache.CacheLoaderException;
import org.apache.geode.cache.LoaderHelper;

public class DecisionManagementSystemLoader implements CacheLoader<Object, EligibilityDecision> {

    public DecisionManagementSystemLoader() {
    }

    @Override
    public EligibilityDecision load(LoaderHelper<Object, EligibilityDecision> helper) throws CacheLoaderException {

        System.out.println("DecisionManagementSystemLoader::load -> In this method you can load data from database.");
        EligibilityDecision eligibilityDecision = Database.getInstance().getDatabaseMap().get(helper.getKey());

        return eligibilityDecision;
    }

}
