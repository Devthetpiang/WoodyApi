package com.xavey.woody.api.model;

import java.io.Serializable;

/**
 * Created by tinmaungaye on 8/27/15.
 */
public class UserReferral implements Serializable {
    private String phone;
    private User user;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
