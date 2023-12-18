package com.example.audiobook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import audiobook
import com.example.audiobook.ui.detail.DetailFragment
import com.example.audiobook.ui.detail.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Searching : AppCompatActivity(), SearchAdapter.OnItemClickListener{
    private lateinit var buttonBack: Button
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var query: String
    private lateinit var query2: String
    private lateinit var adapter: SearchAdapter
    private lateinit var tv1: TextView
    private lateinit var tv2: TextView
    private lateinit var tv3: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searching)

        buttonBack = findViewById(R.id.buttonBack)
        buttonBack.setOnClickListener {
            finish()
        }
        searchView = findViewById(R.id.searchAudio)
        searchRecyclerView = findViewById(R.id.searchList)
//        setupRecyclerView(searchRecyclerView,"allAudio")

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query2 = query.orEmpty().toLowerCase()
                searchInDatabase(searchRecyclerView,query2)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Cập nhật giá trị query khi người dùng nhập vào searchView
                query = newText.orEmpty().toLowerCase()
                //Log.d("Searching", "Query: $query")
                // Gọi hàm tìm kiếm với query mới
                searchInDatabase(searchRecyclerView,query)
                return true
            }
        })
    }

    private fun searchInDatabase(recyclerView: RecyclerView,query: String) {
    recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = SearchAdapter(emptyList())
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(this)
        if (query.isNotEmpty()) {
            val databaseReference = FirebaseDatabase.getInstance().reference.child("allAudio")

            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val genres = mutableListOf<audiobook>()
                    for (dataSnapshot in snapshot.children) {
                        // Kiểm tra xem query có khớp với bất kỳ trường nào không
                        val name = dataSnapshot.child("name").getValue(String::class.java) ?: ""
                        val author = dataSnapshot.child("author").getValue(String::class.java) ?: ""
                        val type = dataSnapshot.child("type").getValue(String::class.java) ?: ""

                        Log.d("Searching", "Name: $name, Author: $author, Type: $type")

                        if (name?.contains(query, ignoreCase = true) == true ||
                            author?.contains(query, ignoreCase = true) == true ||
                            type?.contains(query, ignoreCase = true) == true
                        ) {
                            // Nếu khớp, thêm vào danh sách genres
                            val image = dataSnapshot.child("image").getValue(String::class.java) ?: ""
                            val file = dataSnapshot.child("file").getValue(String::class.java) ?: ""
                            val detailPageType = dataSnapshot.child("detailPageType").getValue(String::class.java) ?: ""
                            val genre = audiobook(name, image, type, author, file, detailPageType)
                            genres.add(genre)
                        }
                    }
                    // Cập nhật dữ liệu vào RecyclerView khi có kết quả tìm kiếm
                    adapter.setData(genres)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                   // Log.e("Searching", "Database error: ${error.message}")
                }
            })
        } else {
            // Xử lý khi query rỗng
            adapter.setData(emptyList())
        }
    }
    override fun onItemClick(audiobook: audiobook) {
        val intent = Intent(this, play_book::class.java)
        intent.putExtra("IMAGE", audiobook.image)
        intent.putExtra("NAME", audiobook.name)
        intent.putExtra("AUTHOR", audiobook.author)
        intent.putExtra("FILE", audiobook.file)
        startActivity(intent)
    }

}
