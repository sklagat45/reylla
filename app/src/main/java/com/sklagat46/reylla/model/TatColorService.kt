package com.sklagat46.reylla.model

class TatColorService(
    val providerID: String,
    var styleName: String,
    var styleDuration: String,
    var styleCost: String,
    var mserviceImageURL: String,
    var service_id: String = "",
    ) {
    constructor():this("","","","","")
}