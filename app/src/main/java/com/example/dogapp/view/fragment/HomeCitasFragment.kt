package com.example.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogapp.databinding.FragmentHomeCitasBinding
import com.example.dogapp.view.adapter.CitaAdapter
import com.example.dogapp.utils.irADetalle
import com.example.dogapp.utils.irANuevaCita
import com.example.dogapp.viewmodel.DogViewModel

/** HU 2.0: lista las citas observando los cambios de Room. */
class HomeCitasFragment : Fragment() {

    private var _binding: FragmentHomeCitasBinding? = null
    private val binding get() = _binding!!

    private val dogViewModel: DogViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeCitasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarLista()
        configurarControles()
        observarCitas()
        observarCarga()
        controlarBotonAtras()
    }

    private fun configurarLista() {
        binding.rvCitas.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCitas.setHasFixedSize(true)
    }

    private fun configurarControles() {
        binding.fabAgregarCita.setOnClickListener {
            irANuevaCita()
        }
    }

    private fun observarCitas() {
        dogViewModel.citas.observe(viewLifecycleOwner) { citas ->
            val listaCitas = citas.orEmpty()
            binding.rvCitas.adapter = CitaAdapter(listaCitas) { cita ->
                irADetalle(cita)
            }
            binding.tvSinCitas.visibility =
                if (listaCitas.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun observarCarga() {
        dogViewModel.isLoading.observe(viewLifecycleOwner) { cargando ->
            binding.progressCitas.visibility =
                if (cargando) View.VISIBLE else View.GONE
        }
    }

    /** En Home, Atrás cierra la Activity y devuelve al escritorio del celular. */
    private fun controlarBotonAtras() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}