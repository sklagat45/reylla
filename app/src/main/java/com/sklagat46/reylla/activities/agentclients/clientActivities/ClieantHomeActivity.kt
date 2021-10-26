package com.sklagat46.reylla.activities.agentclients.clientActivities

import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sklagat46.reylla.R
import com.sklagat46.reylla.activities.BaseActivity
import com.sklagat46.reylla.activities.agentclients.clientsAdapters.FeaturedSalonsAdapter
import com.sklagat46.reylla.firebase.FirestoreClass
import com.sklagat46.reylla.model.Company
import com.sklagat46.reylla.utils.Constants
import com.sklagat46.reylla.utils.Util
import kotlinx.android.synthetic.main.activity_bridal.*
import kotlinx.android.synthetic.main.activity_clieant_home.*
import java.util.ArrayList

class ClieantHomeActivity : BaseActivity() {

    val mFireStore = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clieant_home)

        getSalonsListFromFirestoreDB()
        Util.setRecyclerView(this, rv_featured_saloons)



    }
    override fun onResume() {
        super.onResume()

        getSalonsListFromFirestoreDB()
    }

    private fun getSalonsListFromFirestoreDB() {
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of Firestore class.
        FirestoreClass().getSalonList(this@ClieantHomeActivity, Constants.SALONS)
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

    fun successSalonsListFromFireStore(salonList: ArrayList<Company>) {

        // Hide Progress dialog.
        hideProgressDialog()
        if (salonList.isNotEmpty()) {
            rv_featured_saloons.visibility = View.VISIBLE
            //tv_no_records_available.visibility = View.GONE
            val adapterSalons =
                FeaturedSalonsAdapter(this, salonList)
            rv_featured_saloons.adapter = adapterSalons
        } else {
            rv_featured_saloons.visibility = View.GONE
            //tv_no_records_available.visibility = View.VISIBLE
        }

    }

}