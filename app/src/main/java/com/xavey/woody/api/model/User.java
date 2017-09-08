package com.xavey.woody.api.model;

import java.io.Serializable;
import java.util.Date;

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
public class User implements Serializable, Comparable<User> {
    private String _id;
    private String full_name;
    private String user_name;
    private String hashed_password;
    private String email;
    private String gender;
    private String phone;
    private Date dob;
    private String resetPasswordToken;
    private Date resetPasswordExpires;
    private Date created_on;
    private String liked;
    private String nric;
    private String current_city;
    private String income;
    private String education;
    private String picture;
    private String token;
    private int type;
    private Category[]opt_out_category;
    private boolean profile_completed;
    private String facebook_id;
    private String facebook_token;
    public User() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getHashed_password() {
        return hashed_password;
    }

    public void setHashed_password(String hashed_password) {
        this.hashed_password = hashed_password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getCreated_on() { return created_on; }

    public void setCreated_on(Date created_on) { this.created_on = created_on; }

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public Date getResetPasswordExpires() {
        return resetPasswordExpires;
    }

    public void setResetPasswordExpires(Date resetPasswordExpires) {
        this.resetPasswordExpires = resetPasswordExpires;
    }

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public String getCurrent_city() {
        return current_city;
    }

    public void setCurrent_city(String current_city) {
        this.current_city = current_city;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Category[] getOpt_out_category() {
        return opt_out_category;
    }

    public void setOpt_out_category(Category[] opt_out_category) {
        this.opt_out_category = opt_out_category;
    }

    public boolean getProfile_completed() {
        return profile_completed;
    }

    public void setProfile_completed(boolean profile_completed) {
        this.profile_completed = profile_completed;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }

    public String getFacebook_token() {
        return facebook_token;
    }

    public void setFacebook_token(String facebook_token) {
        this.facebook_token = facebook_token;
    }

    @Override
    public int compareTo(User usr) {
           return getUser_name().compareTo(usr.getUser_name());
    }

    @Override
    public boolean equals(Object object)
    {
        boolean sameSame = false;

        if (object != null && object instanceof Category)
        {
            sameSame = this.user_name.equals(((User) object).user_name);
        }

        return sameSame;
    }

    @Override
    public int hashCode()
    {
        int result = 17;
        result = 31 * result + (this.user_name == null ? 0 : this.user_name.hashCode());
        return result;
    }

}
