package com.example.dogapp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dogapp.R
import com.example.dogapp.databinding.ItemCitaBinding
import com.example.dogapp.model.Cita

/** Presenta una cita en la lista y reenvía su selección al Fragment. */
class CitaViewHolder(
    private val binding: ItemCitaBinding,
    private val alSeleccionarCita: (Cita) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun mostrarCita(cita: Cita) {
        binding.tvNombreMascota.text = cita.nombreMascota
        binding.tvSintoma.text = cita.sintoma
        binding.tvTurno.text = "# ${cita.id}"
        binding.ivImagenMascota.contentDescription = "Imagen de ${cita.nombreMascota}"

        Glide.with(binding.root.context)
            .load(cita.imagenUrl.takeIf { it.isNotBlank() })
            .placeholder(R.drawable.perro_header)
            .error(R.drawable.perro_header)
            .circleCrop()
            .into(binding.ivImagenMascota)

        binding.cvCita.setOnClickListener {
            alSeleccionarCita(cita)
        }
    }
}