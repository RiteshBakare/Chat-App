package com.gmail.bakareritesh1729.chatapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gmail.bakareritesh1729.chatapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var auth: FirebaseAuth
    private var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser

        if (firebaseUser != null) {
            startActivity(Intent(this, UsersActivity::class.java))
            finish()
        }

        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnSignIn.setOnClickListener {
            if (validateCredentials()) {

                val email = binding.etEmail.text.toString()
                val pass = binding.etPass.text.toString()

                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener {

                        if (it.isSuccessful) {
                            binding.etEmail.setText("")
                            binding.etPass.setText("")
                            startActivity(Intent(this, UsersActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Please Enter Correct Email and Password ",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
            }
        }


    }

    private fun validateCredentials(): Boolean {

        if (binding.etEmail.text?.isEmpty() == true) {
            binding.etEmail.error = "Enter Your Email ID "
            return false
        }

        if (binding.etPass.text?.isEmpty() == true) {
            binding.etPass.error = "Enter Your Password "
            return false
        }
        return true
    }

}