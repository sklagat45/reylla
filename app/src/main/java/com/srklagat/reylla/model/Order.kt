package com.srklagat.reylla.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Order(
    val user_id: String = "",
    val items: ArrayList<Cart> = ArrayList(),
    val address:  String = "",
    val title: String = "",
    val image: String = "",
    val sub_total_amount: String = "",
    val service_discount: String = "",
    val total_amount: String = "",
    val order_datetime: Long = 0L,
    var id: String = ""
) : Parcelable