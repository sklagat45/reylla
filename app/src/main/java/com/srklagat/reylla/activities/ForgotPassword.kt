package com.srklagat.reylla.activities

import androidx.appcompat.app.AppCompatActivity

class ForgotPassword : AppCompatActivity() {


//    private var mAuth: FirebaseAuth? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_forgot_password)
//
//        mAuth = FirebaseAuth.getInstance()
//
//        btnResetPassword.setOnClickListener {
//            val email = edtResetEmail.text.toString().trim()
//
//            if (TextUtils.isEmpty(email)) {
//                Toast.makeText(applicationContext, "Enter your email!", Toast.LENGTH_SHORT).show()
//            } else {
//                mAuth!!.sendPasswordResetEmail(email)
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            Toast.makeText(this@ForgotPassword, "Check email to reset your password!", Toast.LENGTH_SHORT).show()
//                        } else {
//                            Toast.makeText(this@ForgotPassword, "Fail to send reset password email!", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//            }
//        }
//
//        btnBack.setOnClickListener {
//            finish()
//        }
//    }
}