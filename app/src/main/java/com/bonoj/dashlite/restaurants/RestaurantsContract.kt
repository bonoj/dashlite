package com.bonoj.dashlite.restaurants

import com.bonoj.dashlite.data.model.Restaurant

interface RestaurantsContract {

    interface View {
        fun displayRestaurants(restaurants: List<Restaurant>)
        fun displayNoRestaurants()
        fun displayError()
    }

    interface Presenter {
        fun loadRestaurants()
        fun unsubscribe()
    }
}