package com.example.audiobook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso


class play_book : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_book)

        val button7: Button = findViewById(R.id.button7)
        button7.setOnClickListener {
            finish() // Kết thúc PlayerActivity
        }
        val intent = intent
        val image = intent.getStringExtra("IMAGE")
        val name = intent.getStringExtra("NAME")
        //val author = intent.getStringExtra("AUTHOR")

        val imageView: ImageView = findViewById(R.id.imageView8)
        val textViewName: TextView = findViewById(R.id.textView19)
        //val textViewAuthor: TextView = findViewById(R.id.textViewAuthor)

        Picasso.get().load(image).into(imageView)
        textViewName.text = name
        //textViewAuthor.text = author
    }
}