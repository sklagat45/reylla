package com.sklagat46.reylla.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Customer(

    val id: String = "",
    val customerProfileImage: String = "0",
    val customerFName: String = "",
    val customerLName: String = "",
    val customerDOB: String = "",
    val customerAddress: String = "",
    val customerPhoneNumber: String = "0",
    val customerEmail: String = "",
    val customerGender: String = "",
    val fcmToken: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0


    ) : Parcelable