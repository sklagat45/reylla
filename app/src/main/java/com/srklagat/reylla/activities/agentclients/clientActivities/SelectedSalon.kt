package com.srklagat.reylla.activities.agentclients.clientActivities

import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.srklagat.reylla.R
import com.srklagat.reylla.activities.BaseActivity
import com.srklagat.reylla.activities.agentclients.clientsAdapters.SalonCategoryAdapter
import com.srklagat.reylla.activities.agentclients.clientsAdapters.ServicesAdapter
import com.srklagat.reylla.activities.serviceproviders.*
import com.srklagat.reylla.firebase.FirestoreClass
import com.srklagat.reylla.listener.CustomItemClickListener
import com.srklagat.reylla.model.CategoriesViews
import com.srklagat.reylla.model.SalonService
import com.srklagat.reylla.utils.Constants
import com.srklagat.reylla.utils.ItemOffsetDecoration
import com.srklagat.reylla.utils.Util
import kotlinx.android.synthetic.main.activity_selected_salon.*

class SelectedSalon : BaseActivity() {

    private val mFireStore = FirebaseFirestore.getInstance()
    private lateinit var mRootView: View
    private val currentUser = FirebaseAuth.getInstance().currentUser!!

    private lateinit var mSalonID: String

    private var salonCategoryAdapter: SalonCategoryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_salon)


        val salonID = intent.getStringExtra("salonID")
        if (salonID != null) {
            mSalonID = salonID
        }
        cartImageViewBtn.setOnTouchListener(View.OnTouchListener { v, event -> // TODO Auto-generated method stub
            val vb = getSystemService(VIBRATOR_SERVICE) as Vibrator
            vb.vibrate(100)
            false
        })

        cartImageViewBtn.setOnClickListener {
            startActivity(Intent(this@SelectedSalon, CartListActivity::class.java))
        }

        Log.e("service_5","" + salonID)

        getFeatureNailsServiceListFromFirestoreDB()
        Util.setRecyclerView(this, rv_featured_salons_service)
        // Click event for add hair service button.

        setRvAdapter()
        setUpAdapter()

    }

    private fun getFeatureNailsServiceListFromFirestoreDB() {
        showProgressDialog(resources.getString(R.string.please_wait))
        // Call the function of Firestore class.
        //TO DO
        FirestoreClass().getSalonServiceList(this@SelectedSalon, Constants.NAIL_SERVICES,mSalonID)
        hideProgressDialog()
    }

    private fun getFeatureServiceListFromFirestoreDB() {
        showProgressDialog(resources.getString(R.string.please_wait))
        // Call the function of Firestore class.
        //TO DO
        FirestoreClass().getHairSalonServiceList(this@SelectedSalon, Constants.HAIR_SERVICES,mSalonID)
        hideProgressDialog()
    }
    private fun getFeatureMassageServiceListFromFirestoreDB() {
        showProgressDialog(resources.getString(R.string.please_wait))
        // Call the function of Firestore class.
        //TO DO
        FirestoreClass().getSalonServiceList(this@SelectedSalon, Constants.MASSAGE_SERVICES,mSalonID)
        hideProgressDialog()
    }
    private fun getFeatureMakeUpServiceListFromFirestoreDB() {
        showProgressDialog(resources.getString(R.string.please_wait))
        // Call the function of Firestore class.
        //TO DO
        FirestoreClass().getSalonServiceList(this@SelectedSalon, Constants.MAKEUP_SERVICES,mSalonID)
        hideProgressDialog()
    }
    private fun getFeatureTattooServiceListFromFirestoreDB() {
        showProgressDialog(resources.getString(R.string.please_wait))
        // Call the function of Firestore class.
        //TO DO
        FirestoreClass().getSalonServiceList(this@SelectedSalon, Constants.TATCOLOUR_SERVICES,mSalonID)
        hideProgressDialog()
    }
    private fun getFeatureBridalServiceListFromFirestoreDB() {
        showProgressDialog(resources.getString(R.string.please_wait))
        // Call the function of Firestore class.
        //TO DO
        FirestoreClass().getSalonServiceList(this@SelectedSalon, Constants.BRIDAL_SERVICES,mSalonID)
        hideProgressDialog()
    }

    fun successServiceListFromFireStore(serviceList: ArrayList<SalonService>) {

        // Hide Progress dialog.
        hideProgressDialog()

        if (serviceList.isNotEmpty()) {
            rv_featured_salons_service.visibility = View.VISIBLE
            //tv_no_records_available.visibility = View.GONE
            val adapterService =
                ServicesAdapter(this, serviceList)
            rv_featured_salons_service.adapter = adapterService
        } else {
            rv_featured_salons_service.visibility = View.GONE
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


    private fun setRvAdapter() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_services.layoutManager = layoutManager
        val itemDecoration = ItemOffsetDecoration(this, R.dimen.item_offset)
        rv_services.addItemDecoration(itemDecoration)

    }


    private fun setUpAdapter() {
        salonCategoryAdapter = SalonCategoryAdapter(this, emptyList(),
            object : CustomItemClickListener {
                override fun onItemClick(view: View?, position: Int) {

                    val category = salonCategoryAdapter?.getItem(position) as? CategoriesViews
                    when (category?.categoryTitle) {

                        "Nails" -> {
                            getFeatureNailsServiceListFromFirestoreDB()

                        }
                        "Hair Care" -> {
                            getFeatureServiceListFromFirestoreDB()


                        }
                        "Massage" -> {
                            getFeatureMassageServiceListFromFirestoreDB()

                        }
                        "Makeups" -> {
                            getFeatureMakeUpServiceListFromFirestoreDB()

                        }
                        "Tattoo" -> {
                            getFeatureTattooServiceListFromFirestoreDB()

                        }
                        "Bridal" -> {
                            getFeatureBridalServiceListFromFirestoreDB()

                        }
                    }


                }
            })

        rv_services.adapter = salonCategoryAdapter

        salonCategoryAdapter?.setCategoryViews(setCategoriesList())
        salonCategoryAdapter?.notifyDataSetChanged()

    }


    private fun setCategoriesList(): List<CategoriesViews> {
        val arrayList = ArrayList<CategoriesViews>()
        arrayList.add(
            CategoriesViews(
                "Nails",
                R.drawable.nail_icon_1,
                1
            )
        )

        arrayList.add(
            CategoriesViews(
                "Hair Care",
                R.drawable.hair_icon,
                2
            )
        )


        arrayList.add(
            CategoriesViews(
                "Massage",
                R.drawable.massage_icon_1,
                3
            )
        )
        arrayList.add(
            CategoriesViews(
                "Makeups",
                R.drawable.facecare_icon_1,
                4
            )
        )

        arrayList.add(
            CategoriesViews(
                "Tattoo",
                R.drawable.tat_icon_1,
                5
            )
        )


        arrayList.add(
            CategoriesViews(
                "Bridal",
                R.drawable.bride_care_icon_1,
                6
            )
        )

        return arrayList
    }

}