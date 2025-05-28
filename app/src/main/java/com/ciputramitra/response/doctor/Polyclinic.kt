package com.ciputramitra.response.doctor


import com.google.gson.annotations.SerializedName

data class Polyclinic(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("updated_at")
    val updatedAt: String
)