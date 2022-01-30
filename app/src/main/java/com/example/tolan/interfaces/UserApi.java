package com.example.tolan.interfaces;

import com.example.tolan.models.ModelUser;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserApi {
    @GET("usuario/{id}")
    public Call<ModelUser> getUserById(@Header("id") String id);
}
