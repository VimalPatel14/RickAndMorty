package com.vimal.rickandmorty.viewmodel.characterdetails

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vimal.rickandmorty.api.CharacterDetailsRepository
import com.vimal.rickandmorty.api.NetworkState
import com.vimal.rickandmorty.model.Episode
import com.vimal.rickandmorty.model.RickSanchez
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CharacterDetailsViewModel (application: Application,
                                 private val mainRepository: CharacterDetailsRepository) :
    AndroidViewModel(application) {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage
    val movieList = MutableLiveData<RickSanchez>()
    val loading = MutableLiveData<Boolean>()
    val episodeList = MutableLiveData<Episode>()
    val allEpisode: LiveData<List<Episode>> = mainRepository.allEpisode

    fun getCharacter() {
        viewModelScope.launch {
            when (val response = mainRepository.getCharacterDetails()) {
                is NetworkState.Success -> {
                    movieList.postValue(response.data)
                    response.data.episode.forEach { episodeUrl ->

                        val episodeId = episodeUrl.substringAfterLast('/').toInt()
                        getEpisode(episodeId)
                    }
//                    for (i in response.data.episode.indices) {
//                        Log.e("vml", "EpisodesID: $i")
//                        getEpisode(i + 1)
//                    }
                }

                is NetworkState.Error -> {
                    onError("Network Error in getCharacter")
                }
            }
        }
    }

    fun getEpisode(episodeID: Int) {
        viewModelScope.launch {
            when (val response = mainRepository.getEpisode(episodeID)) {
                is NetworkState.Success -> {
                    Log.e("vml", "Success")
                    episodeList.postValue(response.data)
                }

                is NetworkState.Error -> {
                    Log.e("vml", "Error")
                    onError("Network Error in getEpisode $episodeID")
                }
            }
        }
    }

    fun deleteEpisode(note: Episode) = viewModelScope.launch(Dispatchers.IO) {
        mainRepository.delete(note)
    }

    fun deleteAllEpisode() = viewModelScope.launch(Dispatchers.IO) {
        mainRepository.deleteAll()
    }

    fun updateEpisode(note: Episode) = viewModelScope.launch(Dispatchers.IO) {
        mainRepository.update(note)
    }

    fun addEpisode(note: Episode) = viewModelScope.launch(Dispatchers.IO) {
        mainRepository.insert(note)
    }

    private fun onError(message: String) {
        _errorMessage.value = message
        loading.value = false
    }

}