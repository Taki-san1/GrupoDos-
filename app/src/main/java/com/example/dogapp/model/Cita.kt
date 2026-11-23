package com.example.dogapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/** Registro local de una cita. El id autogenerado también se muestra como turno. */
@Entity(tableName = "citas")
data class Cita(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombreMascota: String,
    val raza: String,
    val nombrePropietario: String,
    val telefono: String,
    val sintoma: String,
    val imagenUrl: String = ""
) : Serializable
