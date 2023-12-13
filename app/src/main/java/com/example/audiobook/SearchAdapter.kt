package com.example.audiobook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import audiobook
import com.squareup.picasso.Picasso

class SearchAdapter(private var genres: List<audiobook>) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewSearch : ImageView = itemView.findViewById(R.id.imageViewSearch)
        val textViewTypeName : TextView = itemView.findViewById(R.id.nameSearch)
        val textViewGenreAuthor: TextView = itemView.findViewById(R.id.nameSearchAuthor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val genre = genres[position]
        Picasso.get().load(genre.image).into(holder.imageViewSearch)
        holder.textViewTypeName.text = genre.name
        holder.textViewGenreAuthor.text = genre.author
    }

    override fun getItemCount(): Int {
        return genres.size
    }

    fun setData(newGenres: List<audiobook>) {
        genres = newGenres
        notifyDataSetChanged()
    }
}