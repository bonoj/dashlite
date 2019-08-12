package com.bonoj.dashlite.root

import android.content.Context
import com.bonoj.dashlite.data.source.RestaurantsDataSource
import com.bonoj.dashlite.data.source.RestaurantsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: DashLiteApplication) {

    private val restaurantsRepository: RestaurantsRepository

    init {
        restaurantsRepository = RestaurantsRepository(application)
    }

    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideRestaurantsRepository(context: Context): RestaurantsDataSource {
        return restaurantsRepository
    }
}
