package dev.jaineelpetiwale.chatapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DisplayPicActivity : AppCompatActivity() {

    private lateinit var displaypic: ImageView
    private lateinit var btnedit: Button

    private var mAuth = FirebaseAuth.getInstance()
    private var db = FirebaseDatabase.getInstance().reference

    private val pickMedia = registerForActivityResult(PickVisualMedia()) { new_pic_uri ->
        if (new_pic_uri != null) {
            Log.d("PhotoPicker", "Selected URI: $new_pic_uri")
            val ChangeDisplayPicIntent = Intent(this, ChangeDisplayPicActivity::class.java)
            ChangeDisplayPicIntent.putExtra("newDisplayPicUri", new_pic_uri.toString())
            startActivity(ChangeDisplayPicIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_displaypic)

        supportActionBar?.hide()

        displaypic = findViewById(R.id.ViewDisplayPic)
        btnedit = findViewById(R.id.btn_edit)

        db.child("user")
            .child(mAuth.currentUser?.uid!!)
            .get()
            .addOnSuccessListener {
                val dpsrc = Uri.parse(it.child("displaypic").value.toString())
                if (dpsrc.toString() != "") {
                    Glide.with(this).load(dpsrc).into(displaypic)
                } else {
                    Glide.with(this).load(R.mipmap.ic_launcher_round).into(displaypic)
                }
            }

        btnedit.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }
    }

    override fun onResume() {
        super.onResume()
        db.child("user")
            .child(mAuth.currentUser?.uid!!)
            .get()
            .addOnSuccessListener {
                val dpsrc = Uri.parse(it.child("displaypic").value.toString())
                if (dpsrc.toString() != "") {
                    Glide.with(this).load(dpsrc).into(displaypic)
                } else {
                    Glide.with(this).load(R.mipmap.ic_launcher_round).into(displaypic)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        pickMedia.unregister()
        finish()
    }

}