
package com.example.dogapp.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dogapp.R
import com.example.dogapp.databinding.FragmentEditarCitaBinding
import com.example.dogapp.model.Cita
import com.example.dogapp.viewmodel.DogViewModel

class EditarCitaFragment : Fragment(R.layout.fragment_editar_cita) {

    private var _binding: FragmentEditarCitaBinding? = null
    private val binding get() = _binding!!

    private val dogViewModel: DogViewModel by viewModels()

    private var citaOriginal: Cita? = null
    private var listaRazas: List<String> = emptyList()
    private var citaCargada = false

    companion object {
        const val ARG_CITA_ID = "citaId"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentEditarCitaBinding.bind(view)

        configurarBotonVolver()
        configurarObservadores()
        configurarCampos()
        configurarBotonEditar()

        cargarCita()
        dogViewModel.cargarRazas()
    }

    private fun configurarBotonVolver() {
        binding.btnVolver.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun configurarObservadores() {
        dogViewModel.razas.observe(viewLifecycleOwner) { razas ->
            listaRazas = razas

            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                listaRazas
            )

            binding.actvRaza.setAdapter(adapter)
            binding.actvRaza.threshold = 2

            validarCampos()
        }

        dogViewModel.isLoading.observe(viewLifecycleOwner) { cargando ->
            binding.progressEdicion.visibility =
                if (cargando) View.VISIBLE else View.GONE

            validarCampos()
        }

        dogViewModel.error.observe(viewLifecycleOwner) { mensaje ->
            if (!mensaje.isNullOrBlank()) {
                Toast.makeText(
                    requireContext(),
                    mensaje,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        dogViewModel.operacionCompletada.observe(viewLifecycleOwner) { completada ->
            if (completada) {
                Toast.makeText(
                    requireContext(),
                    "Cita actualizada correctamente",
                    Toast.LENGTH_SHORT
                ).show()

                dogViewModel.limpiarEstadoOperacion()
                regresarAHome()
            }
        }
    }

    private fun configurarCampos() {
        observarCambios(binding.etNombreMascota)
        observarCambios(binding.actvRaza)
        observarCambios(binding.etPropietario)
        observarCambios(binding.etTelefono)
    }

    private fun observarCambios(campo: EditText) {
        campo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) = Unit

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                validarCampos()
            }

            override fun afterTextChanged(s: Editable?) = Unit
        })
    }

    private fun cargarCita() {
        val idCita = arguments?.getInt(ARG_CITA_ID, -1) ?: -1

        if (idCita <= 0) {
            Toast.makeText(
                requireContext(),
                "No se recibió una cita válida para editar",
                Toast.LENGTH_LONG
            ).show()
            binding.btnEditarCita.isEnabled = false
            return
        }

        dogViewModel.obtenerCita(idCita) { cita ->
            if (cita == null) {
                Toast.makeText(
                    requireContext(),
                    "No se encontró la cita seleccionada",
                    Toast.LENGTH_LONG
                ).show()
                return@obtenerCita
            }

            citaOriginal = cita
            citaCargada = true

            binding.etNombreMascota.setText(cita.nombreMascota)
            binding.actvRaza.setText(cita.raza, false)
            binding.etPropietario.setText(cita.nombrePropietario)
            binding.etTelefono.setText(cita.telefono)

            validarCampos()
        }
    }

    private fun configurarBotonEditar() {
        binding.btnEditarCita.setOnClickListener {
            actualizarCita()
        }
    }

    private fun formularioValido(): Boolean {
        val nombre = binding.etNombreMascota.text?.toString()?.trim().orEmpty()
        val raza = binding.actvRaza.text?.toString()?.trim().orEmpty()
        val propietario = binding.etPropietario.text?.toString()?.trim().orEmpty()
        val telefono = binding.etTelefono.text?.toString()?.trim().orEmpty()

        return citaCargada &&
                citaOriginal != null &&
                nombre.isNotEmpty() &&
                nombre.length <= 15 &&
                raza.isNotEmpty() &&
                propietario.isNotEmpty() &&
                propietario.length <= 30 &&
                telefono.isNotEmpty() &&
                telefono.length <= 10 &&
                telefono.all { it.isDigit() } &&
                listaRazas.contains(raza)
    }

    private fun validarCampos() {
        binding.btnEditarCita.isEnabled =
            formularioValido() && dogViewModel.isLoading.value != true
    }

    private fun actualizarCita() {
        if (!formularioValido()) {
            Toast.makeText(
                requireContext(),
                "Completa correctamente todos los campos",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val cita = citaOriginal ?: return

        val citaActualizada = cita.copy(
            nombreMascota = binding.etNombreMascota.text.toString().trim(),
            raza = binding.actvRaza.text.toString().trim(),
            nombrePropietario = binding.etPropietario.text.toString().trim(),
            telefono = binding.etTelefono.text.toString().trim()
        )

        dogViewModel.limpiarEstadoOperacion()
        dogViewModel.actualizarCita(citaActualizada)
    }

    private fun regresarAHome() {
        val navController = findNavController()

        // Pila esperada: Home -> Detalle -> Editar.
        val regresoADetalle = navController.popBackStack()

        if (regresoADetalle) {
            navController.popBackStack()
        } else {
            navController.navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

