package com.ciputramitra.domain.repository.polyclinic

import androidx.paging.PagingData
import com.ciputramitra.response.polyclinic.Data
import kotlinx.coroutines.flow.Flow

interface PolyclinicRepository {
	suspend fun fetchPolyclinic(): Flow<PagingData<Data>>
}