package com.example.audiobook

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.audiobook.ui.profile.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso

class DetailProfile : AppCompatActivity() {
    private lateinit var editTextName: EditText
    private lateinit var editTextAge: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId: String
    private lateinit var buttonSave: Button
    private lateinit var buttonClose: Button
    private lateinit var buttonEditImage: Button
    private lateinit var picProfile: RoundedImageView
    private var imageUrl: String = ""

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageUri: Uri? = result.data?.data
                // Xử lý việc tải ảnh lên Firebase Storage và nhận URL mới
                if (imageUri != null) {
                    uploadImageToStorage(imageUri)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_profile)
        editTextName = findViewById(R.id.editTextName)
        editTextAge = findViewById(R.id.editTextAge)
        editTextPhone = findViewById(R.id.editTextPhone)
        buttonSave = findViewById(R.id.buttonSave)
        buttonClose = findViewById(R.id.buttonClose)
        buttonEditImage = findViewById(R.id.editImage)
        picProfile = findViewById(R.id.picProfile)

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
                        if (profile.image.isNotEmpty()) {
                            Picasso.get().load(profile.image).into(picProfile)
                        }
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
            updateProfile(newName, newAge, newPhone,imageUrl)
        }
        buttonClose.setOnClickListener {
            finish()
        }
        buttonEditImage.setOnClickListener {
            // Chọn ảnh từ thư viện
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            getContent.launch(intent)
        }
    }

    private fun updateProfile(newName: String, newAge: String, newPhone: String, imageUrl: String) {
        val updatedProfile = profile()
        updatedProfile.name = newName
        updatedProfile.age = newAge
        updatedProfile.phone = newPhone
        updatedProfile.image = imageUrl

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




    private fun uploadImageToStorage(imageUri: Uri) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference

        // Tạo một reference đến ảnh trên Firebase Storage (sử dụng UID của người dùng làm tên file)
        val imageRef = storageRef.child("images/$userId/profile_image.jpg")

        // Tải ảnh lên Firebase Storage
        val uploadTask = imageRef.putFile(imageUri)

        // Lắng nghe sự kiện khi tải lên thành công hoặc thất bại
        uploadTask.addOnSuccessListener { taskSnapshot ->
            // Lấy URL của ảnh từ Firebase Storage
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                // Cập nhật URL mới vào Realtime Database
                updateImageUrl(uri.toString())
            }
        }.addOnFailureListener { exception ->
            // Xử lý khi tải lên thất bại
            Toast.makeText(this, "Tải ảnh lên thất bại", Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateImageUrl(imageUrl: String) {
        // Lưu URL vào biến imageUrl
        this.imageUrl = imageUrl

        databaseReference.child("image").setValue(imageUrl)
            .addOnSuccessListener {
                // Xử lý khi cập nhật URL ảnh mới thành công
                Toast.makeText(this@DetailProfile, "Cập nhật ảnh thành công", Toast.LENGTH_SHORT).show()

                // Gọi hàm cập nhật dữ liệu Profile (bao gồm cả ảnh) trong Realtime Database
                updateProfile(editTextName.text.toString(), editTextAge.text.toString(), editTextPhone.text.toString(), imageUrl)
            }
            .addOnFailureListener {
                // Xử lý khi cập nhật URL ảnh mới thất bại
                Toast.makeText(this@DetailProfile, "Cập nhật ảnh thất bại", Toast.LENGTH_SHORT).show()
            }
    }



}