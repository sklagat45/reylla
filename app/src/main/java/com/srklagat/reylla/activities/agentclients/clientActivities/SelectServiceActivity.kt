package com.srklagat.reylla.activities.agentclients.clientActivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import com.srklagat.reylla.R
import com.srklagat.reylla.utils.Util
import kotlinx.android.synthetic.main.activity_bridal.*

class SelectServiceActivity : AppCompatActivity() {
    val mFireStore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_service)
//        getSalonsListFromFirestoreDB()
//        Util.setRecyclerView(this, rv_bridal_care_list)

    }

//    private fun getSalonsListFromFirestoreDB() {
//        TODO("Not yet implemented")
//    }
}