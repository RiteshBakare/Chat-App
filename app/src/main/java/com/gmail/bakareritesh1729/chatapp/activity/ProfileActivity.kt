package com.gmail.bakareritesh1729.chatapp.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.gmail.bakareritesh1729.chatapp.R
import com.gmail.bakareritesh1729.chatapp.databinding.ActivityProfileBinding
import com.gmail.bakareritesh1729.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.UUID

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var databaseReference: DatabaseReference

    private var filePath: Uri? = null

    companion object {
        const val PICK_IMAGE_REQUEST = 2020
    }

    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        databaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)

        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val user = snapshot.getValue(User::class.java)

                binding.tvName.setText(user!!.userName)

                if (user.profileImage == "") {
                    binding.ivProfileImage.setImageResource(R.drawable.baseline_person_24)
                } else {
                    Glide.with(this@ProfileActivity)
                        .load(user.profileImage)
                        .into(binding.ivProfileImage)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProfileActivity, error.message, Toast.LENGTH_SHORT).show()
            }

        })

        binding.imgProfile.setOnClickListener {
            startActivity(Intent(this, UsersActivity::class.java))
            finish()
        }

        binding.imgBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            finish()
        }

        binding.ivProfileImage.setOnClickListener {
            chooseImage()
        }

        binding.btnSave.setOnClickListener {
            uploadImage()
        }

    }

    private fun chooseImage() {

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode != null) {
            filePath = data!!.data
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                binding.ivProfileImage.setImageBitmap(bitmap)
                binding.btnSave.visibility = View.VISIBLE
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage() {

        if (filePath != null) {

            binding.myProgressBar.visibility = View.VISIBLE

            val ref: StorageReference =
                storageReference.child("image/" + UUID.randomUUID().toString())

            ref.putFile(filePath!!)
                .addOnSuccessListener {
                    binding.myProgressBar.visibility = View.GONE
                    Toast.makeText(applicationContext, "Uploaded", Toast.LENGTH_LONG).show()
                    binding.btnSave.visibility = View.GONE

                    val hashMap: HashMap<String, String> = HashMap()
                    hashMap["userName"] = binding.tvName.text.toString()
                    hashMap["profileImage"] = filePath.toString()
                    databaseReference.updateChildren(hashMap as Map<String, Any>)

                }
                .addOnFailureListener {
                    binding.myProgressBar.visibility = View.GONE
                    Toast.makeText(
                        applicationContext,
                        "Failed " + it.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
        }

    }

}