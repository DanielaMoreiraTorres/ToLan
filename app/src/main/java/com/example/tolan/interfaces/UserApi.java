package com.example.tolan.interfaces;

import com.example.tolan.clases.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface UserApi {
    @GET("usuario/{id}")
    public Call<User> getUserById(@Header("id") int id);
}
