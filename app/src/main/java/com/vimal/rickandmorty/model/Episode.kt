package com.vimal.rickandmorty.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "episode_table")
data class Episode(
    @PrimaryKey
    val id: Int,
    val name: String,
    val air_date: String,
    val episode: String,
    var characterId: Int?,
)