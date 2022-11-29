package dev.jaineelpetiwale.chatapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var MainScreenUserImage: ImageButton
//    private lateinit var search: ImageButton
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference

        MainScreenUserImage = findViewById(R.id.MainScreenUserImage)
//        search = findViewById(R.id.searchButton)

        mDbRef.child("user")
            .child(mAuth.currentUser?.uid!!)
            .get()
            .addOnSuccessListener {
                val displaypic = it.child("displaypic").value
                if (displaypic != "") {
                    Glide.with(this).load(Uri.parse(displaypic.toString())).circleCrop().into(MainScreenUserImage)
                } else {
                    Glide.with(this).load(R.drawable.profile).circleCrop().into(MainScreenUserImage)
                }
            }

        MainScreenUserImage.setOnClickListener {
            startActivity(Intent(this, UserProfile::class.java))
        }

//        search.setOnClickListener {
//            Toast.makeText(this, "Search Not Working", Toast.LENGTH_SHORT).show()
//        }


        userList = ArrayList()
        adapter = UserAdapter(this,userList)
        
        userRecyclerView = findViewById(R.id.userRecyclerView)

        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter
        
        mDbRef.child("user").addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)
                    if(mAuth.currentUser?.uid != currentUser?.uid){
                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.UserProfile){
            val gotoProfileIntent = Intent(this@MainActivity, UserProfile::class.java)
            startActivity(gotoProfileIntent)
        }
        if(item.itemId == R.id.logout){
            // write he login for logout
            mAuth.signOut()
            finish()
            return true
        }
        return true
    }
}