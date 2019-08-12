package com.bonoj.dashlite.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Business(
    val id: Long,
    val name: String

    //val business_vertical: Any?
) : Parcelable