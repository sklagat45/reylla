package com.sklagat46.reylla.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.synthetic.main.activity_register_customer.*

data class Customer(

    val id: String = "",
    val customerImage: String = "",
    val customerFName: String ="",
    val customerLName: String = "",
    val customerDOB: String = "",
    val customerAddress: String = "",
    val customerPhoneNumber: Long = 0,
    val customerEmail: String = "",
    val customerGender: String = "",
    val fcmToken: String = "",
    val customerPassword: String = ""

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
        writeString(customerImage)
        writeString(customerFName)
        writeString(customerLName)
        writeString(customerDOB)
        writeString(customerAddress)
        writeLong(customerPhoneNumber)
        writeString(customerEmail)
        writeString(customerGender)
        writeString(fcmToken)
        writeString(customerPassword)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Customer> = object : Parcelable.Creator<Customer> {
            override fun createFromParcel(source: Parcel): Customer = Customer(source)
            override fun newArray(size: Int): Array<Customer?> = arrayOfNulls(size)
        }
    }
}