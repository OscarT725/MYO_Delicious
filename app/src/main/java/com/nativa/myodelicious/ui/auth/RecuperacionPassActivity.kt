package com.nativa.myodelicious.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nativa.myodelicious.R

class RecuperacionPassActivity : AppCompatActivity() {

    private lateinit var btnSolicitaCorreo: Button

    private lateinit var img_Regresar1: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recuperacion_pass)

        btnSolicitaCorreo = findViewById(R.id.btn_solicita_co)
        btnSolicitaCorreo.setOnClickListener {
            startActivity(Intent(this, RecupPassOkActivity::class.java))
            finish()
        }

        img_Regresar1 = findViewById(R.id.img_regre1)
        img_Regresar1.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }
}