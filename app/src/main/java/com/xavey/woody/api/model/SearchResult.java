package com.xavey.woody.api.model;

import com.google.gson.annotations.Expose;

public class SearchResult {

@Expose
private Posts posts;
@Expose
private String stat;

public Posts getPosts() {
return posts;
}

public void setPosts(Posts posts) {
this.posts = posts;
}

public String getStat() {
return stat;
}

public void setStat(String stat) {
this.stat = stat;
}

}