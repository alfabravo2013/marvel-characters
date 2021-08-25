package com.github.alfabravo2013.marvelcharacters.networking

import com.github.alfabravo2013.marvelcharacters.networking.model.CharacterDataWrapper
import com.github.alfabravo2013.marvelcharacters.utils.CHARACTERS_ID_ENDPOINT
import com.github.alfabravo2013.marvelcharacters.utils.CHARACTERS_PAGE_ENDPOINT
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelApi {

    @GET(CHARACTERS_PAGE_ENDPOINT)
    suspend fun getCharactersPage(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): CharacterDataWrapper

    @GET(CHARACTERS_ID_ENDPOINT)
    suspend fun getCharacterById(
        @Path("characterId") characterId: Int
    ): CharacterDataWrapper
}
