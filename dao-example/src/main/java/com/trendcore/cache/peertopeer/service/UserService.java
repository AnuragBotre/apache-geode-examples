package com.trendcore.cache.peertopeer.service;

import com.trendcore.cache.peertopeer.models.User;

import java.util.stream.Stream;

public interface UserService {
    void createUserRegion();

    void executeUserTransaction(String start);

    User createUser(String username, String firstname);

    void updatingUserBatch(String start);

    Stream<User> showUserDataForCurrentDistributedMember();

    void insertUser(User user);

    void attachRoleToUser(Long userId, Long roleId);
}
