package com.xavey.woody.api.model;

import java.io.Serializable;

/**
 * Created by tinmaungaye on 3/27/15.
 *
 * User just name, rank, and profile image will be sufficient for all user related info
 *
 */
public class BaseUser implements Serializable {
    private String _id;
    private String name;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
