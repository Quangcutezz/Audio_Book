package com.example.audiobook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
class Login_screen_2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen2)

        val signup: Button = findViewById(R.id.button2)
        signup.setOnClickListener{
            val intent = Intent(this,Sign_up::class.java)
            startActivity(intent)
        }
    }
}