package com.sklagat46.reylla.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.sklagat46.reylla.R
import com.sklagat46.reylla.firebase.FirestoreClass
import com.sklagat46.reylla.model.User
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : BaseActivity() {

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_intro)

        // This is used to hide the status bar and make the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        signInRegisteredUser()

        btn_offer_service.setOnClickListener {

            // Launch the service provider screen.
            startActivity(Intent(this@IntroActivity, ServiceProvidersReg::class.java))
        }

        btn_book_service.setOnClickListener {

            // Launch the register customer screen.
            startActivity(Intent(this@IntroActivity, RegisterCustomer::class.java))
        }

        intro_forgotpass.setOnClickListener {

            // Launch the lounge forgot password screen.
            startActivity(Intent(this@IntroActivity, ForgotPassword::class.java))
        }
    }

    private fun signInRegisteredUser() {
        // Here we get the text from editText and trim the space
        val email: String = et_intro_email.text.toString().trim { it <= ' ' }
        val password: String = et_intro_password.text.toString().trim { it <= ' ' }

        if (validateForm(email, password)) {
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            // Sign-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Calling the FirestoreClass signInUser function to get the data of user from database.
                        FirestoreClass().signInUser(this@IntroActivity)
                    } else {
                        Toast.makeText(
                            this@IntroActivity,
                            task.exception!!.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
    /**
     * A function to validate the entries of a user.
     */
    private fun validateForm(email: String, password: String): Boolean {
        return if (TextUtils.isEmpty(email)) {
            showErrorSnackBar("Please enter email.")
            false
        } else if (TextUtils.isEmpty(password)) {
            showErrorSnackBar("Please enter password.")
            false
        } else {
            true
        }
    }

    /**
     * A function to get the user details from the firestore database after authentication.
     */
    fun signInSuccess(user: User) {
        hideProgressDialog()
        startActivity(Intent(this@IntroActivity, MainActivity::class.java))
        this.finish()
    }


    fun btnSignUp(view: View) {
        cardSignIn.visibility = GONE
        cardSignUp.visibility = VISIBLE
    }
    fun btnSignIn(view: View) {
        cardSignUp.visibility = GONE
        cardSignIn.visibility = VISIBLE
    }


}