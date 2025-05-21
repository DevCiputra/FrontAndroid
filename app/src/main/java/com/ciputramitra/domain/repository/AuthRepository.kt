package com.ciputramitra.domain.repository

import com.ciputramitra.response.auth.AuthResponse
import com.ciputramitra.response.auth.User
import com.ciputramitra.response.otprequest.OtpRequestResponse
import com.ciputramitra.response.resetpassword.ResetPasswordResponse
import com.ciputramitra.response.verifyemail.VerifyEmailResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
	suspend fun register(
		username: String,
		email: String,
		password: String,
		passwordConfirm:String,
		whatsapp:String,
		gender: String,
		fcm: String,
		role: String,
	): Result<AuthResponse>
	
	suspend fun login(email: String, password: String): Result<AuthResponse>
	suspend fun verifyEmail(email: String): Result<VerifyEmailResponse>
	suspend fun verifyOtp(email: String, otp: String): Result<OtpRequestResponse>
	suspend fun resetPassword(id: Int , password: String): Result<ResetPasswordResponse>
	suspend fun saveToken(token: String)
	suspend fun saveUser(user: User)
	suspend fun getToken() : Flow<String?>
	suspend fun getUser(): Flow<User?>
	suspend fun logout()
}