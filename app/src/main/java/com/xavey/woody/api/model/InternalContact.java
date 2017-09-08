package com.xavey.woody.api.model;

import java.io.Serializable;

/**
 * Created by tinmaungaye on 8/27/15.
 */
public class InternalContact implements Serializable,  Comparable<InternalContact> {
    private String picture;
    private String name;
    private String phone;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public int compareTo(InternalContact ic) {
        return getName().compareTo(ic.getName());
    }

    @Override
    public boolean equals(Object object)
    {
        boolean sameSame = false;

        if (object != null && object instanceof InternalContact)
        {
            sameSame = this.name.equals(((InternalContact) object).name);
        }

        return sameSame;
    }

    @Override
    public int hashCode()
    {
        int result = 17;
        result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
        return result;
    }
}
