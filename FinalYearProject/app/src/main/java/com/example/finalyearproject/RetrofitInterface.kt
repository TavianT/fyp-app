package com.example.finalyearproject

import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import okhttp3.MultipartBody;
import retrofit2.Call;

interface RetrofitInterface {
    @Multipart
    @POST("files/")
    fun uploadImage(@Part image: MultipartBody.Part) : Call<ImageClassification>
}