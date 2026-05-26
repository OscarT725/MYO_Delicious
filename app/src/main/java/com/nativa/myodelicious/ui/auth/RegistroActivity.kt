package com.nativa.myodelicious.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.nativa.myodelicious.R
import com.nativa.myodelicious.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class RegistroActivity : AppCompatActivity() {

    private lateinit var img_Regresar: ImageView
    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etContrasena: EditText
    private lateinit var etReContrasena: EditText
    private lateinit var checkTerminos: CheckBox
    private lateinit var btnRegistro: Button
    private lateinit var tv_Inicio_sesion: TextView

    @OptIn(kotlinx.serialization.InternalSerializationApi::class)
    @Serializable
    data class UsuarioData(
        val id: String,
        val nombre: String,
        val apellido: String,
        val correo: String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_registro)

        val rootView = findViewById<ViewGroup>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val bottomPadding = maxOf(a = systemBars.bottom, b = imeInsets.bottom)
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                bottomPadding
            )
            insets
        }

        img_Regresar = findViewById(R.id.img_regre)
        etNombre = findViewById(R.id.re_nombre)
        etApellido = findViewById(R.id.re_apellido)
        etCorreo = findViewById(R.id.re_correo)
        etContrasena = findViewById(R.id.re_pass)
        etReContrasena = findViewById(R.id.re_conf_pass)
        checkTerminos = findViewById(R.id.ch_termin_cond)
        btnRegistro = findViewById(R.id.btn_crear_cuenta)
        tv_Inicio_sesion = findViewById(R.id.tv_ini_sesi)

        img_Regresar.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnRegistro.setOnClickListener {
            val nombres = etNombre.text.toString().trim()
            val apellidos = etApellido.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val contrasena = etContrasena.text.toString().trim()
            val reContrasena = etReContrasena.text.toString().trim()
            val regex = Regex("^(?=.*[A-Z])(?=.*[!@#\$%^&*(),.?\":{}|<>]).{8,}$")

            if (nombres.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || reContrasena.isEmpty()){
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (contrasena != reContrasena){
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (contrasena.length < 8){
                Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!regex.matches(contrasena)) {
                Toast.makeText(this, "La contraseña debe tener mínimo 8 caracteres, una mayúscula y un carácter especial",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val userInfo = SupabaseClient.client.auth.signUpWith(Email){
                        email = correo
                        password = contrasena
                    }
                    val userId = SupabaseClient.client.auth.currentUserOrNull()?.id?: ""

                    SupabaseClient.client.postgrest["usuarios"].insert(
                        buildJsonObject {
                            put("id", userId)
                            put("nombres", nombres)
                            put("apellidos", apellidos)
                            put("correo", correo)  // ← agregar esta línea
                        }
                    )

                    runOnUiThread {
                        Toast.makeText(this@RegistroActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegistroActivity, LoginActivity::class.java))
                        finish()
                    }
                }catch (e: Exception){
                    runOnUiThread {
                        Toast.makeText(this@RegistroActivity, "Error en el registro: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        tv_Inicio_sesion.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

}
