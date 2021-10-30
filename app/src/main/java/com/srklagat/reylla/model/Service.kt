package com.srklagat.reylla.model

data class Service(
    var provider_id: String = "",
    var styleName: String = "",
    var styleDuration: String = "",
    var styleCost: String = "",
    val mserviceImageURL: String = "",
    var service_id: String = "",
) {
    constructor():this("","","","","")
}
