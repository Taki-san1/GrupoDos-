package com.example.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.dogapp.R
import com.example.dogapp.databinding.FragmentDetalleCitaBinding
import com.example.dogapp.model.Cita
import com.example.dogapp.utils.citaRecibida
import com.example.dogapp.utils.irAEditar
import com.example.dogapp.utils.volverAtras
import com.example.dogapp.viewmodel.DogViewModel

/** HU 4.0: presenta el detalle y permite eliminar o editar la cita seleccionada. */
class DetalleCitaFragment : Fragment() {

    private var _binding: FragmentDetalleCitaBinding? = null
    private val binding get() = _binding!!

    private val dogViewModel: DogViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalleCitaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cita = try {
            citaRecibida()
        } catch (exception: Exception) {
            Toast.makeText(
                requireContext(),
                "No se recibió la cita seleccionada",
                Toast.LENGTH_LONG
            ).show()
            findNavController().navigateUp()
            return
        }
        mostrarDetalle(cita)
        configurarControles(cita)
        observarEstado()
    }

    private fun mostrarDetalle(cita: Cita) {
        binding.tvTituloDetalle.text = cita.nombreMascota
        binding.tvTurno.text = "# ${cita.id}"
        binding.tvRaza.text = cita.raza
        binding.tvSintoma.text = cita.sintoma
        binding.tvPropietario.text = "Propietario: ${cita.nombrePropietario}"
        binding.tvTelefono.text = "Teléfono: ${cita.telefono}"
        binding.ivImagenMascota.contentDescription = "Imagen de ${cita.nombreMascota}"

        Glide.with(this)
            .load(cita.imagenUrl.takeIf { it.isNotBlank() })
            .placeholder(R.drawable.perro_header)
            .error(R.drawable.perro_header)
            .centerCrop()
            .into(binding.ivImagenMascota)
    }

    private fun configurarControles(cita: Cita) {
        binding.btnVolver.setOnClickListener {
            volverAtras()
        }

        binding.fabEliminar.setOnClickListener {
            dogViewModel.limpiarEstadoOperacion()
            dogViewModel.eliminarCita(cita)
        }

        binding.fabEditar.setOnClickListener {
            irAEditar(cita)
        }
    }

    private fun observarEstado() {
        dogViewModel.isLoading.observe(viewLifecycleOwner) { cargando ->
            binding.progressDetalle.visibility =
                if (cargando) View.VISIBLE else View.GONE
            binding.fabEliminar.isEnabled = !cargando
            binding.fabEditar.isEnabled = !cargando
        }

        dogViewModel.error.observe(viewLifecycleOwner) { mensaje ->
            if (!mensaje.isNullOrBlank()) {
                Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()
            }
        }

        dogViewModel.operacionCompletada.observe(viewLifecycleOwner) { completada ->
            if (completada) {
                dogViewModel.limpiarEstadoOperacion()
                findNavController().popBackStack(R.id.homeCitasFragment, false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}