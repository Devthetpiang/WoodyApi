package com.xavey.woody.api.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hnin on 8/13/15.
 */
public class PostSet implements Serializable{

    private String _id;

    private String intro;

    private String desc;

    private String ack;

    private Post[] posts;

    private User created_by;

    private int flag_count;

    private String status;

    private Date created_on;

    public PostSet(){
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAck() {
        return ack;
    }

    public void setAck(String ack) {
        this.ack = ack;
    }

    public Post[] getPosts() {
        return posts;
    }

    public void setPosts(Post[] posts) {
        this.posts = posts;
    }

    public User getCreated_by() {
        return created_by;
    }

    public void setCreated_by(User created_by) {
        this.created_by = created_by;
    }

    public int getFlag_count() {
        return flag_count;
    }

    public void setFlag_count(int flag_count) {
        this.flag_count = flag_count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreated_on() {
        return created_on;
    }

    public void setCreated_on(Date created_on) {
        this.created_on = created_on;
    }
}
