package com.ciputramitra.response.doctor


import com.google.gson.annotations.SerializedName

data class Photo(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("doctor_id")
    val doctorId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("mime_type")
    val mimeType: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("updated_at")
    val updatedAt: String
)