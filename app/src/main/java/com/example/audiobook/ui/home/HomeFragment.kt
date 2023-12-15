package com.example.audiobook.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import audiobook
import com.example.audiobook.GenreAdapter

import com.example.audiobook.databinding.FragmentHomeBinding
import com.example.audiobook.ui.detail.DetailFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import androidx.navigation.fragment.findNavController
import com.example.audiobook.R
import com.example.audiobook.play_book


class HomeFragment : Fragment() {
    interface OnItemClickListener {
        fun onItemClick(audiobook: audiobook)
    }
    private var onItemClickListener: OnItemClickListener? = null


    private lateinit var binding: FragmentHomeBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var recyclerView3: RecyclerView
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.rvBest
        recyclerView2 = binding.rvRecent


        //adapterGenre = GenreAdapter(emptyList())
        setupRecyclerView(recyclerView, "Audio")
        setupRecyclerView(recyclerView2, "New")

        return root
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, databasePath: String) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val adapter = when (databasePath) {
            "Audio" -> GenreAdapter(emptyList())
            "New" -> GenreAdapter(emptyList())
            "ForYou" -> GenreAdapter(emptyList())
            "Top" -> GenreAdapter(emptyList())
            "Author" -> GenreAdapter(emptyList())
            else -> GenreAdapter(emptyList())
        }

        recyclerView.adapter = adapter


        val databaseReference = FirebaseDatabase.getInstance().reference.child(databasePath)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val genres = mutableListOf<audiobook>()
                for (dataSnapshot in snapshot.children) {
                    val image = dataSnapshot.child("image").getValue(String::class.java) ?: ""
                    val name = dataSnapshot.child("name").getValue(String::class.java) ?: ""
                    val author = dataSnapshot.child("author").getValue(String::class.java) ?: ""
                    val type = dataSnapshot.child("type").getValue(String::class.java) ?: ""
                    val file = dataSnapshot.child("file").getValue(String::class.java) ?: ""
                    val detailPageType = dataSnapshot.child("detailPageType").getValue(String::class.java) ?: ""
                    val genre = audiobook(name, image, type, author,file,detailPageType)
                    genres.add(genre)
                }
                adapter.setData(genres)

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
        adapter.setOnItemClickListener(object : GenreAdapter.OnItemClickListener {
            override fun onItemClick(audiobook: audiobook) {

                // Lấy ID hoặc loại dữ liệu liên quan từ audiobook
                val detailPageType = audiobook.detailPageType

                val bundle = Bundle()
                bundle.putSerializable("ARG_AUDIOBOOK", audiobook)
                bundle.putString("ARG_DETAIL_PAGE_TYPE", detailPageType)

                val detailFragment2 = DetailFragment()
                detailFragment2.arguments = bundle
                // code để chuyển đến DetailFragment
                val fragmentManager = requireActivity().supportFragmentManager
                val transaction = fragmentManager.beginTransaction()

                val homeFragment = fragmentManager.findFragmentById(R.id.container)
                homeFragment?.let {
                    transaction.remove(it)
                }


                val detailFragment = DetailFragment.newInstance(audiobook) // Truyền dữ liệu cần thiết nếu cần
                transaction.add(R.id.container, detailFragment)

                transaction.addToBackStack(null) // Để có thể sử dụng nút back để quay lại HomeFragment
                transaction.commit()
            }
        })
    }
}