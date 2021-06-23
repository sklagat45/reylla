package com.sklagat46.reylla.activities.serviceproviders

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sklagat46.reylla.R
import com.sklagat46.reylla.activities.BaseActivity
import com.sklagat46.reylla.activities.serviceproviders.addingNewService.AddMakeUpServiceActivity
import com.sklagat46.reylla.adapter.MakeupServiceAdapter
import com.sklagat46.reylla.firebase.FirestoreClass
import com.sklagat46.reylla.model.MakeupService
import com.sklagat46.reylla.utils.Constants
import com.sklagat46.reylla.utils.Util
import kotlinx.android.synthetic.main.activity_make_u_p.*

class MakeUPActivity : BaseActivity() {
    private val mFireStore = FirebaseFirestore.getInstance()
    private lateinit var mRootView: View

    private val currentUser = FirebaseAuth.getInstance().currentUser!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_u_p)

        Util.setRecyclerView(this, rv_makeup_list)
        getMakeupServiceListFromFirestoreDB()

        fab_add_Makeup.setOnClickListener {
            val addMakeupServiceIntent =
                Intent(this@MakeUPActivity, AddMakeUpServiceActivity::class.java)
            startActivityForResult(
                addMakeupServiceIntent,
                ADD_SERVICE_ACTIVITY_REQUEST_CODE
            )
        }
    }

    companion object {
        private const val ADD_SERVICE_ACTIVITY_REQUEST_CODE = 1
        internal const val EXTRA_PLACE_DETAILS = "extra_place_details"
    }


    private fun getMakeupServiceListFromFirestoreDB() {

        showProgressDialog(resources.getString(R.string.please_wait))
        // Call the function of Firestore class.
        FirestoreClass().getServiceList(this@MakeUPActivity, Constants.MAKEUP_SERVICES)
        hideProgressDialog()

    }


    /**
     * A function to get the successful product list from cloud firestore.
     *
     * @param productsList Will receive the product list from cloud firestore.
     */
//    fun successServiceListFromFireStore(makeupServiceList: ArrayList<MakeupService>) {
//
//        // Hide Progress dialog.
//        hideProgressDialog()
//
//        if (makeupServiceList.size > 0) {
//            rv_makeup_list.visibility = View.VISIBLE
//            tv_no_records_available.visibility = View.GONE
//
//            rv_makeup_list.layoutManager = LinearLayoutManager(this)
//            rv_makeup_list.setHasFixedSize(true)
//
//            val adapterService =
//                MakeupServiceAdapter(applicationContext, makeupServiceList, this@MakeUPActivity)
//            rv_makeup_list.adapter = adapterService
//        } else {
//            rv_makeup_list.visibility = View.GONE
//            tv_no_records_available.visibility = View.VISIBLE
//        }
//    }

    fun successMakeupServiceListFromFireStore(makeupServiceList: ArrayList<MakeupService>) {

        // Hide Progress dialog.
        hideProgressDialog()

        if (makeupServiceList.isNotEmpty()) {
            rv_makeup_list.visibility = View.VISIBLE
            //tv_no_records_available.visibility = View.GONE
            val adapterService =
                MakeupServiceAdapter(this, makeupServiceList)
            rv_makeup_list.adapter = adapterService
        } else {
            rv_makeup_list.visibility = View.GONE
            //tv_no_records_available.visibility = View.VISIBLE
        }
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

    fun deleteService(serviceID: String) {
        showAlertDialogToDeleteProduct(serviceID)

    }

    /**
     * A function to show the alert dialog for the confirmation of delete product from cloud firestore.
     */
    private fun showAlertDialogToDeleteProduct(serviceID: String) {

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
            FirestoreClass().deleteService(this@MakeUPActivity, serviceID)

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