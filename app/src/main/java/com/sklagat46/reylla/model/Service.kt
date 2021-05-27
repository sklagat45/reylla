package com.sklagat46.reylla.model

import android.os.Parcel
import android.os.Parcelable

data class Service(
    var provider_id: String,
    var styleName: String,
    var styleDuration: String,
    var styleCost: String,
    val mServiceImageURL: String,
    var service_id: String = "",
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(provider_id)
        writeString(styleName)
        writeString(styleDuration)
        writeString(styleCost)
        writeString(mServiceImageURL)
        writeString(service_id)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Service> = object : Parcelable.Creator<Service> {
            override fun createFromParcel(source: Parcel): Service = Service(source)
            override fun newArray(size: Int): Array<Service?> = arrayOfNulls(size)
        }
    }
}
