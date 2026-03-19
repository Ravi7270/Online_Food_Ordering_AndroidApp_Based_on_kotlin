package com.example.raviproject

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import android.os.Handler


@Suppress("DEPRECATION")
class spleshscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splesh_screen)
        Handler().postDelayed({
            val intent = Intent(this, loginactivity::class.java)
            startActivity(intent)
            finish()
        },3000)

    }
}