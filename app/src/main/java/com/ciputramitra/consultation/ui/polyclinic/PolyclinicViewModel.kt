package com.ciputramitra.consultation.ui.polyclinic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ciputramitra.domain.usecase.PolyclinicUseCase
import com.ciputramitra.response.polyclinic.Data
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update

class PolyclinicViewModel(
	private val polyclinicUseCase : PolyclinicUseCase
): ViewModel() {
	private val _queryParams = MutableStateFlow("")
	private val queryParams : StateFlow<String> = _queryParams.asStateFlow()
	
	@OptIn(ExperimentalCoroutinesApi::class)
	val polyclinic : Flow<PagingData<Data>> = queryParams
		.flatMapLatest { params ->
			polyclinicUseCase.fetchPolyclinic()
		}
		.cachedIn(viewModelScope)
	
	fun fetchPolyclinic() {
		_queryParams.update { currentParams ->
			currentParams
		}
	}
}