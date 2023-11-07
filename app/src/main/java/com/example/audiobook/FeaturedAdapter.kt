package com.example.audiobook
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.ImageView
import FeaturedItem
class FeaturedAdapter(private val items: List<FeaturedItem>) : RecyclerView.Adapter<FeaturedAdapter.FeaturedViewHolder>() {

    inner class FeaturedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.featured_item_layout, parent, false)
        return FeaturedViewHolder(view)
    }


    override fun onBindViewHolder(holder: FeaturedViewHolder, position: Int) {
        val item = items[position]
        // Gán dữ liệu từ item vào các view trong layout item
        holder.imageView.setImageResource(item.imageResource)
        // Gán dữ liệu cho các view khác nếu có
    }

    override fun getItemCount() = items.size
}
