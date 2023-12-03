package com.example.audiobook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
class Sign_up : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: FirebaseDatabase

    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var passwordConfirm: EditText
    private lateinit var signupButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance()

        usernameEditText = findViewById(R.id.Username)
        emailEditText = findViewById(R.id.your_email)
        passwordEditText = findViewById(R.id.Password)
        passwordConfirm = findViewById(R.id.ConfirmPassword)

        signupButton = findViewById(R.id.signup)

        val turn_back : ImageButton = findViewById(R.id.turn_back)
        turn_back.setOnClickListener{
            val intent = Intent(this,Login_screen_2::class.java)
            startActivity(intent)
        }
    }
}