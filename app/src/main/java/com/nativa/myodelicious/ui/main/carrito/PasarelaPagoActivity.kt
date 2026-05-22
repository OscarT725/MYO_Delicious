package com.nativa.myodelicious.ui.main.carrito

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.nativa.myodelicious.R

class PasarelaPagoActivity : AppCompatActivity() {

    private lateinit var img_OutPas: ImageView
    private lateinit var btn_ContPagr: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pasarela_pago)

        img_OutPas = findViewById(R.id.img_out_pasa)
        img_OutPas.setOnClickListener {
            // Cerramos esta actividad para volver al fragmento anterior (CarritoFragment) en la MainActivity
            finish()
        }

        btn_ContPagr = findViewById(R.id.btn_cont_pag)
        btn_ContPagr.setOnClickListener {
            // Ir a la pantalla de pago exitoso
            startActivity(Intent(this, PagoExitosoActivity::class.java))
            finish()
        }
    }
}
