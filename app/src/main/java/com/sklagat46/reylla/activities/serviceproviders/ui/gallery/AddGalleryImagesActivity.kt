package com.sklagat46.reylla.activities.serviceproviders.ui.gallery

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sklagat46.reylla.R
import com.sklagat46.reylla.firebase.FirestoreClass
import com.sklagat46.reylla.model.GalleryItem
import com.sklagat46.reylla.utils.Constants
import kotlinx.android.synthetic.main.activity_add_gallery_images.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.IOException
import java.util.*


class AddGalleryImagesActivity : AppCompatActivity() {


    // Add a global variable for URI of a selected image from phone storage.
    private var mSelectedImageFileUri: Uri? = null

    // A global variable for user details.
    private var mGalleryItem: GalleryItem? = null

    // A global variable for a user profile image URL
    private var mGalleryImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_gallery_images)

        setupActionBar()

//        FirestoreClass().loadGalleryData(this@AddGalleryImagesActivity)
        FirestoreClass().getCurrentUserID()




        tv_add_image.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Constants.showImageChooser(this@AddGalleryImagesActivity)
            } else {
                /*Requests permissions to be granted to this application. These permissions
                 must be requested in your manifest, they should not be granted to your app,
                 and they should have protection level*/
                ActivityCompat.requestPermissions(
                    this,
                    (arrayOf(READ_EXTERNAL_STORAGE)),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }

        btn_save.setOnClickListener {

            // Here if the image is not selected then update the other details of user.
            if (mSelectedImageFileUri != null) {

                uploadUserImage()
            } else {

//                showProgressDialog(resources.getString(R.string.please_wait))

                // Call a function to update user details in the database.
//                updateUserProfileData()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && requestCode == Constants.PICK_IMAGE_REQUEST_CODE
            && data!!.data != null
        ) {
            // The uri of selection image from phone storage.
            mSelectedImageFileUri = data.data!!

            try {
                // Load the user image in the ImageView.
                Glide
                    .with(this@AddGalleryImagesActivity)
                    .load(Uri.parse(mSelectedImageFileUri.toString())) // URI of the image
                    .centerCrop() // Scale type of the image.
                    .placeholder(R.drawable.ic_baseline_image_24) // A default place holder
                    .into(iv_place_image) // the view in which the image will be loaded.
            } catch (e: IOException) {
                e.printStackTrace()
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
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this@AddGalleryImagesActivity)
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

    /**
     * A function to setup action bar
     */
    private fun setupActionBar() {

        setSupportActionBar(toolbar_add_image)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.back_btn)
            actionBar.title = resources.getString(R.string.add_images)
        }

        toolbar_add_image.setNavigationOnClickListener { onBackPressed() }
    }


    /**
     * A function to upload the selected user image to firebase cloud storage.
     */
    private fun uploadUserImage() {

//        showProgressDialog(resources.getString(R.string.please_wait))

        Toast.makeText(this@AddGalleryImagesActivity, "please wait", Toast.LENGTH_LONG).show()

        if (mSelectedImageFileUri != null) {

            //getting the storage reference
            val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                "GALLEY_IMAGE" + System.currentTimeMillis() + "."
                        + Constants.getFileExtension(
                    this@AddGalleryImagesActivity,
                    mSelectedImageFileUri
                )
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
                        .addOnSuccessListener { uri ->
                            Log.e("Downloadable Image URL", uri.toString())

                            // assign the image url to the variable.
                            mGalleryImageURL = uri.toString()

                            // Call a function to update user details in the database.
//                            updateUserProfileData()
                        }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        this@AddGalleryImagesActivity,
                        exception.message,
                        Toast.LENGTH_LONG
                    ).show()

//                    hideProgressDialog()
                }
        }
    }


    /**
     * A function to notify the user gallery is updated successfully.
     */
    fun galleryUpdateSuccess() {

//        hideProgressDialog()

        Toast.makeText(
            this@AddGalleryImagesActivity,
            "Profile updated successfully!",
            Toast.LENGTH_SHORT
        ).show()

        setResult(Activity.RESULT_OK)
        finish()
    }


}