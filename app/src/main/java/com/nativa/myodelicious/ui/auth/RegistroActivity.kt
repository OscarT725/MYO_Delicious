package com.nativa.myodelicious.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.nativa.myodelicious.R

class RegistroActivity : AppCompatActivity() {

    private lateinit var img_Regresar: ImageView

    private lateinit var btnCrearCuenta: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_registro)
        //Manejo del scroll adaptable por teclado

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
        img_Regresar.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnCrearCuenta = findViewById(R.id.btn_crear_cuenta)
        btnCrearCuenta.setOnClickListener {
            startActivity(Intent(this, CuentaCreadaActivity::class.java))
            finish()
        }

    }
}