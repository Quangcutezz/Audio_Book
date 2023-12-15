package com.example.audiobook.ui.music

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import audiobook
import com.example.audiobook.DetailAdapter

import com.example.audiobook.databinding.FragmentMusicBinding
import com.example.audiobook.ui.detail.ViewModel

class MusicFragment : Fragment(){

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
        // Set item click listener to remove the item from the list
        adapter.setOnFavoriteItemRemoveListener(object : DetailAdapter.OnFavoriteItemRemoveListener {
            override fun onFavoriteItemRemove(item: audiobook) {
                sharedViewModel.removeFavorite(item)
                updateAdapterData(sharedViewModel.favoritesLiveData.value ?: emptyList())
            }
        })
    }
    private fun updateAdapterData(newData: List<audiobook>) {
        adapter.setData(newData)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}

