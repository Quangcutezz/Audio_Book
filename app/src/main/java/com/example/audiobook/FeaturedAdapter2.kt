package com.example.audiobook
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.ImageView
import FeaturedItem
class FeaturedAdapter2(private val items: List<FeaturedItem>) : RecyclerView.Adapter<FeaturedAdapter2.FeaturedViewHolder2>() {

    inner class FeaturedViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView2 : ImageView = itemView.findViewById(R.id.imageView2)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedViewHolder2 {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.featured_item_layout2, parent, false)
        return FeaturedViewHolder2(view)
    }


    override fun onBindViewHolder(holder: FeaturedViewHolder2, position: Int) {
        val item = items[position]
        // Gán dữ liệu từ item vào các view trong layout item
        holder.imageView2.setImageResource(item.imageResource)
        // Gán dữ liệu cho các view khác nếu có
    }

    override fun getItemCount() = items.size
}
