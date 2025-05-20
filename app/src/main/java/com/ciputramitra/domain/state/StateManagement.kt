package com.ciputramitra.domain.state

sealed class StateManagement {
	data object Idle: StateManagement()
	data object Loading: StateManagement()
	data class Error(val message: String): StateManagement()
	
}