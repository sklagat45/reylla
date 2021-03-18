package com.sklagat46.reylla.model

import android.os.Parcel
import android.os.Parcelable


data class IndividualProviders(

    val id: String = "",
    val individualProfileImage: String = "",
    val individualFirstName: String ="",
    val individualLastName: String = "",
    val individualDOB: String = "",
    val individualAddress: String = "",
    val individualPhoneNum: String = "0",
    val individualEmail: String = "",
    val individualGender: String = "",
    val fcmToken: String = ""

) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(individualProfileImage)
        writeString(individualFirstName)
        writeString(individualLastName)
        writeString(individualDOB)
        writeString(individualAddress)
        writeString(individualPhoneNum)
        writeString(individualEmail)
        writeString(individualGender)
        writeString(fcmToken)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<IndividualProviders> = object : Parcelable.Creator<IndividualProviders> {
            override fun createFromParcel(source: Parcel): IndividualProviders = IndividualProviders(source)
            override fun newArray(size: Int): Array<IndividualProviders?> = arrayOfNulls(size)
        }
    }
}