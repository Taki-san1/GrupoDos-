
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
import com.example.dogapp.databinding.FragmentNuevaCitaBinding
import com.example.dogapp.model.Cita
import com.example.dogapp.utils.Constants
import com.example.dogapp.viewmodel.DogViewModel

class NuevaCitaFragment : Fragment() {

    private var _binding: FragmentNuevaCitaBinding? = null
    private val binding get() = _binding!!

    private val dogViewModel: DogViewModel by viewModels()

    private var listaRazas: List<String> = emptyList()
    private var cargandoRazas = false
    private var cargandoOperacion = false

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
        configurarCampoPropietario()
        configurarCampoTelefono()
        configurarListaSintomas()
        configurarBotonGuardar()

        observarRazas()
        observarCarga()
        observarErrores()
        observarGuardado()

        solicitarRazas()
    }

    // Regresar al Home de citas
    private fun configurarBotonVolver() {
        binding.btnVolver.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    // HU 3.2: Nombre de la mascota
    private fun configurarCampoNombre() {

        binding.etNombreMascota.addTextChangedListener {
            validarFormulario()
        }
    }

    // HU 3.3: Selección de raza
    private fun configurarCampoRaza() {

        binding.actvRaza.threshold = 2

        binding.actvRaza.addTextChangedListener { texto ->

            val consulta = texto?.toString()?.trim().orEmpty()

            if (consulta.length >= 2 && listaRazas.isEmpty()) {
                solicitarRazas()
            }

            if (consulta.length >= 2 && listaRazas.isNotEmpty()) {
                binding.actvRaza.post {
                    if (binding.actvRaza.hasFocus() && binding.actvRaza.text.length >= 2) {
                        binding.actvRaza.showDropDown()
                    }
                }
            }

            validarFormulario()
        }

        binding.actvRaza.setOnItemClickListener { _, _, _, _ ->
            validarFormulario()
        }
    }

    private fun observarRazas() {

        dogViewModel.razas.observe(viewLifecycleOwner) { razas ->

            if (!razas.isNullOrEmpty()) {

                cargandoRazas = false
                listaRazas = razas

                val adaptador = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    listaRazas
                )

                binding.actvRaza.setAdapter(adaptador)

                if (binding.actvRaza.text.length >= 2) {
                    binding.actvRaza.post {
                        if (binding.actvRaza.hasFocus()) {
                            binding.actvRaza.showDropDown()
                        }
                    }
                }
            }

            validarFormulario()
        }
    }

    private fun solicitarRazas() {
        if (cargandoRazas || listaRazas.isNotEmpty()) return

        cargandoRazas = true
        dogViewModel.cargarRazas()
    }

    // HU 3.4: Nombre del propietario
    private fun configurarCampoPropietario() {

        binding.etPropietario.addTextChangedListener {
            validarFormulario()
        }
    }

    // HU 3.4: Teléfono de contacto
    private fun configurarCampoTelefono() {

        binding.etTelefono.addTextChangedListener {
            validarFormulario()
        }
    }

    // HU 3.4: Selector de síntomas
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

    // HU 3.5: Validación de los campos obligatorios
    private fun validarFormulario(): Boolean {

        val nombre = binding.etNombreMascota.text
            ?.toString()?.trim().orEmpty()

        val raza = binding.actvRaza.text
            ?.toString()?.trim().orEmpty()

        val propietario = binding.etPropietario.text
            ?.toString()?.trim().orEmpty()

        val telefono = binding.etTelefono.text
            ?.toString()?.trim().orEmpty()

        val razaValida = listaRazas.any {
            it.equals(raza, ignoreCase = true)
        }

        val camposCompletos =
            nombre.isNotEmpty() &&
            nombre.length <= 15 &&
            razaValida &&
            propietario.isNotEmpty() &&
            propietario.length <= 30 &&
            telefono.isNotEmpty() &&
            telefono.all { it.isDigit() } &&
            telefono.length <= 10

        binding.btnGuardarCita.isEnabled = camposCompletos && !cargandoOperacion
        return camposCompletos
    }

    private fun configurarBotonGuardar() {

        binding.btnGuardarCita.setOnClickListener {

            if (!validarFormulario()) {
                return@setOnClickListener
            }

            val sintoma = binding.actvSintoma.text
                ?.toString()?.trim().orEmpty()

            if (sintoma !in Constants.SINTOMAS) {
                Toast.makeText(
                    requireContext(),
                    "Selecciona un síntoma",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            guardarCita()
        }
    }

    // HU 3.5: Guardar la cita mediante Room
    private fun guardarCita() {

        val nuevaCita = Cita(
            nombreMascota = binding.etNombreMascota.text
                .toString().trim(),

            raza = binding.actvRaza.text
                .toString().trim(),

            nombrePropietario = binding.etPropietario.text
                .toString().trim(),

            telefono = binding.etTelefono.text
                .toString().trim(),

            sintoma = binding.actvSintoma.text
                .toString().trim()
        )

        binding.btnGuardarCita.isEnabled = false

        dogViewModel.limpiarEstadoOperacion()
        dogViewModel.guardarCita(nuevaCita)
    }

    private fun observarCarga() {

        dogViewModel.isLoading.observe(viewLifecycleOwner) { cargando ->

            binding.progressRazas.visibility =
                if (cargando) View.VISIBLE else View.GONE

            cargandoOperacion = cargando
            validarFormulario()
        }
    }

    private fun observarErrores() {

        dogViewModel.error.observe(viewLifecycleOwner) { mensaje ->

            if (!mensaje.isNullOrBlank()) {

                cargandoRazas = false

                Toast.makeText(
                    requireContext(),
                    mensaje,
                    Toast.LENGTH_LONG
                ).show()

                validarFormulario()
            }
        }
    }

    private fun observarGuardado() {

        dogViewModel.operacionCompletada.observe(viewLifecycleOwner) { completada ->

            if (completada == true) {

                Toast.makeText(
                    requireContext(),
                    "Cita guardada correctamente",
                    Toast.LENGTH_SHORT
                ).show()

                dogViewModel.limpiarEstadoOperacion()

                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
