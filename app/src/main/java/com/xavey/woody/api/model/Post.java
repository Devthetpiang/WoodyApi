package com.xavey.woody.api.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tinmaungaye on 3/27/15.
 */
public class Post implements Serializable {

    private String _id;
    private String title;
    private String desc;
    private String cover;
    private Item[] items;
    private Item[] itemsx;
    private Category[] categories;
    private User created_by;
    private Date created_on;
    private int comment_count;
    private int flag_count;
    private String status;
    private String picture;
    private String type;
    private Boolean extra;
    private String extra_title;
    private String extra_value;
    private String code;

    public Post() {
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
       this.cover = cover;
    }

    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }

    public Category[] getCategories() {
        return categories;
    }

    public void setCategory(Category[] categories) {
        this.categories = categories;
    }

    public User getCreated_by() {
        return created_by;
    }

    public void setCreated_by(User created_by) {
        this.created_by = created_by;
    }

    public Date getCreated_on() {
        return created_on;
    }

    public void setCreated_on(Date created_on) {
        this.created_on = created_on;
    }

    public int getFlag_count() {
        return flag_count;
    }

    public void setFlag_count(int flat_count) {
        this.flag_count = flag_count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Item[] getItemsx() {
        return itemsx;
    }

    public void setItemsx(Item[] itemsx) {
        this.itemsx = itemsx;
    }

    public void setCategories(Category[] categories) {
        this.categories = categories;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExtra_title() {
        return extra_title;
    }

    public void setExtra_title(String extra_title) {
        this.extra_title = extra_title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getExtra() {
        return extra;
    }

    public void setExtra(Boolean extra) {
        this.extra = extra;
    }

    public String getExtra_value() {
        return extra_value;
    }

    public void setExtra_value(String extra_value) {
        this.extra_value = extra_value;
    }
}
