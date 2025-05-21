package com.ciputramitra.consultation.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ciputramitra.domain.state.StateManagement
import com.ciputramitra.domain.usecase.AuthUseCase
import com.ciputramitra.remote.HttpClient
import com.ciputramitra.response.auth.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AuthViewModel(
	private val authUseCase: AuthUseCase,
	private val httpClient: HttpClient
): ViewModel() {
	
	private val _authState = MutableStateFlow<StateManagement>(StateManagement.Idle)
	val authState: StateFlow<StateManagement> = _authState.asStateFlow()
	
	private val _token = MutableStateFlow<String?>(value = null)
	val token: StateFlow<String?> = _token.asStateFlow()
	
	private val _fetchUser = MutableStateFlow<User?>(value = null)
	val fetchUser: StateFlow<User?> = _fetchUser.asStateFlow()
	
	
	
	val isLoggedIn = _token.map { it != null }
	init {
		viewModelScope.launch {
			authUseCase.getUser().collect { user ->
				_fetchUser.value = user
			}
		}
		
		viewModelScope.launch {
			authUseCase.getToken().collect { token ->
				_token.value = token
			}
		}
	}
	
	fun register(
		username: String,
		email: String,
		password: String,
		passwordConfirm: String,
		whatsapp: String,
		gender: String,
		fcm: String,
		role: String
	) {
		viewModelScope.launch {
			_authState.value = StateManagement.Loading
			val result = authUseCase.invoke(
				username, email, password, passwordConfirm, whatsapp, gender, fcm, role
			)
			result.fold(
				onSuccess = { authResponse ->
					_authState.value = StateManagement.RegisterSuccess(authResponse = authResponse)
					httpClient.rebuildClient()
				},
				onFailure = { error ->
					_authState.value = StateManagement.Error(error.message.toString())
				}
			)
		}
	}
	
	fun login(email: String, password: String) {
		viewModelScope.launch {
			_authState.value = StateManagement.Loading
			val result = authUseCase.invoke(email, password)
			result.fold(
				onSuccess = { authResponse ->
					if (authResponse.user.role != "Patient") {
						// Jika bukan Pasien, jangan simpan data dan langsung kirim error
						_authState.value = StateManagement.Error("Hanya untuk Pasien")
						return@fold
						
					}
					
					authUseCase.invoke(user = authResponse.user)
					authUseCase.invoke(token = authResponse.accessToken)
					_authState.value = StateManagement.LoginSuccess(authResponse = authResponse)
					
					// Rebuild HTTP client agar menggunakan token baru
					httpClient.rebuildClient() // Inject httpClient ke ViewModel
					
				},
				onFailure = { error ->
					_authState.value = StateManagement.Error(error.message.toString())
				}
			)
		}
	}
	
	fun verifyEmail(email: String) {
		viewModelScope.launch {
			_authState.value = StateManagement.Loading
			val result = authUseCase.verifyEmail(email = email)
			result.fold(
				onSuccess = { verifyEmailResponse ->
					_authState.value = StateManagement.VerifyEmailSuccess(
						verifyEmailResponse = verifyEmailResponse
					)
				},
				
				onFailure = { error ->
					_authState.value = StateManagement.Error(error.message.toString())
				}
			)
		}
	}
	
	fun verifyOtp(email: String, otp: String) {
		viewModelScope.launch {
			_authState.value = StateManagement.Loading
			val result = authUseCase.verifyOtp(email = email, otp = otp)
			
			result.fold(
				onSuccess = { otpRequestResponse ->
					_authState.value = StateManagement.VerifyOtpSuccess(
						otpRequestResponse = otpRequestResponse
					)
				},
				onFailure = { error ->
					_authState.value = StateManagement.Error(error.message.toString())
				}
			)
		}
	}
	
	fun resetPassword(id: Int, password : String) {
		viewModelScope.launch {
			_authState.value = StateManagement.Loading
			val result = authUseCase.resetPassword(id, password)
			
			result.fold(
				onSuccess = { messsage ->
					_authState.value = StateManagement.ResetPasswordSuccess(
						message = messsage.meta.message
					)
				},
				onFailure = { error ->
					_authState.value = StateManagement.Error(error.message.toString())
				}
			)
		}
	}
	
	fun logout() {
		viewModelScope.launch {
			try {
				authUseCase.invoke()
				_authState.value = StateManagement.Idle
				_fetchUser.value = null
				_token.value = null
			} catch (e: Exception) {
				error(e.message ?: "Logout gagal")
			}
			
		}
	}
	
	fun clearAuthState() {
		viewModelScope.launch {
			_authState.value = StateManagement.Idle
		}
	}
}