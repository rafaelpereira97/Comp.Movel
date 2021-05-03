package com.example.compmovel.models

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiService {
    @POST("user/login")
    @FormUrlEncoded
    fun login(@Field("email") username: String, @Field("password") password: String) : Call<LoginResponse>

    @POST("event/insert")
    @FormUrlEncoded
    fun insertIncident(
        @Field("title") title: String, @Field("description") description: String, @Field(
            "image"
        ) image: String, @Field("latitude") latitude: String, @Field("longitude") longitude: String, @Field("user_id") user_id: Int
    ) : Call<InsertincidentResponse>

    @GET("event/get")
    fun getIncidents(): Call<List<IncidentResponse>>

    @POST("event/update")
    @FormUrlEncoded
    fun updateIncident( @Field("id") id: Int,
        @Field("title") title: String, @Field("description") description: String) : Call<UpdateincidentResponse>

    @POST("event/delete")
    @FormUrlEncoded
    fun deleteIncident( @Field("id") id: Int) : Call<DeleteincidentResponse>
}
