package com.example.compmovel.models

import com.google.gson.annotations.SerializedName

class LoginResponse {
    @SerializedName("status")
    var status: String? = null
    @SerializedName("token")
    var token: String? = null
}
