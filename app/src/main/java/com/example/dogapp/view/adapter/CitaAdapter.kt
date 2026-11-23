package com.example.dogapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dogapp.databinding.ItemCitaBinding
import com.example.dogapp.model.Cita
import com.example.dogapp.view.viewholder.CitaViewHolder

/** Adapter de RecyclerView para las citas almacenadas en Room. */
class CitaAdapter(
    private val citas: List<Cita>,
    private val alSeleccionarCita: (Cita) -> Unit
) : RecyclerView.Adapter<CitaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        val binding = ItemCitaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CitaViewHolder(binding, alSeleccionarCita)
    }

    override fun getItemCount(): Int = citas.size

    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        holder.mostrarCita(citas[position])
    }
}