package com.nativa.myodelicious.ui.main.usuario

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nativa.myodelicious.R

class AyudaActivity : AppCompatActivity() {

    private lateinit var imgOutAyuda: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ayuda)

        imgOutAyuda = findViewById(R.id.img_out_ayuda)
        imgOutAyuda.setOnClickListener {

            finish()
        }
    }
}