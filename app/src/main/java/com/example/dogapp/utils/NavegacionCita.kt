package com.example.dogapp.utils

import androidx.core.os.BundleCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dogapp.R
import com.example.dogapp.model.Cita

/*
 * Navegación entre las cinco pantallas de DogApp.
 * Úsenlas desde los Fragments para que todos naveguen y pasen la cita igual.
 */

/** HU 2 -> HU 3: botón flotante (+) del Home. */
fun Fragment.irANuevaCita() =
    navegarDesde(R.id.homeCitasFragment, R.id.action_homeCitasFragment_to_nuevaCitaFragment)

/** HU 2 -> HU 4: clic en un ítem de la lista. */
fun Fragment.irADetalle(cita: Cita) =
    navegarDesde(
        R.id.homeCitasFragment,
        R.id.action_homeCitasFragment_to_detalleCitaFragment,
        bundleOf(Constants.CLAVE_CITA to cita)
    )

/** HU 4 -> HU 5: botón flotante de editar. */
fun Fragment.irAEditar(cita: Cita) =
    navegarDesde(
        R.id.detalleCitaFragment,
        R.id.action_detalleCitaFragment_to_editarCitaFragment,
        bundleOf(Constants.CLAVE_CITA to cita, Constants.CLAVE_CITA_ID to cita.id)
    )

/** HU 5 -> HU 2: después de guardar la edición (criterio 8). */
fun Fragment.irAlHomeDespuesDeEditar() =
    navegarDesde(R.id.editarCitaFragment, R.id.action_editarCitaFragment_to_homeCitasFragment)

/** HU 3 y HU 4 -> HU 2: después de guardar una cita nueva o de eliminar. */
fun Fragment.volverAlHome() {
    findNavController().popBackStack(R.id.homeCitasFragment, false)
}

/** Flecha rosada de las toolbars: vuelve a la pantalla anterior. */
fun Fragment.volverAtras() {
    findNavController().navigateUp()
}

/** En Detalle y Editar: obtiene la cita que llegó desde la pantalla anterior. */
fun Fragment.citaRecibida(): Cita =
    BundleCompat.getSerializable(requireArguments(), Constants.CLAVE_CITA, Cita::class.java)
        ?: error("No se recibió la cita")

/** Evita que la app se cierre si el usuario toca dos veces seguidas un botón. */
private fun Fragment.navegarDesde(origen: Int, accion: Int, args: android.os.Bundle? = null) {
    val nav = findNavController()
    if (nav.currentDestination?.id == origen) {
        nav.navigate(accion, args)
    }
}