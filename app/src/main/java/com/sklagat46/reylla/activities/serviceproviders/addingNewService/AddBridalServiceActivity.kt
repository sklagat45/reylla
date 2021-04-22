package com.sklagat46.reylla.activities.serviceproviders.addingNewService

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sklagat46.reylla.R
import com.sklagat46.reylla.activities.BaseActivity
import com.sklagat46.reylla.firebase.FirestoreClass
import com.sklagat46.reylla.model.BridalService
import kotlinx.android.synthetic.main.activity_add_bridal_service.*
import java.io.IOException


class AddBridalServiceActivity : BaseActivity() {

    // A global variable for URI of a selected image from phone storage.
    private var mSelectedImageFileUri: Uri? = null
    private val currentUser = FirebaseAuth.getInstance().currentUser!!


    // Access a Cloud Firestore instance.
    private val mFireStore = FirebaseFirestore.getInstance()

    // A global variable for uploaded product image URL.
    lateinit var mServiceImageURL: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_bridal_service)

        setupActionBar()

        // Assign the click event to iv_add_update_product image.
        tv_add_bridal_service_image.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED

            ) {
                // An intent for launching the image selection of phone storage.
                val galleryIntent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                // Launches the image selection of phone storage using the constant code.
                startActivityForResult(galleryIntent, 222)
            } else {
                /*Requests permissions to be granted to this application. These permissions
                     must be requested in your manifest, they should not be granted to your app,
                     and they should have protection level*/
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    121
                )
            }
            // END
        }


        // Assign the click event to submit button.
        btn_save_bridal_service.setOnClickListener {
            if (validateServiceDetails()) {

                if (mSelectedImageFileUri != null) {
                    // Get the current user id
                    var currentUserID = ""
                    currentUserID = currentUser.uid

//                getCurrentProviderID()
                    // Get the image extension.
                    /*MimeTypeMap: Two-way map that maps MIME-types to file extensions and vice versa.
        getSingleton(): Get the singleton instance of MimeTypeMap.
        getExtensionFromMimeType: Return the registered extension for the given MIME type.
        contentResolver.getType: Return the MIME type of the given content URL.*/
                    val imageExtension = MimeTypeMap.getSingleton()
                        .getExtensionFromMimeType(contentResolver.getType(mSelectedImageFileUri!!))

                    //getting the storage reference
                    val sRef: StorageReference =
                        FirebaseStorage.getInstance().reference.child(currentUserID)
                            .child("bridalStyles")
                            .child(
                                "Image" + System.currentTimeMillis() + "."
                                        + imageExtension
                            )

                    //adding the file to reference
                    sRef.putFile(mSelectedImageFileUri!!)
                        .addOnSuccessListener { taskSnapshot ->
                            // The image upload is success
                            Log.e(
                                "Firebase Image URL",
                                taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                            )
                            // Get the downloadable url from the task snapshot
                            taskSnapshot.metadata!!.reference!!.downloadUrl
                                .addOnSuccessListener { url ->
                                    Log.e("Downloadable Image URL", url.toString())

                                    Toast.makeText(
                                        this@AddBridalServiceActivity,
                                        "Your image uploaded successfully",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    mServiceImageURL = url.toString()
                                    uploadProductDetails()
                                    // TODO Step 3: Load the uploaded image url using Glide.
                                    // START
                                    Glide.with(this@AddBridalServiceActivity)
                                        .load(url)
                                        .placeholder(R.drawable.bridal_img) // If the image is failed to load then the placeholder will be visible.
                                        .into(iv_service_image)
                                    // END
                                }
                                .addOnFailureListener { exception ->
                                    Toast.makeText(
                                        this,
                                        exception.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                    Log.e(javaClass.simpleName, exception.message, exception)
                                }
                        }
                } else {

                    Toast.makeText(
                        this,
                        "Please select the image to upload.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
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
        if (requestCode == 121) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // An intent for launching the image selection of phone storage.
                val galleryIntent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                // Launches the image selection of phone storage using the constant code.
                startActivityForResult(galleryIntent, 222)
                // END
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    "Oops, you just denied the permission for storage. You can also allow it from settings.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 222) {
                if (data != null) {
                    try {
                        // The uri of selected image from phone storage.
                        mSelectedImageFileUri = data.data!!

                        // TODO Step 2: Load the image using Glide.
                        // START
                        // The URI is loaded using Glide Image Loading Library.
                        // START
//                        iv_place_image.setImageURI(mSelectedImageFileUri)
                        Glide.with(this@AddBridalServiceActivity)
                            .load(mSelectedImageFileUri)
                            .into(iv_service_image)
                        // END
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@AddBridalServiceActivity,
                            "Image selection Failed!",
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
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(toolbar_add_bridal_care_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.back_btn)
        }

        toolbar_add_bridal_care_activity.setNavigationOnClickListener { onBackPressed() }
    }


    /**
     * A function to validate the product details.
     */
    private fun validateServiceDetails(): Boolean {
        return when {

            mSelectedImageFileUri == null -> {
                showErrorSnackBar("please select the style image")
                false
            }

            TextUtils.isEmpty(et_bridal_style_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Enter the style name")
                false
            }

            TextUtils.isEmpty(et_bridal_service_duration.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Enter styling Duration")
                false
            }

            TextUtils.isEmpty(et_bridal_style_cost.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Enter the Styling cost")
                false
            }
            else -> {
                true
            }
        }
    }


    private fun uploadProductDetails() {

        // Get the current user id
        var providerID = ""
        providerID = currentUser.uid

        // Here we get the text from editText and trim the space
        val bridalService = BridalService(
            providerID,
            et_bridal_style_name.text.toString().trim { it <= ' ' },
            et_bridal_service_duration.text.toString().trim { it <= ' ' },
            et_bridal_style_cost.text.toString().trim { it <= ' ' },
            mServiceImageURL
        )

        FirestoreClass().uploadBridalDetails(this@AddBridalServiceActivity, bridalService)
    }

    fun serviceUploadSuccess() {
// Hide the progress dialog
//        hideProgressDialog()

        Toast.makeText(
            this@AddBridalServiceActivity, "data uploaded successfully",
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }
}