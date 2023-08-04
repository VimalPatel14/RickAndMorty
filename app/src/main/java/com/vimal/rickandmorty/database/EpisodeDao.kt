package com.vimal.rickandmorty.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.vimal.rickandmorty.model.Episode

@Dao
interface EpisodeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Episode): Long

    @Update
    suspend fun update(note: Episode)

    @Delete
    suspend fun delete(note: Episode)

    @Query("DELETE FROM episode_table")
    suspend fun deleteAllEpisode()

    @Query("Select * from episode_table WHERE characterId = :idToFind order by id ASC")
    fun getAllEpisode(idToFind: Int): LiveData<List<Episode>>
}