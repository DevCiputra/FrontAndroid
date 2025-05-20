package com.ciputramitra.response


import com.google.gson.annotations.SerializedName

data class Wrapper<T>(
    @SerializedName("data")
    val `data`: T? = null,
    @SerializedName("meta")
    val meta: Meta? = null
)