package com.xavey.woody.api.model;

import java.util.Date;

public class Notifications {

    private String _id;
    private String user;
    private Date last_pushed_on;
    private Date last_requested_on;
    private Date updated_on;
    private Notification[] notices;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getLast_pushed_on() {
        return last_pushed_on;
    }

    public void setLast_pushed_on(Date last_pushed_on) {
        this.last_pushed_on = last_pushed_on;
    }

    public Date getLast_requested_on() {
        return last_requested_on;
    }

    public void setLast_requested_on(Date last_requested_on) {
        this.last_requested_on = last_requested_on;
    }

    public Date getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(Date updated_on) {
        this.updated_on = updated_on;
    }

    public Notification[] getNotices() {
        return notices;
    }

    public void setNotices(Notification[] notices) {
        this.notices = notices;
    }
}