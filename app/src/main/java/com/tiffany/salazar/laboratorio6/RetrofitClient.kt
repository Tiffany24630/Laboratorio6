package com.tiffany.salazar.laboratorio6

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PexelsApi{ //Interfaz de la API de Pexels
    @GET("v1/search")
    suspend fun searchPhotos(
        @Header("Authorization") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): PexelsResponse
}

object RetrofitClient{ //Cliente Retrofit para la API de Pexels
    private const val BASE_URL = "https://api.pexels.com/"

    val api: PexelsApi by lazy{
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(PexelsApi::class.java)
    }
}
