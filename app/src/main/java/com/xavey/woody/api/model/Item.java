package com.xavey.woody.api.model;

import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.Rabbit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by tinmaungaye on 3/27/15.
 *
 * _id would be same but tie with post will make them unique
 */
public class Item implements Serializable {
    private String _id;
    private String title;
    private int vote_count;
    private Boolean extra;

    public Item() {
    }

    public Boolean getExtra() {
        return extra;
    }

    public void setExtra(Boolean extra) {
        this.extra = extra;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        if(AppValues.getInstance().getZawGyiDisplay()){
            return Rabbit.uni2zg(title);
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public ArrayList<Item> JSONToItems(JSONArray rawItems, Post post) throws JSONException {
        ArrayList<Item> reList = new ArrayList<Item>();
        for (int i=0; i < rawItems.length(); i++) {
            JSONObject jobj = rawItems.getJSONObject(i);
            Iterator<?> keys = jobj.keys();

            Item iobj = new Item();
           // iobj.post = post;
            while(keys.hasNext() ) {
                String key = (String)keys.next();
                switch (key){
                    case "_id":
             //           iobj.set_id((String)jobj.get(key));
                    case "title":
                        iobj.setTitle((String)jobj.get(key));
                    case "vote_count":
                        iobj.setVote_count((Integer)jobj.get(key));
                }
                reList.add(iobj);
            }
        }
        return reList;
    }
}
