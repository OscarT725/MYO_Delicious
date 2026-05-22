package com.nativa.myodelicious.ui.main.usuario

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nativa.myodelicious.R
import com.nativa.myodelicious.SupabaseClient
import com.nativa.myodelicious.data.UsuarioRepository
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

class EditarPerfilActivity : AppCompatActivity() {

    private lateinit var imgOutEditPer: ImageView
    private lateinit var btnGuardarCambios: Button
    private lateinit var etNombreActualizado: EditText
    private lateinit var etNumCel: EditText
    private lateinit var tvCorreoLectura: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_editar_perfil)

        imgOutEditPer       = findViewById(R.id.img_out_edit_per)
        btnGuardarCambios   = findViewById(R.id.btn_guardar_cambios)
        etNombreActualizado = findViewById(R.id.tv_nombre_actualizado)
        etNumCel            = findViewById(R.id.tv_num_cel)
        tvCorreoLectura     = findViewById(R.id.tv_correo_lectura)

        // Cargar los datos del usuario
        lifecycleScope.launch {
            val usuario = UsuarioRepository.obtenerUsuarioActual()
            runOnUiThread {
                if (usuario != null) {

                    etNombreActualizado.setText("${usuario.nombres} ${usuario.apellidos}")
                    etNumCel.setText(usuario.telefono ?: "")
                    //Mostrar correo solo lectura sin opcion de modificar
                    tvCorreoLectura.text = usuario.correo ?: "Sin correo"
                }
            }
        }


        imgOutEditPer.setOnClickListener {
            finish()
        }

        // Guardar cambios realizados
        btnGuardarCambios.setOnClickListener {
            val nombreCompleto = etNombreActualizado.text.toString().trim()
            val telefono       = etNumCel.text.toString().trim()


            if (nombreCompleto.isEmpty()) {
                etNombreActualizado.error = "El nombre no puede estar vacío"
                return@setOnClickListener
            }
            if (telefono.isEmpty()) {
                etNumCel.error = "El teléfono no puede estar vacío"
                return@setOnClickListener
            }


            val partes    = nombreCompleto.split(" ")
            val nombres   = partes.firstOrNull() ?: nombreCompleto
            val apellidos = partes.drop(1).joinToString(" ")

            lifecycleScope.launch {
                val userId = SupabaseClient.client.auth.currentUserOrNull()?.id

                if (userId == null) {
                    runOnUiThread {
                        Toast.makeText(this@EditarPerfilActivity,
                            "Error: sesión no encontrada", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                UsuarioRepository.actualizarPerfil(
                    id        = userId,
                    nombres   = nombres,
                    apellidos = apellidos,
                    telefono  = telefono
                )

                runOnUiThread {
                    Toast.makeText(this@EditarPerfilActivity,
                        "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}