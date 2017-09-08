package com.xavey.woody.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tinmaungaye on 3/27/15.
 * App user related info
 *
 *
 {
 "user_name": "tinmaungaye",
 "hashed_password": "e9d897d3e7e4ab5eec3f1278d94b3ca081fb5aca",
 "full_name": "Tin Maung Aye",
 "gender": "Male",
 "phone": "095199937",
 "dob": "1984-05-23T00:00:00.000Z"
 }
 *
 */

public class UserRelated implements Serializable {
    private String message;
    private User user;
    private List<RelatedStat> related_stat;

    public UserRelated() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<RelatedStat> getRelatedStat() {
        return related_stat;
    }

    public void setRelatedStat(List<RelatedStat> relatedStat) {
        this.related_stat = relatedStat;
    }
}
