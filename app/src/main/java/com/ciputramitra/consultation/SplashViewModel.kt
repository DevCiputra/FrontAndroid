package com.ciputramitra.consultation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel: ViewModel() {
	private val _isReady = MutableStateFlow(value = false)
	val isReady = _isReady.asStateFlow()
	
	init {
		viewModelScope.launch {
			delay(3000)
			_isReady.value = true
		}
	}
}