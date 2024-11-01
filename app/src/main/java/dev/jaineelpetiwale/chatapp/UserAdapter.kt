package dev.jaineelpetiwale.chatapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class UserAdapter(val context: Context, private val userList: ArrayList<User>):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]

        if (currentUser.displaypic != "") {
            Glide.with(context).load(currentUser.displaypic).circleCrop().into(holder.displayPic)
        } else {
            Glide.with(context).load(R.drawable.profile).circleCrop().into(holder.displayPic)
        }

        holder.textName.text = currentUser.name

        holder.itemView.setOnClickListener {
            val intent = Intent(context,ChatActivity::class.java)

            intent.putExtra("name",currentUser.name)
            intent.putExtra("uid",currentUser.uid)
            intent.putExtra("displaypic",currentUser.displaypic)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val displayPic: ImageView = itemView.findViewById(R.id.txt_picture)
        val textName = itemView.findViewById<TextView>(R.id.txt_name)!!
    }
}