package com.srklagat.reylla.model

import com.google.firebase.firestore.Exclude

data class GalleryItem(
    @Exclude
    var id: String = "",
    val userId: String = "",
    val imageUrl: String = "",
    val date: String
) {
    constructor():this("","","","")
}