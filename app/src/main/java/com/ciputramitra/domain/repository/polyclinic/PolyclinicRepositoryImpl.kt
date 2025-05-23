package com.ciputramitra.domain.repository.polyclinic

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ciputramitra.remote.ApiService
import com.ciputramitra.response.polyclinic.Data
import kotlinx.coroutines.flow.Flow

class PolyclinicRepositoryImpl(
	private val apiService : ApiService
): PolyclinicRepository {
	override suspend fun fetchPolyclinic() : Flow<PagingData<Data>> = Pager(
		config = PagingConfig(
			pageSize = 10,
			enablePlaceholders = false,
			maxSize = 50
		),
		pagingSourceFactory = {
			PolyclinicPagingSource(
				apiService = apiService
			)
		}
	).flow
}