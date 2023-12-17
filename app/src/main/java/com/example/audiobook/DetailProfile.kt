package com.example.audiobook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailProfile : AppCompatActivity() {
    private lateinit var editTextName : EditText
    private lateinit var editTextAge : EditText
    private lateinit var editTextPhone: EditText
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId: String
    private lateinit var buttonSave : Button
    private lateinit var buttonClose: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_profile)
        editTextName = findViewById(R.id.editTextName)
        editTextAge = findViewById(R.id.editTextAge)
        editTextPhone = findViewById(R.id.editTextPhone)
        buttonSave = findViewById(R.id.buttonSave)
        buttonClose = findViewById(R.id.buttonClose)

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        userId = firebaseUser?.uid ?: ""
        databaseReference = FirebaseDatabase.getInstance().getReference("Profile").child(userId)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Kiểm tra xem có dữ liệu không
                if (dataSnapshot.exists()) {
                    val profile = dataSnapshot.getValue(profile::class.java)
                    if (profile != null) {
                        // Cập nhật dữ liệu vào EditText
                        editTextName.setText(profile.name)
                        editTextAge.setText(profile.age)
                        editTextPhone.setText(profile.phone)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Xử lý lỗi
            }
        })
        buttonSave.setOnClickListener {
            // Lấy giá trị từ EditText
            val newName = editTextName.text.toString()
            val newAge = editTextAge.text.toString()
            val newPhone = editTextPhone.text.toString()

            // Cập nhật dữ liệu vào Realtime Database
            updateProfile(newName, newAge, newPhone)
        }
        buttonClose.setOnClickListener{
            finish()
        }
    }
    private fun updateProfile(newName: String, newAge: String, newPhone: String) {
        val updatedProfile = profile(newName, newAge, newPhone)

        // Thực hiện cập nhật vào Realtime Database
        databaseReference.setValue(updatedProfile)
            .addOnSuccessListener {
                // Xử lý khi cập nhật thành công
                Toast.makeText(this@DetailProfile, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                // Xử lý khi cập nhật thất bại
                Toast.makeText(this@DetailProfile, "Cập nhật thất bại", Toast.LENGTH_SHORT).show()
            }
    }
}