package com.bonoj.dashlite.details

import com.bonoj.dashlite.data.model.RestaurantDetails

interface DetailsContract {

    interface View {
        val id: Long
        fun displayDetails(restaurantDetails: RestaurantDetails)
        fun displayError()
    }

    interface Presenter {
        fun loadDetails()
        fun unsubscribe()
    }
}