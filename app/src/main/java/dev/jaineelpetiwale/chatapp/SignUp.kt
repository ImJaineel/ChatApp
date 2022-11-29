package dev.jaineelpetiwale.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtName: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignUp: Button
    private lateinit var loading: ProgressBar
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
        loading = findViewById(R.id.loading)

        loading.visibility = ProgressBar.INVISIBLE

        btnSignUp.setOnClickListener {
            val email = edtEmail.text.toString()
            val name = edtName.text.toString()
            val password = edtPassword.text.toString()

            if (email=="" || name=="" || password=="") {
                Toast.makeText(this,"Email or Name or Password is blank",Toast.LENGTH_SHORT).show()
            }
            else {
                loading.visibility = ProgressBar.VISIBLE
                signup(email, name, password)
            }
        }

    }

    private fun signup(email: String, name: String, password: String){
        // logic for creating a user
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val uid = mAuth.currentUser?.uid.toString()
                    mDbRef = FirebaseDatabase.getInstance().reference
                    mDbRef.child("user").child(uid).setValue(User(email, name, uid, ""))
                    val intent = Intent(this@SignUp, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                }
                else {
                    loading.visibility = ProgressBar.INVISIBLE
                    Toast.makeText(this,"Error: ${task.exception?.message}",Toast.LENGTH_SHORT).show()
                }

            }
    }

}