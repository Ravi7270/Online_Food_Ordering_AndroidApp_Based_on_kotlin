package com.example.raviproject

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.raviproject.databinding.ActivityStartBinding
import android.widget.Button


class startActivity : AppCompatActivity() {
    private val binding : ActivityStartBinding by lazy {
        ActivityStartBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        setContentView(R.layout.activity_start)
//        val openSecondActivityButton:Button = findViewById(R.id.button)
//        openSecondActivityButton.setOnClickListener {
//            val intent = Intent(this,loginActivity::class.java)
//            startActivity(intent)
//        }

        setContentView(binding.root)
        binding.button.setOnClickListener {
            val intent = Intent(this, loginactivity::class.java)
//            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}