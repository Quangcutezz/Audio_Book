package com.example.audiobook

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import android.view.LayoutInflater
import audiobook
import com.squareup.picasso.Picasso
// GenreAdapter.kt

class GenreAdapter(private var genres: List<audiobook>) : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {
    private var onItemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(audiobook: audiobook)
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewGenreIcon: ImageView = itemView.findViewById(R.id.imageViewGenreIcon)
        val textViewTypeName: TextView = itemView.findViewById(R.id.textViewTypeName)
        val textViewGenreAuthor: TextView = itemView.findViewById(R.id.textViewGenreAuthor)
        init {
            // Thêm sự kiện onClick cho itemView
            itemView.setOnClickListener {
                onItemClickListener?.onItemClick(genres[adapterPosition])
            }
        }
    }



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_genre, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val genre = genres[position]

        Picasso.get().load(genre.image).into(holder.imageViewGenreIcon)
        // Set icon và tên type
        holder.textViewTypeName.text = genre.name
        holder.textViewGenreAuthor.text = genre.author
//        holder.itemView.setOnClickListener {
//            onItemClickListener?.onItemClick(genre)
//        }
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }
    fun setData(newGenres: List<audiobook>) {
        genres = newGenres
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return genres.size
    }
}
