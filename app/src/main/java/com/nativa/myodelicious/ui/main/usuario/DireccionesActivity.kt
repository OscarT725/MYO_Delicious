package com.nativa.myodelicious.ui.main.usuario

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nativa.myodelicious.R

class DireccionesActivity : AppCompatActivity() {

    private lateinit var img_SalirCrea_dir: ImageView
    private lateinit var img_Agrega_dir: ImageView
    private lateinit var rvDirecciones: RecyclerView

    private val listaDirecciones = listOf(
        Direcciones("Apartamento", "calle 13 # 30 - 163", "Ciudad Verde", "n/a", true),
        Direcciones("Casa", "calle 48x#5a-22sur", "Diana T.", "n/a", false),
        Direcciones("Oficina", "calle 21#79b-19", "Montevideo", "n/a", false),
        Direcciones("Taller", "calle 15#34-12", "Kennedy", "n/a", false),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_direcciones)


        img_SalirCrea_dir = findViewById(R.id.img_out_crea_dir)
        img_SalirCrea_dir.setOnClickListener {
            finish()
        }

        img_Agrega_dir = findViewById(R.id.img_agre_direc)
        img_Agrega_dir.setOnClickListener {
            startActivity(Intent(this, CrearDireccionActivity::class.java))
        }


        rvDirecciones = findViewById(R.id.rv_direcciones)
        rvDirecciones.layoutManager = LinearLayoutManager(this)
        rvDirecciones.adapter = DirecAdapter(listaDirecciones)
    }
}
