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
import com.google.firebase.auth.FirebaseUser
import android.widget.Toast
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
        signupButton.setOnClickListener {
            signUp()
        }

        val turn_back : ImageButton = findViewById(R.id.turn_back)
        turn_back.setOnClickListener{
            val intent = Intent(this,Login_screen_2::class.java)
            startActivity(intent)
        }
    }
    private fun signUp(){

        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            return
        }

        // Đăng ký tài khoản mới bằng email và mật khẩu
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Đăng ký thành công
                    val user: FirebaseUser? = auth.currentUser
                    if (user != null) {
                        // Cập nhật thông tin tài khoản trên Firebase Authentication (nếu cần)
                        // Ví dụ: user.updateEmail(newEmail)

                        // Chuyển sang Home_page khi đăng ký thành công
                        navigateToHomeActivity()
                    } else {
                        Log.e("SignUpActivity", "Current user is null")
                    }
                } else {
                    // Đăng ký thất bại
                    Log.w("SignUpActivity", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToHomeActivity() {
        val intent = Intent(this, Home_page::class.java)
        startActivity(intent)
        finish() // Đóng SignUpActivity sau khi chuyển sang HomeActivity
    }
}