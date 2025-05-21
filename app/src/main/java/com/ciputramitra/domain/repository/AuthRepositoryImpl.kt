package com.ciputramitra.domain.repository

import com.ciputramitra.cache.DataStoreManager
import com.ciputramitra.remote.ApiService
import com.ciputramitra.response.auth.AuthResponse
import com.ciputramitra.response.auth.User
import com.ciputramitra.response.otprequest.OtpRequestResponse
import com.ciputramitra.response.resetpassword.ResetPasswordResponse
import com.ciputramitra.response.verifyemail.VerifyEmailResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class AuthRepositoryImpl(
	private val apiService: ApiService,
	private val dataStoreManager: DataStoreManager
): AuthRepository {
	override suspend fun register(
		username: String,
		email: String,
		password: String,
		passwordConfirm: String,
		whatsapp: String,
		gender: String,
		fcm: String,
		role: String
	): Result<AuthResponse> {
		return try {
			val response = apiService.register(
				username, email, password, passwordConfirm, whatsapp, gender, fcm, role
			)
			when(response.meta?.code == 200 && response.data != null) {
				true -> Result.success(value = response.data)
				false -> Result.failure(exception = Exception(response.meta?.message))
			}
		}
		catch (e: Exception) {
			when {
				e is HttpException && e.code() == 500 -> {
					Result.failure(exception =  Exception("Server sedang sibuk"))
				}
				
				e is HttpException && e.code() == 400 -> {
					Result.failure(exception =  Exception("The email has already been taken."))
				}
				else -> Result.failure(exception = e)
			}
		}
	}
	
	override suspend fun login(email: String, password: String): Result<AuthResponse> {
		return try {
			val response = apiService.login(
				email, password
			)
			when(response.meta?.code == 200 && response.data != null) {
				true -> Result.success(value = response.data)
				false -> Result.failure(exception = Exception(response.meta?.message))
			}
		}
		catch (e: Exception) {
			when {
				e is HttpException && e.code() == 500 -> {
					Result.failure(exception =  Exception("Server sedang sibuk"))
				}
				
				e is HttpException && e.code() == 400 -> {
					Result.failure(exception =  Exception("The email has already been taken."))
				}
				else -> Result.failure(exception = e)
			}
		}
	}
	
	override suspend fun verifyEmail(email : String) : Result<VerifyEmailResponse> {
		return try {
			val response = apiService.requestOtp(email = email)
			when(response.meta?.code == 200 && response.data != null) {
				true -> Result.success(value = response.data)
				false -> Result.failure(exception = Exception(response.meta?.message ?: "Not Found"))
			}
		}
		catch (e: Exception) {
			when {
				e is HttpException && e.code() == 500 -> {
					Result.failure(exception =  Exception("Permintaan Reset Password Gagal"))
				}
				
				e is HttpException && e.code() == 400 -> {
					Result.failure(exception =  Exception("Account tidak bisa di akses"))
				}
				else -> Result.failure(exception = e)
				
			}
		}
	}
	
	override suspend fun verifyOtp(email : String , otp : String) : Result<OtpRequestResponse> {
		return try {
			val response = apiService.verificationOtp(email = email, otp = otp)
			when(response.meta?.code == 200 && response.data != null) {
				true -> Result.success(value = response.data)
				false -> Result.failure(exception = Exception(response.meta?.message ?: "Not Found"))
			}
		}
		catch (e: Exception) {
			when {
				e is HttpException && e.code() == 500 -> {
					Result.failure(exception =  Exception("Permintaan Reset Password Gagal"))
				}
				
				e is HttpException && e.code() == 400 -> {
					Result.failure(exception =  Exception("Account tidak bisa di akses"))
				}
				else -> Result.failure(exception = e)
				
			}
		}
	}
	
	override suspend fun resetPassword(id : Int , password : String) : Result<ResetPasswordResponse> {
		return try {
			val response = apiService.resetPassword(id, password)
			when(response.meta?.code == 200 && response.data != null) {
				true -> Result.success(value = response.data)
				false -> Result.failure(exception = Exception(response.meta?.message ?: "Not Found"))
			}
		}
		catch (e: Exception) {
			when {
				e is HttpException && e.code() == 500 -> {
					Result.failure(exception =  Exception("Permintaan Reset Password Gagal"))
				}
				
				e is HttpException && e.code() == 400 -> {
					Result.failure(exception =  Exception("Account tidak bisa di akses"))
				}
				else -> Result.failure(exception = e)
				
			}
		}
	}
	
	override suspend fun saveToken(token: String) {
		dataStoreManager.saveToken(token = token)
	}
	
	override suspend fun saveUser(user: User) {
		dataStoreManager.saveUser(user = user)
	}
	
	override suspend fun getToken(): Flow<String?> {
		return dataStoreManager.tokenFlow
	}
	
	override suspend fun getUser(): Flow<User?> {
		return dataStoreManager.userFlow
	}
	
	override suspend fun logout() {
		dataStoreManager.clearDataStore()
	}
}