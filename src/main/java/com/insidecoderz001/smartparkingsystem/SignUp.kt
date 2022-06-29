package com.insidecoderz001.smartparkingsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {
    lateinit var signup_2_signin:LinearLayout
    lateinit var name_signup: EditText
    lateinit var email_signup:EditText
    lateinit var password_signup:EditText
    lateinit var phone:EditText
    lateinit var btn_signup: TextView
    var mAuth: FirebaseAuth?=null
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserId : String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signup_2_signin = findViewById(R.id.signup_2_signin)
        mAuth = FirebaseAuth.getInstance()
        btn_signup = findViewById(R.id.btn_signup)
        name_signup = findViewById(R.id.name_signup)
        email_signup = findViewById(R.id.email_signup)
        password_signup = findViewById(R.id.password_signup)
        phone = findViewById(R.id.phone)

        signup_2_signin.setOnClickListener {
            startActivity(Intent(this, SignIn::class.java))
        }
        btn_signup.setOnClickListener {

            var dialog =CustomFragment()
            dialog.show(supportFragmentManager,"customDialog")
            registerUser()
        }
    }
        private fun registerUser() {

            val username: String = name_signup.text.toString()
            val email: String = email_signup.text.toString()
            val password: String = password_signup.text.toString()
            val phone: String = phone.text.toString()

            if (username == "" || email == "" || password == ""||phone=="") {

                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()

            } else {
                mAuth!!.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            firebaseUserId = mAuth!!.currentUser!!.uid
                            refUsers = FirebaseDatabase.getInstance().getReference("Users")
                                .child(firebaseUserId)

                            val userHashMap = HashMap<String, Any>()
                            userHashMap["uid"] = firebaseUserId
                            userHashMap["fullname"] = username
                            userHashMap["Number"]= phone
                            userHashMap["Email"]= email
                            userHashMap["search"] = username.toLowerCase()

                            refUsers.updateChildren(userHashMap)
                                .addOnCompleteListener {
                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    startActivity(intent)

                                }

                        } else {
                            Toast.makeText(
                                this,
                                "Error Message" + task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
    }
}