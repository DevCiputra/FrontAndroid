package com.ciputramitra.response.verifyemail


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("message")
    val message: String
)