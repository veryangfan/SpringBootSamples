package com.demo.security.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Fan at 20:40, on 2020/3/8 .
 */
@Entity
public class User{
    @Id
    private String username;
    private String password;
    private String enable;
    private String roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
