package com.sklagat46.reylla.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GalleryItem(
    val id: String = "",
    val userId: String = "",
    val imageUrl: String = "",
    val date: String
) : Parcelable