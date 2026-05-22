package com.nativa.myodelicious.ui.inicio

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nativa.myodelicious.R
import com.nativa.myodelicious.R.id.btn_omitir
import com.nativa.myodelicious.ui.MainActivity
import com.nativa.myodelicious.ui.auth.LoginActivity


class SlideInicialActivity : AppCompatActivity() {

    private lateinit var btnOmitir: Button
    private lateinit var btnSiguiente1: Button

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_slide_inicial)

    btnOmitir = findViewById(btn_omitir)
        btnOmitir.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

    btnSiguiente1 = findViewById(R.id.btn_siguiente1)
        btnSiguiente1.setOnClickListener {
            startActivity(Intent(this, SlideSecundarioActivity::class.java))
        }

    }
}