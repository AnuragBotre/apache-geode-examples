package com.trendcore.cache.peertopeer.service;

import com.trendcore.cache.peertopeer.models.Role;
import com.trendcore.cache.peertopeer.models.User;
import com.trendcore.core.util.Util;
import org.apache.geode.CopyHelper;
import org.apache.geode.cache.*;
import org.apache.geode.cache.partition.PartitionRegionHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class UserServiceImpl implements UserService {

    public static final String ROLE_REGION_NAME = "Role";
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
            user.setId((long) (i + id));
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
        for (int i = 0; i < 60; i++) {
            User user = createUser("Agent" + (i + id), "");
            user.setId((long) (i + id));
            user.setUsername("Updated" + (i + id));
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

    @Override
    public void attachRoleToUser(Long userId, Long roleId) {
        Region<Long, User> userRegion = cache.getRegion(USER_REGION);
        Region<Long, Role> roleRegion = cache.getRegion("Role");
        CacheTransactionManager cacheTransactionManager = cache.getCacheTransactionManager();
        try {
            //Role role = roleRegion.get(roleId);

            cacheTransactionManager.setDistributed(true);
            cacheTransactionManager.begin();

            //This line is causing below exception
            //org.apache.geode.cache.UnsupportedOperationInTransactionException: Expected size of 1 {[/__PR/_B__User_101]}
            Role roleFromCache = roleRegion.get(roleId);
            Role role = CopyHelper.deepCopy(roleFromCache);

            if (role != null) {
                User user = userRegion.get(userId);
                user.addRole(role.getId());
                userRegion.put(userId, user);

                //Once the region is retrived from the cache then it has to put back.
                //then you wont get UnsupportedOperationInTransactionException.
                roleRegion.put(roleId,roleFromCache);
            }
            cacheTransactionManager.commit();
        } catch (Exception e) {
            try {
                if (cacheTransactionManager != null && cacheTransactionManager.exists())
                    cacheTransactionManager.rollback();
            } catch (Exception rbe) {

            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public Stream<User> getAllUsers() {

        Region<Long, Role> regionRole = cache.getRegion(ROLE_REGION_NAME);

        Stream<User> userStream = userRegion.values().stream().map(user -> {
            Optional
                    .ofNullable(user.getRoles())
                    .map(longObjectMap -> {
                        longObjectMap
                                .entrySet()
                                .stream()
                                .forEach(longObjectEntry ->
                                        longObjectEntry.setValue(
                                                regionRole.get(longObjectEntry.getKey())
                                        )
                                );

                        return longObjectMap;
                    }).orElse(null);
            return user;
        });
        return userStream;
    }
}
