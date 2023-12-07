package com.example.audiobook.ui.home

import android.graphics.drawable.GradientDrawable.Orientation
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.BiasAbsoluteAlignment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.audiobook.GenreAdapter
import com.example.audiobook.audiobook
import com.example.audiobook.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

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

//        recyclerView.adapter = adapterGenre
//        recyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

//        databaseReference = FirebaseDatabase.getInstance().reference.child("Audio")
//        databaseReference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val genres = mutableListOf<audiobook>()
//                for (dataSnapshot in snapshot.children) {
//                    val image = dataSnapshot.child("image").getValue(String::class.java)?: ""
//                    val name = dataSnapshot.child("name").getValue(String::class.java)?: ""
//                    val author = dataSnapshot.child("author").getValue(String::class.java)?: ""
//                    val type = dataSnapshot.child("type").getValue(String::class.java)?: ""
//                    val genre = audiobook(name, image, type,author)
//                    genres.add(genre)
//                }
//                for (genre in genres) {
//                    Log.d("YourFragment", "Name: ${genre.name}, Image: ${genre.image}, Type: ${genre.type}, Author: ${genre.author}")
//                }
//                adapterGenre.setData(genres)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Xử lý khi có lỗi xảy ra
//                Log.e("YourFragment", "Error fetching data from database", error.toException())
//            }
//        })

        return root
    }
    private fun setupRecyclerView(recyclerView: RecyclerView, databasePath: String) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val adapter = when (databasePath) {
            "Audio" -> GenreAdapter(emptyList())
            "New" -> GenreAdapter(emptyList())
            "OtherDatabase1" -> GenreAdapter(emptyList())
            "OtherDatabase2" -> GenreAdapter(emptyList())
            "OtherDatabase3" -> GenreAdapter(emptyList())
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
                    val genre = audiobook(name, image, type, author)
                    genres.add(genre)
                }
                adapter.setData(genres)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}