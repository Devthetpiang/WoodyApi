package com.xavey.woody.api.model;

import java.util.List;

/**
 * Created by tinmaungaye on 8/27/15.
 */
public class UserReferrals {
    private List<UserReferral> foundUsers;
    private List<UserReferral> referredUsers;

    public List<UserReferral> getFoundUsers() {
        return foundUsers;
    }

    public void setFoundUsers(List<UserReferral> foundUsers) {
        this.foundUsers = foundUsers;
    }

    public List<UserReferral> getReferredUsers() {
        return referredUsers;
    }

    public void setReferredUsers(List<UserReferral> referredUsers) {
        this.referredUsers = referredUsers;
    }
}
