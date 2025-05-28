package com.ciputramitra.data

data class DoctorRequest(
	val name: String? = "",
	val doctorPolyclinicID: Int? = 0,
	val isPublished: Int? = 0,
	val schedule: String? = ""
)