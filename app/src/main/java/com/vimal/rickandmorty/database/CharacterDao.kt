package com.vimal.rickandmorty.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.vimal.rickandmorty.model.CharacterDto

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: CharacterDto): Long

    @Update
    suspend fun update(note: CharacterDto)

    @Delete
    suspend fun delete(note: CharacterDto)

    @Query("DELETE FROM characterDto_table")
    suspend fun deleteAllNotes()

    @Query("Select * from characterDto_table order by id ASC")
    fun getAllNotes(): LiveData<List<CharacterDto>>
}