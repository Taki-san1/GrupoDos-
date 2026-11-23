
package com.example.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dogapp.R
import com.example.dogapp.databinding.FragmentNuevaCitaBinding
import com.example.dogapp.utils.Constants
import com.example.dogapp.viewmodel.DogViewModel

class NuevaCitaFragment : Fragment() {

    private var _binding: FragmentNuevaCitaBinding? = null
    private val binding get() = _binding!!

    private val dogViewModel: DogViewModel by viewModels()

    private var listaRazas: List<String> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNuevaCitaBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarBotonVolver()
        configurarCampoNombre()
        configurarCampoRaza()
        configurarListaSintomas()
        observarRazas()
        observarCarga()
        observarErrores()
    }

    // Navegación hacia la pantalla anterior
    private fun configurarBotonVolver() {
        binding.btnVolver.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    // HU 3.2: Nombre de la mascota
    private fun configurarCampoNombre() {

        binding.etNombreMascota.filters = arrayOf(
            android.text.InputFilter.LengthFilter(15)
        )

        binding.etNombreMascota.addTextChangedListener {
            validarCamposPrincipales()
        }
    }

    // HU 3.3: Autocompletado de razas
    private fun configurarCampoRaza() {

        binding.actvRaza.threshold = 2

        binding.actvRaza.addTextChangedListener { texto ->
            val consulta = texto?.toString()?.trim().orEmpty()

            if (consulta.length >= 2 && listaRazas.isEmpty()) {
                cargarRazas()
            }

            validarCamposPrincipales()
        }

        binding.actvRaza.setOnItemClickListener { _, _, _, _ ->
            validarCamposPrincipales()
        }
    }

    private fun cargarRazas() {
        dogViewModel.cargarRazas()
    }

    private fun observarRazas() {

        dogViewModel.razas.observe(viewLifecycleOwner) { razas ->

            if (!razas.isNullOrEmpty()) {

                listaRazas = razas

                val adaptador = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    listaRazas
                )

                binding.actvRaza.setAdapter(adaptador)

                val consulta = binding.actvRaza.text.toString()

                if (consulta.length >= 2) {
                    binding.actvRaza.showDropDown()
                }
            }

            validarCamposPrincipales()
        }
    }

    private fun observarCarga() {

        dogViewModel.isLoading.observe(viewLifecycleOwner) { cargando ->

            binding.progressRazas.visibility =
                if (cargando) View.VISIBLE else View.GONE

            binding.actvRaza.isEnabled = !cargando
        }
    }

    private fun observarErrores() {

        dogViewModel.error.observe(viewLifecycleOwner) { mensaje ->

            if (!mensaje.isNullOrBlank()) {

                Toast.makeText(
                    requireContext(),
                    mensaje,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // Lista de síntomas preparada para el bloque 3.4
    private fun configurarListaSintomas() {

        val adaptador = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            Constants.SINTOMAS
        )

        binding.actvSintoma.setAdapter(adaptador)

        binding.actvSintoma.setOnClickListener {
            binding.actvSintoma.showDropDown()
        }
    }

    // Validación de los campos que estamos desarrollando
    private fun validarCamposPrincipales() {

        val nombre = binding.etNombreMascota.text
            ?.toString()
            ?.trim()
            .orEmpty()

        val raza = binding.actvRaza.text
            ?.toString()
            ?.trim()
            .orEmpty()

        val nombreValido =
            nombre.isNotEmpty() && nombre.length <= 15

        val razaValida = listaRazas.any {
            it.equals(raza, ignoreCase = true)
        }

        // La habilitación definitiva del botón se completará
        // cuando implementemos todos los campos del formulario.
        binding.btnGuardarCita.isEnabled =
            nombreValido &&
            razaValida &&
            binding.etPropietario.text.toString().isNotBlank() &&
            binding.etTelefono.text.toString().length == 10 &&
            binding.actvSintoma.text.toString() in Constants.SINTOMAS
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
