package com.xavey.woody.api.model;

import java.io.Serializable;

/**
 * Created by tinmaungaye on 3/27/15.
 */
public class Category implements Serializable, Comparable<Category>{

    private String _id;
    private String title;
    private Boolean isSelected=false;

    public Category() {
    }

    @Override
    public int compareTo(Category cat) {
        return getTitle().compareTo(cat.getTitle());
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

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public boolean equals(Object object)
    {
        boolean sameSame = false;

        if (object != null && object instanceof Category)
        {
            sameSame = this.title.equals(((Category) object).title);
        }

        return sameSame;
    }

    @Override
    public int hashCode()
    {
        int result = 17;
        result = 31 * result + (this.title == null ? 0 : this.title.hashCode());
        return result;
    }
}
