package com.srklagat.reylla.activities.serviceproviders.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.srklagat.reylla.R
import com.srklagat.reylla.activities.BaseActivity
import com.srklagat.reylla.activities.serviceproviders.ServiceProvidersMainPage
import com.srklagat.reylla.firebase.FirestoreClass
import com.srklagat.reylla.model.IndividualProviders
import com.srklagat.reylla.utils.Constants
import com.srklagat.reylla.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_complete_profile.*
import java.io.IOException

class CompleteProfile : BaseActivity() {
    private var mLatitude: Double = 0.0 // A variable which will hold the latitude value.
    private var mLongitude: Double = 0.0 // A variable which will hold the longitude value.
    private var mIndividualProviders: IndividualProviders? = null

    // Add a global variable for URI of a selected image from phone storage.
    private var mSelectedImageFileUri: Uri? = null
    private val currentUser = FirebaseAuth.getInstance().currentUser!!

    // Access a Cloud Firestore instance.
    private val mFireStore = FirebaseFirestore.getInstance()

    // A global variable for uploaded product image URL.
    private var mUserProfileImageURL: String = ""

    private lateinit var mFusedLocationClient: FusedLocationProviderClient // A fused location client variable which is further user to get the user's current location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_profile)
        // Initialize the Fused location variable
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        /**
         * Initialize the places sdk if it is not initialized earlier using the api key.
         */
        if (!Places.isInitialized()) {
            Places.initialize(
                this@CompleteProfile,
                resources.getString(R.string.google_maps_key)
            )
        }


        tv_add_image.setOnClickListener {
            cameraActivity()
        }

//        if (mIndividualProviders != null) {
//            supportActionBar?.title = "Edit Happy Place"
//            et_location.setText(mIndividualProviders!!.individualAddress)
//            mLatitude = mIndividualProviders!!.latitude
//            mLongitude = mIndividualProviders!!.longitude
//
//            saveImageToInternalStorage = Uri.parse(mIndividualProviders!!.individualProfileImage)
//
//            iv_place_image.setImageURI(saveImageToInternalStorage)
//
//            btn_save.text = "UPDATE"
//        }


    }

    private fun cameraActivity() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            Constants.showImageChooser(this@CompleteProfile)
        } else {
            /*Requests permissions to be granted to this application. These permissions
             must be requested in your manifest, they should not be granted to your app,
             and they should have protection level*/
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                Constants.READ_STORAGE_PERMISSION_CODE
            )
        }
    }

    /**
     * This function will identify the result of runtime permission after the user allows or deny permission based on the unique code.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this@CompleteProfile)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /**
     * Receive the result from a previous call to
     * {@link #startActivityForResult(Intent, int)}.  This follows the
     * related Activity API as described there in
     * {@link Activity#onActivityResult(int, int, Intent)}.
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {

                        // The uri of selected image from phone storage.
                        mSelectedImageFileUri = data.data!!

                        GlideLoader(this@CompleteProfile).loadUserPicture(
                            mSelectedImageFileUri!!,
                            iv_logo_image
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@CompleteProfile,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // A log is printed when user close or cancel the image selection.
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }

    /**
     * A function to update user profile details to the firestore.
     */
    private fun updateUserProfileDetails() {

        val userHashMap = HashMap<String, Any>()
        if (mUserProfileImageURL.isNotEmpty()) {
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }

        // call the registerUser function of FireStore class to make an entry in the database.
        FirestoreClass().updateUserProfileData(
            this@CompleteProfile,
            userHashMap
        )
    }

    /**
     * A function to notify the success result and proceed further accordingly after updating the user details.
     */
    fun userProfileUpdateSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            this@CompleteProfile,
            resources.getString(R.string.msg_profile_update_success),
            Toast.LENGTH_SHORT
        ).show()


        // Redirect to the Main Screen after profile completion.
        startActivity(Intent(this@CompleteProfile, ServiceProvidersMainPage::class.java))
        finish()
    }

    /**
     * A function to notify the success result of image upload to the Cloud Storage.
     *
     * @param imageURL After successful upload the Firebase Cloud returns the URL.
     */
    fun imageUploadSuccess(imageURL: String) {

        mUserProfileImageURL = imageURL

        updateUserProfileDetails()
    }
}