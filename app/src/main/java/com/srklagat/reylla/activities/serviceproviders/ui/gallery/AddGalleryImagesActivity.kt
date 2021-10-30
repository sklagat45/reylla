package com.srklagat.reylla.activities.serviceproviders.ui.gallery

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.srklagat.reylla.R
import com.srklagat.reylla.activities.BaseActivity
import com.srklagat.reylla.firebase.FirestoreClass
import com.srklagat.reylla.model.GalleryItem
import kotlinx.android.synthetic.main.activity_add_gallery_images.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AddGalleryImagesActivity : BaseActivity() {

    // A global variable for URI of a selected image from phone storage.
    private val currentUser = FirebaseAuth.getInstance().currentUser!!
    private var cal = Calendar.getInstance()

    private var mGalleryItem: GalleryItem? = null


    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    private var mSelectedImageFileUri: Uri? = null

    // A global variable for uploaded product image URL.
    private var imageType: String = "galleryImage"
    private var mGalleryImageURL: String = ""
    private var imageDetailsId: String = ""


    private var mFcmToken: String = ""

    /**!
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_add_gallery_images)

        setupActionBar()

        tv_select_image.setOnClickListener {
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

        btn_save.setOnClickListener {

            saveImageData()
        }


        // https://www.tutorialkart.com/kotlin-android/android-datepicker-kotlin-example/
        // create an OnDateSetListener
        dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                updateDateInView()
            }

        updateDateInView() // Here the calender instance what we have created before will give us the current date which is formatted in the format in function

        if (mGalleryItem != null) {
            supportActionBar?.title = "ADD IMAGE"
            et_date.setText(mGalleryItem!!.date)

        }

        et_date.setOnClickListener {
            DatePickerDialog(
                this@AddGalleryImagesActivity,
                dateSetListener, // This is the variable which have created globally and initialized in setupUI method.
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR), // Here the cal instance is created globally and used everywhere in the class where it is required.
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


    }

    private fun saveImageData() {
        if (validateGalleryDetails()) {
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
                        .child("gallery")
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
                                    this@AddGalleryImagesActivity,
                                    "Your image uploaded successfully :: $url",
                                    Toast.LENGTH_LONG
                                ).show()

                                // TODO Step 3: Load the uploaded image url using Glide.
                                 mGalleryImageURL = url.toString()

                                uploadGalleryDetails()
                                Glide.with(this@AddGalleryImagesActivity)
                                    .load(url)
                                    .placeholder(R.mipmap.ic_launcher) // If the image is failed to load then the placeholder will be visible.
                                    .into(iv_place_image)


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

    private fun updateDateInView() {

        val myFormat = "dd.MM.yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault()) // A date format
        et_date.setText(
            sdf.format(cal.time).toString()
        ) // A selected date using format which we have used is set to the UI.

    }


    private fun uploadGalleryDetails() {

        // Get the current user id
        var currentUserID = ""
        currentUserID = currentUser.uid

        // Here we get the text from editText and trim the space

        val mGalleryItem = GalleryItem(
            imageDetailsId,
            currentUserID,
            mGalleryImageURL.toString(),
            et_date.text.toString(),
        )
        FirestoreClass().uploadImageDetails(this@AddGalleryImagesActivity, mGalleryItem)

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
                        Glide.with(this@AddGalleryImagesActivity)
                            .load(mSelectedImageFileUri)
                            .into(iv_place_image)
                        // END
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@AddGalleryImagesActivity,
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
     * A function to validate the product details.
     */
    private fun validateGalleryDetails(): Boolean {
        return when {

            mSelectedImageFileUri == null -> {
                showErrorSnackBar("please select the image")
                false
            }

            TextUtils.isEmpty(et_date.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("make sure the date is enter")
                false
            }
            else -> {
                true
            }
        }
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_add_image)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.back_btn)
        }
        toolbar_add_image.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun imageUploadSuccess(imageUrl: String) {
        mGalleryImageURL = imageUrl
    }

    fun serviceUploadSuccess(id: String) {
        Toast.makeText(
            this@AddGalleryImagesActivity, "data uploaded successfully",
            Toast.LENGTH_SHORT
        ).show()
        imageDetailsId = id
        finish()
    }

}