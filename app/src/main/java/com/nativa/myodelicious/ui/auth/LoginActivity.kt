package com.nativa.myodelicious.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nativa.myodelicious.R
import com.nativa.myodelicious.ui.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var tvCrear_Cuenta: TextView
    private lateinit var tvRecuperar_Contraseña: TextView
    private lateinit var btnIniciarSesion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        tvCrear_Cuenta = findViewById(R.id.tv_crear_cuenta)
        tvCrear_Cuenta.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
            finish()
        }

        tvRecuperar_Contraseña = findViewById(R.id.tv_olvidaste)
        tvRecuperar_Contraseña.setOnClickListener {
            startActivity(Intent(this, RecuperacionPassActivity::class.java))
            finish()
        }

        btnIniciarSesion = findViewById(R.id.btn_iniciar)
        btnIniciarSesion.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }



    }
}