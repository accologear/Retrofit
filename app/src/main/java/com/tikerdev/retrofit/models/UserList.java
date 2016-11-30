package com.tikerdev.retrofit.models;

import java.util.ArrayList;
import java.util.List;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Error404 on 29/11/2559.
 */
@Parcel
public class UserList {

    @SerializedName("success")
    @Expose
    Boolean success;
    @SerializedName("user")
    @Expose
    List<User> user = new ArrayList<User>();

    /**
     * @return The success
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * @param success The success
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     * @return The user
     */
    public List<User> getUser() {
        return user;
    }

    /**
     * @param user The user
     */
    public void setUser(List<User> user) {
        this.user = user;
    }

}