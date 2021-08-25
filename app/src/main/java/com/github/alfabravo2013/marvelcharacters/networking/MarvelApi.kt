package com.github.alfabravo2013.marvelcharacters.networking

import com.github.alfabravo2013.marvelcharacters.networking.model.CharacterDataWrapper
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelApi {

    @GET("v1/public/characters")
    suspend fun getCharactersPage(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): CharacterDataWrapper

    @GET("v1/public/characters/{characterId}")
    suspend fun getCharacterById(
        @Path("characterId") characterId: Int
    ): CharacterDataWrapper
}
