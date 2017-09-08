package com.xavey.woody.api.model;

/**
 * Created by tinmaungaye on 8/20/15.
 */
public class PostSetResult {
    private PostSet postset;
    private String owned;
    private String voted;

    public PostSet getPostset() {
        return postset;
    }

    public void setPostset(PostSet postset) {
        this.postset = postset;
    }

    public String getOwned() {
        return owned;
    }

    public void setOwned(String owned) {
        this.owned = owned;
    }

    public String getVoted() {
        return voted;
    }

    public void setVoted(String voted) {
        this.voted = voted;
    }
}
