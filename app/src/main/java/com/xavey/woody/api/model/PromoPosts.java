package com.xavey.woody.api.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hnin on 8/31/15.
 */
public class PromoPosts {
    @Expose
    private List<Post> posts =new ArrayList<>();

    @Expose
    private List<PostSet> postsets=new ArrayList<PostSet>();

    private String message;

    private int total;

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<PostSet> getPostsets() {
        return postsets;
    }

    public void setPostsets(List<PostSet> postsets) {
        this.postsets = postsets;
    }

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
}
