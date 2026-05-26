package com.nativa.myodelicious.ui.main.usuario

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.nativa.myodelicious.R

class CrearDireccionActivity : AppCompatActivity() {

    private lateinit var img_SalirCreaDir: ImageView
    private lateinit var btn_Guardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_direccion)

        img_SalirCreaDir = findViewById(R.id.img_out_crea_dir)
        img_SalirCreaDir.setOnClickListener {
            finish()
        }

        btn_Guardar = findViewById(R.id.btn_guardar_cambios)
        btn_Guardar.setOnClickListener {
            finish()
        }
    }
}
