package com.xavey.woody.api.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tinmaungaye on 5/6/15.
 *
 * {
 "post": {
 "_id": "5548e2eb3f83da112e4ef83f",
 "title": ""
 },
 "liked_by": null,
 "liked_on": null
 }

 */
public class UserLike implements Serializable {
    private User user;
    private User liked_by;
    private Date liked_on;

    public UserLike() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getLiked_by() {
        return liked_by;
    }

    public void setLiked_by(User liked_by) {
        this.liked_by = liked_by;
    }

    public Date getLiked_on() {
        return liked_on;
    }

    public void setLiked_on(Date liked_on) {
        this.liked_on = liked_on;
    }
}