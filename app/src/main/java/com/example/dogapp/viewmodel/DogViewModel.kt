package com.example.dogapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dogapp.data.DogAppDB
import com.example.dogapp.model.Cita
import com.example.dogapp.repository.DogRepository
import com.example.dogapp.webservice.ApiUtils
import kotlinx.coroutines.launch

class DogViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = DogRepository(
        DogAppDB.getDatabase(application).citaDao(),
        ApiUtils.getApiService()
    )

    val citas: LiveData<List<Cita>> = repository.citas
    private val _razas = MutableLiveData<List<String>>()
    val razas: LiveData<List<String>> = _razas
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    private val _operacionCompletada = MutableLiveData(false)
    val operacionCompletada: LiveData<Boolean> = _operacionCompletada

    fun cargarRazas() = ejecutarOperacion {
        _razas.value = repository.obtenerRazas()
    }

    fun obtenerCita(id: Int, alObtener: (Cita?) -> Unit) = ejecutarOperacion {
        alObtener(repository.obtenerCita(id))
    }

    fun guardarCita(cita: Cita) = ejecutarOperacion {
        repository.guardarCita(cita)
        _operacionCompletada.value = true
    }

    fun actualizarCita(cita: Cita) = ejecutarOperacion {
        repository.actualizarCita(cita)
        _operacionCompletada.value = true
    }

    fun eliminarCita(cita: Cita) = ejecutarOperacion {
        repository.eliminarCita(cita)
        _operacionCompletada.value = true
    }

    fun limpiarEstadoOperacion() {
        _operacionCompletada.value = false
        _error.value = null
    }

    private fun ejecutarOperacion(operacion: suspend () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                operacion()
            } catch (exception: Exception) {
                _error.value = exception.message ?: "Ocurrió un error inesperado."
            } finally {
                _isLoading.value = false
            }
        }
    }
}
