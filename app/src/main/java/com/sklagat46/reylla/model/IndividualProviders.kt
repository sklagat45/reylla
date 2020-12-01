package com.sklagat46.reylla.model

import android.os.Parcel
import android.os.Parcelable


data class IndividualProviders(

    val id: String = "",
    val IndividualProfileImage: String = "",
    val IndividualFirstName: String ="",
    val IndividualLastName: String = "",
    val IndividualDOB: String = "",
    val IndividualEddress: String = "",
    val IndividualPhoneNum: Long = 0,
    val IndividualEmail: String = "",
    val IndividualGender: String = "",
    val fcmToken: String = "",
    val IndividaulPassword: String = ""

) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readLong(),
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(IndividualProfileImage)
        writeString(IndividualFirstName)
        writeString(IndividualLastName)
        writeString(IndividualDOB)
        writeString(IndividualEddress)
        writeLong(IndividualPhoneNum)
        writeString(IndividualEmail)
        writeString(IndividualGender)
        writeString(fcmToken)
        writeString(IndividaulPassword)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<IndividualProviders> = object : Parcelable.Creator<IndividualProviders> {
            override fun createFromParcel(source: Parcel): IndividualProviders = IndividualProviders(source)
            override fun newArray(size: Int): Array<IndividualProviders?> = arrayOfNulls(size)
        }
    }
}