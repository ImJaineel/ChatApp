package dev.jaineelpetiwale.chatapp

import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class ChangeDisplayPicActivity : AppCompatActivity() {

    private lateinit var selectedImg: ImageView
    private lateinit var btnCancelEdit: Button
    private lateinit var btnSaveEdit: Button
    private lateinit var loading: ProgressBar
    private lateinit var storage: FirebaseStorage
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_changedisplaypic)

        supportActionBar?.hide()

        selectedImg = findViewById(R.id.selectedImg)
        btnCancelEdit = findViewById(R.id.btn_CancelEdit)
        btnSaveEdit = findViewById(R.id.btn_SaveEdit)

        loading = findViewById(R.id.changeDisplayPicLoading)
        loading.visibility = ProgressBar.INVISIBLE


        mAuth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        db = FirebaseDatabase.getInstance().reference

        // Get new display pic uri from extra
        val newDisplayPicUri = Uri.parse(intent.getStringExtra("newDisplayPicUri"))
        Log.d(TAG, "onCreate: $newDisplayPicUri")
        if (newDisplayPicUri != null) {
            Glide.with(this).load(newDisplayPicUri).into(selectedImg)
        }

        btnCancelEdit.setOnClickListener {
            finish()
        }

        btnSaveEdit.setOnClickListener {
            loading.visibility = ProgressBar.VISIBLE

            val ref = storage.reference.child("DisplayPic/${mAuth.currentUser?.uid}")
            ref.putFile(newDisplayPicUri)
                .addOnSuccessListener {
                    Log.d(TAG, "onSuccess: Display pic uploaded")
                ref.downloadUrl.addOnSuccessListener { downloaded ->
                    Log.d(TAG, "onSuccess: $downloaded")
                    db.child("user")
                        .child(mAuth.currentUser?.uid!!)
                        .child("displaypic")
                        .setValue(downloaded.toString())
                        .addOnSuccessListener {
                            Log.d(TAG, "onSuccess: Display pic updated")
                            finish()
                        }
                        .addOnFailureListener {except ->
                            Log.d(TAG, "onFailure: ${except.message}")
                        }
                }
            }
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        loading.visibility = ProgressBar.INVISIBLE
//        // Go back to DisplayPicActivity
//        val displaypicintent = Intent(this, DisplayPicActivity::class.java)
//        displaypicintent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//        startActivity(displaypicintent)
//    }

}
