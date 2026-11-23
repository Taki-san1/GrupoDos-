package com.example.dogapp.model

import com.google.gson.annotations.SerializedName

data class DogImageResponse(
    @SerializedName("message") val imagenUrl: String,
    @SerializedName("status") val estado: String
)
