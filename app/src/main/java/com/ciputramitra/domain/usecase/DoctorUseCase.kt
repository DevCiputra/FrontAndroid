package com.ciputramitra.domain.usecase

import androidx.paging.PagingData
import com.ciputramitra.domain.repository.doctor.DoctorRepository
import com.ciputramitra.response.doctor.DoctorItems
import kotlinx.coroutines.flow.Flow

class DoctorUseCase(
	private val doctorRepository : DoctorRepository
) {
	suspend fun fetchDoctor(
		name: String,
		doctorPolyclinicID: Int,
		isPublished: Int,
		schedule: String
	): Flow<PagingData<DoctorItems>> {
		return doctorRepository.fetchDoctor(
			name = name,
			doctorPolyclinicID = doctorPolyclinicID,
			isPublished = isPublished,
			schedule = schedule
		)
	}
}