package com.nativa.myodelicious.ui.main.usuario

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nativa.myodelicious.R
import com.nativa.myodelicious.ui.auth.LoginActivity

class ConfiguracionActivity : AppCompatActivity() {

    private lateinit var img_OutConfig: ImageView

    private lateinit var btn_ElimCuent: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_configuracion)

        img_OutConfig = findViewById(R.id.img_out_conf)
        img_OutConfig.setOnClickListener {
            startActivity(Intent(this, PerfilFragment::class.java))
        }

        btn_ElimCuent = findViewById(R.id.btn_elim_cuent)
        btn_ElimCuent.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}