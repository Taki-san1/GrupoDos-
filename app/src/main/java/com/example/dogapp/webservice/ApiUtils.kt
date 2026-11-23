package com.example.dogapp.webservice

object ApiUtils {
    private val apiService: ApiService by lazy {
        RetrofitClient.getRetrofit().create(ApiService::class.java)
    }

    fun getApiService(): ApiService = apiService
}
