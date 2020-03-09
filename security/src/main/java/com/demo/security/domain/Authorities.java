package com.demo.security.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Fan at 20:49, on 2020/3/8 .
 */
@Entity
public class Authorities {
    @Id
    private int id;
    private String username;
    private String authority;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
