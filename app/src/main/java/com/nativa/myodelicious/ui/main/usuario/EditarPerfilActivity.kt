package com.nativa.myodelicious.ui.main.usuario

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.nativa.myodelicious.R
import com.nativa.myodelicious.SupabaseClient
import com.nativa.myodelicious.data.UsuarioRepository
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch
import java.io.File

class EditarPerfilActivity : AppCompatActivity() {

    private var uriFotoSeleccionada: Uri? = null
    private lateinit var ivEditarFoto: ImageView
    private lateinit var tvUserFoto: TextView
    private lateinit var archivoFotoTemp: File
    private lateinit var imgOutEditPer: ImageView
    private lateinit var btnGuardarCambios: Button
    private lateinit var etNombreActualizado: EditText
    private lateinit var etNumCel: EditText
    private lateinit var tvCorreoLectura: TextView

    private val lanzadorPermisoCamara =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
            if (concedido) {
                abrirCamara()
            } else {
                Toast.makeText(
                    this,
                    "Se necesita el permiso de la camara",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private val lanzadorCamara =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { exito ->
            if (exito) {
                uriFotoSeleccionada = Uri.fromFile(archivoFotoTemp)
                ivEditarFoto.load(uriFotoSeleccionada) {
                    transformations(CircleCropTransformation())
                }
            }
        }

    private val lanzadorGaleria =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                uriFotoSeleccionada = uri
                ivEditarFoto.load(uriFotoSeleccionada) {
                    transformations(CircleCropTransformation())
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_editar_perfil)

        ivEditarFoto = findViewById(R.id.img_usuario)
        imgOutEditPer = findViewById(R.id.img_out_edit_per)
        btnGuardarCambios = findViewById(R.id.btn_guardar_cambios)
        etNombreActualizado = findViewById(R.id.tv_nombre_actualizado)
        etNumCel = findViewById(R.id.tv_num_cel)
        tvCorreoLectura = findViewById(R.id.tv_correo_lectura)
        tvUserFoto = findViewById(R.id.tv_subir_foto)

        // Cargar los datos del usuario
        lifecycleScope.launch {
            val usuario = UsuarioRepository.obtenerUsuarioActual()
            if (usuario != null) {
                runOnUiThread {
                    etNombreActualizado.setText("${usuario.nombres} ${usuario.apellidos}")
                    etNumCel.setText(usuario.telefono ?: "")
                    tvCorreoLectura.text = usuario.correo ?: "Sin correo"

                    if (!usuario.foto_url.isNullOrEmpty()) {
                        ivEditarFoto.load(usuario.foto_url) {
                            transformations(CircleCropTransformation())
                            placeholder(R.drawable.logo2)
                            error(R.drawable.logo2)
                        }
                    }
                }
            }
        }

        tvUserFoto.setOnClickListener {
            mostrarOpcionesFoto()
        }

        imgOutEditPer.setOnClickListener {
            finish()
        }

        btnGuardarCambios.setOnClickListener {
            guardarCambios()
        }
    }

    private fun mostrarOpcionesFoto() {
        val opciones = arrayOf("Tomar foto", "Seleccionar foto de la galeria")
        AlertDialog.Builder(this)
            .setTitle("Foto de perfil")
            .setItems(opciones) { _, indice ->
                when (indice) {
                    0 -> verificarPermisoCamara()
                    1 -> lanzadorGaleria.launch("image/*")
                }
            }
            .show()
    }

    private fun verificarPermisoCamara() {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                abrirCamara()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                AlertDialog.Builder(this)
                    .setTitle("Permiso necesario")
                    .setMessage("Se necesita el permiso para acceder a la camara")
                    .setPositiveButton("Aceptar") { _, _ ->
                        lanzadorPermisoCamara.launch(Manifest.permission.CAMERA)
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
            else -> {
                lanzadorPermisoCamara.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun abrirCamara() {
        val carpeta = File(cacheDir, "images")
        carpeta.mkdirs()

        archivoFotoTemp = File(carpeta, "foto_perfil_temp.jpg")

        val uri = FileProvider.getUriForFile(
            this,
            "$packageName.provider",
            archivoFotoTemp
        )
        lanzadorCamara.launch(uri)
    }

    private fun guardarCambios() {
        val nombreCompleto = etNombreActualizado.text.toString().trim()
        val telefono = etNumCel.text.toString().trim()

        if (nombreCompleto.isEmpty()) {
            etNombreActualizado.error = "El nombre no puede estar vacío"
            return
        }
        if (telefono.isEmpty()) {
            etNumCel.error = "El teléfono no puede estar vacío"
            return
        }

        val partes = nombreCompleto.split(" ")
        val nombres = partes.firstOrNull() ?: nombreCompleto
        val apellidos = if (partes.size > 1) partes.drop(1).joinToString(" ") else ""

        lifecycleScope.launch {
            try {
                val userId = SupabaseClient.client.auth.currentUserOrNull()?.id
                if (userId == null) {
                    runOnUiThread {
                        Toast.makeText(this@EditarPerfilActivity, "Error: sesión no encontrada", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                var fotoUrl: String? = null
                if (uriFotoSeleccionada != null) {
                    fotoUrl = UsuarioRepository.subirFotoPerfil(this@EditarPerfilActivity, uriFotoSeleccionada!!)
                    Log.d("DEBUG_CAMARA", "fotoUrl retornada: $fotoUrl")
                }

                UsuarioRepository.actualizarPerfil(
                    id = userId,
                    nombres = nombres,
                    apellidos = apellidos,
                    telefono = telefono,
                    foto_url = fotoUrl
                )

                runOnUiThread {
                    Toast.makeText(this@EditarPerfilActivity, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                Log.e("EditarPerfilActivity", "Error al actualizar perfil", e)
                runOnUiThread {
                    Toast.makeText(this@EditarPerfilActivity, "Error al actualizar el perfil", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
