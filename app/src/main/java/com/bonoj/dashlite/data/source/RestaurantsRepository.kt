package com.bonoj.dashlite.data.source

import android.content.Context
import com.bonoj.dashlite.data.model.Restaurant
import com.bonoj.dashlite.data.model.RestaurantDetails
import com.bonoj.dashlite.data.source.remote.DashLiteApiUtils
import io.reactivex.Single

class RestaurantsRepository(private val context: Context) : RestaurantsDataSource {

    // Local cache
    val restaurants = ArrayList<Restaurant>()


    // Remote
    private val apiService = DashLiteApiUtils.apiService
    private val lat = 37.422740
    private val lng = -122.139956
    private var limit = 20
    private var offset = 0

    override fun getRestaurants(): Single<List<Restaurant>> {
        return Single.fromCallable { requestRestaurantsFromApi() }
    }

    override fun getRestaurantDetails(id: Long): Single<RestaurantDetails> {
        return Single.fromCallable { requestDetailsFromApi(id) }
    }

    private fun requestRestaurantsFromApi(): List<Restaurant> {

        val response = apiService.setParameters(lat, lng, limit, offset).execute()

        response.body()?.forEach {
            if (!restaurants.contains(it)) {
                restaurants.add(it)
                offset += 1
            }
        }
        return restaurants
    }

    private fun requestDetailsFromApi(id: Long): RestaurantDetails? {
        val response = apiService.setId(id).execute()
        return response.body()
    }
}