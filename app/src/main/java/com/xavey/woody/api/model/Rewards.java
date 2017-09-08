package com.xavey.woody.api.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Rewards {
    @Expose
    private int total;
    @Expose
    private List<Reward> rewards = new ArrayList<Reward>();

    @Expose
    private String[] enrolled;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public void setRewards(List<Reward> rewards) {
        this.rewards = rewards;
    }

    public String[] getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(String[] enrolled) {
        this.enrolled = enrolled;
    }
}