package com.ciputramitra.navgraph

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Register


@Serializable
object Home

@Serializable
object VerifyEmailRoute

@Serializable
data class VerifyOtpArgs(
	val email : String,
)

@Serializable
data class ResetPasswordArgs(
	val userID: Int
)

@Serializable
object Polyclinic

@Serializable
data class DoctorAllArgs(
	val polyclinicID: Int,
	val namePolyclinic: String
)
