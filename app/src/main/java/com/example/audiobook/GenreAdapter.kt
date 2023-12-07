package com.example.audiobook

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.squareup.picasso.Picasso
// GenreAdapter.kt
class GenreAdapter(private var genres: List<audiobook>) : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewGenreIcon: ImageView = itemView.findViewById(R.id.imageViewGenreIcon)
        val textViewTypeName: TextView = itemView.findViewById(R.id.textViewTypeName)
        val textViewGenreAuthor: TextView = itemView.findViewById(R.id.textViewGenreAuthor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_genre, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val genre = genres[position]

        Picasso.get().load(genre.image).into(holder.imageViewGenreIcon)
        // Set icon và tên thể loại
        holder.textViewTypeName.text = genre.name
        holder.textViewGenreAuthor.text = genre.author
    }
    fun setData(newGenres: List<audiobook>) {
        genres = newGenres
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return genres.size
    }
}
