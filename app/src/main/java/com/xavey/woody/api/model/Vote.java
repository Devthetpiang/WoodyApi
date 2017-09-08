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
public class Vote implements Serializable {
    private Post post;
    private Item item;
    private String voted_by;
    private String voted_on;

    public Vote() {
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getVoted_by() {
        return voted_by;
    }

    public void setVoted_by(String voted_by) {
        this.voted_by = voted_by;
    }

    public String getVoted_on() {
        return voted_on;
    }

    public void setVoted_on(String voted_on) {
        this.voted_on = voted_on;
    }
}
