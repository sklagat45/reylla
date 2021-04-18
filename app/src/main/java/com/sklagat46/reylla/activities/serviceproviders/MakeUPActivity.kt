package com.sklagat46.reylla.activities.serviceproviders

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sklagat46.reylla.R
import com.sklagat46.reylla.activities.serviceproviders.addingNewService.AddMakeUpServiceActivity
import kotlinx.android.synthetic.main.activity_make_u_p.*

class MakeUPActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_u_p)


        fab_add_Makeup.setOnClickListener {

            val addMakeupServiceIntent =
                Intent(this@MakeUPActivity, AddMakeUpServiceActivity::class.java)
            startActivityForResult(
                addMakeupServiceIntent,
                MakeUPActivity.ADD_SERVICE_ACTIVITY_REQUEST_CODE
            )

        }
    }

    // Call Back method  to get the Message form other Activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // check if the request code is same as what is passed  here it is 'ADD_PLACE_ACTIVITY_REQUEST_CODE'
        if (requestCode == MakeUPActivity.ADD_SERVICE_ACTIVITY_REQUEST_CODE) {
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