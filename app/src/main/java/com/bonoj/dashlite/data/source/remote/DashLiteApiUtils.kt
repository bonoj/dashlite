package com.bonoj.dashlite.data.source.remote

object DashLiteApiUtils {

    val BASE_URL = "https://api.doordash.com"

    val apiService: DashLiteApiService
        get() = RetrofitClient.getClient(BASE_URL).create(DashLiteApiService::class.java)
}