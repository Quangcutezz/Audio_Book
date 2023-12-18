package com.example.audiobook.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import audiobook
import com.example.audiobook.DetailAdapter
import com.example.audiobook.DetailProfile
import com.example.audiobook.R
import com.example.audiobook.databinding.FragmentHomeBinding
import com.example.audiobook.databinding.FragmentProfileBinding
import com.example.audiobook.play_book
import com.example.audiobook.profile
import com.example.audiobook.ui.detail.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment(),DetailAdapter.OnWaitItemClickListener,DetailAdapter.OnItemClickListener,DetailAdapter.OnFavoriteItemClickListener,DetailAdapter.OnFavoriteItemRemoveListener {
    private var _binding: FragmentProfileBinding? = null
    private lateinit var editButton :Button
    private lateinit var name: TextView
    private lateinit var adapter: DetailAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId: String
    private lateinit var playListRecyclerView: RecyclerView
    private val sharedViewModel: ViewModel by activityViewModels()
    private lateinit var imageProfile: RoundedImageView
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        editButton = binding.editBtn
        name = binding.name
        imageProfile = binding.imageProfile
        playListRecyclerView = binding.audioProfileList
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
                        if (profile.image.isNotEmpty()) {
                            updateProfileImage(profile.image)
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Xử lý lỗi
            }
        })
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the LiveData in MusicFragment
        sharedViewModel.waitData.observe(viewLifecycleOwner, Observer { waitList ->
            // Update your RecyclerView with the new data
            setupRecyclerView(playListRecyclerView, waitList)
        })
    }
    private fun setupRecyclerView(recyclerView: RecyclerView, waitList: List<audiobook>) {
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        adapter = DetailAdapter(waitList)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        adapter.setOnItemClickListener(this)
        adapter.setOnFavoriteItemClickListener(this)
        // Set item click listener to remove the item from the list
        adapter.setOnFavoriteItemRemoveListener(this)
        adapter.setOnWaitItemRemoveListener(object : DetailAdapter.OnWaitItemRemoveListener {
            override fun onWaitItemRemove(item: audiobook) {
                sharedViewModel.removeWait(item)
                updateAdapterData(sharedViewModel.waitData.value ?: emptyList())
            }
        })
    }
    fun updateProfileImage(imageUrl: String) {
        // Hiển thị ảnh mới trong Picasso
        Picasso.get().load(imageUrl).into(imageProfile)
    }

    private fun updateAdapterData(newData: List<audiobook>) {
        adapter.setData(newData)
    }
    override fun onWaitItemClick(item: audiobook) {
        Toast.makeText(
            requireContext(),
            "Bạn đã thêm audiobook này vào danh sách phát rồi",
            Toast.LENGTH_SHORT
        ).show()
    }
    override fun onItemClick(audiobook: audiobook) {
        val intent = Intent(requireContext(), play_book::class.java)
        intent.putExtra("IMAGE", audiobook.image)
        intent.putExtra("NAME", audiobook.name)
        intent.putExtra("AUTHOR", audiobook.author)
        intent.putExtra("FILE", audiobook.file)
        startActivity(intent)
    }
    override fun onFavoriteItemRemove(item: audiobook) {
        Toast.makeText(
            requireContext(),
            "You cannot remove favorite items from this page. Please go back to the Music page",
            Toast.LENGTH_SHORT
        ).show()
    }
    override fun onFavoriteItemClick(item: audiobook) {
        if (isItemInFavorites(item)) {
            Toast.makeText(
                requireContext(),
                "Bài này đã được thêm vào danh sách yêu thích rồi",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val favoriteItem = audiobook(
                name = item.name,
                image = item.image,
                type = item.type,
                author = item.author,
                file = item.file,
                detailPageType = item.detailPageType
            )

            // Set the updated favoritesList in the ViewModel
            sharedViewModel.addToFavorites(favoriteItem)
            Toast.makeText(
                requireContext(),
                "Đã thêm thành công vào danh sách yêu thích",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private fun isItemInFavorites(item: audiobook): Boolean {
        val favoritesList = sharedViewModel.favoritesLiveData.value
        return favoritesList?.any {
            it.name == item.name && it.author == item.author
        } == true
    }
}