package com.ciputramitra.domain.state

data class ValidationStatement(
	var value: String = "",
	var isVisible: Boolean = false,
	var showError: Boolean = false,
	var validation : (String) -> Boolean = { true },
	var validationInt: (Int) -> Boolean = { true},
	var valueInt: Int = 0
)