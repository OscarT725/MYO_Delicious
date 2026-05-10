package com.nativa.myodelicious.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nativa.myodelicious.R
import com.nativa.myodelicious.ui.MainActivity

class RecupPassOkActivity : AppCompatActivity() {

    private lateinit var btnVolverInicio: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recup_pass_ok)

        btnVolverInicio = findViewById(R.id.btn_volv_inicio)
        btnVolverInicio.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }
}