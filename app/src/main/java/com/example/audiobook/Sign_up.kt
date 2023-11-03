package com.example.audiobook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.content.Intent
class Sign_up : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val turn_back : ImageButton = findViewById(R.id.turn_back)
        turn_back.setOnClickListener{
            val intent = Intent(this,Login_screen_2::class.java)
            startActivity(intent)
        }
    }
}