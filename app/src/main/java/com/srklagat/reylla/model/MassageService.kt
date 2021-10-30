package com.srklagat.reylla.model

class MassageService(
    val providerID: String,
    var styleName: String,
    var styleDuration: String,
    var styleCost: String,
    val mserviceImageURL: String,
    var service_id: String = "",
    ) {
    constructor():this("","","","","")
}