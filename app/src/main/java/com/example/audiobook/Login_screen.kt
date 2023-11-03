package com.example.audiobook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
@Suppress("ClassName")
class Login_screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        val  button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            val intent = Intent(this, Login_screen_2::class.java)
            startActivity(intent)
        }
    }
}