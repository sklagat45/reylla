package com.sklagat46.reylla.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Company(

    val id: String = "",
    val companyImage: String = "",
    val companyName: String = "",
    val registrationNumber: String = "",
    val companyAddress: String = "",
    val companyPhonenumber: String = "0",
    val companyEmail: String = "",
    val fcmToken: String = "",

    ) : Parcelable