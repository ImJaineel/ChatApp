package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtName: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignUp: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        edtEmail = findViewById(R.id.edt_email)
        edtName = findViewById(R.id.edt_name)
        edtPassword = findViewById(R.id.edt_password)
        btnSignUp = findViewById(R.id.signup)

        btnSignUp.setOnClickListener {
            val email = edtEmail.text.toString()
            val name = edtName.text.toString()
            val password = edtPassword.text.toString()

            signup(email, name, password)

        }

    }

    private fun signup(email: String, name: String, password: String){
        // logic for creating a user
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // code for jumping to home
                    addUserToDatabase(email,name,mAuth.currentUser?.uid!!)
                    val intent = Intent(this@SignUp, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    Toast.makeText(this@SignUp, "Some error occurred", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(email: String, name: String,  uid: String){
        mDbRef = FirebaseDatabase.getInstance().getReference()

        mDbRef.child("user").child(uid).setValue(User(email, name, uid))
    }

}