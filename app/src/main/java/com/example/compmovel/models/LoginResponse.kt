package com.example.compmovel.models

import com.google.gson.annotations.SerializedName

class LoginResponse {
    @SerializedName("status")
    var status: String? = null
    @SerializedName("token")
    var token: String? = null
    @SerializedName("user_id")
    var user_id: String? = null
}
