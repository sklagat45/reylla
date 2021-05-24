package com.sklagat46.reylla.model

import com.sklagat46.reylla.utils.Constants

data class BridalService(
    var providerID: String = Constants.EMPTY_STRING,
    var styleName: String = Constants.EMPTY_STRING,
    var styleDuration: String = Constants.EMPTY_STRING,
    var styleCost: String = Constants.EMPTY_STRING,
    val mServiceImageURL: String = Constants.EMPTY_STRING,
    var service_id: String = "",
)