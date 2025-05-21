package com.ciputramitra.domain.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ValidationPassword: ViewModel() {
	
	var password by mutableStateOf(
		ValidationStatement(validation = { it.length >= 6  })
	)
	
	var passwordConfirmation by mutableStateOf(
		ValidationStatement(validation = { it.length >= 6})
	)
	
	fun validateForm(): Boolean {
		password = password.copy(showError = !password.validation(password.value))
		passwordConfirmation = passwordConfirmation.copy(showError = !passwordConfirmation.validation(passwordConfirmation.value))
		
		
		return password.validation(password.value) && passwordConfirmation.validation(passwordConfirmation.value)
	}
}
