package com.tikerdev.retrofit.service;

import com.tikerdev.retrofit.models.Response;
import com.tikerdev.retrofit.models.User;
import com.tikerdev.retrofit.models.UserList;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Error404 on 29/11/2559.
 */

public interface ApiService {

    @GET("get")
    Observable<UserList> loadUserList();

    @FormUrlEncoded
    @POST("post/")
    Observable<User> postUser(@Field("user") String user, @Field("name") String name);

    @FormUrlEncoded
    @POST("update/")
    Observable<User> updateUser(@Field("id") int id, @Field("user") String user, @Field("name") String name);

    @FormUrlEncoded
    @POST("delete/")
    Observable<Response> deleteUser(@Field("id") int id);
}
