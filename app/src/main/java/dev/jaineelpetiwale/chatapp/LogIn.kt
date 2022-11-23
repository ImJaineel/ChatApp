package dev.jaineelpetiwale.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class LogIn : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogIn: Button
    private lateinit var btnSignUp: Button
    private lateinit var btnResetPassword: Button
    private lateinit var loading: ProgressBar

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
        loading = findViewById(R.id.loading)

        loading.visibility = ProgressBar.INVISIBLE

        if (mAuth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

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
                loading.visibility = ProgressBar.VISIBLE
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // code for logging in
                            val intent = Intent(this@LogIn, MainActivity::class.java)
                            Toast.makeText(this,"User Logged in",Toast.LENGTH_SHORT).show()
                            finish()
                            startActivity(intent)
                        }
                        // wrong password
                        else if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            loading.visibility = ProgressBar.INVISIBLE
                            Toast.makeText(this,"Wrong Password",Toast.LENGTH_SHORT).show()
                        }
                        // user not found
                        else if(task.exception is FirebaseAuthInvalidCredentialsException) {
                            loading.visibility = ProgressBar.INVISIBLE
                            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            loading.visibility = ProgressBar.INVISIBLE
                            Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

    }
}
