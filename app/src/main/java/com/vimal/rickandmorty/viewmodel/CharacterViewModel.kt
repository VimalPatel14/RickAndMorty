package com.vimal.rickandmorty.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vimal.rickandmorty.api.CharacterRepository
import com.vimal.rickandmorty.api.NetworkState
import com.vimal.rickandmorty.model.CharacterDto
import com.vimal.rickandmorty.model.CharacterListResponse
import kotlinx.coroutines.*

class CharacterViewModel(
    application: Application,
    private val characterRepository: CharacterRepository
) : AndroidViewModel(application) {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    val characterList = MutableLiveData<CharacterListResponse>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    val loading = MutableLiveData<Boolean>()
    val allNotes: LiveData<List<CharacterDto>> = characterRepository.allNotes


    fun getAllCharacter(page:Int) {
        viewModelScope.launch(exceptionHandler) {
            when (val response = characterRepository.getAllCharacter(page)) {
                is NetworkState.Success -> {
                    characterList.postValue(response.data)
                }
                is NetworkState.Error -> {
                    onError("Network Error")
                }
            }
        }
    }

    fun deleteNote(note: CharacterDto) = viewModelScope.launch(Dispatchers.IO) {
        characterRepository.delete(note)
    }

    fun deleteAllNote() = viewModelScope.launch(Dispatchers.IO) {
        characterRepository.deleteAll()
    }

    fun updateNote(note: CharacterDto) = viewModelScope.launch(Dispatchers.IO) {
        characterRepository.update(note)
    }

    fun addNote(note: CharacterDto) = viewModelScope.launch(Dispatchers.IO) {
        characterRepository.insert(note)
    }

    private fun onError(message: String) {
        _errorMessage.value = message
        loading.value = false
    }
}