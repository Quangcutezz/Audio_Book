package com.example.audiobook
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import FeaturedItem
import android.widget.SearchView
class Home_page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.queryHint = "Search..."

        val featuredList = mutableListOf<FeaturedItem>()
        // Thêm các mục vào danh sách featuredList (dữ liệu mẫu)
        featuredList.add(FeaturedItem(R.drawable.rectangle_20, "Title 1"))
        featuredList.add(FeaturedItem(R.drawable.rectangle_20, "Title 2"))
        // Thêm dữ liệu khác nếu cần
        val featuredlist2 = mutableListOf<FeaturedItem>()
        featuredlist2.add(FeaturedItem(R.drawable.rectangle_15_2, "Title 3"))
        featuredlist2.add(FeaturedItem(R.drawable.rectangle_15_3, "Title 4"))
        featuredlist2.add(FeaturedItem(R.drawable.rectangle_15_2, "Title 5"))
        featuredlist2.add(FeaturedItem(R.drawable.rectangle_15_3, "Title 6"))
        featuredlist2.add(FeaturedItem(R.drawable.rectangle_15_2, "Title 7"))
        featuredlist2.add(FeaturedItem(R.drawable.rectangle_15_3, "Title 8"))

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = FeaturedAdapter(featuredList)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        val recyclerView2 = findViewById<RecyclerView>(R.id.recyclerView2)
        val adapter2 = FeaturedAdapter2(featuredlist2)
        recyclerView2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView2.adapter = adapter2

        val recyclerView3 = findViewById<RecyclerView>(R.id.recyclerView3)
        val adapter3 = FeaturedAdapter2(featuredlist2)
        recyclerView3.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView3.adapter = adapter3

        val recyclerView4 = findViewById<RecyclerView>(R.id.recyclerView4)
        val adapter4 = FeaturedAdapter2(featuredlist2)
        recyclerView4.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView4.adapter = adapter4

        val recyclerView5 = findViewById<RecyclerView>(R.id.recyclerView5)
        val adapter5 = FeaturedAdapter2(featuredlist2)
        recyclerView5.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView5.adapter = adapter5

    }
}