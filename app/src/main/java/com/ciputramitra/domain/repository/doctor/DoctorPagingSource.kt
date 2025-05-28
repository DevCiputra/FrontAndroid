package com.ciputramitra.domain.repository.doctor

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ciputramitra.remote.ApiService
import com.ciputramitra.response.doctor.DoctorItems

class DoctorPagingSource(
	private val apiService : ApiService,
	private val name: String,
	private val doctorPolyclinicID: Int,
	private val isPublished: Int,
	private val schedule: String
): PagingSource<Int, DoctorItems>() {
	override fun getRefreshKey(state : PagingState<Int , DoctorItems>) : Int {
		return 1
	}
	
	override suspend fun load(params : LoadParams<Int>) : LoadResult<Int , DoctorItems> {
		return try {
			val currentPage = params.key ?: 1
			val response = apiService.fetchDoctor(
				page = currentPage,
				doctorPolyclinicID = doctorPolyclinicID,
				name = name,
				isPublished = isPublished,
				schedule = schedule
			).data
			
			val data = response?.data ?: emptyList()
			val prevKey = if (response?.prevPageUrl == null) null else currentPage -1
			val nextKey = if (response?.nextPageUrl == null) null else currentPage +1
			
			LoadResult.Page(
				data = data,
				prevKey = prevKey,
				nextKey = nextKey
			)
		}
		catch (e: Exception) {
			e.printStackTrace()
			LoadResult.Error(e)
		}
	}
	
}