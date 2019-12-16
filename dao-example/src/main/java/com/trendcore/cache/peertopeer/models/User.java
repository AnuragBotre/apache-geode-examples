package com.trendcore.cache.peertopeer.models;

import java.util.Map;

public class User {

    private Long id;

    private String username;

    private boolean enabled;

    private boolean active;

    private boolean firstName;

    private boolean lastName;

    private Map<Long,Object> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isFirstName() {
        return firstName;
    }

    public void setFirstName(boolean firstName) {
        this.firstName = firstName;
    }

    public boolean isLastName() {
        return lastName;
    }

    public void setLastName(boolean lastName) {
        this.lastName = lastName;
    }

    public Map<Long, Object> getRoles() {
        return roles;
    }

    public void setRoles(Map<Long, Object> roles) {
        this.roles = roles;
    }
}
