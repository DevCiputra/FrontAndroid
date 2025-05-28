package com.ciputramitra.domain.repository.doctor

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ciputramitra.remote.ApiService
import com.ciputramitra.response.doctor.DoctorItems
import kotlinx.coroutines.flow.Flow

class DoctorRepositoryImpl(
	private val apiService : ApiService
): DoctorRepository {
	override suspend fun fetchDoctor(
		name : String ,
		doctorPolyclinicID : Int,
		isPublished: Int,
		schedule: String
	) : Flow<PagingData<DoctorItems>> =
		Pager(
			config = PagingConfig(
				pageSize = 10,
				enablePlaceholders = false,
				maxSize = 50
			),
			pagingSourceFactory = {
				DoctorPagingSource(
					apiService = apiService,
					name = name,
					doctorPolyclinicID = doctorPolyclinicID,
					isPublished = isPublished,
					schedule = schedule
				)
			}
		).flow
}