package com.sklagat46.reylla.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IndividualProviders(

    val id: String = "",
    val individualProfileImage: String = "",
    val individualFirstName: String = "",
    val individualLastName: String = "",
    val individualDOB: String = "",
    val individualAddress: String = "",
    val individualPhoneNum: String = "0",
    val individualEmail: String = "",
    val individualGender: String = "",
    val fcmToken: String = ""

) : Parcelable