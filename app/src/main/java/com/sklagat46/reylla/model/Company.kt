package com.sklagat46.reylla.model

import android.os.Parcel
import android.os.Parcelable


data class Company(

    val id: String = "",
    val companyImage: String = "",
    val companyName: String ="",
    val registrationNumber: String = "",
    val companyAddress: String = "",
    val companyPhonenumber: Long = 0,
    val companyEmail: String = "",
    val fcmToken: String = "",
    val companyPassword: String = ""

) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readLong(),
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(companyImage)
        writeString(companyName)
        writeString(companyAddress)
        writeLong(companyPhonenumber)
        writeString(companyEmail)
        writeString(fcmToken)
        writeString(companyPassword)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Company> = object : Parcelable.Creator<Company> {
            override fun createFromParcel(source: Parcel): Company = Company(source)
            override fun newArray(size: Int): Array<Company?> = arrayOfNulls(size)
        }
    }
}