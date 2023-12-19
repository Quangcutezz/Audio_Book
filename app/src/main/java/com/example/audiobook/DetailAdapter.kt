package com.example.audiobook

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.Button
import android.widget.PopupMenu
import androidx.core.content.ContentProviderCompat.requireContext
import audiobook
import com.squareup.picasso.Picasso

class DetailAdapter(private var genres: List<audiobook>) : RecyclerView.Adapter<DetailAdapter.ViewHolder>() {
    private var onItemClickListener: OnItemClickListener? = null
    private var onFavoriteItemClickListener: OnFavoriteItemClickListener? = null
    private var onFavoriteItemRemoveListener: OnFavoriteItemRemoveListener? = null
    private var onWaitItemClickListener: OnWaitItemClickListener? = null
    private var onWaitItemRemoveListener: OnWaitItemRemoveListener? = null
    private var onDownloadItemClickListener: OnDownloadItemClickListener? = null
    interface OnDownloadItemClickListener{
        fun onDownloadItemClick(item: audiobook)
    }
    interface OnWaitItemRemoveListener{
        fun onWaitItemRemove(item: audiobook)
    }
    interface OnWaitItemClickListener{
        fun onWaitItemClick(item: audiobook)
    }

    interface OnItemClickListener {
        fun onItemClick(audiobook: audiobook)
    }
    interface OnFavoriteItemClickListener {
        fun onFavoriteItemClick(item: audiobook)
    }
    interface OnFavoriteItemRemoveListener {
        fun onFavoriteItemRemove(item: audiobook)
    }
    fun removeItem(item: audiobook) {
        val updatedList = genres.toMutableList()
        updatedList.remove(item)
        genres = updatedList
        notifyDataSetChanged()
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewGenreIcon: ImageView = itemView.findViewById(R.id.imageViewDetail)
        val textViewTypeName: TextView = itemView.findViewById(R.id.nameDetail)
        val textViewGenreAuthor: TextView = itemView.findViewById(R.id.nameDetailAuthor)
        val buttonOption: Button = itemView.findViewById(R.id.buttonOption)
        init {
            // Thêm sự kiện onClick cho itemView
            itemView.setOnClickListener {
                onItemClickListener?.onItemClick(genres[adapterPosition])
            }
            buttonOption.setOnClickListener {
                showPopupMenu(buttonOption, genres[adapterPosition])
            }

        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail, parent, false)
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
    fun setOnDownloadItemClickListnener(listener: OnDownloadItemClickListener){
        this.onDownloadItemClickListener = listener
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }
    fun setOnFavoriteItemClickListener(listener: OnFavoriteItemClickListener) {
        this.onFavoriteItemClickListener = listener
    }
    fun setOnFavoriteItemRemoveListener(listener: OnFavoriteItemRemoveListener) {
        this.onFavoriteItemRemoveListener = listener
    }
    fun setOnWaitItemClickListener(listener: OnWaitItemClickListener){
        this.onWaitItemClickListener = listener
    }
    fun setOnWaitItemRemoveListener(listener: OnWaitItemRemoveListener){
        this.onWaitItemRemoveListener = listener
    }
    fun setData(newGenres: List<audiobook>) {
        genres = newGenres
        notifyDataSetChanged()
    }
    private fun showPopupMenu(view: View, item: audiobook) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.option_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_item_1 -> {
                    // Do something for menu item 1
                    onFavoriteItemClickListener?.onFavoriteItemClick(item)
                    true
                }
                R.id.menu_item_2 ->{
                    onFavoriteItemRemoveListener?.onFavoriteItemRemove(item)
                    true
                }
                R.id.menu_item_3 ->{
                    onWaitItemClickListener?.onWaitItemClick(item)
                    true
                }
                R.id.menu_item_4 ->{
                    onWaitItemRemoveListener?.onWaitItemRemove(item)
                    true
                }
                R.id.menu_item_5 ->{
                    onDownloadItemClickListener?.onDownloadItemClick(item)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }


    override fun getItemCount(): Int {
        return genres.size
    }
}