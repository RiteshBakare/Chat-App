package com.gmail.bakareritesh1729.chatapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.gmail.bakareritesh1729.chatapp.R
import com.gmail.bakareritesh1729.chatapp.adapter.UserAdapter
import com.gmail.bakareritesh1729.chatapp.databinding.ActivityUsersBinding
import com.gmail.bakareritesh1729.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class UsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsersBinding

    private val userList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.UserRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.imgBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        binding.imgProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }


        getUsersList()
    }


    private fun getUsersList() {

        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Users")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val currentUser = snapshot.getValue(User::class.java)

                if (currentUser!!.profileImage == "") {
                    binding.imgProfile.setImageResource(R.drawable.baseline_person_24)
                } else {
                    Glide.with(this@UsersActivity)
                        .load(currentUser.profileImage)
                        .into(binding.imgProfile)
                }

                userList.clear()

                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    var user: User

                    try {
                        user = dataSnapShot.getValue(User::class.java)!!
                    } catch (e: Exception) {
                        val map = dataSnapShot.value as? Map<String, String> ?: continue
                        user = User(map)
                    }

                    if (!user.userId.equals(firebase.uid)) {
                        userList.add(user)
                    }
                }


                val userAdapter = UserAdapter(this@UsersActivity, userList)
                binding.UserRecyclerView.adapter = userAdapter

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UsersActivity, error.message, Toast.LENGTH_SHORT).show()
            }

        })

    }

}