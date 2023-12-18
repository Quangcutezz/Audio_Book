package com.example.audiobook.ui.music

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import audiobook
import com.example.audiobook.DetailAdapter

import com.example.audiobook.databinding.FragmentMusicBinding
import com.example.audiobook.play_book
import com.example.audiobook.ui.detail.ViewModel

class MusicFragment : Fragment(),DetailAdapter.OnItemClickListener,DetailAdapter.OnFavoriteItemClickListener,DetailAdapter.OnWaitItemRemoveListener,DetailAdapter.OnWaitItemClickListener{

    private lateinit var binding: FragmentMusicBinding
    private lateinit var tvFavor: TextView
    private lateinit var favorList: RecyclerView
    private lateinit var adapter: DetailAdapter
    private val sharedViewModel: ViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMusicBinding.inflate(inflater, container, false)
        val root: View = binding.root
        tvFavor = binding.tvFavorite
        favorList = binding.favorList


        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the LiveData in MusicFragment
        sharedViewModel.favoritesLiveData.observe(viewLifecycleOwner, Observer { favoritesList ->
            // Update your RecyclerView with the new data
            setupRecyclerView(favorList, favoritesList)
        })
    }
    private fun setupRecyclerView(recyclerView: RecyclerView, favoritesList: List<audiobook>) {
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        adapter = DetailAdapter(favoritesList)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        adapter.setOnItemClickListener(this)
        adapter.setOnFavoriteItemClickListener(this)
        adapter.setOnWaitItemRemoveListener(this)
        adapter.setOnWaitItemClickListener(this)
        // Set item click listener to remove the item from the list
        adapter.setOnFavoriteItemRemoveListener(object : DetailAdapter.OnFavoriteItemRemoveListener {
            override fun onFavoriteItemRemove(item: audiobook) {
                sharedViewModel.removeFavorite(item)
                updateAdapterData(sharedViewModel.favoritesLiveData.value ?: emptyList())
            }
        })
    }
    override fun onItemClick(audiobook: audiobook) {
        val intent = Intent(requireContext(), play_book::class.java)
        intent.putExtra("IMAGE", audiobook.image)
        intent.putExtra("NAME", audiobook.name)
        intent.putExtra("AUTHOR", audiobook.author)
        intent.putExtra("FILE", audiobook.file)
        startActivity(intent)
    }

    override fun onWaitItemRemove(item: audiobook) {
        Toast.makeText(
            requireContext(),
            "You cannot remove favorite items from this page. Please go to the Profile page",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onWaitItemClick(item: audiobook) {
        if (isItemInWait(item)) {
            Toast.makeText(
                requireContext(),
                "Bài này đã được thêm vào danh sách phát rồi",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val waitItem = audiobook(
                name = item.name,
                image = item.image,
                type = item.type,
                author = item.author,
                file = item.file,
                detailPageType = item.detailPageType
            )

            // Set the updated favoritesList in the ViewModel
            sharedViewModel.addToWait(waitItem)
            Toast.makeText(
                requireContext(),
                "Đã thêm thành công vào danh sách phát",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private fun isItemInWait(item: audiobook): Boolean {
        val waitList = sharedViewModel.waitData.value
        return waitList?.any {
            it.name == item.name && it.author == item.author
        } == true
    }
    override fun onFavoriteItemClick(item: audiobook) {
        Toast.makeText(
            requireContext(),
            "Bạn đã thêm audiobook này vào danh sách yêu thích rồi",
            Toast.LENGTH_SHORT
        ).show()
    }
    private fun updateAdapterData(newData: List<audiobook>) {
        adapter.setData(newData)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}

