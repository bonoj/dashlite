package com.bonoj.dashlite.data.model

data class MerchantPromotion(
    val delivery_fee: Any,
    val delivery_fee_monetary_fields: DeliveryFeeMonetaryFields,
    val id: Int,
    val minimum_subtotal: Any,
    val minimum_subtotal_monetary_fields: MinimumSubtotalMonetaryFields,
    val new_store_customers_only: Boolean
)