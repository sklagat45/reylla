package com.sklagat46.reylla.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.sklagat46.reylla.R
import com.sklagat46.reylla.model.Company
import com.sklagat46.reylla.model.Customer
import com.sklagat46.reylla.model.IndividualProviders
import com.sklagat46.reylla.utils.Constants
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


        btn_intro_sign_in.setOnClickListener {
            signInRegisteredUser()
        }

//        signInRegisteredUser()

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
//                        FirestoreClass().signInUser(this@IntroActivity)

                        val currentUser = FirebaseAuth.getInstance().currentUser
                        val registeredUserID = currentUser.uid
                        val userLoginDatabase = FirebaseFirestore.getInstance()
//                            .getReference().child("Users").child(RegisteredUserID);

                        val userCompanyRef: CollectionReference = userLoginDatabase.collection("company")
                        val ComQuery: Query = userCompanyRef.whereEqualTo("id", registeredUserID)
                        if(ComQuery!=null){

                            // Here we pass the collection name from which we wants the data.
                            userLoginDatabase.collection(Constants.COMPANIES)
                                // The document id to get the Fields of user.
                                .document(getCurrentUserID())
                                .get()
                                .addOnSuccessListener { document ->
                                    Log.e(
                                        javaClass.simpleName, document.toString()
                                    )

                                    // Here we have received the document snapshot which is converted into the User Data model object.
                                    val loggedInUser = document.toObject(Company::class.java)!!

                                    // Here call a function of base activity for transferring the result to it.
                                    hideProgressDialog()

                                    startActivity(Intent(this@IntroActivity, ProviderHomeActivity::class.java))
                                    this.finish()
                                }
                                .addOnFailureListener { e ->
                                    Log.e(
                                        javaClass.simpleName,
                                        "Error while getting loggedIn user details",
                                        e
                                    )
                                }
                        }

                        val userIndividualProviderRef: CollectionReference = userLoginDatabase.collection("company")
                        val IndivQuery: Query = userIndividualProviderRef.whereEqualTo("id", registeredUserID)
                        if(IndivQuery!=null){

                            // Here we pass the collection name from which we wants the data.
                            userLoginDatabase.collection(Constants.INDIVIDUALPROVIDERS)
                                // The document id to get the Fields of user.
                                .document(getCurrentUserID())
                                .get()
                                .addOnSuccessListener { document ->
                                    Log.e(
                                        javaClass.simpleName, document.toString()
                                    )

                                    // Here we have received the document snapshot which is converted into the User Data model object.
                                    val loggedInUser = document.toObject(IndividualProviders::class.java)!!

                                    // Here call a function of base activity for transferring the result to it.
                                    hideProgressDialog()

                                    startActivity(Intent(this@IntroActivity, ProviderHomeActivity::class.java))
                                    this.finish()
                                }
                                .addOnFailureListener { e ->
                                    Log.e(
                                        javaClass.simpleName,
                                        "Error while getting loggedIn user details",
                                        e
                                    )
                                }
                        }

                        val userCustomerRef: CollectionReference = userLoginDatabase.collection("customers")
                        val customerQuery: Query = userCustomerRef.whereEqualTo("id", registeredUserID)
                        if(customerQuery !=null){

                            // Here we pass the collection name from which we wants the data.
                            userLoginDatabase.collection(Constants.CUSTOMERS)
                                // The document id to get the Fields of user.
                                .document(getCurrentUserID())
                                .get()
                                .addOnSuccessListener { document ->
                                    Log.e(
                                        javaClass.simpleName, document.toString()
                                    )

                                    // Here we have received the document snapshot which is converted into the User Data model object.
                                    val loggedInUser = document.toObject(Customer::class.java)!!

                                    // Here call a function of base activity for transferring the result to it.
                                    hideProgressDialog()

                                    startActivity(Intent(this@IntroActivity, MainActivity::class.java))
                                    this.finish()
                                }
                                .addOnFailureListener { e ->
                                    Log.e(
                                        javaClass.simpleName,
                                        "Error while getting loggedIn user details",
                                        e
                                    )
                                }
                        }

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
//    fun signInSuccess(user: User) {
//        hideProgressDialog()
//        startActivity(Intent(this@IntroActivity, MainActivity::class.java))
//        this.finish()
//    }


    fun btnSignUp(view: View) {
        cardSignIn.visibility = GONE
        cardSignUp.visibility = VISIBLE
    }
    fun btnSignIn(view: View) {
        cardSignUp.visibility = GONE
        cardSignIn.visibility = VISIBLE
    }


}