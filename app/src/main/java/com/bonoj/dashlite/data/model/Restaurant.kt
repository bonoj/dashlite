package com.bonoj.dashlite.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Restaurant(
//    val address: Address,
//    val asap_time: Any?,
//    val average_rating: Double,
    val business: Business,
//    val business_id: Int,
//    val composite_score: Int,
    val cover_img_url: String,
//    val delivery_fee: Int,
//    val description: String,
//    val extra_sos_delivery_fee: Int,
//    val featured_category_description: Any,
    val header_img_url: String?,
    val id: Long,
//    val is_newly_added: Boolean,
//    val is_only_catering: Boolean,
//    val is_time_surging: Boolean,
//    val max_composite_score: Int,
//    val max_order_size: Int,
//    val menus: List<Menu>,
//    val merchant_promotions: List<MerchantPromotion>,
//    val name: String,
//    val number_of_ratings: Int,
//    val price_range: Int,
//    val promotion: Any,
//    val service_rate: Int,
//    val slug: String,
    val status: String?,
//    val status_type: String?,
    val tags: List<String>
//    val url: String,
//    val yelp_rating: Int,
//    val yelp_review_count: Int
) : Parcelable