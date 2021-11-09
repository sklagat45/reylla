package com.srklagat.reylla.activities.agentclients.clientActivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.srklagat.reylla.R
import com.srklagat.reylla.activities.BaseActivity
import com.srklagat.reylla.model.Cart
import java.util.ArrayList

class CheckOutActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Cart>) {
        TODO("Not yet implemented")
    }
}