package com.bonoj.dashlite.details

import com.bonoj.dashlite.data.model.RestaurantDetails
import com.bonoj.dashlite.data.source.RestaurantsDataSource
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class DetailsPresenter(private val view: DetailsContract.View,
                       private val restaurantsDataSource: RestaurantsDataSource,
                       private val mainScheduler: Scheduler
) : DetailsContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    override fun loadDetails() {

        val disposableSingleObserver = restaurantsDataSource.getRestaurantDetails(view.id)
            .subscribeOn(Schedulers.io())
            .observeOn(mainScheduler)
            .subscribeWith(object : DisposableSingleObserver<RestaurantDetails>() {
                override fun onSuccess(restaurantDetails: RestaurantDetails) {
                    view.displayDetails(restaurantDetails)
                }

                override fun onError(e: Throwable) {
                    view.displayError()
                }
            })

        compositeDisposable.add(disposableSingleObserver)
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }
}