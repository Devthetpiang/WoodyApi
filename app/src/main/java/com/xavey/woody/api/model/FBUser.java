package com.xavey.woody.api.model;

import com.facebook.Profile;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class FBUser {
@Expose
private String fbID;
@Expose
private String fbAuth;
@Expose
private Profile profile;

    public String getFbID() {
        return fbID;
    }

    public void setFbID(String fbID) {
        this.fbID = fbID;
    }

    public String getFbAuth() {
        return fbAuth;
    }

    public void setFbAuth(String fbAuth) {
        this.fbAuth = fbAuth;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}