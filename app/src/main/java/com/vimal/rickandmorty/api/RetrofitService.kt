package com.vimal.rickandmorty.api

import com.vimal.rickandmorty.model.CharacterListResponse
import com.vimal.rickandmorty.model.Episode
import com.vimal.rickandmorty.model.RickSanchez
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {

    @GET("character")
    suspend fun getAllCharacter(@Query("page") page: Int? = null): Response<CharacterListResponse>

    @GET("character/{id}")
    suspend fun getCharacterDetails(@Path("id") id: Int): Response<RickSanchez>

    @GET("episode/{id}")
    suspend fun getEpisode(@Path("id") id: Int): Response<Episode>

    companion object {
        var retrofitService: RetrofitService? = null
        fun getInstance(): RetrofitService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://rickandmortyapi.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }

    }
}