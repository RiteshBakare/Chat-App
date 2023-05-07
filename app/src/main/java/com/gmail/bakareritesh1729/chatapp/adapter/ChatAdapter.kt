package com.gmail.bakareritesh1729.chatapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gmail.bakareritesh1729.chatapp.R
import com.gmail.bakareritesh1729.chatapp.model.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import de.hdodenhof.circleimageview.CircleImageView

class ChatAdapter(private val context: Context, private val chatList: ArrayList<Chat>) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private val MESSAGE_TYPE_LEFT = 0
    private val MESSAGE_TYPE_RIGHT = 1

    private var firebaseUser : FirebaseUser? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val message : TextView = view.findViewById(R.id.tvMessage)
        val userImage : CircleImageView = view.findViewById(R.id.civ_Profile)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        if(viewType == MESSAGE_TYPE_RIGHT) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_right,parent,false)
            return ViewHolder(view)
        }
        else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_left,parent,false)
            return ViewHolder(view)
        }

    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chatList[position]
        holder.message.text = chat.message
    }

    override fun getItemViewType(position: Int): Int {

        firebaseUser = FirebaseAuth.getInstance().currentUser

        if(chatList[position].senderId == firebaseUser!!.uid) {
            return MESSAGE_TYPE_RIGHT
        }
        else {
            return MESSAGE_TYPE_LEFT
        }

    }


}