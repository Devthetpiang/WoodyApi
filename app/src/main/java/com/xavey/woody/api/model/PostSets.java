package com.xavey.woody.api.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hnin on 8/14/15.
 */
public class PostSets  {
    private int total;
    @Expose
    private List<PostSet> postSets= new ArrayList<>();

    public List<PostSet> getPostSets() {
        return postSets;
    }

    public void setPostSets(List<PostSet> postSets) {
        this.postSets = postSets;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
