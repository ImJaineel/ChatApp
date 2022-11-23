package dev.jaineelpetiwale.chatapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserProfile : AppCompatActivity() {

    private lateinit var showName: TextView
    private lateinit var showEmail: TextView
    private lateinit var btnResetPasswd: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        showName = findViewById(R.id.textView2)
        showEmail = findViewById(R.id.textView3)
        btnResetPasswd = findViewById(R.id.resetPassword)

        auth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference

        mDbRef.child("user")
            .child(auth.currentUser?.uid!!)
            .child("name")
            .get().addOnSuccessListener {
                val name = "Name: " + it.value
                showName.text = name
        }

        val email = "Email: " + auth.currentUser?.email
        showEmail.text = email

        btnResetPasswd.setOnClickListener {
            auth.sendPasswordResetEmail(auth.currentUser?.email!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Password reset email sent", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}