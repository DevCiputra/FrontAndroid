package com.ciputramitra.domain.repository.doctor

import androidx.paging.PagingData
import com.ciputramitra.response.doctor.DoctorItems
import kotlinx.coroutines.flow.Flow

interface DoctorRepository {
	suspend fun fetchDoctor(
		name: String,
		doctorPolyclinicID: Int,
		isPublished: Int,
		schedule: String
	): Flow<PagingData<DoctorItems>>
}