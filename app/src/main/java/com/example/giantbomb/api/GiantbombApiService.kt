package com.example.giantbomb.api

import com.example.giantbomb.network.GamesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap
import retrofit2.http.QueryName

interface GiantbombApiService {
    /**
     * Returns the Games list per search text provided.
     */
    @GET("api/games")
    fun getGamesFeed(
        @Query("api_key") apiKey: String,
        @Query("filter") filter: String,
        @Query("format") format: String,
        @Query("field_list") fieldList: String
    ): Call<GamesResponse>
}