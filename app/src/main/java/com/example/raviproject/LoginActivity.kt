package com.example.raviproject

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.raviproject.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class loginactivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        // Go to SignUp Activity
        binding.donthavebutton.setOnClickListener {
            val intent = Intent(this, signactivity::class.java)
            startActivity(intent)
        }

        // Login button click
        binding.LoginButton.setOnClickListener {
            email = binding.loginEmail.text.toString().trim()
            password = binding.loginPassword.text.toString().trim()

            when {
                email.isEmpty() || password.isEmpty() -> {
                    Toast.makeText(this, "Please fill in all details", Toast.LENGTH_SHORT).show()
                }

                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                }

                password.length < 6 -> {
                    Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    loginUser()
                }
            }
        }
    }

    private fun loginUser() {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
//                saveUserData()
                updateUi(auth.currentUser)
            } else {
                Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    private fun saveUserData() {
//        val user = UserModel(email, password)
//        val userId = auth.currentUser?.uid ?: return
//
//        // Save data into database
//        database.child("userdata").child(userId).setValue(user)
//    }

    private fun updateUi(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, mainactivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
