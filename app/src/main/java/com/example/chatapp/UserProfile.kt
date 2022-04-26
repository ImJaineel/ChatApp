package com.example.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class UserProfile : AppCompatActivity() {

    private lateinit var showName: TextView
    private lateinit var showEmail: TextView

//    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

//        auth = Firebase.auth
//        val user = Firebase.auth.currentUser
//        showName.text = "Name: Jaineel"
//        showEmail.text = "Email-id: jaineel2001@gmail.com"
    }
}