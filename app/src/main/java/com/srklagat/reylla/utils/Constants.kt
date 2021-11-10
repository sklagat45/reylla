package com.srklagat.reylla.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {


    const val ORDERS: String = "orders"
    const val SALONSERVICE: String = "SalonService"
    const val CART_ITEMS: String = "cart_items"
    const val USER_ID: String= "userId"

    // Firebase Constants
    // This  is used for the collection name for USERS.
    const val EMPTY_STRING: String = ""
    const val USERS: String = "users"
    const val CUSTOMERS: String = "customers"
    const val COMPANIES: String = "companies"
    const val INDIVIDUALPROVIDERS: String = "individualProviders"
    const val HAIR_SERVICES: String = "hairServices"
    const val HAIR_STYLE_NAME: String = "hairStyleName"
    const val BRIDAL_SERVICES: String = "bridalServices"
    const val NAIL_SERVICES: String = "nailServices"
    const val MAKEUP_SERVICES: String = "makeupServices"
    const val MASSAGE_SERVICES: String = "massageService"
    const val SALONS: String = "salons"

    const val SERVICE: String = "service"


    const val PROVIDER_ID: String = "provider_id"
    const val PROVIDER_IDD: String = "providerID"
    const val TATCOLOUR_SERVICES: String = "tatcolourService"
    const val GALLERY_ITEMS: String = "galleryItems"


    // Intent extra constants.
    const val EXTRA_USER_DETAILS: String = "extra_user_details"

    const val EXTRA_SERVICE_ID: String = "extra_service_id"
    const val EXTRA_TIME_PICKED_ID: String = "extra_time_picked_id"

    const val EXTRA_SERVICE_OWNER_ID: String = "extra_service_owner_id"

    const val EXTRA_SALON_ID: String= "extra_salon_id"
    const val EXTRA_USER_ID: String= "extra_user_id"
    const val EXTRA_SALON_NAME: String= "extra_salon_name"

    const val EXTRA_SALON_ADDRESS: String= "extra_salon_address_id"
    const val EXTRA_LONGITUDE: String= "extra_user_longitude_id"
    const val EXTRA_LATITUDE: String= "extra_latitude_name"



    const val SERVICE_IMAGE: String = "Service_Image"


    // Firebase database field names
    const val IMAGE: String = "image"
    const val NAME: String = "name"
    const val MOBILE: String = "mobile"
    const val ASSIGNED_TO: String = "assignedTo"
    const val DOCUMENT_ID: String = "documentId"
    const val TASK_LIST: String = "taskList"
    const val ID: String = "id"
    const val EMAIL: String = "email"

    //A unique code for asking the Read Storage Permission using this we will be check and identify in the method onRequestPermissionsResult
    const val READ_STORAGE_PERMISSION_CODE = 1
    // A unique code of image selection from Phone Storage.
    const val PICK_IMAGE_REQUEST_CODE = 2

    const val REYLLA_PREFERENCES: String = "ReyllaPrefs"
    const val FCM_TOKEN:String = "fcmToken"
    const val FCM_TOKEN_UPDATED:String = "fcmTokenUpdated"

    // TODO (Step 1: Add the base url  and key params for sending firebase notification.)
    // START
    const val FCM_BASE_URL:String = "https://fcm.googleapis.com/fcm/send"
    const val FCM_AUTHORIZATION:String = "authorization"
    const val FCM_KEY:String = "key"
//    const val FCM_SERVER_KEY:String = "AAAA-oftC-Q:APA91bFbmaYmj3lu_Oy3vwYkqh0tHjFNFanTjYIR-ilKo1SsoQHF8ODMiOzZg3JZAAfYGdOdaM_kBRRyM5IfXbaXq0phVHnUFrG35TWnxfJgK6ysrwqBYdoP-WPWAEjwQn1eeA7Sv1CC"
    const val FCM_KEY_TITLE:String = "title"
    const val FCM_KEY_MESSAGE:String = "message"
    const val FCM_KEY_DATA:String = "data"
    const val FCM_KEY_TO:String = "to"
// END



    /**
     * A function for user profile image selection from phone storage.
     */
    fun showImageChooser(activity: Activity) {
        // An intent for launching the image selection of phone storage.
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        // Launches the image selection of phone storage using the constant code.
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    /**
     * A function to get the image file extension of the selected image.
     *
     * @param activity Activity reference.
     * @param uri Image file uri.
     */
    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        /*
         * MimeTypeMap: Two-way map that maps MIME-types to file extensions and vice versa.
         *
         * getSingleton(): Get the singleton instance of MimeTypeMap.
         *
         * getExtensionFromMimeType: Return the registered extension for the given MIME type.
         *
         * contentResolver.getType: Return the MIME type of the given content URL.
         */
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}