package com.bonoj.dashlite.data.source

import com.bonoj.dashlite.data.model.Restaurant
import com.bonoj.dashlite.data.model.RestaurantDetails
import io.reactivex.Single

interface RestaurantsDataSource {

    fun getRestaurantDetails(id: Long): Single<RestaurantDetails>

    fun getRestaurants(): Single<List<Restaurant>>
}
