package com.xavey.woody.api.model;

import java.io.Serializable;

/**
 * Created by tinmaungaye on 8/21/15.
 */
public class PostSetHolder implements Serializable {

    private PostSet postSet;
    private String message;
    private int pageCount;

    public PostSet getPostSet() {
        return postSet;
    }

    public void setPostSet(PostSet postSet) {
        this.postSet = postSet;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
}
