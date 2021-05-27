package com.sklagat46.reylla.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BridalService(

    val providerID: String,
    var styleName: String,
    var styleDuration: String,
    var styleCost: String,
    val mServiceImageURL: String,
    var service_id: String = "",


    ) : Parcelable