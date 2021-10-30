package com.srklagat.reylla.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Company(

    var id: String = "",
    val idController: Int = 3,
    val companyImage: String = "",
    val companyName: String = "",
    val registrationNumber: String = "",
    val companyAddress: String = "",
    val companyPhonenumber: String = "0",
    val companyEmail: String = "",
    val fcmToken: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
    ) : Parcelable