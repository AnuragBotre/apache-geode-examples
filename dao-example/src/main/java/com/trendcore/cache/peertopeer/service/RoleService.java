package com.trendcore.cache.peertopeer.service;

import com.trendcore.cache.peertopeer.models.Role;

public interface RoleService {


    void createRoleRegion();

    void insertRole(Role roleName);
}
