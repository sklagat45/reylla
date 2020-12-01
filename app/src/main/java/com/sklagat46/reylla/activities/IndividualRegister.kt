package com.sklagat46.reylla.activities

import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sklagat46.reylla.R
import com.sklagat46.reylla.firebase.FirestoreClass
import com.sklagat46.reylla.model.User
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

        setSupportActionBar(toolbar_sign_up_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.back_btn)
        }

        toolbar_sign_up_activity.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * A function to register a user to our app using the Firebase.
     * For more details visit: https://firebase.google.com/docs/auth/android/custom-auth
     */
    private fun registerUser() {

        // Here we get the text from editText and trim the space
        val IndividualProfileImage: ImageView = iv_indiv_profile_image
        val IndividualFirstName: String = et_indiv_first_name.text.toString().trim { it <= ' ' }
        val IndividualLastName: String = et_indiv_last_name.text.toString().trim { it <= ' ' }
        val IndividualDOB: String = et_indiv_birth_date.text.toString().trim { it <= ' ' }
        val IndividualEddress: String = et_indiv_address.text.toString().trim { it <= ' ' }
        val IndividualPhoneNum: String = et_indiv_phonenumber.text.toString().trim { it <= ' ' }
        val IndividualEmail: String = et_indiv_email.text.toString().trim { it <= ' ' }
        val IndividualGender: String = et_indiv_gender.text.toString().trim { it <= ' ' }
        val IndividaulPassword: String = et_indiv_password.text.toString().trim { it <= ' ' }

        if (validateForm(name, email, password)) {
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        // If the registration is successfully done
                        if (task.isSuccessful) {

                            // Firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            // Registered Email
                            val registeredEmail = firebaseUser.email!!

                            val user = User(
                                firebaseUser.uid, name, registeredEmail
                            )

                            // call the registerUser function of FirestoreClass to make an entry in the database.
                            FirestoreClass().registerUser(this@SignUpActivity, user)
                        } else {
                            Toast.makeText(
                                this@SignUpActivity,
                                task.exception!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
        }
    }
}