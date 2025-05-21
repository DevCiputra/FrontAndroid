package com.ciputramitra.domain.state

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ValidationEmailVerify: ViewModel() {
	var email by mutableStateOf(
		ValidationStatement(validation = { Patterns.EMAIL_ADDRESS.matcher(it).matches() })
	)
	
	fun validateForm(): Boolean {
		email = email.copy(showError = !email.validation(email.value))
		
		
		return email.validation(email.value)
	}
}