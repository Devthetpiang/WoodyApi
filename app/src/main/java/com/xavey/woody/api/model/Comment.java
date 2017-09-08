package com.xavey.woody.api.model;

import java.io.Serializable;
import java.util.Date;

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
public class Comment implements Serializable, Comparable<Comment> {
    private String _id;
    private Post post;
    private String comment_text;
    private User commented_by;
    private Date commented_on;

    @Override
    public int compareTo(Comment com) {
        return getCommented_on().compareTo(com.getCommented_on());
    }

    public Comment()  {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getComment_Text() {
        return comment_text;
    }

    public void setComment_Text(String comment) {
        this.comment_text = comment;
    }

    public User getCommented_by() {
        return commented_by;
    }

    public void setCommented_by(User commented_by) {
        this.commented_by = commented_by;
    }

    public Date getCommented_on() {
        return commented_on;
    }

    public void setCommented_on(Date commented_on) {
        this.commented_on = commented_on;
    }

    @Override
    public boolean equals(Object object)
    {
        boolean sameSame = false;

        if (object != null && object instanceof Comment)
        {
            sameSame = this._id.equals(((Comment) object)._id);
        }

        return sameSame;
    }

    @Override
    public int hashCode()
    {
        int result = 17;
        result = 31 * result + (this._id == null ? 0 : this._id.hashCode());
        return result;
    }
}
