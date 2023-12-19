package com.example.audiobook.ui.detail
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import audiobook

class ViewModel:ViewModel() {
    private val _favoritesLiveData = MutableLiveData<List<audiobook>>()
    val favoritesLiveData: LiveData<List<audiobook>> get() = _favoritesLiveData

    private val _removeFavoriteEvent = MutableLiveData<audiobook>()
    val removeFavoriteEvent: LiveData<audiobook> get() = _removeFavoriteEvent

    private val _waitData = MutableLiveData<List<audiobook>>()
    val waitData : LiveData<List<audiobook>> get() = _waitData

    private val _historyData = MutableLiveData<List<audiobook>>()
    val historyData : LiveData<List<audiobook>> get() = _historyData

    fun addToHistory(item: audiobook){
        val currentList = _historyData.value ?: emptyList()
        val updatedList = currentList.toMutableList().apply { add(item) }
        _historyData.value = updatedList
    }
    fun addToFavorites(item: audiobook) {
        val currentList = _favoritesLiveData.value ?: emptyList()
        val updatedList = currentList.toMutableList().apply { add(item) }
        _favoritesLiveData.value = updatedList
    }
    fun removeFavorite(item: audiobook) {
        val currentList = _favoritesLiveData.value ?: emptyList()
        val updatedList = currentList.toMutableList().apply { remove(item) }
        _favoritesLiveData.value = updatedList
    }
    fun addToWait(item: audiobook){
        val currentList = _waitData.value ?: emptyList()
        val updatedList = currentList.toMutableList().apply { add(item) }
        _waitData.value = updatedList
    }
    fun removeWait(item: audiobook) {
        val currentList = _waitData.value ?: emptyList()
        val updatedList = currentList.toMutableList().apply { remove(item) }
        _waitData.value = updatedList
    }
}