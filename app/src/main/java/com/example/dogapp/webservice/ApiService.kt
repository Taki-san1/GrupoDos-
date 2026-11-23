package com.example.dogapp.webservice

import com.example.dogapp.model.BreedsResponse
import com.example.dogapp.model.DogImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("breeds/list/all")
    suspend fun obtenerRazas(): Response<BreedsResponse>

    @GET("breed/{raza}/images/random")
    suspend fun obtenerImagenRaza(@Path("raza") raza: String): Response<DogImageResponse>

    @GET("breed/{raza}/{subraza}/images/random")
    suspend fun obtenerImagenSubraza(
        @Path("raza") raza: String,
        @Path("subraza") subraza: String
    ): Response<DogImageResponse>
}
