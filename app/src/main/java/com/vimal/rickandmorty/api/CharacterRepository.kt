package com.vimal.rickandmorty.api

import androidx.lifecycle.LiveData
import com.vimal.rickandmorty.database.CharacterDao
import com.vimal.rickandmorty.model.CharacterDto
import com.vimal.rickandmorty.model.CharacterListResponse

class CharacterRepository (
    private val retrofitService: RetrofitService,
    private val characterDao: CharacterDao
) {
    suspend fun getAllCharacter(page: Int): NetworkState<CharacterListResponse> {
        val response = retrofitService.getAllCharacter(page)
        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                NetworkState.Success(responseBody)
            } else {
                NetworkState.Error(response)
            }
        } else {
            NetworkState.Error(response)
        }
    }

    val allNotes: LiveData<List<CharacterDto>> = characterDao.getAllNotes()

    suspend fun insert(note: CharacterDto) {
        characterDao.insert(note)
    }

    suspend fun delete(note: CharacterDto) {
        characterDao.delete(note)
    }

    suspend fun deleteAll() {
        characterDao.deleteAllNotes()
    }

    suspend fun update(note: CharacterDto) {
        characterDao.update(note)
    }
}