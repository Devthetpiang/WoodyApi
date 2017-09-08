package com.xavey.woody.api.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Posts {
@Expose
private int total;
@Expose
private List<Post> posts = new ArrayList<Post>();

public int getTotal() {
return total;
}

public void setTotal(int total) {
this.total = total;
}

public List<Post> getPosts() {
return posts;
}

public void setPots(List<Post> posts) {
this.posts = posts;
}

}