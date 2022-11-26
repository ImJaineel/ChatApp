package dev.jaineelpetiwale.chatapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserProfile : AppCompatActivity() {

    private lateinit var dp: ImageView
    private lateinit var showName: TextView
    private lateinit var showEmail: TextView
    private lateinit var btnResetPasswd: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        supportActionBar?.hide()

        dp = findViewById(R.id.img_profile)
        showName = findViewById(R.id.textView2)
        showEmail = findViewById(R.id.textView3)
        btnResetPasswd = findViewById(R.id.resetPassword)

        auth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference

        mDbRef.child("user")
            .child(auth.currentUser?.uid!!)
            .get()
            .addOnSuccessListener {
                val name = "Name: " + it.child("name").value.toString()
                showName.text = name

                val displaypic = it.child("displaypic").value
                if (displaypic != "") {
                    Glide.with(this).load(Uri.parse(displaypic.toString())).into(dp)
                } else {
                    Glide.with(this).load(R.mipmap.ic_launcher_round).into(dp)
                }

            }

        val email = "Email: " + auth.currentUser?.email
        showEmail.text = email

        dp.setOnClickListener{
            val displaypicintent = Intent(this, DisplayPicActivity::class.java)
            startActivity(displaypicintent)
        }

        btnResetPasswd.setOnClickListener {
            auth.sendPasswordResetEmail(auth.currentUser?.email!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Password reset email sent", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    override fun onRestart() {
        super.onRestart()
        mDbRef.child("user")
            .child(auth.currentUser?.uid!!)
            .get()
            .addOnSuccessListener {
                val name = "Name: " + it.child("name").value.toString()
                showName.text = name

                val displaypic = it.child("displaypic").value
                if (displaypic != null) {
                    Glide.with(this).load(displaypic).into(dp)
                }
                else {
                    dp.setImageResource(R.mipmap.ic_launcher_round)
                }

            }
    }
}