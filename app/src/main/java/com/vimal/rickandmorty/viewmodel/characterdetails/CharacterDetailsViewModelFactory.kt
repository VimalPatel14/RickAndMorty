package com.vimal.rickandmorty.viewmodel.characterdetails

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vimal.rickandmorty.api.CharacterDetailsRepository

class CharacterDetailsViewModelFactory (private val application: Application,
                                        private val repository: CharacterDetailsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CharacterDetailsViewModel::class.java)) {
            CharacterDetailsViewModel(application,this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}