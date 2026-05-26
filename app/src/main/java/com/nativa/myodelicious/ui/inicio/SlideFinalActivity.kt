package com.nativa.myodelicious.ui.inicio

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.nativa.myodelicious.R
import com.nativa.myodelicious.ui.MainActivity

class SlideFinalActivity : AppCompatActivity() {

    private lateinit var btnComenzar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_slide_final)

        btnComenzar = findViewById(R.id.btn_comenzar)
        btnComenzar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("ES_INVITADO", true)
            }
            startActivity(intent)
            finish()
        }
    }
}