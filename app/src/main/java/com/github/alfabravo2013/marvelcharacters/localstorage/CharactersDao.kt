package com.github.alfabravo2013.marvelcharacters.localstorage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.github.alfabravo2013.marvelcharacters.localstorage.entities.MarvelCharacterEntity

@Dao
interface CharactersDao {

    @Query("DELETE FROM marvel_characters WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: MarvelCharacterEntity)

    @Query("SELECT * FROM marvel_characters WHERE id = :id")
    suspend fun findById(id: Int): MarvelCharacterEntity?

    @Query("SELECT * FROM marvel_characters WHERE id = :id AND bookmarked = 1")
    suspend fun findBookmarkedById(id: Int): MarvelCharacterEntity?

    @Transaction
    @Query("SELECT * FROM marvel_characters WHERE bookmarked = 1 ORDER BY name")
    suspend fun getBookmarked(): List<MarvelCharacterEntity>
}
