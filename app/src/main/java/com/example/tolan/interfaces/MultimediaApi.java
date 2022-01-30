package com.example.tolan.interfaces;

import com.example.tolan.models.ModelUploadImage;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface MultimediaApi {

    @Multipart
    @POST("multimedia/saveFileMedia")
    Call<ModelUploadImage> uploadImage(@Part MultipartBody.Part image);
}
