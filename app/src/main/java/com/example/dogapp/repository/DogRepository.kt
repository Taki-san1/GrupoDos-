package com.example.dogapp.repository

import androidx.lifecycle.LiveData
import com.example.dogapp.data.CitaDao
import com.example.dogapp.model.BreedsResponse
import com.example.dogapp.model.Cita
import com.example.dogapp.model.DogImageResponse
import com.example.dogapp.webservice.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Fuente de datos única: Room para las citas y Dog CEO para razas e imágenes. */
class DogRepository(
    private val citaDao: CitaDao,
    private val apiService: ApiService
) {
    val citas: LiveData<List<Cita>> = citaDao.observarCitas()

    suspend fun obtenerCita(id: Int): Cita? = withContext(Dispatchers.IO) {
        citaDao.obtenerPorId(id)
    }

    suspend fun obtenerRazas(): List<String> = withContext(Dispatchers.IO) {
        val response = apiService.obtenerRazas()
        val data: BreedsResponse = response.body()
            ?.takeIf { response.isSuccessful && it.estado == "success" }
            ?: error("No fue posible consultar las razas (${response.code()}).")

        data.razas.flatMap { (raza, subrazas) ->
            listOf(raza) + subrazas.map { subraza -> "$subraza $raza" }
        }.sorted()
    }

    suspend fun guardarCita(cita: Cita): Cita = withContext(Dispatchers.IO) {
        val citaConImagen = cita.copy(imagenUrl = consultarImagen(cita.raza))
        val id = citaDao.insertar(citaConImagen)
        citaConImagen.copy(id = id.toInt())
    }

    suspend fun actualizarCita(cita: Cita): Cita = withContext(Dispatchers.IO) {
        val citaConImagen = cita.copy(imagenUrl = consultarImagen(cita.raza))
        citaDao.actualizar(citaConImagen)
        citaConImagen
    }

    suspend fun eliminarCita(cita: Cita) = withContext(Dispatchers.IO) {
        citaDao.eliminar(cita)
    }

    private suspend fun consultarImagen(nombreRaza: String): String {
        val razas = cargarRazas().razas
        val seleccion = nombreRaza.trim().lowercase()
        val subrazaYraza = razas.entries
            .asSequence()
            .flatMap { (raza, subrazas) -> subrazas.asSequence().map { it to raza } }
            .firstOrNull { (subraza, raza) -> "$subraza $raza" == seleccion }

        val response = if (subrazaYraza != null) {
            apiService.obtenerImagenSubraza(subrazaYraza.second, subrazaYraza.first)
        } else {
            val raza = razas.keys.firstOrNull { it.equals(seleccion, ignoreCase = true) }
                ?: error("Selecciona una raza de la lista.")
            apiService.obtenerImagenRaza(raza)
        }

        val data: DogImageResponse = response.body()
            ?.takeIf { response.isSuccessful && it.estado == "success" }
            ?: error("No fue posible cargar la imagen de la raza (${response.code()}).")
        return data.imagenUrl
    }

    private suspend fun cargarRazas(): BreedsResponse {
        val response = apiService.obtenerRazas()
        return response.body()
            ?.takeIf { response.isSuccessful && it.estado == "success" }
            ?: error("No fue posible consultar las razas (${response.code()}).")
    }
}
