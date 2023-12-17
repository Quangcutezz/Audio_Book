package com.example.audiobook.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.audiobook.DetailProfile
import com.example.audiobook.R
import com.example.audiobook.databinding.FragmentHomeBinding
import com.example.audiobook.databinding.FragmentProfileBinding
import com.example.audiobook.play_book
import com.example.audiobook.profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private lateinit var editButton :Button
    private lateinit var name: TextView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId: String
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        editButton = binding.editBtn
        name = binding.name
        editButton.setOnClickListener{
            val intent = Intent(requireContext(), DetailProfile::class.java)
            startActivity(intent)
        }
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
                        name.setText(profile.name)

                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Xử lý lỗi
            }
        })
        return root
    }


}