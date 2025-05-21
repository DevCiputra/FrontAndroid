package com.ciputramitra.response.verifyemail


import com.ciputramitra.response.Meta
import com.google.gson.annotations.SerializedName

data class VerifyEmailResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("meta")
    val meta: Meta
)