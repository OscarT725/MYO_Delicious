package com.nativa.myodelicious.ui.inicio

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nativa.myodelicious.R
import com.nativa.myodelicious.R.id
import com.nativa.myodelicious.R.id.btn_siguiente2
import com.nativa.myodelicious.ui.MainActivity
import com.nativa.myodelicious.ui.auth.LoginActivity

class SlideSecundarioActivity : AppCompatActivity() {

    private lateinit var btnOmitir1: Button
    private lateinit var btnSiguiente2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_slide_secundario)

    btnOmitir1 = findViewById(id.btn_omitir1)
        btnOmitir1.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

    btnSiguiente2 = findViewById(btn_siguiente2)
        btnSiguiente2.setOnClickListener {
            startActivity(Intent(this, SlideFinalActivity::class.java))
        }


    }
}