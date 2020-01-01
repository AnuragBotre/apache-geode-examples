package com.trendcore.cache.peertopeer.models;

import com.trendcore.core.domain.Identifiable;

import java.util.Map;

public class Role implements Identifiable<Long> {

    private Long id;

    private String roleName;

    private String roleDesc;

    private Map<Long,Object> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public Map<Long, Object> getUsers() {
        return users;
    }

    public void setUsers(Map<Long, Object> users) {
        this.users = users;
    }
}
