package com.bonoj.dashlite.restaurants

import com.bonoj.dashlite.data.model.Restaurant
import com.bonoj.dashlite.data.source.RestaurantsDataSource
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class RestaurantsPresenter(private val view: RestaurantsContract.View,
private val restaurantsDataSource: RestaurantsDataSource,
private val mainScheduler: Scheduler) : RestaurantsContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    override fun loadRestaurants() {

        val disposableSingleObserver = restaurantsDataSource.getRestaurants()
            .subscribeOn(Schedulers.io())
            .observeOn(mainScheduler)
            .subscribeWith(object : DisposableSingleObserver<List<Restaurant>>() {
                override fun onSuccess(restaurants: List<Restaurant>) {

                    if (restaurants.isEmpty()) {
                        view.displayNoRestaurants()
                    } else {
                        view.displayRestaurants(restaurants)
                    }
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