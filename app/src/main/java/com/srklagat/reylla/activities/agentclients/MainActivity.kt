package com.srklagat.reylla.activities.agentclients

import android.os.Bundle
import com.srklagat.reylla.R
import com.srklagat.reylla.activities.BaseActivity

class MainActivity : BaseActivity() {

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)

        // This is used to align the xml view to this class
        setContentView(R.layout.activity_main)
    }
}
