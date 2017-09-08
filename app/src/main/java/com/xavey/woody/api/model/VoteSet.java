package com.xavey.woody.api.model;

import java.io.Serializable;

/**
 * Created by hnin on 8/17/15.
 */
public class VoteSet implements Serializable {

    private String post_set_id;

    private VoteSetPost[] vote_set_posts;

    private String voted_by;

    private String voted_on;

    public String getPost_set_id() {
        return post_set_id;
    }

    public void setPost_set_id(String post_set_id) {
        this.post_set_id = post_set_id;
    }

    public VoteSetPost[] getVote_set_posts() {
        return vote_set_posts;
    }

    public void setVote_set_posts(VoteSetPost[] vote_set_posts) {
        this.vote_set_posts = vote_set_posts;
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
