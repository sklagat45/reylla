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
import com.sklagat46.reylla.model.IndividualProviders
import kotlinx.android.synthetic.main.activity_individual_register.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class IndividualRegister : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_individual_register)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setupActionBar()

        // Click event for sign-up button.
        btn_indi_register.setOnClickListener {
            registerUser()
        }
    }

    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(toolbar_individual_sign_up_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.back_btn)
        }

        toolbar_individual_sign_up_activity.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * A function to register a user to our app using the Firebase.
     * For more details visit: https://firebase.google.com/docs/auth/android/custom-auth
     */
    private fun registerUser() {

        // Here we get the text from editText and trim the space
        val individualProfileImage: String = iv_indiv_profile_image.toString()
        val individualFirstName: String = et_indiv_first_name.text.toString().trim { it <= ' ' }
        val individualLastName: String = et_indiv_last_name.text.toString().trim { it <= ' ' }
        val individualDOB: String = et_indiv_birth_date.text.toString().trim { it <= ' ' }
        val individualAddress: String = et_indiv_address.text.toString().trim { it <= ' ' }
        val individualPhoneNum: String = et_indiv_phonenumber.text.toString().trim { it <= ' ' }
        val individualEmail: String = et_indiv_email.text.toString().trim { it <= ' ' }
        val individualGender: String = et_indiv_gender.text.toString().trim { it <= ' ' }
        val individualPassword: String = et_indiv_password.text.toString().trim { it <= ' ' }

        if (validateForm(individualFirstName, individualLastName, individualDOB, individualAddress, individualPhoneNum, individualEmail, individualGender, individualPassword)) {
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(individualEmail, individualPassword)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        // If the registration is successfully done
                        if (task.isSuccessful) {

                            // Firebase registered customer
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            // Registered Email
                            val registeredEmail = firebaseUser.email!!

                            val individualProvider = IndividualProviders(
                                firebaseUser.uid,individualProfileImage,individualFirstName,
                                individualLastName,individualDOB,individualAddress,
                                individualPhoneNum,registeredEmail,individualGender,individualPassword
                            )

                            // call the registerCustomer function of FirestoreClass to make an entry in the database.
                            FirestoreClass().registerIndividualProvider(this@IndividualRegister, individualProvider)
                        } else {
                            Toast.makeText(
                                this@IndividualRegister,
                                task.exception!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
        }
    }

    /**
     * A function to validate the entries of a new user.
     */
    private fun validateForm(IndividualFirstName: String, IndividualLastName: String,
                             IndividualDOB: String, IndividualEddress: String, IndividualPhoneNum: String,
                             IndividualEmail: String, IndividualGender: String, IndividaulPassword: String): Boolean {
        return when {
            TextUtils.isEmpty(IndividualFirstName) or TextUtils.isEmpty(IndividualLastName)  or
                    TextUtils.isEmpty(IndividualDOB)  or TextUtils.isEmpty(IndividualEddress)  or
                    TextUtils.isEmpty(IndividualPhoneNum)  or TextUtils.isEmpty(IndividualGender)  or
                    TextUtils.isEmpty(IndividaulPassword) -> {
                showErrorSnackBar("Please enter all details.")
                false
            }
            TextUtils.isEmpty(IndividualEmail) -> {
                showErrorSnackBar("Please enter email.")
                false
            }
            TextUtils.isEmpty(IndividaulPassword) -> {
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
    fun individualRegisteredSuccess() {

        Toast.makeText(
            this@IndividualRegister,
            "You have successfully registered.",
            Toast.LENGTH_SHORT
        ).show()

        // Hide the progress dialog
        hideProgressDialog()
        /**
         * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
         * and send him to Intro Screen for Sign-In
         */

        // Launch the register customer screen.
        startActivity(Intent(this@IndividualRegister, MainActivity::class.java))
        // Finish the Sign-Up Screen
        finish()
    }
}