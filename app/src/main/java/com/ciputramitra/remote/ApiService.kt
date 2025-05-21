package com.ciputramitra.remote

import com.ciputramitra.response.Wrapper
import com.ciputramitra.response.auth.AuthResponse
import com.ciputramitra.response.otprequest.OtpRequestResponse
import com.ciputramitra.response.resetpassword.ResetPasswordResponse
import com.ciputramitra.response.verifyemail.VerifyEmailResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
	@FormUrlEncoded
	@POST("register")
	suspend fun register(
		@Field("username") username: String,
		@Field("email") email: String,
		@Field("password") password: String,
		@Field("password_confirmation") passwordConfirm: String,
		@Field("whatsapp") whatsapp: String,
		@Field("gender") gender: String,
		@Field("fcm") fcm: String,
		@Field("role") role: String,
	): Wrapper<AuthResponse>
	
	@FormUrlEncoded
	@POST("login")
	suspend fun login(
		@Field("email") email: String,
		@Field("password") password: String
	): Wrapper<AuthResponse>
	
	@FormUrlEncoded
	@POST("requestOTP")
	suspend fun requestOtp(
		@Field("email") email: String
	): Wrapper<VerifyEmailResponse>
	
	@FormUrlEncoded
	@POST("requestReset")
	suspend fun verificationOtp(
		@Field("email") email: String,
		@Field("otp") otp: String
	): Wrapper<OtpRequestResponse>
	
	
	@FormUrlEncoded
	@POST("updatePassword/{id}")
	suspend fun resetPassword(
		@Path(value = "id") id: Int,
		@Field("password") password: String,
	): Wrapper<ResetPasswordResponse>
}