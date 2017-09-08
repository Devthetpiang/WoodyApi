package com.xavey.woody.api.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tinmaungaye on 3/27/15.
 */
public class Notification implements Serializable, Comparable<Notification> {

    private String post;
    private String title;
    private String type;
    private String _id;
    private Date created_on;
    private Boolean checked;

    public Notification() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public Date getCreated_on() {
        return created_on;
    }

    public void setCreated_on(Date created_on) {
        this.created_on = created_on;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int compareTo(Notification noti) {
        return noti.getCreated_on().compareTo(getCreated_on());
    }

    @Override
    public boolean equals(Object object)
    {
        boolean sameSame = false;

        if (object != null && object instanceof Notification)
        {
            sameSame = this._id.equals(((Notification) object)._id);
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
