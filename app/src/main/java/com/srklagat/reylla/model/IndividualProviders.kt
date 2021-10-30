package com.srklagat.reylla.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IndividualProviders(

    val id: String = "",
    val idController: Int = 1,
    val individualProfileImage: String = "",
    val individualFirstName: String = "",
    val individualLastName: String = "",
    val individualDOB: String = "",
    val individualAddress: String = "",
    val individualPhoneNum: String = "0",
    val individualEmail: String = "",
    val individualGender: String = "",
    val fcmToken: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0

) : Parcelable