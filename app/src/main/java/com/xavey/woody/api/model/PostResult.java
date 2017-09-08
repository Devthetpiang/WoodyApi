package com.xavey.woody.api.model;

/**
 * Created by tinmaungaye on 5/11/15.
 */
public class PostResult {
    private Post post;
    private String liked;
    private String voted;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public String getVoted() {
        return voted;
    }

    public void setVoted(String voted) {
        this.voted = voted;
    }
}
