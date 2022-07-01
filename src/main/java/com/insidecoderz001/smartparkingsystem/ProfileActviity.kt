package com.insidecoderz001.smartparkingsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProfileActviity : AppCompatActivity() {

    private lateinit var firebaseUser: FirebaseUser
    var mAuth : FirebaseAuth?=null
    lateinit var signout:CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_actviity)

        ids()
        logoutJob()



    }

    private fun logoutJob() {
        signout.setOnClickListener {
            mAuth!!.signOut()
            val intent = Intent(applicationContext, SignIn::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }

    private fun ids() {
        signout =findViewById(R.id.logout)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        mAuth= FirebaseAuth.getInstance()
    }
}