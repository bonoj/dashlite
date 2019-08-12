package com.bonoj.dashlite.details

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bonoj.dashlite.R
import com.bonoj.dashlite.data.model.Restaurant
import com.bonoj.dashlite.data.model.RestaurantDetails
import com.bonoj.dashlite.data.source.RestaurantsDataSource
import com.bonoj.dashlite.restaurants.RestaurantsActivity
import com.bonoj.dashlite.root.DashLiteApplication
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_details.*
import javax.inject.Inject

class DetailsActivity : AppCompatActivity(), DetailsContract.View {

    @Inject
    lateinit var restaurantsDataSource: RestaurantsDataSource

    lateinit var presenter: DetailsPresenter
    lateinit var restaurant: Restaurant

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        DashLiteApplication.graph.inject(this)

        restaurant = intent.getParcelableExtra(RestaurantsActivity.DETAILS_INTENT_KEY)!!

        displayRestaurant()

        presenter = DetailsPresenter(this, restaurantsDataSource, AndroidSchedulers.mainThread())
        presenter.loadDetails()
    }

    override fun onStop() {
        super.onStop()

        presenter.unsubscribe()
    }

    override val id: Long
        get() = restaurant.id

    private fun displayRestaurant() {
        supportActionBar?.setTitle(restaurant.business.name)

        val requestOptions = RequestOptions()
        requestOptions.placeholder(R.drawable.placeholder)

        Glide.with(this)
            .load(restaurant.header_img_url)
            .apply(requestOptions)
            .into(details_header_iv)
    }

    override fun displayDetails(restaurantDetails: RestaurantDetails) {
        details_display_cl.visibility = View.VISIBLE

        details_description_tv.text = restaurantDetails.description ?: getString(R.string.description_unavailable)

        if (restaurantDetails.average_rating > 0.0) {
            details_rating_tv.text = getString(R.string.rating, restaurantDetails.average_rating.toString())
        } else {
            details_rating_tv.text = getString(R.string.rating_unavailable)
        }

        details_phone_tv.text = restaurantDetails.phone_number ?: getString(R.string.unavailable)
        if (restaurantDetails.phone_number != null) {
            details_phone_tv.setTextColor(Color.BLUE)
            details_phone_tv.setOnClickListener {
                val number = "tel:" + restaurantDetails.phone_number
                startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(number)))
            }
        }

        details_address_tv.text = restaurantDetails.address?.printable_address
    }

    override fun displayError() {
        details_empty_tv.setText(R.string.error_connection)
        details_empty_tv.visibility = View.VISIBLE
    }

}









