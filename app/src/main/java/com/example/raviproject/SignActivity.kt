package com.example.raviproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.raviproject.databinding.ActivityLoginBinding
import com.example.raviproject.databinding.ActivitySignBinding
import com.example.raviproject.model.usermodel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class signactivity : AppCompatActivity() {
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var username: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference


    private val binding: ActivitySignBinding by lazy {
        ActivitySignBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//        firebaseDatabase = FirebaseDatabase.getInstance()
//        database = firebaseDatabase.reference.child("users")

        //intialize fire base
        auth = Firebase.auth
        //intialize firebase database
        database = Firebase.database.reference
        binding.signupButton.setOnClickListener {
             username = binding.userName.text.toString()
             email = binding.emailAddress.text.toString().trim()
             password = binding.password.text.toString().trim()

            when {
                email.isEmpty() || password.isEmpty() || username.isEmpty() -> {
                    Toast.makeText(this, "Please fill in all details", Toast.LENGTH_SHORT).show()
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                }
                password.length < 6 -> {
                    Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    CreateAccount(email, password)
                }
            }
        }
        binding.alreadyhavebutton.setOnClickListener {
            val intent = Intent(this, loginactivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun CreateAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            task ->
            if (task.isComplete){
                Toast.makeText(this, "signup created", Toast.LENGTH_SHORT).show()
                saveUserData()
                startActivity(Intent(this, loginactivity::class.java))
                finish()
            }else{
                Toast.makeText(this, "Account creation failed", Toast.LENGTH_SHORT).show()
                Log.d("Account", "CreateAccount: Failure ",task.exception)
            }
        }
    }

    private fun saveUserData() {
//        retrive data from input field
        username = binding.userName.text.toString()
        email = binding.emailAddress.text.toString().trim()
        password = binding.password.text.toString().trim()
        val user = usermodel(username,email,password)
        val userId :String = FirebaseAuth.getInstance().currentUser!!.uid
        //save data in firebase database
        database.child("userdata").child(userId).setValue(user)
    }

}