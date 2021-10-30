package com.srklagat.reylla.model

import com.google.firebase.firestore.Exclude
import com.srklagat.reylla.utils.Constants

data class BridalService(
    var providerID: String = Constants.EMPTY_STRING,
    var styleName: String = Constants.EMPTY_STRING,
    var styleDuration: String = Constants.EMPTY_STRING,
    var styleCost: String = Constants.EMPTY_STRING,
    val mserviceImageURL: String = Constants.EMPTY_STRING,
    var service_id: String = "",
) {
    constructor():this("","","","","")
}