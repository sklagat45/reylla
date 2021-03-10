package com.sklagat46.reylla.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sklagat46.reylla.R
import com.sklagat46.reylla.firebase.FirestoreClass
import com.sklagat46.reylla.model.Customer
import kotlinx.android.synthetic.main.activity_individual_register.*
import kotlinx.android.synthetic.main.activity_register_customer.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class RegisterCustomer : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_customer)

        // This is used to hide the status bar and make the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setUpActionBar()

        // Click event for sign-up button.
        btn_customer_register.setOnClickListener {
            registerCustomer()
        }
    }

    private fun setUpActionBar() {

        setSupportActionBar(toolbar_customer_sign_up_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.back_btn)
        }
        toolbar_customer_sign_up_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    /**
     * A function to register a user to our app using the Firebase.
     * For more details visit: https://firebase.google.com/docs/auth/android/custom-auth
     */
    private fun registerCustomer() {
        // Here we get the text from editText and trim the space
        val customerProfileImage: ImageView = iv_profile_customer_image
        val customerFName: String = et_customer_first_name.text.toString().trim { it <= ' ' }
        val customerLName: String = et_customer_last_name.text.toString().trim { it <= ' ' }
        val customerDOB: String = et_customer_birth_date.text.toString().trim { it <= ' ' }
        val customerAddress: String = et_customer_address.text.toString().trim { it <= ' ' }
        val customerPhoneNumber: String = et_customer_phonenumber.text.toString().trim { it <= ' ' }
        val customerEmail: String = et_customer_email.text.toString().trim { it <= ' ' }
        val customerGender: String = et_customer_gender.text.toString().trim { it <= ' ' }
        val customerPassword: String = et_customer_password.text.toString().trim { it <= ' ' }

        if (validateForm(customerFName, customerLName, customerDOB, customerAddress, customerPhoneNumber, customerEmail, customerGender, customerPassword)) {
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(customerEmail, customerPassword)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        // If the registration is successfully done
                        if (task.isSuccessful) {

                            // Firebase registered customer
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            // Registered Email
                            val registeredEmail = firebaseUser.email!!

                            val customer = Customer(
                                firebaseUser.uid, customerLName, registeredEmail
                            )

                            // call the registerCustomer function of FirestoreClass to make an entry in the database.
                            FirestoreClass().registerCustomer(this@RegisterCustomer, customer)
                        } else {
                            Toast.makeText(
                                this@RegisterCustomer,
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
    private fun validateForm(customerFName: String, customerLName: String,
                             customerDOB: String, customerAddress: String, customerPhoneNumber: String,
                             customerEmail: String, customerGender: String, customerPassword: String): Boolean {
        return when {
            TextUtils.isEmpty(customerFName) or TextUtils.isEmpty(customerLName)  or
                    TextUtils.isEmpty(customerDOB)  or TextUtils.isEmpty(customerAddress)  or
                    TextUtils.isEmpty(customerPhoneNumber)  or TextUtils.isEmpty(customerGender)  or
                    TextUtils.isEmpty(customerPassword) -> {
                showErrorSnackBar("Please enter all details.")
                false
            }
            TextUtils.isEmpty(customerEmail) -> {
                showErrorSnackBar("Please enter email.")
                false
            }
            TextUtils.isEmpty(customerPassword) -> {
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
    fun customerRegisteredSuccess() {

        Toast.makeText(
            this@RegisterCustomer,
            "You have successfully registered.",
            Toast.LENGTH_SHORT
        ).show()

        // Hide the progress dialog
        hideProgressDialog()



        // Launch the register customer screen.
        startActivity(Intent(this@RegisterCustomer, MainActivity::class.java))

//        /**
//         * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
//         * and send him to Intro Screen for Sign-In
//         */
//        FirebaseAuth.getInstance().signOut()
        // Finish the Sign-Up Screen
        finish()
    }
}