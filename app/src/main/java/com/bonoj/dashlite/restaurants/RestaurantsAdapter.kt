package com.bonoj.dashlite.restaurants

import android.content.Context
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bonoj.dashlite.R
import com.bonoj.dashlite.data.model.Restaurant
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.list_item_restaurant.view.*

class RestaurantsAdapter(private val context: Context,
                          private val clickListener: ItemClickListener) : RecyclerView.Adapter<RestaurantsAdapter.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    private var restaurants: ArrayList<Restaurant> = ArrayList()
    private val favoriteRestaurants: ArrayList<Restaurant> = ArrayList()

    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.list_item_restaurant, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val id = restaurants[position].id
        val name = restaurants[position].business.name
        val imageUrl = restaurants[position].cover_img_url
        val tags = restaurants[position].tags
        val status = restaurants[position].status
        val tagsString: String;
        when(tags.size) {
            0 -> tagsString = context.getString(R.string.unavailable)
            1 -> tagsString = tags.get(0)
            else -> tagsString = tags.get(0) + ", " + tags.get(1)
        }


        val requestOptions = RequestOptions()
        requestOptions.placeholder(R.drawable.placeholder)

        Glide.with(context)
            .load(imageUrl)
            .apply(requestOptions)
            .into(holder.iv)

        holder.itemView.tag = id
        holder.nameTv.text = name
        holder.tagsTv.text = tagsString
        holder.statusTv.text = status ?: context.getString(R.string.unavailable)
        holder.favoriteBtn.setOnClickListener {
            toggleFavorite(restaurants[position])
        }

        // Temporarily disable and hide button
        holder.favoriteBtn.isEnabled = false
        holder.favoriteBtn.visibility = View.INVISIBLE
    }

    override fun getItemCount(): Int {
        return restaurants.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var iv: ImageView = itemView.list_item_iv
        var nameTv: TextView = itemView.list_item_name_tv
        var tagsTv: TextView = itemView.list_item_tags_tv
        var statusTv: TextView = itemView.list_item_status_tv
        var favoriteBtn: Button = itemView.list_item_favorite

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            clickListener.onItemClick(view, adapterPosition)
        }
    }

    fun setRestaurants(restaurants: List<Restaurant>) {

        for (restaurant in restaurants) {
            if (restaurant !in this.restaurants) {
                this.restaurants.add(restaurant)
            }
        }

        notifyDataSetChanged()
    }

    fun getRestaurantsParcel() : ArrayList<Restaurant> {
        return restaurants
    }

    fun getRestaurant(position: Int): Restaurant {
        return restaurants[position]
    }

    fun refillAdapterAfterDeviceRotation(preexistingRestaurants: List<Restaurant>) {
        restaurants.addAll(preexistingRestaurants)
    }

    fun toggleFavorite(restaurant: Restaurant) {
        if (!favoriteRestaurants.contains(restaurant)) {
            favoriteRestaurants.add(restaurant)
        } else {
            favoriteRestaurants.remove(restaurant)
        }
    }

    fun getFavoriteRestaurants() : ArrayList<Restaurant> {
        return favoriteRestaurants
    }
}