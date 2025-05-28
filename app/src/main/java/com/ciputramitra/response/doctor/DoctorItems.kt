package com.ciputramitra.response.doctor


import com.google.gson.annotations.SerializedName

data class DoctorItems(
    @SerializedName("address")
    val address: String,
    @SerializedName("consultation_fee")
    val consultationFee: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("deleted_at")
    val deletedAt: Any,
    @SerializedName("doctor_polyclinic_id")
    val doctorPolyclinicId: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_open_consultation")
    val isOpenConsultation: String,
    @SerializedName("is_open_reservation")
    val isOpenReservation: String,
    @SerializedName("is_published")
    val isPublished: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("photos")
    val photos: List<Photo>,
    @SerializedName("polyclinic")
    val polyclinic: Polyclinic,
    @SerializedName("specialization_name")
    val specializationName: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: Int
)