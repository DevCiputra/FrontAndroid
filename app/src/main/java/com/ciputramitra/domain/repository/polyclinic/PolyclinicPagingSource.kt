package com.ciputramitra.domain.repository.polyclinic

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ciputramitra.remote.ApiService
import com.ciputramitra.response.polyclinic.Data

class PolyclinicPagingSource(
	private val apiService: ApiService
): PagingSource<Int , Data>() {
	override fun getRefreshKey(state: PagingState<Int , Data>): Int {
		return 1
	}
	
	override suspend fun load(params: LoadParams<Int>): LoadResult<Int , Data> {
		return try {
			val currentPage = params.key ?: 1
			val response = apiService.fetchPolyclinic(
				page = currentPage,
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