package com.xavey.woody.api.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Followers {
    @Expose
    private String message;
    @Expose
    private int total;
    @Expose
    private List<UserLike> users = new ArrayList<UserLike>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<UserLike> getUsers() {
        return users;
    }

    public void setUsers(List<UserLike> users) {
        this.users = users;
    }
}