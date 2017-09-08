package com.xavey.woody.api.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tinmaungaye on 5/8/15.
 */
public class Comments {
    @Expose
    private int total;
    @Expose
    private int page;
    @Expose
    private int limit;
    @Expose
    private List<Comment> comments = new ArrayList<Comment>();

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
