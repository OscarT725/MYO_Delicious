package com.nativa.myodelicious.ui.main.usuario

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.nativa.myodelicious.R

class EditarPerfilActivity : AppCompatActivity() {

    private lateinit var imgOutEditPer: ImageView
    private lateinit var btnGuardarCambios: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_editar_perfil)

        // Botón para regresar (Flecha) - El ID en el XML es img_out_edit_per
        imgOutEditPer = findViewById(R.id.img_out_edit_per)
        imgOutEditPer.setOnClickListener {
            // Cerramos esta actividad para volver al fragmento anterior en MainActivity
            finish()
        }

        // Botón para guardar cambios
        btnGuardarCambios = findViewById(R.id.btn_guardar_cambios)
        btnGuardarCambios.setOnClickListener {
            // Lógica de guardado...
            // Cerramos para volver
            finish()
        }
    }
}
