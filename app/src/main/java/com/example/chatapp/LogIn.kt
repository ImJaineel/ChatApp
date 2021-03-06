package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class LogIn : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogIn: Button
    private lateinit var btnSignUp: Button
    private lateinit var btnResetPassword: Button

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()

        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnLogIn = findViewById(R.id.login)
        btnSignUp = findViewById(R.id.signup)
        btnResetPassword = findViewById(R.id.resetPassword)

        btnResetPassword.setOnClickListener {
            val email = edtEmail.text.toString()

            if (email == "") {
                Toast.makeText(this,"Please Enter your email",Toast.LENGTH_SHORT).show()
            }
            else {
                mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this,"Reset Password Email has been sent.",Toast.LENGTH_LONG).show()
                            edtEmail.setText("")
                        }
                    }
            }
        }

        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        btnLogIn.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            if (email == "" || password == "")
            {
                Toast.makeText(this, "Username or Password blank", Toast.LENGTH_LONG).show()
            }
            else {
                login(email, password)
            }
        }

    }

    private fun login(email: String, password: String) {
        // logic for logging a user
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // code for logging in
                    val intent = Intent(this@LogIn, MainActivity::class.java)
                    Toast.makeText(this,"User Logged in",Toast.LENGTH_SHORT).show()
                    finish()
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@LogIn, "User Doesn't exist", Toast.LENGTH_SHORT).show()
                }
            }
    }
}