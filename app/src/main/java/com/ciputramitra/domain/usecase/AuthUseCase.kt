package com.ciputramitra.domain.usecase

import com.ciputramitra.domain.repository.AuthRepository
import com.ciputramitra.response.auth.AuthResponse
import com.ciputramitra.response.auth.User
import com.ciputramitra.response.otprequest.OtpRequestResponse
import com.ciputramitra.response.resetpassword.ResetPasswordResponse
import com.ciputramitra.response.verifyemail.VerifyEmailResponse
import kotlinx.coroutines.flow.Flow

class AuthUseCase(
	private val authRepository: AuthRepository
) {
	suspend operator fun invoke(
		username: String,
		email: String,
		password: String,
		passwordConfirm: String,
		whatsapp: String,
		gender: String,
		fcm: String,
		role: String
	): Result<AuthResponse> {
		return authRepository.register(
			username, email, password, passwordConfirm, whatsapp, gender, fcm, role
		)
	}
	
	suspend operator fun invoke(email: String, password: String): Result<AuthResponse> {
		return authRepository.login(email = email, password = password)
	}
	
	suspend fun verifyEmail(email: String): Result<VerifyEmailResponse> {
		return authRepository.verifyEmail(email = email)
	}
	
	suspend fun verifyOtp(email: String, otp: String): Result<OtpRequestResponse> {
		return authRepository.verifyOtp(email = email, otp = otp)
	}
	
	suspend fun resetPassword(id: Int, password : String): Result<ResetPasswordResponse> {
		return authRepository.resetPassword(id, password)
	}
	
	suspend operator fun invoke(token: String) {
		authRepository.saveToken(token = token)
	}
	
	suspend operator fun invoke(user: User) {
		authRepository.saveUser(user = user)
	}
	
	suspend fun getToken(): Flow<String?> {
		return authRepository.getToken()
	}
	
	suspend fun getUser(): Flow<User?> {
		return authRepository.getUser()
	}
	
	suspend operator fun invoke() {
		authRepository.logout()
	}
}