package com.example.audiobook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import audiobook
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailType : AppCompatActivity(), GenreAdapter.OnItemClickListener {
    private lateinit var rvType: RecyclerView
    private lateinit var rvMore: RecyclerView
    private lateinit var adapter:GenreAdapter
    private lateinit var buttonBack: Button
    private lateinit var tvType: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_type)
        buttonBack = findViewById(R.id.btnBack)
        buttonBack.setOnClickListener{
            finish()
        }
        tvType = findViewById(R.id.tvType)
        rvType = findViewById(R.id.rvType)
        rvMore = findViewById(R.id.rvMore)
        val buttonId = intent.getIntExtra("buttonId",1)
            if(buttonId == 1) {
                tvType.setText("#Tình Yêu")
                setupRecyclerView(rvType, "tinh_yeu")
                setupRecyclerView(rvMore, "kinh_doanh")
            }
            else if(buttonId == 2){
                tvType.setText("#Kinh Doanh")
                setupRecyclerView(rvType, "kinh_doanh")
                setupRecyclerView(rvMore, "chien_tranh")
            } else if(buttonId == 3) {
                tvType.setText("#Chiến Tranh")
                setupRecyclerView(rvType, "chien_tranh")
                setupRecyclerView(rvMore, "kinh_doanh")
            }else if(buttonId == 4) {
                tvType.setText("#Cuộc Sống")
                setupRecyclerView(rvType, "cuoc_song")
                setupRecyclerView(rvMore, "tinh_yeu")
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, query:String) {
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val adapter = when (query) {
            "tinh_yeu" -> GenreAdapter(emptyList())
            "kinh_doanh" -> GenreAdapter(emptyList())
            "chien_tranh" -> GenreAdapter(emptyList())
            "cuoc_song" -> GenreAdapter(emptyList())
            else -> GenreAdapter(emptyList())
        }

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