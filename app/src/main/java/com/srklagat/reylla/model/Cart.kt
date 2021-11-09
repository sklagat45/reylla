package com.srklagat.reylla.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cart(
    val user_id: String = "",
    val title: String = "",
    val duration: String = "",
    val price: String = "",
    val image: String = "",
    var Service_owner_id: String = "",
    var id: String = "",
    var date: String = "",
    var time: String = "",
    ) : Parcelable