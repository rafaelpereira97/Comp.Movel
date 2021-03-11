package com.example.compmovel.models

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    @FormUrlEncoded
    fun login(@Field("email") username: String, @Field("password") password: String) : Call<LoginResponse>
}