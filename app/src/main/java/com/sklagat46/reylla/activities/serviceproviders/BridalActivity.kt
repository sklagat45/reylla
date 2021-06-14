package com.sklagat46.reylla.activities.serviceproviders

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sklagat46.reylla.R
import com.sklagat46.reylla.activities.BaseActivity
import com.sklagat46.reylla.activities.serviceproviders.addingNewService.AddBridalServiceActivity
import com.sklagat46.reylla.adapter.BridalServiceAdapter
import com.sklagat46.reylla.firebase.FirestoreClass
import com.sklagat46.reylla.model.BridalService
import com.sklagat46.reylla.utils.Constants
import kotlinx.android.synthetic.main.activity_bridal.*
import kotlinx.android.synthetic.main.activity_tatoo_and_color.*
import java.util.*

class BridalActivity : BaseActivity() {

    val mFireStore = FirebaseFirestore.getInstance()
    private lateinit var mRootView: View
    private val currentUser = FirebaseAuth.getInstance().currentUser!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bridal)
        getBridalServiceListFromFirestoreDB()

        fab_add_bridal_care.setOnClickListener {

            val addBridalServiceIntent =
                Intent(this@BridalActivity, AddBridalServiceActivity::class.java)
            startActivityForResult(
                addBridalServiceIntent, ADD_SERVICE_ACTIVITY_REQUEST_CODE
            )

        }

    }

    override fun onResume() {
        super.onResume()

        getBridalServiceListFromFirestoreDB()
    }

    private fun getBridalServiceListFromFirestoreDB() {

        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of Firestore class.
        FirestoreClass().getServiceList(this@BridalActivity, Constants.BRIDAL_SERVICES)
        hideProgressDialog()
    }

    /**
     * A function for getting the user id of current logged user.
     */
    override fun getCurrentUserID(): String {
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

    fun successBridalServiceListFromFireStore(bridalServiceList: ArrayList<BridalService>) {

        // Hide Progress dialog.
        hideProgressDialog()
        if (bridalServiceList.isNotEmpty()) {
            rv_bridal_care_list.visibility = View.VISIBLE
            //tv_no_records_available.visibility = View.GONE
            val adapterService =
                BridalServiceAdapter(this, bridalServiceList)
            rv_bridal_care_list.adapter = adapterService
        } else {
            rv_bridal_care_list.visibility = View.GONE
            //tv_no_records_available.visibility = View.VISIBLE
        }
    }

    companion object {
        private const val ADD_SERVICE_ACTIVITY_REQUEST_CODE = 1
        internal const val EXTRA_PLACE_DETAILS = "extra_place_details"
    }

    /**
     * A function to show the alert dialog for the confirmation of delete product from cloud firestore.
     */
    private fun showAlertDialogToDeleteService(serviceID: String) {

        val builder = AlertDialog.Builder(applicationContext)
        //set title for alert dialog
        builder.setTitle("Delete Item")
        //set message for alert dialog
        builder.setMessage("Are you sure you want to delete")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->

            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            // Call the function of Firestore class.
            FirestoreClass().deleteService(this@BridalActivity, serviceID)

            dialogInterface.dismiss()
        }

        //performing negative action
        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, _ ->

            dialogInterface.dismiss()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

}