package com.trendcore.cache.console.commands;

import com.trendcore.cache.peertopeer.CacheApplication;
import com.trendcore.cache.peertopeer.CacheServerOne;
import com.trendcore.cache.peertopeer.models.Role;
import com.trendcore.cache.peertopeer.models.User;
import com.trendcore.console.commands.Context;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Collectors;

public class ShowUsersWithRolesTest {

    private CacheApplication cacheApplication;

    Context context = new Context();

    @Before
    public void setUp() throws Exception {
        cacheApplication = CacheServerOne.getCacheApplication();
    }

    @Test
    public void name() {

        User user = cacheApplication.insertUser("anurag", "anurag", "b");
        Role role = cacheApplication.insertRole("Admin");

        cacheApplication.attachRoleToUser(user.getId(),role.getId());

        cacheApplication.getAllUsers().forEach(user1 ->
                System.out.println(user1.getId() + " " + user1.getFirstName() + " " +
                    user1
                    .getRoles()
                    .entrySet()
                    .stream()
                    .map(longObjectEntry -> longObjectEntry.getKey() + " " + longObjectEntry.getValue())
                    .collect(Collectors.joining())
        ));
    }
}