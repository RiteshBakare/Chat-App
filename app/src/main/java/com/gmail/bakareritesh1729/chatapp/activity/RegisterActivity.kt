package com.gmail.bakareritesh1729.chatapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gmail.bakareritesh1729.chatapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()


        binding.tvSignIn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnRegister.setOnClickListener {

            if (validateCredentials()) {
                registerUser(
                    binding.NameEt.text.toString(),
                    binding.emailEt.text.toString(),
                    binding.passET.text.toString()
                )
            }
        }

    }

    private fun registerUser(userName: String, email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { it ->
                if (it.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    val userId: String = user!!.uid

                    databaseReference =
                        FirebaseDatabase.getInstance().getReference("Users").child(userId)

                    val hashMap: HashMap<String, String> = HashMap()
                    hashMap["userId"] = userId
                    hashMap["userName"] = userName
                    hashMap["profileImage"] = ""

                    databaseReference.setValue(hashMap)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                binding.NameEt.setText("")
                                binding.emailEt.setText("")
                                binding.passET.setText("")
                                binding.confirmPassEt.setText("")
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(
                                    this,
                                    "User Not Register Try Again :-) ",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "SomeThing Went Wrong :-) ", Toast.LENGTH_SHORT).show()
                }

            }

    }

    private fun validateCredentials(): Boolean {

        if (binding.NameEt.text?.isEmpty() == true) {
            binding.NameEt.error = "Please Enter Your Name "
            return false
        }
        if (binding.emailEt.text?.isEmpty() == true) {
            binding.emailEt.error = "Please Enter Your Email "
            return false

        }
        if (binding.passET.text?.isEmpty() == true) {
            binding.passET.error = "Please Enter Your Password "
            return false

        }
        if (binding.confirmPassEt.text?.isEmpty() == true) {
            binding.confirmPassEt.error = "Please Enter Your Above Password "
            return false

        }

        if (binding.passET.text.toString() != binding.confirmPassEt.text.toString()) {
            Toast.makeText(this, " Enter Correct Password ", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

}