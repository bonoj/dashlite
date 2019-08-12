package com.bonoj.dashlite.data.source.remote

import com.bonoj.dashlite.data.model.Restaurant
import com.bonoj.dashlite.data.model.RestaurantDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DashLiteApiService {

    @GET("/v2/restaurant/")
    fun setParameters(@Query("lat") lat: Double,
                      @Query("lng") lng: Double,
                      @Query("limit") limit: Int,
                      @Query("offset") offset: Int
    ): Call<List<Restaurant>>

    @GET("/v2/restaurant/{id}")
    fun setId(@Path("id") id: Long
    ): Call<RestaurantDetails>
}