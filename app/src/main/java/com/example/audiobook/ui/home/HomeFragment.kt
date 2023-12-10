package com.example.audiobook.ui.home

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


class HomeFragment : Fragment() {
    interface OnItemClickListener {
        fun onItemClick(audiobook: audiobook)
    }
    private var onItemClickListener: OnItemClickListener? = null


    private lateinit var binding: FragmentHomeBinding

    // This property is only valid between onCreateView and
    // onDestroyView.

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var recyclerView3: RecyclerView
    private lateinit var recyclerView4: RecyclerView
    private lateinit var recyclerView5: RecyclerView

    private lateinit var adapterGenre: GenreAdapter
    private lateinit var adapterGenre2: GenreAdapter
    private lateinit var adapterGenre3: GenreAdapter
    private lateinit var adapterGenre4: GenreAdapter
    private lateinit var adapterGenre5: GenreAdapter

    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseReference2: DatabaseReference
    private lateinit var databaseReference3: DatabaseReference
    private lateinit var databaseReference4: DatabaseReference
    private lateinit var databaseReference5: DatabaseReference
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
        recyclerView3 = binding.rvForYou
        recyclerView4 = binding.rvTop
        recyclerView5 = binding.rvAuthor


        //adapterGenre = GenreAdapter(emptyList())
        setupRecyclerView(recyclerView, "Audio")
        setupRecyclerView(recyclerView2, "New")
        setupRecyclerView(recyclerView3, "ForYou")
        setupRecyclerView(recyclerView4, "Top")
        setupRecyclerView(recyclerView5, "Author")

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
                    val genre = audiobook(name, image, type, author,file)
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