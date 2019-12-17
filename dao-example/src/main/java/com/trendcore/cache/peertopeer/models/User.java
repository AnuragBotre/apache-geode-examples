package com.trendcore.cache.peertopeer.models;

import com.trendcore.core.domain.Identifiable;

import java.io.Serializable;
import java.util.Map;

public class User implements Identifiable<Long>, Serializable {

    private Long id;

    private String username;

    private boolean enabled;

    private boolean active;

    private String firstName;

    private String lastName;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Map<Long, Object> getRoles() {
        return roles;
    }

    public void setRoles(Map<Long, Object> roles) {
        this.roles = roles;
    }
}
