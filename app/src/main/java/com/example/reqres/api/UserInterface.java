package com.example.reqres.api;

import com.example.reqres.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserInterface {

    @GET("users")
    Call<User> getUsers(
            @Query("page") int pageIndex
    );
}
