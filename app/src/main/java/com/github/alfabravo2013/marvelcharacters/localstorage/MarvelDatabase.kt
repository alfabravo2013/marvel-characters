package com.github.alfabravo2013.marvelcharacters.localstorage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.alfabravo2013.marvelcharacters.localstorage.entities.MarvelCharacterEntity

@Database(entities = [MarvelCharacterEntity::class], version = 1, exportSchema = false)
abstract class MarvelDatabase : RoomDatabase() {
    abstract val charactersDao: CharactersDao

    companion object {
        @Volatile
        private var INSTANCE: MarvelDatabase? = null

        fun getInstance(context: Context): MarvelDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MarvelDatabase::class.java,
                    "marvel_database"
                ).fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
