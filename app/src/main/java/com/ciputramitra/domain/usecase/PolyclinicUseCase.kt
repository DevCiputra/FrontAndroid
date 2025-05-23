package com.ciputramitra.domain.usecase

import androidx.paging.PagingData
import com.ciputramitra.domain.repository.polyclinic.PolyclinicRepository
import com.ciputramitra.response.polyclinic.Data
import kotlinx.coroutines.flow.Flow

class PolyclinicUseCase(
	private val polyclinicRepository : PolyclinicRepository
) {
	suspend fun fetchPolyclinic(): Flow<PagingData<Data>> {
		return polyclinicRepository.fetchPolyclinic()
	}
}