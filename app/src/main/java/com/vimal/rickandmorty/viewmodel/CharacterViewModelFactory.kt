package com.vimal.rickandmorty.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vimal.rickandmorty.api.CharacterRepository

class CharacterViewModelFactory (private val application: Application,
                                 private val repository: CharacterRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CharacterViewModel::class.java)) {
            CharacterViewModel(application,this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}