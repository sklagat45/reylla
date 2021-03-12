package com.sklagat46.reylla.model

import android.os.Parcel
import android.os.Parcelable

data class Customer(

    val id: String = "",
    val customerProfileImage: String = "0",
    val customerFName: String ="",
    val customerLName: String = "",
    val customerDOB: String = "",
    val customerAddress: String = "",
    val customerPhoneNumber: String = "0",
    val customerEmail: String = "",
    val customerGender: String = "",
    val customerPassword: String = "",
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
        source.readString()!!,
        source.readString()!!,
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(customerProfileImage)
        writeString(customerFName)
        writeString(customerLName)
        writeString(customerDOB)
        writeString(customerAddress)
        writeString(customerPhoneNumber)
        writeString(customerEmail)
        writeString(customerGender)
        writeString(customerPassword)
        writeString(fcmToken)

    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Customer> = object : Parcelable.Creator<Customer> {
            override fun createFromParcel(source: Parcel): Customer = Customer(source)
            override fun newArray(size: Int): Array<Customer?> = arrayOfNulls(size)
        }
    }
}