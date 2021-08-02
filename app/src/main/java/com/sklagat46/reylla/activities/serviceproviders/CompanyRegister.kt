package com.sklagat46.reylla.activities.serviceproviders

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.sklagat46.reylla.R
import com.sklagat46.reylla.activities.BaseActivity
import com.sklagat46.reylla.activities.agentclients.MainActivity
import com.sklagat46.reylla.firebase.FirestoreClass
import com.sklagat46.reylla.model.Company
import com.sklagat46.reylla.utils.Constants
import com.sklagat46.reylla.utils.GetAddressFromLatLng
import com.sklagat46.reylla.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_company_register.*
import java.io.IOException

class CompanyRegister : BaseActivity() {

    private var mSelectedImageFileUri: Uri? = null

    // A global variable for uploaded product image URL.
    private var imageType: String = "logoImage"
    private var mLogoImageURL: String = ""
    private var mFcmToken: String = ""

    private var mLatitude: Double = 0.0 // A variable which will hold the latitude value.
    private var mLongitude: Double = 0.0 // A variable which will hold the longitude value.

    // Access a Cloud Firestore instance.
    private val mFireStore = FirebaseFirestore.getInstance()
    private lateinit var mFusedLocationClient: FusedLocationProviderClient // A fused location client variable which is further user to get the user's current location


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_register)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setupActionBar()
        // Initialize the Fused location variable
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        /**
         * Initialize the places sdk if it is not initialized earlier using the api key.
         */
        if (!Places.isInitialized()) {
            Places.initialize(
                this@CompanyRegister,
                resources.getString(R.string.google_maps_key)
            )
        }

        tv_company_add_photo.setOnClickListener {
            selectImage()
        }
        et_company_address.setOnClickListener {
            pickLocation()
        }
        // Click event for sign-up button.
        btn_comp_register.setOnClickListener {
            registerCompany()
        }

        iv_company_loc_image.setOnClickListener {
            if (!isLocationEnabled()) {
                Toast.makeText(
                    this,
                    "Your location provider is turned off. Please turn it on.",
                    Toast.LENGTH_SHORT
                ).show()

                // This will redirect you to settings from where you need to turn on the location provider.
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            } else {
                // For Getting current location of user please have a look at below link for better understanding
                // https://www.androdocs.com/kotlin/getting-current-location-latitude-longitude-in-android-using-kotlin.html
                Dexter.withActivity(this)
                    .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    .withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                            if (report!!.areAllPermissionsGranted()) {

                                requestNewLocationData()
                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                            token: PermissionToken?
                        ) {
                            showRationalDialogForPermissions()
                        }
                    }).onSameThread()
                    .check()
            }
        }

    }

    /**
     * A function which is used to verify that the location or let's GPS is enable or not of the user's device.
     */
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun pickLocation() {
        try {
            // These are the list of fields which we required is passed
            val fields = listOf(
                Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG,
                Place.Field.ADDRESS
            )
            // Start the autocomplete intent with a unique request code.
            val intent =
                Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(this@CompanyRegister)
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun selectImage() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            Constants.showImageChooser(this@CompanyRegister)
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
                Constants.showImageChooser(this@CompanyRegister)
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
//                        ----------------------------------------------------

                        // The uri of selected image from phone storage.
                        mSelectedImageFileUri = data.data!!

                        GlideLoader(this@CompanyRegister).loadUserPicture(
                            mSelectedImageFileUri!!,
                            iv_company_logo_image
                        )
//                        -------------------------------------------------------
                        FirestoreClass().uploadImageToCloudStorage(
                            this@CompanyRegister,
                            mSelectedImageFileUri,
                            imageType
                        )

                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@CompanyRegister,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            } else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {

                val place: Place = Autocomplete.getPlaceFromIntent(data!!)

                et_company_address.setText(place.address)
                mLatitude = place.latLng!!.latitude
                mLongitude = place.latLng!!.longitude
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // A log is printed when user close or cancel the image selection.
            Log.e("Request Cancelled", "activity cancelled")
        }
    }


    private fun setupActionBar() {

        setSupportActionBar(toolbar_company_sign_up_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.back_btn)
        }
        toolbar_company_sign_up_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun imageUploadSuccess(imageUrl: String) {
        mLogoImageURL = imageUrl
    }


    private fun registerCompany() {

//        FirestoreClass().uploadImageToCloudStorage(this@CompanyRegister,mSelectedImageFileUri,imageType)

        // Here we get the text from editText and trim the space
        val companyImage: String = mLogoImageURL
        val companyName: String = et_company_name.text.toString().trim { it <= ' ' }
        val registrationNumber: String = et_registration_number.text.toString().trim { it <= ' ' }
        val companyAddress: String = et_company_address.text.toString().trim { it <= ' ' }
        val companyPhonenumber: String = et_company_phonenumber.text.toString().trim { it <= ' ' }
        val companyEmail: String = et_company_email.text.toString().trim { it <= ' ' }
        val companyPassword: String = et_company_password.text.toString().trim { it <= ' ' }


        if (validateForm(
                companyName,
                registrationNumber,
                companyAddress,
                companyPhonenumber,
                companyEmail,
                companyPassword
            )
        ) {
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(companyEmail, companyPassword)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        // If the registration is successfully done
                        if (task.isSuccessful) {

                            // Firebase registered customer
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            // Registered Email
                            val registeredEmail = firebaseUser.email!!

                            val company = Company(
                                firebaseUser.uid,
                                companyImage,
                                companyName,
                                registrationNumber,
                                companyAddress,
                                companyPhonenumber,
                                registeredEmail,
                                mFcmToken,
                                mLatitude,
                                mLongitude
                            )

                            // call the registerCustomer function of FirestoreClass to make an entry in the database.
                            FirestoreClass().registerCompany(this@CompanyRegister, company)
                        } else {
                            Toast.makeText(
                                this@CompanyRegister,
                                task.exception!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
        }
    }

    private fun validateForm(
        companyName: String, registrationNumber: String,
        companyAddress: String, companyPhonenumber: String, companyEmail: String,
        companyPassword: String
    ): Boolean {
        return when {
            TextUtils.isEmpty(companyName) or TextUtils.isEmpty(registrationNumber) or
                    TextUtils.isEmpty(companyPhonenumber) -> {
                showErrorSnackBar("Please enter all details.")
                false
            }
            TextUtils.isEmpty(companyEmail) -> {
                showErrorSnackBar("Please enter email.")
                false
            }
            TextUtils.isEmpty(companyAddress) -> {
                showErrorSnackBar("Please enter email.")
                false
            }
            TextUtils.isEmpty(companyPassword) -> {
                showErrorSnackBar("Please enter password.")
                false
            }
            else -> {
                true
            }
        }
    }


    /**
     * A function to be called the customer is registered successfully and entry is made in the firestore database.
     */
    fun companyRegisteredSuccess() {

        Toast.makeText(
            this@CompanyRegister,
            "You have successfully registered.",
            Toast.LENGTH_SHORT
        ).show()

        // Hide the progress dialog
        hideProgressDialog()


        // Launch the register customer screen.
        startActivity(Intent(this@CompanyRegister, MainActivity::class.java))
        //        /**
        //         * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
        //         * and send him to Intro Screen for Sign-In
        //         */
        //        FirebaseAuth.getInstance().signOut()
        // Finish the Sign-Up Screen
        finish()
    }


    /**
     * A function to request the current location. Using the fused location provider client.
     */
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {

        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    /**
     * A location callback object of fused location provider client where we will get the current location details.
     */
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            mLatitude = mLastLocation.latitude
            Log.e("Current Latitude", "$mLatitude")
            mLongitude = mLastLocation.longitude
            Log.e("Current Longitude", "$mLongitude")

            // TODO(Step 2: Call the AsyncTask class fot getting an address from the latitude and longitude.)
            // START
            val addressTask =
                GetAddressFromLatLng(this@CompanyRegister, mLatitude, mLongitude)

            addressTask.setAddressListener(object :
                GetAddressFromLatLng.AddressListener {
                override fun onAddressFound(address: String?) {
                    Log.e("Address ::", "" + address)
                    et_company_address.setText(address) // Address is set to the edittext
                }

                override fun onError() {
                    Log.e("Get Address ::", "Something is wrong...")
                }
            })

            addressTask.getAddress()
            // END
        }
    }

    /**
     * A function used to show the alert dialog when the permissions are denied and need to allow it from settings app info.
     */
    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this)
            .setMessage("It Looks like you have turned off permissions required for this feature. It can be enabled under Application Settings")
            .setPositiveButton(
                "GO TO SETTINGS"
            ) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog,
                                           _ ->
                dialog.dismiss()
            }.show()
    }


    companion object {
        // A constant variable for place picker
        private const val PLACE_AUTOCOMPLETE_REQUEST_CODE = 3
    }
}
