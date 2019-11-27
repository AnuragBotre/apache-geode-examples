package com.trendcore.cache.inlinecaching;

import java.util.concurrent.ConcurrentHashMap;

public class Database {

    private ConcurrentHashMap<Object, EligibilityDecision> databaseMap = new ConcurrentHashMap();

    private static Database database;

    public Database() {
        for (int i = 0; i < 10; i++) {
            EligibilityDecision e = new EligibilityDecision();
            e.setId(i);
            e.setEligibilityDecisionKey(i);
            databaseMap.put(i, e);
        }
    }

    public static Database getInstance() {
        if(database == null){
            database = new Database();
        }
        return database;
    }

    public ConcurrentHashMap<Object, EligibilityDecision> getDatabaseMap() {
        return databaseMap;
    }
}
