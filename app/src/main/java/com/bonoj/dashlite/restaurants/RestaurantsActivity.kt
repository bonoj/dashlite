package com.bonoj.dashlite.restaurants

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bonoj.dashlite.R
import com.bonoj.dashlite.data.model.Restaurant
import com.bonoj.dashlite.data.source.RestaurantsDataSource
import com.bonoj.dashlite.details.DetailsActivity
import com.bonoj.dashlite.root.DashLiteApplication
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_restaurants.*
import javax.inject.Inject

class RestaurantsActivity : AppCompatActivity(), RestaurantsContract.View, RestaurantsAdapter.ItemClickListener {

    companion object {
        @JvmStatic
        val DETAILS_INTENT_KEY = "com.bonoj.dashlite.DETAILS_INTENT_KEY"
    }

    private val RESTAURANTS_PARCEL_KEY = "com.bonoj.dashlite.RESTAURANTS_PARCEL_KEY"

    @Inject
    lateinit var restaurantsDataSource: RestaurantsDataSource

    lateinit var presenter: RestaurantsPresenter

    lateinit var adapter: RestaurantsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurants)

        DashLiteApplication.graph.inject(this)

        val layoutManager = LinearLayoutManager(this)

        adapter = RestaurantsAdapter(this, this)
        restaurants_rv.setHasFixedSize(true)
        restaurants_rv.setLayoutManager(layoutManager)
        restaurants_rv.adapter = adapter
        restaurants_rv.addOnScrollListener(
            EndlessScrollListener({ presenter.loadRestaurants() }, layoutManager)
        )

        if (savedInstanceState != null) {
            adapter.refillAdapterAfterDeviceRotation(savedInstanceState.getParcelableArrayList(RESTAURANTS_PARCEL_KEY)!!)
        }

        presenter = RestaurantsPresenter(this, restaurantsDataSource, AndroidSchedulers.mainThread())
        presenter.loadRestaurants()
    }

    override fun onStop() {
        super.onStop()

        presenter.unsubscribe()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putParcelableArrayList(RESTAURANTS_PARCEL_KEY, adapter.getRestaurantsParcel())
    }

    override fun displayRestaurants(restaurants: List<Restaurant>) {
        adapter.setRestaurants(restaurants)

        restaurants_progress_bar.visibility = View.GONE
        restaurants_empty_tv.visibility = View.GONE
        restaurants_rv.visibility = View.VISIBLE
    }

    override fun displayNoRestaurants() {
        if (restaurants_rv.adapter?.itemCount == 0) {
            restaurants_empty_tv.setText(R.string.unavailable)
            restaurants_progress_bar.visibility = View.GONE
            restaurants_rv.visibility = View.GONE
            restaurants_empty_tv.visibility = View.VISIBLE
        }
    }

    override fun displayError() {
        if (restaurants_rv.adapter?.itemCount == 0) {
            restaurants_empty_tv.setText(R.string.error_connection)
            restaurants_progress_bar.visibility = View.GONE
            restaurants_rv.visibility = View.GONE
            restaurants_empty_tv.visibility = View.VISIBLE
        }
    }

    override fun onItemClick(view: View, position: Int) {
        //val id: Int = view.getTag() as Int

        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(DETAILS_INTENT_KEY, adapter.getRestaurant(position))

        startActivity(intent)
    }
}
