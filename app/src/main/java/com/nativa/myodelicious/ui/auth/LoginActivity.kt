package com.nativa.myodelicious.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.nativa.myodelicious.R
import com.nativa.myodelicious.SupabaseClient
import com.nativa.myodelicious.data.CredencialesManager
import com.nativa.myodelicious.data.UsuarioRepository
import com.nativa.myodelicious.ui.MainActivity
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var tvCrear_Cuenta: TextView
    private lateinit var btnIniciarSesion: Button
    private lateinit var LayoutGoogle: LinearLayout
    private lateinit var tvIngresarHuella: TextView
    private lateinit var tvRecuperarContrasena: TextView
    private lateinit var etCorreo: EditText
    private lateinit var etContrasena: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        etCorreo = findViewById(R.id.et_mail_login)
        etContrasena = findViewById(R.id.et_password)
        btnIniciarSesion = findViewById(R.id.btn_iniciar)
        tvCrear_Cuenta = findViewById(R.id.tv_crear_cuenta)
        LayoutGoogle = findViewById(R.id.ly_google)
        tvIngresarHuella = findViewById(R.id.tv_ing_huella)
        tvRecuperarContrasena = findViewById(R.id.tv_olvidaste)

        tvCrear_Cuenta.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
            finish()
        }

        tvRecuperarContrasena.setOnClickListener {
            startActivity(Intent(this, RecuperacionPassActivity::class.java))
            finishAffinity()
        }
        btnIniciarSesion.setOnClickListener {
            iniciarSesion()
        }
        LayoutGoogle.setOnClickListener{
            iniciarSesionConGoogle()
        }
        tvIngresarHuella.setOnClickListener {
            mostrarDialogoHuella()
        }
    }

    private fun mostrarDialogoHuella() {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    val correo = CredencialesManager.obtenerCorreo(this@LoginActivity)
                    val contrasena = CredencialesManager.obtenerContrasena(this@LoginActivity)
                    if (correo != null && contrasena != null) {
                        lifecycleScope.launch {
                            try {
                                SupabaseClient.client.auth.signInWith(Email) {
                                    email = correo
                                    password = contrasena
                                }
                                irAPantallaPrincipal()
                            } catch (e: Exception) {
                                runOnUiThread {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Error al iniciar",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Primero debe iniciar sesión con usuario y contraseña para habilitar la huella", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    if (errorCode != BiometricPrompt.ERROR_USER_CANCELED &&
                        errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON
                    ) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Error biométrico: $errString",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onAuthenticationFailed() {
                    Toast.makeText(
                        this@LoginActivity,
                        "Huella no reconocida, intenta de nuevo",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Inicio de sesión biométrico")
            .setSubtitle("Usa tu huella para entrar")
            .setNegativeButtonText("Cancelar")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun iniciarSesion(){
        val correo = etCorreo.text.toString().trim()
        val contrasena : String = etContrasena.text.toString().trim()

        if (correo.isEmpty() || contrasena.isEmpty()){
            Toast.makeText(this,"Por favor, ingresa tu correo y contraseña", Toast.LENGTH_SHORT).show()
            return
        }
        lifecycleScope.launch {
            try {
                SupabaseClient.client.auth.signInWith(Email){
                    email = correo
                    password = contrasena
                }
                CredencialesManager.guardarCredenciales(this@LoginActivity, correo, contrasena)
                irAPantallaPrincipal()
            }catch (e: Exception){
                val mensaje = when{
                    e.message?.contains("Invalid login credentials") == true -> "Credenciales invalidas"
                    else -> "Error al iniciar sesión: ${e.message}"
                }
                runOnUiThread {
                    Toast.makeText(this@LoginActivity,mensaje, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun iniciarSesionConGoogle(){
        lifecycleScope.launch {
            try {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId("744605947489-heqqc15hgv744ttpdj0k18uiss4916t6.apps.googleusercontent.com")
                    .setAutoSelectEnabled(false)
                    .build()

                val  request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val credentialManager = CredentialManager.create(this@LoginActivity)
                val result = credentialManager.getCredential(this@LoginActivity, request)

                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)

                SupabaseClient.client.auth.signInWith(IDToken){
                    idToken = googleIdTokenCredential.idToken
                    provider = Google
                }

                val user = SupabaseClient.client.auth.currentUserOrNull()

                if (user != null) {
                    val yaExiste = UsuarioRepository.existeUsuario(user.id)
                    val fullName = user.userMetadata
                        ?.get("full_name")?.toString()
                        ?.replace("\"", "") ?: ""

                    val nombres = fullName.split(" ").firstOrNull() ?: "Sin nombre"
                    val apellidos = fullName.split(" ").drop(1).joinToString(" ")
                    val correo = user.email?.takeIf { it.isNotBlank() }
                        ?: user.userMetadata?.get("email")?.toString()?.replace("\"", "")?.takeIf { it.isNotBlank() }
                        ?: user.userMetadata?.get("preferred_username")?.toString()?.replace("\"", "")?.takeIf { it.isNotBlank() }
                        ?: user.userMetadata?.get("user_name")?.toString()?.replace("\"", "")?.takeIf { it.isNotBlank() }

                    android.util.Log.d("DEBUG_USER", "correo resuelto: $correo")

                    if (!yaExiste) {
                        UsuarioRepository.insertarUsuario(
                            id = user.id,
                            nombres = nombres,
                            apellidos = apellidos,
                            correo = correo
                        )
                    } else if (correo != null) {
                        UsuarioRepository.actualizarCorreo(user.id, correo)
                    }

                    irAPantallaPrincipal()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "Error al iniciar sesion con Google: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private suspend fun irAPantallaPrincipal() {
        val rol = UsuarioRepository.obtenerRolActual()
        runOnUiThread {
            val intent = Intent(this, MainActivity::class.java)
            if (rol.lowercase() == "administrador") {  // ← .lowercase() aquí
                intent.putExtra("TARGET_FRAGMENT", "ADMIN")
            }
            startActivity(intent)
            finish()
        }
    }


}