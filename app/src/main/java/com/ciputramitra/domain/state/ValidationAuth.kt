package com.ciputramitra.domain.state

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ValidationAuth: ViewModel() {
	
	var userName by mutableStateOf(
		ValidationStatement(validation = { it.isNotBlank()})
	)
	
	var email by mutableStateOf(
		ValidationStatement(validation = { Patterns.EMAIL_ADDRESS.matcher(it).matches() })
	)
	
	var password by mutableStateOf(
		ValidationStatement(validation = { it.length >= 6})
	)
	
	var passwordNew by mutableStateOf(
		ValidationStatement(validation = {true})
	)
	
	var whatsapp by mutableStateOf(
		ValidationStatement(
			validation = { input ->
				when {
					input.isBlank() -> false
					!input.startsWith("0") -> false
					else -> true
				}
			}
		)
	)
	
	
	var gender by mutableStateOf(
		ValidationStatement(validation = { it.isNotBlank()})
	)
	
	var checkBoxChange by mutableStateOf(false)
	var showCheckBoxError by mutableStateOf(false)
	
	fun validateForm(): Boolean {
		email = email.copy(showError = !email.validation(email.value))
		
		password = password.copy(showError = !password.validation(password.value))
		
		passwordNew = passwordNew.copy(showError = !passwordNew.validation(passwordNew.value))
		
		userName = userName.copy(showError = !userName.validation(userName.value))
		
		whatsapp = whatsapp.copy(showError = !whatsapp.validation(whatsapp.value))
		gender = gender.copy(showError = !gender.validation(gender.value))
		
		showCheckBoxError = !checkBoxChange
		
		return checkBoxChange &&
				email.validation(email.value)
	}
}