package com.github.alfabravo2013.marvelcharacters.localstorage.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "marvel_characters")
data class MarvelCharacterEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "bookmarked") val bookmarked: Boolean,
    @ColumnInfo(name = "timestamp") val timeStamp: Long = System.currentTimeMillis()
)
