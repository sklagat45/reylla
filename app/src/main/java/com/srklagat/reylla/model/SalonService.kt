package com.srklagat.reylla.model


data class SalonService(
    var providerID: String = "",
    var styleName: String = "",
    var styleDuration: String = "",
    var styleCost: String = "",
    val mserviceImageURL: String = "",
    var service_id: String = "",
) {
    constructor():this("","","","","")
}
