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
 "item": {
 "_id": "5548e2eb3f83da112e4ef841",
 "title": ""
 },
 "voted_by": null,
 "voted_on": null
 }

 */
public class Report implements Serializable {
    private Post post;

    public Report() {
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
