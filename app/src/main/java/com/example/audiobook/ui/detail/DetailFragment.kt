package com.example.audiobook.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import audiobook
import com.example.audiobook.R
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.audiobook.DetailAdapter
import com.example.audiobook.databinding.FragmentDetailBinding
import com.example.audiobook.databinding.FragmentHomeBinding
import com.example.audiobook.play_book
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailFragment : Fragment(), DetailAdapter.OnItemClickListener {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var detailListView1: RecyclerView
    private lateinit var detailListView2: RecyclerView
    private lateinit var detailListView3: RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).findViewById<BottomNavigationView>(R.id.nav_view)
            .visibility = View.GONE
        val backButton = view.findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            // Thực hiện logic khi ấn nút back
            (requireActivity() as AppCompatActivity).findViewById<BottomNavigationView>(R.id.nav_view)
                .visibility = View.VISIBLE
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
    companion object {
        private const val ARG_AUDIOBOOK = "arg_audiobook"

        fun newInstance(audiobook: audiobook): DetailFragment {
            val fragment = DetailFragment()
            val args = Bundle()
            args.putSerializable(ARG_AUDIOBOOK, audiobook)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root
        detailListView1 =binding.detailList1
        detailListView2 =binding.detailList2
        detailListView3 =binding.detailList3

        setupRecyclerView(detailListView1, "Audio")
        setupRecyclerView(detailListView2, "New")
        setupRecyclerView(detailListView3, "ForYou")
        return root
    }
    private fun setupRecyclerView(recyclerView: RecyclerView, databasePath: String) {
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val adapter = when (databasePath) {
            "Audio" -> DetailAdapter(emptyList())
            "New" -> DetailAdapter(emptyList())
            "ForYou" -> DetailAdapter(emptyList())
//            "Top" -> GenreAdapter(emptyList())
//            "Author" -> GenreAdapter(emptyList())
            else -> DetailAdapter(emptyList())
        }

        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(this) // Đặt người nghe nhấp vào

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
    // Triển khai phương thức onItemClick
    override fun onItemClick(audiobook: audiobook) {
        // Khởi chạy PlayerActivity với audiobook được chọn
        val intent = Intent(requireContext(), play_book::class.java)
        //intent.putExtra("AUDIOBOOK", audiobook)
        intent.putExtra("IMAGE", audiobook.image)
        intent.putExtra("NAME", audiobook.name)
        intent.putExtra("AUTHOR", audiobook.author)
        startActivity(intent)
    }
}