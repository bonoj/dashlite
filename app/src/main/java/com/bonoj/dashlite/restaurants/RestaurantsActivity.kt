package com.bonoj.dashlite.restaurants

import android.content.Intent
import android.content.SharedPreferences
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
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_restaurants.*
import java.io.StringReader
import javax.inject.Inject

class RestaurantsActivity : AppCompatActivity(), RestaurantsContract.View, RestaurantsAdapter.ItemClickListener {

    companion object {
        @JvmStatic
        val DETAILS_INTENT_KEY = "com.bonoj.dashlite.DETAILS_INTENT_KEY"
    }

    private val RESTAURANTS_PARCEL_KEY = "com.bonoj.dashlite.RESTAURANTS_PARCEL_KEY"
    private val RESTAURANTS_PREFERNCES = "com.bonoj.dashlite.RESTAURANTS_PREFERENCES"
    private val FAVORITES_KEY = "com.bonoj.dashlite.FAVORITES"

    @Inject
    lateinit var restaurantsDataSource: RestaurantsDataSource

    lateinit var presenter: RestaurantsPresenter

    lateinit var adapter: RestaurantsAdapter

    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurants)

        preferences = this.getSharedPreferences(RESTAURANTS_PREFERNCES,0)

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

//        val favoritesString = preferences.getString(FAVORITES_KEY, "")
//
//        Log.i("fav recovered", favoritesString.toString())
//
//
//        if (!favoritesString.equals("")) {
//            val favorites: List<Restaurant> =
//                Gson().fromJson(StringReader(favoritesString), Array<Restaurant>::class.java).toList()
//
//
//            Log.i("fav", favorites.size.toString())
//
//        }
    }

    override fun onStop() {
        super.onStop()

//        val favorites = adapter.getFavoriteRestaurants()
//        val favoritesString = Gson().toJson(favorites)
//
//        val editor = preferences.edit()
//        editor.putString(FAVORITES_KEY, favoritesString)
//        editor.commit()
//
//        Log.i("fav stop", favoritesString)

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
