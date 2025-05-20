package com.ciputramitra.domain.state

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ValidationAuth: ViewModel() {
	var email by mutableStateOf(
		ValidationStatement(validation = { Patterns.EMAIL_ADDRESS.matcher(it).matches() })
	)
	
	var password by mutableStateOf(
		ValidationStatement(validation = { it.length >= 6})
	)
	
	var passwordNew by mutableStateOf(
		ValidationStatement(validation = {true})
	)
	
	var userName by mutableStateOf(
		ValidationStatement(validation = { it.isNotBlank()})
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
	
	var address by mutableStateOf(
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
		address = address.copy(showError = !address.validation(address.value))
		
		showCheckBoxError = !checkBoxChange
		
		return checkBoxChange &&
				email.validation(email.value)
	}
}