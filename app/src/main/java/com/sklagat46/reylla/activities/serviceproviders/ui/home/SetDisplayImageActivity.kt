package com.sklagat46.reylla.activities.serviceproviders.ui.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sklagat46.reylla.R
import kotlinx.android.synthetic.main.activity_add_gallery_images.*
import java.io.IOException

class SetDisplayImageActivity : AppCompatActivity() {

    // A global variable for URI of a selected image from phone storage.
    private var mSelectedImageFileUri: Uri? = null
    private val currentUser = FirebaseAuth.getInstance().currentUser!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_display_image)
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
//        val btnSaveToGallery: Button? = findViewById<Button>(R.id.btn_save);


        btn_save.setOnClickListener {
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
                    FirebaseStorage.getInstance().reference.child("profileImages")
                        .child(currentUserID)

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
                                    this@SetDisplayImageActivity,
                                    "Your image uploaded successfully :: $url",
                                    Toast.LENGTH_LONG
                                ).show()

                                // TODO Step 3: Load the uploaded image url using Glide.
                                // START
                                Glide.with(this@SetDisplayImageActivity)
                                    .load(url)
                                    .placeholder(R.mipmap.ic_launcher) // If the image is failed to load then the placeholder will be visible.
                                    .into(iv_place_image)

                                intent.putExtra("profileImageUrl", url)

                                val bundle = Bundle()
                                bundle.putString("url", "From SetDisplayImageActivity")
// set Fragmentclass Arguments
// set Fragmentclass Arguments
                                val fragobj = HomeFragment()
                                fragobj.arguments = bundle
                                // END
                            }
                            .addOnFailureListener { exception ->
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
                        Glide.with(this@SetDisplayImageActivity)
                            .load(mSelectedImageFileUri)
                            .into(iv_place_image)
                        // END
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@SetDisplayImageActivity,
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
}