package com.ciputramitra.consultation.ui.doctor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ciputramitra.data.DoctorRequest
import com.ciputramitra.domain.usecase.DoctorUseCase
import com.ciputramitra.response.doctor.DoctorItems
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update

class DoctorViewModel(
	private val doctorUseCase : DoctorUseCase
): ViewModel() {
	private val _queryParams = MutableStateFlow(DoctorRequest())
	val queryParams : StateFlow<DoctorRequest> = _queryParams.asStateFlow()
	
	@OptIn(ExperimentalCoroutinesApi::class)
	val doctors : Flow<PagingData<DoctorItems>> = queryParams
		.flatMapLatest { params ->
			doctorUseCase.fetchDoctor(
				name = params.name ?: "",
				doctorPolyclinicID = params.doctorPolyclinicID ?: 0,
				isPublished = params.isPublished ?: 0,
				schedule = params.schedule ?: ""
			)
		}
		.cachedIn(viewModelScope)
	
	fun fetchDoctor(
		name: String? = null,
		doctorPolyclinicID: Int? = 0,
		isPublished: Int? = 0,
		schedule: String? = ""
	) {
		_queryParams.update { currentParams ->
			currentParams.copy(
				name = name ?: currentParams.name,
				doctorPolyclinicID = doctorPolyclinicID ?: currentParams.doctorPolyclinicID,
				isPublished = isPublished,
				schedule = schedule
			)
		}
	}
	
	fun resetFilter() {
		_queryParams.value = DoctorRequest()
	}
}