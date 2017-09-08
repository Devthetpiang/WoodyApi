package com.xavey.woody.api.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Categories {
@Expose
private int total;
@Expose
private List<Category> categories = new ArrayList<Category>();

public int getTotal() {
return total;
}

public void setTotal(int total) {
this.total = total;
}

public List<Category> getCategories() {
return categories;
}

public void setCategories(List<Category> val) {
this.categories = val;
}

}