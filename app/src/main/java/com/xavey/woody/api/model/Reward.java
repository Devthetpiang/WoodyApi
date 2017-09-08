package com.xavey.woody.api.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tinmaungaye on 3/27/15.
 */
public class Reward implements Serializable {

    private String _id;
    private int mpoint;
    private String desc;
    private String picture;
    private Boolean open;
    private Date start;
    private Date end;
    private User awarded_to;
    private Date awarded_on;

    public Reward() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getMpoint() {
        return mpoint;
    }

    public void setMpoint(int mpoint) {
        this.mpoint = mpoint;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public User getAwarded_to() {
        return awarded_to;
    }

    public void setAwarded_to(User awarded_to) {
        this.awarded_to = awarded_to;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getAwarded_on() {
        return awarded_on;
    }

    public void setAwarded_on(Date awarded_on) {
        this.awarded_on = awarded_on;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }
}
