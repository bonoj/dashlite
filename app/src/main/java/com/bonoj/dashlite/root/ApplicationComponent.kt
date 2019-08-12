package com.bonoj.dashlite.root

import com.bonoj.dashlite.details.DetailsActivity
import com.bonoj.dashlite.restaurants.RestaurantsActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

    fun inject(application: DashLiteApplication)

    fun inject(restaurantsActivity: RestaurantsActivity)

    fun inject(detailsActivity: DetailsActivity)
}