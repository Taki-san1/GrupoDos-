
package com.example.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
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
        configurarListaSintomas()
        configurarObservadores()
        configurarValidacionInicial()

        cargarRazas()
    }

    private fun configurarBotonVolver() {
        binding.btnVolver.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun configurarListaSintomas() {
        val adaptadorSintomas = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            Constants.SINTOMAS
        )

        binding.actvSintoma.setAdapter(adaptadorSintomas)
        binding.actvSintoma.setOnClickListener {
            binding.actvSintoma.showDropDown()
        }
    }

    private fun cargarRazas() {
        dogViewModel.cargarRazas()
    }

    private fun configurarObservadores() {

        dogViewModel.razas.observe(viewLifecycleOwner) { razas ->
            if (!razas.isNullOrEmpty()) {
                listaRazas = razas

                val adaptadorRazas = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    listaRazas
                )

                binding.actvRaza.setAdapter(adaptadorRazas)
                binding.actvRaza.threshold = 2

                binding.actvRaza.setOnItemClickListener { _, _, _, _ ->
                    validarFormulario()
                }
            }
        }

        dogViewModel.isLoading.observe(viewLifecycleOwner) { cargando ->
            binding.progressRazas.visibility =
                if (cargando) View.VISIBLE else View.GONE

            binding.actvRaza.isEnabled = !cargando
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
    }

    private fun configurarValidacionInicial() {

        binding.etNombreMascota.addTextChangedListener {
            validarFormulario()
        }

        binding.actvRaza.addTextChangedListener {
            validarFormulario()
        }

        binding.etPropietario.addTextChangedListener {
            validarFormulario()
        }

        binding.etTelefono.addTextChangedListener {
            validarFormulario()
        }

        binding.actvSintoma.setOnItemClickListener { _, _, _, _ ->
            validarFormulario()
        }

        binding.actvRaza.setOnFocusChangeListener { _, tieneFoco ->
            if (tieneFoco && listaRazas.isNotEmpty()) {
                binding.actvRaza.showDropDown()
            }
        }

        binding.btnGuardarCita.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "La funcionalidad de guardado se implementará en el bloque 3.5.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun validarFormulario() {

        val nombreMascota = binding.etNombreMascota.text
            ?.toString()
            ?.trim()
            .orEmpty()

        val raza = binding.actvRaza.text
            ?.toString()
            ?.trim()
            .orEmpty()

        val propietario = binding.etPropietario.text
            ?.toString()
            ?.trim()
            .orEmpty()

        val telefono = binding.etTelefono.text
            ?.toString()
            ?.trim()
            .orEmpty()

        val sintoma = binding.actvSintoma.text
            ?.toString()
            ?.trim()
            .orEmpty()

        val razaValida = listaRazas.any {
            it.equals(raza, ignoreCase = true)
        }

        binding.btnGuardarCita.isEnabled =
            nombreMascota.isNotEmpty() &&
            nombreMascota.length <= 15 &&
            razaValida &&
            propietario.isNotEmpty() &&
            propietario.length <= 30 &&
            telefono.length == 10 &&
            sintoma in Constants.SINTOMAS
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
