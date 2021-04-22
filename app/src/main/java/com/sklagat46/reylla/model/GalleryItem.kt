package com.sklagat46.reylla.model

import android.os.Parcel
import android.os.Parcelable

data class GalleryItem(
    val id: String = "",
    val userId:  String = "",
    val image: Int= 0,
    val date: String
) : Parcelable {
    constructor(source: Parcel) : this(

        source.readString()!!,
        source.readString()!!,
        source.readInt(),
        source.readString()!!,

        )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(userId)
        writeInt(image)
        writeString(date)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GalleryItem> = object : Parcelable.Creator<GalleryItem> {
            override fun createFromParcel(source: Parcel): GalleryItem = GalleryItem(source)
            override fun newArray(size: Int): Array<GalleryItem?> = arrayOfNulls(size)
        }
    }

}

