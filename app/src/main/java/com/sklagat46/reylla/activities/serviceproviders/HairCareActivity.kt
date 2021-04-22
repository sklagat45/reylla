package com.sklagat46.reylla.activities.serviceproviders

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sklagat46.reylla.R
import com.sklagat46.reylla.activities.serviceproviders.addingNewService.AddHairServiceActivity
import com.sklagat46.reylla.model.Service
import com.sklagat46.reylla.utils.Constants
import com.sklagat46.reylla.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_add_hair_service.*
import kotlinx.android.synthetic.main.activity_hair_care.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class HairCareActivity : AppCompatActivity() {


    val mFireStore = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hair_care)

        // Click event for add hair service button.
        fab_add_hair_care.setOnClickListener {

            val addHairServiceIntent =
                Intent(this@HairCareActivity, AddHairServiceActivity::class.java)
            startActivityForResult(addHairServiceIntent, ADD_SERVICE_ACTIVITY_REQUEST_CODE)

        }
//        getServiceListFromFirestoreDB()
    }


    // Call Back method  to get the Message form other Activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // check if the request code is same as what is passed  here it is 'ADD_PLACE_ACTIVITY_REQUEST_CODE'
        if (requestCode == ADD_SERVICE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                getServiceListFromFirestoreDB()
            } else {
                Log.e("Activity", "Cancelled or Back Pressed")
            }
        }
    }

    /**
     * A function to get the list of services from firebase database.
     */
    fun getServiceListFromFirestoreDB() {
        // The collection name for PRODUCTS
        mFireStore.collection(Constants.HAIR_SERVICES)
            .document(getCurrentUserID())
            .get() // Will get the document snapshots.
            .addOnSuccessListener { document ->

                // Here we get the product details in the form of document.
                Log.e(javaClass.simpleName, document.toString())

                // Convert the snapshot to the object of Product data model class.
                val service = document.toObject(Service::class.java)!!

                productDetailsSuccess(service)
            }
            .addOnFailureListener { e ->

                // Hide the progress dialog if there is an error.
                hideProgressDialog()

                Log.e(javaClass.simpleName, "Error while getting the product details.", e)
            }
    }

    fun productDetailsSuccess(service: Service) {
        // Hide Progress dialog.
        hideProgressDialog()

        // Populate the product details in the UI.
        GlideLoader(this@HairCareActivity).loadServicePicture(
            service.mServiceImageURL,
            iv_service_image
        )

//        tv.text = product.title
//        tv_product_details_price.text = "$${product.price}"
//        tv_product_details_description.text = product.description
//        tv_product_details_stock_quantity.text = product.stock_quantity
    }

    fun hideProgressDialog() {
        TODO("Not yet implemented")
    }

    companion object {
        private const val ADD_SERVICE_ACTIVITY_REQUEST_CODE = 1
        internal const val EXTRA_PLACE_DETAILS = "extra_place_details"
    }

    /**
     * A function for getting the user id of current logged user.
     */
    private fun getCurrentUserID(): String {
        // TODO (Step 1: Return the user id if he is already logged in before or else it will be blank.)
        // START
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
        // END
    }
}