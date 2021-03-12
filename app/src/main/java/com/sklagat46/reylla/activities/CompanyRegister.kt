package com.sklagat46.reylla.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sklagat46.reylla.R
import com.sklagat46.reylla.firebase.FirestoreClass
import com.sklagat46.reylla.model.Company
import kotlinx.android.synthetic.main.activity_company_register.*
import kotlinx.android.synthetic.main.activity_individual_register.*
import kotlinx.android.synthetic.main.activity_register_customer.*

class CompanyRegister : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_register)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setupActionBar()

        // Click event for sign-up button.
        btn_comp_register.setOnClickListener {
            registerCompany()
        }
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_company_sign_up_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.back_btn)
        }
        toolbar_company_sign_up_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun registerCompany() {
        // Here we get the text from editText and trim the space
        val companyImage: String = iv_company_logo_image.toString()
        val companyName: String = et_company_name.text.toString().trim { it <= ' ' }
        val registrationNumber: String = et_registration_number.text.toString().trim { it <= ' ' }
        val companyAddress: String = et_company_address.text.toString().trim { it <= ' ' }
        val companyPhonenumber: String = et_company_phonenumber.text.toString().trim { it <= ' ' }
        val companyEmail: String = et_company_email.text.toString().trim { it <= ' ' }
        val companyPassword: String = et_company_password.text.toString().trim { it <= ' ' }

        if (validateForm(companyName, registrationNumber, companyAddress, companyPhonenumber, companyEmail, companyPassword)) {
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(companyEmail, companyPassword)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        // If the registration is successfully done
                        if (task.isSuccessful) {

                            // Firebase registered customer
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            // Registered Email
                            val registeredEmail = firebaseUser.email!!

                            val company = Company(
                                firebaseUser.uid,companyImage,companyName, registrationNumber,
                                companyAddress, companyPhonenumber,registeredEmail,companyPassword
                            )

                            // call the registerCustomer function of FirestoreClass to make an entry in the database.
                            FirestoreClass().registerCompany(this@CompanyRegister, company)
                        } else {
                            Toast.makeText(
                                this@CompanyRegister,
                                task.exception!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
        }
    }

    private fun validateForm(companyName: String, registrationNumber: String,
                                     companyAddress: String, companyPhonenumber: String, companyEmail: String,
                                     companyPassword: String): Boolean {
                return when {
                    TextUtils.isEmpty(companyName) or TextUtils.isEmpty(registrationNumber)  or
                            TextUtils.isEmpty(companyAddress)  or TextUtils.isEmpty(companyPhonenumber)  or
                            TextUtils.isEmpty(companyEmail) or
                            TextUtils.isEmpty(companyPassword) -> {
                        showErrorSnackBar("Please enter all details.")
                        false
                    }
                    TextUtils.isEmpty(companyEmail) -> {
                        showErrorSnackBar("Please enter email.")
                        false
                    }
                    TextUtils.isEmpty(companyPassword) -> {
                        showErrorSnackBar("Please enter password.")
                        false
                    }
                    else -> {
                        true
                    }
                }
            }


    /**
     * A function to be called the customer is registered successfully and entry is made in the firestore database.
     */
    fun companyRegisteredSuccess() {

        Toast.makeText(
            this@CompanyRegister,
            "You have successfully registered.",
            Toast.LENGTH_SHORT
        ).show()

        // Hide the progress dialog
        hideProgressDialog()



        // Launch the register customer screen.
        startActivity(Intent(this@CompanyRegister, MainActivity::class.java))

    //        /**
    //         * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
    //         * and send him to Intro Screen for Sign-In
    //         */
    //        FirebaseAuth.getInstance().signOut()
        // Finish the Sign-Up Screen
        finish()
    }
    }
