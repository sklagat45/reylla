package com.sklagat46.reylla.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sklagat46.reylla.R
import kotlinx.android.synthetic.main.activity_register_customer.*

class RegisterCustomer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_customer)
    }

    private fun setUpActionBar() {

        setSupportActionBar(toolbar_sign_up_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.back_btn)
        }
        toolbar_sign_up_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}