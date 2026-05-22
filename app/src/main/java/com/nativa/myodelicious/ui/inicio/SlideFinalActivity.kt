package com.nativa.myodelicious.ui.inicio

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nativa.myodelicious.R
import com.nativa.myodelicious.R.id.btn_comenzar
import com.nativa.myodelicious.ui.MainActivity
import com.nativa.myodelicious.ui.auth.LoginActivity

class SlideFinalActivity : AppCompatActivity() {

    private lateinit var btnComenzar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_slide_final)

    btnComenzar = findViewById(btn_comenzar)
        btnComenzar.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }
}