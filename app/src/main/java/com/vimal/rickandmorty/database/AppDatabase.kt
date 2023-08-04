package com.vimal.rickandmorty.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vimal.rickandmorty.model.CharacterDto
import com.vimal.rickandmorty.model.Episode

@Database(entities = [CharacterDto::class, Episode::class], version = 1, exportSchema = false)
@TypeConverters(StringListConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getNotesDao(): CharacterDao

    abstract fun getEpisodeDao(): EpisodeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "app_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}