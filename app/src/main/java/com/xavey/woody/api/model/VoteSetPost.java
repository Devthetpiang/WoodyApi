package com.xavey.woody.api.model;

/**
 * Created by tinmaungaye on 8/22/15.
 */
public class VoteSetPost {
    private String post_id;
    private String item_id;
    private String extra;

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
