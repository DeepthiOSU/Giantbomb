package com.example.giantbomb.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientFactory {
    private fun retrofit(
        baseUrl: String
    ) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> createApiService(
        baseUrl: String,
        service: Class<T>
    ): T = retrofit(baseUrl).create(service)
}