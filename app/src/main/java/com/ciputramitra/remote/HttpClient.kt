package com.ciputramitra.remote

import com.ciputramitra.cache.DataStoreManager
import com.ciputramitra.consultation.BuildConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class HttpClient(private val dataStoreManager: DataStoreManager) {
	private var retrofit: Retrofit? = null
	private var apiService: ApiService? = null
	
	init {
		buildRetrofitClient()
	}
	
	fun getApi(): ApiService? {
		if (apiService == null) {
			apiService = retrofit?.create(ApiService::class.java)
		}
		return apiService
	}
	
	// Funsi untuk rebuild client secara eksplisit jika diperlukan
	fun rebuildClient() {
		buildRetrofitClient()
		apiService = retrofit?.create(ApiService::class.java)
	}
	
	private fun buildRetrofitClient() {
		val clientBuilder = OkHttpClient.Builder()
			.connectTimeout(20, TimeUnit.SECONDS)
			.readTimeout(60, TimeUnit.SECONDS)
			.writeTimeout(60, TimeUnit.SECONDS)
		if (BuildConfig.DEBUG) {
			clientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
				level = HttpLoggingInterceptor.Level.BODY
			})
		}
		
		// Gunakan interceptor yang selalu mengambil token terbaru
		clientBuilder.addInterceptor { chain ->
			val currentToken = runBlocking { dataStoreManager.tokenFlow.first() }
			val request = chain.request().newBuilder()
				.apply {
					if (!currentToken.isNullOrEmpty()) {
						addHeader("Authorization", "Bearer $currentToken")
					}
				}
				.build()
			chain.proceed(request)
		}
		
		retrofit = Retrofit.Builder()
			.baseUrl("${BuildConfig.BASE_URL}/api/v1/")
			.client(clientBuilder.build())
			.addConverterFactory(GsonConverterFactory.create())
			.build()
	}
}