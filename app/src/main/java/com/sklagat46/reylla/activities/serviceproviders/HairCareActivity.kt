package com.sklagat46.reylla.activities.serviceproviders

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sklagat46.reylla.R
import com.sklagat46.reylla.activities.serviceproviders.addingNewService.AddHairServiceActivity
import kotlinx.android.synthetic.main.activity_hair_care.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class HairCareActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hair_care)

        // Click event for add hair service button.
        fab_add_hair_care.setOnClickListener {
//            startActivity(Intent(this@HairCareActivity, AddHairServiceActivity::class.java))

            val addHairServiceIntent = Intent(this@HairCareActivity, AddHairServiceActivity::class.java)
            startActivityForResult(addHairServiceIntent, ADD_PLACE_ACTIVITY_REQUEST_CODE)

        }
//        getServiceListFromFirestoreDB()
    }


    // Call Back method  to get the Message form other Activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // check if the request code is same as what is passed  here it is 'ADD_PLACE_ACTIVITY_REQUEST_CODE'
        if (requestCode == ADD_PLACE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
//                getServiceListFromFirestoreDB()
            }else{
                Log.e("Activity", "Cancelled or Back Pressed")
            }
        }
    }

    /**
     * A function to get the list of services from firebase database.
     */
    private fun getServiceListFromFirestoreDB() {
        TODO("Not yet implemented")
    }

    /**
     * A function to populate the recyclerview to the UI.
     */
//    private fun setupServicesOnRecyclerView(happyPlacesList: ArrayList<HairCareServiceModel>) {
//
//        rv_hair_care_list.layoutManager = LinearLayoutManager(this)
//        rv_hair_care_list.setHasFixedSize(true)
//
//        val placesAdapter = HairCareServiceAdapter(this, happyPlacesList)
//        rv_hair_care_list.adapter = placesAdapter
//
//        placesAdapter.setOnClickListener(object :
//            HairCareServiceAdapter.OnClickListener {
//            override fun onClick(position: Int, model: HairCareServiceModel) {
//                val intent = Intent(this@HairCareActivity, AddHairServiceActivity::class.java)
//                intent.putExtra(EXTRA_PLACE_DETAILS, model) // Passing the complete serializable data class to the detail activity using intent.
//                startActivity(intent)
//            }
//        })
//
//        val editSwipeHandler = object : SwipeToEditCallback(this) {
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val adapter = rv_hair_care_list.adapter as HairCareServiceAdapter
//                adapter.notifyEditItem(
//                    this@HairCareActivity,
//                    viewHolder.adapterPosition,
//                    ADD_PLACE_ACTIVITY_REQUEST_CODE
//                )
//            }
//        }
//        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
//        editItemTouchHelper.attachToRecyclerView(rv_hair_care_list)
//
//        val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val adapter = rv_hair_care_list.adapter as HairCareServiceAdapter
//                adapter.removeAt(viewHolder.adapterPosition)
//
//                getServiceListFromFirestoreDB()
//                // Gets the latest list from the firebase database after item being delete from it.
//            }
//        }
//        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
//        deleteItemTouchHelper.attachToRecyclerView(rv_hair_care_list)
//    }

    companion object{
        private const val ADD_PLACE_ACTIVITY_REQUEST_CODE = 1
        internal const val EXTRA_PLACE_DETAILS = "extra_place_details"
    }
}