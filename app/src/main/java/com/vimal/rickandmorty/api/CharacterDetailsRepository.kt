package com.vimal.rickandmorty.api

import androidx.lifecycle.LiveData
import com.vimal.rickandmorty.database.EpisodeDao
import com.vimal.rickandmorty.model.Episode
import com.vimal.rickandmorty.model.RickSanchez

class CharacterDetailsRepository (
    private val retrofitService: RetrofitService,
    private val notesDao: EpisodeDao,
    private val id: Int
) {
    suspend fun getCharacterDetails(): NetworkState<RickSanchez> {
        val response = retrofitService.getCharacterDetails(id)
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

    suspend fun getEpisode(episodeID: Int): NetworkState<Episode> {
        val response = retrofitService.getEpisode(episodeID)
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

    val allEpisode: LiveData<List<Episode>> = notesDao.getAllEpisode(id)

    suspend fun insert(note: Episode) {
        notesDao.insert(note)
    }

    suspend fun delete(note: Episode) {
        notesDao.delete(note)
    }

    suspend fun deleteAll() {
        notesDao.deleteAllEpisode()
    }

    suspend fun update(note: Episode) {
        notesDao.update(note)
    }
}