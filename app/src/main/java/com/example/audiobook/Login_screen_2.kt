package com.example.audiobook

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Login_screen_2 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen2)

        // Khởi tạo Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Khởi tạo Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().reference

        usernameEditText = findViewById(R.id.Username)
        passwordEditText = findViewById(R.id.Password)
        loginButton = findViewById(R.id.signin)

        val button: Button = findViewById(R.id.signup)
        button.setOnClickListener {
            val intent = Intent(this, Sign_up::class.java)
            startActivity(intent)
        }
        loginButton.setOnClickListener {
            login();
        }
    }
    private fun login() {
        // Thực hiện đăng nhập bằng email và mật khẩu
        val email = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            return
        }
        // Kiểm tra xem tài khoản có tồn tại trong Firebase Authentication không
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Đăng nhập thành công
                    val user: FirebaseUser? = auth.currentUser
                    if (user != null) {
                        // Chuyển sang Home_page khi đăng nhập thành công
                        navigateToHomeActivity()
                    } else {
                        Log.e("LoginActivity", "Current user is null")
                    }
                } else {
                    // Đăng nhập thất bại
                    Log.w("LoginActivity", "signInWithEmail:failure", task.exception)
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToHomeActivity() {
        val intent = Intent(this, MainScreen::class.java)
        startActivity(intent)
        finish() // Đóng LoginActivity sau khi chuyển sang HomeActivity
    }
}



