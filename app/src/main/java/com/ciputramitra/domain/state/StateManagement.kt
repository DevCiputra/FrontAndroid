package com.ciputramitra.domain.state

import com.ciputramitra.response.auth.AuthResponse
import com.ciputramitra.response.otprequest.OtpRequestResponse
import com.ciputramitra.response.polyclinic.PolyclinicResponse
import com.ciputramitra.response.resetpassword.ResetPasswordResponse
import com.ciputramitra.response.verifyemail.VerifyEmailResponse

sealed class StateManagement {
	data object Idle: StateManagement()
	data object Loading: StateManagement()
	data class Error(val message: String): StateManagement()
	
	//	Authentication
	data class RegisterSuccess(val authResponse: AuthResponse): StateManagement()
	data class LoginSuccess(val authResponse : AuthResponse): StateManagement()
	data class VerifyEmailSuccess(val verifyEmailResponse : VerifyEmailResponse): StateManagement()
	data class VerifyOtpSuccess(val otpRequestResponse : OtpRequestResponse): StateManagement()
	data class ResetPasswordSuccess(val message : String): StateManagement()
	
	
	//	Polyclinic Kategori
	data class PolyclinicSuccess(
		val polyclinicResponse : PolyclinicResponse
	): StateManagement()
	
}