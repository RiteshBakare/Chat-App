package com.gmail.bakareritesh1729.chatapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.gmail.bakareritesh1729.chatapp.R
import com.gmail.bakareritesh1729.chatapp.adapter.ChatAdapter
import com.gmail.bakareritesh1729.chatapp.databinding.ActivityChatBinding
import com.gmail.bakareritesh1729.chatapp.model.Chat
import com.gmail.bakareritesh1729.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    private var firebaseUser: FirebaseUser? = null

    private var reference: DatabaseReference? = null

    private var chatList : ArrayList<Chat> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = getIntent()
        val userId = intent.getStringExtra("UserId")

        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId!!)


        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this@ChatActivity)


        binding.imgBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        reference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val user = snapshot.getValue(User::class.java)

                binding.tvUserName.text = user!!.userName

                if (user.profileImage == "") {
                    binding.imgProfile.setImageResource(R.drawable.baseline_person_24)
                } else {
                    Glide.with(this@ChatActivity)
                        .load(user.profileImage)
                        .into(binding.imgProfile)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatActivity, error.message, Toast.LENGTH_SHORT).show()
            }

        })

        binding.btnSendMessage.setOnClickListener {

            val message = binding.etMessage.text.toString()

            if (message.isEmpty()) {
                binding.etMessage.error = "Please Enter Some thing"
            } else {
                sendMessage(firebaseUser!!.uid, userId, message)
                binding.etMessage.text.clear()
            }

        }


        readMessage(firebaseUser!!.uid,userId)
    }

    private fun sendMessage(senderId: String, receiverId: String, message: String) {

        val reference: DatabaseReference? = FirebaseDatabase.getInstance().getReference()

        val hashMap: HashMap<String, String> = HashMap()

        hashMap.put("senderId", senderId)
        hashMap.put("receiverId", receiverId)
        hashMap.put("message", message)

        reference!!.child("Chat").push().setValue(hashMap)

    }


    private fun readMessage(senderId: String, receiverId: String) {

        val databaseReference : DatabaseReference =FirebaseDatabase.getInstance().getReference("Chat")

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                chatList.clear()

                for(dataSnapshot: DataSnapshot in snapshot.children  ) {

                    val chat = dataSnapshot.getValue(Chat::class.java)

                    if((chat!!.senderId == senderId && chat!!.receiverId == receiverId)
                        || (chat!!.senderId == receiverId && chat!!.receiverId==senderId ) ) {
                        chatList.add(chat)
                    }
                }

                val chatAdapter = ChatAdapter(this@ChatActivity,chatList)

                binding.chatRecyclerView.adapter = chatAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatActivity,error.message,Toast.LENGTH_SHORT).show()
            }

        })

    }

}