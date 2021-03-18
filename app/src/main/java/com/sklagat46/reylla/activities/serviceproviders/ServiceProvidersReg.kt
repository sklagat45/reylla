package com.sklagat46.reylla.activities.serviceproviders

import android.content.Intent
import android.os.Bundle
import com.sklagat46.reylla.R
import com.sklagat46.reylla.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_intro.*
import kotlinx.android.synthetic.main.activity_service_providers_reg.*

class ServiceProvidersReg : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_providers_reg)

        btn_company_register.setOnClickListener {

            // Launch the register customer screen.
            startActivity(Intent(this@ServiceProvidersReg, CompanyRegister::class.java))
        }

        btn_individual_register.setOnClickListener {

            // Launch the lounge forgot password screen.
            startActivity(Intent(this@ServiceProvidersReg, IndividualRegister::class.java))
        }

    }
}