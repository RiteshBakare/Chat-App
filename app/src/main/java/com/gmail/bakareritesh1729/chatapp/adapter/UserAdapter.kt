package com.gmail.bakareritesh1729.chatapp.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gmail.bakareritesh1729.chatapp.R
import com.gmail.bakareritesh1729.chatapp.model.User
import de.hdodenhof.circleimageview.CircleImageView
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.gmail.bakareritesh1729.chatapp.activity.ChatActivity


class UserAdapter(private val context: Context, private val userList: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtUserName: TextView = itemView.findViewById(R.id.userName)
        val txtDummy: TextView = itemView.findViewById(R.id.tv_temp)
        val imageUser: CircleImageView = itemView.findViewById(R.id.userImage)
        val layoutUser : CardView = itemView.findViewById(R.id.layoutUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val user = userList[position]
        holder.txtUserName.text = user.userName

        Glide.with(context)
            .load(user.profileImage)
            .placeholder(R.drawable.baseline_person_24)
            .into(holder.imageUser)

        holder.layoutUser.setOnClickListener {

            val intent = Intent(context,ChatActivity::class.java)
            intent.putExtra("UserId",user.userId)
            context.startActivity(intent)

        }

    }

}