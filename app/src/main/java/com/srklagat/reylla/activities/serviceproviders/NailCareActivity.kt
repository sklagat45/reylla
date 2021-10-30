package com.srklagat.reylla.activities.serviceproviders

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.srklagat.reylla.R
import com.srklagat.reylla.activities.BaseActivity
import com.srklagat.reylla.activities.serviceproviders.addingNewService.AddNailCareServiceActivity
import com.srklagat.reylla.adapter.NailServiceAdapter
import com.srklagat.reylla.firebase.FirestoreClass
import com.srklagat.reylla.model.NailService
import com.srklagat.reylla.utils.Constants
import com.srklagat.reylla.utils.Util
import kotlinx.android.synthetic.main.activity_nail_care.*

class NailCareActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nail_care)

        getNailCareServiceListFromFirestoreDB()
        Util.setRecyclerView(this, rv_nail_care_list)

        fab_add_nail_care.setOnClickListener {

            val addNailServiceIntent =
                Intent(this@NailCareActivity, AddNailCareServiceActivity::class.java)
            startActivityForResult(
                addNailServiceIntent,
                NailCareActivity.ADD_SERVICE_ACTIVITY_REQUEST_CODE
            )

        }
    }

    private fun getNailCareServiceListFromFirestoreDB() {
        showProgressDialog(resources.getString(R.string.please_wait))
        // Call the function of Firestore class.
        FirestoreClass().getServiceList(this@NailCareActivity, Constants.NAIL_SERVICES)
        hideProgressDialog()
    }


    fun successNailCareServiceListFromFireStore(nailServiceList: ArrayList<NailService>) {

        // Hide Progress dialog.
//        hideProgressDialog()

        if (nailServiceList.isNotEmpty()) {
            rv_nail_care_list.visibility = View.VISIBLE
            //tv_no_records_available.visibility = View.GONE
            val adapterService =
                NailServiceAdapter(this, nailServiceList)
            rv_nail_care_list.adapter = adapterService
        } else {
            rv_nail_care_list.visibility = View.GONE
            //tv_no_records_available.visibility = View.VISIBLE
        }
    }

    // Call Back method  to get the Message form other Activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // check if the request code is same as what is passed  here it is 'ADD_PLACE_ACTIVITY_REQUEST_CODE'
        if (requestCode == NailCareActivity.ADD_SERVICE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
//                getServiceListFromFirestoreDB()
            } else {
                Log.e("Activity", "Cancelled or Back Pressed")
            }
        }
    }

    companion object {
        private const val ADD_SERVICE_ACTIVITY_REQUEST_CODE = 1
        internal const val EXTRA_PLACE_DETAILS = "extra_place_details"
    }
}