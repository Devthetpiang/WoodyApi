package com.xavey.woody.api.model;

import java.io.Serializable;

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
public class Like implements Serializable {
    private Post post;
    private String liked_by;
    private String liked_on;

    public Like() {
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getLiked_by() {
        return liked_by;
    }

    public void setLiked_by(String liked_by) {
        this.liked_by = liked_by;
    }

    public String getLiked_on() {
        return liked_on;
    }

    public void setLiked_on(String liked_on) {
        this.liked_on = liked_on;
    }
}