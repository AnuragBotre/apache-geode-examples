package com.trendcore.cache.peertopeer.service;

import com.trendcore.cache.peertopeer.models.Role;

import java.util.stream.Stream;

public interface RoleService {


    void createRoleRegion();

    void insertRole(Role roleName);

    Stream<Role> showRoleDataForCurrentDistributedMember();
}
