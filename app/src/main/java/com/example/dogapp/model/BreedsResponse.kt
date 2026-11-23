package com.example.dogapp.model

import com.google.gson.annotations.SerializedName

/** Dog CEO entrega cada raza principal como clave y sus subrazas como lista. */
data class BreedsResponse(
    @SerializedName("message") val razas: Map<String, List<String>>,
    @SerializedName("status") val estado: String
)
