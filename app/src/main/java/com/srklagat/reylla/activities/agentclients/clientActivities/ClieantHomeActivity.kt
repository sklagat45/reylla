package com.srklagat.reylla.activities.agentclients.clientActivities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.srklagat.reylla.R
import com.srklagat.reylla.activities.BaseActivity
import com.srklagat.reylla.activities.agentclients.clientsAdapters.FeaturedSalonsAdapter
import com.srklagat.reylla.activities.serviceproviders.ServiceProvidersReg
import com.srklagat.reylla.firebase.FirestoreClass
import com.srklagat.reylla.model.Company
import com.srklagat.reylla.utils.Constants
import com.srklagat.reylla.utils.Util
import kotlinx.android.synthetic.main.activity_bridal.*
import kotlinx.android.synthetic.main.activity_clieant_home.*
import kotlinx.android.synthetic.main.activity_intro.*
import java.util.ArrayList

class ClieantHomeActivity : BaseActivity() {

    val mFireStore = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clieant_home)

        cartImageViewBtn.setOnTouchListener(View.OnTouchListener { v, event -> // TODO Auto-generated method stub
            val vb = getSystemService(VIBRATOR_SERVICE) as Vibrator
            vb.vibrate(100)
            false
        })

        cartImageViewBtn.setOnClickListener {
            startActivity(Intent(this@ClieantHomeActivity, CartListActivity::class.java))
        }

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
        FirestoreClass().getSalonList(this@ClieantHomeActivity, Constants.COMPANIES)
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