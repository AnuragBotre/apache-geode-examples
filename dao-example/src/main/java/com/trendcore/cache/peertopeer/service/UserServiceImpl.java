package com.trendcore.cache.peertopeer.service;

import com.trendcore.cache.peertopeer.models.User;
import org.apache.geode.cache.*;
import org.apache.geode.cache.partition.PartitionRegionHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class UserServiceImpl implements UserService {

    private Cache cache;

    private Region<Long, User> userRegion;
    public static final String USER_REGION = "User";

    public UserServiceImpl(Cache cache) {
        this.cache = cache;
    }

    @Override
    public void createUserRegion() {
        RegionFactory<Long, User> regionFactory = this.cache.createRegionFactory(RegionShortcut.PARTITION);
        userRegion = regionFactory.create(USER_REGION);
    }

    @Override
    public void executeUserTransaction(String start) {
        Map<Long, User> transactionData = new HashMap();
        int id = Integer.parseInt(start);
        for (int i = 0; i < 100; i++) {
            User user = createUser("Agent" + (i + id), "");
            user.setId((long) (i+id));
            transactionData.put(user.getId(), user);
        }

        CacheTransactionManager cacheTransactionManager = cache.getCacheTransactionManager();
        cacheTransactionManager.setDistributed(true);
        cacheTransactionManager.begin();
        userRegion.putAll(transactionData);
        cacheTransactionManager.commit();
    }

    @Override
    public User createUser(String username, String firstname) {
        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstname);
        return user;
    }

    @Override
    public void updatingUserBatch(String start) {
        Map<Long, User> transactionData = new HashMap();
        int id = Integer.parseInt(start);
        for (int i = 0; i < 60 ; i++) {
            User user = createUser("Agent" + (i + id), "");
            user.setId((long) (i+id));
            user.setUsername("Updated" + (i+id));
            transactionData.put(user.getId(), user);
        }

        CacheTransactionManager cacheTransactionManager = cache.getCacheTransactionManager();
        cacheTransactionManager.setDistributed(true);
        cacheTransactionManager.begin();
        userRegion.putAll(transactionData);
        cacheTransactionManager.commit();
    }

    @Override
    public Stream<User> showUserDataForCurrentDistributedMember() {
        Region<Long, User> localData = PartitionRegionHelper.getLocalData(userRegion);
        return localData.values().stream();
    }

    @Override
    public void insertUser(User user) {
        CacheTransactionManager cacheTransactionManager = cache.getCacheTransactionManager();
        try {
            cacheTransactionManager.begin();
            userRegion.put(user.getId(), user);
            cacheTransactionManager.commit();
        } catch (Exception e) {
            cacheTransactionManager.rollback();
        }
    }
}
