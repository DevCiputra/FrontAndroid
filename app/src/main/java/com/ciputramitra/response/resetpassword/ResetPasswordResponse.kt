package com.ciputramitra.response.resetpassword


import com.ciputramitra.response.Meta
import com.google.gson.annotations.SerializedName

data class ResetPasswordResponse(
    @SerializedName("data")
    val `data`: String,
    @SerializedName("meta")
    val meta: Meta
)