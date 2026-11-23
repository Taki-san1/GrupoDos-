package com.example.dogapp.view.fragment

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.example.dogapp.R
import com.example.dogapp.databinding.FragmentLoginBinding

/** HU 1.0: Ventana Login con autenticación biométrica. */
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cambia el color de la animación para que se vea sobre el fondo oscuro
        binding.lottieHuella.addValueCallback(
            KeyPath("**"), LottieProperty.COLOR_FILTER
        ) {
            PorterDuffColorFilter(
                ContextCompat.getColor(requireContext(), R.color.huella_turquesa),
                PorterDuff.Mode.SRC_ATOP
            )
        }

        // Criterio 5: al tocar la huella se abre el diálogo biométrico
        binding.lottieHuella.setOnClickListener { mostrarDialogoBiometrico() }
    }

    private fun mostrarDialogoBiometrico() {
        val autenticadores = BiometricManager.Authenticators.BIOMETRIC_STRONG
        val puedeAutenticar = BiometricManager.from(requireContext())
            .canAuthenticate(autenticadores)

        if (puedeAutenticar != BiometricManager.BIOMETRIC_SUCCESS) {
            Toast.makeText(
                requireContext(),
                "Este dispositivo no tiene huella disponible o configurada",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val executor = ContextCompat.getMainExecutor(requireContext())
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    // Criterio 6: huella correcta -> HU 2.0 Home Administrador de Citas
                    findNavController().navigate(R.id.action_loginFragment_to_homeCitasFragment)
                }
                // Si la huella es incorrecta, el propio sistema muestra el error.
                // Si el usuario pulsa "Cancelar", el diálogo se cierra solo.
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticación con Biometría")
            .setSubtitle("Ingrese su huella digital")
            .setNegativeButtonText("Cancelar")
            .setAllowedAuthenticators(autenticadores)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
