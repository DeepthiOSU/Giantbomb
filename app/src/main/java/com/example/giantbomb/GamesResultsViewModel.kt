package com.example.giantbomb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giantbomb.network.GamesStorage
import com.example.giantbomb.network.Results
import kotlinx.coroutines.launch


class GamesResultsViewModel (private val gamesStorage: GamesStorage) : ViewModel() {
    private val _gameResults: MutableLiveData<List<Results>> = MutableLiveData()
    val gameResults: LiveData<List<Results>>
        get() = _gameResults

    init {
        _gameResults.value = gamesStorage.findAll()
    }

    fun setResults(results : List<Results>) {
        viewModelScope.launch {
            gamesStorage.saveResults(results)
            _gameResults.value = gamesStorage.findAll()
        }
    }
}