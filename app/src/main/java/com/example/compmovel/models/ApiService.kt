package com.example.compmovel.models

import android.widget.EditText
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    @FormUrlEncoded
    fun login(@Field("email") username: String, @Field("password") password: String) : Call<LoginResponse>

    @POST("insertIncident")
    @FormUrlEncoded
    fun insertIncident(@Field("title") title: String, @Field("description") description: String, @Field("image") image: String, @Field("latitude") latitude: String, @Field("longitude") longitude: String) : Call<InsertincidentResponse>
}